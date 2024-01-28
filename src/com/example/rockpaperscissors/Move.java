package com.example.rockpaperscissors;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public static Move fromString(String move) {
        switch (move.toLowerCase()) {
            case "rock":
                return ROCK;
            case "paper":
                return PAPER;
            case "scissors":
                return SCISSORS;
            default:
                throw new IllegalArgumentException("Invalid move: " + move);
        }
    }
}
