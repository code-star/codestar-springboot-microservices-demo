package com.ordina.jwtauthlib.client;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.jwtauthlib.common.JwtUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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

    private final JwtClientService jwtClientService;

    public JwtAuthenticationFilter(JwtClientService jwtClientService) {
        this.jwtClientService = jwtClientService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var decodeResult = Jwt.decoder()
                .withToken(JwtUtils.getJwtFromRequest(request))
                .withKey(jwtClientService.getPublicKey());

        UUID userId = decodeResult.getUserId();
        setAuthentication(request, userId);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(@NonNull HttpServletRequest request, @Nullable UUID userId) {
        JwtUserDetails userDetails = new JwtUserDetails(userId);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails , null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}