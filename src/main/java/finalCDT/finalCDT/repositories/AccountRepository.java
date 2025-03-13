package finalCDT.finalCDT.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import finalCDT.finalCDT.entitys.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByIdUser(UUID idUser);
    Account findByNumber(String number);
}
