package org.gielinor.client.launcher;

import org.gielinor.client.launcher.gui.Frame;
import org.gielinor.client.launcher.guice.LauncherModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.gielinor.client.launcher.util.ActionLogger;
import org.gielinor.client.launcher.util.ErrorLogger;
import org.gielinor.client.launcher.util.Misc;
import org.gielinor.client.launcher.util.StyleLoader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Mike on 2/13/2015.
 * Modified by Corey
 */
public class Launcher {

    public static boolean developerMode = false;

    private Frame frame;

    public Frame getFrame() {
        return frame;
    }

    private static Launcher singleton;

    public static Launcher getLauncher() {
        if (singleton == null) {
            singleton = new Launcher();
        }
        return singleton;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("true")) {
                developerMode = true;
            }
        }
        getLauncher().init();
    }

    private void init() {
        ActionLogger.initialiseActionLogger();
        ErrorLogger.initialiseErrorLogger();

        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        System.out.println(System.lineSeparator() + "Starting new Launcher session");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", Color.decode("#1C1C1C"));
            UIManager.put("Panel.background", Color.decode("#1C1C1C"));
            UIManager.put("Button.background", Color.red);
            UIManager.put("Button.foreground", Color.decode("#161616"));
            UIManager.put("OptionPane.undecorated", true);

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }};

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        try {
            new StyleLoader().loadFiles();
        } catch (Exception e) {
            System.err.println("Failed to load style files!");
        }

        final Injector injector = Guice.createInjector(new LauncherModule());
        frame = injector.getInstance(Frame.class);
        frame.setUndecorated(true);
        frame.requestFocus();
        frame.build();
        frame.showGui();
    }

}