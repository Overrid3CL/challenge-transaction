package alvarado.ms.transaction.users.repository;

import alvarado.ms.transaction.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Integer id);

    boolean existsByIdAndDeletedFalse(Integer id);
}
