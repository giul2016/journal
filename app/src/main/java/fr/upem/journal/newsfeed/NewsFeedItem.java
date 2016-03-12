package fr.upem.journal.newsfeed;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Class representing a news item
 */
public class NewsFeedItem implements Parcelable {

    private final String title;
    private final String description;
    private final String link;
    private final Date pubDate;
    private final String source;

    /**
     *
     * @param title Title of the news
     * @param description Description of the news
     * @param link Link of the news
     * @param pubDate Publication date of the news
     * @param source Source of the news
     */
    public NewsFeedItem(String title, String description, String link, Date pubDate, String source) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.source = source;
    }

    /**
     * Getter for the title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the description
     * @return
     */
    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    /**
     * Getter for the publication date
     * @return
     */
    public Date getPubDate() {
        return pubDate;
    }

    /**
     * Getter for the source
     * @return
     */
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

    public static final Creator<NewsFeedItem> CREATOR = new Creator<NewsFeedItem>() {
        @Override
        public NewsFeedItem createFromParcel(Parcel in) {
            String title = in.readString();
            String description = in.readString();
            String link = in.readString();
            Date pubDate = (Date) in.readSerializable();
            String source = in.readString();
            return new NewsFeedItem(title, description, link, pubDate, source);
        }

        @Override
        public NewsFeedItem[] newArray(int size) {
            return new NewsFeedItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeSerializable(pubDate);
        dest.writeString(source);
    }
}
