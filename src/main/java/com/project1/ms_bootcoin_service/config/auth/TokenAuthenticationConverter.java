package com.project1.ms_bootcoin_service.config.auth;

import com.project1.ms_bootcoin_service.config.auth.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
public class TokenAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> apply(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(authHeader -> authHeader.substring(7))
            .map(jwtService::getAuthentication);
    }
}
