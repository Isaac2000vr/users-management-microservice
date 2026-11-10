package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ReservaHotelPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.ReservaHotelEntity;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ReservaHotelPersistenceMapper {

  public ReservaHotelPersistenceDto fromModelToDto(final ReservaHotelModel reserva) {
    return new ReservaHotelPersistenceDto(
        reserva.getId().value(),
        reserva.getFecha(),
        reserva.getHotel(),
        reserva.getHuesped(),
        reserva.getFechaInicio(),
        reserva.getFechaFin(),
        reserva.getValor(),
        reserva.getHabitacion(),
        reserva.getNumAcompanantes(),
        reserva.getPais(),
        reserva.getDepartamento(),
        reserva.getCiudad(),
        reserva.getHoraCheckin(),
        reserva.getHoraCheckout(),
        reserva.getEmpleadoAtiende(),
        reserva.getEmpleadoDespide(),
        reserva.getDescripcion(),
        reserva.getStatus().name(),
        null,
        null);
  }

  public ReservaHotelEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new ReservaHotelEntity(
        resultSet.getString("id"),
        resultSet.getString("fecha"),
        resultSet.getString("hotel"),
        resultSet.getString("huesped"),
        resultSet.getString("fecha_inicio"),
        resultSet.getString("fecha_fin"),
        resultSet.getDouble("valor"),
        resultSet.getString("habitacion"),
        resultSet.getInt("num_acompanantes"),
        resultSet.getString("pais"),
        resultSet.getString("departamento"),
        resultSet.getString("ciudad"),
        resultSet.getString("hora_checkin"),
        resultSet.getString("hora_checkout"),
        resultSet.getString("empleado_atiende"),
        resultSet.getString("empleado_despide"),
        resultSet.getString("descripcion"),
        resultSet.getString("status"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public ReservaHotelModel fromEntityToModel(final ReservaHotelEntity entity) {
    return new ReservaHotelModel(
        new ReservaHotelId(entity.id()),
        entity.fecha(),
        entity.hotel(),
        entity.huesped(),
        entity.fechaInicio(),
        entity.fechaFin(),
        entity.valor(),
        entity.habitacion(),
        entity.numAcompanantes(),
        entity.pais(),
        entity.departamento(),
        entity.ciudad(),
        entity.horaCheckin(),
        entity.horaCheckout(),
        entity.empleadoAtiende(),
        entity.empleadoDespide(),
        entity.descripcion(),
        ReservaHotelStatus.fromString(entity.status()));
  }

  public ReservaHotelModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<ReservaHotelModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<ReservaHotelModel> reservas = new ArrayList<>();
    while (resultSet.next()) {
      reservas.add(fromResultSetToModel(resultSet));
    }
    return reservas;
  }
}
