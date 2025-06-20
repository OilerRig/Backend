package com.oilerrig.backend.service;

import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.oilerrig.backend.config.ServiceBusConfig;
import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import com.oilerrig.backend.data.repository.OrderItemRepository;
import com.oilerrig.backend.data.repository.OrderRepository;
import com.oilerrig.backend.data.repository.ProductRepository;
import com.oilerrig.backend.data.saga.SagaInstance;
import com.oilerrig.backend.data.saga.SagaSerializationUtils;
import com.oilerrig.backend.domain.Order;
import com.oilerrig.backend.domain.OrderItem;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.exception.OrderCreationException;
import com.oilerrig.backend.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final ServiceBusTemplate serviceBusTemplate;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(ServiceBusTemplate serviceBusTemplate,
                        OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.serviceBusTemplate = serviceBusTemplate;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public boolean canAccessOrder(UUID orderId, Authentication auth) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Requested Order Doesn't Exist"));

        return
                order.getGuest() // allow access to guest orders
                || auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals("ROLE_ADMIN"))// allow access to admins
                || (
                        auth instanceof JwtAuthenticationToken
                        && orderRepository.findByIdAndAuth0_id(orderId, ((JwtAuthenticationToken) auth).getToken().getSubject()).isPresent()
                ) // order is owned by current user
                ;
    }

    // add a given order to the database and to message queue
    @Transactional
    public OrderDto addOrder(PlaceOrderRequestDto dto) {
        return this.addOrder(dto, null);
    }

    @Transactional
    public OrderDto addOrder(PlaceOrderRequestDto dto, String auth0_id) {
        // create and persist order
        List<OrderItem> orderItems = orderMapper.placeOrderRequestToOrderItems(dto);
        log.info("Order Recieved with {} items", orderItems.size());

        Order order = new Order();
        order.setCreatedAt(OffsetDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        if (auth0_id == null) {
            order.setGuest(true);
        }
        else {
            order.setAuth0_id(auth0_id);
            order.setGuest(false);
        }

        OrderEntity entity = orderMapper.toEntity(order);
        log.info("Mapped Order to Entity {} -> {}", order, entity);
        entity = orderRepository.save(entity);

        OrderEntity finalEntity = entity;
        orderItems.forEach(oi -> {
            OrderItemEntity oie = new OrderItemEntity();
            oie.setProduct(productRepository.findById(oi.getProduct().getId()).orElseThrow(() -> new OrderCreationException("Product Not Found")));
            oie.setQuantity(oi.getQuantity());
            oie.setOrder(finalEntity);
            oie.setStatus(OrderItem.ItemStatus.PENDING);
            orderItemRepository.save(oie);
        });

        entity.setOrderItems(orderItemRepository.findAllByOrder_Id(entity.getId()));
        entity = orderRepository.findById(entity.getId()).orElseThrow(() -> new OrderCreationException("Order Lost During Creation."));
        log.info("Order added to Database {}", entity);

        // create saga instance
        SagaInstance instance = new SagaInstance(entity);

        // pass into mq
        serviceBusTemplate.sendAsync(
                ServiceBusConfig.SAGA_QUEUE,
                MessageBuilder.withPayload(SagaSerializationUtils.serializeToJsonBytes(instance)).build()
        ).subscribe();

        // return entity to dto
        return orderMapper.toDto(entity);
    }

    // get order via identifiers
    public OrderDto getOrder(UUID orderId) {
        Optional<OrderEntity> optional = orderRepository.findById(orderId);

        if (optional.isPresent()) {
            OrderEntity order = optional.get();
                return orderMapper.toDto(order);
        }
        else {
            throw new NotFoundException("Order with id " + orderId);
        }
    }

    public List<OrderDto> getAllOrdersByUser(String userId) {
        return orderRepository.findAllByAuth0_id(userId).stream().map(orderMapper::toDto).toList();
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

}
