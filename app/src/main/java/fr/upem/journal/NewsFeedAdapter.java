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
import java.util.List;

import fr.upem.journal.tasks.FetchRSSFeedTask;

public class NewsFeedAdapter extends BaseAdapter {

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView pubDateTextView;
    }

    private final Context context;
    private final LayoutInflater layoutInflater;
    private ArrayList<Item> items;
    private final Object monitor = new Object();

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
            holder.pubDateTextView = (TextView) convertView.findViewById(R.id.itemPubDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);

        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.pubDateTextView.setText(item.getPubDate().toString());

        return convertView;
    }

    public void fetch(ArrayList<String> feeds) {
        for (String feed : feeds) {
            new FetchRSSFeedTask() {
                @Override
                protected void onPostExecute(List<Item> items) {
                    updateItems(items);
                }
            }.execute(feed);
        }
    }

    private void sortDescending() {
        Collections.sort(this.items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return -lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }

    private void sortAscending() {
        Collections.sort(this.items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }

    public void updateItems(List<Item> items) {
        if (items != null) {
            synchronized (monitor) {
                this.items.addAll(items);
                sortDescending();
            }
        }
        notifyDataSetChanged();
    }
}
