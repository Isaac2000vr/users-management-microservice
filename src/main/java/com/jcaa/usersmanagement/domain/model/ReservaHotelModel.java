package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import lombok.Value;

@Value
public class ReservaHotelModel {

  ReservaHotelId id;
  String fecha;
  String hotel;
  String huesped;
  String fechaInicio;
  String fechaFin;
  Double valor;
  String habitacion;
  Integer numAcompanantes;
  String pais;
  String departamento;
  String ciudad;
  String horaCheckin;
  String horaCheckout;
  String empleadoAtiende;
  String empleadoDespide;
  String descripcion;
  ReservaHotelStatus status;

  public static ReservaHotelModel create(
      final ReservaHotelId id,
      final String fecha,
      final String hotel,
      final String huesped,
      final String fechaInicio,
      final String fechaFin,
      final Double valor,
      final String habitacion,
      final Integer numAcompanantes,
      final String pais,
      final String departamento,
      final String ciudad,
      final String horaCheckin,
      final String horaCheckout,
      final String empleadoAtiende,
      final String empleadoDespide,
      final String descripcion) {
    return new ReservaHotelModel(
        id, fecha, hotel, huesped, fechaInicio, fechaFin,
        valor, habitacion, numAcompanantes, pais, departamento, ciudad,
        horaCheckin, horaCheckout, empleadoAtiende, empleadoDespide,
        descripcion, ReservaHotelStatus.PENDING);
  }

  public ReservaHotelModel confirm() {
    return new ReservaHotelModel(
        id, fecha, hotel, huesped, fechaInicio, fechaFin,
        valor, habitacion, numAcompanantes, pais, departamento, ciudad,
        horaCheckin, horaCheckout, empleadoAtiende, empleadoDespide,
        descripcion, ReservaHotelStatus.CONFIRMED);
  }

  public ReservaHotelModel cancel() {
    return new ReservaHotelModel(
        id, fecha, hotel, huesped, fechaInicio, fechaFin,
        valor, habitacion, numAcompanantes, pais, departamento, ciudad,
        horaCheckin, horaCheckout, empleadoAtiende, empleadoDespide,
        descripcion, ReservaHotelStatus.CANCELLED);
  }

  public ReservaHotelModel complete() {
    return new ReservaHotelModel(
        id, fecha, hotel, huesped, fechaInicio, fechaFin,
        valor, habitacion, numAcompanantes, pais, departamento, ciudad,
        horaCheckin, horaCheckout, empleadoAtiende, empleadoDespide,
        descripcion, ReservaHotelStatus.COMPLETED);
  }
}
