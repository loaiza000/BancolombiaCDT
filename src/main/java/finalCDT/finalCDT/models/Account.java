package finalCDT.finalCDT.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Account {
    private UUID id;
    private String number;
    private String type;
    private UUID idUser;
    private Double balance;
    private Boolean isActivated;

    public Account(String number, String type, Double balance, UUID idUser) {
        this.number = number;
        this.type = type;
        this.balance = balance;
        this.id = UUID.randomUUID();
        this.idUser = idUser;
        this.isActivated = true;
    }
}
