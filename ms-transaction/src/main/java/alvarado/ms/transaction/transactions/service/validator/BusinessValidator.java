package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.businesses.domain.entity.Business;
import alvarado.ms.transaction.businesses.repository.BusinessRepository;
import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessValidator {
    
    private final BusinessRepository businessRepository;
    
    public Business validateAndGetBusiness(Integer businessId) {
        if (businessId == null) {
            throw new BusinessRuleViolationException("Business ID cannot be null");
        }
        
        return businessRepository.findByIdAndDeletedFalse(businessId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        "Business with ID " + businessId + " not found or has been deleted"));
    }
}
