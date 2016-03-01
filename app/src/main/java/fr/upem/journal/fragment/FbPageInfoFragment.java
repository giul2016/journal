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
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.upem.journal.R;

/**
 * Created by TTTH on 2/29/2016.
 */
public class FbPageInfoFragment extends android.support.v4.app.Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";
    private CallbackManager callbackManager;
    private TextView pageName_tv;
    private TextView pageAbout_tv;
    private TextView pageDescription_tv;
    private ProfilePictureView pagePicture;

    public static FbPageInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbPageInfoFragment fragment = new FbPageInfoFragment();
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
        final View view = inflater.inflate(R.layout.fb_page_info, container, false);
        Intent intent = getActivity().getIntent();
        String pageId = intent.getStringExtra("pageId");
        final String pageName = intent.getStringExtra("pageName");
        pagePicture = (ProfilePictureView) view.findViewById(R.id.pagePicture);

        pageName_tv = (TextView)view.findViewById(R.id.pageName);


        pagePicture.setProfileId(pageId);
        pageName_tv.setText(pageName);

        pageAbout(pageId);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void pageAbout(final String pageId){

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+pageId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        Log.e("page about", response.toString());
                        String about = object.optString("about");
                        Log.e("page about", about);
                        pageAbout_tv = (TextView)getActivity().findViewById(R.id.pageAbout);
                        pageAbout_tv.setText(about);
                        String description = object.optString("description");
                        pageAbout_tv = (TextView)getActivity().findViewById(R.id.pageDescription);
                        pageAbout_tv.setText(about);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "about,description");
        request.setParameters(parameters);
        request.executeAsync();

    }


}
