package fr.upem.journal;

import java.util.Date;

public class NewsFeedItem {

    private final String title;
    private final String description;
    private final String link;
    private final Date pubDate;
    private final String source;

    public NewsFeedItem(String title, String description, String link, Date pubDate, String source) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return title + " " + " " + description + " " + link + " " + pubDate + " " + source;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NewsFeedItem)) {
            return false;
        }

        NewsFeedItem item = (NewsFeedItem) o;
        return title.equals(item.title) && description.equals(item.description) && link.equals(item.link) && pubDate
                .equals(item.pubDate) & source.equals(item.source);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + pubDate.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }
}
