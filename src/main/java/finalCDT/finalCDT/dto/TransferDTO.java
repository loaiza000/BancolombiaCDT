package finalCDT.finalCDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Creando TransferDTO para manejar las solicitudes de transferencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
    
    @NotBlank(message = "Source account number is required")
    private String sourceAccountNumber;
    
    @NotBlank(message = "Target account number is required")
    private String targetAccountNumber;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    private String description;
}
