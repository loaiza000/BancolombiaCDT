package finalCDT.finalCDT.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import finalCDT.finalCDT.entitys.Cdt;

public interface CdtRepository extends JpaRepository<Cdt, Long> {
    List<Cdt> findByIdUser(UUID idUser);
}
