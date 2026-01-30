package alvarado.ms.transaction.users.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para actualizar un usuario existente. Todos los campos son opcionales.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Schema(description = "Nombre completo del usuario (opcional)", example = "Juan Perez Gonzalez", required = false)
    @Size(max = 100)
    private String name;

    @Schema(description = "Correo electrónico del usuario (opcional, único)", example = "juan@example.com", required = false)
    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;

    @Schema(description = "Teléfono del usuario (opcional)", example = "+56912345678", required = false)
    @Size(max = 20)
    private String phone;

    @Schema(description = "Tipo de usuario (opcional)", example = "User", required = false)
    @Size(max = 20)
    private String userType;
}
