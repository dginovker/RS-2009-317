package org.gielinor.utilities.game;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.world.callback.CallBack;
import org.gielinor.utilities.buffer.ByteBufferUtils;

/**
 * Represents a repository of banned mac addresses.
 * @author 'Vexia
 * @date 25/11/2013
 */
public final class MacBanRepository implements CallBack {

    /**
     * Represents the singleton of this class.
     */
    private final static MacBanRepository SINGLETON = new MacBanRepository();

    /**
     * Represents the list of macbans.
     */
    private static final List<String> MACS = new ArrayList<>();

    /**
     * Represents the directory of ipbans.
     */
    private final String DIRECTORY = "data/world/misc/macbans.arios";

    /**
     * Method used to ban an ip address.
     * @param mac the ip.
     * @return <code>True</code> if added.
     */
    public final boolean ban(final String mac) {
        if (MACS.contains(mac)) {
            return true;
        }
        if (MACS.add(mac)) {
            write();
            return true;
        }
        return false;
    }

    /**
     * Method used to check if the address was removed.
     * @param mac the address.
     * @return <code>True</code> if removed.
     */
    public final boolean remove(final String mac) {
        if (MACS.remove(mac)) {
            write();
            return true;
        }
        return false;
    }

    /**
     * Method used to check if the repository contains this flagged ip.
     * @param mac the address.
     * @return <code>True</code> if flagged.
     */
    public final boolean flagged(final String mac) {
        return MACS.contains(mac);
    }

    /**
     * Method used to write the active mac bans to the file.
     */
    public final void write() {
        final File file = new File(DIRECTORY);
        if (file.exists()) {
            file.delete();
        }
        final ByteBuffer buffer = ByteBuffer.allocate(100000);
        for (String s : MACS) {
            buffer.put((byte) 1);
            ByteBufferUtils.putString(s, buffer);
        }
        buffer.put((byte) 0);//EOF.
        buffer.flip();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel()) {
            channel.write(buffer);
            raf.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean call() {
        final File file = new File(DIRECTORY);
        if (true) {
            return true;
        }
        if (!file.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {
            MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
            String ip;
            if (!buffer.hasRemaining()) {
                return true;
            }
            while ((buffer.get() != 0)) {
                ip = (ByteBufferUtils.getString(buffer));
                if (!MACS.contains(ip)) {
                    MACS.add(ip);
                } else {
                    break;
                }
            }
            raf.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Method used to return he singleton of this class.
     * @return the singleton.
     */
    public static MacBanRepository getRepository() {
        return SINGLETON;
    }

    /**
     * Gets the mac bans.
     * @return the mac bans.
     */
    public static List<String> getBans() {
        return MACS;
    }
}
