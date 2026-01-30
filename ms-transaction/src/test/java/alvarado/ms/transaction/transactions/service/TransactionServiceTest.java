package alvarado.ms.transaction.transactions.service;

import alvarado.ms.transaction.businesses.domain.entity.Business;
import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import alvarado.ms.transaction.exception.TransactionNotFoundException;
import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import alvarado.ms.transaction.transactions.controller.mapper.TransactionMapper;
import alvarado.ms.transaction.transactions.domain.entity.Transaction;
import alvarado.ms.transaction.transactions.repository.TransactionAuditLogRepository;
import alvarado.ms.transaction.transactions.repository.TransactionRepository;
import alvarado.ms.transaction.transactions.repository.TopBusinessProjection;
import alvarado.ms.transaction.transactions.service.impl.TransactionServiceImpl;
import alvarado.ms.transaction.transactions.service.validator.BusinessValidator;
import alvarado.ms.transaction.transactions.service.validator.UserValidator;
import alvarado.ms.transaction.transactions.service.validator.TransactionBusinessValidator;
import alvarado.ms.transaction.users.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private TransactionAuditLogRepository auditLogRepository;
    
    @Mock
    private TransactionMapper mapper;
    
    @Mock
    private UserValidator userValidator;
    
    @Mock
    private BusinessValidator businessValidator;
    
    @Mock
    private TransactionBusinessValidator transactionBusinessValidator;
    
    @InjectMocks
    private TransactionServiceImpl transactionService;
    
    private User testUser;
    private Business testBusiness;
    private Transaction testTransaction;
    private TransactionCreateDTO createDTO;
    private TransactionResponseDTO responseDTO;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .name("Test User")
                .email("test@email.com")
                .userType("USER")
                .deleted(false)
                .build();
        
        testBusiness = Business.builder()
                .id(1)
                .name("Test Business")
                .categoryId(1)
                .deleted(false)
                .build();
        
        testTransaction = Transaction.builder()
                .id(1)
                .user(testUser)
                .business(testBusiness)
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .deleted(false)
                .build();
        
        createDTO = TransactionCreateDTO.builder()
                .userId(1)
                .businessId(1)
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
    }
    
    @Test
    void testFindAll_Success() {
        // Given
        List<Transaction> transactions = Arrays.asList(testTransaction);
        Page<Transaction> page = new PageImpl<>(transactions, PageRequest.of(0, 20), 1);
        when(transactionRepository.findAllActive(eq(null), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponseDTO(any(Transaction.class))).thenReturn(responseDTO);

        // When
        Page<TransactionResponseDTO> result = transactionService.findAll(PageRequest.of(0, 20), null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository).findAllActive(eq(null), any(Pageable.class));
        verify(mapper, times(1)).toResponseDTO(any(Transaction.class));
    }

    @Test
    void testFindAll_WithSearch() {
        List<Transaction> transactions = Arrays.asList(testTransaction);
        Page<Transaction> page = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);
        when(transactionRepository.findAllActive(eq("test"), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponseDTO(any(Transaction.class))).thenReturn(responseDTO);

        Page<TransactionResponseDTO> result = transactionService.findAll(PageRequest.of(0, 10), "test");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(transactionRepository).findAllActive(eq("test"), any(Pageable.class));
    }
    
    @Test
    void testFindById_Success() {
        // Given
        when(transactionRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testTransaction));
        when(mapper.toResponseDTO(testTransaction)).thenReturn(responseDTO);
        
        // When
        TransactionResponseDTO result = transactionService.findById(1);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(transactionRepository).findByIdAndDeletedFalse(1);
        verify(mapper).toResponseDTO(testTransaction);
    }
    
    @Test
    void testFindById_NotFound() {
        // Given
        when(transactionRepository.findByIdAndDeletedFalse(999)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.findById(999);
        });
        verify(transactionRepository).findByIdAndDeletedFalse(999);
    }
    
    @Test
    void testCreate_Success() {
        // Given
        doNothing().when(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        when(userValidator.validateAndGetUser(1)).thenReturn(testUser);
        when(businessValidator.validateAndGetBusiness(1)).thenReturn(testBusiness);
        when(mapper.toEntity(createDTO)).thenReturn(testTransaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(mapper.toResponseDTO(testTransaction)).thenReturn(responseDTO);
        
        // When
        TransactionResponseDTO result = transactionService.create(createDTO);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        verify(userValidator).validateAndGetUser(1);
        verify(businessValidator).validateAndGetBusiness(1);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogRepository).save(any());
    }
    
    @Test
    void testCreate_WithInvalidUser() {
        // Given
        createDTO.setUserId(999); // Cambiar el userId para que coincida con el mock
        doNothing().when(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        when(userValidator.validateAndGetUser(999))
                .thenThrow(new BusinessRuleViolationException("User not found"));
        
        // When & Then
        assertThrows(BusinessRuleViolationException.class, () -> {
            transactionService.create(createDTO);
        });
        verify(transactionRepository, never()).save(any());
    }
    
    @Test
    void testCreate_WithInvalidBusiness() {
        // Given
        createDTO.setBusinessId(999); // Cambiar el businessId para que coincida con el mock
        doNothing().when(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        when(userValidator.validateAndGetUser(1)).thenReturn(testUser);
        when(businessValidator.validateAndGetBusiness(999))
                .thenThrow(new BusinessRuleViolationException("Business not found"));
        
        // When & Then
        assertThrows(BusinessRuleViolationException.class, () -> {
            transactionService.create(createDTO);
        });
        verify(transactionRepository, never()).save(any());
    }
    
    @Test
    void testCreate_WithNegativeAmount() {
        // Given
        createDTO.setAmount(-100);
        doThrow(new BusinessRuleViolationException("Amount must be positive"))
                .when(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        
        // When & Then
        assertThrows(BusinessRuleViolationException.class, () -> {
            transactionService.create(createDTO);
        });
        verify(transactionRepository, never()).save(any());
    }
    
    @Test
    void testCreate_WithFutureDate() {
        // Given
        createDTO.setTransactionDate(Instant.now().plus(1, ChronoUnit.DAYS));
        doThrow(new BusinessRuleViolationException("Transaction date cannot be in the future"))
                .when(transactionBusinessValidator).validateTransactionForCreation(anyInt(), any(Instant.class));
        
        // When & Then
        assertThrows(BusinessRuleViolationException.class, () -> {
            transactionService.create(createDTO);
        });
        verify(transactionRepository, never()).save(any());
    }
    
    @Test
    void testUpdate_Success() {
        // Given
        TransactionUpdateDTO updateDTO = TransactionUpdateDTO.builder()
                .amount(20000)
                .description("Updated description")
                .build();
        
        when(transactionRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testTransaction));
        doNothing().when(transactionBusinessValidator).validateAmount(anyInt());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(mapper.toResponseDTO(testTransaction)).thenReturn(responseDTO);
        
        // When
        TransactionResponseDTO result = transactionService.update(1, updateDTO);
        
        // Then
        assertNotNull(result);
        verify(transactionRepository).findByIdAndDeletedFalse(1);
        verify(transactionBusinessValidator).validateAmount(20000);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogRepository).save(any());
    }
    
    @Test
    void testUpdate_NotFound() {
        // Given
        TransactionUpdateDTO updateDTO = TransactionUpdateDTO.builder().amount(20000).build();
        when(transactionRepository.findByIdAndDeletedFalse(999)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.update(999, updateDTO);
        });
        verify(transactionRepository, never()).save(any());
    }
    
    @Test
    void testDelete_Success() {
        // Given
        when(transactionRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.softDelete(anyInt(), any(LocalDateTime.class), any())).thenReturn(1);
        
        // When
        transactionService.delete(1);
        
        // Then
        verify(transactionRepository).findByIdAndDeletedFalse(1);
        verify(transactionRepository).softDelete(anyInt(), any(LocalDateTime.class), any());
        verify(auditLogRepository).save(any());
    }
    
    @Test
    void testDelete_NotFound() {
        // Given
        when(transactionRepository.findByIdAndDeletedFalse(999)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.delete(999);
        });
        verify(transactionRepository, never()).softDelete(anyInt(), any(), any());
    }
    
    @Test
    void testFindByUserId_Success() {
        // Given
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionRepository.findByUserIdAndDeletedFalse(1)).thenReturn(transactions);
        when(mapper.toResponseDTO(any(Transaction.class))).thenReturn(responseDTO);
        
        // When
        List<TransactionResponseDTO> result = transactionService.findByUserId(1);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository).findByUserIdAndDeletedFalse(1);
    }
    
    @Test
    void testFindByBusinessId_Success() {
        // Given
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionRepository.findByBusinessIdAndDeletedFalse(1)).thenReturn(transactions);
        when(mapper.toResponseDTO(any(Transaction.class))).thenReturn(responseDTO);
        
        // When
        List<TransactionResponseDTO> result = transactionService.findByBusinessId(1);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository).findByBusinessIdAndDeletedFalse(1);
    }
    
    @Test
    void testGetStatistics_Success() {
        // Given
        Long expectedVolumen = 1500000L;
        Double expectedFoco = 12500.50;
        Long expectedControl = 45L;
        
        TopBusinessProjection topBusinessProjection = new TopBusinessProjection() {
            @Override
            public Integer getBusinessId() {
                return 3;
            }
            
            @Override
            public String getBusinessName() {
                return "Supermercado XYZ";
            }
            
            @Override
            public Long getTransactionCount() {
                return 120L;
            }
        };
        
        when(transactionRepository.getTotalVolume()).thenReturn(expectedVolumen);
        when(transactionRepository.getAverageTicket()).thenReturn(expectedFoco);
        when(transactionRepository.countTransactionsAboveThreshold(100000)).thenReturn(expectedControl);
        when(transactionRepository.findTopBusinessByTransactionCount()).thenReturn(topBusinessProjection);
        
        // When
        TransactionStatsDTO result = transactionService.getStatistics();
        
        // Then
        assertNotNull(result);
        assertEquals(expectedVolumen, result.getVolumen());
        assertEquals(expectedFoco, result.getFoco());
        assertEquals(expectedControl, result.getControl());
        assertNotNull(result.getHabito());
        assertEquals(3, result.getHabito().getBusinessId());
        assertEquals("Supermercado XYZ", result.getHabito().getBusinessName());
        assertEquals(120L, result.getHabito().getTransactionCount());
        
        verify(transactionRepository).getTotalVolume();
        verify(transactionRepository).getAverageTicket();
        verify(transactionRepository).countTransactionsAboveThreshold(100000);
        verify(transactionRepository).findTopBusinessByTransactionCount();
    }
    
    @Test
    void testGetStatistics_WithNullValues() {
        // Given
        when(transactionRepository.getTotalVolume()).thenReturn(null);
        when(transactionRepository.getAverageTicket()).thenReturn(null);
        when(transactionRepository.countTransactionsAboveThreshold(100000)).thenReturn(null);
        when(transactionRepository.findTopBusinessByTransactionCount()).thenReturn(null);
        
        // When
        TransactionStatsDTO result = transactionService.getStatistics();
        
        // Then
        assertNotNull(result);
        assertEquals(0L, result.getVolumen());
        assertEquals(0.0, result.getFoco());
        assertEquals(0L, result.getControl());
        assertNull(result.getHabito());
        
        verify(transactionRepository).getTotalVolume();
        verify(transactionRepository).getAverageTicket();
        verify(transactionRepository).countTransactionsAboveThreshold(100000);
        verify(transactionRepository).findTopBusinessByTransactionCount();
    }
    
    @Test
    void testGetStatistics_WithZeroValues() {
        // Given
        when(transactionRepository.getTotalVolume()).thenReturn(0L);
        when(transactionRepository.getAverageTicket()).thenReturn(0.0);
        when(transactionRepository.countTransactionsAboveThreshold(100000)).thenReturn(0L);
        when(transactionRepository.findTopBusinessByTransactionCount()).thenReturn(null);
        
        // When
        TransactionStatsDTO result = transactionService.getStatistics();
        
        // Then
        assertNotNull(result);
        assertEquals(0L, result.getVolumen());
        assertEquals(0.0, result.getFoco());
        assertEquals(0L, result.getControl());
        assertNull(result.getHabito());
        
        verify(transactionRepository).getTotalVolume();
        verify(transactionRepository).getAverageTicket();
        verify(transactionRepository).countTransactionsAboveThreshold(100000);
        verify(transactionRepository).findTopBusinessByTransactionCount();
    }
}
