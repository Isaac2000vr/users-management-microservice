package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidReservaHotelException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ReservaHotelId.
 *
 * <p>Cubre: valor válido y rechazo de valores nulos o en blanco.
 */
@DisplayName("ReservaHotelId")
class ReservaHotelIdTest {

  @Test
  @DisplayName("acepta un id con contenido y expone su valor")
  void shouldAcceptNonBlankValue() {
    // Act
    final ReservaHotelId id = new ReservaHotelId("res-001");

    // Assert
    assertEquals("res-001", id.value(), "value debe conservar el id recibido");
  }

  @Test
  @DisplayName("rechaza un id en blanco")
  void shouldRejectBlankValue() {
    // Act + Assert
    assertThrows(
        InvalidReservaHotelException.class,
        () -> new ReservaHotelId("   "),
        "debe rechazar un id compuesto solo de espacios");
  }

  @Test
  @DisplayName("rechaza un id nulo")
  void shouldRejectNullValue() {
    // Act + Assert
    assertThrows(
        InvalidReservaHotelException.class,
        () -> new ReservaHotelId(null),
        "debe rechazar un id nulo");
  }
}
