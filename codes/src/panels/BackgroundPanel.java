package panels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String fileName) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(fileName));
            // Apply the blur effect before saving it to the backgroundImage variable
            this.backgroundImage = blurImage(originalImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage blurImage(BufferedImage src) {
        int radius = 5;
        int size = radius * radius;
        float[] matrix = new float[size];
        for (int i = 0; i < size; i++) {
            matrix[i] = 1.0f / size;
        }

        ConvolveOp op = new ConvolveOp(new Kernel(radius, radius, matrix), ConvolveOp.EDGE_NO_OP, null);
        return op.filter(src, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
