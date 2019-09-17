package com.runescape.cache.media;

/**
 * Represents a child belonging to an {@link com.runescape.cache.media.RSComponent}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class InterfaceChild {

    /**
     * The id of the child.
     */
    private final int id;
    /**
     * The x coordinate of the child.
     */
    private int x;
    /**
     * The y coordinate of the child.
     */
    private int y;

    /**
     * Constructs a new {@code InterfaceChild}.
     *
     * @param id The id of the child.
     * @param x  The x coordinate of the child.
     * @param y  The y coordinate of the child.
     */
    public InterfaceChild(int id, int x, int y) {
        this.id = id;
        this.y = y;
        this.x = x;
    }

    /**
     * Gets the id of the child.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the x coordinate of the child.
     *
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the child.
     *
     * @param x The x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the child.
     *
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the child.
     *
     * @param y The y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the {@link com.runescape.cache.media.RSComponent} this child belongs to.
     *
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent getRSComponent() {
        return RSComponent.forId(getId());
    }

    /**
     * Removes this child from the {@link com.runescape.cache.media.RSComponent} children cache.
     */
    public void remove() {
        getRSComponent().getInterfaceChildren().remove(this);
    }
}
