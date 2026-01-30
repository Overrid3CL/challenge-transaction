package alvarado.ms.transaction.users.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para crear un nuevo usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    @Schema(description = "Nombre completo del usuario", example = "Juan Perez Gonzalez", required = true)
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @Schema(description = "Correo electrónico del usuario (único)", example = "juan@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;

    @Schema(description = "Teléfono del usuario (opcional)", example = "+56912345678", required = false)
    @Size(max = 20)
    private String phone;

    @Schema(description = "Tipo de usuario", example = "User", required = true)
    @NotNull(message = "User type is required")
    @Size(max = 20)
    private String userType;
}
