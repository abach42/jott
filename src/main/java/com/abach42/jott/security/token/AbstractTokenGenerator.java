package com.abach42.jott.security.token;

import static com.abach42.jott.security.authorization.JwtConfig.MAC_ALGORITHM;

import com.abach42.jott.user.ApplicationUserService;
import com.abach42.jott.user.UserRole;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public abstract class AbstractTokenGenerator {

    public static final String CLAIM_AUTHORITIES = "authorities";

    public static final String CLAIM_ALLOWED = "allowed";

    public final ApplicationUserService applicationUserService;

    private final JwtEncoder jwtEncoder;

    protected Instant now;

    public AbstractTokenGenerator(ApplicationUserService applicationUserService, JwtEncoder jwtEncoder) {
        this.applicationUserService = applicationUserService;
        this.jwtEncoder = jwtEncoder;
    }

    abstract TokenPurpose getAllowedAction();
    abstract int getExpirationMinutes();

    public String generateToken(Authentication authentication) {
        now = Instant.now();

        UserRole userRole =
                applicationUserService.retrieveUserByIdentifier(authentication.getName()).getRole();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(getExpirationMinutes(), ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim(CLAIM_AUTHORITIES, userRole.name())
                .claim(CLAIM_ALLOWED, getAllowedAction())
                .build();
        
        return this.jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MAC_ALGORITHM).build(),
                claims)).getTokenValue();
    }
}