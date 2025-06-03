package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "status", target = "status")
    Order toDomain(OrderEntity entity);
}