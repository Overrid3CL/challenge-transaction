package alvarado.ms.transaction.users.controller.mapper;

import alvarado.ms.transaction.users.controller.dto.UserCreateDTO;
import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.controller.dto.UserUpdateDTO;
import alvarado.ms.transaction.users.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserListItemDTO toListItemDTO(User entity) {
        return UserListItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public UserResponseDTO toResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .userType(entity.getUserType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public User toEntity(UserCreateDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail() != null ? dto.getEmail().trim() : null)
                .phone(dto.getPhone() != null && !dto.getPhone().isBlank() ? dto.getPhone().trim() : null)
                .userType(dto.getUserType() != null ? dto.getUserType().trim() : "USER")
                .build();
    }

    public void updateEntityFromDTO(User entity, UserUpdateDTO dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName().trim());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail().trim());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone().trim().isEmpty() ? null : dto.getPhone().trim());
        }
        if (dto.getUserType() != null) {
            String v = dto.getUserType().trim();
            entity.setUserType("ADMIN".equalsIgnoreCase(v) ? "ADMIN" : "USER");
        }
    }
}
