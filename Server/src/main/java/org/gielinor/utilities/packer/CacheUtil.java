package org.gielinor.utilities.packer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.swing.JProgressBar;

/**
 * Handles cache packing and dumping operations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CacheUtil {

    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);

    /**
     * The archive names.
     */
    private static final String[] ARCHIVE_NAMES = new String[]{ "model", "animation", "sound", "map", "model1", "texture", "texture" };
    /**
     * If we should repack the model cache.
     */
    public static boolean REPACK_MODELS = true;
    /**
     * If we should repack the animation cache.
     */
    public static boolean REPACK_ANIMATIONS = false;
    /**
     * If we should repack the sounds cache.
     */
    public static boolean REPACK_SOUNDS = false;
    /**
     * If we should repack the maps cache.
     */
    public static boolean REPACK_MAPS = false;
    /**
     * If we should unpack the texture cache.
     */
    public static boolean REPACK_TEXTURES = false;
    /**
     * If we should unpack the model cache.
     */
    public static boolean UNPACK_MODELS = false;
    /**
     * If we should unpack the animation cache.
     */
    public static boolean UNPACK_ANIMATIONS = false;
    /**
     * If we should unpack the sounds cache.
     */
    public static boolean UNPACK_SOUNDS = false;
    /**
     * If we should unpack the maps cache.
     */
    public static boolean UNPACK_MAPS = false;
    /**
     * If we should unpack the texture cache.
     */
    public static boolean UNPACK_TEXTURES = false;

    /**
     * Repacks the cache indexes.
     */
    public static void repackCacheIndex(Index index, String location) {
        JProgressBar jProgressBar = new JProgressBar(0, 100);
        log.info("Preparing to repack cache indices...");
        for (int cacheIndex = 1; cacheIndex <= 7; cacheIndex++) {
            if (cacheIndex == 1 && !REPACK_MODELS) {
                continue;
            }
            if (cacheIndex == 2 && !REPACK_ANIMATIONS) {
                continue;
            }
            if (cacheIndex == 3 && !REPACK_SOUNDS) {
                continue;
            }
            if (cacheIndex == 4 && !REPACK_MAPS) {
                continue;
            }
            if (cacheIndex == 5 && !REPACK_MODELS) {
                continue;
            }
            if (cacheIndex == 6 && !REPACK_MODELS) {
                continue;
            }
            if (cacheIndex == 7 && !REPACK_TEXTURES) {
                continue;
            }
            int packedCount = 0;
            List<DataModel> dataModels = new ArrayList<>();
            log.info("Repacking {} cache index...", ARCHIVE_NAMES[cacheIndex - 1]);
            Path path = Paths.get(location);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.{gz}")) {
                for (Path entry : directoryStream) {
                    int id = Integer.parseInt(entry.getFileName().toString().replaceFirst("[.][^.]+$", ""));
                    try (RandomAccessFile randomAccessFile = new RandomAccessFile(entry.toFile(), "r")) {
                        byte[] modelData = new byte[(int) randomAccessFile.length()];
                        randomAccessFile.readFully(modelData);
                        dataModels.add(new DataModel(id, (int) randomAccessFile.length(), modelData));
                    }
                }
            } catch (IOException ex) {
                log.info("Failed to write cache index: {}.", ARCHIVE_NAMES[cacheIndex - 1], ex);
            }
            for (DataModel dataModel : dataModels) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(dataModel.getModelData());
                if (byteBuffer.array().length == 0) {
                    log.warn("Unable to pack {}: {}", ARCHIVE_NAMES[cacheIndex - 1], dataModel.getId());
                    continue;
                }
                index.put(dataModel.getLength(), byteBuffer.array(), dataModel.getId());
                packedCount++;
            }
            jProgressBar.setVisible(true);
            log.info("Finished repacking {0} models into the {} cache index.", packedCount, ARCHIVE_NAMES[cacheIndex - 1]);
        }
    }

    public static void repack2(Index index, String location) {
        int packedCount = 0;
        List<DataModel> dataModels = new ArrayList<>();
        log.info("Repacking model cache index...");
        Path path = Paths.get(location);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.{gz}")) {
            for (Path entry : directoryStream) {
                int id = Integer.parseInt(entry.getFileName().toString().replaceFirst("[.][^.]+$", ""));
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(entry.toFile(), "r")) {
                    byte[] modelData = new byte[(int) randomAccessFile.length()];
                    randomAccessFile.readFully(modelData);
                    dataModels.add(new DataModel(id, (int) randomAccessFile.length(), modelData));
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        for (DataModel dataModel : dataModels) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(dataModel.getModelData());
            if (byteBuffer.array().length == 0) {
                log.warn("Unable to pack model [{}].", dataModel.getId());
                continue;
            }
            log.info("Repacked {} into the model cache index.", dataModel.getId());
            index.put(dataModel.getLength(), byteBuffer.array(), dataModel.getId());
            packedCount++;
        }
        log.info("Finished repacking {} models into the model cache index.", packedCount);
    }


    /**
     * Unpacks the cache indexes.
     */
    public static void unpackCacheIndex(Index index, String location) {
        log.info("Preparing to unpack cache indices...");
        for (int cacheIndex = 1; cacheIndex <= 7; cacheIndex++) {
            if (cacheIndex == 1 && !UNPACK_MODELS) {
                continue;
            }
            if (cacheIndex == 2 && !UNPACK_ANIMATIONS) {
                continue;
            }
            if (cacheIndex == 3 && !UNPACK_SOUNDS) {
                continue;
            }
            if (cacheIndex == 4 && !UNPACK_MAPS) {
                continue;
            }
            if (cacheIndex != 6) {
                continue;
            }
            int unpackedCount = 0;
            log.info("Unpacking {} cache index...", ARCHIVE_NAMES[cacheIndex - 1]);
            try {
                for (int fileId = 0; ; fileId++) {
                    try {
                        byte[] indexByteArray = index.decompress(fileId);
                        if (indexByteArray == null) {
                            if (fileId >= 65535) {
                                break;
                            }
                            continue;
                        }
                        if (fileId >= 65535) {
                            break;
                        }
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                            new GZIPOutputStream(new FileOutputStream(location + File.separator + File.separator + fileId + ".gz")));
                        if (indexByteArray.length == 0) {
                            continue;
                        }
                        bufferedOutputStream.write(indexByteArray);
                        bufferedOutputStream.close();
                        unpackedCount++;
                    } catch (IOException ex) {
                        log.error("Failed to write the {} cache index.", ARCHIVE_NAMES[cacheIndex - 1], ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception while writing cache index.", ex);
            }
            log.info("Finished packing {} into the {} cache index.", unpackedCount, ARCHIVE_NAMES[cacheIndex - 1]);
        }
    }

    /**
     * Represents a model to pack.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    static class DataModel {

        /**
         * The id of the model.
         */
        private final int id;
        /**
         * The length of the model data.
         */
        private final int length;
        /**
         * The model data.
         */
        private final byte[] modelData;

        /**
         * Constructs a new <code>DataModel</code>.
         *
         * @param id        The id of the model.
         * @param length    The length of the model data.
         * @param modelData The model data.
         */
        public DataModel(int id, int length, byte[] modelData) {
            this.id = id;
            this.length = length;
            this.modelData = modelData;
        }

        /**
         * Gets the id of the model.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the length of the model data.
         *
         * @return The length.
         */
        public int getLength() {
            return length;
        }

        /**
         * Gets the model data.
         *
         * @return The data.
         */
        public byte[] getModelData() {
            return modelData;
        }
    }

}
