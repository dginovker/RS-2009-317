package org.gielinor.utilities.packer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DataPacker {

    public static final String cachePath = "cacheFA\\";

    public static void main(String[] args) throws Exception {
        packModels(false, true); // false = custom models 0+
        //packTextures();
        // packAnimations();
    }

    public static void packAnimations() {
        int idx = 6;
        String location = "data/osrsanims";
        File file = new File(cachePath + File.separator + "main_file_cache.idx" + idx);
        System.out.println(file.getPath());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Index index = new Index(new RandomAccessFile(cachePath + File.separator + "main_file_cache.dat", "rw"),
                new RandomAccessFile(cachePath + File.separator + "main_file_cache.idx" + idx, "rw"), idx + 1);
            CacheUtil.repackCacheIndex(index, location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void packTextures() {
        int idx = 5;
        String location = "data/textures";
        File file = new File(cachePath + File.separator + "main_file_cache.idx" + idx);
        System.out.println(file.getPath());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Index index = new Index(new RandomAccessFile(cachePath + File.separator + "main_file_cache.dat", "rw"),
                new RandomAccessFile(cachePath + File.separator + "main_file_cache.idx" + idx, "rw"), idx + 1);
            CacheUtil.repackCacheIndex(index, location);
            System.out.println(index.getFileCount());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void packModels(boolean is498, boolean osrs) {
        int idx = is498 ? 5 : 6;
        if (osrs) {
            idx = 7;
        }
        String location = osrs ? "data/osrs_models" : "data/custom_models";
        File file = new File(cachePath + File.separator + "main_file_cache.idx" + idx);
        System.out.println(file.getPath());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Index index = new Index(new RandomAccessFile(cachePath + File.separator + "main_file_cache.dat", "rw"),
                new RandomAccessFile(cachePath + File.separator + "main_file_cache.idx" + idx, "rw"), idx + 1);
            CacheUtil.repack2(index, location);
            System.out.println(index.getFileCount());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
