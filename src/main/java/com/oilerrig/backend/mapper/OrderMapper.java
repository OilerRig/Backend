package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.dto.OrderEntityDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.domain.Order;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(source = "status", target = "status")
    Order toDomain(OrderEntity entity);

    @Mapping(target = "orderItems", expression = "java(mapToOrderItems(dto.getOrderItemProductIds(), dto.getOrderItemQuantities()))")
    OrderEntity placeOrderDtoToEntity(PlaceOrderRequestDto dto);

    @InheritConfiguration(name = "placeOrderDtoToEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderEntity partialUpdate(PlaceOrderRequestDto dto, @MappingTarget OrderEntity orderEntity);

    default List<Integer> orderItemsToOrderItemQuantities(List<OrderItemEntity> orderItems) {
        return orderItems.stream().map(OrderItemEntity::getQuantity).collect(Collectors.toList());
    }

    default List<OrderItemEntity> mapToOrderItems(List<Integer> productIds, List<Integer> quantities) {
        if (productIds == null || quantities == null || productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Product and quantity lists must be non-null and of equal size");
        }

        List<OrderItemEntity> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            OrderItemEntity item = new OrderItemEntity();
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(productIds.get(i));
            item.setProduct(productEntity);
            item.setQuantity(quantities.get(i));
            items.add(item);
        }
        return items;
    }

    @Mapping(source = "user.role", target = "userRole")
    @Mapping(source = "user.id", target = "userId")
    OrderEntityDto toDto(OrderEntity orderEntity);

}