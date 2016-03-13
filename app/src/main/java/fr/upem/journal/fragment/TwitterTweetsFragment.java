package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import fr.upem.journal.R;

/**
 * TwitterTweetsFragment manage the display all tweets of follower
 */
public class TwitterTweetsFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( !(getArguments()!= null && getArguments().containsKey("FOLLOWER_ID")) ){
            return ;
        }

        UserTimeline timeline = new UserTimeline.Builder().userId( getArguments().getLong("FOLLOWER_ID") ).build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(timeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_tweets, container, false);
    }
}
