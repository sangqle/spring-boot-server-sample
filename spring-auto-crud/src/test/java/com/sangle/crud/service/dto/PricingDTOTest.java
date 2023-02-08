package com.sangle.crud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangle.crud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PricingDTO.class);
        PricingDTO pricingDTO1 = new PricingDTO();
        pricingDTO1.setId(1L);
        PricingDTO pricingDTO2 = new PricingDTO();
        assertThat(pricingDTO1).isNotEqualTo(pricingDTO2);
        pricingDTO2.setId(pricingDTO1.getId());
        assertThat(pricingDTO1).isEqualTo(pricingDTO2);
        pricingDTO2.setId(2L);
        assertThat(pricingDTO1).isNotEqualTo(pricingDTO2);
        pricingDTO1.setId(null);
        assertThat(pricingDTO1).isNotEqualTo(pricingDTO2);
    }
}
