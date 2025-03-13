package finalCDT.finalCDT.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import finalCDT.finalCDT.entitys.TarjetaCredito;

public interface TarjetaCreditoRepository extends JpaRepository<TarjetaCredito, Long> {
    List<TarjetaCredito> findByIdUser(UUID idUser);
}
