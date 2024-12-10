package org.code.Entities;

import java.util.Date;

public interface Bookable {
    boolean checkAvailability(Date checkIn, Date checkOut);
}
