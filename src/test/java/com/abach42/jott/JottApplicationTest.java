
package com.abach42.jott;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import com.abach42.jott.testconfiguration.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestContainerConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class JottApplicationTest {

    @Test
    @DisplayName("Should load context successfully")
    void contextLoads() {
    }

    @Test
    @DisplayName("Should start application successfully")
    void mainMethodShouldStartApplication() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = org.mockito.Mockito.mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(JottApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            JottApplication.main(new String[]{"--spring.profiles.active=test"});

            springApp.verify(() -> SpringApplication.run(JottApplication.class, new String[]{"--spring.profiles.active=test"}));
        }
    }
}