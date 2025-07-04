package com.abach42.jott.user;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ApplicationUser retrieveUserByIdentifier(String identifier) {
        Optional<ApplicationUser> user = applicationUserRepository.findByIdentifier(identifier);
        return user.orElseThrow(UserNotFoundException::new);
    }
}
