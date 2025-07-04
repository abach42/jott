package com.abach42.jott.security;

import com.abach42.jott.user.ApplicationUser;
import com.abach42.jott.user.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration(proxyBeanMethods = false)
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(ApplicationUserService applicationUserService) {
        return customerUserId -> {
            ApplicationUser applicationUser =
                    applicationUserService.retrieveUserByCustomerUserId(customerUserId);
            return User.withUsername(applicationUser.getUserName())
                .password(applicationUser.getPassword())
                .authorities("USER")
                .build();
        };
    }
}