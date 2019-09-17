package org.gielinor.client.launcher.net.impl;

/**
 * @author Corey
 */
public enum DownloadCategory {

    CLIENT("Your client is being updated!", "The client has been updated"),
    CACHE("Your cache is being updated!", "The cache has been updated"),
    LAUNCHER("The launcher is being updated!", "The launcher has been updated"),
    OTHER("", "");

    private String downloadInProgress;
    private String downloadComplete;

    public String getDownloadInProgress() {
        return downloadInProgress;
    }

    public String getDownloadComplete() {
        return downloadComplete;
    }

    DownloadCategory(String downloadInProgress, String downloadComplete) {
        this.downloadInProgress = downloadInProgress;
        this.downloadComplete = downloadComplete;
    }

    public static final String READY_TO_PLAY = "Gielinor ready to play!";

}