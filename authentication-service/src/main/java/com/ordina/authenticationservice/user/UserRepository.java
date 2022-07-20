package com.ordina.authenticationservice.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    default Optional<UserDto> findUserDtoByUsername(String username) {
        Optional<User> user = findByUsername(username);
        return user.map(UserDto::new);
    }

    default Optional<UserDto> findUserDtoByEmail(String email) {
        Optional<User> user = findByEmail(email);
        return user.map(UserDto::new);
    }

}
