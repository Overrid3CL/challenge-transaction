package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TransactionBusinessValidator {
    
    public void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new BusinessRuleViolationException("Transaction amount must be positive");
        }
    }
    
    public void validateTransactionDate(Instant transactionDate) {
        if (transactionDate == null) {
            throw new BusinessRuleViolationException("Transaction date cannot be null");
        }
        
        if (transactionDate.isAfter(Instant.now())) {
            throw new BusinessRuleViolationException("Transaction date cannot be in the future");
        }
    }
    
    public void validateTransactionForCreation(Integer amount, Instant transactionDate) {
        validateAmount(amount);
        validateTransactionDate(transactionDate);
    }
}
