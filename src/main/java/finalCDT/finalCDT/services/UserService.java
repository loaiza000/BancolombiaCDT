package finalCDT.finalCDT.services;

import finalCDT.finalCDT.dto.UserDTO;
import finalCDT.finalCDT.dto.UserResponseDTO;
import finalCDT.finalCDT.entitys.User;
import finalCDT.finalCDT.exceptions.ResourceNotFoundException;
import finalCDT.finalCDT.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserDTO userDTO) {
        validateNewUser(userDTO);

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .cc(userDTO.getCc())
                .phone(userDTO.getPhone())
                .roles(new HashSet<>())
                .isActivated(true)
                .build();

        user = userRepository.save(user);
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByIsActivatedTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponseDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!user.getCc().equals(userDTO.getCc()) && userRepository.existsByCc(userDTO.getCc())) {
            throw new IllegalArgumentException("CC already exists");
        }

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setCc(userDTO.getCc());
        user.setPhone(userDTO.getPhone());

        user = userRepository.save(user);
        return mapToResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActivated(false);
        userRepository.save(user);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cc(user.getCc())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .isActivated(user.getIsActivated())
                .build();
    }

    private void validateNewUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByCc(userDTO.getCc())) {
            throw new IllegalArgumentException("CC already exists");
        }
    }
}
