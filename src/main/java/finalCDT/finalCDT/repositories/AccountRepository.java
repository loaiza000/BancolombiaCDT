package finalCDT.finalCDT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import finalCDT.finalCDT.entitys.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(String number);
    
    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    List<Account> findByUserEmail(String email);
    
    boolean existsByNumber(String number);
    
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
