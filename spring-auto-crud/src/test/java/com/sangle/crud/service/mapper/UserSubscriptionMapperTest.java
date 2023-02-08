package com.sangle.crud.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class UserSubscriptionMapperTest {

    private UserSubscriptionMapper userSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        userSubscriptionMapper = new UserSubscriptionMapperImpl();
    }
}
