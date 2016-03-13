package fr.upem.journal.newsfeed;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class representing a feed
 */
public class NewsFeed implements Parcelable {

    private String label;
    private String link;

    /**
     *
     * @param label The label of the feed
     * @param link The link of the feed
     */
    public NewsFeed(String label, String link) {
        this.label = label;
        this.link = link;
    }

    /**
     * Getter for the label
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Getter for the link
     * @return
     */
    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return label + " : " + link;
    }

    public static final Creator<NewsFeed> CREATOR = new Creator<NewsFeed>() {
        @Override
        public NewsFeed createFromParcel(Parcel in) {
            String label = in.readString();
            String link = in.readString();
            return new NewsFeed(label, link);
        }

        @Override
        public NewsFeed[] newArray(int size) {
            return new NewsFeed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(link);
    }
}
