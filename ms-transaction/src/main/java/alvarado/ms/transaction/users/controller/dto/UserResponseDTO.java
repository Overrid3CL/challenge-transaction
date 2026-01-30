package alvarado.ms.transaction.users.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "DTO de respuesta con los datos completos de un usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Perez Gonzalez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String email;

    @Schema(description = "Teléfono del usuario (opcional)", example = "+56912345678")
    private String phone;

    @Schema(description = "Tipo de usuario", example = "User")
    private String userType;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-01-20T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización del registro", example = "2025-01-20T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
