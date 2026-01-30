package alvarado.ms.transaction.transactions.repository;

import alvarado.ms.transaction.transactions.domain.entity.Transaction;
import alvarado.ms.transaction.users.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    
    @Query("SELECT t FROM Transaction t WHERE t.deleted = false")
    List<Transaction> findAllActive();

    @Query(
            value = "SELECT t FROM Transaction t JOIN FETCH t.user u JOIN FETCH t.business b " +
                    "WHERE t.deleted = false " +
                    "AND (:search IS NULL OR :search = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR (t.description IS NOT NULL AND LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))))",
            countQuery = "SELECT COUNT(t) FROM Transaction t JOIN t.user u JOIN t.business b " +
                    "WHERE t.deleted = false " +
                    "AND (:search IS NULL OR :search = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR (t.description IS NOT NULL AND LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))))"
    )
    Page<Transaction> findAllActive(@Param("search") String search, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.id = :id AND t.deleted = false")
    Optional<Transaction> findByIdAndDeletedFalse(@Param("id") Integer id);
    
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserIdAndDeletedFalse(@Param("userId") Integer userId);
    
    @Query("SELECT t FROM Transaction t WHERE t.business.id = :businessId AND t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByBusinessIdAndDeletedFalse(@Param("businessId") Integer businessId);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :start AND :end AND t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByTransactionDateBetweenAndDeletedFalse(
            @Param("start") Instant start,
            @Param("end") Instant end
    );
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Transaction t WHERE t.id = :id AND t.deleted = false")
    boolean existsByIdAndDeletedFalse(@Param("id") Integer id);
    
    @Modifying
    @Query("UPDATE Transaction t SET t.deleted = true, t.deletedAt = :deletedAt, t.deletedBy = :deletedBy WHERE t.id = :id")
    int softDelete(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt, @Param("deletedBy") User deletedBy);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.deleted = false")
    Long getTotalVolume();
    
    @Query("SELECT COALESCE(AVG(t.amount), 0.0) FROM Transaction t WHERE t.deleted = false")
    Double getAverageTicket();
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.amount > :threshold AND t.deleted = false")
    Long countTransactionsAboveThreshold(@Param("threshold") Integer threshold);
    
    @Query(value = "SELECT t.business_id AS businessId, b.name AS businessName, COUNT(*) AS transactionCount " +
                   "FROM transaction t " +
                   "INNER JOIN business b ON t.business_id = b.id " +
                   "WHERE t.deleted = false AND b.deleted = false " +
                   "GROUP BY t.business_id, b.name " +
                   "ORDER BY COUNT(*) DESC " +
                   "LIMIT 1", nativeQuery = true)
    TopBusinessProjection findTopBusinessByTransactionCount();
}
