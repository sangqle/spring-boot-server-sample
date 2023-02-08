package com.sangle.crud.service;

import com.sangle.crud.domain.UserSubscription;
import com.sangle.crud.repository.UserSubscriptionRepository;
import com.sangle.crud.service.dto.UserSubscriptionDTO;
import com.sangle.crud.service.mapper.UserSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserSubscription}.
 */
@Service
@Transactional
public class UserSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionService.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository, UserSubscriptionMapper userSubscriptionMapper) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
    }

    /**
     * Save a userSubscription.
     *
     * @param userSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserSubscriptionDTO save(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to save UserSubscription : {}", userSubscriptionDTO);
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription = userSubscriptionRepository.save(userSubscription);
        return userSubscriptionMapper.toDto(userSubscription);
    }

    /**
     * Update a userSubscription.
     *
     * @param userSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserSubscriptionDTO update(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to update UserSubscription : {}", userSubscriptionDTO);
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription = userSubscriptionRepository.save(userSubscription);
        return userSubscriptionMapper.toDto(userSubscription);
    }

    /**
     * Partially update a userSubscription.
     *
     * @param userSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserSubscriptionDTO> partialUpdate(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to partially update UserSubscription : {}", userSubscriptionDTO);

        return userSubscriptionRepository
            .findById(userSubscriptionDTO.getId())
            .map(existingUserSubscription -> {
                userSubscriptionMapper.partialUpdate(existingUserSubscription, userSubscriptionDTO);

                return existingUserSubscription;
            })
            .map(userSubscriptionRepository::save)
            .map(userSubscriptionMapper::toDto);
    }

    /**
     * Get all the userSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSubscriptions");
        return userSubscriptionRepository.findAll(pageable).map(userSubscriptionMapper::toDto);
    }

    /**
     * Get one userSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get UserSubscription : {}", id);
        return userSubscriptionRepository.findById(id).map(userSubscriptionMapper::toDto);
    }

    /**
     * Delete the userSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserSubscription : {}", id);
        userSubscriptionRepository.deleteById(id);
    }
}
