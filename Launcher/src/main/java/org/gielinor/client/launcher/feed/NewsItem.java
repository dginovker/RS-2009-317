package org.gielinor.client.launcher.feed;

import java.util.Date;

public class NewsItem {

    private String title;
    private String contents;
    private String author;
    private String authorUrl;
    private String date;
    private Date rawDate;
    private String newsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthor() {
        if (author.trim().length() == 0) {
            return "Gielinor";
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        if (authorUrl.trim().length() == 0) {
            return "https://gielinor.org/community";
        }
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getRawDate() {
        return rawDate;
    }

    public void setRawDate(Date rawDate) {
        this.rawDate = rawDate;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
