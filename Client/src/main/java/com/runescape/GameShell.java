package com.runescape;

import com.runescape.cache.media.InterfaceChild;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.ImageProducer;
import com.runescape.util.Console;
import com.runescape.Game;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameShell extends Applet implements Runnable, MouseListener,
        MouseMotionListener, MouseWheelListener, KeyListener, FocusListener,
        WindowListener {

    private static final long serialVersionUID = 1L;
    public static int anInt34;
    private static Console console = new Console();
    public final int LEFT = 0;
    public final int RIGHT = 1;
    public final int DRAG = 2;
    public final int RELEASED = 3;
    public final int MOVE = 4;
    final int keyArray[] = new int[128];
    private final long aLongArray7[] = new long[10];
    private final int charQueue[] = new int[128];
    public Graphics graphics;
    public boolean loading = true;
    public boolean overrideLoading = false;
    public int mouseX;
    public int mouseY;
    public long aLong29;
    public int clickMode3;
    public int saveClickX;
    public int saveClickY;
    public boolean isLoading;
    public boolean isApplet;
    public boolean resized;
    public int clickType;
    public int releasedX;
    public int releasedY;
    public boolean mouseWheelDown;
    public boolean ctrlShiftPressed;
    public boolean ctrlPressed;
    public boolean shiftPressed;
    public int mouseWheelX;
    public int mouseWheelY;
    public long loadTime;
    protected int rotationGliding;
    int minDelay;
    int fps;
    boolean shouldDebug;
    int myWidth;
    int myHeight;
    ImageProducer fullGameScreen;
    protected GameFrame mainFrame;
    boolean awtFocus;
    int idleTime;
    protected int clickMode2;
    int clickMode1;
    private int exitTimer;
    private int delayTime;
    private boolean shouldClearScreen;
    private int clickX;
    private int clickY;
    private int readIndex;
    private int writeIndex;

    public GameShell() {
        delayTime = 20;
        minDelay = 1;
        shouldDebug = false;
        shouldClearScreen = true;
        awtFocus = true;
    }

    /**
     * Gets the {@link com.runescape.util.Console} instance.
     *
     * @return The {@code Console} instance.
     */
    public static Console getConsole() {
        return console;
    }

    /**
     * Sets the cursor.
     *
     * @param data The cursor image data.
     */
    public void setCursor(byte[] data) {
        Image image = getGameComponent().getToolkit().createImage(data);
        getGameComponent().setCursor(
                getGameComponent().getToolkit().createCustomCursor(image,
                        new Point(0, 0), null));
    }

    public void refreshFrameSize(boolean undecorated, int width, int height,
                                 boolean resizable, boolean full) {
        boolean createdByApplet = (isApplet && !full);
        myWidth = width;
        myHeight = height;
        if (mainFrame != null) {
            mainFrame.dispose();
        }
        if (!createdByApplet) {
            mainFrame = new GameFrame(this, width, height, resizable,
                    undecorated);
            mainFrame.addWindowListener(this);
        }
        graphics = (createdByApplet ? this : mainFrame).getGraphics();
        if (!createdByApplet) {
            getGameComponent().addMouseWheelListener(this);
            getGameComponent().addMouseListener(this);
            getGameComponent().addMouseMotionListener(this);
            getGameComponent().addKeyListener(this);
            getGameComponent().addFocusListener(this);
        }
        if (!createdByApplet && mainFrame != null) {
            mainFrame.setClientIcon();
        }
    }

    public boolean appletClient() {
        return mainFrame == null && isApplet == true;
    }

    final void createClientFrame(int w, int h) {
        isApplet = false;
        myWidth = w;
        myHeight = h;
        mainFrame = new GameFrame(this, myWidth, myHeight,
                Game.getScreenMode() == Game.ScreenMode.RESIZABLE,
                Game.getScreenMode() == Game.ScreenMode.FULLSCREEN);
        mainFrame.setFocusTraversalKeysEnabled(false);
        graphics = getGameComponent().getGraphics();
        fullGameScreen = new ImageProducer(myWidth, myHeight);
        startRunnable(this, 1);
    }

    final void initClientFrame(int w, int h) {
        isApplet = true;
        myWidth = w;
        myHeight = h;
        graphics = getGameComponent().getGraphics();
        fullGameScreen = new ImageProducer(myWidth, myHeight);
        startRunnable(this, 1);
    }

    public void run() {
        getGameComponent().addMouseListener(this);
        getGameComponent().addMouseMotionListener(this);
        getGameComponent().addKeyListener(this);
        getGameComponent().addFocusListener(this);
        getGameComponent().addMouseWheelListener(this);
        if (mainFrame != null) {
            mainFrame.addWindowListener(this);
        }
        drawLoadingText(0, "Loading...");
        startUp();
        int i = 0;
        int j = 256;
        int k = 1;
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < 10; j1++) {
            aLongArray7[j1] = System.currentTimeMillis();
        }
        do {

            if (exitTimer < 0) {
                break;
            }
            if (exitTimer > 0) {
                exitTimer--;
                if (exitTimer == 0) {
                    shutdown(true);
                    return;
                }
            }
            int k1 = j;
            int i2 = k;
            j = 300;
            k = 1;
            long l2 = System.currentTimeMillis();
            if (aLongArray7[i] == 0L) {
                j = k1;
                k = i2;
            } else if (l2 > aLongArray7[i]) {
                j = (int) ((long) (2560 * delayTime) / (l2 - aLongArray7[i]));
            }
            if (j < 25) {
                j = 25;
            }
            if (j > 256) {
                j = 256;
                k = (int) ((long) delayTime - (l2 - aLongArray7[i]) / 10L);
            }
            if (k > delayTime) {
                k = delayTime;
            }
            aLongArray7[i] = l2;
            i = (i + 1) % 10;
            if (k > 1) {
                for (int j2 = 0; j2 < 10; j2++) {
                    if (aLongArray7[j2] != 0L) {
                        aLongArray7[j2] += k;
                    }
                }

            }
            if (k < minDelay) {
                k = minDelay;
            }
            try {
                Thread.sleep(k);
            } catch (InterruptedException interruptedexception) {
                i1++;
            }
            for (; l < 256; l += j) {
                clickMode3 = clickMode1;
                saveClickX = clickX;
                saveClickY = clickY;
                clickMode1 = 0;
                processGameLoop();
                readIndex = writeIndex;
            }

            l &= 0xff;
            if (delayTime > 0) {
                fps = (1000 * j) / (delayTime * 256);
            }
            processDrawing();
            if (shouldDebug) {
                System.out.println((new StringBuilder()).append("ntime:")
                        .append(l2).toString());
                for (int k2 = 0; k2 < 10; k2++) {
                    int i3 = ((i - k2 - 1) + 20) % 10;
                    System.out.println((new StringBuilder()).append("otim")
                            .append(i3).append(":").append(aLongArray7[i3])
                            .toString());
                }

                System.out.println((new StringBuilder()).append("fps:")
                        .append(fps).append(" ratio:").append(j)
                        .append(" count:").append(l).toString());
                System.out.println((new StringBuilder()).append("del:")
                        .append(k).append(" deltime:").append(delayTime)
                        .append(" mindel:").append(minDelay).toString());
                System.out.println((new StringBuilder()).append("intex:")
                        .append(i1).append(" opos:").append(i).toString());
                shouldDebug = false;
                i1 = 0;
            }
        } while (true);
        if (exitTimer == -1) {
            shutdown(true);
        }
    }

    public void shutdown(boolean clean) {
        exitTimer = -2;
        cleanUpForQuit();
        Logger.getLogger(GameShell.class.getName()).log(Level.INFO, "Shutdown complete - clean:" + clean);
        System.exit(-1);
    }

    final void method4(int i) {
        delayTime = 1000 / i;
    }

    public final void start() {
        if (exitTimer >= 0) {
            exitTimer = 0;
        }
    }

    public final void stop() {
        if (exitTimer >= 0) {
            exitTimer = 4000 / delayTime;
        }
    }

    public final void destroy() {
        exitTimer = -1;
        try {
            Thread.sleep(5000L);
        } catch (Exception exception) {
        }
        if (exitTimer == -1) {
            shutdown(false);
        }
    }

    public final void update(Graphics g) {
        if (graphics == null) {
            graphics = g;
        }
        shouldClearScreen = true;
        raiseWelcomeScreen();
    }

    public final void paint(Graphics g) {
        if (graphics == null) {
            graphics = g;
        }
        shouldClearScreen = true;
        raiseWelcomeScreen();
    }

    public void mouseWheelMoved(MouseWheelEvent event) {
        int notches = event.getWheelRotation();
        if (ctrlPressed) {
            if (notches > 0) {
                if (Game.cameraZoom < 1500) {
                    Game.cameraZoom += 50;
                }
            } else {
                if (Game.cameraZoom > 0) {
                    Game.cameraZoom -= 50;
                }
            }
            return;
        }
        handleComponentScrolling(event);
    }

    public void handleComponentScrolling(MouseWheelEvent event) {
        int notches = event.getWheelRotation();
        if (mouseX > 0 && mouseX < 512 && mouseY > Game.getFrameHeight() - 165 && mouseY < Game.getFrameHeight() - 25) {
            if (Game.INSTANCE.inputState == 3) {
                Game.INSTANCE.getGrandExchange().handleScrolling(notches);
            } else {
                int scrollPos = Game.chatScrollPosition;
                scrollPos -= notches * 30;
                if (scrollPos < 0) {
                    scrollPos = 0;
                }
                if (scrollPos > Game.chatScrollMax - 110) {
                    scrollPos = Game.chatScrollMax - 110;
                }
                if (Game.chatScrollPosition != scrollPos) {
                    Game.chatScrollPosition = scrollPos;
                    Game.inputTaken = true;
                }
            }
        }
        int positionX = 0;
        int positionY = 0;
        int width = 0;
        int height = 0;
        int offsetX = 0;
        int offsetY = 0;
        int childID = 0;
        int tabInterfaceID = Game.tabInterfaceIDs[Game.currentTabId];
        if (tabInterfaceID != -1) {
            RSComponent tab = RSComponent.getComponentCache()[tabInterfaceID];
            offsetX = Game.getScreenMode() == Game.ScreenMode.FIXED ? Game.getFrameWidth() - 218 : (Game.getScreenMode() == Game.ScreenMode.FIXED ? 28 : Game.getFrameWidth() - 197);
            offsetY = Game.getScreenMode() == Game.ScreenMode.FIXED ? Game.getFrameHeight() - 298 : (Game.getScreenMode() == Game.ScreenMode.FIXED ? 37 : Game.getFrameHeight() - (Game.getFrameWidth() >= 1000 ? 37 : 74) - 267);
            for (int index = 0; index < tab.getInterfaceChildren().size(); index++) {
                InterfaceChild interfaceChild = tab.getInterfaceChildren().get(index);
                RSComponent rsComponent = interfaceChild.getRSComponent();
                if (rsComponent == null) {
                    continue;
                }
                if (RSComponent.forId(interfaceChild.getId()).scrollMax > 0) {
                    childID = index;
                    positionX = interfaceChild.getX();
                    positionY = interfaceChild.getY();
                    width = rsComponent.width;
                    height = rsComponent.height;
                    break;
                }
            }
            if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
                tab.getInterfaceChildren().get(childID).getRSComponent().scrollPosition += notches * 30;
            }
        }
        if (Game.INSTANCE.getOpenInterfaceId() != -1) {
            RSComponent rsComponent1 = RSComponent.getComponentCache()[Game.INSTANCE.getOpenInterfaceId()];
            offsetX = Game.getScreenMode() == Game.ScreenMode.FIXED ? 4 : (Game.getFrameWidth() / 2) - 356;
            offsetY = Game.getScreenMode() == Game.ScreenMode.FIXED ? 4 : (Game.getFrameHeight() / 2) - 230;
            for (int index = 0; index < rsComponent1.getInterfaceChildren().size(); index++) {
                InterfaceChild interfaceChild = rsComponent1.getInterfaceChildren().get(index);
                RSComponent rsComponent = interfaceChild.getRSComponent();
                if (rsComponent == null) {
                    continue;
                }
                int currentChildX = interfaceChild.getX();
                int currentChildY = interfaceChild.getY();
                boolean myChild = (mouseX >= currentChildX && mouseY >= currentChildY &&
                        mouseX < currentChildX + rsComponent.width && mouseY
                        < currentChildY + rsComponent.height);
                if (myChild && rsComponent.scrollMax > 0) {
                    childID = index;
                    positionX = interfaceChild.getX();
                    positionY = interfaceChild.getY();
                    width = rsComponent.width;
                    height = rsComponent.height;
                    break;
                }
            }
            if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX <
                    offsetX + positionX + width && mouseY < offsetY + positionY + height) {
                rsComponent1.getInterfaceChildren().get(childID).getRSComponent().scrollPosition += notches * 30;
            }
        }
    }

    public final void mousePressed(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        int type = event.getButton();
        if (mainFrame != null) {
            Insets insets = mainFrame.getInsets();
            x -= insets.left;//4
            y -= insets.top;//22
        }
        idleTime = 0;
        clickX = x;
        clickY = y;
        aLong29 = System.currentTimeMillis();
        if (type == 2) {
            mouseWheelDown = true;
            if (ctrlShiftPressed) {
                Game.cameraZoom = 600;
                mouseWheelDown = false;
                return;
            }
            if (Game.INSTANCE.settings[314] != 1) {
                clickType = LEFT;
                clickMode1 = 1;
                clickMode2 = 1;
            }
            mouseWheelX = x;
            mouseWheelY = y;
            return;
        }
        if (event.isMetaDown()) {
            clickType = RIGHT;
            clickMode1 = 2;
            clickMode2 = 2;
        } else {
            clickType = LEFT;
            clickMode1 = 1;
            clickMode2 = 1;
        }
    }

    public final void mouseReleased(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (mainFrame != null) {
            Insets insets = mainFrame.getInsets();
            x -= insets.left;// 4
            y -= insets.top;// 22
        }
        releasedX = x;
        releasedY = y;
        idleTime = 0;
        clickMode2 = 0;
        clickType = RELEASED;
        mouseWheelDown = false;
    }

    public final void mouseClicked(MouseEvent event) {
    }

    public final void mouseEntered(MouseEvent event) {
    }

    public final void mouseExited(MouseEvent event) {
        idleTime = 0;
        mouseX = -1;
        mouseY = -1;
    }

    public final void mouseDragged(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (mainFrame != null) {
            Insets insets = mainFrame.getInsets();
            x -= insets.left;// 4
            y -= insets.top;// 22
        }
        if (mouseWheelDown) {
            y = mouseWheelX - event.getX();
            int k = mouseWheelY - event.getY();
            mouseWheelDragged(y, -k);
            mouseWheelX = event.getX();
            mouseWheelY = event.getY();
            return;
        }
        idleTime = 0;
        mouseX = x;
        mouseY = y;
        clickType = DRAG;
    }

    void mouseWheelDragged(int param1, int param2) {
    }

    public final void mouseMoved(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (mainFrame != null) {
            Insets insets = mainFrame.getInsets();
            x -= insets.left;// 4
            y -= insets.top;// 22
        }
        idleTime = 0;
        mouseX = x;
        mouseY = y;
        clickType = MOVE;
    }

    public final void keyPressed(KeyEvent event) {
        idleTime = 0;
        int keyCode = event.getKeyCode();
        int keyChar = event.getKeyChar();

        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }

        // TODO Needs more testing (dialogue options)
        if (Game.INSTANCE.backDialogueId != -1 && !console.isOpen() && Game.INSTANCE.inputState != 1 &&
                Game.INSTANCE.inputState != 2) {
            switch (keyCode) {
                /**
                 * Space.
                 */
                case 32:
                    if (Game.INSTANCE.isOptionDialogue()) {
                        break;
                    }
                    Game.INSTANCE.menuActionName[2] = "";
                    Game.INSTANCE.menuActionId[2] = 679;
                    Game.INSTANCE.secondMenuAction[2] = RSComponent.forId(Game.INSTANCE.backDialogueId).id;
                    Game.INSTANCE.processMenuActions(2);
                    break;
                /**
                 * Option selects.
                 */
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                    if (!Game.INSTANCE.isOptionDialogue()) {
                        break;
                    }
                    int number;
                    for (number = -1; number < 4; number++) {
                        if (number == (keyCode - 49)) {
                            break;
                        }
                    }
                    if (number == -1) {
                        break;
                    }
                    Game.INSTANCE.menuActionName[1] = "";
                    Game.INSTANCE.menuActionId[1] = 315;
                    Game.INSTANCE.secondMenuAction[1] = (RSComponent.forId(Game.INSTANCE.backDialogueId).id + 2 + number);
                    Game.INSTANCE.processMenuActions(1);
                    break;
            }
        }
        if (keyCode == KeyEvent.VK_BACK_QUOTE || keyChar == 167) {
            // 167 = "Shift + |" (below ESC) (scandinavian keyboard layout; maybe others)
            console.toggle();
            return;
        }
        if (event.getModifiers() == 3) {
            ctrlShiftPressed = true;
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
        }
        if (keyCode == KeyEvent.VK_F1) {
            Game.setTab(3, true);
        } else if (keyCode == KeyEvent.VK_ESCAPE && Game.INSTANCE.fullscreenInterfaceID == -1) {
            // TODO if (escCloses)
            Game.INSTANCE.closeGameInterfaces();
        } else if (keyCode == KeyEvent.VK_F2) {
            Game.setTab(4, true);
        } else if (keyCode == KeyEvent.VK_F3) {
            Game.setTab(5, true);
        } else if (keyCode == KeyEvent.VK_F4) {
            Game.setTab(6, true);
        } else if (keyCode == KeyEvent.VK_F5) {
            Game.setTab(0, true);
        }
        if (keyChar < 30) {
            keyChar = 0;
        }
        if (keyCode == 37) {
            keyChar = 1;
        }
        if (keyCode == 39) {
            keyChar = 2;
        }
        if (keyCode == 38) {
            keyChar = 3;
        }
        if (keyCode == 40) {
            keyChar = 4;
        }
        if (keyCode == 17) {
            keyChar = 5;
        }
        if (keyCode == 8) {
            keyChar = 8;
        }
        if (keyCode == 127) {
            keyChar = 8;
        }
        if (keyCode == 9) {
            keyChar = 9;
        }
        if (keyCode == 10) {
            keyChar = 10;
        }
        if (keyCode >= 112 && keyCode <= 123) {
            keyChar = (1008 + keyCode) - 112;
        }
        if (keyCode == 36) {
            keyChar = 1000;
        }
        if (keyCode == 35) {
            keyChar = 1001;
        }
        if (keyCode == 33) {
            keyChar = 1002;
        }
        if (keyCode == 34) {
            keyChar = 1003;
        }
        if (keyChar > 0 && keyChar < 128) {
            keyArray[keyChar] = 1;
        }
        if (keyChar > 4) {
            charQueue[writeIndex] = keyChar;
            writeIndex = writeIndex + 1 & 0x7f;
        }
    }

    public final void keyReleased(KeyEvent event) {
        idleTime = 0;
        int keyCode = event.getKeyCode();
        char keyChar = event.getKeyChar();
        if (keyCode == 16 || keyCode == 17) {
            ctrlShiftPressed = false;
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
        }
        if (keyChar < '\036') {
            keyChar = '\0';
        }
        if (keyCode == 37) {
            keyChar = '\001';
        }
        if (keyCode == 39) {
            keyChar = '\002';
        }
        if (keyCode == 38) {
            keyChar = '\003';
        }
        if (keyCode == 40) {
            keyChar = '\004';
        }
        if (keyCode == 17) {
            keyChar = '\005';
        }
        if (keyCode == 8) {
            keyChar = '\b';
        }
        if (keyCode == 127) {
            keyChar = '\b';
        }
        if (keyCode == 9) {
            keyChar = '\t';
        }
        if (keyCode == 10) {
            keyChar = '\n';
        }
        if (keyChar > 0 && keyChar < '\200') {
            keyArray[keyChar] = 0;
        }
    }

    public final void keyTyped(KeyEvent event) {
    }

    public int getKeyCode() {
        int keyCode = -1;
        if (writeIndex != readIndex) {
            keyCode = charQueue[readIndex];
            readIndex = readIndex + 1 & 0x7f;
        }
        return keyCode;
    }

    public final void focusGained(FocusEvent event) {
        awtFocus = true;
        shouldClearScreen = true;
        ctrlPressed = false;
        ctrlShiftPressed = false;
        shiftPressed = false;
        raiseWelcomeScreen();
    }

    public final void focusLost(FocusEvent event) {
        awtFocus = false;
        for (int i = 0; i < 128; i++) {
            keyArray[i] = 0;
        }
    }

    public final void windowActivated(WindowEvent windowevent) {
    }

    public final void windowClosed(WindowEvent windowevent) {
    }

    public final void windowClosing(WindowEvent windowevent) {
        destroy();
    }

    public final void windowDeactivated(WindowEvent windowevent) {
    }

    public final void windowDeiconified(WindowEvent windowevent) {
    }

    public final void windowIconified(WindowEvent windowevent) {
    }

    public final void windowOpened(WindowEvent windowevent) {
    }

    void startUp() {
    }

    void processGameLoop() {
    }

    void cleanUpForQuit() {
    }

    void processDrawing() {
    }

    void raiseWelcomeScreen() {
    }

    Component getGameComponent() {
        if (mainFrame != null && !isApplet) {
            return mainFrame;
        } else {
            return this;
        }
    }

    public void startRunnable(Runnable runnable, int i) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
    }

    void prepareGraphics() {
        while (graphics == null) {
            graphics = (isApplet ? this : mainFrame).getGraphics();
            try {
                getGameComponent().repaint();
            } catch (Exception _ex) {
            }

            try {
                // Thread.sleep(1000L);
            } catch (Exception _ex) {
            }
        }
        Font font = new Font("Serif", 1, 16);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
    }

    void drawLoadingText(int percentage, String loadingText) {
        while (graphics == null) {
            graphics = (isApplet ? this : mainFrame).getGraphics();
            try {
                getGameComponent().repaint();
            } catch (Exception _ex) {
            }
            try {
                Thread.sleep(1000L);
            } catch (Exception _ex) {
            }
        }
        Font font = new Font("Helvetica", 1, 13);
        FontMetrics fontmetrics = getGameComponent().getFontMetrics(font);
        Font font1 = new Font("Helvetica", 0, 13);
        FontMetrics fontmetrics1 = getGameComponent().getFontMetrics(font1);
        if (shouldClearScreen) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, Game.getFrameWidth(), Game.getFrameHeight());
            shouldClearScreen = false;
        }
        Color color = new Color(140, 17, 17);
        int y = Game.getFrameHeight() / 2 - 18;
        graphics.setColor(color);
        graphics.drawRect(Game.getFrameWidth() / 2 - 152, y, 304, 34);
        graphics.fillRect(Game.getFrameWidth() / 2 - 150, y + 2, percentage * 3, 30);
        graphics.setColor(Color.black);
        graphics.fillRect((Game.getFrameWidth() / 2 - 150) + percentage * 3, y + 2,
                300 - percentage * 3, 30);
        graphics.setFont(font);
        graphics.setColor(Color.white);
        graphics.drawString(loadingText,
                (Game.getFrameWidth() - fontmetrics.stringWidth(loadingText)) / 2,
                y + 22);
        graphics.drawString("",
                (Game.getFrameWidth() - fontmetrics1.stringWidth("")) / 2, y - 8);
    }

}
