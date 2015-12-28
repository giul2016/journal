package fr.upem.journal;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewsFeedFragment extends Fragment {

    private ListView listView;
    private NewsFeedAdapter newsFeedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView) layout.findViewById(R.id.listView);
        newsFeedAdapter = new NewsFeedAdapter(layout.getContext(), inflater);
        listView.setAdapter(newsFeedAdapter);

        newsFeedAdapter.fill();
        return layout;
    }
}
