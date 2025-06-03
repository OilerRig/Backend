package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, OrderMapperImpl.class, ProductMapperImpl.class, OrderItemMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = {ProductMapperImpl.class})
class ProductMapperTest {
    @Autowired
    private ProductMapper mapper;

    @Test
    void testEntityToDomain() {
        ProductEntity entity = new ProductEntity();
        entity.setName("Widget");
        entity.setPrice(9.99);
        entity.setStock(10);

        Product domain = mapper.toDomain(entity);
        assertThat(domain.getName()).isEqualTo("Widget");
        assertThat(domain.getPrice()).isEqualTo(9.99);
        assertThat(domain.getStock()).isEqualTo(10);
    }


}
