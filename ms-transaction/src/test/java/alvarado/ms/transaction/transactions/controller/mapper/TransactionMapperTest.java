package alvarado.ms.transaction.transactions.controller.mapper;

import alvarado.ms.transaction.businesses.domain.entity.Business;
import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import alvarado.ms.transaction.transactions.domain.entity.Transaction;
import alvarado.ms.transaction.users.domain.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = new TransactionMapper();

    private static final Instant TX_DATE = Instant.parse("2025-01-15T10:00:00Z");
    private static final LocalDateTime CREATED = LocalDateTime.of(2025, 1, 14, 9, 0, 0);
    private static final LocalDateTime UPDATED = LocalDateTime.of(2025, 1, 14, 10, 0, 0);

    @Test
    void toEntity_mapsCreateDTO() {
        // Given
        TransactionCreateDTO dto = TransactionCreateDTO.builder()
                .userId(1)
                .businessId(1)
                .amount(10000)
                .transactionDate(TX_DATE)
                .description("Test transaction")
                .build();

        // When
        Transaction result = mapper.toEntity(dto);

        // Then
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getUser());
        assertNull(result.getBusiness());
        assertEquals(10000, result.getAmount());
        assertEquals(TX_DATE, result.getTransactionDate());
        assertEquals("Test transaction", result.getDescription());
    }

    @Test
    void toResponseDTO_withUserAndBusiness() {
        // Given
        User user = User.builder().id(1).name("Test User").email("a@b.com").userType("USER").deleted(false).build();
        Business business = Business.builder().id(1).name("Test Business").categoryId(1).deleted(false).build();
        Transaction entity = Transaction.builder()
                .id(1)
                .user(user)
                .business(business)
                .amount(10000)
                .transactionDate(TX_DATE)
                .description("Test transaction")
                .createdAt(CREATED)
                .updatedAt(UPDATED)
                .deleted(false)
                .build();

        // When
        TransactionResponseDTO result = mapper.toResponseDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals("Test User", result.getUserName());
        assertEquals(1, result.getBusinessId());
        assertEquals("Test Business", result.getBusinessName());
        assertEquals(10000, result.getAmount());
        assertEquals(TX_DATE, result.getTransactionDate());
        assertEquals("Test transaction", result.getDescription());
        assertEquals(CREATED, result.getCreatedAt());
        assertEquals(UPDATED, result.getUpdatedAt());
    }

    @Test
    void toResponseDTO_withNullUserAndBusiness() {
        // Given: mapper handles null user/business (e.g. detached or partial entity)
        Transaction entity = Transaction.builder()
                .id(2)
                .user(null)
                .business(null)
                .amount(5000)
                .transactionDate(TX_DATE)
                .description("Partial")
                .createdAt(CREATED)
                .updatedAt(UPDATED)
                .deleted(false)
                .build();

        // When
        TransactionResponseDTO result = mapper.toResponseDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertNull(result.getUserId());
        assertNull(result.getUserName());
        assertNull(result.getBusinessId());
        assertNull(result.getBusinessName());
        assertEquals(5000, result.getAmount());
        assertEquals(TX_DATE, result.getTransactionDate());
        assertEquals("Partial", result.getDescription());
        assertEquals(CREATED, result.getCreatedAt());
        assertEquals(UPDATED, result.getUpdatedAt());
    }

    @Test
    void updateEntityFromDTO_partialUpdate() {
        // Given
        Transaction entity = Transaction.builder()
                .id(1)
                .amount(10000)
                .transactionDate(TX_DATE)
                .description("Original")
                .build();
        TransactionUpdateDTO dto = TransactionUpdateDTO.builder()
                .amount(20000)
                .build();

        // When
        mapper.updateEntityFromDTO(entity, dto);

        // Then
        assertEquals(20000, entity.getAmount());
        assertEquals(TX_DATE, entity.getTransactionDate());
        assertEquals("Original", entity.getDescription());
    }

    @Test
    void updateEntityFromDTO_allFields() {
        // Given
        Instant newDate = Instant.parse("2025-02-01T12:00:00Z");
        Transaction entity = Transaction.builder()
                .id(1)
                .amount(10000)
                .transactionDate(TX_DATE)
                .description("Original")
                .build();
        TransactionUpdateDTO dto = TransactionUpdateDTO.builder()
                .amount(30000)
                .transactionDate(newDate)
                .description("Updated")
                .build();

        // When
        mapper.updateEntityFromDTO(entity, dto);

        // Then
        assertEquals(30000, entity.getAmount());
        assertEquals(newDate, entity.getTransactionDate());
        assertEquals("Updated", entity.getDescription());
    }
}
