package finalCDT.finalCDT.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String lastname;
    private String email;
    private String cc;
    private Set<String> roles;
    private Boolean isActivated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
