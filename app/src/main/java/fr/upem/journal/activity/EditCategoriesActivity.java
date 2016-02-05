package fr.upem.journal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.upem.journal.R;
import fr.upem.journal.database.DatabaseHelper;
import fr.upem.journal.newsfeed.NewsCategory;

public class EditCategoriesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<NewsCategory> newsCategories = databaseHelper.selectNewsCategories();
        ArrayList<String> categoryTitles = new ArrayList<>();
        for (NewsCategory newsCategory : newsCategories) {
            categoryTitles.add(newsCategory.getTitle());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryTitles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryTitle = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(EditCategoriesActivity.this, EditNewsFeedsActivity.class);
                intent.putExtra("title", categoryTitle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAddCategory:
                Toast.makeText(getApplicationContext(), "Add Category", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
