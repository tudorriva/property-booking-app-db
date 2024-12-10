package org.code.Mappers;

import org.code.Entities.CancellationPolicy;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CancellationPolicyMapper implements EntityMapper<CancellationPolicy> {
    @Override
    public String getInsertValues(CancellationPolicy policy) {
        return policy.getId() + ", '" + policy.getDescription() + "'";
    }

    @Override
    public CancellationPolicy mapRow(ResultSet rs) throws SQLException {
        return new CancellationPolicy(rs.getInt("policyID"), rs.getString("description"));
    }

    @Override
    public String getUpdateValues(CancellationPolicy policy) {
        return "description = '" + policy.getDescription() + "'";
    }

    @Override
    public int getId(CancellationPolicy policy) {
        return policy.getId();
    }
}