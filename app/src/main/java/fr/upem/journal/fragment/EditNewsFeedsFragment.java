package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.activity.NewsFeedActivity;
import fr.upem.journal.database.DatabaseHelper;
import fr.upem.journal.newsfeed.NewsFeed;

/**
 * Fragment where we can add and remove feeds. Each feed is associated with category
 */
public class EditNewsFeedsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String categoryTitle;
    private ArrayList<String> newsFeedLabels = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            categoryTitle = args.getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit_feeds, container, false);

        listView = (ListView) layout.findViewById(R.id.newsFeedlistView);
        adapter = new ArrayAdapter<>(layout.getContext(), android.R.layout.simple_list_item_1, newsFeedLabels);
        listView.setAdapter(adapter);
        loadData();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String label = (String) listView.getItemAtPosition(position);
                removeNewsFeedLabel(label);
                return true;
            }
        });

        return layout;
    }

    public void updateContent(String categoryTitle) {
        this.categoryTitle = categoryTitle;
        loadData();
    }

    //load data from database
    private void loadData() {
        databaseHelper = new DatabaseHelper(getContext());
        ArrayList<NewsFeed> newsFeeds = databaseHelper.selectNewsFeedsByCategory(categoryTitle);
        databaseHelper.close();
        newsFeedLabels.clear();
        for (NewsFeed newsFeed : newsFeeds) {
            newsFeedLabels.add(newsFeed.getLabel());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Method to add feed in the list
     * @param label the label of the feed
     */
    public void addNewsFeedLabel(String label) {
        newsFeedLabels.add(label);
        adapter.notifyDataSetChanged();
        NewsFeedActivity.refresh();
    }

    /**
     * Method to remove feed from the list
     * @param label The label of the feed
     */
    public void removeNewsFeedLabel(String label) {
        newsFeedLabels.remove(label);
        adapter.notifyDataSetChanged();

        if (!databaseHelper.removeNewsFeed(label)) {
            addNewsFeedLabel(label);
        }else{
            NewsFeedActivity.refresh();
        }
        databaseHelper.close();
    }
}
