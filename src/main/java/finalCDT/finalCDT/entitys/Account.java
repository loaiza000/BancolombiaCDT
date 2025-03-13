package finalCDT.finalCDT.entitys;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuentas")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true)
    private String number;

    @Column(name = "tipo", nullable = false, unique = true)
    private String type;

    @Column(name = "tipo", nullable = false, unique = true)
    private UUID idUser;

    @Column(name = "tipo", nullable = false, unique = true)
    private Double balance;

    @Column(name = "tipo", nullable = false, unique = true)
    private Boolean isActivated;


}
