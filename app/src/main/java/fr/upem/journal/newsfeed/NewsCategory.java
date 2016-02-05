package fr.upem.journal.newsfeed;

import java.util.ArrayList;

public class NewsCategory {

    private String title;
    private final ArrayList<NewsFeed> feeds;

    public NewsCategory(String title) {
        this.title = title;
        this.feeds = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<NewsFeed> getFeeds() {
        return feeds;
    }

    public void addFeed(NewsFeed feed) {
        feeds.add(feed);
    }
}
