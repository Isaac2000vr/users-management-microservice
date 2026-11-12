package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ReservaHotelPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.ReservaHotelEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for ReservaHotelPersistenceMapper.
 *
 * <p>Covers: fromModelToDto(), fromEntityToModel(), fromResultSetToEntity() (happy path and
 * SQLException propagation), and fromResultSetToModelList() — empty list, multiple rows, and
 * SQLException propagation during iteration.
 */
@DisplayName("ReservaHotelPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class ReservaHotelPersistenceMapperTest {

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
  private static final String DEPARTAMENTO = "Bolivar";
  private static final String CIUDAD = "Cartagena";
  private static final String HORA_CHECKIN = "15:00";
  private static final String HORA_CHECKOUT = "12:00";
  private static final String EMPLEADO_ATIENDE = "Maria Lopez";
  private static final String EMPLEADO_DESPIDE = "Carlos Ruiz";
  private static final String DESCRIPCION = "Reserva con vista al mar";
  private static final String STATUS = "PENDING";
  private static final String CREATED_AT = "2026-11-10 00:00:00";
  private static final String UPDATED_AT = "2026-11-11 00:00:00";

  @Mock private ResultSet resultSet;

  private ReservaHotelModel reservaModel;
  private ReservaHotelEntity reservaEntity;

  @BeforeEach
  void setUp() {
    reservaModel =
        new ReservaHotelModel(
            new ReservaHotelId(ID),
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
            DESCRIPCION,
            ReservaHotelStatus.PENDING);

    reservaEntity =
        new ReservaHotelEntity(
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
            DESCRIPCION,
            STATUS,
            CREATED_AT,
            UPDATED_AT);
  }

  // ── fromModelToDto()

  @Test
  @DisplayName("fromModelToDto() maps all model fields and sets null timestamps")
  void shouldMapModelToDto() {
    // Act
    final ReservaHotelPersistenceDto result =
        ReservaHotelPersistenceMapper.fromModelToDto(reservaModel);

    // Assert
    assertAll(
        "fromModelToDto()",
        () -> assertEquals(ID, result.id(), "id"),
        () -> assertEquals(FECHA, result.fecha(), "fecha"),
        () -> assertEquals(HOTEL, result.hotel(), "hotel"),
        () -> assertEquals(HUESPED, result.huesped(), "huesped"),
        () -> assertEquals(FECHA_INICIO, result.fechaInicio(), "fechaInicio"),
        () -> assertEquals(FECHA_FIN, result.fechaFin(), "fechaFin"),
        () -> assertEquals(VALOR, result.valor(), "valor"),
        () -> assertEquals(HABITACION, result.habitacion(), "habitacion"),
        () -> assertEquals(NUM_ACOMPANANTES, result.numAcompanantes(), "numAcompanantes"),
        () -> assertEquals(PAIS, result.pais(), "pais"),
        () -> assertEquals(DEPARTAMENTO, result.departamento(), "departamento"),
        () -> assertEquals(CIUDAD, result.ciudad(), "ciudad"),
        () -> assertEquals(HORA_CHECKIN, result.horaCheckin(), "horaCheckin"),
        () -> assertEquals(HORA_CHECKOUT, result.horaCheckout(), "horaCheckout"),
        () -> assertEquals(EMPLEADO_ATIENDE, result.empleadoAtiende(), "empleadoAtiende"),
        () -> assertEquals(EMPLEADO_DESPIDE, result.empleadoDespide(), "empleadoDespide"),
        () -> assertEquals(DESCRIPCION, result.descripcion(), "descripcion"),
        () -> assertEquals(STATUS, result.status(), "status"),
        () -> assertNull(result.createdAt(), "createdAt must be null"),
        () -> assertNull(result.updatedAt(), "updatedAt must be null"));
  }

  // ── fromEntityToModel()

  @Test
  @DisplayName("fromEntityToModel() maps all entity fields to a domain model")
  void shouldMapEntityToModel() {
    // Act
    final ReservaHotelModel result = ReservaHotelPersistenceMapper.fromEntityToModel(reservaEntity);

    // Assert
    assertAll(
        "fromEntityToModel()",
        () -> assertEquals(ID, result.getId().value(), "id"),
        () -> assertEquals(HOTEL, result.getHotel(), "hotel"),
        () -> assertEquals(HUESPED, result.getHuesped(), "huesped"),
        () -> assertEquals(VALOR, result.getValor(), "valor"),
        () -> assertEquals(NUM_ACOMPANANTES, result.getNumAcompanantes(), "numAcompanantes"),
        () -> assertEquals(DESCRIPCION, result.getDescripcion(), "descripcion"),
        () -> assertEquals(ReservaHotelStatus.PENDING, result.getStatus(), "status"));
  }

  // ── fromResultSetToEntity() — happy path

  @Test
  @DisplayName("fromResultSetToEntity() reads all twenty columns from the ResultSet")
  void shouldReadAllColumnsFromResultSet() throws SQLException {
    // Arrange
    configureSingleRow(ID);

    // Act
    final ReservaHotelEntity result =
        ReservaHotelPersistenceMapper.fromResultSetToEntity(resultSet);

    // Assert
    assertAll(
        "fromResultSetToEntity()",
        () -> assertEquals(ID, result.id(), "id"),
        () -> assertEquals(FECHA, result.fecha(), "fecha"),
        () -> assertEquals(HOTEL, result.hotel(), "hotel"),
        () -> assertEquals(HUESPED, result.huesped(), "huesped"),
        () -> assertEquals(FECHA_INICIO, result.fechaInicio(), "fecha_inicio"),
        () -> assertEquals(FECHA_FIN, result.fechaFin(), "fecha_fin"),
        () -> assertEquals(VALOR, result.valor(), "valor"),
        () -> assertEquals(HABITACION, result.habitacion(), "habitacion"),
        () -> assertEquals(NUM_ACOMPANANTES, result.numAcompanantes(), "num_acompanantes"),
        () -> assertEquals(PAIS, result.pais(), "pais"),
        () -> assertEquals(DEPARTAMENTO, result.departamento(), "departamento"),
        () -> assertEquals(CIUDAD, result.ciudad(), "ciudad"),
        () -> assertEquals(HORA_CHECKIN, result.horaCheckin(), "hora_checkin"),
        () -> assertEquals(HORA_CHECKOUT, result.horaCheckout(), "hora_checkout"),
        () -> assertEquals(EMPLEADO_ATIENDE, result.empleadoAtiende(), "empleado_atiende"),
        () -> assertEquals(EMPLEADO_DESPIDE, result.empleadoDespide(), "empleado_despide"),
        () -> assertEquals(DESCRIPCION, result.descripcion(), "descripcion"),
        () -> assertEquals(STATUS, result.status(), "status"),
        () -> assertEquals(CREATED_AT, result.createdAt(), "created_at"),
        () -> assertEquals(UPDATED_AT, result.updatedAt(), "updated_at"));
  }

  // ── fromResultSetToEntity() — SQLException propagation

  @Test
  @DisplayName("fromResultSetToEntity() propagates SQLException when ResultSet read fails")
  void shouldPropagateExceptionFromResultSet() throws SQLException {
    // Arrange
    when(resultSet.getString(anyString())).thenThrow(new SQLException("Column read failed"));

    // Act + Assert
    assertThrows(
        SQLException.class,
        () -> ReservaHotelPersistenceMapper.fromResultSetToEntity(resultSet),
        "must propagate SQLException when ResultSet throws on getString");
  }

  // ── fromResultSetToModelList() — empty

  @Test
  @DisplayName("fromResultSetToModelList() returns an empty list when ResultSet has no rows")
  void shouldReturnEmptyListWhenResultSetIsEmpty() throws SQLException {
    // Arrange
    when(resultSet.next()).thenReturn(false);

    // Act
    final List<ReservaHotelModel> result =
        ReservaHotelPersistenceMapper.fromResultSetToModelList(resultSet);

    // Assert
    assertTrue(result.isEmpty(), "must return an empty list when ResultSet has no rows");
  }

  // ── fromResultSetToModelList() — multiple rows

  @Test
  @DisplayName("fromResultSetToModelList() returns one model per row in the ResultSet")
  void shouldReturnOneModelPerRow() throws SQLException {
    // Arrange
    when(resultSet.next()).thenReturn(true, true, false);
    when(resultSet.getString("id")).thenReturn(ID, "res-002");
    when(resultSet.getString("fecha")).thenReturn(FECHA, FECHA);
    when(resultSet.getString("hotel")).thenReturn(HOTEL, HOTEL);
    when(resultSet.getString("huesped")).thenReturn(HUESPED, "Maria Garcia");
    when(resultSet.getString("fecha_inicio")).thenReturn(FECHA_INICIO, FECHA_INICIO);
    when(resultSet.getString("fecha_fin")).thenReturn(FECHA_FIN, FECHA_FIN);
    when(resultSet.getDouble("valor")).thenReturn(VALOR, VALOR);
    when(resultSet.getString("habitacion")).thenReturn(HABITACION, "302");
    when(resultSet.getInt("num_acompanantes")).thenReturn(NUM_ACOMPANANTES, NUM_ACOMPANANTES);
    when(resultSet.getString("pais")).thenReturn(PAIS, PAIS);
    when(resultSet.getString("departamento")).thenReturn(DEPARTAMENTO, DEPARTAMENTO);
    when(resultSet.getString("ciudad")).thenReturn(CIUDAD, CIUDAD);
    when(resultSet.getString("hora_checkin")).thenReturn(HORA_CHECKIN, HORA_CHECKIN);
    when(resultSet.getString("hora_checkout")).thenReturn(HORA_CHECKOUT, HORA_CHECKOUT);
    when(resultSet.getString("empleado_atiende")).thenReturn(EMPLEADO_ATIENDE, EMPLEADO_ATIENDE);
    when(resultSet.getString("empleado_despide")).thenReturn(EMPLEADO_DESPIDE, EMPLEADO_DESPIDE);
    when(resultSet.getString("descripcion")).thenReturn(DESCRIPCION, null);
    when(resultSet.getString("status")).thenReturn(STATUS, "CONFIRMED");
    when(resultSet.getString("created_at")).thenReturn(CREATED_AT, CREATED_AT);
    when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT, UPDATED_AT);

    // Act
    final List<ReservaHotelModel> result =
        ReservaHotelPersistenceMapper.fromResultSetToModelList(resultSet);

    // Assert
    assertEquals(2, result.size(), "must return one model per row in the ResultSet");
  }

  // ── fromResultSetToModelList() — SQLException propagation during iteration

  @Test
  @DisplayName("fromResultSetToModelList() propagates SQLException when a row read fails")
  void shouldPropagateExceptionDuringIteration() throws SQLException {
    // Arrange
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString(anyString())).thenThrow(new SQLException("Row read failed"));

    // Act + Assert
    assertThrows(
        SQLException.class,
        () -> ReservaHotelPersistenceMapper.fromResultSetToModelList(resultSet),
        "must propagate SQLException when a row fails to be read");
  }

  // ── helper

  private void configureSingleRow(final String id) throws SQLException {
    when(resultSet.getString("id")).thenReturn(id);
    when(resultSet.getString("fecha")).thenReturn(FECHA);
    when(resultSet.getString("hotel")).thenReturn(HOTEL);
    when(resultSet.getString("huesped")).thenReturn(HUESPED);
    when(resultSet.getString("fecha_inicio")).thenReturn(FECHA_INICIO);
    when(resultSet.getString("fecha_fin")).thenReturn(FECHA_FIN);
    when(resultSet.getDouble("valor")).thenReturn(VALOR);
    when(resultSet.getString("habitacion")).thenReturn(HABITACION);
    when(resultSet.getInt("num_acompanantes")).thenReturn(NUM_ACOMPANANTES);
    when(resultSet.getString("pais")).thenReturn(PAIS);
    when(resultSet.getString("departamento")).thenReturn(DEPARTAMENTO);
    when(resultSet.getString("ciudad")).thenReturn(CIUDAD);
    when(resultSet.getString("hora_checkin")).thenReturn(HORA_CHECKIN);
    when(resultSet.getString("hora_checkout")).thenReturn(HORA_CHECKOUT);
    when(resultSet.getString("empleado_atiende")).thenReturn(EMPLEADO_ATIENDE);
    when(resultSet.getString("empleado_despide")).thenReturn(EMPLEADO_DESPIDE);
    when(resultSet.getString("descripcion")).thenReturn(DESCRIPCION);
    when(resultSet.getString("status")).thenReturn(STATUS);
    when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
    when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
  }
}
