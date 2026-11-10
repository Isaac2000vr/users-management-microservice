package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetReservaHotelByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetReservaHotelByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.exception.ReservaHotelNotFoundException;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetReservaHotelByIdService implements GetReservaHotelByIdUseCase {

  private final GetReservaHotelByIdPort getReservaHotelByIdPort;
  private final Validator validator;

  @Override
  public ReservaHotelModel execute(final GetReservaHotelByIdQuery query) {
    validateQuery(query);

    return getReservaHotelByIdPort
        .getById(query.id())
        .orElseThrow(() -> ReservaHotelNotFoundException.becauseIdWasNotFound(query.id()));
  }

  private void validateQuery(final GetReservaHotelByIdQuery query) {
    final Set<ConstraintViolation<GetReservaHotelByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
