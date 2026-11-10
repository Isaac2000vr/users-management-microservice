package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateReservaHotelPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.mapper.ReservaHotelApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateReservaHotelService implements UpdateReservaHotelUseCase {

  private final UpdateReservaHotelPort updateReservaHotelPort;
  private final GetReservaHotelByIdPort getReservaHotelByIdPort;
  private final Validator validator;

  @Override
  public ReservaHotelModel execute(final UpdateReservaHotelCommand command) {
    validateCommand(command);

    getReservaHotelByIdPort
        .getById(command.id())
        .orElseThrow(() -> ReservaHotelNotFoundException.becauseIdWasNotFound(command.id()));

    final ReservaHotelModel reserva = ReservaHotelApplicationMapper.fromUpdateCommandToModel(command);
    final ReservaHotelModel updated = updateReservaHotelPort.update(reserva);

    log.info("Reserva actualizada con id: {}", updated.getId().value());
    return updated;
  }

  private void validateCommand(final UpdateReservaHotelCommand command) {
    final Set<ConstraintViolation<UpdateReservaHotelCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
