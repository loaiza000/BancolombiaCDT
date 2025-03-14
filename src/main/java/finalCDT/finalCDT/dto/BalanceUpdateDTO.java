package finalCDT.finalCDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateDTO {
    
    @NotBlank(message = "Operation type is required")
    private String operationType;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
