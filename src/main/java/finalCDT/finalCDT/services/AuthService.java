package finalCDT.finalCDT.services;

import finalCDT.finalCDT.dto.AuthDTO;
import finalCDT.finalCDT.entitys.User;
import finalCDT.finalCDT.exceptions.ResourceNotFoundException;
import finalCDT.finalCDT.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticate(AuthDTO authDTO) {
        log.info("Authenticating user with email: {}", authDTO.getEmail());
        
        User user = userRepository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getIsActivated()) {
            throw new ResourceNotFoundException("User account is deactivated");
        }

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        log.info("User authenticated successfully: {}", user.getEmail());
        return user;
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        log.info("Changing password for user with email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResourceNotFoundException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password updated successfully for user with email: {}", email);
    }
}
