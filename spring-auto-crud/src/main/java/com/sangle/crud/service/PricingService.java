package com.sangle.crud.service;

import com.sangle.crud.domain.Pricing;
import com.sangle.crud.repository.PricingRepository;
import com.sangle.crud.service.dto.PricingDTO;
import com.sangle.crud.service.mapper.PricingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pricing}.
 */
@Service
@Transactional
public class PricingService {

    private final Logger log = LoggerFactory.getLogger(PricingService.class);

    private final PricingRepository pricingRepository;

    private final PricingMapper pricingMapper;

    public PricingService(PricingRepository pricingRepository, PricingMapper pricingMapper) {
        this.pricingRepository = pricingRepository;
        this.pricingMapper = pricingMapper;
    }

    /**
     * Save a pricing.
     *
     * @param pricingDTO the entity to save.
     * @return the persisted entity.
     */
    public PricingDTO save(PricingDTO pricingDTO) {
        log.debug("Request to save Pricing : {}", pricingDTO);
        Pricing pricing = pricingMapper.toEntity(pricingDTO);
        pricing = pricingRepository.save(pricing);
        return pricingMapper.toDto(pricing);
    }

    /**
     * Update a pricing.
     *
     * @param pricingDTO the entity to save.
     * @return the persisted entity.
     */
    public PricingDTO update(PricingDTO pricingDTO) {
        log.debug("Request to update Pricing : {}", pricingDTO);
        Pricing pricing = pricingMapper.toEntity(pricingDTO);
        pricing = pricingRepository.save(pricing);
        return pricingMapper.toDto(pricing);
    }

    /**
     * Partially update a pricing.
     *
     * @param pricingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PricingDTO> partialUpdate(PricingDTO pricingDTO) {
        log.debug("Request to partially update Pricing : {}", pricingDTO);

        return pricingRepository
            .findById(pricingDTO.getId())
            .map(existingPricing -> {
                pricingMapper.partialUpdate(existingPricing, pricingDTO);

                return existingPricing;
            })
            .map(pricingRepository::save)
            .map(pricingMapper::toDto);
    }

    /**
     * Get all the pricings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PricingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pricings");
        return pricingRepository.findAll(pageable).map(pricingMapper::toDto);
    }

    /**
     * Get one pricing by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PricingDTO> findOne(Long id) {
        log.debug("Request to get Pricing : {}", id);
        return pricingRepository.findById(id).map(pricingMapper::toDto);
    }

    /**
     * Delete the pricing by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pricing : {}", id);
        pricingRepository.deleteById(id);
    }
}
