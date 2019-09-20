package com.runescape.media.gameframe;

import main.java.com.runescape.Game;
import com.runescape.cache.media.Sprite;
import com.runescape.media.ImageProducer;
import com.runescape.net.CacheArchive;

/**
 * Represents a gameframe.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public abstract class Gameframe {

    /**
     * The {@link Game} instance.
     */
    private final Game game;
    /**
     * The chat {@link com.runescape.media.ImageProducer}.
     */
    private ImageProducer chatImageProducer;
    /**
     * The minimap {@link com.runescape.media.ImageProducer}.
     */
    private ImageProducer minimapImageProducer;

    /**
     * The tab {@link com.runescape.media.ImageProducer}.
     */
    private ImageProducer tabImageProducer;

    /**
     * The game screen {@link com.runescape.media.ImageProducer}.
     */
    private ImageProducer gameScreenImageProducer;

    /**
     * Constructs a new <code>GameFrame</code>.
     *
     * @param game The {@link Game} instance.
     */
    public Gameframe(Game game) {
        this.game = game;
    }

    /**
     * Sets the image producers.
     */
    public abstract void setImageProducers();

    /**
     * Resets the image producers.
     */
    public void resetImageProducers() {
        setChatImageProducer(null);
        setMinimapImageProducer(null);
        setTabImageProducer(null);
        setGameScreenImageProducer(null);
    }

    /**
     * The id of this gameframe.
     *
     * @return The id.
     */
    public abstract int getId();

    /**
     * Sets any pre-variables up.
     */
    public abstract void setup();

    /**
     * Registers the gameframe sprites on the client load.
     *
     * @param cacheArchive The media {@link com.runescape.net.CacheArchive}.
     */
    public abstract void registerSprites(CacheArchive cacheArchive);

    /**
     * Draws the minimap area.
     */
    public abstract void drawMinimapArea();

    /**
     * Draws the XP drop tracker orb.
     *
     * @param xOffset The x offset for drawing.
     */
    public void drawXPTracker(int xOffset) {
    }

    /**
     * Draws the XP drop tracker frame.
     *
     * @param xOffset The x offset for drawing.
     */
    public void drawXPTrackerFrame(int xOffset) {
    }

    /**
     * Draws the minimap {@link com.runescape.cache.media.Sprite}.
     */
    public abstract void drawMinimapSprite();

    /**
     * Renders the minimap scene area.
     *
     * @param currentPlane The current plane.
     */

    public abstract void renderMapScene(int currentPlane);

    /**
     * Refreshes the minimap.
     *
     * @param sprite  The minimap sprite.
     * @param yOffset The y offset.
     * @param xOffset The x offset.
     */
    public abstract void refreshMinimap(Sprite sprite, int yOffset, int xOffset);

    /**
     * Draws minimap scenes.
     *
     * @param y              The y offset.
     * @param primaryColor   The primary color.
     * @param x              The x offset.
     * @param secondaryColor The secondary color.
     * @param z              The plane.
     */
    public abstract void drawMapScenes(int y, int primaryColor, int x, int secondaryColor, int z);

    /**
     * Marks the minimap.
     *
     * @param sprite The sprite marking.
     * @param iconX  The x offset.
     * @param iconY  The y offset.
     */
    public abstract void markMinimap(Sprite sprite, int iconX, int iconY);

    /**
     * Gets the width for the minimap producer.
     *
     * @return The width.
     */
    public abstract int getMinimapProducerWidth();

    /**
     * Draws the tab area.
     */
    public abstract void drawTabArea();

    /**
     * Handles processing the tab area and options.
     */
    public abstract void processTabArea();

    /**
     * Handles processing tab hovers.
     */
    public abstract void processTabHover();

    /**
     * Handles processing tabs.
     */
    public abstract void processTabs();

    /**
     * Handles drawing side icons.
     */
    public abstract void drawSideIcons();

    /**
     * Process the tab area hovering.
     */
    public abstract void processTabAreaHovers();

    /**
     * Process tab area clicking.
     */
    public abstract void processTabAreaClick();

    /**
     * Draws the chat area.
     */
    public abstract void drawChatArea();

    /**
     * Processes right clicking areas.
     */
    public abstract void processHoverAndClicks();

    /**
     * Processes the main screen clicking.
     */
    public abstract void processMainScreenClick();

    /**
     * Updates the game area.
     */
    public abstract void updateGameArea();

    /**
     * If the right frame bar should be drawn.
     *
     * @return <code>True</code> if so.
     */
    public abstract boolean drawRightFrame();

    /**
     * If an area can be clicked.
     *
     * @return <code>True</code> if so.
     */
    public abstract boolean canClickArea();

    /**
     * Determines the open menu size.
     */
    public abstract void determineMenuSize();

    /**
     * Gets the {@link Game} instance.
     *
     * @return The game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the chat {@link com.runescape.media.ImageProducer}.
     *
     * @return The image producer.
     */
    public ImageProducer getChatImageProducer() {
        return chatImageProducer;
    }

    /**
     * Sets the chat {@link com.runescape.media.ImageProducer}.
     *
     * @param chatImageProducer The image producer to set.
     */
    public void setChatImageProducer(ImageProducer chatImageProducer) {
        this.chatImageProducer = chatImageProducer;
    }

    /**
     * Gets the minimap {@link com.runescape.media.ImageProducer}.
     *
     * @return The image producer.
     */
    public ImageProducer getMinimapImageProducer() {
        return minimapImageProducer;
    }

    /**
     * Sets the minimap {@link com.runescape.media.ImageProducer}.
     *
     * @param minimapImageProducer The image producer to set.
     */
    public void setMinimapImageProducer(ImageProducer minimapImageProducer) {
        this.minimapImageProducer = minimapImageProducer;
    }

    /**
     * Gets the tab {@link com.runescape.media.ImageProducer}.
     *
     * @return The image producer.
     */
    public ImageProducer getTabImageProducer() {
        return tabImageProducer;
    }

    /**
     * Sets the tab {@link com.runescape.media.ImageProducer}.
     *
     * @param tabImageProducer The image producer to set.
     */
    public void setTabImageProducer(ImageProducer tabImageProducer) {
        this.tabImageProducer = tabImageProducer;
    }

    /**
     * Gets the game screen {@link com.runescape.media.ImageProducer}.
     *
     * @return The image producer.
     */
    public ImageProducer getGameScreenImageProducer() {
        return gameScreenImageProducer;
    }

    /**
     * Sets the game screen {@link com.runescape.media.ImageProducer}.
     *
     * @param gameScreenImageProducer The image producer to set.
     */
    public void setGameScreenImageProducer(ImageProducer gameScreenImageProducer) {
        this.gameScreenImageProducer = gameScreenImageProducer;
    }
}
