package com.sangle.crud.service.mapper;

import com.sangle.crud.domain.Pricing;
import com.sangle.crud.service.dto.PricingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pricing} and its DTO {@link PricingDTO}.
 */
@Mapper(componentModel = "spring")
public interface PricingMapper extends EntityMapper<PricingDTO, Pricing> {}
