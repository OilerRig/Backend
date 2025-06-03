package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.UserEntity;
import com.oilerrig.backend.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, OrderMapperImpl.class, ProductMapperImpl.class, OrderItemMapperImpl.class, VendorMapperImpl.class})
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void testEntityToDomain() {
        UserEntity entity = new UserEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setName("Yusuf");
        entity.setEmail("yusuf@example.com");
        entity.setRole("CLIENT");

        User domain = mapper.toDomain(entity);
        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getName()).isEqualTo("Yusuf");
        assertThat(domain.getRole().name()).isEqualTo("CLIENT");
    }

    @Test
    void testDomainToEntity() {
        User domain = new User();
        UUID id = UUID.randomUUID();
        domain.setId(id);
        domain.setName("Yusuf");
        domain.setEmail("yusuf@example.com");
        domain.setRole(User.UserRole.CLIENT);

        UserEntity entity = mapper.toEntity(domain);
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Yusuf");
        assertThat(entity.getRole()).isEqualTo("CLIENT");
    }
}
