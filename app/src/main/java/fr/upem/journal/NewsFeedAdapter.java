package fr.upem.journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class NewsFeedAdapter extends BaseAdapter {

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView infosTextView;
    }

    private final Context context;
    private final LayoutInflater layoutInflater;
    private HashSet<NewsFeedItem> items;
    private ArrayList<NewsFeedItem> itemList;
    private final Object monitor = new Object();

    public NewsFeedAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.items = new HashSet<>();
        this.itemList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
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
            holder.infosTextView = (TextView) convertView.findViewById(R.id.itemInfos);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NewsFeedItem item = (NewsFeedItem) getItem(position);

        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.infosTextView.setText(item.getSource() + " / " + item.getPubDate().toString());

        return convertView;
    }

    private void sortDescending(ArrayList<NewsFeedItem> itemList) {
        Collections.sort(itemList, new Comparator<NewsFeedItem>() {
            @Override
            public int compare(NewsFeedItem lhs, NewsFeedItem rhs) {
                return -lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }

    private void sortAscending(ArrayList<NewsFeedItem> itemList) {
        Collections.sort(itemList, new Comparator<NewsFeedItem>() {
            @Override
            public int compare(NewsFeedItem lhs, NewsFeedItem rhs) {
                return lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }

    public void updateItems(List<NewsFeedItem> items) {
        if (items != null) {
            synchronized (monitor) {
                if (this.items.addAll(items)) {
                    ArrayList<NewsFeedItem> updatedItemList = new ArrayList<>(items);
                    sortDescending(updatedItemList);
                    itemList = updatedItemList;
                    notifyDataSetChanged();
                }
            }
        }
    }
}
