package finalCDT.finalCDT.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private BigDecimal balance;
    private boolean active;
    private String userEmail;
}
