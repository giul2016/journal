package fr.upem.journal.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

import fr.upem.journal.fragment.TwitterTweetsFragment;
import fr.upem.journal.fragment.TwitterUserInfoFragment;
import fr.upem.journal.service.TwitterFollowersService;

/**
 * TwitterPagerAdapter initialize all fragments
 */
public class TwitterPagerAdapter extends  FragmentPagerAdapter {
    private final List<User> tabTitles;

    /**
     * Constructor of TwitterPagerAdapter who manage all fragments
     *
     * @param service the followers service
     * @param fm the fragment manager
     */
    public TwitterPagerAdapter(TwitterFollowersService service, FragmentManager fm) {
        super(fm);
        this.tabTitles = service.getFollowers();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tabTitles.size()+1;
    }

    @Override
    public Fragment getItem(int position){
        if( position < 0 ){
            throw new IllegalStateException("Position is negative");
        }
        if( position == 0 ){
            return new TwitterUserInfoFragment();
        }
        if( position > 0 && position < getCount()  ){
            TwitterTweetsFragment tfrag = new TwitterTweetsFragment();
            Bundle args = new Bundle();
            args.putLong("FOLLOWER_ID", tabTitles.get(position - 1).getId());

            tfrag.setArguments(args);

            return tfrag;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if( position < 0 ){
            throw new IllegalStateException("Position is negative");
        }
        if( position == 0 ){
            return "Infos";
        }
        if( tabTitles.get(position-1).name == null || tabTitles.get(position-1).name.isEmpty() ){
            return "@";
        }
        return tabTitles.get(position-1).name;
    }
}