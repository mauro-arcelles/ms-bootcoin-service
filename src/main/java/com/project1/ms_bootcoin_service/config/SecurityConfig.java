package com.project1.ms_bootcoin_service.config;

import com.project1.ms_bootcoin_service.config.auth.TokenAuthenticationConverter;
import com.project1.ms_bootcoin_service.config.auth.jwt.JWTReactiveAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JWTReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    @Autowired
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    private static final String[] AUTH_WHITELIST = {
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/actuator/**",
        "/bootcoin-docs/**",
        "/api/v1/bootcoin/exchange/requests/by-transaction-id/**",
        "/api/v1/bootcoin/wallets/by-user-id/**",
        "/api/v1/bootcoin/wallets/**",
        "/api/v1/bootcoin/exchange/requests/{id}",
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.PUT, "/api/v1/bootcoin/exchange/rates/**").hasAuthority("ROLE_ADMIN")
            .pathMatchers(HttpMethod.POST, "/api/v1/bootcoin/wallets").authenticated()
            .pathMatchers(AUTH_WHITELIST).permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtReactiveAuthenticationManager);
        filter.setAuthenticationConverter(tokenAuthenticationConverter);
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return filter;
    }
}
