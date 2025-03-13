package finalCDT.finalCDT.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalCDT.finalCDT.entitys.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByCc(String cc);
    Optional<User> findByEmail(String email);
    Optional<User> findByCc(String cc);
    List<User> findByIsActivatedTrue();
    Optional<User> findByIdAndIsActivatedTrue(UUID id);
}
