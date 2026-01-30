package alvarado.ms.transaction.users.controller;

import alvarado.ms.transaction.users.controller.dto.UserCreateDTO;
import alvarado.ms.transaction.users.controller.dto.UserResponseDTO;
import alvarado.ms.transaction.users.controller.dto.UserUpdateDTO;
import alvarado.ms.transaction.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "API CRUD de usuarios")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Obtiene una lista de todos los usuarios con información completa (id, name, email, phone, userType, createdAt, updatedAt)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Obtiene un usuario por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado o eliminado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Integer id) {
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario. userType por defecto: User."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza un usuario existente. Todos los campos del body son opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado, datos inválidos o email ya existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO updated = userService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario (soft delete)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado o ya eliminado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
