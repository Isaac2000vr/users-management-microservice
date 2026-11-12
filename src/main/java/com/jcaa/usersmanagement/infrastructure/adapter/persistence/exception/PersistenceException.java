package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_EMAIL = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE = "Failed to delete user with ID: '%s'.";
  private static final String MESSAGE_CONNECTION = "Could not establish database connection.";
  private static final String MESSAGE_SAVE_RESERVA = "Failed to save hotel booking with ID: '%s'.";
  private static final String MESSAGE_UPDATE_RESERVA =
      "Failed to update hotel booking with ID: '%s'.";
  private static final String MESSAGE_FIND_RESERVA =
      "Failed to find hotel booking with ID: '%s'.";
  private static final String MESSAGE_ALL_RESERVAS = "Failed to retrieve all hotel bookings.";
  private static final String MESSAGE_DELETE_RESERVA =
      "Failed to delete hotel booking with ID: '%s'.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMAIL, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE, userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_CONNECTION, cause);
  }

  public static PersistenceException becauseSaveReservaHotelFailed(
      final String reservaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE_RESERVA, reservaId), cause);
  }

  public static PersistenceException becauseUpdateReservaHotelFailed(
      final String reservaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE_RESERVA, reservaId), cause);
  }

  public static PersistenceException becauseFindReservaHotelByIdFailed(
      final String reservaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND_RESERVA, reservaId), cause);
  }

  public static PersistenceException becauseFindAllReservasHotelFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL_RESERVAS, cause);
  }

  public static PersistenceException becauseDeleteReservaHotelFailed(
      final String reservaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE_RESERVA, reservaId), cause);
  }
}
