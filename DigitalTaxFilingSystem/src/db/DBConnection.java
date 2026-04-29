package db;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection {

        // ✅ Change port if your XAMPP MySQL runs on 3307
        private static final String DB_NAME = "tax_system";
        private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
        private static final String URL = BASE_URL + DB_NAME + "?useSSL=false&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASS = ""; // XAMPP default is empty password
        private static final String LOCAL_DRIVER_JAR = "db/mysql-connector-j-9.1.0.jar";

        static {
            ensureDriver();
        }

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

    public static String getDbName() { return DB_NAME; }
    public static String getBaseUrl() { return BASE_URL; }
    public static String getUser() { return USER; }
    public static String getPass() { return PASS; }
    public static String getUrl() { return URL; }

    private static void ensureDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return; // already on classpath
        } catch (ClassNotFoundException ignored) {
            // try loading from local jar
        }

        File jar = new File(LOCAL_DRIVER_JAR);
        if (jar.exists()) {
            try {
                URLClassLoader cl = new URLClassLoader(new URL[]{jar.toURI().toURL()}, DBConnection.class.getClassLoader());
                Class<?> drvClass = Class.forName("com.mysql.cj.jdbc.Driver", true, cl);
                Driver drv = (Driver) drvClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DriverShim(drv));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(null,
                "MySQL driver not found. Place mysql-connector-j*.jar on classpath or in db/.");
    }

    // Simple wrapper so DriverManager accepts drivers loaded from a custom classloader
    private static class DriverShim implements Driver {
        private final Driver d;
        DriverShim(Driver d) { this.d = d; }
        @Override public boolean acceptsURL(String url) throws SQLException { return d.acceptsURL(url); }
        @Override public Connection connect(String url, Properties info) throws SQLException { return d.connect(url, info); }
        @Override public int getMajorVersion() { return d.getMajorVersion(); }
        @Override public int getMinorVersion() { return d.getMinorVersion(); }
        @Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException { return d.getPropertyInfo(url, info); }
        @Override public boolean jdbcCompliant() { return d.jdbcCompliant(); }
        @Override public Logger getParentLogger() throws SQLFeatureNotSupportedException { return d.getParentLogger(); }
    }
}
