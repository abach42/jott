package com.abach42.jott.authentication;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenGenerator extends AbstractTokenGenerator{

    public JwtTokenGenerator(JwtEncoder jwtEncoder) {
        super(jwtEncoder);
    }

    @Override
    String getAllowedAction() {
        return "AUTH";
    }

    @Override
    Long getExpirationMinutes() {
       return 15L;
    }
}
