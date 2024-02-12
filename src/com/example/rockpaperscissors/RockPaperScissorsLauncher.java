package com.example.rockpaperscissors;

import com.example.rockpaperscissors.database.DatabaseManager;
import com.example.rockpaperscissors.game.RockPaperScissorsGame;
import com.example.rockpaperscissors.manager.ProfileManager;
import com.example.rockpaperscissors.ui.RockPaperScissorsGUI;

import javax.swing.*;

public class RockPaperScissorsLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RockPaperScissorsLauncher().runGame());
    }

    private void runGame() {
        DatabaseManager dbManager = new DatabaseManager();
        ProfileManager profileManager = new ProfileManager(dbManager.getConnection());

        RockPaperScissorsGame gameApp = new RockPaperScissorsGame(profileManager);
        RockPaperScissorsGUI gui = new RockPaperScissorsGUI(gameApp, profileManager);
    }
}
