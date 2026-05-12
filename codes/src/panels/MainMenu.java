package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu {

    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;
        buildUI();
    }

    private void buildUI() {
        BackgroundPanel background = new BackgroundPanel("dungeon2.jpg");
        background.setLayout(new GridBagLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("The Riddle Dungeon");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Palatino Linotype", Font.BOLD, 80));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newButton = createStyledButton("New Game");
        JButton loadButton = createStyledButton("Load Game");

        // --- Button actions ---
        newButton.addActionListener(e -> {
            // TODO: new GamePanel(frame) or whatever comes next
            System.out.println("New Game clicked");
        });

        loadButton.addActionListener(e -> {
            // TODO: load save and switch screen
            System.out.println("Load Game clicked");
        });

        contentPanel.add(label);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(newButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(loadButton);

        background.add(contentPanel);

        frame.setContentPane(background);
        frame.revalidate();
        frame.repaint();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private float glowAlpha = 0f;
            private Timer fadeTimer;
            private boolean targetState = false;

            {
                fadeTimer = new Timer(16, e -> {
                    if (targetState) {
                        glowAlpha = Math.min(1f, glowAlpha + 0.08f);
                    } else {
                        glowAlpha = Math.max(0f, glowAlpha - 0.08f);
                    }
                    repaint();
                    if ((targetState && glowAlpha >= 1f) || (!targetState && glowAlpha <= 0f)) {
                        fadeTimer.stop();
                    }
                });

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        targetState = true;
                        fadeTimer.start();
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        targetState = false;
                        fadeTimer.start();
                        setCursor(Cursor.getDefaultCursor());
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        glowAlpha = 1f;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        targetState = false;
                        fadeTimer.start();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (glowAlpha > 0f) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha * 0.25f));
                    g2.setColor(new Color(181, 135, 72));
                    g2.fillRoundRect(-4, -4, w + 8, h + 8, 12, 12);

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha * 0.15f));
                    g2.setColor(new Color(220, 180, 100));
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                }

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color borderColor = interpolateColor(
                        new Color(120, 72, 48),
                        new Color(220, 170, 80),
                        glowAlpha
                );

                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setForeground(new Color(181, 135, 72));
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        return button;
    }

    private Color interpolateColor(Color c1, Color c2, float t) {
        int r = (int) (c1.getRed()   + t * (c2.getRed()   - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue()  + t * (c2.getBlue()  - c1.getBlue()));
        return new Color(r, g, b);
    }
}