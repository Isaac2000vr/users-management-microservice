package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidReservaHotelException;

public enum ReservaHotelStatus {
  PENDING,
  CONFIRMED,
  CANCELLED,
  COMPLETED;

  public static ReservaHotelStatus fromString(final String value) {
    for (final ReservaHotelStatus status : values()) {
      if (status.name().equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw InvalidReservaHotelException.becauseStatusIsInvalid(value);
  }
}
