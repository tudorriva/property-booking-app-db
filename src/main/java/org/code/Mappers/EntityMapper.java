package org.code.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T> {
    String getInsertValues(T entity);
    T mapRow(ResultSet rs) throws SQLException;
    String getUpdateValues(T entity);
    int getId(T entity);
}
