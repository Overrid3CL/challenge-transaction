package alvarado.ms.transaction.businesses.repository;

import alvarado.ms.transaction.businesses.domain.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> {

    @Query("SELECT b FROM Business b WHERE b.deleted = false")
    List<Business> findAllByDeletedFalse();

    Optional<Business> findByIdAndDeletedFalse(Integer id);

    boolean existsByIdAndDeletedFalse(Integer id);
}
