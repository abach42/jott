package com.abach42.jott.security.token;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenGenerator extends AbstractTokenGenerator {

    public RefreshTokenGenerator(JwtEncoder jwtEncoder) {
        super(jwtEncoder);
    }

    @Override
    TokenPurpose getAllowedAction() {
        return TokenPurpose.REFRESH;
    }

    @Override
    int getExpirationMinutes() {
       return 120;
    }
}
