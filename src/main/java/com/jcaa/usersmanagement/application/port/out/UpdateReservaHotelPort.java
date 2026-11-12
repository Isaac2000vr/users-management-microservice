package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;

public interface UpdateReservaHotelPort {
  ReservaHotelModel update(ReservaHotelModel reserva);
}
