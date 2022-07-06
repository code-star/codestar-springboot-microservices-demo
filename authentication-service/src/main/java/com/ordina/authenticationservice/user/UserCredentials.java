package com.ordina.authenticationservice.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@RequiredArgsConstructor
public class UserCredentials {

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    protected String username;

    @Column(nullable = false)
    @Getter @Setter
    protected String password;
}
