package com.sangle.crud.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangle.crud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pricing.class);
        Pricing pricing1 = new Pricing();
        pricing1.setId(1L);
        Pricing pricing2 = new Pricing();
        pricing2.setId(pricing1.getId());
        assertThat(pricing1).isEqualTo(pricing2);
        pricing2.setId(2L);
        assertThat(pricing1).isNotEqualTo(pricing2);
        pricing1.setId(null);
        assertThat(pricing1).isNotEqualTo(pricing2);
    }
}
