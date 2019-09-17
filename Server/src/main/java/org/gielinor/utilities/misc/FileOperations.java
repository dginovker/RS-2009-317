package org.gielinor.utilities.misc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
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
import java.util.zip.GZIPInputStream;

/**
 * A utility tool for file operations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class FileOperations {

    /**
     * The default {@link org.gielinor.utilities.misc.FileOperations} constructor.
     */
    public FileOperations() {
    }

    /**
     * Reads a file.
     *
     * @param file The file.
     * @return The file's contents.
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            StringBuilder stringBuilder = new StringBuilder();
            String lineIn = bufferedReader.readLine();

            while (lineIn != null) {
                stringBuilder.append(lineIn);
                stringBuilder.append("\n");
                lineIn = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();
            return stringBuilder.toString();
        }
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
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(fileInputStream));
            datainputstream.readFully(fileBytes, 0, length);
            datainputstream.close();
            fileInputStream.close();
            return fileBytes;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
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
        } catch (IOException ignored) {
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Appends data to a file.
     *
     * @param fileName The name of the file.
     * @param data     The data to write.
     */
    public static void appendFile(String fileName, String data) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fw);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String s, byte abyte0[], boolean gzip) {
        try {
            (new File((new File(s)).getParent())).mkdirs();
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            fileoutputstream.write(abyte0, 0, abyte0.length);
            fileoutputstream.close();
        } catch (Throwable throwable) {
            System.out.println((new StringBuilder()).append("Write Error: ").append(s).toString());
        }
    }

    public static void writeFile(String s, byte abyte0[]) {
        writeFile(s, abyte0, false);
    }


    public static String[] readLines(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * Gzips files in a directory.
     *
     * @param file The file to gzip.
     */
    public static void gzip(File file) {
        try {
            Runtime.getRuntime().exec(new String[]{ "data/essentials/gzip.exe", "gzip --rsyncable " + file.getCanonicalPath() });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gzips all files in a directory by extension.
     *
     * @param directory The directory.
     * @param extension The extension.
     */
    public static void gzipAll(String directory, String extension) {
        Path path = Paths.get(directory);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + extension);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path entry, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() || matcher.matches(entry)) {
                        gzip(entry.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String file) {
        File f = new File(file);
        return f.exists();
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

    public static byte[] readFile(String name) {
        if (!fileExists(name)) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(name, "r");
            ByteBuffer byteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());
            try {
                if (byteBuffer.hasArray()) {
                    return byteBuffer.array();
                } else {
                    byte[] array = new byte[byteBuffer.remaining()];
                    byteBuffer.get(array);
                    return array;
                }
            } finally {
                randomAccessFile.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getGZipBuffer(File f) throws Exception {
        if (!f.exists()) {
            return null;
        }
        byte[] buffer = new byte[(int) f.length()];
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(f));
        dataInputStream.readFully(buffer);
        dataInputStream.close();
        byte[] gzipInputBuffer = new byte[999999];
        int bufferLength = 0;
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
        do {
            if (bufferLength == gzipInputBuffer.length) {
                break;
            }
            int readByte = gzip.read(gzipInputBuffer, bufferLength, gzipInputBuffer.length - bufferLength);
            if (readByte == -1) {
                break;
            }
            bufferLength += readByte;
        } while (true);
        byte[] inflated = new byte[bufferLength];
        System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferLength);
        buffer = inflated;
        if (buffer.length < 10) {
            return null;
        }
        return buffer;
    }

}
