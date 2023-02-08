package com.sangle.crud.repository;

import com.sangle.crud.domain.UserSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {}
