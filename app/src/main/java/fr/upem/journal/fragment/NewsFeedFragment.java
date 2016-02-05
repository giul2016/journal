package fr.upem.journal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.newsfeed.NewsFeed;
import fr.upem.journal.adapter.NewsFeedAdapter;
import fr.upem.journal.newsfeed.NewsFeedItem;
import fr.upem.journal.R;
import fr.upem.journal.activity.NewsContentActivity;
import fr.upem.journal.task.FetchRSSFeedTask;

public class NewsFeedFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private NewsFeedAdapter newsFeedAdapter;
    private ArrayList<NewsFeed> feeds;

    public static NewsFeedFragment newInstance(ArrayList<NewsFeed> feeds) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("feeds", feeds);

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            feeds = args.getParcelableArrayList("feeds");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) layout.findViewById(R.id.listView);
        newsFeedAdapter = new NewsFeedAdapter(layout.getContext(), inflater);

        listView.setAdapter(newsFeedAdapter);
        listView.setOnItemClickListener(this);

        fetch();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
            }
        });

        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsFeedItem item = (NewsFeedItem) newsFeedAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        intent.putExtra("item", item);

        startActivity(intent);
    }

    private void fetch() {
        new FetchRSSFeedTask() {
            @Override
            protected void onPostExecute(List<NewsFeedItem> items) {
                newsFeedAdapter.updateItems(items);
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute(feeds.toArray(new NewsFeed[feeds.size()]));
    }

}
