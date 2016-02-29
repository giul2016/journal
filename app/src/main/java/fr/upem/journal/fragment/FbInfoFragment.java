package fr.upem.journal.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FbPagerAdapter;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbInfoFragment extends android.support.v4.app.Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";

    private CallbackManager callbackManager;
    private LoginButton login_btn;
    private TextView name_tv;
    private ProfilePictureView pictureView;
    private String[] permissions;
    private ViewPager viewPager;
    private FbPagerAdapter mAdapter;
    private ActionBar actionBar;

    public static FbInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbInfoFragment fragment = new FbInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_fb_info, container, false);

        //region Init

        login_btn = (LoginButton)view.findViewById(R.id.login_button);
        name_tv = (TextView)view.findViewById(R.id.name);
        pictureView = (ProfilePictureView) view.findViewById(R.id.picture);
        permissions = new String[]{"user_friends","user_about_me","user_actions.music","user_birthday",
                "user_likes","user_friends","user_photos","user_relationships",
                "user_tagged_places","user_work_history","user_actions.books","user_actions.news",
                "user_education_history","user_games_activity","user_location",
                "user_posts","user_religion_politics","user_videos","user_actions.fitness",
                "user_actions.video","user_events","user_hometown","user_managed_groups",
                "user_relationship_details","user_status","user_website"};

        if(AccessToken.getCurrentAccessToken()==null){
            name_tv.setText("You are not logged in. Please log in!");
        }
        else {
            init_info();
        }

        //endregion Init

        //region Log in

        login_btn.setReadPermissions(permissions);
        login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                String id = object.optString("id");
                                String name = object.optString("name");
                                name_tv.setText("Hi " + name + "!");
                                pictureView.setProfileId(id);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                name_tv.setText("Login canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                name_tv.setText("Login failed.");
            }
        });

        //endregion Log in

        return view;
    }


    //region Functions

    public void init_info(){
        Log.e("+++ init info", "ok");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
//                        Log.e("GraphResponse", response.toString());
                        object = response.getJSONObject();
                        if (object!=null) {
                            String id = object.optString("id");
                            String name = object.optString("name");
                            String birthday = object.optString("birthday");
                            Log.d("++respons ", response.toString());
                            name_tv = (TextView) getView().findViewById(R.id.name);
                            name_tv.setText("Hi " + name);
                            ProfilePictureView profilePictureView = (ProfilePictureView) getView().findViewById(R.id.picture);
                            profilePictureView.setProfileId(id);
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onResume() {
        super.onResume();
        init_info();
    }
//endregion functions

}



