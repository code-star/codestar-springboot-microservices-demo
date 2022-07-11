package com.ordina.authenticationservice.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean enabled;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User other)) return false;

        if (!username.equals(other.username)) return false;
        if (!password.equals(other.password)) return false;
        return super.equals(other);
    }
}
