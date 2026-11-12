package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllReservasHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.GetReservaHotelByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateReservaHotelRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ReservaHotelRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.ReservaHotelRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-hotel")
@RequiredArgsConstructor
public class ReservaHotelRestController implements ReservaHotelRestControllerDocs {

  private final CreateReservaHotelUseCase createReservaHotelUseCase;
  private final UpdateReservaHotelUseCase updateReservaHotelUseCase;
  private final DeleteReservaHotelUseCase deleteReservaHotelUseCase;
  private final GetReservaHotelByIdUseCase getReservaHotelByIdUseCase;
  private final GetAllReservasHotelUseCase getAllReservasHotelUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReservaHotelRestResponse create(
      @Valid @RequestBody final CreateReservaHotelRestRequest request) {
    final CreateReservaHotelCommand command = ReservaHotelRestMapper.toCreateCommand(request);
    final ReservaHotelModel reserva = createReservaHotelUseCase.execute(command);
    return ReservaHotelRestMapper.toResponse(reserva);
  }

  @GetMapping
  public List<ReservaHotelRestResponse> getAll() {
    return ReservaHotelRestMapper.toResponseList(getAllReservasHotelUseCase.execute());
  }

  @GetMapping("/{id}")
  public ReservaHotelRestResponse getById(@PathVariable final String id) {
    final GetReservaHotelByIdQuery query = ReservaHotelRestMapper.toGetByIdQuery(id);
    final ReservaHotelModel reserva = getReservaHotelByIdUseCase.execute(query);
    return ReservaHotelRestMapper.toResponse(reserva);
  }

  @PutMapping("/{id}")
  public ReservaHotelRestResponse update(
      @PathVariable final String id,
      @Valid @RequestBody final UpdateReservaHotelRestRequest request) {
    final UpdateReservaHotelCommand command = ReservaHotelRestMapper.toUpdateCommand(id, request);
    final ReservaHotelModel reserva = updateReservaHotelUseCase.execute(command);
    return ReservaHotelRestMapper.toResponse(reserva);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    final DeleteReservaHotelCommand command = ReservaHotelRestMapper.toDeleteCommand(id);
    deleteReservaHotelUseCase.execute(command);
  }
}
