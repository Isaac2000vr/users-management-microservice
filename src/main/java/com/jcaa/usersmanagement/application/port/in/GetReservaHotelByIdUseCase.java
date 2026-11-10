package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetReservaHotelByIdUseCase {
  ReservaHotelModel execute(@NotNull @Valid GetReservaHotelByIdQuery query);
}
