package alvarado.ms.transaction.transactions.service;

import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;

import java.util.List;

public interface TransactionService {
    
    List<TransactionResponseDTO> findAll();
    
    TransactionResponseDTO findById(Integer id);
    
    TransactionResponseDTO create(TransactionCreateDTO dto);
    
    TransactionResponseDTO update(Integer id, TransactionUpdateDTO dto);
    
    void delete(Integer id);
    
    List<TransactionResponseDTO> findByUserId(Integer userId);
    
    List<TransactionResponseDTO> findByBusinessId(Integer businessId);
    
    TransactionStatsDTO getStatistics();
}
