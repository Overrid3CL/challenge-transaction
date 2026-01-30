package alvarado.ms.transaction.users.repository;

import alvarado.ms.transaction.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Integer id);

    boolean existsByIdAndDeletedFalse(Integer id);

    @Query("SELECT u FROM User u WHERE u.deleted = false AND LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailAndDeletedFalse(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.deleted = false AND LOWER(u.email) = LOWER(:email) AND u.id != :excludeId")
    Optional<User> findByEmailAndDeletedFalseExcludingId(@Param("email") String email, @Param("excludeId") Integer excludeId);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true, u.deletedAt = :deletedAt WHERE u.id = :id")
    int softDelete(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt);
}
