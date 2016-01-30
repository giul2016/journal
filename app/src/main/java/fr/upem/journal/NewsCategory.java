package fr.upem.journal;

import java.util.ArrayList;

public class NewsCategory {

    private String title;
    private final ArrayList<String> feeds;

    public NewsCategory(String title) {
        this.title = title;
        this.feeds = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getFeeds() {
        return feeds;
    }

    public void addFeed(String feed) {
        feeds.add(feed);
    }
}
