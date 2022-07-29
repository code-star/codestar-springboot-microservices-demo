package com.ordina.jwtauthlib;

import com.ordina.jwtauthlib.host.HostConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HostConfig.class)
public @interface EnableAuthenticationHost {
}
