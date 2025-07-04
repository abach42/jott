package com.abach42.jott.security.token;

import static com.abach42.jott.security.authorization.JwtConfig.MAC_ALGORITHM;

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

    public static final String CLAIM_AUTHORITIES = "authorities";

    public static final String CLAIM_ALLOWED = "allowed";

    private final JwtEncoder jwtEncoder;

    protected Instant now;

    public AbstractTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    abstract TokenPurpose getAllowedAction();
    abstract int getExpirationMinutes();

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
                .claim(CLAIM_AUTHORITIES, authorities)
                .claim(CLAIM_ALLOWED, getAllowedAction())
                .build();
        
        return this.jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MAC_ALGORITHM).build(),
                claims)).getTokenValue();
    }
}