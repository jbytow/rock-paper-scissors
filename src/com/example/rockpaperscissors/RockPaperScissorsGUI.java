package com.example.rockpaperscissors;

import javax.swing.*;
import java.awt.*;

public class RockPaperScissorsGUI {
    private RockPaperScissorsGame gameApp;

    public RockPaperScissorsGUI(RockPaperScissorsGame gameApp) {
        this.gameApp = gameApp;
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Rock, Paper, Scissors Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");

        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);

        rockButton.addActionListener(e -> playGame("rock", textArea));
        paperButton.addActionListener(e -> playGame("paper", textArea));
        scissorsButton.addActionListener(e -> playGame("scissors", textArea));

        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void playGame(String playerMove, JTextArea textArea) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
