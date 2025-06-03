package com.oilerrig.backend.mapper;

import com.oilerrig.backend.domain.OrderItem;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toDomain(OrderItemEntity entity);
    OrderItemEntity toEntity(OrderItem domain);

}
