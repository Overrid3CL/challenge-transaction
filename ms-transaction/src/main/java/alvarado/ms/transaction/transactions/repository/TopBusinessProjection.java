package alvarado.ms.transaction.transactions.repository;

public interface TopBusinessProjection {
    Integer getBusinessId();
    String getBusinessName();
    Long getTransactionCount();
}
