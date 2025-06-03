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

import java.time.OffsetDateTime;
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
        entity.setCreatedAt(OffsetDateTime.MIN);
        entity.setStatus("COMPLETED");

        Order domain = mapper.toDomain(entity);
        assertThat(domain.getCreatedAt()).isEqualTo(OffsetDateTime.MIN);
        assertThat(domain.getStatus().name()).isEqualTo("COMPLETED");
    }

}
