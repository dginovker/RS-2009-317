package org.gielinor.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Backs up the cache daily.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CacheBackup implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CacheBackup.class);

    private final File CACHE_DIRECTORY = new File("");
    private final File WIP_CACHE_DIRECTORY = new File("");
    private final String BACKUP_DIRECTORY = "";
    private boolean running = false;
    private Thread thread;

    public static void main(String... args) {
        new CacheBackup().start();
    }

    public void backupCache(boolean wip) throws IOException {
        File directory = wip ? WIP_CACHE_DIRECTORY : CACHE_DIRECTORY;
        String backup = BACKUP_DIRECTORY + File.separator + (wip ? "WIP" : "LIVE");
        List<File> fileList = new ArrayList<>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String time = calendar.get(Calendar.HOUR) + " " + calendar.get(Calendar.MINUTE) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
        String fileName = monthName + " " + day + ", " + year + " " + time;
        log.info("Gather cache files to back up from: [{}].", directory.getCanonicalPath());
        getAllFiles(directory, fileList);
        log.info("Gathered files. Creating ZIP archive: [{}].", fileName);
        writeZipFile(backup, directory, fileName, fileList);
        log.info("Finished back up for: [{}].", (backup + File.separator + fileName));
        if (!wip) {
            backupCache(true);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                backupCache(false);
                Thread.sleep(TimeUnit.HOURS.toMillis(2));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getAllFiles(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getAbsolutePath().contains("gamepack.jar")) {
                continue;
            }
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }
    }

    public void writeZipFile(String backup, File zipFileName, String fileName, List<File> fileList) {
        try (FileOutputStream fos = new FileOutputStream(backup + File.separator + fileName + ".zip");
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            fileList.stream().filter(file -> !file.isDirectory()).forEach(file -> {
                try {
                    addToZip(zipFileName, file, zos);
                } catch (IOException ex) {
                    log.error("Failed to write ZIP archive: [{}].", zipFileName, ex);
                }
            });
        } catch (IOException ex) {
            log.error("Error while writing ZIP archives: [{}].", fileName, ex);
        }
    }

    public void addToZip(File zipFileName, File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String zipFilePath = file.getCanonicalPath().substring(zipFileName.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fileInputStream.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
        }
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("The cache backup task is already running.");
        }
        this.running = true;
        thread = new Thread(this);
        thread.start();
        log.info("Started cache backup.");
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("The cache backup task is already stopped.");
        }
        running = false;
        thread.interrupt();
    }

    public boolean isRunning() {
        return running;
    }
}
