package com.sangle.crud.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangle.crud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSubscription.class);
        UserSubscription userSubscription1 = new UserSubscription();
        userSubscription1.setId(1L);
        UserSubscription userSubscription2 = new UserSubscription();
        userSubscription2.setId(userSubscription1.getId());
        assertThat(userSubscription1).isEqualTo(userSubscription2);
        userSubscription2.setId(2L);
        assertThat(userSubscription1).isNotEqualTo(userSubscription2);
        userSubscription1.setId(null);
        assertThat(userSubscription1).isNotEqualTo(userSubscription2);
    }
}
