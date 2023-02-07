package com.sangle.sample.repository;

import com.sangle.sample.domain.User;
import java.util.UUID;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Cassandra reactive repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {}
