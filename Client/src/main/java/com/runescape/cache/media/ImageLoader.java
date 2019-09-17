package com.runescape.cache.media;

import com.runescape.Constants;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads images for the client.
 *
 * @author Mike M.
 * @author Gielinor
 */
public class ImageLoader {

    /**
     * The {@link java.util.logging.Logger} instance for debugging.
     */
    private static final Logger logger = Logger.getLogger(ImageLoader.class.getName());
    /**
     * The image archive location.
     */
    private static final String IMAGE_FILE = Constants.getCachePath(true) + File.separator + "image_cache.dat";
    /**
     * The mapping of images.
     */
    private static final Map<String, Sprite> IMAGE_MAP = new HashMap<>();
    /**
     * The image directory.
     */
    private String IMAGE_DIRECTORY = null;

    /**
     * Creates the {@link ImageLoader} instance, loading the images into the {@code IMAGE_MAP}.
     */
    public ImageLoader() {
        if (IMAGE_DIRECTORY != null && !IMAGE_DIRECTORY.isEmpty()) {
            if (Constants.DEBUG_MODE) {
                logger.log(Level.WARNING, "Reading images from directory: {0}!", IMAGE_DIRECTORY);
            }
            Path path = Paths.get(IMAGE_DIRECTORY);
            final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:{*.png,*.jpg}");
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path entry, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory() || pathMatcher.matches(entry)) {
                            try (RandomAccessFile randomAccessFile = new RandomAccessFile(entry.toFile(), "r")) {
                                String fileName = entry.getFileName().toString();
                                byte[] spriteData = new byte[(int) randomAccessFile.length()];
                                randomAccessFile.readFully(spriteData);
                                Sprite sprite = new Sprite(spriteData);
                                IMAGE_MAP.put(fileName.substring(0, fileName.lastIndexOf('.')), sprite);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Constants.DEBUG_MODE) {
                logger.log(Level.INFO, "Loaded {0} images from directory.", IMAGE_MAP.size());
            }
            return;
        }
        if (!new File(IMAGE_FILE).exists()) {
            try {
                new File(IMAGE_FILE).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> names = new ArrayList<>();
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(IMAGE_FILE));
            int size = dataInputStream.readShort();
            while (size > 0) {
                String fileName = getString(dataInputStream);
                int length = dataInputStream.readInt();
                byte[] data = new byte[length];
                dataInputStream.readFully(data);
                Sprite sprite = new Sprite(data);
                names.add(fileName);
                IMAGE_MAP.put(fileName.substring(0, fileName.lastIndexOf('.')), sprite);
                size--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Constants.DEBUG_MODE) {
            logger.log(Level.INFO, "Loaded {0} images from archive.", IMAGE_MAP.size());
        }
    }

    public static void main(String[] args) throws Exception {

    }

    /**
     * Reads a String from the {@link java.io.DataInputStream}.
     *
     * @param dataInputStream The {@link java.io.DataInputStream}.
     * @return The string.
     */
    private static String getString(DataInputStream dataInputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte b;
        while (dataInputStream.available() > 0 && (b = dataInputStream.readByte()) != 10) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    /**
     * Fetches the image {@link Sprite} by name.
     *
     * @param name The name of the file.
     * @return The sprite.
     */
    public static Sprite forName(String name) {
        if (IMAGE_MAP.size() == 0) {
            new ImageLoader();
        }
        if (IMAGE_MAP.get(name) == null) {
            if (!name.isEmpty()) {
                logger.log(Level.SEVERE, "Cached image {0} does not exist, please report this on the forums!", name);
            }
        }
        return IMAGE_MAP.get(name);
    }
}