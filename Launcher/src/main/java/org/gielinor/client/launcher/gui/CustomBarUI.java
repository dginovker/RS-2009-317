package org.gielinor.client.launcher.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ProgressBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author Corey
 * @author arham 4
 */
public class CustomBarUI extends ProgressBarUI {

    @Override
    public void paint(final Graphics g, final JComponent component) {
        update(g, component);
    }

    @Override
    public void update(final Graphics g, final JComponent component) {
        final int WIDTH = component.getWidth();
        final int HEIGHT = component.getHeight();

        final double percentageReady = ((JProgressBar) component).getPercentComplete();

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/themes/default/progress_bar_backer.png"));
            if (percentageReady > 0) {
                image = image.getSubimage(0, 0, (int) (WIDTH * percentageReady), HEIGHT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.clipRect(0, 0, (int) (WIDTH * percentageReady), HEIGHT);
        g.drawImage(image, 0, 0, null);

        g.dispose();
    }
}