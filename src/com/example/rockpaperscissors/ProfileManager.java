package com.example.rockpaperscissors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


    public class ProfileManager {
        private Connection connection;

        public ProfileManager(Connection connection) {
            this.connection = connection;
        }

        public void createProfile(String username) {
            String sql = "INSERT INTO profiles (username) VALUES (?)";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.executeUpdate();
                System.out.println("Profile " + username + " has been created.");
            } catch (SQLException e) {
                System.out.println("Error during profile creation: " + e.getMessage());
            }
        }

        public void deleteProfile(int id) {
            String sql = "DELETE FROM profiles WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                System.out.println("Profile with ID " + id + " has been deleted.");
            } catch (SQLException e) {
                System.out.println("Error during profile removal: " + e.getMessage());
            }
        }

        public PlayerProfile selectProfile(int id) {
            String sql = "SELECT * FROM profiles WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new PlayerProfile(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getInt("wins"),
                                rs.getInt("losses"),
                                rs.getInt("ties")
                        );
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error during profile selection: " + e.getMessage());
            }
            return null;
        }
    }
