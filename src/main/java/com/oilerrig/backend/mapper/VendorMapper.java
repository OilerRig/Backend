package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.domain.Vendor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    Vendor toDomain(VendorEntity vendorEntity);
    VendorEntity toEntity(Vendor vendor);

}

