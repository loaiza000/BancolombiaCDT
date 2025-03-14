package finalCDT.finalCDT.controllers;

import finalCDT.finalCDT.dto.UserDTO;
import finalCDT.finalCDT.dto.UserResponseDTO;
import finalCDT.finalCDT.helpers.ApiResponse;
import finalCDT.finalCDT.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO user = userService.createUser(userDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO user = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully"));
    }
}
