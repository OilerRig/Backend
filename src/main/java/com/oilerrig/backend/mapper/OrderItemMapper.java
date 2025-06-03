package com.oilerrig.backend.mapper;

import com.oilerrig.backend.domain.OrderItem;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderItemMapper {
    OrderItem toDomain(OrderItemEntity entity);
}
