package finalCDT.finalCDT.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalCDT.finalCDT.dto.UserDTO;
import finalCDT.finalCDT.dto.UserResponseDTO;
import finalCDT.finalCDT.entitys.User;
import finalCDT.finalCDT.exceptions.ResourceNotFoundException;
import finalCDT.finalCDT.exceptions.UserAlreadyExistsException;
import finalCDT.finalCDT.repositories.UserRepository;
import jakarta.validation.Valid;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(@Valid UserDTO userDTO) {
        // Validar si ya existe un usuario con el mismo email o cédula
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con el email: " + userDTO.getEmail());
        }
        if (userRepository.existsByCc(userDTO.getCc())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con la cédula: " + userDTO.getCc());
        }

        // Crear nuevo usuario
        User user = User.builder()
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .email(userDTO.getEmail())
                .cc(userDTO.getCc())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // Asignar rol por defecto
        user.getRoles().add("ROLE_USER");

        // Guardar y convertir a DTO
        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllActiveUsers() {
        return userRepository.findByIsActivatedTrue()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findByIdAndIsActivatedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return convertToResponseDTO(user);
    }

    public User getUserByCc(String cc) {
        return userRepository.findByCc(cc)
                .orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public UserResponseDTO updateUser(UUID id, @Valid UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Validar email y cédula únicos
        Optional<User> userByEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(id)) {
            throw new UserAlreadyExistsException("Ya existe un usuario con el email: " + userDTO.getEmail());
        }

        Optional<User> userByCc = userRepository.findByCc(userDTO.getCc());
        if (userByCc.isPresent() && !userByCc.get().getId().equals(id)) {
            throw new UserAlreadyExistsException("Ya existe un usuario con la cédula: " + userDTO.getCc());
        }

        // Actualizar datos
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setCc(userDTO.getCc());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }

    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        user.setIsActivated(false);
        userRepository.save(user);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .cc(user.getCc())
                .roles(user.getRoles())
                .isActivated(user.getIsActivated())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
