package com.abach42.jott.security.authentication;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenGenerator extends AbstractTokenGenerator{

    public RefreshTokenGenerator(JwtEncoder jwtEncoder) {
        super(jwtEncoder);
    }

    @Override
    String getAllowedAction() {
        return "REFR";
    }

    @Override
    Long getExpirationMinutes() {
       return 120L;
    }
}
