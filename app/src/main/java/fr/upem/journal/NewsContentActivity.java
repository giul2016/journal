package fr.upem.journal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class NewsContentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView pubDateTextView;
    private TextView sourceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NewsFeedItem item = getIntent().getParcelableExtra("item");

        titleTextView = (TextView) findViewById(R.id.itemTitle);
        descriptionTextView = (TextView) findViewById(R.id.itemDescription);
        pubDateTextView = (TextView) findViewById(R.id.itemPubDate);
        sourceTextView = (TextView) findViewById(R.id.itemSource);

        titleTextView.setText(item.getTitle());
        descriptionTextView.setText(Html.fromHtml(item.getDescription().replaceAll("<img.+/(img)*>", "")));
        pubDateTextView.setText(item.getPubDate().toString());
        sourceTextView.setText(item.getSource());
    }
}
