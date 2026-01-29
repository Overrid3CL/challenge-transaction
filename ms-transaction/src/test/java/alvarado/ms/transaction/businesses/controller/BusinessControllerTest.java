package alvarado.ms.transaction.businesses.controller;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.service.BusinessService;
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

@WebMvcTest(BusinessController.class)
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessService businessService;

    @Test
    void getAllBusinesses_success() throws Exception {
        // Given
        BusinessListItemDTO dto = BusinessListItemDTO.builder()
                .id(1)
                .name("Test Business")
                .build();
        List<BusinessListItemDTO> businesses = Arrays.asList(dto);
        when(businessService.findAll()).thenReturn(businesses);

        // When & Then
        mockMvc.perform(get("/business"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Business"));

        verify(businessService).findAll();
    }

    @Test
    void getAllBusinesses_emptyList() throws Exception {
        // Given
        when(businessService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/business"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(businessService).findAll();
    }
}
