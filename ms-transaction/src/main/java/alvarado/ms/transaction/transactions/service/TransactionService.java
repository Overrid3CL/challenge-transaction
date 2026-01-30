package alvarado.ms.transaction.transactions.service;

import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Page<TransactionResponseDTO> findAll(Pageable pageable, String search);
    
    TransactionResponseDTO findById(Integer id);
    
    TransactionResponseDTO create(TransactionCreateDTO dto);
    
    TransactionResponseDTO update(Integer id, TransactionUpdateDTO dto);
    
    void delete(Integer id);
    
    List<TransactionResponseDTO> findByUserId(Integer userId);
    
    List<TransactionResponseDTO> findByBusinessId(Integer businessId);
    
    TransactionStatsDTO getStatistics();
}
