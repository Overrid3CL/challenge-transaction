package alvarado.ms.transaction.transactions.controller;

import alvarado.ms.transaction.transactions.controller.dto.TransactionCreateDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionResponseDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionStatsDTO;
import alvarado.ms.transaction.transactions.controller.dto.TransactionUpdateDTO;
import alvarado.ms.transaction.transactions.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Transacciones", description = "API para gestión de transacciones")
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT = "transactionDate,desc";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "userName", "businessName", "amount", "transactionDate", "description");
    private static final Map<String, String> SORT_FIELD_TO_JPA = Map.of("userName", "user.name", "businessName", "business.name");

    private final TransactionService transactionService;

    @Operation(
            summary = "Listar transacciones (paginado)",
            description = "Obtiene transacciones con paginación, ordenamiento y búsqueda. " +
                    "Params: page (default 0), size (default 20, max 100), sort (ej. amount,desc), search (usuario, comercio, descripción)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Página de transacciones obtenida exitosamente"
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getAllTransactions(
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Orden: campo,dirección (ej. amount,desc)", example = "transactionDate,desc")
            @RequestParam(required = false) String sort,
            @Parameter(description = "Búsqueda por usuario, comercio o descripción")
            @RequestParam(required = false) String search) {

        int p = page != null && page >= 0 ? page : DEFAULT_PAGE;
        int s = size != null && size >= 1 && size <= MAX_SIZE ? size : DEFAULT_SIZE;
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(p, s, sortObj);
        String searchTrimmed = (search != null && !search.isBlank()) ? search.trim() : null;

        Page<TransactionResponseDTO> result = transactionService.findAll(pageable, searchTrimmed);
        return ResponseEntity.ok(result);
    }

    private Sort parseSort(String sortParam) {
        String s = (sortParam != null && !sortParam.isBlank()) ? sortParam.trim() : DEFAULT_SORT;
        String[] parts = s.split(",", 2);
        String field = parts[0].trim();
        Sort.Direction dir = Sort.Direction.DESC;
        if (parts.length > 1) {
            String d = parts[1].trim().toLowerCase();
            if ("asc".equals(d)) dir = Sort.Direction.ASC;
        }
        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            field = "transactionDate";
            dir = Sort.Direction.DESC;
        }
        String jpaProperty = SORT_FIELD_TO_JPA.getOrDefault(field, field);
        return Sort.by(dir, jpaProperty);
    }
    
    @Operation(
            summary = "Obtener estadísticas de transacciones",
            description = "Retorna estadísticas agregadas: volumen total transaccionado, ticket promedio, " +
                         "cantidad de transacciones sobre umbral, y comercio top por cantidad de transacciones"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionStatsDTO.class))
            )
    })
    @GetMapping("/stats")
    public ResponseEntity<TransactionStatsDTO> getStatistics() {
        TransactionStatsDTO stats = transactionService.getStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @Operation(
            summary = "Obtener transacción por ID",
            description = "Obtiene los detalles de una transacción específica mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transacción encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transacción no encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @Parameter(description = "ID de la transacción", required = true, example = "1")
            @PathVariable Integer id) {
        TransactionResponseDTO transaction = transactionService.findById(id);
        return ResponseEntity.ok(transaction);
    }
    
    @Operation(
            summary = "Crear nueva transacción",
            description = "Crea una nueva transacción con los datos proporcionados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transacción creada exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la transacción a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionCreateDTO.class))
            )
            @Valid @RequestBody TransactionCreateDTO dto) {
        TransactionResponseDTO created = transactionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @Operation(
            summary = "Actualizar transacción",
            description = "Actualiza una transacción existente. Todos los campos son opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transacción actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transacción no encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @Parameter(description = "ID de la transacción a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la transacción a actualizar (todos los campos son opcionales)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionUpdateDTO.class))
            )
            @Valid @RequestBody TransactionUpdateDTO dto) {
        TransactionResponseDTO updated = transactionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @Operation(
            summary = "Eliminar transacción",
            description = "Elimina una transacción mediante su ID (soft delete)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Transacción eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transacción no encontrada"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID de la transacción a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
            summary = "Obtener transacciones por usuario",
            description = "Obtiene todas las transacciones asociadas a un usuario específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de transacciones del usuario obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUser(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer userId) {
        List<TransactionResponseDTO> transactions = transactionService.findByUserId(userId);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(
            summary = "Obtener transacciones por negocio",
            description = "Obtiene todas las transacciones asociadas a un negocio específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de transacciones del negocio obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            )
    })
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByBusiness(
            @Parameter(description = "ID del negocio", required = true, example = "1")
            @PathVariable Integer businessId) {
        List<TransactionResponseDTO> transactions = transactionService.findByBusinessId(businessId);
        return ResponseEntity.ok(transactions);
    }
}
