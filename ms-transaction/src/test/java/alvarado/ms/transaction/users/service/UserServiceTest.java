package alvarado.ms.transaction.users.service;

import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.controller.mapper.UserMapper;
import alvarado.ms.transaction.users.domain.entity.User;
import alvarado.ms.transaction.users.repository.UserRepository;
import alvarado.ms.transaction.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserResponseDTO testResponseDTO;
    private UserListItemDTO testListItemDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .name("Test User")
                .email("test@email.com")
                .userType("USER")
                .deleted(false)
                .build();
        testResponseDTO = UserResponseDTO.builder()
                .id(1)
                .name("Test User")
                .email("test@email.com")
                .userType("USER")
                .build();
        testListItemDTO = UserListItemDTO.builder()
                .id(1)
                .name("Test User")
                .build();
    }

    @Test
    void findAll_returnsMappedList() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAllByDeletedFalse()).thenReturn(users);
        when(mapper.toResponseDTO(any(User.class))).thenReturn(testResponseDTO);

        // When
        List<UserResponseDTO> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test User", result.get(0).getName());
        verify(userRepository).findAllByDeletedFalse();
        verify(mapper, times(1)).toResponseDTO(testUser);
    }

    @Test
    void findAll_emptyList() {
        // Given
        when(userRepository.findAllByDeletedFalse()).thenReturn(Collections.emptyList());

        // When
        List<UserResponseDTO> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAllByDeletedFalse();
        verify(mapper, never()).toResponseDTO(any(User.class));
    }

    @Test
    void findAllAsListItem_returnsMappedList() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAllByDeletedFalse()).thenReturn(users);
        when(mapper.toListItemDTO(any(User.class))).thenReturn(testListItemDTO);

        // When
        List<UserListItemDTO> result = userService.findAllAsListItem();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test User", result.get(0).getName());
        verify(userRepository).findAllByDeletedFalse();
        verify(mapper, times(1)).toListItemDTO(testUser);
    }
}
