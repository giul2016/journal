package fr.upem.journal.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.upem.journal.R;
import fr.upem.journal.adapter.TwitterPagerAdapter;
import fr.upem.journal.service.TwitterFollowersService;

import io.fabric.sdk.android.Fabric;

public class TwitterActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "pB6KGJc38BAKiAYU1pkm9Jjne";
    private static final String TWITTER_SECRET = "wJdqiIuZZEQktaSrCGBYvWOPL5EFJXfGqIWkqWG8QRXh6Ua6zu";
    public static final AtomicBoolean FOLLOWERS_DOWNLOADED = new AtomicBoolean(false);

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter", "Weather", "Settings"};

    TextView textView;
    TwitterSession session;
    private TwitterAuthClient authClient;

    private ActionBarDrawerToggle getDrawerToggle(){
        return new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
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
    }

    private void itemClickListener(){
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if( session == null){
            return ;
        }

        //Save ID and NAME in android's datas
        outState.putLong("USER_ID",session.getUserId());
        outState.putString("USER_NAME", session.getUserName());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
     /*   if( savedInstanceState != null && savedInstanceState.containsKey("USER_ID") && savedInstanceState.containsKey("USER_NAME") ){
            findViewById(R.id.twitter_login_button).setVisibility(View.GONE);
            session = new TwitterSession(new TwitterAuthToken(TWITTER_KEY,TWITTER_SECRET), savedInstanceState.getLong("USER_ID"), savedInstanceState.getString("USER_NAME") );
            followersService = new TwitterFollowersService(session,session.getUserId());
        }*/
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_twitter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = getDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        itemClickListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textView = (TextView) findViewById(R.id.tv_username);

        // loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //callbackTwitterButton();

        if( Twitter.getSessionManager().getActiveSession() == null){
            initAuthClient(this);
        }
        else{
            instantiatePageAdapter( Twitter.getSessionManager().getActiveSession() );
        }

    }

    private void initAuthClient(final TwitterActivity main){
        authClient = new TwitterAuthClient();
        authClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Twitter.getSessionManager().setActiveSession(result.data);
                instantiatePageAdapter(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void instantiatePageAdapter(TwitterSession session){
        final TwitterFollowersService service = new TwitterFollowersService( session );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!TwitterActivity.FOLLOWERS_DOWNLOADED.get()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                TwitterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_twitter);
                        viewPager.setAdapter(new TwitterPagerAdapter(service, getSupportFragmentManager(), TwitterActivity.this));
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_twitter);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });
            }
        }).start();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_twitter);
        viewPager.setAdapter(new TwitterPagerAdapter(service, getSupportFragmentManager(), TwitterActivity.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_twitter);
        tabLayout.setupWithViewPager(viewPager);
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
        authClient.onActivityResult(requestCode, resultCode, data);

    }
}
