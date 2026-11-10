package com.jcaa.usersmanagement.domain.event;

import java.util.Map;
import lombok.Getter;

@Getter
public final class ReservaHotelDeletedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "reserva_hotel.deleted";

  private final String reservaId;

  public ReservaHotelDeletedDomainEvent(final String reservaId) {
    super(EVENT_NAME);
    this.reservaId = reservaId;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of("id", reservaId);
  }
}
