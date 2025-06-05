package com.oilerrig.backend.mapper;

import com.oilerrig.backend.data.dto.SagaDto;
import com.oilerrig.backend.data.entity.SagaEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SagaMapper {
    SagaEntity toEntity(SagaDto sagaDto);

    SagaDto toDto(SagaEntity sagaEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SagaEntity partialUpdate(SagaDto sagaDto, @MappingTarget SagaEntity sagaEntity);
}