import panels.GamePanel;

import javax.swing.*;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("The Riddle Dungeon");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GamePanel game = new GamePanel();
            frame.add(game);

            // FORCE MINIMUM SIZE SO WINDOW ISN'T EMPTY
            frame.setMinimumSize(new Dimension(700, 550));
            frame.setSize(700, 550); // Or use pack() after ensuring content has size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("🖥️ Frame visible. Game should appear.");
        });
    }
}