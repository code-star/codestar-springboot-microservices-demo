package com.ordina.authenticationservice.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@SuperBuilder
@NoArgsConstructor
public class UserCredentials {

    public UserCredentials(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    private String username;
    private String password;
}

