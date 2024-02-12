package com.example.rockpaperscissors.ui;

import com.example.rockpaperscissors.game.RockPaperScissorsGame;
import com.example.rockpaperscissors.manager.ProfileManager;
import com.example.rockpaperscissors.model.PlayerProfile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RockPaperScissorsGUI extends JFrame {
    private final RockPaperScissorsGame gameApp;
    private final JTextArea textArea;
    private final ProfileManager profileManager;
    private final JLabel statusMessageLabel;
    private final JLabel sessionStatsLabel;
    private final JLabel totalStatsLabel;
    private final JLabel selectedProfileLabel;
    private final JComboBox<PlayerProfile> comboBox;
    // Constructor initializes the game and profile manager, and sets up the main menu

    public RockPaperScissorsGUI(RockPaperScissorsGame gameApp, ProfileManager profileManager) {
        this.gameApp = gameApp;
        this.profileManager = profileManager;
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        statusMessageLabel = new JLabel();
        statusMessageLabel.setHorizontalAlignment(JLabel.CENTER);
        sessionStatsLabel = new JLabel("Session Stats: Wins: 0, Losses: 0, Ties: 0", JLabel.CENTER);
        totalStatsLabel = new JLabel("Total Stats: Wins: 0, Losses: 0, Ties: 0, Win%: 0.00%", JLabel.CENTER);
        selectedProfileLabel = new JLabel("No profile selected", JLabel.CENTER);
        comboBox = new JComboBox<>();
        mainMenu(); // Start by displaying the main menu
    }

    private void mainMenu() {
        updateProfilesList();

        // Create buttons for actions
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton selectButton = new JButton("Select");
        JButton createNewButton = new JButton("Create New");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");

        // Listener for the "Select" button
        selectButton.addActionListener(e -> {
            PlayerProfile selectedProfile = (PlayerProfile) comboBox.getSelectedItem();
            if (selectedProfile != null) {
                gameApp.setActiveProfile(selectedProfile);
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window instanceof JDialog) {
                    window.dispose();
                }
                initializeGameGUI();
                updateProfileDisplay();
            }
        });

        // Listener for the "Create New" button
        createNewButton.addActionListener(e -> {
            PlayerProfile newProfile = createNewProfile();
            if (newProfile != null) {
                gameApp.setActiveProfile(newProfile);
                statusMessageLabel.setText("The " + newProfile.getUsername() + " profile has been successfully created");
                updateProfilesList();
            }
        });

        // Listener for the "Delete" button
        deleteButton.addActionListener(e -> {
            deleteProfile();
            updateProfilesList();
        });

        // Listener for the "Cancel" button
        cancelButton.addActionListener(e -> System.exit(0));

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(e -> displayLeaderboard());

        // Panel for the leaderboard button with some spacing
        JPanel leaderboardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        leaderboardPanel.add(leaderboardButton);

        // Panel for the action buttons without vertical spacing
        buttonsPanel.add(selectButton);
        buttonsPanel.add(createNewButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(cancelButton);

        JPanel statusMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statusMessagePanel.add(statusMessageLabel);

        // Main panel with GridLayout to structure the layout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // 4 rows, 1 column, no hgap, 5 vgap
        mainPanel.add(comboBox);
        mainPanel.add(leaderboardPanel);
        mainPanel.add(buttonsPanel);
        mainPanel.add(statusMessageLabel);

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

    private void updateProfilesList() {
        List<PlayerProfile> profiles = profileManager.getProfiles();
        comboBox.removeAllItems();
        for (PlayerProfile profile : profiles) {
            comboBox.addItem(profile);
        }
        if (profiles.isEmpty()) {
            createNewProfile();
        }
    }

    private PlayerProfile createNewProfile() {
            String username;
            while (true) {
                username = JOptionPane.showInputDialog("Enter new profile name:");
                if (username == null || username.trim().isEmpty()) {
                    return null; // Exiting the method if user cancels or inputs an empty name
                } else {
                    break; // Exit the loop since we have a valid username
                }
            }

        profileManager.createProfile(username);
        updateProfilesList();
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
            statusMessageLabel.setText("The " + selectedProfile.getUsername() + " profile has been successfully deleted");
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
        getContentPane().removeAll();
        gameApp.resetSessionStats();
        textArea.setText("");
        setTitle("Rock, Paper, Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);

        // Create a panel to group the profile and stats labels
        JPanel profileAndStatsPanel = new JPanel();
        profileAndStatsPanel.setLayout(new BoxLayout(profileAndStatsPanel, BoxLayout.Y_AXIS));

        // Add the selected profile label
        selectedProfileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileAndStatsPanel.add(selectedProfileLabel);

        // Add the session stats label
        sessionStatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileAndStatsPanel.add(sessionStatsLabel);

        // Add the total stats label
        totalStatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileAndStatsPanel.add(totalStatsLabel);

        // Add the panel to the NORTH region of the content pane
        getContentPane().add(profileAndStatsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");
        JButton backButton = new JButton("Back to Main Menu");

        rockButton.addActionListener(e -> playGame("rock"));
        paperButton.addActionListener(e -> playGame("paper"));
        scissorsButton.addActionListener(e -> playGame("scissors"));
        backButton.addActionListener(e -> {
            this.setVisible(false); // Hide the game window
            mainMenu(); // Show the main menu again
        });

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(backButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        setVisible(true);

        getContentPane().validate();
        getContentPane().repaint();
    }

    private void updateProfileDisplay() {
        // Update session stats
        int sessionWins = gameApp.getSessionWins();
        int sessionLosses = gameApp.getSessionLosses();
        int sessionTies = gameApp.getSessionTies();
        double sessionWinPercentage = calculateWinPercentage(sessionWins, sessionLosses, sessionTies);
        sessionStatsLabel.setText(String.format("Session Stats: Wins: %d, Losses: %d, Ties: %d, Win%%: %.2f%%",
                sessionWins, sessionLosses, sessionTies, sessionWinPercentage));

        // Update total stats
        int totalWins = gameApp.getActiveProfileWins();
        int totalLosses = gameApp.getActiveProfileLosses();
        int totalTies = gameApp.getActiveProfileTies();
        double totalWinPercentage = calculateWinPercentage(totalWins, totalLosses, totalTies);
        totalStatsLabel.setText(String.format("Total Stats: Wins: %d, Losses: %d, Ties: %d, Win%%: %.2f%%",
                totalWins, totalLosses, totalTies, totalWinPercentage));

        // Update the selected profile label
        PlayerProfile activeProfile = gameApp.getActiveProfile();
        if (activeProfile != null) {
            selectedProfileLabel.setText("Profile Selected: " + activeProfile.getUsername());
        } else {
            selectedProfileLabel.setText("No profile selected");
        }
    }

    private double calculateWinPercentage(int wins, int losses, int ties) {
        int totalGames = wins + losses + ties;
        return totalGames > 0 ? (double) wins / totalGames * 100 : 0;
    }

    private void playGame(String playerMove) {
        String result = gameApp.playGame(playerMove);
        textArea.append(result);
        updateProfileDisplay();
    }
}
