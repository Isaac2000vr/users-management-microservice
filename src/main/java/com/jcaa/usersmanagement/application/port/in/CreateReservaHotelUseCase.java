package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateReservaHotelUseCase {
  ReservaHotelModel execute(@NotNull @Valid CreateReservaHotelCommand command);
}
