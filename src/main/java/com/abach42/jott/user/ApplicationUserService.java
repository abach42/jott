package com.abach42.jott.user;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User retrieveUserByCustomerUserId(String customerUserId) {
        Optional<User> user = userRepository.findByCustomerUserId(customerUserId);
        return user.get(); //todo handle optional
    }
}
