package alvarado.ms.transaction.transactions.service.impl;

import alvarado.ms.transaction.exception.TransactionNotFoundException;
import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import alvarado.ms.transaction.transactions.controller.mapper.TransactionMapper;
import alvarado.ms.transaction.transactions.domain.entity.Transaction;
import alvarado.ms.transaction.transactions.domain.entity.TransactionAuditLog;
import alvarado.ms.transaction.transactions.repository.TransactionAuditLogRepository;
import alvarado.ms.transaction.transactions.repository.TransactionRepository;
import alvarado.ms.transaction.transactions.repository.TopBusinessProjection;
import alvarado.ms.transaction.transactions.service.TransactionService;
import alvarado.ms.transaction.transactions.service.validator.BusinessValidator;
import alvarado.ms.transaction.transactions.service.validator.UserValidator;
import alvarado.ms.transaction.transactions.service.validator.TransactionBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    
    private static final Integer CONTROL_THRESHOLD = 100000;
    
    private final TransactionRepository transactionRepository;
    private final TransactionAuditLogRepository auditLogRepository;
    private final TransactionMapper mapper;
    private final UserValidator userValidator;
    private final BusinessValidator businessValidator;
    private final TransactionBusinessValidator transactionBusinessValidator;
    
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> findAll(Pageable pageable, String search) {
        return transactionRepository.findAllActive(search, pageable)
                .map(mapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO findById(Integer id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapper.toResponseDTO(transaction);
    }
    
    @Override
    @Transactional
    public TransactionResponseDTO create(TransactionCreateDTO dto) {
        // Validaciones de negocio
        transactionBusinessValidator.validateTransactionForCreation(
                dto.getAmount(), dto.getTransactionDate());
        
        // Validar user y business
        var user = userValidator.validateAndGetUser(dto.getUserId());
        var business = businessValidator.validateAndGetBusiness(dto.getBusinessId());
        
        // Crear entidad
        Transaction transaction = mapper.toEntity(dto);
        transaction.setUser(user);
        transaction.setBusiness(business);
        
        // Guardar
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Registrar en audit log
        registerAuditLog(savedTransaction.getId(), "CREATE", null, null);
        
        return mapper.toResponseDTO(savedTransaction);
    }
    
    @Override
    @Transactional
    public TransactionResponseDTO update(Integer id, TransactionUpdateDTO dto) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        
        // Validar y actualizar campos
        if (dto.getAmount() != null) {
            transactionBusinessValidator.validateAmount(dto.getAmount());
            transaction.setAmount(dto.getAmount());
        }
        
        if (dto.getTransactionDate() != null) {
            transactionBusinessValidator.validateTransactionDate(dto.getTransactionDate());
            transaction.setTransactionDate(dto.getTransactionDate());
        }
        
        if (dto.getUserId() != null) {
            var user = userValidator.validateAndGetUser(dto.getUserId());
            transaction.setUser(user);
        }
        
        if (dto.getBusinessId() != null) {
            var business = businessValidator.validateAndGetBusiness(dto.getBusinessId());
            transaction.setBusiness(business);
        }
        
        if (dto.getDescription() != null) {
            transaction.setDescription(dto.getDescription());
        }
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Registrar en audit log
        registerAuditLog(updatedTransaction.getId(), "UPDATE", null, null);
        
        return mapper.toResponseDTO(updatedTransaction);
    }
    
    @Override
    @Transactional
    public void delete(Integer id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        
        // Soft delete
        LocalDateTime now = LocalDateTime.now();
        transactionRepository.softDelete(id, now, null);
        
        // Registrar en audit log
        registerAuditLog(id, "DELETE", null, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findByUserId(Integer userId) {
        return transactionRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findByBusinessId(Integer businessId) {
        return transactionRepository.findByBusinessIdAndDeletedFalse(businessId).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public TransactionStatsDTO getStatistics() {
        // Obtener volumen (SUM de amount)
        Long volumen = transactionRepository.getTotalVolume();
        if (volumen == null) {
            volumen = 0L;
        }
        
        // Obtener foco (AVG de amount)
        Double foco = transactionRepository.getAverageTicket();
        if (foco == null) {
            foco = 0.0;
        }
        
        // Obtener control (COUNT de transacciones > umbral)
        Long control = transactionRepository.countTransactionsAboveThreshold(CONTROL_THRESHOLD);
        if (control == null) {
            control = 0L;
        }
        
        // Obtener hábito (comercio top)
        TopBusinessProjection topBusiness = transactionRepository.findTopBusinessByTransactionCount();
        TransactionStatsDTO.TopBusinessDTO habito = null;
        
        if (topBusiness != null) {
            habito = TransactionStatsDTO.TopBusinessDTO.builder()
                    .businessId(topBusiness.getBusinessId())
                    .businessName(topBusiness.getBusinessName())
                    .transactionCount(topBusiness.getTransactionCount())
                    .build();
        }
        
        return TransactionStatsDTO.builder()
                .volumen(volumen)
                .foco(foco)
                .control(control)
                .habito(habito)
                .build();
    }
    
    private void registerAuditLog(Integer transactionId, String action, Integer performedBy, String metadata) {
        TransactionAuditLog auditLog = TransactionAuditLog.builder()
                .transactionId(transactionId)
                .action(action)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .metadata(metadata)
                .build();
        auditLogRepository.save(auditLog);
    }
}
