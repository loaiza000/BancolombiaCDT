package finalCDT.finalCDT.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private int originAccount;
    
    @Column(nullable = false)
    private int destinationAccount;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private Long timestamp;

}
