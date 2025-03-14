package finalCDT.finalCDT.dto;

import finalCDT.finalCDT.validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    
    @NotBlank(message = "La cédula es obligatoria", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[0-9]{8,12}$", message = "La cédula debe tener entre 8 y 12 dígitos numéricos")
    private String cc;

    @NotBlank(message = "El nombre es obligatorio", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastname;

    @NotBlank(message = "El email es obligatorio", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "El estado de activación es obligatorio", groups = ValidationGroups.Update.class)
    private Boolean isActivated;

    private String phone;
}
