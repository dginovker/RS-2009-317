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
 * Represents a repository of banned ip addresses.
 * @author 'Vexia
 * @date 25/11/2013
 */
public class IPBanRepository implements CallBack {

    /**
     * Represents the singleton of this class.
     */
    private final static IPBanRepository SINGLETON = new IPBanRepository();

    /**
     * Represents the list of ipbans.
     */
    private static final List<String> IPS = new ArrayList<>();

    /**
     * Represents the directory of ipbans.
     */
    private final String DIRECTORY = "data/world/misc/ipbans.arios";

    /**
     * Method used to ban an ip address.
     * @param ip the ip.
     * @return <code>True</code> if added.
     */
    public final boolean ban(final String ip) {
        if (IPS.contains(ip) || ip.equals("127.0.0.1")) {
            return true;
        }
        if (IPS.add(ip)) {
            write();
            return true;
        }
        return false;
    }

    /**
     * Method used to check if the address was removed.
     * @param ip the ip.
     * @return <code>True</code> if removed.
     */
    public final boolean remove(final String ip) {
        if (IPS.remove(ip)) {
            write();
            return true;
        }
        return false;
    }

    /**
     * Method used to check if the repository contains this flagged ip.
     * @param ip the ip.
     * @return <code>True</code> if flagged.
     */
    public final boolean flagged(final String ip) {
        return IPS.contains(ip);
    }

    /**
     * Method used to write the active ip banns to the file.
     */
    public final void write() {
        final File file = new File(DIRECTORY);
        if (file.exists()) {
            file.delete();
        }
        final ByteBuffer buffer = ByteBuffer.allocate(100000);
        for (String s : IPS) {
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
                if (!IPS.contains(ip)) {
                    IPS.add(ip);
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
    public static IPBanRepository getRepository() {
        return SINGLETON;
    }

    /**
     * Method used to return all ip bans.
     * @return the ip bans.
     */
    public static List<String> getIps() {
        return IPS;
    }
}
