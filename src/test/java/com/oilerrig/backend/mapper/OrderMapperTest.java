package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.data.entity.UserEntity;
import com.oilerrig.backend.domain.Order;
import com.oilerrig.backend.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import({OrderMapperImpl.class, OrderItemMapperImpl.class, UserMapperImpl.class, ProductMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = OrderMapperImpl.class)
class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    void testEntityToDomain_basicFields() {
        OrderEntity entity = new OrderEntity();
        entity.setStatus(Order.OrderStatus.COMPLETED);
        entity.setCreatedAt(OffsetDateTime.now());

        Order domain = mapper.toDomain(entity);

        assertThat(domain.getStatus()).isEqualTo(Order.OrderStatus.COMPLETED);
        assertThat(domain.getCreatedAt()).isEqualTo(entity.getCreatedAt());
    }

    @Test
    void testPlaceOrderDtoToEntity_valid() {
        PlaceOrderRequestDto dto = new PlaceOrderRequestDto();
        dto.setOrderItemProductIds(Arrays.asList(1, 2));
        dto.setOrderItemQuantities(Arrays.asList(5, 10));

        OrderEntity entity = mapper.placeOrderDtoToEntity(dto);

        assertThat(entity.getOrderItems()).hasSize(2);
        assertThat(entity.getOrderItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    void testPlaceOrderDtoToEntity_invalidSizes() {
        PlaceOrderRequestDto dto = new PlaceOrderRequestDto();
        dto.setOrderItemProductIds(Collections.singletonList(1));
        dto.setOrderItemQuantities(Arrays.asList(5, 10)); // Mismatch

        assertThrows(IllegalArgumentException.class, () -> mapper.placeOrderDtoToEntity(dto));
    }

    @Test
    void testPartialUpdate_ignoresNulls() {
        PlaceOrderRequestDto dto = new PlaceOrderRequestDto();
        // Intentionally empty dto to test nullValuePropertyMappingStrategy
        OrderEntity target = new OrderEntity();
        target.setStatus(Order.OrderStatus.IN_PROGRESS);

        OrderEntity updated = mapper.partialUpdate(dto, target);

        assertThat(updated.getStatus()).isEqualTo(Order.OrderStatus.IN_PROGRESS);
    }

    @Test
    void testOrderItemsToOrderItemQuantities() {
        ProductEntity product = new ProductEntity();
        product.setId(1);

        OrderItemEntity item = new OrderItemEntity();
        item.setQuantity(7);
        item.setProduct(product);

        List<Integer> result = mapper.orderItemsToOrderItemQuantities(Collections.singletonList(item));

        assertThat(result).containsExactly(7);
    }

    @Test
    void testToDto_basicFields() {
        OrderEntity entity = new OrderEntity();
        UserEntity user = new UserEntity();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setRole(User.UserRole.ADMIN);

        entity.setUser(user);

        OrderDto dto = mapper.toDto(entity);

        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getUserRole()).isEqualTo(User.UserRole.ADMIN.name());
    }
}
