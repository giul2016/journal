package fr.upem.journal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

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
        return convertView;
    }
}