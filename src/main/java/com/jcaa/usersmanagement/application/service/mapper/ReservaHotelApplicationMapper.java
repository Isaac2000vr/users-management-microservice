package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaHotelApplicationMapper {

  public ReservaHotelModel fromCreateCommandToModel(final CreateReservaHotelCommand command) {
    return ReservaHotelModel.create(
        new ReservaHotelId(command.id()),
        command.fecha(),
        command.hotel(),
        command.huesped(),
        command.fechaInicio(),
        command.fechaFin(),
        command.valor(),
        command.habitacion(),
        command.numAcompanantes(),
        command.pais(),
        command.departamento(),
        command.ciudad(),
        command.horaCheckin(),
        command.horaCheckout(),
        command.empleadoAtiende(),
        command.empleadoDespide(),
        command.descripcion());
  }

  public ReservaHotelModel fromUpdateCommandToModel(final UpdateReservaHotelCommand command) {
    return new ReservaHotelModel(
        new ReservaHotelId(command.id()),
        command.fecha(),
        command.hotel(),
        command.huesped(),
        command.fechaInicio(),
        command.fechaFin(),
        command.valor(),
        command.habitacion(),
        command.numAcompanantes(),
        command.pais(),
        command.departamento(),
        command.ciudad(),
        command.horaCheckin(),
        command.horaCheckout(),
        command.empleadoAtiende(),
        command.empleadoDespide(),
        command.descripcion(),
        ReservaHotelStatus.fromString(command.status()));
  }
}
