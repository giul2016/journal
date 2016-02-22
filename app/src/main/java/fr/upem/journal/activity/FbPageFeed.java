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

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPageFeed extends Activity {

    ListView pageFeedLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_page_feed);
        pageFeedLV = (ListView)findViewById(R.id.pageFeedLV);
        Intent intent = getIntent();
        String pageId = intent.getStringExtra("pageId");
        final String pageName = intent.getStringExtra("pageName");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+pageId+"/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            JSONArray array = object.getJSONArray("data");
                            Log.e("ok;", array.toString());
                            ArrayList<String> pages_feed = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                Log.e("+ feed : ", ((JSONObject) array.get(i)).optString("message"));
                                pages_feed.add(((JSONObject) array.get(i)).optString("message"));

                            }
                            TextView pageName_tv = (TextView)findViewById(R.id.pageName);
                            pageName_tv.setText(pageName);
                            ArrayAdapter adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, pages_feed);
                            pageFeedLV.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).executeAsync();

    }
}
