package fr.upem.journal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fr.upem.journal.newsfeed.NewsCategory;
import fr.upem.journal.fragment.NewsFeedFragment;

public class NewsFeedFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<NewsCategory> categories;
    private Context context;

    public NewsFeedFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<NewsCategory> categories) {
        super(fm);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return NewsFeedFragment.newInstance(categories.get(position).getFeeds());
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getTitle();
    }
}
