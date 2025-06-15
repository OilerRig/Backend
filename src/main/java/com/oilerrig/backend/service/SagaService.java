package com.oilerrig.backend.service;

import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.azure.spring.messaging.servicebus.support.ServiceBusMessageHeaders;
import com.oilerrig.backend.config.ServiceBusConfig;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import com.oilerrig.backend.data.repository.OrderItemRepository;
import com.oilerrig.backend.data.repository.OrderRepository;
import com.oilerrig.backend.data.repository.VendorOrderRepository; // New import
import com.oilerrig.backend.data.saga.SagaCompletedVendorOrder;
import com.oilerrig.backend.data.saga.SagaInstance;
import com.oilerrig.backend.data.saga.SagaOrderItem;
import com.oilerrig.backend.data.saga.SagaSerializationUtils;
import com.oilerrig.backend.domain.Order;
import com.oilerrig.backend.domain.OrderItem;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.exception.VendorApiException;
import com.oilerrig.backend.gateway.dto.VendorCancelOrderDto;
import com.oilerrig.backend.gateway.dto.VendorOrderDto;
import com.oilerrig.backend.gateway.dto.VendorPlaceOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class SagaService {

    private static final Logger log = LoggerFactory.getLogger(SagaService.class);

    private final OrderRepository brokerOrderRepository;
    private final OrderItemRepository brokerOrderItemRepository;
    private final VendorOrderRepository vendorOrderRepository;
    private final ServiceBusTemplate serviceBusTemplate;

    @Autowired
    public SagaService(OrderRepository brokerOrderRepository,
                       OrderItemRepository brokerOrderItemRepository,
                       VendorOrderRepository vendorOrderRepository, ServiceBusTemplate serviceBusTemplate) {
        this.brokerOrderRepository = brokerOrderRepository;
        this.brokerOrderItemRepository = brokerOrderItemRepository;
        this.vendorOrderRepository = vendorOrderRepository;
        this.serviceBusTemplate = serviceBusTemplate;
    }

    @ServiceBusListener(destination = ServiceBusConfig.SAGA_QUEUE)
    public void handleSaga(byte[] sagabytes) {
        SagaInstance saga = SagaSerializationUtils.deserializeFromJsonBytes(sagabytes, SagaInstance.class);

        log.info("Handling Saga: {} for Broker Order: {})", saga.getSagaId(), saga.getBrokerOrderId());

        // 1. check if saga expired
        if (OffsetDateTime.now().isAfter(saga.getExpiresAt())) {
            log.warn("Saga {} for Order {} expired {} minutes ago. Discarding and marking order as FAILED_EXPIRED.",
                    saga.getSagaId(), saga.getBrokerOrderId(), Duration.between(OffsetDateTime.now(), saga.getExpiresAt()).abs().toMinutes());
            updateBrokerOrderStatus(saga.getBrokerOrderId(), Order.OrderStatus.CANCELLED);
            // ack and remove msg if this succeeds
            return;
        }

        OrderEntity brokerOrder = brokerOrderRepository.findById(saga.getBrokerOrderId())
                .orElseThrow(() -> new NotFoundException("Broker order not found for saga: " + saga.getBrokerOrderId()));

        boolean allItemsProcessedSuccessfully = true;
        // deep copy list to ensure we dont modify the original saga.getCompletedVendorOrders
        List<SagaCompletedVendorOrder> currentSuccessfulVendorOrders = new ArrayList<>(saga.getCompletedVendorOrders());

        for (SagaOrderItem sagaItem : saga.getOrderItems()) {
            // check if alrdy processed for idempotency
            boolean itemAlreadyProcessed = saga.getCompletedVendorOrders().stream()
                    .anyMatch(cvo -> cvo.getVendorId() == sagaItem.getVendorId() &&
                            brokerOrderItemRepository.findByOrder_Id_AndProduct_Id(brokerOrder.getId(), sagaItem.getProductId())
                                    .map(OrderItemEntity::getVendorOrderId)
                                    .isPresent());

            if (itemAlreadyProcessed) {
                log.debug("Saga {}: Order item for product {} with vendor {} already processed successfully in a previous attempt. Skipping.",
                        saga.getSagaId(), sagaItem.getProductId(), sagaItem.getVendorId());
                continue; // skip to next one
            }

            try {
                VendorPlaceOrderDto placeOrderCommand = new VendorPlaceOrderDto(sagaItem.getVendorProductId(), sagaItem.getQuantity());
                VendorOrderDto vendorOrderConfirmation = vendorOrderRepository.placeOrder(sagaItem.getVendorId(), placeOrderCommand);

                // record successful vendor order
                SagaCompletedVendorOrder completedOrder = new SagaCompletedVendorOrder(
                        sagaItem.getVendorId(),
                        vendorOrderConfirmation.getId()
                );
                currentSuccessfulVendorOrders.add(completedOrder);
                // update saga instance
                saga.getCompletedVendorOrders().add(completedOrder);

                // update broker order item status
                updateBrokerOrderItemStatus(brokerOrder, sagaItem.getVendorProductId(), sagaItem.getVendorId(), OrderItem.ItemStatus.COMPLETED, vendorOrderConfirmation.getId());

                log.info("Saga {}: Successfully placed order for product {} with vendor {}. Vendor Order ID: {}",
                        saga.getSagaId(), sagaItem.getProductId(), sagaItem.getVendorId(), vendorOrderConfirmation.getId());

            } catch (VendorApiException e) {
                log.error("Saga {}: Failed to place order for product {} with vendor {}: {}",
                        saga.getSagaId(), sagaItem.getProductId(), sagaItem.getVendorId(), e.getMessage());
                allItemsProcessedSuccessfully = false;
                updateBrokerOrderItemStatus(brokerOrder, sagaItem.getProductId(), sagaItem.getVendorId(), OrderItem.ItemStatus.FAILED, null);
                break; // failure, compensate
            } catch (Exception e) {
                log.error("Saga {}: Unexpected error during order placement for product {} with vendor {}: {}",
                        saga.getSagaId(), sagaItem.getProductId(), sagaItem.getVendorId(), e.getMessage(), e);
                allItemsProcessedSuccessfully = false;
                updateBrokerOrderItemStatus(brokerOrder, sagaItem.getProductId(), sagaItem.getVendorId(), OrderItem.ItemStatus.FAILED, null);
                break; // failure, compensate
            }
        }

        if (allItemsProcessedSuccessfully) {
            log.info("Saga {}: All items processed successfully. Marking broker order {} as COMPLETED.", saga.getSagaId(), saga.getBrokerOrderId());
            updateBrokerOrderStatus(saga.getBrokerOrderId(), Order.OrderStatus.COMPLETED);
        } else {
            // Saga failed, now initiate compensation
            log.warn("Saga {}: Saga failed. Initiating compensation for {} previously completed vendor orders.",
                    saga.getSagaId(), currentSuccessfulVendorOrders.size());
            compensateSaga(currentSuccessfulVendorOrders);

            updateBrokerOrderStatus(saga.getBrokerOrderId(), Order.OrderStatus.RETRYING);

            HashMap<String, Object> headerMap = new HashMap<>();
            headerMap.put(ServiceBusMessageHeaders.SCHEDULED_ENQUEUE_TIME, OffsetDateTime.now().plusSeconds(10));
            MessageHeaders headers = new MessageHeaders(headerMap);

            serviceBusTemplate.sendAsync(
                    ServiceBusConfig.SAGA_QUEUE,
                    MessageBuilder.createMessage(
                            SagaSerializationUtils.serializeToJsonBytes(saga),
                            headers
                    )
            ).subscribe();

            log.info("Saga processing failed, triggering redelivery for saga {}", saga.getSagaId());
        }
    }

    private void compensateSaga(List<SagaCompletedVendorOrder> completedOrders) {
        for (SagaCompletedVendorOrder completedOrder : completedOrders) {
            try {
                VendorCancelOrderDto cancelCommand = new VendorCancelOrderDto(completedOrder.getVendorOrderId());
                VendorOrderDto cancelConfirmation = vendorOrderRepository.cancelOrder(completedOrder.getVendorId(), cancelCommand);
                log.info("Compensation: Successfully cancelled vendor order {} for vendor {}. Status: {}",
                        completedOrder.getVendorOrderId(), completedOrder.getVendorId(), cancelConfirmation.getStatus());
                 brokerOrderItemRepository.findByVendorOrderId(completedOrder.getVendorOrderId()).ifPresent(item -> {
                    item.setStatus(OrderItem.ItemStatus.CANCELLED);
                    brokerOrderItemRepository.save(item);
                 });
            } catch (VendorApiException e) {
                log.error("Compensation FAILED for vendor order {} with vendor {}: {}",
                        completedOrder.getVendorOrderId(), completedOrder.getVendorId(), e.getMessage());
                // super critical failure case, should be logged and in a prod case sent to a dlq for manual rev
            } catch (Exception e) {
                log.error("Unexpected error during compensation for vendor order {} with vendor {}: {}",
                        completedOrder.getVendorOrderId(), completedOrder.getVendorId(), e.getMessage(), e);
            }
        }
    }

    private void updateBrokerOrderStatus(UUID orderId, Order.OrderStatus status) {
        brokerOrderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            order.setResolvedAt(OffsetDateTime.now());
            brokerOrderRepository.save(order);
            log.info("Broker Order {}: Status updated to {}", orderId, status);
        });
    }

    private void updateBrokerOrderItemStatus(OrderEntity order, Integer productId, Integer vendorId, OrderItem.ItemStatus status, UUID vendorOrderId) {
        order.getOrderItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setStatus(status);
                    if (vendorOrderId != null) {
                        item.setVendorOrderId(vendorOrderId);
                    }
                    brokerOrderItemRepository.save(item);
                    log.info("Broker Order {} Item (Product: {}, Vendor: {}): Status updated to {}{}",
                            order.getId(), productId, vendorId, status, (vendorOrderId != null ? " (Vendor Order ID: " + vendorOrderId + ")" : ""));
                });
    }

    private static class SagaProcessingException extends RuntimeException {
        public SagaProcessingException(String message) {
            super(message);
        }
    }
}