package fr.upem.journal;

public class Item {

    private final String title;
    private final String description;
    private final String link;
    private final String pubDate;

    public Item(String title, String description, String link, String pubDate) {
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

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return title + " " + " " + description + " " + link + " " + pubDate;
    }
}
