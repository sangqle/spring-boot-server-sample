package com.sangle.crud.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricingMapperTest {

    private PricingMapper pricingMapper;

    @BeforeEach
    public void setUp() {
        pricingMapper = new PricingMapperImpl();
    }
}
