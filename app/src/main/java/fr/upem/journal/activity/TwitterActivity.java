package fr.upem.journal.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import fr.upem.journal.R;
import fr.upem.journal.adapter.TwitterPagerAdapter;
import io.fabric.sdk.android.Fabric;

public class TwitterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter", "Weather", "Settings"};

    private static final String TWITTER_KEY = "DyAJEKsgPQ6L0is9rOlSzpWQb";
    private static final String TWITTER_SECRET = "hsP2rm9qHhBhoXBJmQ23gQwjdFaafulPAzlPm84atc99cYP3KM";

    private TwitterLoginButton loginButton;
    TextView textView;
    TwitterSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_twitter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(TwitterActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(TwitterActivity.this, FacebookActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        // ALREADY SELECTED
                        /*intent = new Intent(TwitterActivity.this, TwitterActivity.class);
                        startActivity(intent);*/
                        break;
                    case 3:
                        intent = new Intent(TwitterActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(TwitterActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        textView = (TextView) findViewById(R.id.tv_username);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;

                String username = session.getUserName();
                Long  userid = session.getUserId();


//                textView.setText("Hi " + username);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_twitter);
        viewPager.setAdapter(new TwitterPagerAdapter(getSupportFragmentManager(), TwitterActivity.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_twitter);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.actionSettings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.actionEditCategories:
                Intent intentEdit = new Intent(this, EditCategoriesActivity.class);
                startActivity(intentEdit);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);

    }
}
