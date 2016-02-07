package fr.upem.journal.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.upem.journal.R;
import fr.upem.journal.database.DatabaseHelper;
import fr.upem.journal.dialog.AddNewsFeedDialogFragment;
import fr.upem.journal.fragment.EditNewsFeedsFragment;
import fr.upem.journal.newsfeed.NewsFeed;

public class EditNewsFeedsActivity extends AppCompatActivity implements AddNewsFeedDialogFragment
        .AddNewsFeedDialogListener {

    private Toolbar toolbar;
    private String categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configuration = getResources().getConfiguration();
        int screenSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && configuration.orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_edit_news_feeds);

        categoryTitle = getIntent().getStringExtra("title");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(categoryTitle);
        setSupportActionBar(toolbar);

        Bundle args = new Bundle();
        args.putString("title", categoryTitle);
        EditNewsFeedsFragment fragment = new EditNewsFeedsFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.editNewsFeedsFragment, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                AddNewsFeedDialogFragment dialogFragment = new AddNewsFeedDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "addNewsFeed");
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(String label, String link) {
        NewsFeed newsFeed = new NewsFeed(label, link);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if (databaseHelper.insertNewsFeed(newsFeed, categoryTitle)) {
            EditNewsFeedsFragment fragment = (EditNewsFeedsFragment) getSupportFragmentManager().findFragmentById(
                    R.id.editNewsFeedsFragment);
            fragment.addNewsFeedLabel(label);
        }
        databaseHelper.close();
    }

    @Override
    public void onDialogNegativeClick() {
    }
}
