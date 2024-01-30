package com.example.rockpaperscissors;

public class PlayerProfile {
    private int id;
    private String username;
    private int wins;
    private int losses;
    private int ties;

    public PlayerProfile(int id, String username, int wins, int losses, int ties) {
        this.id = id;
        this.username = username;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getTotalGames() {
        return wins + losses + ties;
    }

    public double getWinPercentage() {
        return (double) wins / getTotalGames() * 100;
    }

    public double getLossPercentage() {
        return (double) losses / getTotalGames() * 100;
    }

    public double getTiePercentage() {
        return (double) ties / getTotalGames() * 100;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
