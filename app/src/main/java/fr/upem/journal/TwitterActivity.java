package fr.upem.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TwitterActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TwitterActivity.this, NewsFeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(TwitterActivity.this, FacebookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
    }
}
