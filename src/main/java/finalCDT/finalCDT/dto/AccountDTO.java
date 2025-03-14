package finalCDT.finalCDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "User email is required")
    private String userEmail;

    @NotNull(message = "Initial balance is required")
    @Positive(message = "Initial balance must be positive")
    private BigDecimal balance;
}
