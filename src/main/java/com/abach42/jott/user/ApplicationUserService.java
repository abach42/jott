package com.abach42.jott.user;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {
    ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ApplicationUser retrieveUserByCustomerUserId(String customerUserId) {
        Optional<ApplicationUser> user = applicationUserRepository.findByCustomerUserId(customerUserId);
        return user.get(); //todo handle optional
    }
}
