package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.businesses.domain.entity.Business;
import alvarado.ms.transaction.businesses.repository.BusinessRepository;
import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessValidatorTest {
    
    @Mock
    private BusinessRepository businessRepository;
    
    @InjectMocks
    private BusinessValidator businessValidator;
    
    private Business testBusiness;
    
    @BeforeEach
    void setUp() {
        testBusiness = Business.builder()
                .id(1)
                .name("Test Business")
                .categoryId(1)
                .deleted(false)
                .build();
    }
    
    @Test
    void testValidateAndGetBusiness_Success() {
        // Given
        Integer businessId = 1;
        when(businessRepository.findByIdAndDeletedFalse(businessId)).thenReturn(Optional.of(testBusiness));
        
        // When
        Business result = businessValidator.validateAndGetBusiness(businessId);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Business", result.getName());
        assertEquals(1, result.getCategoryId());
        verify(businessRepository).findByIdAndDeletedFalse(businessId);
    }
    
    @Test
    void testValidateAndGetBusiness_NullId() {
        // Given
        Integer businessId = null;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            businessValidator.validateAndGetBusiness(businessId);
        });
        
        assertEquals("Business ID cannot be null", exception.getMessage());
        verify(businessRepository, never()).findByIdAndDeletedFalse(any());
    }
    
    @Test
    void testValidateAndGetBusiness_NotFound() {
        // Given
        Integer businessId = 999;
        when(businessRepository.findByIdAndDeletedFalse(businessId)).thenReturn(Optional.empty());
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            businessValidator.validateAndGetBusiness(businessId);
        });
        
        assertEquals("Business with ID 999 not found or has been deleted", exception.getMessage());
        verify(businessRepository).findByIdAndDeletedFalse(businessId);
    }
    
    @Test
    void testValidateAndGetBusiness_Deleted() {
        // Given
        Integer businessId = 2;
        when(businessRepository.findByIdAndDeletedFalse(businessId)).thenReturn(Optional.empty());
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            businessValidator.validateAndGetBusiness(businessId);
        });
        
        assertEquals("Business with ID 2 not found or has been deleted", exception.getMessage());
        verify(businessRepository).findByIdAndDeletedFalse(businessId);
    }
}
