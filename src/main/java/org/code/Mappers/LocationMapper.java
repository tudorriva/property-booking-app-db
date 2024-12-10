package org.code.Mappers;

import org.code.Entities.Location;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements EntityMapper<Location> {
    @Override
    public String getInsertValues(Location location) {
        return location.getId() + ", '" + location.getCity() + "', '" + location.getCountry() + "'";
    }

    @Override
    public Location mapRow(ResultSet rs) throws SQLException {
        return new Location(rs.getInt("locationID"), rs.getString("city"), rs.getString("country"));
    }

    @Override
    public String getUpdateValues(Location location) {
        return "city = '" + location.getCity() + "', country = '" + location.getCountry() + "'";
    }

    @Override
    public int getId(Location location) {
        return location.getId();
    }
}