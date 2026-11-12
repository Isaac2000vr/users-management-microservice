package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ReservaHotelModel.
 *
 * <p>Sólo se testean los métodos con lógica real: {@code create()} que fija el estado inicial
 * PENDING, y las transiciones {@code confirm()}, {@code cancel()} y {@code complete()}. El
 * constructor trivial y los getters quedan cubiertos como efecto secundario de estos tests.
 */
@DisplayName("ReservaHotelModel")
class ReservaHotelModelTest {

  private ReservaHotelId reservaId;

  @BeforeEach
  void setUp() {
    reservaId = new ReservaHotelId("res-001");
  }

  // ── create()

  @Test
  @DisplayName("create() debe fijar status PENDING y preservar todos los campos recibidos")
  void shouldCreateReservaWithPendingStatusAndPreserveAllFields() {
    // Act
    final ReservaHotelModel model =
        ReservaHotelModel.create(
            reservaId,
            "2026-11-10",
            "Hotel Cartagena",
            "Juan Perez",
            "2026-12-01",
            "2026-12-05",
            450000.0,
            "301",
            2,
            "Colombia",
            "Bolívar",
            "Cartagena",
            "15:00",
            "12:00",
            "Maria Lopez",
            "Carlos Ruiz",
            "Reserva con vista al mar");

    // Assert
    assertAll(
        "create() factory",
        () ->
            assertEquals(
                ReservaHotelStatus.PENDING, model.getStatus(), "status debe iniciar como PENDING"),
        () -> assertSame(reservaId, model.getId(), "id debe preservarse"),
        () -> assertEquals("Hotel Cartagena", model.getHotel(), "hotel"),
        () -> assertEquals("Juan Perez", model.getHuesped(), "huesped"),
        () -> assertEquals(450000.0, model.getValor(), "valor"),
        () -> assertEquals(2, model.getNumAcompanantes(), "numAcompanantes"));
  }

  // ── confirm()

  @Test
  @DisplayName("confirm() debe retornar nueva instancia con CONFIRMED y demás campos intactos")
  void shouldConfirmAndPreserveOtherFields() {
    // Arrange
    final ReservaHotelModel pending = buildSampleModel();

    // Act
    final ReservaHotelModel confirmed = pending.confirm();

    // Assert
    assertAll(
        "resultado de confirm()",
        () -> assertNotSame(pending, confirmed, "debe ser una nueva instancia"),
        () ->
            assertEquals(
                ReservaHotelStatus.CONFIRMED, confirmed.getStatus(), "status debe ser CONFIRMED"),
        () -> assertSame(reservaId, confirmed.getId(), "id debe preservarse"),
        () -> assertEquals("Hotel Cartagena", confirmed.getHotel(), "hotel debe preservarse"),
        () -> assertEquals(450000.0, confirmed.getValor(), "valor debe preservarse"));
  }

  // ── cancel()

  @Test
  @DisplayName("cancel() debe retornar nueva instancia con CANCELLED y demás campos intactos")
  void shouldCancelAndPreserveOtherFields() {
    // Arrange
    final ReservaHotelModel confirmed = buildSampleModel().confirm();

    // Act
    final ReservaHotelModel cancelled = confirmed.cancel();

    // Assert
    assertAll(
        "resultado de cancel()",
        () -> assertNotSame(confirmed, cancelled, "debe ser una nueva instancia"),
        () ->
            assertEquals(
                ReservaHotelStatus.CANCELLED, cancelled.getStatus(), "status debe ser CANCELLED"),
        () -> assertSame(reservaId, cancelled.getId(), "id debe preservarse"));
  }

  // ── complete()

  @Test
  @DisplayName("complete() debe retornar nueva instancia con COMPLETED y demás campos intactos")
  void shouldCompleteAndPreserveOtherFields() {
    // Arrange
    final ReservaHotelModel confirmed = buildSampleModel().confirm();

    // Act
    final ReservaHotelModel completed = confirmed.complete();

    // Assert
    assertAll(
        "resultado de complete()",
        () -> assertNotSame(confirmed, completed, "debe ser una nueva instancia"),
        () ->
            assertEquals(
                ReservaHotelStatus.COMPLETED, completed.getStatus(), "status debe ser COMPLETED"),
        () -> assertSame(reservaId, completed.getId(), "id debe preservarse"),
        () -> assertEquals("Juan Perez", completed.getHuesped(), "huesped debe preservarse"));
  }

  // ── helper

  private ReservaHotelModel buildSampleModel() {
    return ReservaHotelModel.create(
        reservaId,
        "2026-11-10",
        "Hotel Cartagena",
        "Juan Perez",
        "2026-12-01",
        "2026-12-05",
        450000.0,
        "301",
        2,
        "Colombia",
        "Bolívar",
        "Cartagena",
        "15:00",
        "12:00",
        "Maria Lopez",
        "Carlos Ruiz",
        null);
  }
}
