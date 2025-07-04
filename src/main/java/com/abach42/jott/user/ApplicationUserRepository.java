package com.abach42.jott.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUserName(String userName);

    Optional<ApplicationUser> findByIdentifier(String identifier);

    Optional<ApplicationUser> findByEmail(String email);
}
