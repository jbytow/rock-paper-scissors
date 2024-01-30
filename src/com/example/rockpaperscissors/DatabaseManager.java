package com.example.rockpaperscissors;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database/rockpaperscissors.db"; // database path
    private Connection connection;

    public DatabaseManager() {
        connection = initializeDatabase();
        createTable();
    }

    private Connection initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Driver name: " + meta.getDriverName());
                System.out.println("Database connection has been established.");
                return conn;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTable() {
        String sqlCreateResults = "CREATE TABLE IF NOT EXISTS results (\n"
                + " id integer PRIMARY KEY,\n"
                + " profile_id integer,\n"
                + " playerMove text NOT NULL,\n"
                + " computerMove text NOT NULL,\n"
                + " outcome text NOT NULL,\n"
                + " FOREIGN KEY (profile_id) REFERENCES profiles(id)\n"
                + ");";

        String sqlCreateProfiles = "CREATE TABLE IF NOT EXISTS profiles (\n"
                + " id integer PRIMARY KEY,\n"
                + " username text NOT NULL,\n"
                + " wins integer NOT NULL DEFAULT 0,\n"
                + " losses integer NOT NULL DEFAULT 0,\n"
                + " ties integer NOT NULL DEFAULT 0\n"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            // create results table
            stmt.execute(sqlCreateResults);
            // create profiles table
            stmt.execute(sqlCreateProfiles);
        } catch (SQLException e) {
            System.out.println("Error during table creation: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}