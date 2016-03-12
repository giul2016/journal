package fr.upem.journal.adapter;


import android.content.Context;
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
 * Created by TTTH on 3/1/2016.
 */
public class TwitterPagerAdapter extends  FragmentPagerAdapter {

    private final List<User> tabTitles;

    //final int PAGE_COUNT = 2;
    // private String tabTitles[] = new String[] { "Info", "Liked pages"};
    private Context context;

    public TwitterPagerAdapter(TwitterFollowersService service, FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.tabTitles = service.getFollowers();

        System.err.println(tabTitles.size());
        System.err.println("\n\n\nICI\n\n\n");
    }

    @Override
    public int getCount() {
        return tabTitles.size()+1;
    }

    @Override
    public Fragment getItem(int position){
        if( position == 0 ){
            return new TwitterUserInfoFragment();
        }
        if( position > 0 && position < getCount()  ){
            TwitterTweetsFragment tfrag = new TwitterTweetsFragment();
            Bundle args = new Bundle();
            System.err.println("J'envois ça +++" + args.toString());
            args.putLong("FOLLOWER_ID", tabTitles.get(position - 1).getId());

            tfrag.setArguments(args);

            return tfrag;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        if( position == 0 ){
            return "Infos";
        }
        return tabTitles.get(position-1).name;
    }
}