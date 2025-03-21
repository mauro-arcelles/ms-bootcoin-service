package com.project1.ms_bootcoin_service.config.auth.jwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        if (token != null) {
            try {
                return Mono.just(new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    token,
                    authentication.getAuthorities()
                ));
            } catch (Exception e) {
                return Mono.empty();
            }
        }
        return Mono.empty();
    }
}
