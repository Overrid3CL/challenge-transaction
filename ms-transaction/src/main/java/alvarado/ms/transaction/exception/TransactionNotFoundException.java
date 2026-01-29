package alvarado.ms.transaction.exception;

public class TransactionNotFoundException extends RuntimeException {
    
    public TransactionNotFoundException(String message) {
        super(message);
    }
    
    public TransactionNotFoundException(Integer id) {
        super("Transaction with ID " + id + " not found");
    }
}
