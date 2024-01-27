package com.example.rockpaperscissors;

import java.util.Random;

public class RockPaperScissorsGame {
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;

    public String playGame(String playerMove) {
        String[] rps = {"rock", "paper", "scissors"};
        Random random = new Random();
        String computerMove = rps[random.nextInt(rps.length)];

        String result;
        if (playerMove.equals(computerMove)) {
            result = "It's a tie!";
            ties++;
        } else if ((playerMove.equals("rock") && computerMove.equals("scissors")) ||
                (playerMove.equals("paper") && computerMove.equals("rock")) ||
                (playerMove.equals("scissors") && computerMove.equals("paper"))) {
            result = "You win!";
            wins++;
        } else {
            result = "You lose!";
            losses++;
        }

        return "You chose " + playerMove + ", computer chose " + computerMove + ". " + result + "\n" +
                "Wins: " + wins + ", Losses: " + losses + ", Ties: " + ties + "\n";
    }
}
