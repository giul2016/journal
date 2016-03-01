package fr.upem.journal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.upem.journal.R;
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
        FbPageFeed page = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fb_feed, parent, false);
        }

        TextView tvMess = (TextView) convertView.findViewById(R.id.page_message_tv);
        tvMess.setText(page.getMessage());
        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.page_created_time_tv);
        tvCreatedTime.setText(page.getCreated_time());

        if (page.getPicture()!=null){
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.feed_picture_iv))
                    .execute(page.getPicture());
            Log.e("+", page.getPicture());

        }

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