package alvarado.ms.transaction.businesses.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de ítem de listado de negocio (id, name)")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessListItemDTO {

    @Schema(description = "ID único del negocio", example = "1")
    private Integer id;

    @Schema(description = "Nombre del negocio", example = "Supermercado Lider")
    private String name;
}
