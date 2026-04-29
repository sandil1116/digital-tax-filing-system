package dao;

import db.DBConnection;
import model.Company;
import model.Individual;
import model.TaxPayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaxPayerDAO {

    public static boolean existsTaxpayer(String taxpayerID) {
        String sql = "SELECT taxpayerID FROM taxpayers WHERE taxpayerID=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, taxpayerID);
                return pst.executeQuery().next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertTaxPayer(TaxPayer tp, String password) {
        if (existsTaxpayer(tp.getTaxpayerID())) return false;

        String sql = "INSERT INTO taxpayers (taxpayerID, name, income, taxType, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, tp.getTaxpayerID());
                pst.setString(2, tp.getName());
                pst.setDouble(3, tp.getIncome());
                pst.setString(4, tp.getTaxType());
                pst.setString(5, password);
                return pst.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TaxPayer login(String taxpayerID, String password) {
        String sql = "SELECT taxpayerID, name, income, taxType FROM taxpayers WHERE taxpayerID=? AND password=?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return null;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, taxpayerID);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String id = rs.getString("taxpayerID");
                    String name = rs.getString("name");
                    double income = rs.getDouble("income");
                    String type = rs.getString("taxType");

                    if ("Company".equalsIgnoreCase(type)) return new Company(id, name, income);
                    return new Individual(id, name, income);
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
