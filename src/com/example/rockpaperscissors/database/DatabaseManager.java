package com.example.rockpaperscissors.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database/rockpaperscissors.db"; // database path
    private final Connection connection;

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
        String sqlCreateResults = """
                CREATE TABLE IF NOT EXISTS results (
                 id integer PRIMARY KEY,
                 profile_id integer,
                 playerMove text NOT NULL,
                 computerMove text NOT NULL,
                 outcome text NOT NULL,
                 FOREIGN KEY (profile_id) REFERENCES profiles(id)
                );""";

        String sqlCreateProfiles = """
                CREATE TABLE IF NOT EXISTS profiles (
                 id integer PRIMARY KEY,
                 username text NOT NULL,
                 wins integer NOT NULL DEFAULT 0,
                 losses integer NOT NULL DEFAULT 0,
                 ties integer NOT NULL DEFAULT 0
                );""";

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