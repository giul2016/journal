package fr.upem.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsContentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView pubDateTextView;
    private TextView sourceTextView;
    NewsFeedItem newsFeedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        newsFeedItem = getIntent().getParcelableExtra("item");

        titleTextView = (TextView) findViewById(R.id.itemTitle);
        descriptionTextView = (TextView) findViewById(R.id.itemDescription);
        pubDateTextView = (TextView) findViewById(R.id.itemPubDate);
        sourceTextView = (TextView) findViewById(R.id.itemSource);

        titleTextView.setText(newsFeedItem.getTitle());
        descriptionTextView.setText(Html.fromHtml(newsFeedItem.getDescription().replaceAll("<img.+/(img)*>", "")));
        pubDateTextView.setText(newsFeedItem.getPubDate().toString());
        sourceTextView.setText(newsFeedItem.getSource());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.actionExplore:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("link", newsFeedItem.getLink());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
