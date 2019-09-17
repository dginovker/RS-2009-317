package com.runescape;

import java.awt.*;
import java.io.File;

public final class GameFrame extends Frame {

    private static final long serialVersionUID = 1L;
    protected final Insets insets;
    private final GameShell applet;
    public Toolkit toolkit = Toolkit.getDefaultToolkit();
    public Dimension screenSize = toolkit.getScreenSize();
    public int screenWidth = (int) screenSize.getWidth();
    public int screenHeight = (int) screenSize.getHeight();

    public GameFrame(GameShell applet, int width, int height, boolean resizable, boolean fullscreen) {
        this.applet = applet;
        setTitle(Constants.CLIENT_NAME);
        setResizable(resizable);
        setUndecorated(fullscreen);
        setVisible(true);
        insets = getInsets();
        if (resizable) {
            setMinimumSize(new Dimension(766 + insets.left + insets.right, 536 + insets.top + insets.bottom));
        }
        setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
        //setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        requestFocus();
        toFront();
    }

    public void setClientIcon() {
        Image img = Toolkit.getDefaultToolkit().createImage(Constants.getCachePath(true) + File.separator + "icon.png");
        if (img == null) {
            return;
        }
        setIconImage(img);
    }

    public Graphics getGraphics() {
        final Graphics graphics = super.getGraphics();
        Insets insets = this.getInsets();
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.translate(insets != null ? insets.left : 0, insets != null ? insets.top : 0);
        return graphics;
    }

    public int getFrameWidth() {
        Insets insets = this.getInsets();
        return getWidth() - (insets.left + insets.right);
    }

    public int getFrameHeight() {
        Insets insets = this.getInsets();
        return getHeight() - (insets.top + insets.bottom);
    }

    public void update(Graphics graphics) {
        applet.update(graphics);
    }

    public void paint(Graphics graphics) {
        applet.paint(graphics);
    }
}