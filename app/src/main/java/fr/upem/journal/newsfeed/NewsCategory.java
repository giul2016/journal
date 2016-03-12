package fr.upem.journal.newsfeed;

import java.util.ArrayList;

/**
 * Class representing a category of news
 */
public class NewsCategory {

    private String title;
    private final ArrayList<NewsFeed> feeds;

    /**
     *
     * @param title Category title
     */
    public NewsCategory(String title) {
        this.title = title;
        this.feeds = new ArrayList<>();
    }

    /**
     * Getter for the title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the list of feeds contained in this category
     * @return
     */
    public ArrayList<NewsFeed> getFeeds() {
        return feeds;
    }

    /**
     * Add feed to this category
     * @param feed Feed to add
     */
    public void addFeed(NewsFeed feed) {
        feeds.add(feed);
    }
}
