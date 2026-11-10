package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteReservaHotelPort;
import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
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
public class DeleteReservaHotelService implements DeleteReservaHotelUseCase {

  private final DeleteReservaHotelPort deleteReservaHotelPort;
  private final GetReservaHotelByIdPort getReservaHotelByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteReservaHotelCommand command) {
    validateCommand(command);

    getReservaHotelByIdPort
        .getById(command.id())
        .orElseThrow(() -> ReservaHotelNotFoundException.becauseIdWasNotFound(command.id()));

    deleteReservaHotelPort.delete(command.id());
    log.info("Reserva eliminada con id: {}", command.id());
  }

  private void validateCommand(final DeleteReservaHotelCommand command) {
    final Set<ConstraintViolation<DeleteReservaHotelCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
