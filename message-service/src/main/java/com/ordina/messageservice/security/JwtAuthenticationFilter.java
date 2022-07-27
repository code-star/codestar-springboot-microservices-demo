package com.ordina.messageservice.security;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.jwtauthlib.JwtDecodeResult;
import com.ordina.jwtauthlib.MyUserDetails;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var decodeResult = Jwt.decoder()
                .withToken(getJwtFromRequest(request))
                .withKey(jwtService.getPublicKey());

        UUID userId = getUserId(decodeResult);
        setAuthentication(request, userId);

        filterChain.doFilter(request, response);
    }

    private UUID getUserId(JwtDecodeResult decodeResult) {
        UUID userId;
        if(decodeResult.isValid()) {
            userId = decodeResult.getUserId();
            log.info("User logged in with id: " + userId);
        } else {
            userId = null;
            log.warn("User not authorised! Reason: " + decodeResult.getErrorMessage());
        }
        return userId;
    }

    private void setAuthentication(@NonNull HttpServletRequest request, @Nullable UUID userId) {
        MyUserDetails userDetails = new MyUserDetails(userId);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails , null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}