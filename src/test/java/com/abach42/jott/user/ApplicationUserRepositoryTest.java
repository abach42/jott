package com.abach42.jott.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.abach42.jott.testconfiguration.TestContainerConfiguration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@Tags(value = {@Tag("integration"), @Tag("user")})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationUserRepository subject;

    private ApplicationUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new ApplicationUser();
        testUser.setUserName("testuser");
        testUser.setIdentifier("test123");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.USER);
    }

    @Test
    @DisplayName("should save and find user by identifier")
    void shouldSaveAndFindUserByIdentifier() {
        ApplicationUser savedUser = entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByIdentifier("test123");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedUser.getId());
        assertThat(result.get().getUserName()).isEqualTo("testuser");
        assertThat(result.get().getIdentifier()).isEqualTo("test123");
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("should save and find user by userName")
    void shouldSaveAndFindUserByUserName() {
        entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByUserName("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getUserName()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("should save and find user by email")
    void shouldSaveAndFindUserByEmail() {
        entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("should save admin user with correct role")
    void shouldSaveAdminUserWithCorrectRole() {
        testUser.setRole(UserRole.ADMIN);
        entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByIdentifier("test123");

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<ApplicationUser> result = subject.findByIdentifier("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    @DisplayName("should work with USER role security context")
    void shouldWorkWithUserRoleSecurityContext() {
        entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @DisplayName("should work with ADMIN role security context")
    void shouldWorkWithAdminRoleSecurityContext() {
        testUser.setEmail("admin@example.com");
        testUser.setRole(UserRole.ADMIN);
        entityManager.persistAndFlush(testUser);

        Optional<ApplicationUser> result = subject.findByEmail("admin@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(UserRole.ADMIN);
    }
}
