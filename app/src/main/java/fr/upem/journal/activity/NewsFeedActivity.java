package fr.upem.journal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fr.upem.journal.newsfeed.NewsCategory;
import fr.upem.journal.adapter.NewsFeedFragmentPagerAdapter;
import fr.upem.journal.R;
import fr.upem.journal.database.DatabaseHelper;
import fr.upem.journal.service.NotificationService;

public class NewsFeedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter", "Settings"};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NewsFeedFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (isFirstTime()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            databaseHelper.initialData();
        }

        //launch notification service when the app is used
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);

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

        adapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(), NewsFeedActivity.this,
                loadDataFromDatabase());

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(NewsFeedActivity.this, FacebookActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(NewsFeedActivity.this, TwitterActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(NewsFeedActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    private ArrayList<NewsCategory> loadDataFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        return databaseHelper.selectNewsCategories();
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getSharedPreferences("fr.upem.Journal", MODE_PRIVATE);
        if (preferences.getBoolean("first_time", true)) {
            preferences.edit().putBoolean("first_time", false).apply();
            return true;
        } else {
            return false;
        }
    }

}
