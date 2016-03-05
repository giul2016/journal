package fr.upem.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FbFeedAdapter;
import fr.upem.journal.adapter.FbUserFeedAdapter;
import fr.upem.journal.database.FbPage;
import fr.upem.journal.database.FbPageFeed;
import fr.upem.journal.database.FbUserFeed;

/**
 * Created by TTTH on 3/1/2016.
 */
public class FbUserFeedFreagment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private CallbackManager callbackManager;
    private ListView feedList;
    ArrayList<FbUserFeed> facebookUserFeed = new ArrayList<FbUserFeed>();

    public static FbUserFeedFreagment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbUserFeedFreagment fragment = new FbUserFeedFreagment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fb_user_feed, container, false);
        feedList = (ListView)view.findViewById(R.id.fb_user_feed_lv);
        Intent intent = getActivity().getIntent();
        String pageId = intent.getStringExtra("pageId");
        final String pageName = intent.getStringExtra("pageName");

        getFeed();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getFeed();
    }


    public void getFeed(){

        facebookUserFeed.clear();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            if (object!=null)
                            {

                                JSONObject feed = object.getJSONObject("feed");
                                JSONArray array = feed.getJSONArray("data");
                                ArrayList<String> pages_feed = new ArrayList<String>();
                                for (int i = 0; i < array.length(); i++) {
                                    facebookUserFeed.add(new FbUserFeed((JSONObject) array.get(i)));
                                }



                                FbUserFeedAdapter adapter = new FbUserFeedAdapter(getContext(),facebookUserFeed);
                                feedList.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "feed.limit(50){full_picture,message,created_time}");
        request.setParameters(parameters);
        request.executeAsync();



    }

}

