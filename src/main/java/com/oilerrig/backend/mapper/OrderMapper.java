package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.domain.Order;
import com.oilerrig.backend.domain.OrderItem;
import com.oilerrig.backend.domain.Product;
import com.oilerrig.backend.exception.ValidityException;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order toDomain(OrderEntity entity);
    OrderDto toDto(OrderEntity orderEntity);
    OrderEntity toEntity(Order domain);

    default List<OrderItem> placeOrderRequestToOrderItems(PlaceOrderRequestDto dto) {
        List<Integer> productIds = dto.getOrderItemProductIds();
        List<Integer> quantities = dto.getOrderItemQuantities();
        if(
            productIds == null
            || quantities == null
            || productIds.isEmpty()
            || quantities.isEmpty()
            || productIds.size() != quantities.size()
        ) throw new ValidityException("Invalid Order Request Data"); // throw for invalid inputs

        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            OrderItem item = new OrderItem();
            Product product = new Product();
            product.setId(productIds.get(i));
            item.setProduct(product);
            item.setQuantity(quantities.get(i));
            items.add(item);
        }
        return items;
    }


}