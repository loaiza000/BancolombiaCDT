package finalCDT.finalCDT.repositories;

import finalCDT.finalCDT.entitys.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccount_AccountNumber(String accountNumber);
    List<Transfer> findByTargetAccount_AccountNumber(String accountNumber);
}
