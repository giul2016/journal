package fr.upem.journal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.upem.journal.fragment.FbInfoFragment;
import fr.upem.journal.fragment.FbLikedPagesFragment;
import fr.upem.journal.fragment.FbPageFeedFragment;
import fr.upem.journal.fragment.FbPageInfoFragment;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FBPagePagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Feed", "Page Info"};
    private Context context;

    public FBPagePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new FbPageFeedFragment();
            case 1:
                // Games fragment activity
                return new FbPageInfoFragment();
            case 2:
                // Movies fragment activity
                return new FbInfoFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}