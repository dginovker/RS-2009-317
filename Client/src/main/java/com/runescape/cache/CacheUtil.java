package com.runescape.cache;

import com.runescape.Constants;
import com.runescape.Game;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * Handles cache packing and dumping operations.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class CacheUtil {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(CacheUtil.class.getName());
    /**
     * The archive names.
     */
    private static final String[] ARCHIVE_NAMES = new String[]{"model", "animation", "sound", "map"};
    /**
     * If we should repack the model cache.
     */
    public static boolean REPACK_MODELS = false;
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
     * If we should pack the media index.
     */
    public static boolean PACK_MEDIA = true;

    /**
     * Repacks the cache indexes.
     */
    public static void repackCacheIndexes() {
        JProgressBar jProgressBar = new JProgressBar(0, 100);
        logger.log(Level.INFO, "Preparing to repack cache indexes...");
        for (int cacheIndex = 1; cacheIndex <= 4; cacheIndex++) {
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
            int packedCount = 0;
            List<DataModel> dataModels = new ArrayList<>();
            logger.log(Level.INFO, "Repacking {0} cache index...", ARCHIVE_NAMES[cacheIndex - 1]);
            Path path = Paths.get(Constants.getCachePath(true) + File.separator + "pack" + cacheIndex);
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
            int totalPack = dataModels.size();
            for (DataModel dataModel : dataModels) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(dataModel.getModelData());
                if (byteBuffer.array().length == 0) {
                    logger.log(Level.WARNING, "Unable to pack " + ARCHIVE_NAMES[cacheIndex - 1] + " #{0}!", dataModel.getId());
                    continue;
                }
                int percent = ((packedCount * 100) / totalPack);
                Game.INSTANCE.overrideLoading = true;
                jProgressBar.setValue(percent);
                jProgressBar.setStringPainted(true);
                Game.INSTANCE.indices[cacheIndex].put(dataModel.getLength(), byteBuffer.array(), dataModel.getId());
                packedCount++;
            }
            jProgressBar.setVisible(true);
            logger.log(Level.INFO, "Finished repacking {0} " + ARCHIVE_NAMES[cacheIndex - 1] + "s into the {1} cache index.", new Object[]{packedCount, ARCHIVE_NAMES[cacheIndex - 1]});
            Game.INSTANCE.overrideLoading = false;
        }
    }

    /**
     * Unpacks the cache indexes.
     */
    public static void unpackCacheIndexes() {
        logger.log(Level.INFO, "Preparing to unpack cache indexes...");
        for (int cacheIndex = 1; cacheIndex <= 4; cacheIndex++) {
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
            int unpackedCount = 0;
            logger.log(Level.INFO, "Unpacking {0} cache index...", ARCHIVE_NAMES[cacheIndex - 1]);
            try {
                for (int fileId = 0; ; fileId++) {
                    try {
                        byte[] indexByteArray = Game.INSTANCE.indices[cacheIndex].decompress(fileId);
                        if (indexByteArray == null) {
                            continue;
                        }
                        if (fileId >= 65535) {
                            break;
                        }
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(Constants.getCachePath(true)
                                + File.separator + "unpack" + cacheIndex + File.separator + fileId + ".gz")));
                        if (indexByteArray.length == 0) {
                            continue;
                        }
                        bufferedOutputStream.write(indexByteArray);
                        bufferedOutputStream.close();
                        unpackedCount++;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            logger.log(Level.INFO, "Finished unpacking {0} " + ARCHIVE_NAMES[cacheIndex - 1] + " into the {1} cache index.", new Object[]{unpackedCount, ARCHIVE_NAMES[cacheIndex - 1]});
        }
    }

    /**
     * Represents a model to pack.
     *
     * @author <a href="http://Gielinor.org">Gielinor</a>
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
