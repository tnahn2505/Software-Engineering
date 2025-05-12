package com.sagroup.teacherservice.config;

import com.sagroup.teacherservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        log.debug("Processing request to: {}", request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("Found Bearer token");

            try {
                if (jwtUtils.validateToken(token)) {
                    log.debug("Token is valid");
                    Claims claims = jwtUtils.getAllClaims(token);
                    String username = claims.getSubject();
                    log.info("Processing token for user: {}", username);

                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("roles", List.class);
                    log.info("User roles from token: {}", roles);

                    var authorities = roles.stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    log.debug("Mapped authorities: {}", authorities);

                    var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Successfully set authentication for user: {} with roles: {}", username, roles);
                } else {
                    log.warn("Token validation failed");
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
            }
        } else {
            log.debug("No Bearer token found in request");
        }

        filterChain.doFilter(request, response);
    }
} 