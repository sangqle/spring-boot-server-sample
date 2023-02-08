package com.sangle.crud.repository;

import com.sangle.crud.domain.Pricing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {}
