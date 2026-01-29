package alvarado.ms.transaction.transactions.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Schema(description = "DTO para actualizar una transacción existente. Todos los campos son opcionales.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateDTO {
    
    @Schema(description = "ID del usuario que realiza la transacción (opcional)", example = "1", required = false)
    private Integer userId;
    
    @Schema(description = "ID del negocio donde se realiza la transacción (opcional)", example = "1", required = false)
    private Integer businessId;
    
    @Schema(description = "Monto de la transacción (opcional)", example = "45000", minimum = "1", required = false)
    @Min(value = 1, message = "Amount must be positive")
    private Integer amount;
    
    @Schema(description = "Fecha y hora de la transacción (opcional, ISO-8601 UTC)", example = "2026-01-30T01:40:00.000Z", required = false)
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private Instant transactionDate;
    
    @Schema(description = "Descripción de la transacción (opcional)", example = "Monthly supermarket purchase", required = false)
    private String description;
}
