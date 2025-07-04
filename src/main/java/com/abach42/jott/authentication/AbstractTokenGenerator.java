package com.abach42.jott.authentication;

import static com.abach42.jott.security.JwtConfig.MAC_ALGORITHM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public abstract class AbstractTokenGenerator {
    private final JwtEncoder jwtEncoder;
    protected Instant now; 

    public AbstractTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    abstract String getAllowedAction();
    abstract Long getExpirationMinutes();

    public String generateToken(Authentication authentication) {
        now = Instant.now();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(getExpirationMinutes(), ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .claim("allowedAction", getAllowedAction())
                .build();
        
        return this.jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MAC_ALGORITHM).build(),
                claims)).getTokenValue();
    }
}