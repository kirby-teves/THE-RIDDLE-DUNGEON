package panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MapPanel extends JPanel {
    private JFrame frame;
    private BufferedImage bgImage;
    private boolean[] roomUnlocked;
    private int currentRoom, hoveredRoom = -1, selectedRoom = -1;
    private Rectangle continueButton;

    private static final int ROOM_W = 100, ROOM_H = 80, ROOM_GAP = 50, ROOMS = 6;

    private final Font pixelTitle = new Font("Monospaced", Font.BOLD, 24);
    private final Font pixelText  = new Font("Monospaced", Font.BOLD, 14);
    private final Font pixelSmall = new Font("Monospaced", Font.BOLD, 12);

    private static final Color PANEL_BG         = new Color(18, 18, 18, 240);
    private static final Color BORDER_COLOR     = new Color(120, 80, 30);
    private static final Color ROOM_LOCKED_BG   = new Color(55, 55, 55);
    private static final Color ROOM_UNLOCKED_BG = new Color(100, 70, 20);
    private static final Color ROOM_CURRENT_BG  = new Color(150, 100, 30);
    private static final Color TITLE_COLOR      = new Color(255, 210, 90);
    private static final Color LABEL_COLOR      = new Color(230, 200, 120);

    public MapPanel(JFrame frame, int currentRoom) {
        this.frame = frame;
        this.currentRoom = currentRoom;
        roomUnlocked = new boolean[ROOMS];
        roomUnlocked[0] = true;
        setOpaque(false);
        try { bgImage = ImageIO.read(new File("dungeon1.jpg")); }
        catch (Exception e) { System.out.println("Background not found."); }
        setupMouse();
    }

    private void setupMouse() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoveredRoom = -1;
                int[] L = getPanelLayout();
                for (int i = 0; i < ROOMS; i++) {
                    int rx = L[2] + i * (ROOM_W + ROOM_GAP);
                    if (roomUnlocked[i] && new Rectangle(rx, L[3], ROOM_W, ROOM_H).contains(e.getPoint()))
                        hoveredRoom = i;
                }
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (hoveredRoom != -1) { selectedRoom = hoveredRoom; repaint(); }
                if (continueButton != null && continueButton.contains(e.getPoint()) && selectedRoom == 0) {
                    GamePanel gamePanel = new GamePanel();
                    frame.setContentPane(gamePanel);
                    frame.setSize(700, 550);
                    frame.setLocationRelativeTo(null);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
    }

    // Returns [panelX, panelY, startX, roomsY, panelW, panelH]
    private int[] getPanelLayout() {
        int panelPad = 50;
        int panelW = ROOMS * ROOM_W + (ROOMS - 1) * ROOM_GAP + panelPad * 2;
        int panelH = ROOM_H + 120;
        int panelX = (getWidth() - panelW) / 2;
        int panelY = (getHeight() - panelH) / 2;
        return new int[]{ panelX, panelY, panelX + panelPad, panelY + 65, panelW, panelH };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        int W = getWidth(), H = getHeight();
        if (bgImage != null) g2.drawImage(bgImage, 0, 0, W, H, this);
        else { g2.setColor(Color.BLACK); g2.fillRect(0, 0, W, H); }

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, W, H);

        int[] L = getPanelLayout();
        int panelX = L[0], panelY = L[1], startX = L[2], roomsY = L[3], panelW = L[4], panelH = L[5];

        drawPixelPanel(g2, panelX, panelY, panelW, panelH);

        g2.setFont(pixelTitle);
        g2.setColor(TITLE_COLOR);
        String title = "DUNGEON MAP";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, panelX + (panelW - fm.stringWidth(title)) / 2, panelY + 35);

        g2.setColor(BORDER_COLOR);
        g2.fillRect(panelX + 30, panelY + 45, panelW - 60, 4);

        for (int i = 0; i < ROOMS - 1; i++) {
            int rx = startX + i * (ROOM_W + ROOM_GAP);
            drawConnector(g2, rx + ROOM_W, roomsY + ROOM_H / 2, rx + ROOM_W + ROOM_GAP, roomUnlocked[i + 1]);
        }
        for (int i = 0; i < ROOMS; i++)
            drawRoom(g2, startX + i * (ROOM_W + ROOM_GAP), roomsY, i);

        if (selectedRoom != -1) {
            int bw = 170, bh = 45, bx = (W - bw) / 2, by = panelY + panelH + 25;
            continueButton = new Rectangle(bx, by, bw, bh);
            g2.setColor(Color.BLACK);            g2.fillRect(bx + 4, by + 4, bw, bh);
            g2.setColor(new Color(120, 80, 20)); g2.fillRect(bx, by, bw, bh);
            g2.setColor(new Color(255, 210, 100)); g2.drawRect(bx, by, bw, bh);
            g2.setFont(pixelText);
            String txt = "CONTINUE";
            FontMetrics tfm = g2.getFontMetrics();
            g2.drawString(txt, bx + (bw - tfm.stringWidth(txt)) / 2, by + 28);
        }
    }

    private void drawPixelPanel(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.BLACK);  g2.fillRect(x + 6, y + 6, w, h);
        g2.setColor(PANEL_BG);     g2.fillRect(x, y, w, h);
        g2.setColor(BORDER_COLOR);
        g2.fillRect(x, y, w, 4);
        g2.fillRect(x, y + h - 4, w, 4);
        g2.fillRect(x, y, 4, h);
        g2.fillRect(x + w - 4, y, 4, h);
    }

    private void drawConnector(Graphics2D g2, int x1, int y, int x2, boolean unlocked) {
        g2.setColor(unlocked ? new Color(170, 120, 40) : new Color(70, 70, 70));
        g2.fillRect(x1, y - 2, x2 - x1, 4);
    }

    private void drawRoom(Graphics2D g2, int x, int y, int index) {
        boolean isLocked = !roomUnlocked[index];
        Color bg = (index == currentRoom) ? ROOM_CURRENT_BG : isLocked ? ROOM_LOCKED_BG : ROOM_UNLOCKED_BG;
        g2.setColor(Color.BLACK); g2.fillRect(x + 4, y + 4, ROOM_W, ROOM_H);
        g2.setColor(bg);          g2.fillRect(x, y, ROOM_W, ROOM_H);
        if (hoveredRoom == index) { g2.setColor(new Color(255, 255, 255, 50)); g2.fillRect(x, y, ROOM_W, ROOM_H); }
        g2.setColor(selectedRoom == index ? new Color(255, 220, 120) : new Color(180, 130, 40));
        g2.drawRect(x, y, ROOM_W, ROOM_H);

        g2.setFont(pixelText);
        String icon = isLocked ? "X" : "O";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(icon, x + (ROOM_W - fm.stringWidth(icon)) / 2, y + 35);

        g2.setFont(pixelSmall);
        String label = "ROOM " + (index + 1);
        FontMetrics fm2 = g2.getFontMetrics();
        g2.setColor(LABEL_COLOR);
        g2.drawString(label, x + (ROOM_W - fm2.stringWidth(label)) / 2, y + ROOM_H - 10);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dungeon Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MapPanel(frame, 0));
            frame.setVisible(true);
        });
    }
}