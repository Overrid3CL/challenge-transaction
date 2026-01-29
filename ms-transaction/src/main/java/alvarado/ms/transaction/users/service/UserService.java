package alvarado.ms.transaction.users.service;

import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;

import java.util.List;

public interface UserService {

    List<UserListItemDTO> findAll();
}
