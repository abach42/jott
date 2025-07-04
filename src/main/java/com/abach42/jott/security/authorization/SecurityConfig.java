package com.abach42.jott.security.authorization;

import com.abach42.jott.security.token.AbstractTokenGenerator;
import com.abach42.jott.security.token.TokenPurpose;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http,
            Function<TokenPurpose, WebExpressionAuthorizationManager> tokenAccess)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login").authenticated()
                        .requestMatchers("/api/auth/refresh-token")
                                .access(tokenAccess.apply(TokenPurpose.REFRESH))
                        .requestMatchers("/api/any/**")
                                .access(tokenAccess.apply(TokenPurpose.AUTH))
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain documentationResourceFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/chart.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll());
        return http.build();
    }


    @Bean
    public Function<TokenPurpose, WebExpressionAuthorizationManager> tokenAccess() {
        return tokenPurpose -> new WebExpressionAuthorizationManager(
                "principal.claims['"+ AbstractTokenGenerator.CLAIM_ALLOWED +"'] == '"
                        + tokenPurpose.name() + "'");
    }
}