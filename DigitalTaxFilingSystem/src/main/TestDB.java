package main;

import db.DBConnection;

public class TestDB {
    public static void main(String[] args) {
        var con = DBConnection.getConnection();
        System.out.println(con != null ? "DB Connected!" : "DB Connection Failed!");
    }
}
