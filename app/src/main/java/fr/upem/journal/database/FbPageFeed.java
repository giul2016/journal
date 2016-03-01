package fr.upem.journal.database;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPageFeed {
    String id;
    String message;
    String created_time;

    public FbPageFeed(JSONObject object){
        try {
            this.message = object.getString("message");
            this.id = object.getString("id");
            this.created_time = object.getString("created_time");
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


    public String getCreated_time(){
        return created_time;
    }
}
