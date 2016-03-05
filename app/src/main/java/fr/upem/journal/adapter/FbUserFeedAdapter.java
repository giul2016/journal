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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.activity.FbComment;
import fr.upem.journal.database.FbPageFeed;
import fr.upem.journal.database.FbUserFeed;

/**
 * Created by TTTH on 3/1/2016.
 */
public class FbUserFeedAdapter extends ArrayAdapter<FbUserFeed> {

    TextView tvTotalLike;
    TextView tvTotalComment;

    public FbUserFeedAdapter(Context context, ArrayList<FbUserFeed> feed) {
        super(context, 0, feed);
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final FbUserFeed feed = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_feed_fb, parent, false);

        }
        convertView.setTag(position);


        //region init
        TextView tvMess = (TextView) convertView.findViewById(R.id.user_feed_message_tv);
        ImageView picture = (ImageView) convertView.findViewById(R.id.user_feed_picture_iv);
        tvTotalLike = (TextView) convertView.findViewById(R.id.user_feed_total_like_tv);
        tvTotalComment = (TextView) convertView.findViewById(R.id.user_feed_total_comment_tv);
        tvTotalComment.setTag(position);
        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.user_feed_created_time_tv);

        //endregion init

        //region get mess, photo,..
        if (feed.getLink() != null) {
            Glide.with(getContext())
                    .load(feed.getLink())
                    .into(picture);
            tvMess.setText(feed.getLink());
        }
        if ((feed.getPicture() != null) & (feed.getPicture() != " ")) {
            Glide.with(getContext())
                    .load(feed.getPicture())
                    .into(picture);
        }
        if ((feed.getMessage() != null) & (feed.getMessage() != " ")) {
            tvMess.setText(feed.getMessage());
        } else tvMess.setText("");

        if (feed.getMessage() != null) {
            tvCreatedTime.setText(feed.getCreated_time());
        } else tvCreatedTime.setText("");
        //endregion get mess, photo,..


        //region get Comments, Likes
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + feed.getID(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        //region get Likes
                        try {
                            JSONObject object = response.getJSONObject();
                            if (object != null) {
                                JSONObject feed1 = object.getJSONObject("likes");

                                JSONArray array1 = feed1.getJSONArray("data");
                                tvTotalLike.setText(String.valueOf(array1.length()) + " ");
                            } else
                                tvTotalLike.setText(" 0" );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //endregion get Likes

                        //region get Comments
                        try {
                            JSONObject object = response.getJSONObject();
                            if (object != null) {
                                JSONObject feed2 = object.getJSONObject("comments");

                                JSONArray array2 = feed2.getJSONArray("data");
                                tvTotalComment.setText(String.valueOf(array2.length()) + " comments");

                            }else {
                                tvTotalComment.setText(" 0 comment");
                            }
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
        //endregion get Comments, Likes

        //region view Comments
        tvTotalComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("abc", String.valueOf(v.getTag()));
                Log.e("abc", feed.getID()+feed.getMessage());
                Intent intent = new Intent(getContext(), FbComment.class);
                intent.putExtra("feedId", feed.getID());
                getContext().startActivity(intent);
            }
        });
        //endregion view Comments


        return convertView;
    }



}