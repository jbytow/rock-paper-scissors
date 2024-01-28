package com.example.rockpaperscissors;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RockPaperScissorsGUI {
    private RockPaperScissorsGame gameApp;
    private JTextArea textArea;
    private ProfileManager profileManager;

    public RockPaperScissorsGUI(RockPaperScissorsGame gameApp, ProfileManager profileManager) {
        this.gameApp = gameApp;
        this.profileManager = profileManager;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Rock, Paper, Scissors Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);

        rockButton.addActionListener(e -> playGame("rock"));
        paperButton.addActionListener(e -> playGame("paper"));
        scissorsButton.addActionListener(e -> playGame("scissors"));

        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        // select profile before showing GUI
        selectProfile();

        frame.setVisible(true);
    }

    private void selectProfile() {
        List<PlayerProfile> profiles = profileManager.getProfiles();
        PlayerProfile selectedProfile = null;

        if (profiles != null && !profiles.isEmpty()) {
            Object[] profileOptions = profiles.toArray();
            selectedProfile = (PlayerProfile) JOptionPane.showInputDialog(
                    null,
                    "Select Your Profile:",
                    "Profile Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    profileOptions,
                    profileOptions[0]
            );
        }

        if (selectedProfile != null) {
            gameApp.setActiveProfile(selectedProfile);
            textArea.append("Profile Selected: " + selectedProfile.getUsername() + "\n");
        }
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
