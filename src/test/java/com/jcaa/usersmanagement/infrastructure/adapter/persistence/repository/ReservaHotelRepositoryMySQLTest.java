package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for ReservaHotelRepositoryMySQL.
 *
 * <p>Covers all public methods with their branches: save() — happy path, INSERT failure, reserva
 * not found after insert (orElseThrow); update() — happy path, UPDATE failure; getById() — found,
 * not found, SQLException; getAll() — happy path, SQLException; delete() — happy path,
 * SQLException.
 */
@DisplayName("ReservaHotelRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class ReservaHotelRepositoryMySQLTest {

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

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private ReservaHotelRepositoryMySQL repository;
  private ReservaHotelModel reservaModel;

  @BeforeEach
  void setUp() {
    repository = new ReservaHotelRepositoryMySQL(dataSource);
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
  }

  // Helper: wire dataSource → connection → statement → resultSet
  private void configureStatementAndResultSet() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
  }

  // Helper: configure resultSet to return one full reserva row
  private void configureResultSetRow() throws SQLException {
    when(resultSet.getString("id")).thenReturn(ID);
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

  // ── save() — happy path

  @Test
  @DisplayName("save() executes INSERT and returns the persisted reserva fetched by id")
  void shouldSaveReservaAndReturnById() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    // Act
    final ReservaHotelModel result = repository.save(reservaModel);

    // Assert
    assertAll(
        "save() happy path",
        () -> assertEquals(ID, result.getId().value(), "id"),
        () -> assertEquals(HOTEL, result.getHotel(), "hotel"),
        () -> assertEquals(HUESPED, result.getHuesped(), "huesped"),
        () -> assertEquals(VALOR, result.getValor(), "valor"));
  }

  // ── save() — INSERT fails → PersistenceException

  @Test
  @DisplayName("save() throws PersistenceException when INSERT raises SQLException")
  void shouldThrowPersistenceExceptionWhenInsertFails() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.save(reservaModel),
        "must throw PersistenceException when INSERT raises SQLException");
  }

  // ── save() → findByIdOrFail — reserva not found after insert → ReservaHotelNotFoundException

  @Test
  @DisplayName("save() throws ReservaHotelNotFoundException when the saved reserva cannot be found")
  void shouldThrowReservaHotelNotFoundExceptionWhenReservaNotFoundAfterSave() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(false);

    // Act + Assert
    assertThrows(
        ReservaHotelNotFoundException.class,
        () -> repository.save(reservaModel),
        "must throw ReservaHotelNotFoundException when SELECT returns no rows after INSERT");
  }

  // ── update() — happy path

  @Test
  @DisplayName("update() executes UPDATE and returns the refreshed reserva fetched by id")
  void shouldUpdateReservaAndReturnById() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    // Act
    final ReservaHotelModel result = repository.update(reservaModel);

    // Assert
    assertEquals(ID, result.getId().value(), "id must match the updated reserva");
  }

  // ── update() — UPDATE fails → PersistenceException

  @Test
  @DisplayName("update() throws PersistenceException when UPDATE raises SQLException")
  void shouldThrowPersistenceExceptionWhenUpdateFails() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.update(reservaModel),
        "must throw PersistenceException when UPDATE raises SQLException");
  }

  // ── getById() — row found → Optional.of(reserva)

  @Test
  @DisplayName("getById() returns Optional.of(reserva) when a matching row exists")
  void shouldReturnReservaWhenFound() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    // Act
    final Optional<ReservaHotelModel> result = repository.getById(ID);

    // Assert
    assertAll(
        "getById() found",
        () -> assertTrue(result.isPresent(), "must be present"),
        () -> assertEquals(ID, result.map(r -> r.getId().value()).orElse(null), "id"));
  }

  // ── getById() — no row → Optional.empty()

  @Test
  @DisplayName("getById() returns Optional.empty() when no matching row exists")
  void shouldReturnEmptyWhenNotFound() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(false);

    // Act
    final Optional<ReservaHotelModel> result = repository.getById(ID);

    // Assert
    assertTrue(result.isEmpty(), "must return Optional.empty() when no row matches the id");
  }

  // ── getById() — SQLException → PersistenceException (from prepareStatement)

  @Test
  @DisplayName("getById() throws PersistenceException when prepareStatement raises SQLException")
  void shouldThrowPersistenceExceptionOnGetByIdFailure() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.getById(ID),
        "must throw PersistenceException when prepareStatement raises SQLException");
  }

  // ── getById() — SQLException → PersistenceException (from executeQuery, inside try body)

  @Test
  @DisplayName("getById() throws PersistenceException when executeQuery raises SQLException")
  void shouldThrowPersistenceExceptionWhenGetByIdExecuteQueryFails() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.getById(ID),
        "must throw PersistenceException when executeQuery raises SQLException inside the try block");
  }

  // ── getAll() — happy path

  @Test
  @DisplayName("getAll() returns one model per row in the result set")
  void shouldReturnAllReservas() throws SQLException {
    // Arrange
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true, false);
    configureResultSetRow();

    // Act
    final List<ReservaHotelModel> result = repository.getAll();

    // Assert
    assertAll(
        "getAll() happy path",
        () -> assertEquals(1, result.size(), "list size"),
        () -> assertEquals(ID, result.get(0).getId().value(), "first reserva id"));
  }

  // ── getAll() — SQLException → PersistenceException

  @Test
  @DisplayName("getAll() throws PersistenceException when the query raises SQLException")
  void shouldThrowPersistenceExceptionOnGetAllFailure() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.getAll(),
        "must throw PersistenceException when SELECT raises SQLException");
  }

  // ── delete() — happy path

  @Test
  @DisplayName("delete() executes DELETE without throwing")
  void shouldDeleteReservaWithoutThrowing() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);

    // Act + Assert
    assertDoesNotThrow(
        () -> repository.delete(ID),
        "delete() must not throw when DELETE executes successfully");
  }

  // ── delete() — SQLException → PersistenceException

  @Test
  @DisplayName("delete() throws PersistenceException when DELETE raises SQLException")
  void shouldThrowPersistenceExceptionWhenDeleteFails() throws SQLException {
    // Arrange
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

    // Act + Assert
    assertThrows(
        PersistenceException.class,
        () -> repository.delete(ID),
        "must throw PersistenceException when DELETE raises SQLException");
  }
}
