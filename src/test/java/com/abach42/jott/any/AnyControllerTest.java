
package com.abach42.jott.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abach42.jott.config.methodsecurity.MethodSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@Tags(value = {@Tag("integration"), @Tag("controller")})
@WebMvcTest(AnyController.class)
@Import(MethodSecurityConfig.class)
class AnyControllerTest {

    @Value("${com.abach42.jott.basePath}")
    String basePath;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @DisplayName("USER role: should access /bar endpoint")
    void userCanAccessBar() throws Exception {
        mockMvc.perform(get(basePath + "/any/bar"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"bar\": 42}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @DisplayName("USER role: should access /baz endpoint")
    void userCanAccessBaz() throws Exception {
        mockMvc.perform(get(basePath + "/any/baz"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"baz\": 43}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @DisplayName("USER role: should be forbidden from /boo endpoint")
    void userCannotAccessBoo() throws Exception {
        mockMvc.perform(get(basePath + "/any/boo"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @DisplayName("ADMIN role: should access /bar endpoint")
    void adminCanAccessBar() throws Exception {
        mockMvc.perform(get(basePath + "/any/bar"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"bar\": 42}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @DisplayName("ADMIN role: should access /baz endpoint")
    void adminCanAccessBaz() throws Exception {
        mockMvc.perform(get(basePath + "/any/baz"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"baz\": 43}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @DisplayName("ADMIN role: should access /boo endpoint")
    void adminCanAccessBoo() throws Exception {
        mockMvc.perform(get(basePath + "/any/boo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"bar\": 44}"))
                .andDo(print());
    }

    @Test
    @DisplayName("unauthenticated user: should be unauthorized for all endpoints")
    void unauthenticatedUserShouldBeUnauthorized() throws Exception {
        mockMvc.perform(get(basePath + "/any/bar"))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        mockMvc.perform(get(basePath + "/any/baz"))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        mockMvc.perform(get(basePath + "/any/boo"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}