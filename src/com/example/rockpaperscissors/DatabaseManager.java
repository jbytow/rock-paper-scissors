package com.example.rockpaperscissors;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database/rockpaperscissors.db"; // database path

    public DatabaseManager() {
        initializeDatabase();
        createTable();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Driver name: " + meta.getDriverName());
                System.out.println("Database connection has been established.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTable() {
        // sql new table creation
        String sql = "CREATE TABLE IF NOT EXISTS results (\n"
                + " id integer PRIMARY KEY,\n"
                + " playerMove text NOT NULL,\n"
                + " computerMove text NOT NULL,\n"
                + " outcome text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}