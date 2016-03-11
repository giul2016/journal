package fr.upem.journal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.database.*;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPageFeed extends Activity {

    ListView pageFeedLV;
    ArrayList<fr.upem.journal.database.FbPageFeed> facebookPageFeed = new ArrayList<fr.upem.journal.database.FbPageFeed>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_page_feed);
        pageFeedLV = (ListView) findViewById(R.id.pageFeedLV);
        Intent intent = getIntent();
        String pageId = intent.getStringExtra("pageId");
        final String pageName = intent.getStringExtra("pageName");


        //region get Feed
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + pageId + "/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        try {
                            JSONArray array = object.getJSONArray("data");
                            ArrayList<String> pages_feed = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                pages_feed.add(((JSONObject) array.get(i)).optString("message"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "about,description");
        request.setParameters(parameters);
        request.executeAsync();

        //endregion get Feed
    }


}
