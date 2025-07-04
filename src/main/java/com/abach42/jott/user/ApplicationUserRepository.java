package com.abach42.jott.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByCustomerUserId(String customerUserId);

    Optional<User> findByEmail(String email);
}
