package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllReservasHotelPort;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetAllReservasHotelService.
 *
 * <p>Cubre: lista con reservas y lista vacía.
 */
@DisplayName("GetAllReservasHotelService")
@ExtendWith(MockitoExtension.class)
class GetAllReservasHotelServiceTest {

  @Mock private GetAllReservasHotelPort getAllReservasHotelPort;

  private GetAllReservasHotelService service;

  @BeforeEach
  void setUp() {
    service = new GetAllReservasHotelService(getAllReservasHotelPort);
  }

  // ── lista con reservas

  @Test
  @DisplayName("execute() retorna la lista de reservas del puerto")
  void shouldReturnReservasFromPort() {
    // Arrange
    final ReservaHotelModel reserva =
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

    when(getAllReservasHotelPort.getAll()).thenReturn(List.of(reserva));

    // Act
    final List<ReservaHotelModel> result = service.execute();

    // Assert
    assertAll(
        "getAll con una reserva",
        () -> assertEquals(1, result.size(), "debe retornar exactamente una reserva"),
        () -> assertSame(reserva, result.get(0), "debe ser el mismo objeto del puerto"));
  }

  // ── lista vacía

  @Test
  @DisplayName("execute() retorna lista vacía cuando no hay reservas")
  void shouldReturnEmptyListWhenNoReservas() {
    // Arrange
    when(getAllReservasHotelPort.getAll()).thenReturn(List.of());

    // Act
    final List<ReservaHotelModel> result = service.execute();

    // Assert
    assertTrue(result.isEmpty(), "debe retornar lista vacía");
  }
}
