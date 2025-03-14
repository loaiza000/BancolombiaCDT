package finalCDT.finalCDT.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String cc;
    private String phone;
    private Set<String> roles;
    private boolean isActivated;
}
