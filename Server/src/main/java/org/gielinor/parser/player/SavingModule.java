package org.gielinor.parser.player;

import java.nio.ByteBuffer;

/**
 * Represents a saving module.
 *
 * @author Emperor
 */
public interface SavingModule {

    /**
     * Writes data on the byte buffer.
     *
     * @param byteBuffer The byte buffer.
     */
    void save(ByteBuffer byteBuffer);

    /**
     * Parses data from the byte buffer.
     *
     * @param byteBuffer The byte buffer.
     */
    void parse(ByteBuffer byteBuffer);
}