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

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var decodeResult = Jwt.decoder()
                .withToken(getJwtFromRequest(request))
                .withKey(jwtService.getPublicKey());

        Long userId = getUserId(decodeResult);
        setAuthentication(request, userId);

        filterChain.doFilter(request, response);
    }

    private Long getUserId(JwtDecodeResult decodeResult) {
        Long userId;
        if(decodeResult.isValid()) {
            userId = decodeResult.getUserId();
            log.info("User logged in with id: " + userId);
        } else {
            userId = null;
            log.warn("User not authorised! Reason: " + decodeResult.getErrorMessage());
        }
        return userId;
    }

    private void setAuthentication(@NonNull HttpServletRequest request, @Nullable Long userId) {
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