package dao;

import db.DBConnection;
import model.Payment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAO {

    public static boolean insertPayment(Payment p) {
        String sql = "INSERT INTO payments (paymentID, filingID, amount, method, verified, paymentDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, p.getPaymentID());
                pst.setString(2, p.getFiling().getFilingID());
                pst.setDouble(3, p.getAmount());
                pst.setString(4, p.getMethod());
                pst.setBoolean(5, p.isVerified());
                pst.setDate(6, new Date(System.currentTimeMillis()));
                return pst.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getPaymentsByTaxpayer(String taxpayerID) {
        String sql =
                "SELECT p.paymentID, p.filingID, p.amount, p.method, p.verified " +
                "FROM payments p " +
                "JOIN filings f ON p.filingID = f.filingID " +
                "WHERE f.taxpayerID=? " +
                "ORDER BY p.paymentDate DESC";

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return null;

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, taxpayerID);
            return pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
