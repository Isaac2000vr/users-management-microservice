package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.out.SaveReservaHotelPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.mapper.ReservaHotelApplicationMapper;
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
public class CreateReservaHotelService implements CreateReservaHotelUseCase {

  private final SaveReservaHotelPort saveReservaHotelPort;
  private final Validator validator;

  @Override
  public ReservaHotelModel execute(final CreateReservaHotelCommand command) {
    validateCommand(command);

    final ReservaHotelModel reserva = ReservaHotelApplicationMapper.fromCreateCommandToModel(command);
    final ReservaHotelModel saved = saveReservaHotelPort.save(reserva);

    log.info("Reserva creada con id: {}", saved.getId().value());
    return saved;
  }

  private void validateCommand(final CreateReservaHotelCommand command) {
    final Set<ConstraintViolation<CreateReservaHotelCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
