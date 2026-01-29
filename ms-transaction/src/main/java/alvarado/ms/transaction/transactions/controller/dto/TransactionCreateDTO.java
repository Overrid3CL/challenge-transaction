package alvarado.ms.transaction.transactions.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Schema(description = "DTO para crear una nueva transacción")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateDTO {
    
    @Schema(description = "ID del usuario que realiza la transacción", example = "1", required = true)
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @Schema(description = "ID del negocio donde se realiza la transacción", example = "1", required = true)
    @NotNull(message = "Business ID is required")
    private Integer businessId;
    
    @Schema(description = "Monto de la transacción", example = "45000", minimum = "1", required = true)
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be positive")
    private Integer amount;
    
    @Schema(description = "Fecha y hora de la transacción (ISO-8601 UTC)", example = "2026-01-30T01:40:00.000Z", required = true)
    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private Instant transactionDate;
    
    @Schema(description = "Descripción opcional de la transacción", example = "Monthly supermarket purchase", required = false)
    private String description;
}
