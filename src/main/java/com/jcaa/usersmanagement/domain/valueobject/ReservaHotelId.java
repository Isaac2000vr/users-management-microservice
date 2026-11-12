package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidReservaHotelException;

public record ReservaHotelId(String value) {

  public ReservaHotelId {
    if (value == null || value.isBlank()) {
      throw InvalidReservaHotelException.becauseFieldIsBlank("id");
    }
  }
}
