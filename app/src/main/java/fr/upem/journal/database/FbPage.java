package fr.upem.journal.database;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPage {
    String id;
    String name;
    String created_time;
    String description;
    String about;

    public FbPage(JSONObject object){
        try {
            this.name = object.getString("name");
            this.id = object.getString("id");
            this.about = object.getString("about");
            this.description = object.getString("description");
            this.created_time = object.optString("created_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setID(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCreated_time(String created_time){
        this.created_time = created_time;
    }

    public String getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAbout(){
        return about;
    }

    public String getCreated_time(){
        return created_time;
    }
}
