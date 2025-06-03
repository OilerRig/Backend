package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, OrderMapperImpl.class, ProductMapperImpl.class, OrderItemMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = {OrderMapperImpl.class})
class OrderMapperTest {
    @Autowired
    private OrderMapper mapper;

    @Test
    void testEntityToDomain() {
        OrderEntity entity = new OrderEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setStatus("COMPLETED");

        Order domain = mapper.toDomain(entity);
        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getStatus().name()).isEqualTo("COMPLETED");
    }

    @Test
    void testDomainToEntity() {
        Order domain = new Order();
        UUID id = UUID.randomUUID();
        domain.setId(id);
        domain.setStatus(Order.OrderStatus.COMPLETED);

        OrderEntity entity = mapper.toEntity(domain);
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getStatus()).isEqualTo("COMPLETED");
    }
}
