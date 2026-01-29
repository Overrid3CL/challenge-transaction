package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionBusinessValidatorTest {
    
    @InjectMocks
    private TransactionBusinessValidator transactionBusinessValidator;
    
    private Instant pastDate;
    private Instant futureDate;
    private Instant now;
    
    @BeforeEach
    void setUp() {
        pastDate = Instant.now().minus(1, ChronoUnit.DAYS);
        futureDate = Instant.now().plus(1, ChronoUnit.DAYS);
        now = Instant.now();
    }
    
    // Tests para validateAmount
    
    @Test
    void testValidateAmount_Success() {
        // Given
        Integer amount = 10000;
        
        // When & Then
        assertDoesNotThrow(() -> {
            transactionBusinessValidator.validateAmount(amount);
        });
    }
    
    @Test
    void testValidateAmount_Null() {
        // Given
        Integer amount = null;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateAmount(amount);
        });
        
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }
    
    @Test
    void testValidateAmount_Zero() {
        // Given
        Integer amount = 0;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateAmount(amount);
        });
        
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }
    
    @Test
    void testValidateAmount_Negative() {
        // Given
        Integer amount = -100;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateAmount(amount);
        });
        
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }
    
    // Tests para validateTransactionDate
    
    @Test
    void testValidateTransactionDate_Success() {
        // Given
        Instant transactionDate = pastDate;
        
        // When & Then
        assertDoesNotThrow(() -> {
            transactionBusinessValidator.validateTransactionDate(transactionDate);
        });
    }
    
    @Test
    void testValidateTransactionDate_Null() {
        // Given
        Instant transactionDate = null;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionDate(transactionDate);
        });
        
        assertEquals("Transaction date cannot be null", exception.getMessage());
    }
    
    @Test
    void testValidateTransactionDate_Future() {
        // Given
        Instant transactionDate = futureDate;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionDate(transactionDate);
        });
        
        assertEquals("Transaction date cannot be in the future", exception.getMessage());
    }
    
    @Test
    void testValidateTransactionDate_Now() {
        // Given
        Instant transactionDate = now;
        
        // When & Then
        // La fecha actual no es futura, por lo que debería pasar la validación
        assertDoesNotThrow(() -> {
            transactionBusinessValidator.validateTransactionDate(transactionDate);
        });
    }
    
    // Tests para validateTransactionForCreation
    
    @Test
    void testValidateTransactionForCreation_Success() {
        // Given
        Integer amount = 10000;
        Instant transactionDate = pastDate;
        
        // When & Then
        assertDoesNotThrow(() -> {
            transactionBusinessValidator.validateTransactionForCreation(amount, transactionDate);
        });
    }
    
    @Test
    void testValidateTransactionForCreation_InvalidAmount() {
        // Given
        Integer amount = -100;
        Instant transactionDate = pastDate;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionForCreation(amount, transactionDate);
        });
        
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }
    
    @Test
    void testValidateTransactionForCreation_InvalidDate() {
        // Given
        Integer amount = 10000;
        Instant transactionDate = futureDate;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionForCreation(amount, transactionDate);
        });
        
        assertEquals("Transaction date cannot be in the future", exception.getMessage());
    }
    
    @Test
    void testValidateTransactionForCreation_NullAmount() {
        // Given
        Integer amount = null;
        Instant transactionDate = pastDate;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionForCreation(amount, transactionDate);
        });
        
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }
    
    @Test
    void testValidateTransactionForCreation_NullDate() {
        // Given
        Integer amount = 10000;
        Instant transactionDate = null;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            transactionBusinessValidator.validateTransactionForCreation(amount, transactionDate);
        });
        
        assertEquals("Transaction date cannot be null", exception.getMessage());
    }
}
