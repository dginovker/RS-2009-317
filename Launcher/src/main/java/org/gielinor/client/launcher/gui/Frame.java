package org.gielinor.client.launcher.gui;

import com.google.inject.Inject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import com.jhlabs.image.GaussianFilter;
import com.rometools.rome.io.FeedException;
import org.gielinor.client.launcher.Launcher;
import org.gielinor.client.launcher.configuration.Configuration;
import org.gielinor.client.launcher.feed.FeedReader;
import org.gielinor.client.launcher.feed.NewsItem;
import org.gielinor.client.launcher.net.UpdateManager;
import org.gielinor.client.launcher.net.impl.DownloadCategory;
import org.gielinor.client.launcher.net.impl.DownloadTask;
import org.gielinor.client.launcher.util.Misc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Created by Mike on 2/13/2015.
 * Heavily modified by Corey
 */
public class Frame extends JFrame implements ActionListener, PropertyChangeListener {

    private final Configuration configuration;
    private final UpdateManager updateManager;

    private JLabel background;
    private JLabel banner;
    private JLabel status;
    private JButton play;
    private JButton debug;

    private JButton home;
    private JButton community;
    private JButton highscores;
    private JButton store;
    private JButton vote;
    private JButton discord;

    private JButton minimize;
    private JButton close;
    private JLabel launcherVersion;

    private Point initialClick;

    private JLabel latestUpdates;
    private JScrollPane scroller;

    private JProgressBar progressBar;
    private JLabel downloadProgress;
    private JLabel downloadPercentage;

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getDownloadProgress() {
        return downloadProgress;
    }

    public JLabel getDownloadPercentage() {
        return downloadPercentage;
    }

    @Inject
    public Frame(Configuration configuration) {
        this.configuration = configuration;
        this.updateManager = new UpdateManager(configuration, this);
    }

    private void buildDebug() {
        Rectangle rec = this.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        paint(bufferedImage.getGraphics());

        BufferedImage dst = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage blurredImage = new GaussianFilter(6.5f).filter(bufferedImage, dst);

        try {
            Graphics2D g2 = blurredImage.createGraphics();
            g2.drawImage(ImageIO.read(getClass().getResource("/themes/default/debug_background.png")), 482, 228, null);
            g2.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getContentPane().removeAll();
        background = new JLabel(new ImageIcon(blurredImage));
        background.setLayout(null);
        add(background);

        JButton debugClose = new CustomButton(this, "debug_close", new Rectangle(776, 228, 25, 27));
        background.add(debugClose);

        JButton deleteCache = new CustomButton(this, "delete_cache", new Rectangle(510, 288, 260, 51));
        background.add(deleteCache);

        JButton batchDebug = new CustomButton(this, "batch_debug", new Rectangle(510, 353, 260, 51));
        background.add(batchDebug);

        JButton logLocation = new CustomButton(this, "log_location", new Rectangle(510, 421, 260, 51));
        background.add(logLocation);

        addMouseListener();
        pack();
    }

    /**
     * Rebuild the main panel with saved components
     */
    private void rebuildMain() {
        getContentPane().removeAll();

        background = new JLabel(new ImageIcon(getClass().getResource("/themes/default/background.png")));
        background.setLayout(null);
        add(background);

        background.add(banner);
        background.add(status);
        background.add(progressBar);
        background.add(debug);
        background.add(home);
        background.add(minimize);
        background.add(close);
        background.add(community);
        background.add(store);
        background.add(vote);
        background.add(highscores);
        background.add(discord);
        background.add(launcherVersion);
        background.add(downloadProgress);
        background.add(downloadPercentage);
        background.add(scroller);
        background.add(latestUpdates);
        background.add(play);

        addMouseListener();
        pack();
    }

    public void build() {
        getContentPane().removeAll();
        setTitle("Gielinor Launcher");
        setPreferredSize(new Dimension(1280, 720));
        setIconImage(new ImageIcon(getClass().getResource("/themes/default/icon.png")).getImage());

        background = new JLabel(new ImageIcon(getClass().getResource("/themes/default/background.png")));
        background.setLayout(null);
        background.setName("background");
        add(background);

        buildForumFeed();
        buildNavBar();
        buildUtilities();
        buildRunSection();

        addMouseListener();
        pack();
    }

    private void buildForumFeed() {
        latestUpdates = new JLabel();
        latestUpdates.setText("<html>" + Misc.getFontStyling() + "<h1>Latest News</h1></html>");
        latestUpdates.setBounds(9, 59, 284, 25);
        background.add(latestUpdates, BorderLayout.CENTER);

        Box box = new Box(BoxLayout.Y_AXIS);
        scroller = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // forumFeed, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scroller.setBounds(0, 91, 451, 556);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        scroller.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroller.getVerticalScrollBar().setUnitIncrement(10);

        try {
            ArrayList<NewsItem> newsItems = FeedReader.getFeed(configuration.getMapValueByKey("urls", "rss_feed"), configuration.getMapValueByKey("urls", "user_profile"));
            for (NewsItem newsItem : newsItems) {
                JPanel newsPanel = getNewsPanel(newsItem);
                box.add(newsPanel);
            }
        } catch (IOException | FeedException e) {
            box.add(feedErrorPanel());
        } finally {
            box.revalidate();
        }

        background.add(scroller);
    }

    private JPanel getNewsPanel(NewsItem newsItem) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#080a13"));
        panel.setBorder(BorderFactory.createLineBorder(Color.decode("#222635")));
        panel.setBounds(506, 110, 451, 90);
        panel.setPreferredSize(new Dimension(902, 90));
        panel.setMaximumSize(new Dimension(902, 90));

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#0d0f17"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#080a13"));
            }
        });

        JEditorPane newsTitlePane = new JEditorPane("text/html","<html><body><h1>Loading...</h1></body></html>");
        newsTitlePane.setEditable(false);
        newsTitlePane.setOpaque(false);
        newsTitlePane.setHighlighter(null);
        newsTitlePane.addHyperlinkListener(hle -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                openWebpage(hle.getURL().toString());
            }
        });
        newsTitlePane.setBounds(6, 7, 321, 25);
        newsTitlePane.setText("<html>" + Misc.getFontStyling() + "<a href=\"" + newsItem.getNewsUrl() + "\" class=\"news_title\">" + newsItem.getTitle() + "</a></html>");
        newsTitlePane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newsTitlePane.getParent().setBackground(Color.decode("#0d0f17"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                newsTitlePane.getParent().setBackground(Color.decode("#080a13"));
            }
        });
        panel.add(newsTitlePane);

        JEditorPane authorPane = new JEditorPane("text/html", "Loading...");
        authorPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        authorPane.setEditable(false);
        authorPane.setOpaque(false);
        authorPane.setHighlighter(null);
        authorPane.addHyperlinkListener(hle -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                openWebpage(hle.getURL().toString());
            }
        });
        authorPane.setBounds(260, 65, 171, 20);
        authorPane.setText("<html>" + Misc.getFontStyling() + "<body>by&nbsp;<a href=\"" + newsItem.getAuthorUrl() + "\" class=\"author\">" + newsItem.getAuthor() + "</a></body></html>");
        authorPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                authorPane.getParent().setBackground(Color.decode("#0d0f17"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                authorPane.getParent().setBackground(Color.decode("#080a13"));
            }
        });
        panel.add(authorPane);

        JLabel newsContentLabel = new JLabel("<html>" + Misc.getFontStyling() + "<p>" + newsItem.getContents() + "</p></html>");
        newsContentLabel.setBounds(9, 21, 415, 70);
        panel.add(newsContentLabel);

        JLabel dateLabel = new JLabel("<html>" + Misc.getFontStyling() + "<body>" + newsItem.getDate() + "</body></html>");
        dateLabel.setBounds(260, 13, 171, 20);
        dateLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel.add(dateLabel);

        return panel;
    }

    private JPanel feedErrorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#080a13"));
        panel.setBorder(BorderFactory.createLineBorder(Color.decode("#141722")));
        panel.setBounds(506, 110, 450, 90);
        panel.setPreferredSize(new Dimension(900, 90));
        panel.setMaximumSize(new Dimension(900, 90));

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#0d0f17"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#080a13"));
            }
        });

        JLabel newsTitleLabel = new JLabel("<html>" + Misc.getFontStyling() + "<h1>Unable to load feed!</h1></html>");
        newsTitleLabel.setBounds(9, 11, 321, 20);
        panel.add(newsTitleLabel);
        return panel;
    }

    private void buildNavBar() {
        banner = new JLabel(new ImageIcon(getClass().getResource("/themes/default/banner.png")));
        banner.setBounds(530, 201, 699, 224);
        background.add(banner);

        // home
        home = new CustomButton(this, "home", new Rectangle(57, 0, 100, 54));
        background.add(home);

        // community
        community = new CustomButton(this, "community", new Rectangle(157, 0, 143, 54));
        background.add(community);

        // vote
        vote = new CustomButton(this, "vote", new Rectangle(300, 0, 89, 54));
        background.add(vote);

        // store
        store = new CustomButton(this, "store", new Rectangle(389, 0, 102, 54));
        background.add(store);

        // highscores
        highscores = new CustomButton(this, "highscores", new Rectangle(491, 0, 129, 54));
        background.add(highscores);

        // discord
        discord = new CustomButton(this, "discord", new Rectangle(620, 0, 101, 54));
        background.add(discord);
    }

    private void buildUtilities() {
        minimize = new CustomButton(this, "minimize", new Rectangle(1205, 0, 25, 27));
        background.add(minimize);

        debug = new CustomButton(this, "debug", new Rectangle(1230, 0, 25, 27));
        background.add(debug);

        close = new CustomButton(this, "close", new Rectangle(1255, 0, 25, 27));
        background.add(close);
    }

    private void buildRunSection() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null || version.trim().equalsIgnoreCase("null")) {
            version = "- Test Build";
        } else {
            version = "v" + version;
        }

        launcherVersion = new JLabel("<html>" + Misc.getFontStyling() + "<div class=\"launcher_version\">Gielinor Launcher " + version + "</div></html>");
        launcherVersion.setBounds(11, 697, 172, 14);
        background.add(launcherVersion);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(11, 680, 972, 11);
        progressBar.setUI(new CustomBarUI());
        background.add(progressBar);

        downloadProgress = new JLabel("") {
          @Override
          public void setText(String text) {
              super.setText("<html>" + Misc.getFontStyling() + "<div class=\"download_progress\">" + text + "</div></html>");
          }
        };
        downloadProgress.setBounds(700, 655, 285, 14);
        downloadProgress.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        background.add(downloadProgress);

        downloadPercentage = new JLabel("") {
            @Override
            public void setText(String text) {
                super.setText("<html>" + Misc.getFontStyling() + "<div class=\"download_percentage\">" + text + "</div></html>");
            }
        };
        downloadPercentage.setBounds(485, 697, 500, 14);
        downloadPercentage.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        background.add(downloadPercentage);

        status = new JLabel("Checking for updates...") {
            @Override
            public void setText(String text) {
                super.setText("<html>" + Misc.getFontStyling() + "<div class=\"status_text\">" + text + "</div></html>");
            }
        };
        status.setBounds(11, 655, 229, 14);
        background.add(status);

        play = new CustomButton(this, "play", new Rectangle(1006, 659, 260, 51));
        background.add(play);
    }

    public void showGui() {
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
        try {
            if (!Launcher.developerMode) {
                updateManager.startLoaderDownload();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>" + e.getMessage() + "</font>", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateManager.startCacheDownload();
    }

    private void addMouseListener() {
        background.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });
        background.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int iX = initialClick.x;
                int iY = initialClick.y;
                if (iX >= 0 && iX <= background.getWidth() && iY >= 0 && iY <= background.getHeight()) { // these bounds are where the user can drag the window within
                    int thisX = getLocation().x;
                    int thisY = getLocation().y;
                    int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
                    int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    setLocation(X, Y);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        System.out.println(action);
        switch (action) {
            case "minimize":
                this.setState(JFrame.ICONIFIED);
                break;
            case "close":
                System.exit(0);
                break;
            case "home":
            case "community":
            case "highscores":
            case "store":
            case "vote":
            case "discord":
                //TODO: This is unsafe as fuck, lmfao
                openWebpage(configuration.getMapValueByKey("urls", action));
                break;
            case "play":
                startClient();
                break;
            case "debug":
                buildDebug();
                break;
            case "debug_close":
                rebuildMain();
                break;
            case "delete_cache":
                try {
                    deleteFileOrFolder(Paths.get(configuration.getMapValueByKey("paths", "cache")));
                    JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Successfully deleted cache.\nPlease restart the loader.</font>", "Success", JOptionPane.PLAIN_MESSAGE);
                    System.exit(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Could not delete cache!\n" + e.getMessage()+ "</font>", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                break;
            case "batch_debug":
                JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Attempting to run " + (configuration.getMapValueByKey("paths", "cache") + "GielinorDebug.bat</font>"), "Message", JOptionPane.PLAIN_MESSAGE);
                try {
                    Runtime.getRuntime().exec("cmd /c start " + configuration.getMapValueByKey("paths", "cache") + "GielinorDebug.bat");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "<html><font color=#FFFFFF>Could not run GielinorDebug.bat!\n</font>" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                break;
            case "log_location":
                try {
                    Desktop.getDesktop().open(Misc.WRITE_LOCATION);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("no command: " + action);
                break;
        }
    }

    private class CustomButton extends JButton {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public CustomButton(ActionListener listener, String text, Rectangle rect) {
            setActionCommand(text);
            addActionListener(listener);
            setIcon(new ImageIcon(getClass().getResource("/themes/default/" + text + ".png")));
            setRolloverIcon(new ImageIcon(getClass().getResource("/themes/default/" + text + "_hover.png")));

            URL disabledIcon = getClass().getResource("/themes/default/" + text + "_disabled.png");
            if (disabledIcon != null) {
                setDisabledIcon(new ImageIcon(disabledIcon));
            }

            setBorderPainted(false);
            setBounds(rect);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
        }
    }

    private void openWebpage(String url) {
        try {
            openWebpage(new URL(url).toURI());
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startClient() {
        this.getContentPane().removeAll();
        this.dispose();
        new Thread() {
            public void run() {
                try {
                    final ClassLoader classLoader = new URLClassLoader(new URL[]{new URL("file:///" + configuration.getMapValueByKey("paths", "client_jar"))}, ClassLoader.getSystemClassLoader().getParent());
                    Thread.currentThread().setContextClassLoader(classLoader);
                    final Class<?> gameLaunch = classLoader.loadClass("com.runescape.Game");
                    gameLaunch.getMethod("main", String[].class).invoke(null, new Object[]{new String[]{}});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public JLabel getStatus() {
        return status;
    }

    public JButton getPlay() {
        return play;
    }

    public JButton getDebug() {
        return debug;
    }

    private static void deleteFileOrFolder(final Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                return handleException(e);
            }

            private FileVisitResult handleException(final IOException e) {
                e.printStackTrace();
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                    throws IOException {
                if (e != null) {
                    return handleException(e);
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Update the progress bar's state whenever the progress of download changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    public void startDownload(String downloadURL, String saveDir, DownloadCategory category) {
        DownloadTask task = new DownloadTask(this, downloadURL, saveDir);
        task.addPropertyChangeListener(this);
        task.execute();
        task.setDownloadCategory(category);
        task.setUpdateManager(updateManager);
        status.setText(category.getDownloadInProgress());
    }

}