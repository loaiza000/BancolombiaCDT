package finalCDT.finalCDT.models;

import lombok.Data;
import java.util.UUID;

@Data
public class TarjetaCredito {

    private UUID id;
    private String nombre;
    private String apellido;
    private int edad;
    private UUID idUser;

    public TarjetaCredito(String nombre, String apellido, int edad, UUID idUser) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.idUser = idUser;
        this.id = UUID.randomUUID();
    }

}
