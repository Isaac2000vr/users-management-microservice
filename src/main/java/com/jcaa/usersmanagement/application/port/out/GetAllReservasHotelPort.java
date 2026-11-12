package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import java.util.List;

public interface GetAllReservasHotelPort {
  List<ReservaHotelModel> getAll();
}
