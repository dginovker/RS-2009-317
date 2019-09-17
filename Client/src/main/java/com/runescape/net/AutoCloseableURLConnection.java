package com.runescape.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mike on 2/20/2015.
 * <p/>
 * This class exists and is used because URLConnection, by default, does not support the new 'AutoClosable' feature.
 * This class allows us to use try with resources and not have to worry about clean-up.
 */
public class AutoCloseableURLConnection implements AutoCloseable {

    private final URLConnection urlConnection;

    public AutoCloseableURLConnection(URL url) throws IOException {
        urlConnection = url.openConnection();
    }

    public InputStream getInputStream() throws IOException {
        return urlConnection.getInputStream();
    }

    @Override
    public void close() throws IOException {
        getInputStream().close();
    }

    public URLConnection get() {
        return urlConnection;
    }
}
