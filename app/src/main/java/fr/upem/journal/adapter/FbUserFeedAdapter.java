package fr.upem.journal.adapter;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import fr.upem.journal.R;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FbUserFeed feed = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_feed_fb, parent, false);
        }

//        Log.e("xxxx", feed.getCreated_time());
        TextView tvMess = (TextView) convertView.findViewById(R.id.user_feed_message_tv);
        if (feed.getMessage()!=null){
//            Log.e("+++", feed.getMessage());
            tvMess.setText(feed.getMessage());
        }else tvMess.setText("");
        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.user_feed_created_time_tv);
        if (feed.getMessage()!=null){
//            Log.e("+++", feed.getCreated_time());
            tvCreatedTime.setText(feed.getCreated_time());
        }else tvCreatedTime.setText("");


        if (feed.getPicture()!=null){
//            Log.e("+++", feed.getPicture());
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.user_feed_picture_iv))
                    .execute(feed.getPicture());
        }

        tvTotalLike = (TextView) convertView.findViewById(R.id.user_feed_total_like_tv);
        tvTotalLike.setText("0 like");
        tvTotalComment = (TextView) convertView.findViewById(R.id.user_feed_total_comment_tv);
        tvTotalComment.setText("0 comment");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+feed.getID(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            if (object!=null) {
                                JSONObject feed1 = object.getJSONObject("likes");

                            JSONArray array1 = feed1.getJSONArray("data");
                                Log.e("===", array1.toString());
//                                tvTotalLike.setText(array.length());
                                tvTotalLike.setText(String.valueOf(array1.length()) + "likes");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject object = response.getJSONObject();
                            if (object!=null) {
                                JSONObject feed2 = object.getJSONObject("comments");

                                JSONArray array2 = feed2.getJSONArray("data");
                                Log.e("===", array2.toString());
//                                tvTotalLike.setText(array.length());
                                tvTotalLike.setText(String.valueOf(array2.length()) + "comments");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes,comments");
        request.setParameters(parameters);
        request.executeAsync();

        return convertView;
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}