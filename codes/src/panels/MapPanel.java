package panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MapPanel extends JPanel {

    private JFrame frame;

    private BufferedImage bgImage;

    private boolean[] roomUnlocked;

    private int currentRoom;

    private int hoveredRoom = -1;

    private int selectedRoom = -1;

    // Continue button
    private Rectangle continueButton;

    // Layout
    private static final int ROOM_W = 100;
    private static final int ROOM_H = 80;
    private static final int ROOM_GAP = 50;
    private static final int ROOMS = 6;

    // Pixel fonts
    private final Font pixelTitle =
            new Font("Monospaced", Font.BOLD, 24);

    private final Font pixelText =
            new Font("Monospaced", Font.BOLD, 14);

    private final Font pixelSmall =
            new Font("Monospaced", Font.BOLD, 12);

    // Colors
    private static final Color PANEL_BG =
            new Color(18, 18, 18, 240);

    private static final Color BORDER_COLOR =
            new Color(120, 80, 30);

    private static final Color ROOM_LOCKED_BG =
            new Color(55, 55, 55);

    private static final Color ROOM_UNLOCKED_BG =
            new Color(100, 70, 20);

    private static final Color ROOM_CURRENT_BG =
            new Color(150, 100, 30);

    private static final Color TITLE_COLOR =
            new Color(255, 210, 90);

    private static final Color LABEL_COLOR =
            new Color(230, 200, 120);

    public MapPanel(JFrame frame, int currentRoom) {

        this.frame = frame;

        this.currentRoom = currentRoom;

        roomUnlocked = new boolean[ROOMS];

        // Only room 1 unlocked
        roomUnlocked[0] = true;

        setOpaque(false);

        loadBackground();

        setupMouse();
    }

    public MapPanel(int i) {
    }

    private void setupMouse() {

        addMouseMotionListener(
                new java.awt.event.MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(
                            java.awt.event.MouseEvent e
                    ) {

                        hoveredRoom = -1;

                        int W = getWidth();

                        int H = getHeight();

                        int totalRoomsWidth =
                                ROOMS * ROOM_W
                                        +
                                        (ROOMS - 1) * ROOM_GAP;

                        int panelPad = 50;

                        int panelW =
                                totalRoomsWidth + panelPad * 2;

                        int panelH = ROOM_H + 120;

                        int panelX = (W - panelW) / 2;

                        int panelY = (H - panelH) / 2;

                        int roomsY = panelY + 65;

                        int startX = panelX + panelPad;

                        for (int i = 0; i < ROOMS; i++) {

                            int rx =
                                    startX
                                            +
                                            i * (ROOM_W + ROOM_GAP);

                            Rectangle roomRect =
                                    new Rectangle(
                                            rx,
                                            roomsY,
                                            ROOM_W,
                                            ROOM_H
                                    );

                            if (roomUnlocked[i]
                                    &&
                                    roomRect.contains(
                                            e.getPoint()
                                    )) {

                                hoveredRoom = i;
                            }
                        }

                        repaint();
                    }
                }
        );

        addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        // Room clicked
                        if (hoveredRoom != -1) {

                            selectedRoom = hoveredRoom;

                            repaint();
                        }

                        // Continue button clicked
                        if (continueButton != null
                                &&
                                continueButton.contains(
                                        e.getPoint()
                                )) {

                            // ROOM 1
                            if (selectedRoom == 0) {

                                GamePanel gamePanel =
                                        new GamePanel();

                                frame.setContentPane(
                                        gamePanel
                                );

                                frame.setSize(700, 550);

                                frame.setLocationRelativeTo(
                                        null
                                );

                                frame.revalidate();

                                frame.repaint();
                            }
                        }
                    }
                }
        );
    }

    private void loadBackground() {

        try {

            bgImage =
                    ImageIO.read(
                            new File("dungeon1.jpg")
                    );

        } catch (Exception e) {

            System.out.println(
                    "Background not found."
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // PIXELATED STYLE
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        );

        int W = getWidth();

        int H = getHeight();

        // Background
        if (bgImage != null) {

            g2.drawImage(
                    bgImage,
                    0,
                    0,
                    W,
                    H,
                    this
            );

        } else {

            g2.setColor(Color.BLACK);

            g2.fillRect(0, 0, W, H);
        }

        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 170));

        g2.fillRect(0, 0, W, H);

        // Panel dimensions
        int totalRoomsWidth =
                ROOMS * ROOM_W
                        +
                        (ROOMS - 1) * ROOM_GAP;

        int panelPad = 50;

        int panelW =
                totalRoomsWidth + panelPad * 2;

        int panelH = ROOM_H + 120;

        int panelX = (W - panelW) / 2;

        int panelY = (H - panelH) / 2;

        drawPixelPanel(
                g2,
                panelX,
                panelY,
                panelW,
                panelH
        );

        // Title
        g2.setFont(pixelTitle);

        g2.setColor(TITLE_COLOR);

        String title = "DUNGEON MAP";

        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(
                title,
                panelX
                        +
                        (panelW - fm.stringWidth(title)) / 2,
                panelY + 35
        );

        // Divider
        g2.setColor(BORDER_COLOR);

        g2.fillRect(
                panelX + 30,
                panelY + 45,
                panelW - 60,
                4
        );

        int roomsY = panelY + 65;

        int startX = panelX + panelPad;

        // Connectors
        for (int i = 0; i < ROOMS - 1; i++) {

            int rx =
                    startX
                            +
                            i * (ROOM_W + ROOM_GAP);

            int x1 = rx + ROOM_W;

            int x2 =
                    rx + ROOM_W + ROOM_GAP;

            drawConnector(
                    g2,
                    x1,
                    roomsY + ROOM_H / 2,
                    x2,
                    roomUnlocked[i + 1]
            );
        }

        // Rooms
        for (int i = 0; i < ROOMS; i++) {

            int rx =
                    startX
                            +
                            i * (ROOM_W + ROOM_GAP);

            drawRoom(
                    g2,
                    rx,
                    roomsY,
                    i
            );
        }

        // Continue button
        if (selectedRoom != -1) {

            int bw = 170;

            int bh = 45;

            int bx = (W - bw) / 2;

            int by =
                    panelY + panelH + 25;

            continueButton =
                    new Rectangle(
                            bx,
                            by,
                            bw,
                            bh
                    );

            // Shadow
            g2.setColor(Color.BLACK);

            g2.fillRect(
                    bx + 4,
                    by + 4,
                    bw,
                    bh
            );

            // Button
            g2.setColor(
                    new Color(120, 80, 20)
            );

            g2.fillRect(
                    bx,
                    by,
                    bw,
                    bh
            );

            // Border
            g2.setColor(
                    new Color(255, 210, 100)
            );

            g2.drawRect(
                    bx,
                    by,
                    bw,
                    bh
            );

            // Text
            g2.setFont(pixelText);

            String txt = "CONTINUE";

            FontMetrics tfm =
                    g2.getFontMetrics();

            g2.drawString(
                    txt,
                    bx
                            +
                            (bw - tfm.stringWidth(txt)) / 2,
                    by + 28
            );
        }
    }

    private void drawPixelPanel(
            Graphics2D g2,
            int x,
            int y,
            int w,
            int h
    ) {

        // Shadow
        g2.setColor(Color.BLACK);

        g2.fillRect(
                x + 6,
                y + 6,
                w,
                h
        );

        // Panel
        g2.setColor(PANEL_BG);

        g2.fillRect(x, y, w, h);

        // Border
        g2.setColor(BORDER_COLOR);

        g2.fillRect(x, y, w, 4);

        g2.fillRect(
                x,
                y + h - 4,
                w,
                4
        );

        g2.fillRect(x, y, 4, h);

        g2.fillRect(
                x + w - 4,
                y,
                4,
                h
        );
    }

    private void drawConnector(
            Graphics2D g2,
            int x1,
            int y,
            int x2,
            boolean unlocked
    ) {

        if (unlocked) {

            g2.setColor(
                    new Color(170, 120, 40)
            );

        } else {

            g2.setColor(
                    new Color(70, 70, 70)
            );
        }

        g2.fillRect(
                x1,
                y - 2,
                x2 - x1,
                4
        );
    }

    private void drawRoom(
            Graphics2D g2,
            int x,
            int y,
            int index
    ) {

        boolean isLocked =
                !roomUnlocked[index];

        boolean isCurrent =
                index == currentRoom;

        Color bg;

        if (isCurrent) {

            bg = ROOM_CURRENT_BG;

        } else if (isLocked) {

            bg = ROOM_LOCKED_BG;

        } else {

            bg = ROOM_UNLOCKED_BG;
        }

        // Shadow
        g2.setColor(Color.BLACK);

        g2.fillRect(
                x + 4,
                y + 4,
                ROOM_W,
                ROOM_H
        );

        // Room
        g2.setColor(bg);

        g2.fillRect(
                x,
                y,
                ROOM_W,
                ROOM_H
        );

        // Hover
        if (hoveredRoom == index) {

            g2.setColor(
                    new Color(255, 255, 255, 50)
            );

            g2.fillRect(
                    x,
                    y,
                    ROOM_W,
                    ROOM_H
            );
        }

        // Border
        if (selectedRoom == index) {

            g2.setColor(
                    new Color(255, 220, 120)
            );

        } else {

            g2.setColor(
                    new Color(180, 130, 40)
            );
        }

        g2.drawRect(
                x,
                y,
                ROOM_W,
                ROOM_H
        );

        // Icon
        g2.setFont(pixelText);

        String icon;

        if (isLocked) {

            icon = "X";

        } else {

            icon = "O";
        }

        FontMetrics fm =
                g2.getFontMetrics();

        g2.drawString(
                icon,
                x
                        +
                        (ROOM_W - fm.stringWidth(icon)) / 2,
                y + 35
        );

        // Label
        g2.setFont(pixelSmall);

        String label =
                "ROOM " + (index + 1);

        FontMetrics fm2 =
                g2.getFontMetrics();

        g2.setColor(LABEL_COLOR);

        g2.drawString(
                label,
                x
                        +
                        (ROOM_W - fm2.stringWidth(label)) / 2,
                y + ROOM_H - 10
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame =
                    new JFrame("Dungeon Map");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(1200, 700);

            frame.setLocationRelativeTo(null);

            frame.setContentPane(
                    new MapPanel(frame, 0)
            );

            frame.setVisible(true);
        });
    }
}