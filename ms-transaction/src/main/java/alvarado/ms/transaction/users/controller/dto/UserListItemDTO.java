package alvarado.ms.transaction.users.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de ítem de listado de usuario (id, name)")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListItemDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Perez Gonzalez")
    private String name;
}
