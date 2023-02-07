package com.sangle.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sangle.sample.IntegrationTest;
import com.sangle.sample.domain.User;
import com.sangle.sample.repository.UserRepository;
import com.sangle.sample.service.dto.UserDTO;
import com.sangle.sample.service.mapper.UserMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebTestClient webTestClient;

    private User user;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity() {
        User user = new User().email(DEFAULT_EMAIL).phoneNumber(DEFAULT_PHONE_NUMBER).userId(DEFAULT_USER_ID);
        return user;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity() {
        User user = new User().email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER).userId(UPDATED_USER_ID);
        return user;
    }

    @BeforeEach
    public void initTest() {
        userRepository.deleteAll().block();
        user = createEntity();
    }

    @Test
    void getAllUsersAsStream() {
        // Initialize the database
        user.setId(UUID.randomUUID());
        userRepository.save(user).block();

        List<User> userList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UserDTO.class)
            .getResponseBody()
            .map(userMapper::toEntity)
            .filter(user::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(1);
        User testUser = userList.get(0);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    void getAllUsers() {
        // Initialize the database
        user.setId(UUID.randomUUID());
        userRepository.save(user).block();

        // Get all the userList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(user.getId().toString()))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID));
    }

    @Test
    void getUser() {
        // Initialize the database
        user.setId(UUID.randomUUID());
        userRepository.save(user).block();

        // Get the user
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, user.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(user.getId().toString()))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID));
    }

    @Test
    void getNonExistingUser() {
        // Get the user
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }
}
