package com.sangle.crud.web.rest;

import com.sangle.crud.repository.UserSubscriptionRepository;
import com.sangle.crud.service.UserSubscriptionService;
import com.sangle.crud.service.dto.UserSubscriptionDTO;
import com.sangle.crud.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sangle.crud.domain.UserSubscription}.
 */
@RestController
@RequestMapping("/api")
public class UserSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionResource.class);

    private static final String ENTITY_NAME = "userSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSubscriptionService userSubscriptionService;

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionResource(
        UserSubscriptionService userSubscriptionService,
        UserSubscriptionRepository userSubscriptionRepository
    ) {
        this.userSubscriptionService = userSubscriptionService;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    /**
     * {@code POST  /user-subscriptions} : Create a new userSubscription.
     *
     * @param userSubscriptionDTO the userSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSubscriptionDTO, or with status {@code 400 (Bad Request)} if the userSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-subscriptions")
    public ResponseEntity<UserSubscriptionDTO> createUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserSubscription : {}", userSubscriptionDTO);
        if (userSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSubscriptionDTO result = userSubscriptionService.save(userSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/user-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-subscriptions/:id} : Updates an existing userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to save.
     * @param userSubscriptionDTO the userSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-subscriptions/{id}")
    public ResponseEntity<UserSubscriptionDTO> updateUserSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserSubscription : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserSubscriptionDTO result = userSubscriptionService.update(userSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-subscriptions/:id} : Partial updates given fields of an existing userSubscription, field will ignore if it is null
     *
     * @param id the id of the userSubscriptionDTO to save.
     * @param userSubscriptionDTO the userSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSubscriptionDTO> partialUpdateUserSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSubscription partially : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSubscriptionDTO> result = userSubscriptionService.partialUpdate(userSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-subscriptions} : get all the userSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSubscriptions in body.
     */
    @GetMapping("/user-subscriptions")
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UserSubscriptions");
        Page<UserSubscriptionDTO> page = userSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-subscriptions/:id} : get the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-subscriptions/{id}")
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(@PathVariable Long id) {
        log.debug("REST request to get UserSubscription : {}", id);
        Optional<UserSubscriptionDTO> userSubscriptionDTO = userSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSubscriptionDTO);
    }

    /**
     * {@code DELETE  /user-subscriptions/:id} : delete the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-subscriptions/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Long id) {
        log.debug("REST request to delete UserSubscription : {}", id);
        userSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
