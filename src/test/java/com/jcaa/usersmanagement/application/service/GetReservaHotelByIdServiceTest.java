package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
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
 * Tests para GetReservaHotelByIdService.
 *
 * <p>Cubre: retorno de la reserva encontrada, ReservaHotelNotFoundException y validación del query.
 */
@DisplayName("GetReservaHotelByIdService")
@ExtendWith(MockitoExtension.class)
class GetReservaHotelByIdServiceTest {

  @Mock private GetReservaHotelByIdPort getReservaHotelByIdPort;

  private GetReservaHotelByIdService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new GetReservaHotelByIdService(getReservaHotelByIdPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() retorna la reserva cuando el id existe")
  void shouldReturnReservaWhenFound() {
    // Arrange
    final GetReservaHotelByIdQuery query = new GetReservaHotelByIdQuery("res-001");

    final ReservaHotelModel expected =
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

    when(getReservaHotelByIdPort.getById(any())).thenReturn(Optional.of(expected));

    // Act
    final ReservaHotelModel result = service.execute(query);

    // Assert
    assertSame(expected, result, "debe retornar exactamente la reserva del puerto");
  }

  // ── reserva no encontrada

  @Test
  @DisplayName("execute() lanza ReservaHotelNotFoundException cuando el id no existe")
  void shouldThrowWhenReservaNotFound() {
    // Arrange
    final GetReservaHotelByIdQuery query = new GetReservaHotelByIdQuery("no-existe");

    when(getReservaHotelByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ReservaHotelNotFoundException.class, () -> service.execute(query));
  }

  // ── validación del query

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenQueryIsInvalid() {
    // Arrange
    final GetReservaHotelByIdQuery query = new GetReservaHotelByIdQuery("");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(query));
    verifyNoInteractions(getReservaHotelByIdPort);
  }
}
