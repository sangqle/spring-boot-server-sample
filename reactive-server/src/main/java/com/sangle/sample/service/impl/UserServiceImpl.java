package com.sangle.sample.service.impl;

import com.sangle.sample.domain.User;
import com.sangle.sample.repository.UserRepository;
import com.sangle.sample.service.UserService;
import com.sangle.sample.service.dto.UserDTO;
import com.sangle.sample.service.mapper.UserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        log.debug("Request to save User : {}", userDTO);
        return userRepository.save(userMapper.toEntity(userDTO)).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDTO> update(UserDTO userDTO) {
        log.debug("Request to update User : {}", userDTO);
        return userRepository.save(userMapper.toEntity(userDTO)).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDTO> partialUpdate(UserDTO userDTO) {
        log.debug("Request to partially update User : {}", userDTO);

        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                userMapper.partialUpdate(existingUser, userDTO);

                return existingUser;
            })
            .flatMap(userRepository::save)
            .map(userMapper::toDto);
    }

    @Override
    public Flux<UserDTO> findAll() {
        log.debug("Request to get all Users");
        return userRepository.findAll().map(userMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userRepository.count();
    }

    @Override
    public Mono<UserDTO> findOne(UUID id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete User : {}", id);
        return userRepository.deleteById(id);
    }
}
