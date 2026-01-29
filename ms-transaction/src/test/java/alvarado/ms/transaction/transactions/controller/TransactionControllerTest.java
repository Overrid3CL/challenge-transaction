package alvarado.ms.transaction.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import alvarado.ms.transaction.exception.TransactionNotFoundException;
import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import alvarado.ms.transaction.transactions.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TransactionService transactionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetAllTransactions_Success() throws Exception {
        // Given
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        List<TransactionResponseDTO> transactions = Arrays.asList(responseDTO);
        when(transactionService.findAll()).thenReturn(transactions);
        
        // When & Then
        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(10000));
        
        verify(transactionService).findAll();
    }
    
    @Test
    void testGetTransactionById_Success() throws Exception {
        // Given
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        when(transactionService.findById(1)).thenReturn(responseDTO);
        
        // When & Then
        mockMvc.perform(get("/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(10000));
        
        verify(transactionService).findById(1);
    }
    
    @Test
    void testGetTransactionById_NotFound() throws Exception {
        // Given
        when(transactionService.findById(999))
                .thenThrow(new TransactionNotFoundException(999));
        
        // When & Then
        mockMvc.perform(get("/transaction/999"))
                .andExpect(status().isNotFound());
        
        verify(transactionService).findById(999);
    }
    
    @Test
    void testCreateTransaction_Success() throws Exception {
        // Given
        TransactionCreateDTO createDTO = TransactionCreateDTO.builder()
                .userId(1)
                .businessId(1)
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        when(transactionService.create(any(TransactionCreateDTO.class))).thenReturn(responseDTO);
        
        // When & Then
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(10000));
        
        verify(transactionService).create(any(TransactionCreateDTO.class));
    }
    
    @Test
    void testCreateTransaction_ValidationError() throws Exception {
        // Given
        TransactionCreateDTO createDTO = TransactionCreateDTO.builder()
                .userId(null) // Invalid: null userId
                .businessId(1)
                .amount(-100) // Invalid: negative amount
                .transactionDate(Instant.now().plus(1, ChronoUnit.DAYS)) // Invalid: future date
                .build();
        
        // When & Then
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
        
        verify(transactionService, never()).create(any());
    }
    
    @Test
    void testUpdateTransaction_Success() throws Exception {
        // Given
        TransactionUpdateDTO updateDTO = TransactionUpdateDTO.builder()
                .amount(20000)
                .description("Updated description")
                .build();
        
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(20000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Updated description")
                .build();
        
        when(transactionService.update(anyInt(), any(TransactionUpdateDTO.class))).thenReturn(responseDTO);
        
        // When & Then
        mockMvc.perform(put("/transaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(20000))
                .andExpect(jsonPath("$.description").value("Updated description"));
        
        verify(transactionService).update(anyInt(), any(TransactionUpdateDTO.class));
    }
    
    @Test
    void testUpdateTransaction_NotFound() throws Exception {
        // Given
        TransactionUpdateDTO updateDTO = TransactionUpdateDTO.builder()
                .amount(20000)
                .build();
        
        when(transactionService.update(anyInt(), any(TransactionUpdateDTO.class)))
                .thenThrow(new TransactionNotFoundException(999));
        
        // When & Then
        mockMvc.perform(put("/transaction/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
        
        verify(transactionService).update(anyInt(), any(TransactionUpdateDTO.class));
    }
    
    @Test
    void testDeleteTransaction_Success() throws Exception {
        // Given
        doNothing().when(transactionService).delete(1);
        
        // When & Then
        mockMvc.perform(delete("/transaction/1"))
                .andExpect(status().isNoContent());
        
        verify(transactionService).delete(1);
    }
    
    @Test
    void testDeleteTransaction_NotFound() throws Exception {
        // Given
        doThrow(new TransactionNotFoundException(999)).when(transactionService).delete(999);
        
        // When & Then
        mockMvc.perform(delete("/transaction/999"))
                .andExpect(status().isNotFound());
        
        verify(transactionService).delete(999);
    }
    
    @Test
    void testGetTransactionsByUser_Success() throws Exception {
        // Given
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        List<TransactionResponseDTO> transactions = Arrays.asList(responseDTO);
        when(transactionService.findByUserId(1)).thenReturn(transactions);
        
        // When & Then
        mockMvc.perform(get("/transaction/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1));
        
        verify(transactionService).findByUserId(1);
    }
    
    @Test
    void testGetTransactionsByBusiness_Success() throws Exception {
        // Given
        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .id(1)
                .userId(1)
                .userName("Test User")
                .businessId(1)
                .businessName("Test Business")
                .amount(10000)
                .transactionDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .description("Test transaction")
                .build();
        
        List<TransactionResponseDTO> transactions = Arrays.asList(responseDTO);
        when(transactionService.findByBusinessId(1)).thenReturn(transactions);
        
        // When & Then
        mockMvc.perform(get("/transaction/business/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].businessId").value(1));
        
        verify(transactionService).findByBusinessId(1);
    }
    
    @Test
    void testGetStatistics_Success() throws Exception {
        // Given
        TransactionStatsDTO.TopBusinessDTO topBusiness = TransactionStatsDTO.TopBusinessDTO.builder()
                .businessId(3)
                .businessName("Supermercado XYZ")
                .transactionCount(120L)
                .build();
        
        TransactionStatsDTO statsDTO = TransactionStatsDTO.builder()
                .volumen(1500000L)
                .foco(12500.50)
                .control(45L)
                .habito(topBusiness)
                .build();
        
        when(transactionService.getStatistics()).thenReturn(statsDTO);
        
        // When & Then
        mockMvc.perform(get("/transaction/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.volumen").value(1500000))
                .andExpect(jsonPath("$.foco").value(12500.50))
                .andExpect(jsonPath("$.control").value(45))
                .andExpect(jsonPath("$.habito.businessId").value(3))
                .andExpect(jsonPath("$.habito.businessName").value("Supermercado XYZ"))
                .andExpect(jsonPath("$.habito.transactionCount").value(120));
        
        verify(transactionService).getStatistics();
    }
    
    @Test
    void testGetStatistics_WithNullHabito() throws Exception {
        // Given
        TransactionStatsDTO statsDTO = TransactionStatsDTO.builder()
                .volumen(500000L)
                .foco(10000.0)
                .control(10L)
                .habito(null)
                .build();
        
        when(transactionService.getStatistics()).thenReturn(statsDTO);
        
        // When & Then
        mockMvc.perform(get("/transaction/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.volumen").value(500000))
                .andExpect(jsonPath("$.foco").value(10000.0))
                .andExpect(jsonPath("$.control").value(10))
                .andExpect(jsonPath("$.habito").isEmpty());
        
        verify(transactionService).getStatistics();
    }
    
    @Test
    void testGetStatistics_WithZeroValues() throws Exception {
        // Given
        TransactionStatsDTO statsDTO = TransactionStatsDTO.builder()
                .volumen(0L)
                .foco(0.0)
                .control(0L)
                .habito(null)
                .build();
        
        when(transactionService.getStatistics()).thenReturn(statsDTO);
        
        // When & Then
        mockMvc.perform(get("/transaction/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.volumen").value(0))
                .andExpect(jsonPath("$.foco").value(0.0))
                .andExpect(jsonPath("$.control").value(0))
                .andExpect(jsonPath("$.habito").isEmpty());
        
        verify(transactionService).getStatistics();
    }
}
