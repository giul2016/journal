package fr.upem.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class NewsFeedFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private NewsFeedAdapter newsFeedAdapter;

    private String feed;

    public static NewsFeedFragment newInstance(String feed) {
        Bundle args = new Bundle();
        args.putString("feed", feed);

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            feed = args.getString("feed");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView) layout.findViewById(R.id.listView);
        newsFeedAdapter = new NewsFeedAdapter(layout.getContext(), inflater);
        listView.setAdapter(newsFeedAdapter);
        listView.setOnItemClickListener(this);

        newsFeedAdapter.fetch(feed);

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
