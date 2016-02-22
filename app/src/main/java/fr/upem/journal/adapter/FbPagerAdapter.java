package fr.upem.journal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.upem.journal.fragment.FbInfoFragment;
import fr.upem.journal.fragment.FbLikedPagesFragment;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Info", "Liked pages"};
    private Context context;

    public FbPagerAdapter(FragmentManager fm, Context context) {
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
                return new FbInfoFragment();
            case 1:
                // Games fragment activity
                return new FbLikedPagesFragment();
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