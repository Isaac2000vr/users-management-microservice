package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DeleteReservaHotelUseCase {
  void execute(@NotNull @Valid DeleteReservaHotelCommand command);
}
