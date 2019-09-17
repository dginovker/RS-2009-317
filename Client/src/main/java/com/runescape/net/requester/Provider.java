package com.runescape.net.requester;

public abstract class Provider {

    /**
     * Provides a file for the index.
     *
     * @param type The type.
     * @param file The file id.
     */
    public abstract void provide(int type, int file);
}
