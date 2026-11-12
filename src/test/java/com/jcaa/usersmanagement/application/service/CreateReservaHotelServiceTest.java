package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.SaveReservaHotelPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para CreateReservaHotelService.
 *
 * <p>Cubre: flujo feliz (save) y validación del command.
 */
@DisplayName("CreateReservaHotelService")
@ExtendWith(MockitoExtension.class)
class CreateReservaHotelServiceTest {

  @Mock private SaveReservaHotelPort saveReservaHotelPort;

  private CreateReservaHotelService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new CreateReservaHotelService(saveReservaHotelPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() guarda la reserva cuando el command es válido")
  void shouldSaveReservaWhenCommandIsValid() {
    // Arrange
    final CreateReservaHotelCommand command =
        new CreateReservaHotelCommand(
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
            "Reserva con vista al mar");

    final ReservaHotelModel saved = ReservaHotelModel.create(
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
        "Reserva con vista al mar");

    when(saveReservaHotelPort.save(any())).thenReturn(saved);

    // Act
    final ReservaHotelModel result = service.execute(command);

    // Assert
    assertAll(
        "flujo feliz de CreateReservaHotelService",
        () -> assertNotNull(result, "resultado no debe ser null"),
        () -> assertEquals("res-001", result.getId().value(), "id de la reserva guardada"),
        () -> assertEquals(ReservaHotelStatus.PENDING, result.getStatus(), "status inicial"));

    verify(saveReservaHotelPort).save(any(ReservaHotelModel.class));
  }

  // ── validación del command

  @Test
  @DisplayName(
      "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange — id en blanco y valor negativo
    final CreateReservaHotelCommand command =
        new CreateReservaHotelCommand(
            "",
            "",
            "Hotel Cartagena",
            "Juan Perez",
            "2026-12-01",
            "2026-12-05",
            -1.0,
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

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveReservaHotelPort);
  }
}
