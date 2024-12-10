package org.code.Mappers;

import org.code.Entities.Review;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewMapper implements EntityMapper<Review> {
    @Override
    public String getInsertValues(Review review) {
        return review.getId() + ", " + review.getGuestID() + ", " + review.getPropertyID() + ", " + review.getRating() + ", '" + review.getComment() + "', '" + review.getDate() + "'";
    }

    @Override
    public Review mapRow(ResultSet rs) throws SQLException {
        return new Review(rs.getInt("reviewID"), rs.getInt("guestID"), rs.getInt("propertyID"), rs.getDouble("rating"), rs.getString("comment"), rs.getDate("date"));
    }

    @Override
    public String getUpdateValues(Review review) {
        return "guestID = " + review.getGuestID() + ", propertyID = " + review.getPropertyID() + ", rating = " + review.getRating() + ", comment = '" + review.getComment() + "', date = '" + review.getDate() + "'";
    }

    @Override
    public int getId(Review review) {
        return review.getId();
    }
}