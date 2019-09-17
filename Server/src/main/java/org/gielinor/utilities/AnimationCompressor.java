package org.gielinor.utilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/21/2015.
 * Everything in the main method lolll u n0 how w3 d00
 */
public class AnimationCompressor {

    public static boolean osrs = false;

    public static void main(String args[]) {
        Path path = Paths.get("data/" + (osrs ? "osrs" : "") + "anims");
        List<DataModel> dataModels = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, osrs ? "*.{gz}" : "*")) {
            for (Path entry : stream) {
                int id = Integer.parseInt(entry.getFileName().toString().replaceFirst("[.][^.]+$", ""));
                try (RandomAccessFile fileHandle = new RandomAccessFile(entry.toFile(), "r")) {
                    MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileHandle.length());
                    dataModels.add(new DataModel(id, (int) fileHandle.length(), mbb));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int totalLength = dataModels.stream().mapToInt(dm -> dm.getData().capacity()).sum() + (dataModels.size() * 8);
        try (RandomAccessFile fileHandle = new RandomAccessFile(new File("cache\\animation_" + (osrs ? "osrs_cache.dat" : "cache.dat")), "rw")) {
            final MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, totalLength);
            dataModels.forEach(e -> {
                mbb.putInt(e.getId());
                mbb.putInt(e.getLength());
                mbb.put(e.getData());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class DataModel {

        private final int id;
        private final int length;
        private final ByteBuffer data;

        public DataModel(int id, int length, ByteBuffer data) {
            this.id = id;
            this.length = length;
            this.data = data;
        }

        public int getId() {
            return id;
        }

        public int getLength() {
            return length;
        }

        public ByteBuffer getData() {
            return data;
        }

    }

}
