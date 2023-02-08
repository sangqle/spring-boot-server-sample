package com.sangle.crud.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sangle.crud.IntegrationTest;
import com.sangle.crud.domain.Pricing;
import com.sangle.crud.repository.PricingRepository;
import com.sangle.crud.service.dto.PricingDTO;
import com.sangle.crud.service.mapper.PricingMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private PricingMapper pricingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPricingMockMvc;

    private Pricing pricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createEntity(EntityManager em) {
        Pricing pricing = new Pricing()
            .title(DEFAULT_TITLE)
            .desc(DEFAULT_DESC)
            .price(DEFAULT_PRICE)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isDeleted(DEFAULT_IS_DELETED);
        return pricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createUpdatedEntity(EntityManager em) {
        Pricing pricing = new Pricing()
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .price(UPDATED_PRICE)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED);
        return pricing;
    }

    @BeforeEach
    public void initTest() {
        pricing = createEntity(em);
    }

    @Test
    @Transactional
    void createPricing() throws Exception {
        int databaseSizeBeforeCreate = pricingRepository.findAll().size();
        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);
        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
            .andExpect(status().isCreated());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate + 1);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPricing.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testPricing.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPricing.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPricing.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createPricingWithExistingId() throws Exception {
        // Create the Pricing with an existing ID
        pricing.setId(1L);
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        int databaseSizeBeforeCreate = pricingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPricings() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        // Get all the pricingList
        restPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        // Get the pricing
        restPricingMockMvc
            .perform(get(ENTITY_API_URL_ID, pricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pricing.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPricing() throws Exception {
        // Get the pricing
        restPricingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing
        Pricing updatedPricing = pricingRepository.findById(pricing.getId()).get();
        // Disconnect from session so that the updates on updatedPricing are not directly saved in db
        em.detach(updatedPricing);
        updatedPricing
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .price(UPDATED_PRICE)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED);
        PricingDTO pricingDTO = pricingMapper.toDto(updatedPricing);

        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPricing.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testPricing.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPricing.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing.updatedAt(UPDATED_UPDATED_AT).isDeleted(UPDATED_IS_DELETED);

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPricing.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testPricing.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPricing.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .price(UPDATED_PRICE)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED);

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPricing.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testPricing.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPricing.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pricingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(count.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pricingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        int databaseSizeBeforeDelete = pricingRepository.findAll().size();

        // Delete the pricing
        restPricingMockMvc
            .perform(delete(ENTITY_API_URL_ID, pricing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
