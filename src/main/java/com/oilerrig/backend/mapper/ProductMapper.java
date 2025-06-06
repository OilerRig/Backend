package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = VendorMapper.class)
public interface ProductMapper {
    @Mapping(source = "vendor", target = "vendor")
    Product toDomain(ProductEntity entity);

    @Mapping(source = "vendor.name", target = "vendorName")
    ProductDto toDto(ProductEntity entity);
}
