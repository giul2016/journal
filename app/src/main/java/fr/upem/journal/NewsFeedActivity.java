package fr.upem.journal;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class NewsFeedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NewsFeedFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        adapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(), NewsFeedActivity.this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
