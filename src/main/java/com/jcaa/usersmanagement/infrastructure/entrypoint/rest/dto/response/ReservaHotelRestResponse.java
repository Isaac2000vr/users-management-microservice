package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

public record ReservaHotelRestResponse(
    String id,
    String fecha,
    String hotel,
    String huesped,
    String fechaInicio,
    String fechaFin,
    Double valor,
    String habitacion,
    Integer numAcompanantes,
    String pais,
    String departamento,
    String ciudad,
    String horaCheckin,
    String horaCheckout,
    String empleadoAtiende,
    String empleadoDespide,
    String descripcion,
    String status) {}
