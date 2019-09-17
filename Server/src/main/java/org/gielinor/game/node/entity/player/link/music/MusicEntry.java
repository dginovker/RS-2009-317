package org.gielinor.game.node.entity.player.link.music;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Represents a song.
 *
 * @author Emperor
 *
 */
public final class MusicEntry {

    /**
     * The songs mapping.
     */
    private static final Int2ObjectMap<MusicEntry> SONGS = new Int2ObjectOpenHashMap<>();

    /**
     * The music id.
     */
    private final int id;

    /**
     * The song name.
     */
    private final String name;

    /**
     * The id of the button.
     */
    private final int buttonId;

    /**
     * The index in the list.
     */
    private final int index;

    /**
     * Constructs a new {@code MusicEntry} {@code Object}.
     *
     * @param id
     *            the music id.
     * @param name
     *            The name.
     * @param index
     *            The list index.
     */
    public MusicEntry(int id, String name, int buttonId, int index) {
        this.id = id;
        this.name = name;
        this.buttonId = buttonId;
        this.index = index;
    }

    /**
     * Gets the song for the given music id.
     *
     * @param id
     *            The music id.
     * @return The song.
     */
    public static MusicEntry forId(int id) {
        return SONGS.get(id);
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.link.music.MusicEntry} for
     * the given button id.
     *
     * @param buttonId
     *            The id of the button.
     * @return The entry.
     */
    public static MusicEntry forButtonId(int buttonId) {
        for (MusicEntry musicEntry : SONGS.values()) {
            if (musicEntry.buttonId == buttonId) {
                return musicEntry;
            }
        }

        return null;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets id of the button.
     *
     * @return The button id.
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Gets the index.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the songs.
     *
     * @return The songs.
     */
    public static Int2ObjectMap<MusicEntry> getSongs() {
        return SONGS;
    }

}
