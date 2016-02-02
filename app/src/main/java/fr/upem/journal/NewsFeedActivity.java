package fr.upem.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter"};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NewsFeedFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        adapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(), NewsFeedActivity.this, initialData());

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(NewsFeedActivity.this, FacebookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(NewsFeedActivity.this, TwitterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<NewsCategory> initialData() {
        ArrayList<NewsCategory> categories = new ArrayList<>();

        NewsCategory general = new NewsCategory("General");
        general.addFeed(new NewsFeed("Le Monde", "http://www.lemonde.fr/m-actu/rss_full.xml"));

        NewsCategory international = new NewsCategory("International");
        international.addFeed(new NewsFeed("Le Monde - Japon", "http://www.lemonde.fr/japon/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Europe", "http://www.lemonde.fr/europe/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Amériques", "http://www.lemonde.fr/ameriques/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Asie-Pacifique", "http://www.lemonde" +
                ".fr/asie-pacifique/rss_full.xml"));

        NewsCategory sport = new NewsCategory("Sport");
        sport.addFeed(new NewsFeed("Le Monde - Sport", "http://www.lemonde.fr/sport/rss_full.xml"));

        NewsCategory technology = new NewsCategory("Technology");
        technology.addFeed(new NewsFeed("Le Monde - Technologies", "http://www.lemonde.fr/technologies/rss_full.xml"));

        NewsCategory gaming = new NewsCategory("Gaming");
        gaming.addFeed(new NewsFeed("Gamekult", "http://www.gamekult.com/feeds/actu.html"));
        gaming.addFeed(new NewsFeed("Le Monde - Jeux Vidéo", "http://www.lemonde.fr/jeux-video/rss_full.xml"));

        NewsCategory culture = new NewsCategory("Culture");
        culture.addFeed(new NewsFeed("Le Monde - Culture", "http://www.lemonde.fr/culture/rss_full.xml"));

        categories.add(general);
        categories.add(international);
        categories.add(sport);
        categories.add(technology);
        categories.add(gaming);
        categories.add(culture);

        return categories;
    }
}
