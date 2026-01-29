package alvarado.ms.transaction.users.service.impl;

import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.controller.mapper.UserMapper;
import alvarado.ms.transaction.users.repository.UserRepository;
import alvarado.ms.transaction.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserListItemDTO> findAll() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(mapper::toListItemDTO)
                .collect(Collectors.toList());
    }
}
