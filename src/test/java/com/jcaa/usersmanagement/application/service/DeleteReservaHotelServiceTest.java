package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteReservaHotelPort;
import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para DeleteReservaHotelService.
 *
 * <p>Cubre: flujo feliz (delete invocado), reserva inexistente y validación del command.
 */
@DisplayName("DeleteReservaHotelService")
@ExtendWith(MockitoExtension.class)
class DeleteReservaHotelServiceTest {

  @Mock private DeleteReservaHotelPort deleteReservaHotelPort;
  @Mock private GetReservaHotelByIdPort getReservaHotelByIdPort;

  private DeleteReservaHotelService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new DeleteReservaHotelService(
              deleteReservaHotelPort, getReservaHotelByIdPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() invoca deleteReservaHotelPort cuando la reserva existe")
  void shouldDeleteWhenReservaExists() {
    // Arrange
    final DeleteReservaHotelCommand command = new DeleteReservaHotelCommand("res-001");

    final ReservaHotelModel existing =
        ReservaHotelModel.create(
            new ReservaHotelId("res-001"),
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

    when(getReservaHotelByIdPort.getById("res-001")).thenReturn(Optional.of(existing));

    // Act
    service.execute(command);

    // Assert
    verify(deleteReservaHotelPort).delete("res-001");
  }

  // ── reserva inexistente

  @Test
  @DisplayName("execute() lanza ReservaHotelNotFoundException cuando el id no existe")
  void shouldThrowWhenReservaNotFound() {
    // Arrange
    final DeleteReservaHotelCommand command = new DeleteReservaHotelCommand("no-existe");

    when(getReservaHotelByIdPort.getById("no-existe")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ReservaHotelNotFoundException.class, () -> service.execute(command));
    verify(deleteReservaHotelPort, never()).delete(any());
  }

  // ── validación del command

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final DeleteReservaHotelCommand command = new DeleteReservaHotelCommand("  ");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(deleteReservaHotelPort, getReservaHotelByIdPort);
  }
}
