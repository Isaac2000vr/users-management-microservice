package com.jcaa.usersmanagement.domain.exception;

public class ReservaHotelNotFoundException extends DomainException {

  private ReservaHotelNotFoundException(final String message) {
    super(message);
  }

  public static ReservaHotelNotFoundException becauseIdWasNotFound(final String id) {
    return new ReservaHotelNotFoundException(
        "No se encontró una reserva con el id: " + id);
  }
}
