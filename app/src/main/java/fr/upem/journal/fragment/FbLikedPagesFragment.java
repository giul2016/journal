package fr.upem.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import fr.upem.journal.activity.FbPageFeed;
import fr.upem.journal.database.FbPage;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbLikedPagesFragment extends android.support.v4.app.Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";

    private CallbackManager callbackManager;
    private ListView pagesList;
    ArrayList<FbPage> facebookPage = new ArrayList<FbPage>();

    public static FbLikedPagesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbLikedPagesFragment fragment = new FbLikedPagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_fb_liked_pages, container, false);
        getLikedPages();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLikedPages();
    }
//region Functions

    private void getLikedPages(){
        Log.e("+++ get pages", "ok");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        // Application code
//                        Log.e("GraphResponse", response.toString());

                        GraphRequest likeRequest = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                                "/me/likes/",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {

                                        try {
//                                            Intent intent = new Intent(MainActivity.this,PagesList.class);
                                            Log.d("GraphLikeResponse", "Likes:- " + response.toString());
                                            JSONObject object = response.getJSONObject();
                                            JSONArray array = new JSONArray();

                                            if(object != null) {
                                                array = object.getJSONArray("data");
                                            }
//                                            intent.putExtra("jsonPagesData",array.toString());
//                                            startActivity(intent);
                                            Log.e("GraphObject", "Likes:- " + array.toString());
                                            ArrayList<String> pages_list = new ArrayList<String>();
                                            for (int i=0; i<array.length();i++){
//                                                Log.e("+ name : ", ((JSONObject) array.get(i)).optString("name"));
                                                pages_list.add(((JSONObject) array.get(i)).optString("name"));
                                                facebookPage.add(new FbPage((JSONObject) array.get(i)));

                                            }
                                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, pages_list);
                                            pagesList = (ListView)getView().findViewById(R.id.listView);
                                            pagesList.setAdapter(adapter);
                                            pagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                    Log.e("----------",facebookPage.get(position).getID());
                                                    Intent newIntent = new Intent(getActivity(),FbPageFeed.class);
                                                    newIntent.putExtra("pageId",facebookPage.get(position).getID());
                                                    newIntent.putExtra("pageName",facebookPage.get(position).getName());
                                                    startActivity(newIntent);

                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        likeRequest.executeAsync();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //endregion Functions

}

