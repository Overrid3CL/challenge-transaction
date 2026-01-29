package alvarado.ms.transaction.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "DTO de respuesta para errores de la API")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    
    @Schema(description = "Fecha y hora en que ocurrió el error", example = "2025-01-20T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP", example = "404")
    private Integer status;
    
    @Schema(description = "Tipo de error", example = "Not Found")
    private String error;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Transaction with ID 999 not found")
    private String message;
    
    @Schema(description = "Ruta de la petición que causó el error", example = "/transaction/999")
    private String path;
    
    @Schema(description = "Mapa de errores de validación (solo presente en errores 400 con validaciones)", example = "{\"userId\": \"User ID is required\"}")
    private Map<String, String> validationErrors;
}
