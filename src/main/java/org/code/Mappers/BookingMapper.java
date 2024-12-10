package org.code.Mappers;

import org.code.Entities.Booking;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingMapper implements EntityMapper<Booking> {
    @Override
    public String getInsertValues(Booking booking) {
        return booking.getId() + ", '" + booking.getCheckInDate() + "', '" + booking.getCheckOutDate() + "', " + booking.getTotalPrice() + ", " + booking.getGuestID() + ", " + booking.getPropertyID() + ", " + booking.getPayment().getId();
    }

    @Override
    public Booking mapRow(ResultSet rs) throws SQLException {
        // Assuming PaymentMapper and other dependencies are properly implemented
        return new Booking(rs.getInt("bookingID"), rs.getDate("checkOutDate"), rs.getDate("checkInDate"), rs.getDouble("totalPrice"), rs.getInt("guestID"), rs.getInt("propertyID"), new PaymentMapper().mapRow(rs));
    }

    @Override
    public String getUpdateValues(Booking booking) {
        return "checkInDate = '" + booking.getCheckInDate() + "', checkOutDate = '" + booking.getCheckOutDate() + "', totalPrice = " + booking.getTotalPrice() + ", guestID = " + booking.getGuestID() + ", propertyID = " + booking.getPropertyID() + ", paymentID = " + booking.getPayment().getId();
    }

    @Override
    public int getId(Booking booking) {
        return booking.getId();
    }
}