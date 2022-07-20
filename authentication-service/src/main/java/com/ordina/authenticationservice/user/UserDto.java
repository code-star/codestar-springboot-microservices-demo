package com.ordina.authenticationservice.user;

import com.ordina.authenticationservice.security.PasswordHasher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class UserDto extends UserCredentials {

    public UserDto(User user) {
        super(user);

        this.id = user.getId();
        this.email = user.getEmail();
        this.enabled = user.getEnabled();
    }

    private Long id;
    private String email;
    private Boolean enabled;

    public boolean hasPassword(String plainPassword) {
        return PasswordHasher.areEqual(plainPassword, this.getPassword());
    }
}
