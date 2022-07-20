package com.ordina.authenticationservice.user;

import com.ordina.authenticationservice.security.PasswordHasher;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "users")
public class User {

    protected User() {}

    public User(UserDto userDto) {
        this.email = userDto.getEmail();
        this.username = userDto.getUsername();
        this.password = PasswordHasher.hash(userDto.getPassword());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled = true;
}
