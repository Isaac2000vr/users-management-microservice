package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record ReservaHotelEntity(
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
    String status,
    String createdAt,
    String updatedAt) {}
