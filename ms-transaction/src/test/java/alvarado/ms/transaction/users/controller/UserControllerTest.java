package alvarado.ms.transaction.users.controller;

import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers_success() throws Exception {
        // Given
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(1)
                .name("Test User")
                .email("test@example.com")
                .userType("USER")
                .build();
        List<UserResponseDTO> users = Arrays.asList(dto);
        when(userService.findAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService).findAll();
    }

    @Test
    void getAllUsers_emptyList() throws Exception {
        // Given
        when(userService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).findAll();
    }
}
