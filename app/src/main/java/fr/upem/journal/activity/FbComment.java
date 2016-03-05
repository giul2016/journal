package fr.upem.journal.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FBPagePagerAdapter;
import fr.upem.journal.adapter.FbUserFeedAdapter;
import fr.upem.journal.adapter.FbUserFeedCommentAdapter;
import fr.upem.journal.database.FbUserComment;
import fr.upem.journal.database.FbUserFeed;

public class FbComment extends AppCompatActivity {

    ArrayList<FbUserComment> facebookUserComment = new ArrayList<FbUserComment>();
    private ListView commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_comment);
        Intent intent = getIntent();
        String feedId = intent.getStringExtra("feedId");
        commentList = (ListView)findViewById(R.id.fb_comment_lv);

//        facebookUserComment.clear();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + feedId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        if (object != null) {

                            try {

                                JSONObject comment = object.getJSONObject("comments");
                                JSONArray array = comment.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    facebookUserComment.add(new FbUserComment((JSONObject) array.get(i)));
                                }

                                FbUserFeedCommentAdapter adapter = new FbUserFeedCommentAdapter(getBaseContext(),facebookUserComment);
                                commentList.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "comments");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
