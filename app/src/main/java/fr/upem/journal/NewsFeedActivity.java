package fr.upem.journal;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NewsFeedFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        adapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(), NewsFeedActivity.this, initialData());

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private ArrayList<NewsCategory> initialData() {
        ArrayList<NewsCategory> categories = new ArrayList<>();

        NewsCategory general = new NewsCategory("General");
        general.addFeed("http://www.lemonde.fr/m-actu/rss_full.xml");

        NewsCategory international = new NewsCategory("International");
        international.addFeed("http://www.lemonde.fr/japon/rss_full.xml");
        international.addFeed("http://www.lemonde.fr/europe/rss_full.xml");
        international.addFeed("http://www.lemonde.fr/ameriques/rss_full.xml");
        international.addFeed("http://www.lemonde.fr/asie-pacifique/rss_full.xml");

        NewsCategory sport = new NewsCategory("Sport");
        sport.addFeed("http://www.lemonde.fr/sport/rss_full.xml");

        NewsCategory technology = new NewsCategory("Technology");
        technology.addFeed("http://www.lemonde.fr/technologies/rss_full.xml");

        NewsCategory gaming = new NewsCategory("Gaming");
        gaming.addFeed("http://www.gamekult.com/feeds/actu.html");
        gaming.addFeed("http://www.lemonde.fr/jeux-video/rss_full.xml");

        NewsCategory culture = new NewsCategory("Culture");
        culture.addFeed("http://www.lemonde.fr/culture/rss_full.xml");

        categories.add(general);
        categories.add(international);
        categories.add(sport);
        categories.add(technology);
        categories.add(gaming);
        categories.add(culture);

        return categories;
    }
}
