package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ReservaHotelRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contrato OpenAPI / Swagger para {@link ReservaHotelRestController}.
 *
 * <p>Toda la documentación de la API (anotaciones {@code @Operation}, {@code @ApiResponses}, etc.)
 * vive aquí para mantener el controlador limpio y enfocado únicamente en la lógica de despacho.
 */
@Tag(name = "Reservas de hotel", description = "Gestión de reservas: crear, consultar, actualizar y eliminar.")
public interface ReservaHotelRestControllerDocs {

  // ─────────────────────────────────────────────────────────────────────────────
  // POST /api/reservas-hotel
  // ─────────────────────────────────────────────────────────────────────────────

  @Operation(
      summary = "Crear reserva de hotel",
      description =
          "Registra una nueva reserva de hotel en el sistema. "
              + "El ID debe ser único y la reserva inicia en estado PENDING.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Reserva creada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservaHotelRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos (campos requeridos vacíos o formato incorrecto).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  ReservaHotelRestResponse create(@Valid @RequestBody CreateReservaHotelRestRequest request);

  // ─────────────────────────────────────────────────────────────────────────────
  // GET /api/reservas-hotel
  // ─────────────────────────────────────────────────────────────────────────────

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Listar todas las reservas de hotel",
      description =
          "Retorna la lista completa de reservas ordenada por fecha de inicio "
              + "de la más reciente a la más antigua.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de reservas obtenida exitosamente. Puede ser vacía.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ReservaHotelRestResponse.class)))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @SuppressWarnings("unused") // Invocado por Spring MVC y leído por springdoc mediante reflexión.
  List<ReservaHotelRestResponse> getAll();

  // ─────────────────────────────────────────────────────────────────────────────
  // GET /api/reservas-hotel/{id}
  // ─────────────────────────────────────────────────────────────────────────────

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Obtener reserva de hotel por ID",
      description = "Retorna los datos de una reserva específica identificada por su ID único.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Reserva encontrada.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservaHotelRestResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna reserva con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  ReservaHotelRestResponse getById(
      @Parameter(description = "ID único de la reserva.", example = "res-001", required = true)
          @PathVariable
          String id);

  // ─────────────────────────────────────────────────────────────────────────────
  // PUT /api/reservas-hotel/{id}
  // ─────────────────────────────────────────────────────────────────────────────

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Actualizar reserva de hotel",
      description =
          "Actualiza los datos de una reserva existente. "
              + "El estado debe ser uno de: PENDING, CONFIRMED, CANCELLED o COMPLETED.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Reserva actualizada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservaHotelRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna reserva con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  ReservaHotelRestResponse update(
      @Parameter(description = "ID único de la reserva a actualizar.", example = "res-001", required = true)
          @PathVariable
          String id,
      @Valid @RequestBody UpdateReservaHotelRestRequest request);

  // ─────────────────────────────────────────────────────────────────────────────
  // DELETE /api/reservas-hotel/{id}
  // ─────────────────────────────────────────────────────────────────────────────

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Eliminar reserva de hotel",
      description = "Elimina permanentemente una reserva del sistema por su ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente."),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna reserva con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  void delete(
      @Parameter(description = "ID único de la reserva a eliminar.", example = "res-001", required = true)
          @PathVariable
          String id);
}
