package com.ordina.authenticationservice.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Table(name = "users")
@Entity
public class User extends UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private Boolean enabled;
}
