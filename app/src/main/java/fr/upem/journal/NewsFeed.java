package fr.upem.journal;

import android.os.Parcel;
import android.os.Parcelable;


public class NewsFeed implements Parcelable {

    private String label;
    private String link;

    public NewsFeed(String label, String link) {
        this.label = label;
        this.link = link;
    }

    public String getLabel() {
        return label;
    }

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
