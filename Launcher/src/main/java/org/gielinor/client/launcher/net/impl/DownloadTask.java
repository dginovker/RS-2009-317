package org.gielinor.client.launcher.net.impl;

import org.gielinor.client.launcher.gui.Frame;
import org.gielinor.client.launcher.net.UpdateManager;
import org.gielinor.client.launcher.util.Misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Execute file download in a background thread and update the progress.
 *
 * @author www.codejava.net
 * @author Corey
 */
public class DownloadTask extends SwingWorker<Void, Void> {

    private static final int BUFFER_SIZE = 4096;
    private String downloadURL;
    private String saveDirectory;
    private Frame gui;
    private DownloadCategory category;
    private UpdateManager updateManager;

    private int lastSecond = 0;
    private String lastTimeRemaining = "";

    public void setDownloadCategory(DownloadCategory category) {
        this.category = category;
    }

    public void setUpdateManager(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    public DownloadCategory getDownloadCategory() {
        return category;
    }

    public DownloadTask(Frame gui, String downloadURL, String saveDirectory) {
        this.gui = gui;
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
    }

    /**
     * Executed in background thread
     */
    @Override
    protected Void doInBackground() throws Exception {
        try {
            HTTPDownloadUtil util = new HTTPDownloadUtil();
            util.downloadFile(downloadURL);

            InputStream inputStream = util.getInputStream();

            File saveDir = new File(saveDirectory);

            if (!saveDir.getParentFile().exists()) {
                System.out.println("doesn't exist, creating " + saveDir.getParentFile());
                saveDir.getParentFile().mkdirs();
            }

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveDir);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            long totalBytesRead = 0;
            int percentCompleted = 0;
            long fileSize = util.getContentLength();

            long start = System.nanoTime();
            final double NANOS_PER_SECOND = 1000000000.0;
            final double BYTES_PER_MIB = 1024 * 1024;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                percentCompleted = (int) (totalBytesRead * 100 / fileSize);

                int speed = (int) (NANOS_PER_SECOND / BYTES_PER_MIB * totalBytesRead / (System.nanoTime() - start + 1) * 1024);

                String downloadProgress = String.format("%.2f/%.2f MB Downloaded (%d KB/s)", ((double)(totalBytesRead / 1024) / 1024), ((double)(fileSize / 1024) / 1024), speed);
                gui.getDownloadProgress().setText(downloadProgress);

                String downloadPercentage = percentCompleted + "% Downloaded / Complete in " + getTimeRemaining(fileSize, totalBytesRead, speed);
                gui.getDownloadPercentage().setText(downloadPercentage);

                setProgress(percentCompleted);
                gui.getProgressBar().repaint();
            }

            outputStream.close();
            util.disconnect();

            gui.getDownloadPercentage().setText("");
            gui.getDownloadProgress().setText("");

            setProgress(100);
            gui.getProgressBar().repaint();

            if (category == DownloadCategory.CACHE) {
                gui.getStatus().setText("Unzipping cache...");
                updateManager.unzip();
                gui.getStatus().setText("Successfully extracted the cache");
                updateManager.startDownload();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui, "<html><font color=#FFFFFF>Error downloading file: " + ex.getMessage() + "</font>",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            setProgress(0);
            cancel(true);
        }
        return null;
    }

    /**
     * Executed in Swing's event dispatching thread
     */
    @Override
    protected void done() {
        if (!isCancelled()) {
            /*JOptionPane.showMessageDialog(gui,
                    "File has been downloaded successfully!", "Message",
                    JOptionPane.INFORMATION_MESSAGE);*/
            System.out.println(category.getDownloadComplete());
            setProgress(100);
            gui.getProgressBar().repaint();
            gui.getStatus().setText(category.getDownloadComplete());
            gui.getPlay().setEnabled(true);
            gui.getDebug().setEnabled(true);
        }
    }

    private String getTimeRemaining(long fileSize, long totalBytesRead, int speed) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getSecond() == lastSecond) {
            return lastTimeRemaining;
        }
        long estimatedSecondsRemaining = ((fileSize - totalBytesRead) / 1024) / speed;
        String timeRemaining = Misc.getFriendlyTimeRemaining(estimatedSecondsRemaining);

        lastSecond = now.getSecond();
        lastTimeRemaining = timeRemaining;

        return timeRemaining;
    }

}