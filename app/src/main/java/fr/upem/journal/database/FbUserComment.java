package fr.upem.journal.database;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TTTH on 3/4/2016.
 */
public class FbUserComment {
    String id;
    String message;
    String created_time;
    String userId;

    public FbUserComment(JSONObject object){
        try {
            this.message = object.getString("message");
            this.id = object.getString("id");
            this.created_time = object.getString("created_time");
            this.userId = object.getJSONObject("from").getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getID(){
        return id;
    }

    public String getMessage(){
        return message;
    }

    public String getUser(){
        return userId;
    }


    public String getCreated_time(){
        return created_time;
    }
}
