package dao;

import db.DBConnection;
import model.TaxFiling;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaxFilingDAO {

    public static boolean insertTaxFiling(TaxFiling filing) {
        String sql = "INSERT INTO filings (filingID, taxpayerID, taxAmount, status, filingDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, filing.getFilingID());
                pst.setString(2, filing.getTaxpayer().getTaxpayerID());
                pst.setDouble(3, filing.getTaxAmount());
                pst.setString(4, filing.getStatus());
                pst.setDate(5, new Date(System.currentTimeMillis()));
                return pst.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateFilingStatus(String filingID, String status) {
        String sql = "UPDATE filings SET status=? WHERE filingID=?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, status);
                pst.setString(2, filingID);
                return pst.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getFilingsByTaxpayer(String taxpayerID) {
        String sql = "SELECT filingID, taxAmount, status, filingDate FROM filings WHERE taxpayerID=? ORDER BY filingDate DESC";
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return null;

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, taxpayerID);
            return pst.executeQuery(); // caller reads
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getAllFilings() {
        String sql = "SELECT filingID, taxpayerID, taxAmount, status, filingDate FROM filings ORDER BY filingDate DESC";
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return null;

            PreparedStatement pst = con.prepareStatement(sql);
            return pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
