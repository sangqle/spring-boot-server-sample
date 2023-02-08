package com.sangle.crud.service.mapper;

import com.sangle.crud.domain.UserSubscription;
import com.sangle.crud.service.dto.UserSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSubscription} and its DTO {@link UserSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper extends EntityMapper<UserSubscriptionDTO, UserSubscription> {}
