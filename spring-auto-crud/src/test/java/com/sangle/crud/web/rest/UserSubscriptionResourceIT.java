package com.sangle.crud.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sangle.crud.IntegrationTest;
import com.sangle.crud.domain.UserSubscription;
import com.sangle.crud.repository.UserSubscriptionRepository;
import com.sangle.crud.service.dto.UserSubscriptionDTO;
import com.sangle.crud.service.mapper.UserSubscriptionMapper;
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
 * Integration tests for the {@link UserSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSubscriptionResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PRICING_ID = 1L;
    private static final Long UPDATED_PRICING_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSubscriptionMockMvc;

    private UserSubscription userSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createEntity(EntityManager em) {
        UserSubscription userSubscription = new UserSubscription()
            .userId(DEFAULT_USER_ID)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .pricingId(DEFAULT_PRICING_ID);
        return userSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createUpdatedEntity(EntityManager em) {
        UserSubscription userSubscription = new UserSubscription()
            .userId(UPDATED_USER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .pricingId(UPDATED_PRICING_ID);
        return userSubscription;
    }

    @BeforeEach
    public void initTest() {
        userSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createUserSubscription() throws Exception {
        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();
        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);
        restUserSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserSubscription.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testUserSubscription.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testUserSubscription.getPricingId()).isEqualTo(DEFAULT_PRICING_ID);
    }

    @Test
    @Transactional
    void createUserSubscriptionWithExistingId() throws Exception {
        // Create the UserSubscription with an existing ID
        userSubscription.setId(1L);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserSubscriptions() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].pricingId").value(hasItem(DEFAULT_PRICING_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get the userSubscription
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, userSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSubscription.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.pricingId").value(DEFAULT_PRICING_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserSubscription() throws Exception {
        // Get the userSubscription
        restUserSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription
        UserSubscription updatedUserSubscription = userSubscriptionRepository.findById(userSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedUserSubscription are not directly saved in db
        em.detach(updatedUserSubscription);
        updatedUserSubscription
            .userId(UPDATED_USER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .pricingId(UPDATED_PRICING_ID);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(updatedUserSubscription);

        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserSubscription.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testUserSubscription.getPricingId()).isEqualTo(UPDATED_PRICING_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription.userId(UPDATED_USER_ID).pricingId(UPDATED_PRICING_ID);

        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserSubscription.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testUserSubscription.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testUserSubscription.getPricingId()).isEqualTo(UPDATED_PRICING_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription
            .userId(UPDATED_USER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .pricingId(UPDATED_PRICING_ID);

        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserSubscription.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testUserSubscription.getPricingId()).isEqualTo(UPDATED_PRICING_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(count.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeDelete = userSubscriptionRepository.findAll().size();

        // Delete the userSubscription
        restUserSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
