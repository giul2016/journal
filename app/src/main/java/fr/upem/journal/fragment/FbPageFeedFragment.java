package fr.upem.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FbFeedAdapter;
import fr.upem.journal.adapter.FbPagerAdapter;
import fr.upem.journal.adapter.PageAdapter;
import fr.upem.journal.database.FbPage;
import fr.upem.journal.database.FbPageFeed;

/**
 * Created by TTTH on 2/29/2016.
 */
public class FbPageFeedFragment extends android.support.v4.app.Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";
    private CallbackManager callbackManager;
    ListView pageFeedLV;
    ArrayList<FbPageFeed> facebookPage = new ArrayList<FbPageFeed>();

    public static FbPageFeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbPageFeedFragment fragment = new FbPageFeedFragment();
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
        final View view = inflater.inflate(R.layout.fb_page_feed, container, false);
        pageFeedLV = (ListView)view.findViewById(R.id.pageFeedLV);
        Intent intent = getActivity().getIntent();
        String pageId = intent.getStringExtra("pageId");
        final String pageName = intent.getStringExtra("pageName");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + pageId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            JSONObject feed = object.getJSONObject("feed");
                            JSONArray array = feed.getJSONArray("data");
                            ArrayList<String> pages_feed = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                pages_feed.add(((JSONObject) array.get(i)).optString("message"));

                                facebookPage.add(new FbPageFeed((JSONObject) array.get(i)));

                            }
                            FbFeedAdapter adapter = new FbFeedAdapter(getContext(),facebookPage);

                            pageFeedLV.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "feed{full_picture,message,created_time}");
        request.setParameters(parameters);
        request.executeAsync();




return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
