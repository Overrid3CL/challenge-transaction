package alvarado.ms.transaction.users.service.impl;

import alvarado.ms.transaction.exception.BusinessRuleViolationException;
import alvarado.ms.transaction.users.controller.dto.UserCreateDTO;
import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.controller.dto.UserUpdateDTO;
import alvarado.ms.transaction.users.controller.mapper.UserMapper;
import alvarado.ms.transaction.users.domain.entity.User;
import alvarado.ms.transaction.users.repository.UserRepository;
import alvarado.ms.transaction.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /** Valor que acepta la BD: CHECK (user_type IN ('USER', 'ADMIN')) */
    public static final String USER_TYPE_DEFAULT = "USER";

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Integer id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessRuleViolationException("User with ID " + id + " not found or has been deleted"));
        return mapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        String email = dto.getEmail() != null ? dto.getEmail().trim() : null;
        if (email == null || email.isBlank()) {
            throw new BusinessRuleViolationException("Email is required");
        }
        if (userRepository.findByEmailAndDeletedFalse(email).isPresent()) {
            throw new BusinessRuleViolationException("Email already exists");
        }
        String userType = normalizeUserType(
                dto.getUserType() != null && !dto.getUserType().isBlank() ? dto.getUserType().trim() : null);
        User entity = mapper.toEntity(dto);
        entity.setUserType(userType);
        User saved = userRepository.save(entity);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Integer id, UserUpdateDTO dto) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessRuleViolationException("User with ID " + id + " not found or has been deleted"));
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            String email = dto.getEmail().trim();
            userRepository.findByEmailAndDeletedFalseExcludingId(email, id)
                    .ifPresent(u -> {
                        throw new BusinessRuleViolationException("Email already exists");
                    });
        }
        mapper.updateEntityFromDTO(user, dto);
        if (dto.getUserType() != null) {
            user.setUserType(normalizeUserType(user.getUserType()));
        }
        User saved = userRepository.save(user);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessRuleViolationException("User with ID " + id + " not found or has been deleted"));
        LocalDateTime now = LocalDateTime.now();
        int updated = userRepository.softDelete(id, now);
        if (updated == 0) {
            throw new BusinessRuleViolationException("User with ID " + id + " could not be deleted");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserListItemDTO> findAllAsListItem() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(mapper::toListItemDTO)
                .collect(Collectors.toList());
    }

    /** Normaliza userType a los valores permitidos por la BD: USER o ADMIN. */
    private static String normalizeUserType(String value) {
        if (value == null || value.isBlank()) return USER_TYPE_DEFAULT;
        String upper = value.trim().toUpperCase();
        return "ADMIN".equals(upper) ? "ADMIN" : "USER";
    }
}
