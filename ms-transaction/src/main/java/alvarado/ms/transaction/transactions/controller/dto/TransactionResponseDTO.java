package alvarado.ms.transaction.transactions.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Schema(description = "DTO de respuesta con los datos completos de una transacción")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    
    @Schema(description = "ID único de la transacción", example = "1")
    private Integer id;
    
    @Schema(description = "ID del usuario que realiza la transacción", example = "1")
    private Integer userId;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Perez Gonzalez")
    private String userName;
    
    @Schema(description = "ID del negocio donde se realiza la transacción", example = "1")
    private Integer businessId;
    
    @Schema(description = "Nombre del negocio", example = "Supermercado Lider")
    private String businessName;
    
    @Schema(description = "Monto de la transacción", example = "45000")
    private Integer amount;
    
    @Schema(description = "Fecha y hora de la transacción (ISO-8601 UTC)", example = "2026-01-30T01:40:00.000Z")
    private Instant transactionDate;
    
    @Schema(description = "Descripción de la transacción", example = "Monthly supermarket purchase")
    private String description;
    
    @Schema(description = "Fecha y hora de creación del registro", example = "2025-01-20T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Schema(description = "Fecha y hora de última actualización del registro", example = "2025-01-20T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
