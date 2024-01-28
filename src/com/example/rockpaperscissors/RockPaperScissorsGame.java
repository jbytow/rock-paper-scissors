package com.example.rockpaperscissors;

import java.util.Random;

public class RockPaperScissorsGame {
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;

    public String playGame(String playerMoveString) {
        Move playerMove = Move.fromString(playerMoveString);
        Move computerMove = getRandomMove();
        String result = determineWinner(playerMove, computerMove);

        return "You chose " + playerMove + ", computer chose " + computerMove + ". " + result + "\n" +
                "Wins: " + wins + ", Losses: " + losses + ", Ties: " + ties + "\n";
    }

    private Move getRandomMove() {
        Random random = new Random();
        return Move.values()[random.nextInt(Move.values().length)];
    }

    private String determineWinner(Move playerMove, Move computerMove) {
        if (playerMove == computerMove) {
            ties++;
            return "It's a tie!";
        }

        switch (playerMove) {
            case ROCK:
                return (computerMove == Move.SCISSORS) ? win() : lose();
            case PAPER:
                return (computerMove == Move.ROCK) ? win() : lose();
            case SCISSORS:
                return (computerMove == Move.PAPER) ? win() : lose();
            default:
                throw new IllegalStateException("Unexpected value: " + playerMove);
        }
    }

    private String win() {
        wins++;
        return "You win!";
    }

    private String lose() {
        losses++;
        return "You lose!";
    }
}
