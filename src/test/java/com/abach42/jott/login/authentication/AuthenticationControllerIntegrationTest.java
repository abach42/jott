package com.abach42.jott.login.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abach42.jott.testconfiguration.TestContainerConfiguration;
import com.abach42.jott.login.token.TokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tags(value = {@Tag("integration"), @Tag("auth")})
@SpringBootTest(classes = {TestContainerConfiguration.class})
@AutoConfigureMockMvc
@Testcontainers
@Import(TestContainerConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationControllerIntegrationTest {

    private static final String SLUG = "auth";

    @Value("${com.abach42.jott.basePath}")
    String basePath;

    String slug;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Admin should be able to receive a jwt")
    @WithUserDetails(value = "ADM1")
    void testAdminShouldBeAbleToReceiveAJwt() throws Exception {
        performRequest();
    }

    @Test
    @DisplayName("User should be able to receive a jwt")
    @WithUserDetails(value = "USR1")
    void testUserShouldBeAbleToReceiveAJwt() throws Exception {
        performRequest();
    }

    private void performRequest() throws Exception {
        String uri = UriComponentsBuilder.fromPath(basePath)
                .pathSegment(SLUG, "login")
                .toUriString();

        MvcResult result = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();

        TokenResponseDto actualTokenPair = objectMapper.readValue(actualResponse,
                TokenResponseDto.class);

        assertThat(actualTokenPair.access_token()).isNotBlank();
        assertThat(actualTokenPair.refresh_token()).isNotBlank();
        assertThat(actualTokenPair.refresh_token()).isNotEqualTo(actualTokenPair.access_token());
    }

    @Test
    @DisplayName("Should get new token with refresh token")
    @WithUserDetails("USR1")
    void shouldGetNewTokenWithRefreshToken() throws Exception {
        TokenResponseDto tokenPair = getInitialToken();

        MvcResult result = mockMvc.perform(get(basePath + "/auth/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenPair.refresh_token()))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto refreshed = objectMapper.readValue(
                result.getResponse().getContentAsString(), TokenResponseDto.class);

        assertThat(refreshed.access_token()).isNotBlank();
        assertThat(refreshed.refresh_token()).isNotBlank();
        assertThat(refreshed.refresh_token()).isNotEqualTo(refreshed.access_token());
    }

    @Test
    @DisplayName("Should forbid using access token for refresh endpoint")
    @WithUserDetails("USR1")
    void shouldRejectAccessTokenForRefreshEndpoint() throws Exception {
        TokenResponseDto tokenPair = getInitialToken();

        mockMvc.perform(get(basePath + "/auth/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenPair.access_token()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should forbid using refresh token for protected endpoint")
    @WithUserDetails("USR1")
    void shouldRejectRefreshTokenForProtectedEndpoint() throws Exception {
        TokenResponseDto tokenPair = getInitialToken();

        mockMvc.perform(get(basePath + "/users/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenPair.refresh_token()))
                .andExpect(status().isForbidden());
    }

    private TokenResponseDto getInitialToken() throws Exception {
        MvcResult result = mockMvc.perform(get(basePath + "/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(),
                TokenResponseDto.class);
    }
}
