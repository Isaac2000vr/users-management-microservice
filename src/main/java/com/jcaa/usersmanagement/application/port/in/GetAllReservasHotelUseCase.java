package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import java.util.List;

public interface GetAllReservasHotelUseCase {
  List<ReservaHotelModel> execute();
}
