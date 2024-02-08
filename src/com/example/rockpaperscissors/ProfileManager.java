package com.example.rockpaperscissors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private final Connection connection;

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

    public PlayerProfile selectProfileByName(String name) {
        String sql = "SELECT * FROM profiles WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PlayerProfile(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("ties")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error during profile selection by name: " + e.getMessage());
        }
        return null;
    }

    public List<PlayerProfile> getProfiles() {
        List<PlayerProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM profiles";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                profiles.add(new PlayerProfile(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("ties")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error during getting profiles: " + e.getMessage());
        }
        return profiles;
    }

    public void updateWins(int profileId) {
        updateProfileStatistic(profileId, "wins");
    }

    public void updateLosses(int profileId) {
        updateProfileStatistic(profileId, "losses");
    }

    public void updateTies(int profileId) {
        updateProfileStatistic(profileId, "ties");
    }

    private void updateProfileStatistic(int profileId, String statistic) {
        String sql = "UPDATE profiles SET " + statistic + " = " + statistic + " + 1 WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error during updating " + statistic + ": " + e.getMessage());
        }
    }
}
