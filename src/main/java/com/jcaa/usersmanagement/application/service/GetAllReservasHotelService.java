package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllReservasHotelUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllReservasHotelPort;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllReservasHotelService implements GetAllReservasHotelUseCase {

  private final GetAllReservasHotelPort getAllReservasHotelPort;

  @Override
  public List<ReservaHotelModel> execute() {
    return getAllReservasHotelPort.getAll();
  }
}
