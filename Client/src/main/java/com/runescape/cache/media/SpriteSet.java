package com.runescape.cache.media;

/**
 * Represents an enabled and disabled {@link Sprite} set.
 *
 * @author Gielinor
 */
public class SpriteSet {

    /**
     * Represents a null type.
     */
    public static final SpriteSet NULL_TYPE = null;
    /**
     * Represents an empty sprite.
     */
    public static final Sprite EMPTY = SpriteRepository.EMPTY;
    /**
     * Represents an empty sprite name.
     */
    public static final String EMPTY_STRING = SpriteRepository.EMPTY.spriteName;
    /**
     * The enabled {@link Sprite}.
     */
    private Sprite enabled;
    /**
     * The disabled {@link Sprite}.
     */
    private Sprite disabled;

    /**
     * Constructs a new {@link com.runescape.cache.media.SpriteSet}.
     */
    public SpriteSet() {

    }

    /**
     * Constructs a new {@link SpriteSet}.
     *
     * @param enabled  The enabled {@link Sprite}.
     * @param disabled The disabled {@link Sprite}.
     */
    public SpriteSet(Sprite enabled, Sprite disabled) {
        this.enabled = enabled;
        this.disabled = disabled;
    }

    /**
     * Constructs a new {@link SpriteSet} from the {@link ImageLoader}.
     *
     * @param enabled  The enabled {@link Sprite} name.
     * @param disabled The disabled {@link Sprite} name.
     * @deprecated {@link SpriteSet(String, String)} has been deprecated, use {@link SpriteSet(Sprite, Sprite)} instead.
     */
    @Deprecated
    public SpriteSet(String enabled, String disabled) {
        this.enabled = enabled == null ? null : ImageLoader.forName(enabled);
        this.disabled = disabled == null ? null : ImageLoader.forName(disabled);
        if (this.enabled != null) {
            this.enabled.spriteName = enabled;
        }
        if (this.disabled != null) {
            this.disabled.spriteName = disabled;
        }
    }

    /**
     * Constructs a new {@link SpriteSet} from the {@link ImageLoader}.
     *
     * @param sprite The {@link Sprite} name.
     */
    public SpriteSet(String sprite) {
        this.enabled = ImageLoader.forName(sprite);
        this.disabled = ImageLoader.forName(sprite);
    }

    /**
     * Sets the enabled and disabled {@link Sprite}.
     *
     * @param enabled  The enabled sprite.
     * @param disabled The disabled sprite.
     */
    public void set(Sprite enabled, Sprite disabled) {
        this.enabled = enabled;
        this.disabled = disabled;
    }

    /**
     * Gets the enabled {@link Sprite}.
     *
     * @return The enabled sprite.
     */
    public Sprite getEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled {@link Sprite}.
     *
     * @param enabled The enabled sprite.
     */
    public void setEnabled(Sprite enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the disabled {@link com.runescape.cache.media.Sprite}.
     *
     * @return The disabled sprite.
     */
    public Sprite getDisabled() {
        return disabled;
    }

    /**
     * Sets the disabled {@link com.runescape.cache.media.Sprite}.
     *
     * @param disabled The disabled sprite.
     */
    public void setDisabled(Sprite disabled) {
        this.disabled = disabled;
    }

    /**
     * Swaps the two {@link com.runescape.cache.media.Sprite} classes.
     *
     * @return The new {@link com.runescape.cache.media.SpriteSet}.
     */
    public SpriteSet swap() {
        return new SpriteSet(disabled, enabled);
    }

    /**
     * Converts the {@link #enabled} {@link com.runescape.cache.media.SpriteSet} to a {@link com.runescape.cache.media.Sprite}.
     *
     * @return The {@code Sprite}.
     */
    public Sprite toSprite() {
        return enabled;
    }
}
