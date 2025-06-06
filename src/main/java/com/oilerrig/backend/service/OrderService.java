package com.oilerrig.backend.service;

import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.oilerrig.backend.config.ServiceBusConfig;
import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.repository.OrderRepository;
import com.oilerrig.backend.data.saga.Saga;
import com.oilerrig.backend.data.saga.SagaStep;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ServiceBusTemplate serviceBusTemplate;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(ServiceBusTemplate serviceBusTemplate,
                        OrderMapper orderMapper,
                        OrderRepository orderRepository) {
        this.serviceBusTemplate = serviceBusTemplate;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
    }

    // add a given order to the database and to message queue
    public OrderDto addOrder(PlaceOrderRequestDto dto) {
        // create and persist order
        OrderEntity entity = orderMapper.placeOrderDtoToEntity(dto); // TODO CHECK IF CONVERSION IS LOSSY OR NEED MORE DATA
        orderRepository.save(entity);

        // create saga instance
        Saga saga = new Saga(
            entity.getOrderItems().stream()
                    .map(item -> new SagaStep(item.getProduct().getId(), item.getQuantity()))
                    .collect(Collectors.toList())
        );

        // pass into mq
        serviceBusTemplate.sendAsync(
                ServiceBusConfig.SAGA_QUEUE,
                MessageBuilder.withPayload(saga).build()
        ).subscribe();

        // return entity to dto
        return orderMapper.toDto(entity);
    }

    // get order via identifiers
    public OrderDto getOrder(UUID orderId) {
        Optional<OrderEntity> optional = orderRepository.findById(orderId);

        if (optional.isPresent()) {
            return orderMapper.toDto(optional.get());
        }
        else {
            throw new NotFoundException("Order with id " + orderId);
        }
    }

}
