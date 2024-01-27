package com.example.rockpaperscissors;

import javax.swing.*;

public class RockPaperScissorsLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RockPaperScissorsLauncher().runGame());
    }

    private void runGame() {
        RockPaperScissorsGame gameApp = new RockPaperScissorsGame();
        RockPaperScissorsGUI gui = new RockPaperScissorsGUI(gameApp);
        gui.createAndShowGUI();
    }
}
