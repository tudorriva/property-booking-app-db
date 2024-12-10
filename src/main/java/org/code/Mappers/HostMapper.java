package org.code.Mappers;

import org.code.Entities.Host;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HostMapper implements EntityMapper<Host> {
    @Override
    public String getInsertValues(Host host) {
        return host.getId() + ", '" + host.getName() + "', '" + host.getEmail() + "', '" + host.getPhone() + "', " + host.getHostRating();
    }

    @Override
    public Host mapRow(ResultSet rs) throws SQLException {
        return new Host(rs.getInt("userID"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), rs.getDouble("hostRating"));
    }

    @Override
    public String getUpdateValues(Host host) {
        return "name = '" + host.getName() + "', email = '" + host.getEmail() + "', phone = '" + host.getPhone() + "', hostRating = " + host.getHostRating();
    }

    @Override
    public int getId(Host host) {
        return host.getId();
    }
}