package dao;

import db.DBConnection;
import model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public static Admin login(String username, String password) {
        String sql = "SELECT admin_id, username FROM admin WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return null;

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    return new Admin(rs.getString("admin_id"), rs.getString("username"));
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
