package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateReservaHotelPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
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
 * Tests para UpdateReservaHotelService.
 *
 * <p>Cubre: flujo feliz (update), reserva inexistente y validación del command.
 */
@DisplayName("UpdateReservaHotelService")
@ExtendWith(MockitoExtension.class)
class UpdateReservaHotelServiceTest {

  @Mock private UpdateReservaHotelPort updateReservaHotelPort;
  @Mock private GetReservaHotelByIdPort getReservaHotelByIdPort;

  private UpdateReservaHotelService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateReservaHotelService(
              updateReservaHotelPort, getReservaHotelByIdPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() actualiza la reserva cuando el id existe")
  void shouldUpdateReservaWhenIdExists() {
    // Arrange
    final UpdateReservaHotelCommand command =
        new UpdateReservaHotelCommand(
            "res-001",
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
            "Reserva actualizada",
            "CONFIRMED");

    final ReservaHotelModel updated =
        ReservaHotelModel.create(
                new com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId("res-001"),
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
                "Reserva actualizada")
            .confirm();

    when(getReservaHotelByIdPort.getById("res-001")).thenReturn(Optional.of(updated));
    when(updateReservaHotelPort.update(any())).thenReturn(updated);

    // Act
    final ReservaHotelModel result = service.execute(command);

    // Assert
    assertAll(
        "flujo feliz de UpdateReservaHotelService",
        () -> assertNotNull(result, "resultado no debe ser null"),
        () -> assertEquals("res-001", result.getId().value(), "id de la reserva actualizada"),
        () -> assertEquals(ReservaHotelStatus.CONFIRMED, result.getStatus(), "status"));

    verify(updateReservaHotelPort).update(any(ReservaHotelModel.class));
  }

  // ── reserva inexistente

  @Test
  @DisplayName("execute() lanza ReservaHotelNotFoundException cuando el id no existe")
  void shouldThrowWhenReservaNotFound() {
    // Arrange
    final UpdateReservaHotelCommand command =
        new UpdateReservaHotelCommand(
            "no-existe",
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
            null,
            "CONFIRMED");

    when(getReservaHotelByIdPort.getById("no-existe")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ReservaHotelNotFoundException.class, () -> service.execute(command));
    verify(updateReservaHotelPort, never()).update(any());
  }

  // ── validación del command

  @Test
  @DisplayName(
      "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange — id en blanco y status en blanco
    final UpdateReservaHotelCommand command =
        new UpdateReservaHotelCommand(
            "  ",
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
            null,
            "");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(updateReservaHotelPort, getReservaHotelByIdPort);
  }
}
