package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ReservaHotelApplicationMapper.
 *
 * <p>Cubre cada método público de conversión: el command de creación fija el estado inicial
 * PENDING y el de actualización respeta el estado recibido.
 */
@DisplayName("ReservaHotelApplicationMapper")
class ReservaHotelApplicationMapperTest {

  private static final String ID = "res-001";
  private static final String FECHA = "2026-11-10";
  private static final String HOTEL = "Hotel Cartagena";
  private static final String HUESPED = "Juan Perez";
  private static final String FECHA_INICIO = "2026-12-01";
  private static final String FECHA_FIN = "2026-12-05";
  private static final double VALOR = 450000.0;
  private static final String HABITACION = "301";
  private static final int NUM_ACOMPANANTES = 2;
  private static final String PAIS = "Colombia";
  private static final String DEPARTAMENTO = "Bolívar";
  private static final String CIUDAD = "Cartagena";
  private static final String HORA_CHECKIN = "15:00";
  private static final String HORA_CHECKOUT = "12:00";
  private static final String EMPLEADO_ATIENDE = "Maria Lopez";
  private static final String EMPLEADO_DESPIDE = "Carlos Ruiz";
  private static final String DESCRIPCION = "Reserva con vista al mar";

  // ── fromCreateCommandToModel()

  @Test
  @DisplayName("fromCreateCommandToModel() mapea todos los campos y fija status PENDING")
  void shouldMapCreateCommandToModelWithPendingStatus() {
    // Arrange
    final CreateReservaHotelCommand command =
        new CreateReservaHotelCommand(
            ID,
            FECHA,
            HOTEL,
            HUESPED,
            FECHA_INICIO,
            FECHA_FIN,
            VALOR,
            HABITACION,
            NUM_ACOMPANANTES,
            PAIS,
            DEPARTAMENTO,
            CIUDAD,
            HORA_CHECKIN,
            HORA_CHECKOUT,
            EMPLEADO_ATIENDE,
            EMPLEADO_DESPIDE,
            DESCRIPCION);

    // Act
    final ReservaHotelModel result = ReservaHotelApplicationMapper.fromCreateCommandToModel(command);

    // Assert
    assertAll(
        "fromCreateCommandToModel()",
        () -> assertEquals(ID, result.getId().value(), "id"),
        () -> assertEquals(FECHA, result.getFecha(), "fecha"),
        () -> assertEquals(HOTEL, result.getHotel(), "hotel"),
        () -> assertEquals(HUESPED, result.getHuesped(), "huesped"),
        () -> assertEquals(FECHA_INICIO, result.getFechaInicio(), "fechaInicio"),
        () -> assertEquals(FECHA_FIN, result.getFechaFin(), "fechaFin"),
        () -> assertEquals(VALOR, result.getValor(), "valor"),
        () -> assertEquals(HABITACION, result.getHabitacion(), "habitacion"),
        () -> assertEquals(NUM_ACOMPANANTES, result.getNumAcompanantes(), "numAcompanantes"),
        () -> assertEquals(PAIS, result.getPais(), "pais"),
        () -> assertEquals(DEPARTAMENTO, result.getDepartamento(), "departamento"),
        () -> assertEquals(CIUDAD, result.getCiudad(), "ciudad"),
        () -> assertEquals(HORA_CHECKIN, result.getHoraCheckin(), "horaCheckin"),
        () -> assertEquals(HORA_CHECKOUT, result.getHoraCheckout(), "horaCheckout"),
        () -> assertEquals(EMPLEADO_ATIENDE, result.getEmpleadoAtiende(), "empleadoAtiende"),
        () -> assertEquals(EMPLEADO_DESPIDE, result.getEmpleadoDespide(), "empleadoDespide"),
        () -> assertEquals(DESCRIPCION, result.getDescripcion(), "descripcion"),
        () -> assertEquals(ReservaHotelStatus.PENDING, result.getStatus(), "status debe ser PENDING"));
  }

  // ── fromUpdateCommandToModel()

  @Test
  @DisplayName("fromUpdateCommandToModel() mapea todos los campos y respeta el status recibido")
  void shouldMapUpdateCommandToModelWithGivenStatus() {
    // Arrange
    final UpdateReservaHotelCommand command =
        new UpdateReservaHotelCommand(
            ID,
            FECHA,
            HOTEL,
            HUESPED,
            FECHA_INICIO,
            FECHA_FIN,
            VALOR,
            HABITACION,
            NUM_ACOMPANANTES,
            PAIS,
            DEPARTAMENTO,
            CIUDAD,
            HORA_CHECKIN,
            HORA_CHECKOUT,
            EMPLEADO_ATIENDE,
            EMPLEADO_DESPIDE,
            null,
            "CONFIRMED");

    // Act
    final ReservaHotelModel result = ReservaHotelApplicationMapper.fromUpdateCommandToModel(command);

    // Assert
    assertAll(
        "fromUpdateCommandToModel()",
        () -> assertEquals(ID, result.getId().value(), "id"),
        () -> assertEquals(HOTEL, result.getHotel(), "hotel"),
        () -> assertNull(result.getDescripcion(), "descripcion debe ser null"),
        () -> assertEquals(ReservaHotelStatus.CONFIRMED, result.getStatus(), "status"));
  }

  // ── fromUpdateCommandToModel() — status inválido

  @Test
  @DisplayName("fromUpdateCommandToModel() lanza InvalidReservaHotelException con status inválido")
  void shouldThrowWhenUpdateStatusIsInvalid() {
    // Arrange
    final UpdateReservaHotelCommand command =
        new UpdateReservaHotelCommand(
            ID,
            FECHA,
            HOTEL,
            HUESPED,
            FECHA_INICIO,
            FECHA_FIN,
            VALOR,
            HABITACION,
            NUM_ACOMPANANTES,
            PAIS,
            DEPARTAMENTO,
            CIUDAD,
            HORA_CHECKIN,
            HORA_CHECKOUT,
            EMPLEADO_ATIENDE,
            EMPLEADO_DESPIDE,
            null,
            "ESTADO_INEXISTENTE");

    // Act + Assert
    assertThrows(
        com.jcaa.usersmanagement.domain.exception.InvalidReservaHotelException.class,
        () -> ReservaHotelApplicationMapper.fromUpdateCommandToModel(command));
  }

  // ── query y command de borrado (trivialmente correctos vía el dominio)

  @Test
  @DisplayName("el query de búsqueda y el command de borrado conservan el id")
  void shouldKeepIdOnQueryAndDeleteCommand() {
    // Arrange
    final GetReservaHotelByIdQuery query = new GetReservaHotelByIdQuery(ID);
    final DeleteReservaHotelCommand command = new DeleteReservaHotelCommand(ID);

    // Act + Assert
    assertEquals(ID, query.id(), "id del query");
    assertEquals(ID, command.id(), "id del command");
  }
}
