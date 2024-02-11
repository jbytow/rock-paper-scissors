package com.example.rockpaperscissors;

import java.util.Random;

public class RockPaperScissorsGame {
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;

    private final ProfileManager profileManager;
    private PlayerProfile activeProfile;



    public RockPaperScissorsGame(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void setActiveProfile(PlayerProfile profile) {
        this.activeProfile = profile;
        // Setting local variables based on the profile
        if (profile != null) {
            wins = profile.getWins();
            losses = profile.getLosses();
            ties = profile.getTies();
        }
    }

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

    public enum GameResult {
        WIN, LOSE, TIE
    }


    public String playGame(String playerMoveString) {
        Move playerMove = Move.fromString(playerMoveString);
        Move computerMove = getRandomMove();
        GameResult result = determineWinner(playerMove, computerMove);

        // Update profile statistics based on result
        if (activeProfile != null) {
            updateProfileStats(result);
        }


        // Generate result message
        String resultMessage = switch (result) {
            case WIN -> "You win!";
            case LOSE -> "You lose!";
            case TIE -> "It's a tie!";
        };

        return String.format("You chose %s, computer chose %s. %s\nWins: %d, Losses: %d, Ties: %d\n",
                playerMove, computerMove, resultMessage,
                activeProfile.getWins(), activeProfile.getLosses(), activeProfile.getTies());
    }

    private Move getRandomMove() {
        Random random = new Random();
        return Move.values()[random.nextInt(Move.values().length)];
    }

    private GameResult determineWinner(Move playerMove, Move computerMove) {
        if (playerMove == computerMove) return GameResult.TIE;
        switch (playerMove) {
            case ROCK -> {
                return (computerMove == Move.SCISSORS) ? GameResult.WIN : GameResult.LOSE;
            }
            case PAPER -> {
                return (computerMove == Move.ROCK) ? GameResult.WIN : GameResult.LOSE;
            }
            case SCISSORS -> {
                return (computerMove == Move.PAPER) ? GameResult.WIN : GameResult.LOSE;
            }
            default -> throw new IllegalStateException("Unexpected value: " + playerMove);
        }
    }

    private void updateProfileStats(GameResult result) {
        switch (result) {
            case WIN -> {
                activeProfile.setWins(activeProfile.getWins() + 1);
                profileManager.updateWins(activeProfile.getId());
            }
            case LOSE -> {
                activeProfile.setLosses(activeProfile.getLosses() + 1);
                profileManager.updateLosses(activeProfile.getId());
            }
            case TIE -> {
                activeProfile.setTies(activeProfile.getTies() + 1);
                profileManager.updateTies(activeProfile.getId());
            }
        }
    }
}
