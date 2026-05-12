package panels;

import model.Player;
import model.RiddleImpl;
import model.Room;
import gamemasters.Awit;
import gamemasters.Deanver;
import gamemasters.Kirby;
import gamemasters.Jojan;
import gamemasters.Hayes;
import gamemasters.Patrick;


import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private JPanel gamePanel; // Must be named "gamePanel" in .form file

    // Components inside gamePanel — MUST match field names in .form
    private JLabel lblHearts;
    private JLabel lblRoomInfo;
    private JTextArea txtRiddle;
    private JLabel lblHint;
    private JTextField txtInput;
    private JButton btnSubmit;
    private JLabel lblFeedback;

    private Player player;
    private Room[] rooms;

    public GamePanel() {
        System.out.println(" RiddleDungeonGUI constructor started...");

        // Set layout for outer panel
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY); // So we can see if this panel appears

        initNewGame();

        // IMPORTANT: If gamePanel is null, create a fallback UI manually
        if (gamePanel == null) {
            System.err.println("⚠️ WARNING: gamePanel is NULL! Creating fallback UI...");
            createFallbackUI();
        } else {
            System.out.println("✅ gamePanel found. Adding to root...");
            add(gamePanel, BorderLayout.CENTER);
        }

        setupEventListeners();
        loadLevel();

        System.out.println(" RiddleDungeonGUI initialized.");
    }

    // Fallback UI in case GUI Designer binding failed
    private void createFallbackUI() {
        gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gamePanel.setBackground(new Color(20, 20, 30));

        // Top
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        lblHearts = new JLabel("❤️❤️❤️");
        lblHearts.setForeground(Color.RED);
        lblRoomInfo = new JLabel("Room 1 | Easy");
        lblRoomInfo.setForeground(Color.WHITE);
        top.add(lblHearts);
        top.add(Box.createHorizontalStrut(20));
        top.add(lblRoomInfo);
        gamePanel.add(top, BorderLayout.NORTH);

        // Center
        txtRiddle = new JTextArea("Welcome to the dungeon...\nSolve riddles to escape!");
        txtRiddle.setEditable(false);
        txtRiddle.setLineWrap(true);
        txtRiddle.setWrapStyleWord(true);
        txtRiddle.setFont(new Font("Serif", Font.ITALIC, 16));
        txtRiddle.setBackground(new Color(30, 30, 40));
        txtRiddle.setForeground(Color.CYAN);
        JScrollPane scroll = new JScrollPane(txtRiddle);
        gamePanel.add(scroll, BorderLayout.CENTER);

        lblHint = new JLabel("Hint: ...");
        lblHint.setForeground(Color.ORANGE);
        gamePanel.add(lblHint, BorderLayout.SOUTH);

        // Bottom
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        JPanel inputRow = new JPanel(new FlowLayout());
        inputRow.setOpaque(false);
        inputRow.add(new JLabel("Answer:"));
        txtInput = new JTextField(20);
        btnSubmit = new JButton("Unlock Door");
        inputRow.add(txtInput);
        inputRow.add(btnSubmit);
        bottom.add(inputRow, BorderLayout.NORTH);

        lblFeedback = new JLabel(" ");
        lblFeedback.setOpaque(true);
        bottom.add(lblFeedback, BorderLayout.CENTER);
        gamePanel.add(bottom, BorderLayout.SOUTH);

        // Add fallback gamePanel to root
        add(gamePanel, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        if (btnSubmit != null) {
            btnSubmit.addActionListener(e -> checkAnswer());
        }
        if (txtInput != null) {
            txtInput.addActionListener(e -> checkAnswer());
        }
    }

    private void initNewGame() {
        player = new Player();
        createRooms();
    }

    private void createRooms() {

        rooms = new Room[6];

        rooms[0] = new Room(
                1,
                "Easy",
                new Kirby(new RiddleImpl(
                        "I have keys but no locks.",
                        "piano",
                        "It makes music."
                )),
                "A dusty room filled with strange melodies.",
                "Bronze Key"
        );

        rooms[1] = new Room(
                2,
                "Easy",
                new Deanver(new RiddleImpl(
                        "What has holes but holds water?",
                        "sponge",
                        "Used for cleaning."
                )),
                "Water drips from the ceiling.",
                "Small Potion"
        );

        rooms[2] = new Room(
                3,
                "Medium",
                new Jojan(new RiddleImpl(
                        "Echo riddle...",
                        "echo",
                        "Shout in hallway."
                )),
                "Your voice bounces across the walls.",
                "Silver Key"
        );

        rooms[3] = new Room(
                4,
                "Medium",
                new Hayes(new RiddleImpl(
                        "Chain riddle...",
                        "chain",
                        "Iron links."
                )),
                "Chains hang from the darkness.",
                "Magic Scroll"
        );

        rooms[4] = new Room(
                5,
                "Hard",
                new Awit(new RiddleImpl(
                        "Shadow riddle...",
                        "shadow",
                        "Follows you."
                )),
                "The room is unnaturally cold.",
                "Golden Key"
        );

        rooms[5] = new Room(
                6,
                "Hard",
                new Patrick(new RiddleImpl(
                        "Gate riddle...",
                        "gate",
                        "Exit door."
                )),
                "A massive gate blocks your escape.",
                "Freedom"
        );
    }

    private void loadLevel() {
        if (player.hasWon()) {
            showEndScreen(true);
            return;
        }
        if (!player.isAlive()) {
            showEndScreen(false);
            return;
        }

        Room current = rooms[player.getCurrentRoomIndex()];
        player.adjustHeartsForRoom(current.getRoomNumber());

        if (lblHearts != null) {
            StringBuilder hearts = new StringBuilder();
            for (int i = 0; i < player.getHearts(); i++) hearts.append("❤️");
            lblHearts.setText(hearts.toString());
        }

        if (lblRoomInfo != null) {
            lblRoomInfo.setText(String.format("Room %d | %s", current.getRoomNumber(), current.getDifficultyLabel()));
        }

        if (txtRiddle != null) {
            txtRiddle.setText(
                    current.getGreeting()
                            + "\n\n"
                            + current.getDescription()
                            + "\n\n"
                            + current.getRiddleQuestion()
            );
        }

        if (lblHint != null) {
            lblHint.setText("Hint: " + current.getRiddleHint());
        }

        if (txtInput != null) txtInput.setText("");
        if (lblFeedback != null) {
            lblFeedback.setText(" ");
            lblFeedback.setBackground(null);
        }
    }

    private void checkAnswer() {
        if (player.hasWon() || !player.isAlive()) return;

        Room current = rooms[player.getCurrentRoomIndex()];
        String ans = txtInput.getText();

        if (current.attemptAnswer(ans)) {
            lblFeedback.setText("CORRECT!");
            lblFeedback.setForeground(Color.GREEN);
            lblFeedback.setBackground(new Color(0, 50, 0));

            txtInput.setEnabled(false);
            btnSubmit.setEnabled(false);

            Timer t = new Timer(1500, e -> {
                player.nextRoom();
                txtInput.setEnabled(true);
                btnSubmit.setEnabled(true);
                loadLevel();
            });
            t.setRepeats(false);
            t.start();

        } else {
            player.loseHeart();
            lblFeedback.setText("WRONG! Lose a heart.");
            lblFeedback.setForeground(Color.WHITE);
            lblFeedback.setBackground(new Color(100, 0, 0));

            if (lblHearts != null) {
                StringBuilder hearts = new StringBuilder();
                for (int i = 0; i < player.getHearts(); i++) hearts.append("❤️");
                lblHearts.setText(hearts.toString());
            }

            if (!player.isAlive()) {
                Timer t = new Timer(1500, e -> loadLevel());
                t.setRepeats(false);
                t.start();
            }
        }
    }

    private void showEndScreen(boolean won) {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        JLabel msg = new JLabel(won ? "ESCAPED!" : "YOU DIED");
        msg.setFont(new Font("Serif", Font.BOLD, 40));
        msg.setForeground(won ? Color.YELLOW : Color.RED);

        JLabel sub = new JLabel(won ? "You escaped!" : "Game Over.");
        sub.setFont(new Font("Serif", Font.PLAIN, 20));
        sub.setForeground(Color.WHITE);

        JButton restart = new JButton("Play Again");
        restart.addActionListener(e -> {
            initNewGame();
            // Re-add gamePanel
            removeAll();
            setLayout(new BorderLayout());
            if (gamePanel != null) {
                add(gamePanel, BorderLayout.CENTER);
            } else {
                createFallbackUI();
            }
            loadLevel();
            revalidate();
            repaint();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0; add(msg, gbc);
        gbc.gridy = 1; add(sub, gbc);
        gbc.gridy = 2; add(restart, gbc);

        revalidate();
        repaint();
    }
}