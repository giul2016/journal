package fr.upem.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFeedFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private NewsFeedAdapter newsFeedAdapter;

    private ArrayList<String> feeds;

    public static NewsFeedFragment newInstance(ArrayList<String> feeds) {
        Bundle args = new Bundle();
        args.putStringArrayList("feeds", feeds);

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            feeds = args.getStringArrayList("feeds");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView) layout.findViewById(R.id.listView);
        newsFeedAdapter = new NewsFeedAdapter(layout.getContext(), inflater);
        listView.setAdapter(newsFeedAdapter);
        listView.setOnItemClickListener(this);

        newsFeedAdapter.fetch(feeds);

        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) newsFeedAdapter.getItem(position);

        String link = item.getLink();

        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("link", link);

        startActivity(intent);
    }

}
