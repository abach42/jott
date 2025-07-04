package com.abach42.jott.security.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
  private final JwtTokenGenerator jwTokenGenerator;
  private final RefreshTokenGenerator refreshTokenGenerator;

  public AuthController(JwtTokenGenerator jwTokenGenerator, RefreshTokenGenerator refreshTokenGenerator) {
    this.jwTokenGenerator = jwTokenGenerator;
    this.refreshTokenGenerator = refreshTokenGenerator;
  }

  @Operation(summary = "Authenticate to get authorization")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200", description = "Authenticated",
          content = @Content,
          headers =  @Header(
              name = "Authorization",
              required = true,
              description = "Set email and password to Authorization header."
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content =  @Content
      )
  })
  @GetMapping("/login")
  public ResponseEntity<AuthResponse> showToken(Authentication authentication) {
    String jwt = jwTokenGenerator.generateToken(authentication);
    String refreshToken = refreshTokenGenerator.generateToken(authentication);
    return ResponseEntity.ok().body(new AuthResponse(jwt, refreshToken));
  }

  @Operation(summary = "Send a refresh token, get a new jwt")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200", description = "Authenticated",
          content = @Content,
          headers =  @Header(
              name = "Authorization",
              required = true,
              description = "Send a refresh token, get a new jwt."
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content =  @Content
      )
  })
  @GetMapping("/refresh-token")
  public ResponseEntity<AuthResponse> refreshToken(Authentication authentication) {
    String jwt = jwTokenGenerator.generateToken(authentication);
    String refreshToken = refreshTokenGenerator.generateToken(authentication);
    return ResponseEntity.ok().body(new AuthResponse(jwt, refreshToken));
  }

  public record AuthResponse(String jwt, String refreshToken) {
  }

}