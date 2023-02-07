package com.sangle.sample.service;

import com.sangle.sample.service.dto.UserDTO;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sangle.sample.domain.User}.
 */
public interface UserService {
    /**
     * Save a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserDTO> save(UserDTO userDTO);

    /**
     * Updates a user.
     *
     * @param userDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserDTO> update(UserDTO userDTO);

    /**
     * Partially updates a user.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserDTO> partialUpdate(UserDTO userDTO);

    /**
     * Get all the users.
     *
     * @return the list of entities.
     */
    Flux<UserDTO> findAll();

    /**
     * Returns the number of users available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserDTO> findOne(UUID id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(UUID id);
}
