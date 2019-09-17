package org.gielinor.utilities;

import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Compresses images.
 */
public class ImageCompressor {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ImageCompressor.class);

    private static final boolean using498 = false;
    private static String CACHE_DIRECTORY = "data\\cache\\";
    private static String CACHE_FILE = File.separator + "image_cache.dat";

    public static void main(String args[]) {
        Path path = Paths.get("data/" + (using498 ? "rssprite" : "images"));
        List<DataImage> dataImageList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:.png");
        final int[] compressed = { 0 };
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path entry, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() || matcher.matches(entry)) {
                        try (RandomAccessFile fileHandle = new RandomAccessFile(entry.toFile(), "r")) {
                            MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileHandle.length());
                            log.info("Added imagine: {}.", entry.getFileName());
                            dataImageList.add(new DataImage(
                                entry.getFileName().toString(),
                                (int) fileHandle.length(),
                                mbb));
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        File cacheFile = new File(CACHE_DIRECTORY + CACHE_FILE);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
        int totalLength = dataImageList.stream().mapToInt(dm -> dm.getData().capacity()).sum() + (dataImageList.size() * 32);
        try (RandomAccessFile fileHandle = new RandomAccessFile(cacheFile, "rw")) {
            final MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, totalLength);
            mbb.putShort((short) 0);
            dataImageList.forEach(e -> {
                compressed[0]++;
                mbb.put(e.getName().getBytes());
                mbb.put((byte) 10);
                mbb.putInt(e.getLength());
                mbb.put(e.getData());
                names.add(e.getName());
                log.info("Compressing image: {}", e.getName());
            });
            mbb.putShort(0, (short) compressed[0]);
        } catch (IOException ex) {
            log.error("Failed to write file: [{}].", cacheFile, ex);
        }
        log.info("{} images compressed under {}.", compressed[0], CACHE_DIRECTORY);
    }

    static class DataImage {

        private final String name;
        private final int length;
        private final ByteBuffer data;

        public DataImage(String name, int length, ByteBuffer data) {
            this.name = name;
            this.length = length;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public int getLength() {
            return length;
        }

        public ByteBuffer getData() {
            return data;
        }

    }

}
