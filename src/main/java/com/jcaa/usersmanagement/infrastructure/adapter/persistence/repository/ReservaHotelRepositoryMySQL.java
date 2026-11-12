package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteReservaHotelPort;
import com.jcaa.usersmanagement.application.port.out.GetAllReservasHotelPort;
import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveReservaHotelPort;
import com.jcaa.usersmanagement.application.port.out.UpdateReservaHotelPort;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ReservaHotelPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.ReservaHotelPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReservaHotelRepositoryMySQL
    implements SaveReservaHotelPort,
        UpdateReservaHotelPort,
        GetReservaHotelByIdPort,
        GetAllReservasHotelPort,
        DeleteReservaHotelPort {

  private static final String SQL_INSERT =
      "INSERT INTO reservas_hotel "
      + "(id, fecha, hotel, huesped, fecha_inicio, fecha_fin, valor, habitacion, "
      + "num_acompanantes, pais, departamento, ciudad, hora_checkin, hora_checkout, "
      + "empleado_atiende, empleado_despide, descripcion, status, created_at, updated_at) "
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE reservas_hotel "
      + "SET fecha = ?, hotel = ?, huesped = ?, fecha_inicio = ?, fecha_fin = ?, "
      + "valor = ?, habitacion = ?, num_acompanantes = ?, pais = ?, departamento = ?, "
      + "ciudad = ?, hora_checkin = ?, hora_checkout = ?, empleado_atiende = ?, "
      + "empleado_despide = ?, descripcion = ?, status = ?, updated_at = NOW() "
      + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, fecha, hotel, huesped, fecha_inicio, fecha_fin, valor, habitacion, "
      + "num_acompanantes, pais, departamento, ciudad, hora_checkin, hora_checkout, "
      + "empleado_atiende, empleado_despide, descripcion, status, created_at, updated_at "
      + "FROM reservas_hotel "
      + "WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, fecha, hotel, huesped, fecha_inicio, fecha_fin, valor, habitacion, "
      + "num_acompanantes, pais, departamento, ciudad, hora_checkin, hora_checkout, "
      + "empleado_atiende, empleado_despide, descripcion, status, created_at, updated_at "
      + "FROM reservas_hotel "
      + "ORDER BY fecha_inicio DESC, huesped ASC";

  private static final String SQL_DELETE =
      "DELETE FROM reservas_hotel "
      + "WHERE id = ?";

  private final DataSource dataSource;

  @Override
  public ReservaHotelModel save(final ReservaHotelModel reserva) {
    final ReservaHotelPersistenceDto dto = ReservaHotelPersistenceMapper.fromModelToDto(reserva);
    executeSave(dto);
    return findByIdOrFail(reserva.getId().value());
  }

  @Override
  public ReservaHotelModel update(final ReservaHotelModel reserva) {
    final ReservaHotelPersistenceDto dto = ReservaHotelPersistenceMapper.fromModelToDto(reserva);
    executeUpdate(dto);
    return findByIdOrFail(reserva.getId().value());
  }

  @Override
  public Optional<ReservaHotelModel> getById(final String id) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, id);
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(ReservaHotelPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindReservaHotelByIdFailed(id, exception);
    }
  }

  @Override
  public List<ReservaHotelModel> getAll() {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return ReservaHotelPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindAllReservasHotelFailed(exception);
    }
  }

  @Override
  public void delete(final String id) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, id);
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseDeleteReservaHotelFailed(id, exception);
    }
  }

  private void executeSave(final ReservaHotelPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.fecha());
      statement.setString(3, dto.hotel());
      statement.setString(4, dto.huesped());
      statement.setString(5, dto.fechaInicio());
      statement.setString(6, dto.fechaFin());
      statement.setDouble(7, dto.valor());
      statement.setString(8, dto.habitacion());
      statement.setInt(9, dto.numAcompanantes());
      statement.setString(10, dto.pais());
      statement.setString(11, dto.departamento());
      statement.setString(12, dto.ciudad());
      statement.setString(13, dto.horaCheckin());
      statement.setString(14, dto.horaCheckout());
      statement.setString(15, dto.empleadoAtiende());
      statement.setString(16, dto.empleadoDespide());
      statement.setString(17, dto.descripcion());
      statement.setString(18, dto.status());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseSaveReservaHotelFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final ReservaHotelPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.fecha());
      statement.setString(2, dto.hotel());
      statement.setString(3, dto.huesped());
      statement.setString(4, dto.fechaInicio());
      statement.setString(5, dto.fechaFin());
      statement.setDouble(6, dto.valor());
      statement.setString(7, dto.habitacion());
      statement.setInt(8, dto.numAcompanantes());
      statement.setString(9, dto.pais());
      statement.setString(10, dto.departamento());
      statement.setString(11, dto.ciudad());
      statement.setString(12, dto.horaCheckin());
      statement.setString(13, dto.horaCheckout());
      statement.setString(14, dto.empleadoAtiende());
      statement.setString(15, dto.empleadoDespide());
      statement.setString(16, dto.descripcion());
      statement.setString(17, dto.status());
      statement.setString(18, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseUpdateReservaHotelFailed(dto.id(), exception);
    }
  }

  private ReservaHotelModel findByIdOrFail(final String id) {
    return getById(id)
        .orElseThrow(() -> ReservaHotelNotFoundException.becauseIdWasNotFound(id));
  }
}
