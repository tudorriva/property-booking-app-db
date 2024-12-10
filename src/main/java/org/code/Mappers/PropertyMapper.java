package org.code.Mappers;

import org.code.Entities.Property;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyMapper implements EntityMapper<Property> {
    @Override
    public String getInsertValues(Property property) {
        return property.getId() + ", '" + property.getAddress() + "', " + property.getPricePerNight() + ", '" + property.getDescription() + "', " + property.getLocation().getId() + ", " + property.getHostID();
    }

    @Override
    public Property mapRow(ResultSet rs) throws SQLException {
        // Assuming LocationMapper and other dependencies are properly implemented
        return new Property(rs.getInt("propertyID"), rs.getString("address"), rs.getDouble("pricePerNight"), rs.getString("description"), new LocationMapper().mapRow(rs), null, null, rs.getInt("hostID"));
    }

    @Override
    public String getUpdateValues(Property property) {
        return "address = '" + property.getAddress() + "', pricePerNight = " + property.getPricePerNight() + ", description = '" + property.getDescription() + "', locationID = " + property.getLocation().getId() + ", hostID = " + property.getHostID();
    }

    @Override
    public int getId(Property property) {
        return property.getId();
    }
}