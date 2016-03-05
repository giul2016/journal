package fr.upem.journal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.upem.journal.fragment.FbInfoFragment;
import fr.upem.journal.fragment.FbLikedPagesFragment;
import fr.upem.journal.fragment.FbUserFeedFreagment;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Feed", "Liked pages", "Info"};
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
                return new FbUserFeedFreagment();
            case 1:
                return new FbLikedPagesFragment();
            case 2:
                return new FbInfoFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}