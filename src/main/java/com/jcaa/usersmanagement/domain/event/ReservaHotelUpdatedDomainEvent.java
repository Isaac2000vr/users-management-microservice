package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class ReservaHotelUpdatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "reserva_hotel.updated";

  private final ReservaHotelModel reserva;

  public ReservaHotelUpdatedDomainEvent(final ReservaHotelModel reserva) {
    super(EVENT_NAME);
    this.reserva = reserva;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of(
        "id", reserva.getId().value(),
        "hotel", reserva.getHotel(),
        "huesped", reserva.getHuesped(),
        "status", reserva.getStatus().name());
  }
}
