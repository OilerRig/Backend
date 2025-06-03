package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.OrderItemEntity;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, OrderMapperImpl.class, ProductMapperImpl.class, OrderItemMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = {OrderItemMapperImpl.class})
class OrderItemMapperTest {
    @Autowired
    private OrderItemMapper mapper;

    @Test
    void testEntityToDomain() {
        OrderItemEntity entity = new OrderItemEntity();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("test");
        entity.setProduct(productEntity);
        entity.setQuantity(5);

        OrderItem domain = mapper.toDomain(entity);
        assertThat(domain.getProduct().getName()).isEqualTo("test");
        assertThat(domain.getQuantity()).isEqualTo(5);
    }
}
