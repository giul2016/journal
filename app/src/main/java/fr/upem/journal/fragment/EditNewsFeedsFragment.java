package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.database.DatabaseHelper;
import fr.upem.journal.newsfeed.NewsFeed;

public class EditNewsFeedsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String categoryTitle;
    private ArrayList<String> newsFeedLabels = new ArrayList<>();

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

        listView = (ListView) layout.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(layout.getContext(), android.R.layout.simple_list_item_1, newsFeedLabels);
        listView.setAdapter(adapter);
        loadData();

        return layout;
    }

    public void updateContent(String categoryTitle) {
        this.categoryTitle = categoryTitle;
        loadData();
    }

    private void loadData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        ArrayList<NewsFeed> newsFeeds = databaseHelper.selectNewsFeedsByCategory(categoryTitle);
        newsFeedLabels.clear();
        for (NewsFeed newsFeed : newsFeeds) {
            newsFeedLabels.add(newsFeed.getLabel());
        }
        adapter.notifyDataSetChanged();
    }

}
