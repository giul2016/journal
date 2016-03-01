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

/**
 * Created by TTTH on 2/29/2016.
 */
public class PageAdapter extends ArrayAdapter<FbPage> {
    public PageAdapter(Context context, ArrayList<FbPage> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FbPage page = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_page, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.page_name_tv);
        ProfilePictureView pagePicture = (ProfilePictureView)convertView.findViewById(R.id.pageItemPicture);
         tvName.setText(page.getName());
        pagePicture.setProfileId(page.getID());
        return convertView;
    }
}