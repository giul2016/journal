package fr.upem.journal.database;

import org.json.JSONObject;

/**
 * Created by TTTH on 3/1/2016.
 */
public class FbUserFeed {
    String id = " ";
    String message = " ";
    String createdTime = " ";
    String photoId = " ";
    String link = " ";

    public FbUserFeed(JSONObject object) {
        message = object.optString("message", " ");
        link = object.optString("link", " ");
        id = object.optString("id", " ");
        createdTime = object.optString("created_time", " ");
        photoId = object.optString("full_picture", " ");
    }

    public String getID() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPicture() {
        return this.photoId;
    }

    public String getLink() {
        return this.link;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
