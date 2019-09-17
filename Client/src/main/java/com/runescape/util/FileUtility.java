package com.runescape.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A utility tool for file operations.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public class FileUtility {

    /**
     * The default {@link FileUtility} constructor.
     */
    public FileUtility() {

    }

    /**
     * Gets bytes from a file.
     *
     * @param file The file.
     * @return The bytes of the file contents.
     */
    public static byte[] getBytes(File file) {
        try {
            int length = (int) file.length();
            byte[] fileBytes = new byte[length];
            DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getAbsoluteFile())));
            datainputstream.readFully(fileBytes, 0, length);
            datainputstream.close();
            return fileBytes;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Gets bytes from a file.
     *
     * @param fileName The name of the file.
     * @return The bytes of the file contents.
     */
    public static byte[] getBytes(String fileName) {
        return getBytes(new File(fileName));
    }

    /**
     * Reads a file.
     *
     * @param file The file.
     * @return The file's contents.
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    /**
     * Reads a file.
     *
     * @param file The file.
     * @return The file's contents.
     * @throws IOException
     */
    public static String readFile(String file) throws IOException {
        return readFile(file, false);
    }

    /**
     * Reads a file.
     *
     * @param file    The file.
     * @param newLine Whether or not to append a new line.
     * @return The file's contents.
     * @throws IOException
     */
    public static String readFile(String file, boolean newLine) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                if (newLine) {
                    stringBuilder.append("\r\n");
                }
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        }
    }

    /**
     * Writes data to a file.
     *
     * @param fileName The name of the file.
     * @param data     The data to write.
     */
    public static void writeFile(String fileName, String data) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            writer.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(File f, byte[] data) {
        try {
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            try {
                raf.write(data);
            } finally {
                raf.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends data to a file.
     *
     * @param fileName The name of the file.
     * @param data     The data to append.
     */
    public static void appendToFile(String fileName, String data) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            writer.append(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getGZipBuffer(File f) throws Exception {
        if (!f.exists()) {
            return null;
        }
        byte[] buffer = new byte[(int) f.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(f));
        dis.readFully(buffer);
        dis.close();
        byte[] gzipInputBuffer = new byte[999999];
        int bufferlength = 0;
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
        do {
            if (bufferlength == gzipInputBuffer.length) {
                break;
            }
            int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
            if (readByte == -1) {
                break;
            }
            bufferlength += readByte;
        } while (true);
        byte[] inflated = new byte[bufferlength];
        System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
        buffer = inflated;
        if (buffer.length < 10) {
            return null;
        }
        return buffer;
    }

    public static byte[] gZipDecompress(byte[] b) throws IOException {
        GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(b));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = gzi.read(buf, 0, buf.length)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            out.close();
        }
        return out.toByteArray();
    }

    public static boolean fileExists(String file) {
        File f = new File(file);
        return f.exists();
    }

    public static void unzip(String location, String fileName) {
        try {
            InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(location + File.separator + fileName));
            ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    (new File(location + zipEntry.getName())).mkdir();
                } else {
                    if (zipEntry.getName().equals(location + File.separator + fileName)) {
                        unzip(zipInputStream, location + File.separator + fileName);
                        break;
                    }
                    unzip(zipInputStream, location + File.separator + zipEntry.getName());
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unzip(ZipInputStream zin, String s) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(s);
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = zin.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, length);
        }
        fileOutputStream.close();
    }

}