package com.runescape.net;

import com.runescape.cache.bzip.BZip2Decompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class CacheArchive {

    /**
     * The buffer containing the decompressed data in this Archive.
     */
    public final byte[] buffer;
    /**
     * The amount of entries in this Archive.
     */
    private final int entries;
    /**
     * The identifiers (i.e. hashed names) of each of the entries in this Archive.
     */
    private final int[] identifiers;
    /**
     * The raw (i.e. decompressed) sizes of each of the entries in this Archive.
     */
    private final int[] extractedSizes;
    /**
     * The compressed sizes of each of the entries in this Archive.
     */
    private final int[] sizes;
    /**
     * The indices.
     */
    private final int[] indices;
    /**
     * Whether or not this Archive was compressed as a whole: if false, decompression will be performed on each of the
     * individual entries.
     */
    private final boolean extracted;

    public CacheArchive(byte[] data) {
        RSStream buffer = new RSStream(data);
        int length = buffer.getTri();
        int decompressedLength = buffer.getTri();
        if (decompressedLength != length) {
            byte output[] = new byte[length];
            BZip2Decompressor.decompress(output, length, data, decompressedLength, 6);
            this.buffer = output;
            buffer = new RSStream(this.buffer);
            extracted = true;
        } else {
            this.buffer = data;
            extracted = false;
        }
        entries = buffer.getShort();
        identifiers = new int[entries];
        extractedSizes = new int[entries];
        sizes = new int[entries];
        indices = new int[entries];
        int offset = buffer.currentPosition + entries * 10;
        for (int file = 0; file < entries; file++) {
            identifiers[file] = buffer.getInt();
            extractedSizes[file] = buffer.getTri();
            sizes[file] = buffer.getTri();
            indices[file] = offset;
            offset += sizes[file];
        }
    }

    public byte[] getEntry(String name) {
        byte output[] = null;
        int hash = 0;
        name = name.toUpperCase();
        for (int index = 0; index < name.length(); index++) {
            hash = (hash * 61 + name.charAt(index)) - 32;
        }

        for (int file = 0; file < entries; file++) {
            if (identifiers[file] == hash) {
                if (output == null) {
                    output = new byte[extractedSizes[file]];
                }
                if (!extracted) {
                    BZip2Decompressor.decompress(output, extractedSizes[file], this.buffer,
                            sizes[file], indices[file]);
                } else {
                    System.arraycopy(this.buffer, indices[file], output,
                            0, extractedSizes[file]);
                }
                return output;
            }
        }
        return null;
    }

    public byte[] getEntry2(String name) {
        byte output[] = null;
        int hash = 0;
        name = name.toUpperCase();
        for (int index = 0; index < name.length(); index++) {
            hash = (hash * 61 + name.charAt(index)) - 32;
        }

        for (int file = 0; file < entries; file++) {
            if (identifiers[file] == hash) {
                if (output == null) {
                    output = new byte[extractedSizes[file]];
                }
                if (!extracted) {
                    BZip2Decompressor.decompress(output, extractedSizes[file], this.buffer, sizes[file], indices[file]);
                } else {
                    System.arraycopy(this.buffer, indices[file], output, 0, extractedSizes[file]);
                }
                return output;
            }
        }
        return null;
    }

    @SuppressWarnings("resource")
    public byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
