package com.runescape.cache.def;

import com.runescape.Constants;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

import java.nio.ByteBuffer;

/**
 * Shit attempt to make loading definitions better.
 *
 * @author Hacker
 */
public class DefinitionData {
    public RSStream buffer;
    public int[] indices;
    public int cacheIndex;
    private int count;

    public void load(CacheArchive archive, String name, boolean osrs) {
        load(archive, name, osrs, 0);
    }

    public void load(CacheArchive archive, String name, boolean osrs, int extra) {
        ByteBuffer indexBuffer;
        if (osrs) {
            buffer = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/" + name + "159.dat"));
            indexBuffer = ByteBuffer.wrap(FileUtility.getBytes(Constants.getCachePath(true) + "/config/" + name + "159.idx"));
        } else {
            buffer = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/498" + name + ".dat"));
            indexBuffer = ByteBuffer.wrap(FileUtility.getBytes(Constants.getCachePath(true) + "/config/498" + name + ".idx"));
        }
        count = indexBuffer.getShort();
        indices = new int[count + extra];
        int offset = 2;
        for (int index = 0; index < count; index++) {
            indices[index] = offset;
            offset += indexBuffer.getShort() & 0xFFFF;
        }
    }

    public int getOffset(int id) {
        return indices[id];
    }

    public int getCount() {
        return count;
    }

    public int getCacheIndex() {
        return cacheIndex;
    }

    public void setCacheIndex(int cacheIndex) {
        this.cacheIndex = cacheIndex;
    }

    public int[] getIndices() {
        return indices;
    }

    public RSStream getBuffer() {
        return buffer;
    }

    public void clear() {
        buffer = null;
        indices = null;
    }
}
