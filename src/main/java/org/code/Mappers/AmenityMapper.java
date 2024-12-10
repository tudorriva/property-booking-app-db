package org.code.Mappers;

import org.code.Entities.Amenity;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AmenityMapper implements EntityMapper<Amenity> {
    @Override
    public String getInsertValues(Amenity amenity) {
        return amenity.getId() + ", '" + amenity.getName() + "', '" + amenity.getDescription() + "'";
    }

    @Override
    public Amenity mapRow(ResultSet rs) throws SQLException {
        return new Amenity(rs.getInt("amenityID"), rs.getString("name"), rs.getString("description"));
    }

    @Override
    public String getUpdateValues(Amenity amenity) {
        return "name = '" + amenity.getName() + "', description = '" + amenity.getDescription() + "'";
    }

    @Override
    public int getId(Amenity amenity) {
        return amenity.getId();
    }
}