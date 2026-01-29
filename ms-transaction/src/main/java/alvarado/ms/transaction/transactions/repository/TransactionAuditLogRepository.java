package alvarado.ms.transaction.transactions.repository;

import alvarado.ms.transaction.transactions.domain.entity.TransactionAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAuditLogRepository extends JpaRepository<TransactionAuditLog, Integer> {
}
