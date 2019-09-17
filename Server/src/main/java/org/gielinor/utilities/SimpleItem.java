package org.gielinor.utilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compresses images.
 */
public class SimpleItem {

    private static final Logger log = LoggerFactory.getLogger(SimpleItem.class);

    public static void main(String args[]) {
        List<Item> itemList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        final int[] compressed = { 0 };
        for (ItemDefinition itemDefinition : ItemDefinition.getDefinitions().values()) {
            itemList.add(new Item(itemDefinition.getName(), itemDefinition.getId()));
        }
        File cacheFile = new File("items.dat");
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
        int totalLength = 402000;
        try (RandomAccessFile fileHandle = new RandomAccessFile(cacheFile, "rw")) {
            final MappedByteBuffer mbb = fileHandle.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, totalLength);
            mbb.putShort((short) 0);
            itemList.forEach(e -> {
                compressed[0]++;
                mbb.put(e.getName().getBytes());
                mbb.put((byte) 10);
                mbb.putInt(e.getId());
                names.add(e.getName());
            });
            mbb.putShort(0, (short) compressed[0]);
        } catch (IOException ex) {
            log.error("Failed to compress: [{}].", cacheFile, ex);
        }
        log.info("{} items compressed.", compressed[0]);
    }

    static class Item {

        private final String name;
        private final int id;

        public Item(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

    }

}
