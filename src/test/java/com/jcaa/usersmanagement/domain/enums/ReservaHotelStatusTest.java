package com.jcaa.usersmanagement.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidReservaHotelException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ReservaHotelStatus.
 *
 * <p>Cubre: fromString() con valores válidos (incluyendo case-insensitive) e inválidos.
 */
@DisplayName("ReservaHotelStatus")
class ReservaHotelStatusTest {

  @Test
  @DisplayName("fromString() resuelve cada estado por su nombre")
  void shouldResolveAllStatusNames() {
    // Act + Assert
    assertEquals(ReservaHotelStatus.PENDING, ReservaHotelStatus.fromString("PENDING"));
    assertEquals(ReservaHotelStatus.CONFIRMED, ReservaHotelStatus.fromString("CONFIRMED"));
    assertEquals(ReservaHotelStatus.CANCELLED, ReservaHotelStatus.fromString("CANCELLED"));
    assertEquals(ReservaHotelStatus.COMPLETED, ReservaHotelStatus.fromString("COMPLETED"));
  }

  @Test
  @DisplayName("fromString() ignora mayúsculas y minúsculas")
  void shouldResolveStatusIgnoringCase() {
    // Act + Assert
    assertEquals(ReservaHotelStatus.CONFIRMED, ReservaHotelStatus.fromString("confirmed"));
    assertEquals(ReservaHotelStatus.CANCELLED, ReservaHotelStatus.fromString("Cancelled"));
  }

  @Test
  @DisplayName("fromString() lanza InvalidReservaHotelException con un estado inválido")
  void shouldThrowWhenStatusIsInvalid() {
    // Act + Assert
    assertThrows(
        InvalidReservaHotelException.class,
        () -> ReservaHotelStatus.fromString("ESTADO_INEXISTENTE"),
        "debe rechazar un estado que no existe");
  }
}
