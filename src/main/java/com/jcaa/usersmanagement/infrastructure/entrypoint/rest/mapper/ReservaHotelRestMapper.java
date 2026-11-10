package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ReservaHotelRestResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ReservaHotelRestMapper {

  public CreateReservaHotelCommand toCreateCommand(final CreateReservaHotelRestRequest request) {
    return new CreateReservaHotelCommand(
        request.id(),
        request.fecha(),
        request.hotel(),
        request.huesped(),
        request.fechaInicio(),
        request.fechaFin(),
        request.valor(),
        request.habitacion(),
        request.numAcompanantes(),
        request.pais(),
        request.departamento(),
        request.ciudad(),
        request.horaCheckin(),
        request.horaCheckout(),
        request.empleadoAtiende(),
        request.empleadoDespide(),
        request.descripcion());
  }

  public UpdateReservaHotelCommand toUpdateCommand(
      final String id, final UpdateReservaHotelRestRequest request) {
    return new UpdateReservaHotelCommand(
        id,
        request.fecha(),
        request.hotel(),
        request.huesped(),
        request.fechaInicio(),
        request.fechaFin(),
        request.valor(),
        request.habitacion(),
        request.numAcompanantes(),
        request.pais(),
        request.departamento(),
        request.ciudad(),
        request.horaCheckin(),
        request.horaCheckout(),
        request.empleadoAtiende(),
        request.empleadoDespide(),
        request.descripcion(),
        request.status());
  }

  public GetReservaHotelByIdQuery toGetByIdQuery(final String id) {
    return new GetReservaHotelByIdQuery(id);
  }

  public DeleteReservaHotelCommand toDeleteCommand(final String id) {
    return new DeleteReservaHotelCommand(id);
  }

  public ReservaHotelRestResponse toResponse(final ReservaHotelModel reserva) {
    return new ReservaHotelRestResponse(
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
        reserva.getStatus().name());
  }

  public List<ReservaHotelRestResponse> toResponseList(
      final List<ReservaHotelModel> reservas) {
    return reservas.stream().map(ReservaHotelRestMapper::toResponse).toList();
  }
}
