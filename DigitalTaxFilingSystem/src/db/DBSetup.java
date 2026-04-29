package db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBSetup {

    public static void bootstrap() {
        // Create database if missing
        try (Connection rootCon = DriverManager.getConnection(
                DBConnection.getBaseUrl() + "?useSSL=false&serverTimezone=UTC",
                DBConnection.getUser(),
                DBConnection.getPass());
             Statement rootStmt = rootCon.createStatement()) {

            rootStmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DBConnection.getDbName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to ensure database exists.\n" + e.getMessage());
            return;
        }

        // Create tables if missing
        try (Connection con = DriverManager.getConnection(
                DBConnection.getUrl(),
                DBConnection.getUser(),
                DBConnection.getPass());
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE TABLE IF NOT EXISTS taxpayers (" +
                    "taxpayerID VARCHAR(32) PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(150) NOT NULL," +
                    "taxType VARCHAR(20) NOT NULL," +
                    "password VARCHAR(100) NOT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            // Migrate older schema that used an income column instead of email
            try {
                st.executeUpdate("ALTER TABLE taxpayers CHANGE COLUMN income email VARCHAR(150) NOT NULL");
            } catch (Exception ignored) { /* column might already be renamed/added */ }
            try {
                st.executeUpdate("ALTER TABLE taxpayers ADD COLUMN email VARCHAR(150) NOT NULL AFTER name");
            } catch (Exception ignored) { /* column already exists */ }
            try {
                st.executeUpdate("ALTER TABLE taxpayers DROP COLUMN income");
            } catch (Exception ignored) { /* column already dropped */ }

            st.executeUpdate("CREATE TABLE IF NOT EXISTS filings (" +
                    "filingID VARCHAR(32) PRIMARY KEY," +
                    "taxpayerID VARCHAR(32) NOT NULL," +
                    "taxAmount DOUBLE NOT NULL," +
                    "status VARCHAR(32) NOT NULL," +
                    "filingDate DATE NOT NULL," +
                    "CONSTRAINT fk_filings_taxpayer FOREIGN KEY (taxpayerID) REFERENCES taxpayers(taxpayerID) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS payments (" +
                    "paymentID VARCHAR(32) PRIMARY KEY," +
                    "filingID VARCHAR(32) NOT NULL," +
                    "amount DOUBLE NOT NULL," +
                    "method VARCHAR(32) NOT NULL," +
                    "verified BOOLEAN NOT NULL DEFAULT TRUE," +
                    "paymentDate DATE NOT NULL," +
                    "CONSTRAINT fk_payments_filing FOREIGN KEY (filingID) REFERENCES filings(filingID) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS admin (" +
                    "admin_id VARCHAR(32) PRIMARY KEY," +
                    "username VARCHAR(64) UNIQUE NOT NULL," +
                    "password VARCHAR(64) NOT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("INSERT INTO admin (admin_id, username, password) " +
                    "VALUES ('ADMIN1','admin','admin') " +
                    "ON DUPLICATE KEY UPDATE username=username");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to create tables.\n" + e.getMessage());
        }
    }
}
