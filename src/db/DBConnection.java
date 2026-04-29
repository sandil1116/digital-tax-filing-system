package db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // ✅ Change port if your XAMPP MySQL runs on 3307
    private static final String URL =
            "jdbc:mysql://localhost:3306/tax_system?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // XAMPP default is empty password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "DB Connection Failed!\n\n" +
                            "Check:\n" +
                            "1) XAMPP MySQL is RUNNING\n" +
                            "2) Port in DBConnection is correct (3306/3307)\n" +
                            "3) Database exists: tax_system\n\n" +
                            "Error: " + e.getMessage());
            return null;
        }
    }
}
