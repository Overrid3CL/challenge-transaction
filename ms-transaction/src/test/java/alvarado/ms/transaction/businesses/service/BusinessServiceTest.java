package alvarado.ms.transaction.businesses.service;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.controller.mapper.BusinessMapper;
import alvarado.ms.transaction.businesses.domain.entity.Business;
import alvarado.ms.transaction.businesses.repository.BusinessRepository;
import alvarado.ms.transaction.businesses.service.impl.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private BusinessMapper mapper;

    @InjectMocks
    private BusinessServiceImpl businessService;

    private Business testBusiness;
    private BusinessListItemDTO testDTO;

    @BeforeEach
    void setUp() {
        testBusiness = Business.builder()
                .id(1)
                .name("Test Business")
                .categoryId(1)
                .deleted(false)
                .build();
        testDTO = BusinessListItemDTO.builder()
                .id(1)
                .name("Test Business")
                .build();
    }

    @Test
    void findAll_returnsMappedList() {
        // Given
        List<Business> businesses = Arrays.asList(testBusiness);
        when(businessRepository.findAllByDeletedFalse()).thenReturn(businesses);
        when(mapper.toListItemDTO(any(Business.class))).thenReturn(testDTO);

        // When
        List<BusinessListItemDTO> result = businessService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test Business", result.get(0).getName());
        verify(businessRepository).findAllByDeletedFalse();
        verify(mapper, times(1)).toListItemDTO(testBusiness);
    }

    @Test
    void findAll_emptyList() {
        // Given
        when(businessRepository.findAllByDeletedFalse()).thenReturn(Collections.emptyList());

        // When
        List<BusinessListItemDTO> result = businessService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(businessRepository).findAllByDeletedFalse();
        verify(mapper, never()).toListItemDTO(any(Business.class));
    }
}
