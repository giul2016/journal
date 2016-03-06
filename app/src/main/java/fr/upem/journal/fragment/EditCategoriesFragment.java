package fr.upem.journal.fragment;

import android.app.Activity;
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
import fr.upem.journal.newsfeed.NewsCategory;

public class EditCategoriesFragment extends Fragment {

    public interface OnItemSelectedListener {
        void onItemSelected(String categoryTitle);
    }

    private OnItemSelectedListener callback;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> categoryTitles;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit_categories, container, false);

        listView = (ListView) layout.findViewById(R.id.categorylistView);
        databaseHelper = new DatabaseHelper(layout.getContext());
        ArrayList<NewsCategory> newsCategories = databaseHelper.selectNewsCategories();
        databaseHelper.close();
        categoryTitles = new ArrayList<>();
        for (NewsCategory newsCategory : newsCategories) {
            categoryTitles.add(newsCategory.getTitle());
        }
        adapter = new ArrayAdapter<>(layout.getContext(), android.R.layout.simple_list_item_1, categoryTitles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryTitle = (String) listView.getItemAtPosition(position);
                callback.onItemSelected(categoryTitle);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryTitle = (String) listView.getItemAtPosition(position);
                removeCategoryTitle(categoryTitle);
                return true;
            }
        });

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
        }
    }

    public void addCategoryTitle(String categoryTitle) {
        categoryTitles.add(categoryTitle);
        adapter.notifyDataSetChanged();
        NewsFeedActivity.refresh();
    }

    public void removeCategoryTitle(String categoryTitle) {
        categoryTitles.remove(categoryTitle);
        adapter.notifyDataSetChanged();

        NewsCategory newsCategory = new NewsCategory(categoryTitle);

        if (!databaseHelper.removeNewsCategory(newsCategory)) {
            addCategoryTitle(categoryTitle);
        }else{
            NewsFeedActivity.refresh();
        }
        databaseHelper.close();
    }
}
