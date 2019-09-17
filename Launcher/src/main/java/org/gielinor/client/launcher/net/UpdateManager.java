package org.gielinor.client.launcher.net;

import javax.swing.*;

import org.gielinor.client.launcher.Launcher;
import org.gielinor.client.launcher.configuration.Configuration;
import org.gielinor.client.launcher.gui.Frame;
import org.gielinor.client.launcher.net.impl.AutoCloseableURLConnection;
import org.gielinor.client.launcher.net.impl.DownloadCategory;
import org.gielinor.client.launcher.net.impl.DownloadTask;
import org.gielinor.client.launcher.util.Misc;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by Mike on 13/02/2015.
 * Modified by Corey on 07/08/2017
 */
public class UpdateManager {

    private URL loaderURL;
    private URL url;
    private URL cacheURL;
    private final Configuration configuration;
    private final Frame frame;

    public UpdateManager(Configuration configuration, Frame frame) {
        this.configuration = configuration;
        this.frame = frame;
        try {
            this.loaderURL = new URL(configuration.getMapValueByKey("urls", "loader_jar"));
            this.url = new URL(configuration.getMapValueByKey("urls", "jar"));
            this.cacheURL = new URL(configuration.getMapValueByKey("urls", "cache"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to load URLs!");
        }

    }

    private boolean isCacheUpdateAvailable() {
        System.out.println("Checking for cache update...");
        frame.getStatus().setText("Checking for cache update...");
        //File file = new File(configuration.getMapValueByKey("paths", "cache_zip"));
        File mainFileCache = new File(configuration.getMapValueByKey("paths", "main_cache"));
        if (!mainFileCache.exists()) {
            return true;
        }
        if (mainFileCache.length() == 0) {
            return true;
        }
        return fileUpdateAvailable(cacheURL, mainFileCache);
    }

    private boolean isClientUpdateAvailable() {
        System.out.println("Checking for client update...");
        frame.getStatus().setText("Checking for client update...");
        File file = new File(configuration.getMapValueByKey("paths", "cache_zip"));
        if (!file.exists()) {
            return true;
        }
        return fileUpdateAvailable(url, file);
    }

    private boolean fileUpdateAvailable(URL fileUrl, File file) {
        try (AutoCloseableURLConnection urlConnection = new AutoCloseableURLConnection(fileUrl)) {
            urlConnection.get().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
            long time = urlConnection.get().getLastModified();
            return time > file.lastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startCacheDownload() {
        if (!isCacheUpdateAvailable()) {
            System.out.println("Cache currently up-to-date.");
            frame.getStatus().setText("Cache currently up-to-date.");
            frame.getPlay().setEnabled(true);
            frame.getDebug().setEnabled(true);
            startDownload();
            return;
        }
        System.out.println("Cache update available.");
        frame.getPlay().setEnabled(false);
        frame.getDebug().setEnabled(false);
        frame.getStatus().setText("Your cache is being updated.");

        frame.startDownload(cacheURL.toString(), configuration.getMapValueByKey("paths", "cache_zip"), DownloadCategory.CACHE);
    }

    public void unzip() {
        try {
            System.out.println("Extracting cache...");
            InputStream in = new BufferedInputStream(new FileInputStream(configuration.getMapValueByKey("paths", "cache_zip")));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                if (e.isDirectory()) {
                    (new File(configuration.getMapValueByKey("paths", "cache") + e.getName())).mkdir();
                } else {
                    if (e.getName().equals(configuration.getMapValueByKey("paths", "cache_zip"))) {
                        unzip(zin, configuration.getMapValueByKey("paths", "cache_zip"));
                        break;
                    }
                    unzip(zin, configuration.getMapValueByKey("paths", "cache") + e.getName());
                }
            }
            zin.close();
            File file = new File(configuration.getMapValueByKey("paths", "cache_zip"));
            file.delete(); // Delete the old .zip file we no longer need
            System.out.println("Extracted cache.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unzip(ZipInputStream zin, String s)
            throws IOException {
        FileOutputStream out = new FileOutputStream(s);
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = zin.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
    }

    public void startDownload() {
        if (!isClientUpdateAvailable()) {
            System.out.println("Client currently up-to-date.");
            frame.getPlay().setEnabled(true);
            frame.getDebug().setEnabled(true);
            frame.getStatus().setText("Your client is currently up-to-date.");
            frame.getProgressBar().setValue(100);
            System.out.println("Ready.");
            return;
        }
        System.out.println("Client update available.");
        frame.getPlay().setEnabled(false);
        frame.getDebug().setEnabled(false);
        frame.getStatus().setText("Your client is being updated.");

       frame.startDownload(url.toString(), configuration.getMapValueByKey("paths", "client_jar"), DownloadCategory.CLIENT);
    }

    private boolean isUpdateAvailable() throws URISyntaxException {
        System.out.println("Checking for loader update...");
        File jarFile = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        if (!jarFile.exists() || !jarFile.toString().endsWith(".jar")) {
            JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Could not find the loader jar file!</font></html>\n<html><font color=#FFFFFF>Please download the loader update manually.</font>", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
            return false;
        }
        return fileUpdateAvailable(loaderURL, jarFile);
    }

    public void startLoaderDownload() throws URISyntaxException {
        if (isUpdateAvailable()) {
            int option = JOptionPane.showConfirmDialog(null, "<html><font color=#FFFFFF>An update is available.</font></html>\n<html><font color=#FFFFFF>Do you wish to download it?</font>", "Update Available", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                final File jarFile = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                if (!jarFile.exists() || !jarFile.toString().endsWith(".jar")) {
                    JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Could not find the loader jar file!</font></html>\n<html><font color=#FFFFFF>Please download the loader update manually.</font>", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                    return;
                }
                new Thread() {
                    public void run() {
                        try (AutoCloseableURLConnection urlConnection = new AutoCloseableURLConnection(loaderURL)) {
                            urlConnection.get().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
                            try (ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream())) {
                                try (FileOutputStream fos = new FileOutputStream(jarFile)) {
                                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                                } catch (IOException e) {
                                    //TODO: Exception handling & Logging
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                //TODO: Exception handling & Logging
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            //TODO: Exception handling & Logging
                            e.printStackTrace();
                        }
                        try {
                            Runtime.getRuntime().exec("java -jar " + jarFile);
                            System.exit(-1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Please restart the loader.</font>");
                        }
                    }
                }.start();
            }
        }
    }
}