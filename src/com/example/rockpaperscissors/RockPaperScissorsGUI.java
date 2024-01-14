package com.example.rockpaperscissors;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RockPaperScissorsGUI {

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    // Method to create and show the GUI
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Rock, Paper, Scissors Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Layout for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");

        // Text area to display results
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);

        // Adding action listeners to buttons
        rockButton.addActionListener(e -> playGame("rock", textArea));
        paperButton.addActionListener(e -> playGame("paper", textArea));
        scissorsButton.addActionListener(e -> playGame("scissors", textArea));

        // Adding buttons to the panel
        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);

        // Adding panel and text area to frame
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Display the window.
        frame.setVisible(true);
    }

    // Method to play the game
    private static void playGame(String playerMove, JTextArea textArea) {
        String[] rps = {"rock", "paper", "scissors"};
        Random random = new Random();
        String computerMove = rps[random.nextInt(rps.length)];

        String result;
        if (playerMove.equals(computerMove)) {
            result = "It's a tie!";
        } else if ((playerMove.equals("rock") && computerMove.equals("scissors")) ||
                (playerMove.equals("paper") && computerMove.equals("rock")) ||
                (playerMove.equals("scissors") && computerMove.equals("paper"))) {
            result = "You win!";
        } else {
            result = "You lose!";
        }

        textArea.append("You chose " + playerMove + ", computer chose " + computerMove + ". " + result + "\n");
    }
}
