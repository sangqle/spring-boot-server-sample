package com.sangle.crud.web.rest;

import com.sangle.crud.domain.UserSubscription;
import com.sangle.crud.repository.UserSubscriptionRepository;
import com.sangle.crud.service.JSubscriptionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link UserSubscription}.
 */
@RestController
@RequestMapping("/api")
public class UserSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionResource.class);

    private static final String ENTITY_NAME = "jSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JSubscriptionService jSubscriptionService;

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionResource(JSubscriptionService jSubscriptionService, UserSubscriptionRepository userSubscriptionRepository) {
        this.jSubscriptionService = jSubscriptionService;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    /**
     * {@code POST  /j-subscriptions} : Create a new jSubscription.
     *
     * @param userSubscriptionDTO the jSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jSubscriptionDTO, or with status {@code 400 (Bad Request)} if the jSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/j-subscriptions")
    public ResponseEntity<UserSubscriptionDTO> createJSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save JSubscription : {}", userSubscriptionDTO);
        if (userSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new jSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSubscriptionDTO result = jSubscriptionService.save(userSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/j-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /j-subscriptions/:id} : Updates an existing jSubscription.
     *
     * @param id the id of the jSubscriptionDTO to save.
     * @param userSubscriptionDTO the jSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the jSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/j-subscriptions/{id}")
    public ResponseEntity<UserSubscriptionDTO> updateJSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JSubscription : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserSubscriptionDTO result = jSubscriptionService.update(userSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /j-subscriptions/:id} : Partial updates given fields of an existing jSubscription, field will ignore if it is null
     *
     * @param id the id of the jSubscriptionDTO to save.
     * @param userSubscriptionDTO the jSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the jSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/j-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSubscriptionDTO> partialUpdateJSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JSubscription partially : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSubscriptionDTO> result = jSubscriptionService.partialUpdate(userSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /j-subscriptions} : get all the jSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jSubscriptions in body.
     */
    @GetMapping("/j-subscriptions")
    public ResponseEntity<List<UserSubscriptionDTO>> getAllJSubscriptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of JSubscriptions");
        Page<UserSubscriptionDTO> page = jSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /j-subscriptions/:id} : get the "id" jSubscription.
     *
     * @param id the id of the jSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/j-subscriptions/{id}")
    public ResponseEntity<UserSubscriptionDTO> getJSubscription(@PathVariable Long id) {
        log.debug("REST request to get JSubscription : {}", id);
        Optional<UserSubscriptionDTO> jSubscriptionDTO = jSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jSubscriptionDTO);
    }

    /**
     * {@code DELETE  /j-subscriptions/:id} : delete the "id" jSubscription.
     *
     * @param id the id of the jSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/j-subscriptions/{id}")
    public ResponseEntity<Void> deleteJSubscription(@PathVariable Long id) {
        log.debug("REST request to delete JSubscription : {}", id);
        jSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
