package com.example.rockpaperscissors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RockPaperScissorsGUI extends JFrame {
    private final RockPaperScissorsGame gameApp;
    private final JTextArea textArea;
    private final ProfileManager profileManager;
    // Constructor initializes the game and profile manager, and sets up the main menu

    public RockPaperScissorsGUI(RockPaperScissorsGame gameApp, ProfileManager profileManager) {
        this.gameApp = gameApp;
        this.profileManager = profileManager;
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        mainMenu(); // Start by displaying the main menu
    }

    private void mainMenu() {
        List<PlayerProfile> profiles = profileManager.getProfiles();

        if (profiles == null || profiles.isEmpty()) {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                textArea.append("Profile Created: " + newProfile.getUsername() + "\n");
                mainMenu(); // Reopen profile selection window
            }
            return; // Close if there are no profiles and the user doesn't create one
        }

        PlayerProfile[] profileArray = profiles.toArray(new PlayerProfile[0]);
        JComboBox<PlayerProfile> comboBox = new JComboBox<>(profileArray);

        // Create buttons for actions
        JButton selectButton = new JButton("Select");
        JButton createNewButton = new JButton("Create New");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");

        // Listener for the "Select" button
        selectButton.addActionListener(e -> {
            PlayerProfile selectedProfile = (PlayerProfile) comboBox.getSelectedItem();
            if (selectedProfile != null) {
                gameApp.setActiveProfile(selectedProfile);
                textArea.append("Profile Selected: " + selectedProfile.getUsername() + "\n");
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window instanceof JDialog) {
                    window.dispose();
                }
                initializeGameGUI();
            }
        });

        // Listener for the "Create New" button
        createNewButton.addActionListener(e -> {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                textArea.append("Profile Created: " + newProfile.getUsername() + "\n");
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window instanceof Dialog) {
                    window.dispose();
                }
                mainMenu();
            }
        });

        // Listener for the "Delete" button
        deleteButton.addActionListener(e -> {
            deleteProfile();
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (window instanceof Dialog) {
                window.dispose();
            }
            mainMenu();
        });

        // Listener for the "Cancel" button
        cancelButton.addActionListener(e -> System.exit(0));

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(e -> displayLeaderboard());

        // Panel for the leaderboard button with some spacing
        JPanel leaderboardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        leaderboardPanel.add(leaderboardButton);

        // Panel for the action buttons without vertical spacing
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.add(selectButton);
        buttonsPanel.add(createNewButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(cancelButton);

        // Main panel with GridLayout to structure the layout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // 4 rows, 1 column, no hgap, 5 vgap
        mainPanel.add(comboBox);
        mainPanel.add(leaderboardPanel);
        mainPanel.add(buttonsPanel);

        // Display the profile selection dialog
        int result = JOptionPane.showOptionDialog(
                null,
                mainPanel,
                "Select, Create, or Delete Profile",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[] {}, // Pass an empty array to prevent the default buttons from showing
                null);

        if (result == JOptionPane.CLOSED_OPTION) {
            // Handle the case where the dialog is closed without an option being chosen
        }
        // No further action is taken here since the action listeners will handle it
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
            double winPercentage = profile.getWinPercentage();
            String winPercentageText = (Double.isNaN(winPercentage) ? "0.00" : String.format("%.2f", winPercentage)) + "%";

            Object[] row = {
                    profile.getUsername(),
                    profile.getWins(),
                    profile.getLosses(),
                    profile.getTies(),
                    winPercentageText
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

    private void initializeGameGUI() {
        textArea.setText("");
        setTitle("Rock, Paper, Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");
        JButton backButton = new JButton("Back to Main Menu"); // Add a back button

        rockButton.addActionListener(e -> playGame("rock"));
        paperButton.addActionListener(e -> playGame("paper"));
        scissorsButton.addActionListener(e -> playGame("scissors"));
        backButton.addActionListener(e -> {
            this.setVisible(false); // Hide the game window
            mainMenu(); // Show the main menu again
        });

        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);
        panel.add(backButton); // Add the back button to the panel

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
    }
}
