package com.oilerrig.backend.mapper;

import com.oilerrig.backend.domain.User;
import com.oilerrig.backend.data.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role")
    User toDomain(UserEntity entity);

    @Mapping(source = "role", target = "role")
    UserEntity toEntity(User domain);
}
