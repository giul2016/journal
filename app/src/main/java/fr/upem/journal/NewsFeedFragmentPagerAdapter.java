package fr.upem.journal;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsFeedFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int pagesCount = 5;
    private String tabTitles[] = new String[]{"General", "Sport", "Technology", "Gaming", "Culture"};
    private String feeds[] = new String[]{
            "http://www.lemonde.fr/m-actu/rss_full.xml",
            "http://www.lemonde.fr/sport/rss_full.xml",
            "http://www.lemonde.fr/technologies/rss_full.xml",
            "http://www.gamekult.com/feeds/actu.html",
            "http://www.lemonde.fr/culture/rss_full.xml"
    };
    private Context context;

    public NewsFeedFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsFeedFragment.newInstance(feeds[position]);
    }

    @Override
    public int getCount() {
        return pagesCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
