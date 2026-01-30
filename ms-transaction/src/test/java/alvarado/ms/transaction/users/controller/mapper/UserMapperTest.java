package alvarado.ms.transaction.users.controller.mapper;

import alvarado.ms.transaction.users.controller.dto.UserListItemDTO;
import alvarado.ms.transaction.users.domain.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toListItemDTO_returnsIdAndName() {
        // Given
        User entity = User.builder()
                .id(1)
                .name("Test User")
                .email("test@email.com")
                .userType("USER")   
                .deleted(false)
                .build();

        // When
        UserListItemDTO result = mapper.toListItemDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void toListItemDTO_withNullName() {
        // Given: name can be null; mapper should not NPE
        User entity = User.builder()
                .id(2)
                .name(null)
                .email("other@email.com")
                .userType("USER")
                .deleted(false)
                .build();

        // When
        UserListItemDTO result = mapper.toListItemDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertNull(result.getName());
    }
}
