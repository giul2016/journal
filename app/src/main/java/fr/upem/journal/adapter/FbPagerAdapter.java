package fr.upem.journal.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.upem.journal.R;
import fr.upem.journal.fragment.FbInfoFragment;
import fr.upem.journal.fragment.FbLikedPagesFragment;
import fr.upem.journal.fragment.FbUserFeedFreagment;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private Context context;
    private String tabTitles[];


    public FbPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{context.getString(R.string.fb_user_feed), context.getString(R.string.fb_user_likes_page),context.getString(R.string.fb_user_info)};
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