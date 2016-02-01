package fr.upem.journal;

import java.util.Date;

public class Item {

    private final String title;
    private final String description;
    private final String link;
    private final Date pubDate;

    public Item(String title, String description, String link, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
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

    @Override
    public String toString() {
        return title + " " + " " + description + " " + link + " " + pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) {
            return false;
        }

        Item item = (Item) o;
        return title.equals(item.title) && description.equals(item.description) && link.equals(item.link) && pubDate.equals
                (item.pubDate);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + pubDate.hashCode();
        return result;
    }
}
