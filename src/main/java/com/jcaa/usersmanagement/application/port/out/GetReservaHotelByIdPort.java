package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import java.util.Optional;

public interface GetReservaHotelByIdPort {
  Optional<ReservaHotelModel> getById(String id);
}
