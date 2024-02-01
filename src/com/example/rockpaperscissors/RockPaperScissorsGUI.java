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

        if (profiles == null || profiles.isEmpty()) {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                textArea.append("Profile Created: " + newProfile.getUsername() + "\n");
                selectProfile(); // Reopen profile selection window
            }
            return; //close if there are no profiles and user doesn't create one
        }

        PlayerProfile[] profileArray = profiles.toArray(new PlayerProfile[0]);
        JComboBox<PlayerProfile> comboBox = new JComboBox<>(profileArray);
        int result = JOptionPane.showOptionDialog(
                null,
                comboBox,
                "Select, Create, or Delete Profile",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Select", "Create New", "Delete", "Cancel"},
                "Select");

        if (result == JOptionPane.YES_OPTION) {
            PlayerProfile selectedProfile = (PlayerProfile) comboBox.getSelectedItem();
            if (selectedProfile != null) {
                gameApp.setActiveProfile(selectedProfile);
                textArea.append("Profile Selected: " + selectedProfile.getUsername() + "\n");
            }
        } else if (result == JOptionPane.NO_OPTION) {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                textArea.append("Profile Created: " + newProfile.getUsername() + "\n");
            }
            selectProfile(); // open window after profile creation
        } else if (result == JOptionPane.CANCEL_OPTION) {
            deleteProfile();
            selectProfile(); // open window after profile deleted
        } else {
            System.exit(0); // close app when user cancels
        }
    }
    private PlayerProfile createNewProfile() {
        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Enter new profile name:");
            if (username == null) {
                System.exit(0); // close app if user clicks "cancel"
            } else if (username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid profile name.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        profileManager.createProfile(username);
        return profileManager.selectProfileByName(username);
    }


    private void deleteProfile() {
        List<PlayerProfile> profiles = profileManager.getProfiles();
        PlayerProfile selectedProfile = (PlayerProfile) JOptionPane.showInputDialog(
                null,
                "Select Profile to Delete:",
                "Delete Profile",
                JOptionPane.QUESTION_MESSAGE,
                null,
                profiles.toArray(),
                profiles.get(0));

        if (selectedProfile != null) {
            profileManager.deleteProfile(selectedProfile.getId());
            textArea.append("Profile Deleted: " + selectedProfile.getUsername() + "\n");
        }
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
