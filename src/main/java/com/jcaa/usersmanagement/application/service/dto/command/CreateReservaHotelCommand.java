package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateReservaHotelCommand(
    @NotBlank(message = "id must not be blank") String id,
    @NotBlank(message = "fecha must not be blank") String fecha,
    @NotBlank(message = "hotel must not be blank") String hotel,
    @NotBlank(message = "huesped must not be blank") String huesped,
    @NotBlank(message = "fechaInicio must not be blank") String fechaInicio,
    @NotBlank(message = "fechaFin must not be blank") String fechaFin,
    @NotNull(message = "valor must not be null") @PositiveOrZero(message = "valor must be zero or positive") Double valor,
    @NotBlank(message = "habitacion must not be blank") String habitacion,
    @NotNull(message = "numAcompanantes must not be null") @Min(value = 0, message = "numAcompanantes must be zero or positive") Integer numAcompanantes,
    @NotBlank(message = "pais must not be blank") String pais,
    @NotBlank(message = "departamento must not be blank") String departamento,
    @NotBlank(message = "ciudad must not be blank") String ciudad,
    @NotBlank(message = "horaCheckin must not be blank") String horaCheckin,
    @NotBlank(message = "horaCheckout must not be blank") String horaCheckout,
    @NotBlank(message = "empleadoAtiende must not be blank") String empleadoAtiende,
    @NotBlank(message = "empleadoDespide must not be blank") String empleadoDespide,
    String descripcion)
{}
