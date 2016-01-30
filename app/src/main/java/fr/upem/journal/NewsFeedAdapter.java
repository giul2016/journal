package fr.upem.journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedAdapter extends BaseAdapter {

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
    }

    private final Context context;
    private final LayoutInflater layoutInflater;
    private ArrayList<Item> items;

    public NewsFeedAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_item, null);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.itemDescription);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);
        String title = item.getTitle();
        String description = item.getDescription();

        holder.titleTextView.setText(title);
        holder.descriptionTextView.setText(description);

        return convertView;
    }

    public void updateItems(List<Item> items) {
        this.items.addAll(items);

        notifyDataSetChanged();
    }
}
