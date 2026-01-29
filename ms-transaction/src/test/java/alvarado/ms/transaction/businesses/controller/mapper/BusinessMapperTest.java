package alvarado.ms.transaction.businesses.controller.mapper;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.domain.entity.Business;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessMapperTest {

    private final BusinessMapper mapper = new BusinessMapper();

    @Test
    void toListItemDTO_returnsIdAndName() {
        // Given
        Business entity = Business.builder()
                .id(1)
                .name("Test Business")
                .categoryId(1)
                .deleted(false)
                .build();

        // When
        BusinessListItemDTO result = mapper.toListItemDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Business", result.getName());
    }

    @Test
    void toListItemDTO_withNullName() {
        // Given: name can be null (e.g. legacy data); mapper should not NPE
        Business entity = Business.builder()
                .id(2)
                .name(null)
                .categoryId(1)
                .deleted(false)
                .build();

        // When
        BusinessListItemDTO result = mapper.toListItemDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertNull(result.getName());
    }
}
