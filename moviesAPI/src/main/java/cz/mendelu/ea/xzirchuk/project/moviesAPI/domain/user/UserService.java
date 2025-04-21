package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user;


import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<RentalUser> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }
}
