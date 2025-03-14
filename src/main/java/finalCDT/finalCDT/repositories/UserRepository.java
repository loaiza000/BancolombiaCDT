package finalCDT.finalCDT.repositories;

import finalCDT.finalCDT.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCc(String cc);
    boolean existsByEmail(String email);
    boolean existsByCc(String cc);
    List<User> findByIsActivatedTrue();
}
