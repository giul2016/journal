package fr.upem.journal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.activity.FbComment;
import fr.upem.journal.database.FbUserComment;
import fr.upem.journal.database.FbUserFeed;

/**
 * Created by TTTH on 3/4/2016.
 */
public class FbUserFeedCommentAdapter extends ArrayAdapter<FbUserComment> {

    TextView comment_tv;
    TextView created_tv;
    ProfilePictureView profilePictureView;
    TextView user_name;

    public FbUserFeedCommentAdapter(Context context, ArrayList<FbUserComment> comment) {
        super(context, 0, comment);
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FbUserComment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_feed_comment, parent, false);
        }


        comment_tv = (TextView)convertView.findViewById(R.id.user_feed_comment_mess_tv);
        created_tv = (TextView)convertView.findViewById(R.id.user_feed_comment_created_tv);
        profilePictureView = (ProfilePictureView)convertView.findViewById(R.id.user_comment_picture);
        user_name = (TextView)convertView.findViewById(R.id.fb_user_name);

        comment_tv.setText(comment.getMessage());
        created_tv.setText(comment.getCreated_time());
        profilePictureView.setProfileId(comment.getUser());

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + comment.getUser(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            user_name.setText(response.getJSONObject().getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        request.executeAsync();


        return convertView;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
