package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.domain.Vendor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, OrderMapperImpl.class, ProductMapperImpl.class, OrderItemMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = {VendorMapperImpl.class})
class VendorMapperTest {
    @Autowired
    private VendorMapper mapper;

    @Test
    void testEntityToDomain() {
        VendorEntity entity = new VendorEntity();
        entity.setId(1);
        entity.setName("Test Vendor");
        entity.setBaseurl("http://vendor.test");

        Vendor domain = mapper.toDomain(entity);
        assertThat(domain.getId()).isEqualTo(1);
        assertThat(domain.getName()).isEqualTo("Test Vendor");
        assertThat(domain.getBaseurl()).isEqualTo("http://vendor.test");
    }

    @Test
    void testDomainToEntity() {
        Vendor domain = new Vendor();
        domain.setId(1);
        domain.setName("Test Vendor");
        domain.setBaseurl("http://vendor.test");

        VendorEntity entity = mapper.toEntity(domain);
        assertThat(entity.getId()).isEqualTo(1);
        assertThat(entity.getName()).isEqualTo("Test Vendor");
        assertThat(entity.getBaseurl()).isEqualTo("http://vendor.test");
    }
}
