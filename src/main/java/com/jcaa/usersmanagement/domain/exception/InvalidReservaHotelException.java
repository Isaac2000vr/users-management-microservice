package com.jcaa.usersmanagement.domain.exception;

public class InvalidReservaHotelException extends DomainException {

  private InvalidReservaHotelException(final String message) {
    super(message);
  }

  public static InvalidReservaHotelException becauseFieldIsBlank(final String fieldName) {
    return new InvalidReservaHotelException(
        "El campo '" + fieldName + "' de la reserva no puede estar vacío.");
  }

  public static InvalidReservaHotelException becauseValorIsNegative(final double valor) {
    return new InvalidReservaHotelException(
        "El valor de la reserva no puede ser negativo. Valor recibido: " + valor);
  }

  public static InvalidReservaHotelException becauseAcompanantesIsNegative(final int n) {
    return new InvalidReservaHotelException(
        "El número de acompañantes no puede ser negativo. Valor recibido: " + n);
  }

  public static InvalidReservaHotelException becauseFechaFinBeforeFechaInicio() {
    return new InvalidReservaHotelException(
        "La fecha de fin no puede ser anterior a la fecha de inicio.");
  }

  public static InvalidReservaHotelException becauseStatusIsInvalid(final String status) {
    return new InvalidReservaHotelException(
        "El estado de la reserva no es válido: " + status);
  }
}
