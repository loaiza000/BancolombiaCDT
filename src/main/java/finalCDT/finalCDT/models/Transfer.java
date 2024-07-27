package finalCDT.finalCDT.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Transfer {
    private UUID id; 
    private UUID originAccount;
    private UUID destinationAccount;
    private Double amount;


    public Transfer(Double amount, UUID destinationAccount, UUID originAccount) {
        this.amount = amount;
        this.destinationAccount = destinationAccount;
        this.originAccount = originAccount;
        this.id = UUID.randomUUID();
    }
}
