package org.gielinor.client.launcher.feed;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.gielinor.client.launcher.util.Misc;

/**
 * It Reads and prints any RSS/Atom feed type.
 * <p>
 *
 * @author Alejandro Abdelnur
 * @author Corey
 */
public class FeedReader {

    public static ArrayList<NewsItem> getFeed(String rssFeed, String userProfileUrl) throws IOException, FeedException {
        try {
            URL feedUrl = new URL(rssFeed);

            ArrayList<NewsItem> newsItems = new ArrayList<>();
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            for (SyndEntry entry : feed.getEntries()) {
                NewsItem item = new NewsItem();

                String contents;
                if (entry.getDescription().getValue().length() >= 220) {
                    contents = entry.getDescription().getValue().substring(0, 217).trim() + "...";
                } else if (entry.getDescription().getValue().length() == 0) {
                    contents = "Click to see more...";
                } else {
                    contents = entry.getDescription().getValue();
                }

                String newsTitle;
                if (entry.getTitle().length() >= 33) {
                    newsTitle = entry.getTitle().substring(0, 30).trim() + "...";
                } else {
                    newsTitle = entry.getTitle();
                }

                String rawAuthor = entry.getAuthor();
                String authorUsername = rawAuthor.substring(rawAuthor.indexOf('-') + 1);
                String authorProfile = userProfileUrl + rawAuthor.trim().replaceAll(" ", "-") + "/";
                String publishDate = Misc.getFriendlyTime(entry.getPublishedDate());
                String link = entry.getLink();

                item.setContents(contents);
                item.setTitle(newsTitle);
                item.setAuthor(authorUsername);
                item.setAuthorUrl(authorProfile);
                item.setDate(publishDate);
                item.setRawDate(entry.getPublishedDate());
                item.setNewsUrl(link);

                newsItems.add(item);
            }
            newsItems.sort(Comparator.comparing(NewsItem::getRawDate).reversed());
            return newsItems;
        } catch (Exception ex) {
            System.err.println("Failed to load forum RSS feed!");
            throw ex;
        }
    }
}