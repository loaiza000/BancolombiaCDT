package finalCDT.finalCDT.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import finalCDT.finalCDT.entitys.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByOriginAccount(UUID originAccount);
    List<Transfer> findByDestinationAccount(UUID destinationAccount);
}
