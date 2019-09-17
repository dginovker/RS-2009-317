package com.runescape.cache.update;

import com.runescape.Constants;
import com.runescape.net.AutoCloseableURLConnection;
import com.runescape.net.protocol.ProtocolConstants;
import com.runescape.util.FileUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Updates required files for the client's cache.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public final class CacheUpdate {

    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(CacheUpdate.class.getName());

    /**
     * Creates the <code>CacheUpdate</code> instance for updating the cache.
     */
    public CacheUpdate() {
        /**
         * empty.
         */
    }

    /**
     * Checks whether or not an update is available for the cache.
     *
     * @return <code>True</code> if so.
     */
    private boolean isUpdateAvailable() {
        File file = new File(Constants.getCachePath(false) + File.separator + "cache.jar");
        if (!file.exists()) {
            return true;
        }
        try (AutoCloseableURLConnection urlConnection = new AutoCloseableURLConnection(new URL("https://Gielinor.org/client/rev" + ProtocolConstants.PROTOCOL_REVISION + "/cache.jar"))) {
            urlConnection.get().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
            long time = urlConnection.get().getLastModified();
            return time > file.lastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Starts the update of the cache files, providing there is an update.
     */
    public void startDownload() {
        logger.log(Level.INFO, "Checking for cache update...");
        if (!isUpdateAvailable()) {
            logger.log(Level.INFO, "Cache currently up-to-date.");
            return;
        }
        logger.log(Level.INFO, "Cache update available.");
        new Thread() {
            public void run() {
                try (AutoCloseableURLConnection autoCloseableURLConnection = new AutoCloseableURLConnection(new URL("https://Gielinor.org/client/rev" + ProtocolConstants.PROTOCOL_REVISION + "/cache.jar"))) {
                    autoCloseableURLConnection.get().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
                    File file = new File(Constants.getCachePath(false) + "/cache.jar");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    }
                    try (ReadableByteChannel readableByteChannel = Channels.newChannel(autoCloseableURLConnection.getInputStream())) {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(Constants.getCachePath(false) + "/cache.jar")) {
                            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
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
                logger.log(Level.INFO, "Cache downloaded from server.");
                logger.log(Level.INFO, "Extracting cache...");
                FileUtility.unzip(Constants.getCachePath(false), "cache.jar");
                logger.log(Level.INFO, "Extracted cache.");
            }
        }.start();
    }
}
