package com.project1.ms_bootcoin_service.config.auth;

import com.project1.ms_bootcoin_service.config.auth.jwt.JwtService;
import com.project1.ms_bootcoin_service.exception.BadRequestException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityUtils {

    @Autowired
    private JwtService jwtService;

    public Mono<String> getUserIdFromToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest()
            .getHeaders()
            .getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Mono.error(new BadRequestException("Invalid token"));
        }

        String token = authorization.substring(7);
        Claims claims = jwtService.getClaims(token);
        return Mono.just(claims.get("id").toString());
    }
}
