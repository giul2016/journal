package fr.upem.journal.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import fr.upem.journal.R;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPageFeed {
    String id;
    String message;
    String created_time;
    String photoId;

    public FbPageFeed(JSONObject object){
        try {
            this.message = object.getString("message");
            this.id = object.getString("id");
            this.created_time = object.getString("created_time");
            this.photoId = object.getString("full_picture");
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
        return id;
    }

    public String getMessage(){
        return message;
    }

    public String getPicture(){
        return photoId;
    }


    public String getCreated_time(){
        return created_time;
    }
}
