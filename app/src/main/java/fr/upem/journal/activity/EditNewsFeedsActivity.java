package fr.upem.journal.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import fr.upem.journal.R;
import fr.upem.journal.fragment.EditNewsFeedsFragment;

public class EditNewsFeedsActivity extends AppCompatActivity {

    private Toolbar toolbar;

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

        String categoryTitle = getIntent().getStringExtra("title");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(categoryTitle);
        setSupportActionBar(toolbar);

        Bundle args = new Bundle();
        args.putString("title", categoryTitle);
        EditNewsFeedsFragment fragment = new EditNewsFeedsFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.editNewsFeedsFragment, fragment).commit();
    }
}
