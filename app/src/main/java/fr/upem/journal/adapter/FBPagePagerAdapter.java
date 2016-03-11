package fr.upem.journal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.upem.journal.R;
import fr.upem.journal.fragment.FbInfoFragment;
import fr.upem.journal.fragment.FbLikedPagesFragment;
import fr.upem.journal.fragment.FbPageFeedFragment;
import fr.upem.journal.fragment.FbPageInfoFragment;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FBPagePagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Context context;

    public FBPagePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{context.getString(R.string.fb_page_feed), context.getString(R.string.fb_page_info)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FbPageFeedFragment();
            case 1:
                return new FbPageInfoFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}