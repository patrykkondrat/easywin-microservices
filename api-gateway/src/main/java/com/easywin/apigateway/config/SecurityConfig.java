package com.easywin.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/api/bet", "/api/bet/id", "/api/bet/discipline/**",
                                        "/api/event", "/api/event/id")
                                .permitAll()
                                .pathMatchers("/chat", "/api/ticket", "api/wallet/id")
                                .hasAnyRole("ROLE_USER", "ROLE_STAFF", "ROLE_ADMIN")
                                .pathMatchers("/api/bet/create", "/api/bet/isbet", "/api/event/create",
                                        "/api/event/publish", "/api/event/update", "/api/event/delete")
                                .hasAnyRole("ROLE_STAFF", "ROLE_ADMIN")
                                .pathMatchers("/api/bet/delete", "/eureka/**", "/api/event/publish",
                                        "/api/wallet/add", "/api/wallet")
                                .hasAnyRole("ROLE_ADMIN")
                                .anyExchange()
                                .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();
    }
}
