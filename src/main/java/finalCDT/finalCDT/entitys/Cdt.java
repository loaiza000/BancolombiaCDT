package finalCDT.finalCDT.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdts")
public class Cdt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id", nullable = false)
    private int idUser;

    @Column(nullable = false)
    private double monto;

    @Column(name = "plazo_meses", nullable = false)
    private int plazoMeses;

}

    
