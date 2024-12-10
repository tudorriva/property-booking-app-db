package org.code.Mappers;

import org.code.Entities.Payment;
import org.code.Mappers.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMapper implements EntityMapper<Payment> {
    @Override
    public String getInsertValues(Payment payment) {
        return payment.getId() + ", " + payment.getAmount() + ", '" + payment.getDate() + "', " + payment.isProcessed();
    }

    @Override
    public Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(rs.getInt("paymentID"), rs.getDouble("amount"), rs.getDate("date"));
    }

    @Override
    public String getUpdateValues(Payment payment) {
        return "amount = " + payment.getAmount() + ", date = '" + payment.getDate() + "', processed = " + payment.isProcessed();
    }

    @Override
    public int getId(Payment payment) {
        return payment.getId();
    }
}