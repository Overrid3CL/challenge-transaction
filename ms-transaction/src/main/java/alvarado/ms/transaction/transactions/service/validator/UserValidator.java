package alvarado.ms.transaction.transactions.service.validator;

import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import alvarado.ms.transaction.users.domain.entity.User;
import alvarado.ms.transaction.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    
    private final UserRepository userRepository;
    
    public User validateAndGetUser(Integer userId) {
        if (userId == null) {
            throw new BusinessRuleViolationException("User ID cannot be null");
        }
        
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        "User with ID " + userId + " not found or has been deleted"));
    }
}
