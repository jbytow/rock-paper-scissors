package com.example.rockpaperscissors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
        PlayerProfile createNewProfileOption = new PlayerProfile(-1, "Create new profile", 0, 0, 0);
        profiles.add(createNewProfileOption);

        PlayerProfile selectedProfile = (PlayerProfile) JOptionPane.showInputDialog(
                null,
                "Select Your Profile:",
                "Profile Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                profiles.toArray(),
                profiles.get(0)
        );

        if (selectedProfile == createNewProfileOption) {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                textArea.append("Profile Created: " + newProfile.getUsername() + "\n");
            }
            selectProfile(); // Reopen the profile selection dialog
        } else if (selectedProfile != null) {
            gameApp.setActiveProfile(selectedProfile);
            textArea.append("Profile Selected: " + selectedProfile.getUsername() + "\n");
        }
    }
    private PlayerProfile createNewProfile() {
        String username = JOptionPane.showInputDialog("Enter new profile name:");
        if (username != null && !username.trim().isEmpty()) {
            profileManager.createProfile(username);
            return profileManager.selectProfileByName(username);
        }
        return null;
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
