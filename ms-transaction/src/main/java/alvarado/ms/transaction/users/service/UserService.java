package alvarado.ms.transaction.users.service;

import alvarado.ms.transaction.users.controller.dto.UserCreateDTO;
import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.controller.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Integer id);

    UserResponseDTO create(UserCreateDTO dto);

    UserResponseDTO update(Integer id, UserUpdateDTO dto);

    void delete(Integer id);

    /**
     * Lista usuarios como ítem (id, name) para combos u otros usos.
     */
    List<UserListItemDTO> findAllAsListItem();
}
