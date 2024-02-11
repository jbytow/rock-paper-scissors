package com.example.rockpaperscissors;

import java.util.concurrent.ThreadLocalRandom;

public class RockPaperScissorsGame {
    private int sessionWins = 0;
    private int sessionLosses = 0;
    private int sessionTies = 0;

    private final ProfileManager profileManager;
    private PlayerProfile activeProfile;
    private static final Move[] MOVES = Move.values();



    public RockPaperScissorsGame(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void setActiveProfile(PlayerProfile profile) {
        this.activeProfile = profile;
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

        return String.format("You chose %s, computer chose %s. %s",
                playerMove, computerMove, resultMessage);
    }

    private Move getRandomMove() {
        return MOVES[ThreadLocalRandom.current().nextInt(MOVES.length)];
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
                sessionWins++;
            }
            case LOSE -> {
                activeProfile.setLosses(activeProfile.getLosses() + 1);
                profileManager.updateLosses(activeProfile.getId());
                sessionLosses++;
            }
            case TIE -> {
                activeProfile.setTies(activeProfile.getTies() + 1);
                profileManager.updateTies(activeProfile.getId());
                sessionTies++;
            }
        }
    }
    public void resetSessionStats() {
        sessionWins = 0;
        sessionLosses = 0;
        sessionTies = 0;
    }
}
