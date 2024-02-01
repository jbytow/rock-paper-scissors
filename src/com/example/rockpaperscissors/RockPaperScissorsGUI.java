package com.example.rockpaperscissors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RockPaperScissorsGUI extends JFrame {
    private RockPaperScissorsGame gameApp;
    private JTextArea textArea;
    private ProfileManager profileManager;

    public RockPaperScissorsGUI(RockPaperScissorsGame gameApp, ProfileManager profileManager) {
        this.gameApp = gameApp;
        this.profileManager = profileManager;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Rock, Paper, Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

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

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Select a profile before showing the GUI
        selectProfile();

        setVisible(true);
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
            return; // Close if there are no profiles and the user doesn't create one
        }

        PlayerProfile[] profileArray = profiles.toArray(new PlayerProfile[0]);
        JComboBox<PlayerProfile> comboBox = new JComboBox<>(profileArray);

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(e -> {
            displayLeaderboard(); // Display leaderboard within the same window
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Set panel layout to four rows
        panel.add(comboBox);
        panel.add(leaderboardButton);

        int result = JOptionPane.showOptionDialog(
                null,
                panel,
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
            selectProfile(); // Open window after profile creation
        } else if (result == JOptionPane.CANCEL_OPTION) {
            deleteProfile();
            selectProfile(); // Open window after profile deleted
        } else {
            System.exit(0); // Close the app when the user cancels
        }
    }
    private PlayerProfile createNewProfile() {
        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Enter new profile name:");
            if (username == null) {
                System.exit(0); // Close the app if the user clicks "Cancel"
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

    public void displayLeaderboard() {
        // Initialize a JDialog for the leaderboard
        JDialog leaderboardDialog = new JDialog(this, "Leaderboard", true);
        leaderboardDialog.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Wins", "Losses", "Ties", "Win Percentage"};
        List<PlayerProfile> profiles = profileManager.getProfiles();
        profiles.sort((p1, p2) -> Double.compare(p2.getWinPercentage(), p1.getWinPercentage()));

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (PlayerProfile profile : profiles) {
            Object[] row = {
                    profile.getUsername(),
                    profile.getWins(),
                    profile.getLosses(),
                    profile.getTies(),
                    String.format("%.2f%%", profile.getWinPercentage())
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        leaderboardDialog.add(scrollPane, BorderLayout.CENTER);

        // Optionally add a close button at the bottom of the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> leaderboardDialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        leaderboardDialog.add(bottomPanel, BorderLayout.SOUTH);

        // Set the dialog size and make it visible
        leaderboardDialog.pack();
        leaderboardDialog.setLocationRelativeTo(null); // Center on screen
        leaderboardDialog.setVisible(true);
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
