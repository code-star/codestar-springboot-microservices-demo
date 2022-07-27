package com.ordina.authenticationservice.controller.dto;

import com.ordina.authenticationservice.model.User;
import com.ordina.authenticationservice.security.PasswordHasher;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserDto {

    public UserDto(User u) {
        this.id = u.getId();
        this.email = u.getEmail();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.enabled = u.getEnabled();
    }

    public UserDto(UserRequest ur) {
        this.id = UUID.randomUUID();
        this.email = ur.email();
        this.username = ur.username();
        this.password = PasswordHasher.hash(ur.password());
        this.enabled = true;
    }

    public UserDto(UUID id, String email, String username, String plainPassword, Boolean enabled) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = PasswordHasher.hash(plainPassword);
        this.enabled = enabled;
    }

    private UUID id;
    private String email;
    private String username;
    private String password;
    @Builder.Default
    private Boolean enabled = true;

    public boolean passwordEquals(String plainPassword) {
        return PasswordHasher.areEqual(plainPassword, this.getPassword());
    }

    public User toUserEntity() {
        return new User(this.id, this.email, this.username, this.password, this.enabled);
    }

    public UserResponse toUserResponse() {
        return new UserResponse(this.id, this.username, this.email);
    }
}
