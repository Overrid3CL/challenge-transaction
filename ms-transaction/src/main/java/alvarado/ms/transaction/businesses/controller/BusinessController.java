package alvarado.ms.transaction.businesses.controller;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Negocios", description = "API para listado de negocios")
@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @Operation(
            summary = "Listar todos los negocios",
            description = "Obtiene una lista de todos los negocios disponibles (id, name)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de negocios obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = BusinessListItemDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<BusinessListItemDTO>> getAllBusinesses() {
        List<BusinessListItemDTO> businesses = businessService.findAll();
        return ResponseEntity.ok(businesses);
    }
}
