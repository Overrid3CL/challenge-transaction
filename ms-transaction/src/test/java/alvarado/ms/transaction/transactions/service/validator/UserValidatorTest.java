package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import alvarado.ms.transaction.users.domain.entity.User;
import alvarado.ms.transaction.users.repository.UserRepository;
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
class UserValidatorTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserValidator userValidator;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .name("Test User")
                .email("test@email.com")
                .userType("USER")
                .deleted(false)
                .build();
    }
    
    @Test
    void testValidateAndGetUser_Success() {
        // Given
        Integer userId = 1;
        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(testUser));
        
        // When
        User result = userValidator.validateAndGetUser(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@email.com", result.getEmail());
        verify(userRepository).findByIdAndDeletedFalse(userId);
    }
    
    @Test
    void testValidateAndGetUser_NullId() {
        // Given
        Integer userId = null;
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            userValidator.validateAndGetUser(userId);
        });
        
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(userRepository, never()).findByIdAndDeletedFalse(any());
    }
    
    @Test
    void testValidateAndGetUser_NotFound() {
        // Given
        Integer userId = 999;
        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.empty());
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            userValidator.validateAndGetUser(userId);
        });
        
        assertEquals("User with ID 999 not found or has been deleted", exception.getMessage());
        verify(userRepository).findByIdAndDeletedFalse(userId);
    }
    
    @Test
    void testValidateAndGetUser_Deleted() {
        // Given
        Integer userId = 2;
        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.empty());
        
        // When & Then
        BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class, () -> {
            userValidator.validateAndGetUser(userId);
        });
        
        assertEquals("User with ID 2 not found or has been deleted", exception.getMessage());
        verify(userRepository).findByIdAndDeletedFalse(userId);
    }
}
