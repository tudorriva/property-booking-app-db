package org.code.Mappers;

import org.code.Entities.Guest;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestMapper implements EntityMapper<Guest> {
    @Override
    public String getInsertValues(Guest guest) {
        return guest.getId() + ", '" + guest.getName() + "', '" + guest.getEmail() + "', '" + guest.getPhone() + "', " + guest.getGuestRating();
    }

    @Override
    public Guest mapRow(ResultSet rs) throws SQLException {
        return new Guest(rs.getInt("userID"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), rs.getDouble("guestRating"));
    }

    @Override
    public String getUpdateValues(Guest guest) {
        return "name = '" + guest.getName() + "', email = '" + guest.getEmail() + "', phone = '" + guest.getPhone() + "', guestRating = " + guest.getGuestRating();
    }

    @Override
    public int getId(Guest guest) {
        return guest.getId();
    }
}