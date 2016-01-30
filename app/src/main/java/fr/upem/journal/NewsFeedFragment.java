package fr.upem.journal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class NewsFeedFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private NewsFeedAdapter newsFeedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView) layout.findViewById(R.id.listView);
        newsFeedAdapter = new NewsFeedAdapter(layout.getContext(), inflater);
        listView.setAdapter(newsFeedAdapter);
        listView.setOnItemClickListener(this);

        return layout;
    }

    public void updateItems(List<Item> items) {
        newsFeedAdapter.updateItems(items);
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
