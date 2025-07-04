package com.abach42.jott.security.token;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenGenerator extends AbstractTokenGenerator {
    public JwtTokenGenerator(JwtEncoder jwtEncoder) {
        super(jwtEncoder);
    }

    @Override
    TokenPurpose getAllowedAction() {
        return TokenPurpose.AUTH;
    }

    @Override
    public int getExpirationMinutes() {
       return 15;
    }
}
