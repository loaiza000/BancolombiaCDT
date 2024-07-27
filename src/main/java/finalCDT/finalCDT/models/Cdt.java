package finalCDT.finalCDT.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Cdt {
    private UUID id;
    private UUID idUser;
    private double monto;
    private int plazoMeses;

    public Cdt(UUID idUser, double monto, int plazoMeses) {
        this.idUser = idUser;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.id = UUID.randomUUID();
    }
}
