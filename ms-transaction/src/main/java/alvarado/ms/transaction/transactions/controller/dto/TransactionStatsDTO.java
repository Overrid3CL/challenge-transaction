package alvarado.ms.transaction.transactions.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de respuesta con estadísticas agregadas de transacciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatsDTO {
    
    @Schema(description = "Volumen: Total transaccionado (SUM de amount)", example = "1500000")
    private Long volumen;
    
    @Schema(description = "Foco: Ticket promedio (AVG de amount)", example = "12500.50")
    private Double foco;
    
    @Schema(description = "Control: Cantidad de transacciones con monto mayor al umbral", example = "45")
    private Long control;
    
    @Schema(description = "Hábito: Comercio top con más transacciones")
    private TopBusinessDTO habito;
    
    @Schema(description = "DTO para el comercio top")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopBusinessDTO {
        
        @Schema(description = "ID del negocio", example = "3")
        private Integer businessId;
        
        @Schema(description = "Nombre del negocio", example = "Supermercado XYZ")
        private String businessName;
        
        @Schema(description = "Cantidad de transacciones en este negocio", example = "120")
        private Long transactionCount;
    }
}
