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
public class ModelCompressor {
    // OK

    //
    public static boolean Gielinor = true;
    private static final String MODEL_FILE = "FACache" + File.separator + "main_file_cache.idx5";

    public static void main(String args[]) {
        Path path = Paths.get("data/models");
        List<DataModel> dataModels = new ArrayList<>();
        List<Integer> IDS_ADDED = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.{gz}")) {
            for (Path entry : stream) {
                int id = Integer.parseInt(entry.getFileName().toString().replaceFirst("[.][^.]+$", ""));
                try (RandomAccessFile fileHandle = new RandomAccessFile(entry.toFile(), "r")) {
                    MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileHandle.length());
                    dataModels.add(new DataModel((short) id, (int) fileHandle.length(), mbb, false, false, false));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int totalLength = dataModels.size() * 4;
        try (RandomAccessFile fileHandle = new RandomAccessFile(new File("data\\FACache\\modeldata.dat"), "rw")) {
            final MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, totalLength);
            dataModels.forEach(e -> {
                mbb.putInt((int) e.getId());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished compressing models.");
    }

    static class DataModel {

        private final short id;
        private final int length;
        private final ByteBuffer data;
        private final boolean npc;
        private final boolean item;
        private final boolean player;

        public DataModel(short id, int length, ByteBuffer data, boolean npc, boolean item, boolean player) {
            this.id = id;
            this.length = length;
            this.data = data;
            this.npc = npc;
            this.item = item;
            this.player = player;
        }

        public short getId() {
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
