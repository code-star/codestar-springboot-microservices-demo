package com.ordina.authenticationservice.model;

import com.ordina.authenticationservice.controller.dto.UserDto;
import com.ordina.authenticationservice.controller.dto.UserRequest;
import com.ordina.authenticationservice.exception.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDtoRepository {

    final UserRepository repository;

    public UserDtoRepository(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public Optional<UserDto> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserDto::new);
    }

    public UserDto save(UserDto userDto) {
        try {
            return new UserDto(repository.save(userDto.toUserEntity()));
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public UserDto save(UserRequest userRequest) {
        return save(new UserDto(userRequest));
    }

    public long count() {
        return repository.count();
    }
}
