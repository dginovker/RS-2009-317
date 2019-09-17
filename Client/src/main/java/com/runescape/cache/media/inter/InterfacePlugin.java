package com.runescape.cache.media.inter;

import com.runescape.cache.media.RSComponent;
import com.runescape.media.font.GameFont;

/**
 * Represents a custom interface.
 *
 * @author Gielinor
 */
public interface InterfacePlugin {

    /**
     * Loads the interface and its data.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont} classes.
     * @return This interface.
     */
    public RSComponent loadInterface(GameFont[] gameFonts);
}
