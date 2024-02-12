package com.example.rockpaperscissors.game;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public static Move fromString(String move) {
        return switch (move.toLowerCase()) {
            case "rock" -> ROCK;
            case "paper" -> PAPER;
            case "scissors" -> SCISSORS;
            default -> throw new IllegalArgumentException("Invalid move: " + move);
        };
    }
}
