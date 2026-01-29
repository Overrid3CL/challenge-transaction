package alvarado.ms.transaction.users.controller.mapper;

import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
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
}
