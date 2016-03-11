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

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.activity.FbComment;
import fr.upem.journal.database.FbPage;
import fr.upem.journal.database.FbPageFeed;

/**
 * Created by TTTH on 2/29/2016.
 */
public class FbFeedAdapter extends ArrayAdapter<FbPageFeed> {
    public FbFeedAdapter(Context context, ArrayList<FbPageFeed> feed) {
        super(context, 0, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FbPageFeed page = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fb_feed, parent, false);
        }

        //region init
        final TextView tvMess = (TextView) convertView.findViewById(R.id.page_message_tv);
        final TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.page_created_time_tv);
        final TextView tvTotalLike = (TextView) convertView.findViewById(R.id.page_feed_total_like_tv);
        final TextView tvTotalComment = (TextView) convertView.findViewById(R.id.page_feed_total_comment_tv);
        final ImageView picture = (ImageView) convertView.findViewById(R.id.feed_picture_iv);
        //endregion init

        //region get Mess, photo,..
        tvMess.setText(page.getMessage());
        tvCreatedTime.setText(page.getCreated_time());
        if (page.getPicture() != null) {
            Glide.with(getContext())
                    .load(page.getPicture())
                    .into(picture);
        }
        //endregion get Mess, photo,..

        //region get Likes, Comments
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + page.getID(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        //region get Likes
                        try {
                            JSONObject object = response.getJSONObject();
                            if (object != null) {
                                JSONObject feed1 = object.getJSONObject("likes");

                                JSONArray array1 = feed1.getJSONArray("data");
                                Log.e("===", array1.toString());
                                tvTotalLike.setText(String.valueOf(array1.length()) + " ");
                            }
                            else
                                tvTotalLike.setText("0 ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //endregion getLikes

                        //region get Comments

                        try {
                            JSONObject object = response.getJSONObject();
                            if (object != null) {
                                JSONObject feed2 = object.getJSONObject("comments");

                                JSONArray array2 = feed2.getJSONArray("data");
                                Log.e("===", array2.toString());
//                                tvTotalLike.setText(array.length());
                                tvTotalComment.setText(String.valueOf(array2.length()) + " comments");
                                if (array2.length()!=0){
                                    tvTotalComment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), FbComment.class);
                                            intent.putExtra("feedId", page.getID());
                                            getContext().startActivity(intent);
                                        }
                                    });
                                }
                            } else tvTotalComment.setText(" 0 comment");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //endregion get Comments
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes,comments");
        request.setParameters(parameters);
        request.executeAsync();

        //endregion get Likes, Comments

        return convertView;
    }


}