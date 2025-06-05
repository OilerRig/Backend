package com.oilerrig.backend.service;

import com.oilerrig.backend.config.RabbitConfig;
import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.dto.SagaDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.SagaEntity;
import com.oilerrig.backend.data.repository.OrderRepository;
import com.oilerrig.backend.data.saga.SagaMetadata;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.mapper.OrderMapper;
import com.oilerrig.backend.mapper.SagaMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final SagaMapper sagaMapper;

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate,
                        OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        SagaMapper sagaMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.sagaMapper = sagaMapper;
    }

    // add a given order to the database and to message queue
    public OrderDto addOrder(PlaceOrderRequestDto dto) {
        // create and persist order
        OrderEntity entity = orderMapper.placeOrderDtoToEntity(dto); // TODO CHECK IF CONVERSION IS LOSSY OR NEED MORE DATA
        orderRepository.save(entity);

        // create saga and persist it
        SagaEntity sagaEntity = new SagaEntity();


        // pass into mq
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_SAGA_EXCHANGE,
                RabbitConfig.ORDER_SAGA_ROUTING_KEY,
                sagaMapper.toDto(sagaEntity)
        );

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
