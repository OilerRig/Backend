package com.oilerrig.backend.service;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.UserEntity;
import com.oilerrig.backend.data.repository.UserRepository;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.mapper.OrderMapper;
import com.oilerrig.backend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public UserService(UserMapper userMapper, UserRepository orderRepository, OrderMapper orderMapper) {
        this.userMapper = userMapper;
        this.userRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    public List<OrderDto> getUserOrders(UUID id) {
        Optional<UserEntity> optional = userRepository.findById(id);

        if (optional.isPresent()) {
            return optional.get().getOrders().stream().map(orderMapper::toDto).collect(Collectors.toList());
        }
        else {
            throw new NotFoundException("User with id " + id);
        }
    }
}
