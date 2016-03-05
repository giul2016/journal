package fr.upem.journal.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TTTH on 3/1/2016.
 */
public class FbUserFeed {
    String id = " ";
    String message = " ";
    String created_time = " ";
    String photoId = " ";
    String link = " ";

    public FbUserFeed(JSONObject object){
        try {

            if (object.getString("message")!=null){
                this.message = object.getString("message");
            }

            if (object.getString("link")!=null){
                this.link = object.getString("link");
            }
            if (object.getString("id")!=null){
                this.id = object.getString("id");
            }
            if (object.getString("created_time")!=null){
                this.created_time = object.getString("created_time");
            }

            if (object.getString("full_picture")!=null){
                this.photoId = object.getString("full_picture");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setID(String id){
        this.id = id;
    }


    public void setMessage(String name){
        this.message = message;
    }


    public void setCreated_time(String created_time){
        this.created_time = created_time;
    }


    public String getID(){
        return this.id;
    }

    public String getMessage(){
        return this.message;
    }

    public String getPicture(){
        return this.photoId;
    }

    public String getLink(){
        return this.link;
    }
    public String getCreated_time(){
        return created_time;
    }
}
