package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import fr.upem.journal.R;

public class TwitterTweetsFragment extends ListFragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView userName_tv;
    private TwitterSession session;
    private User user;

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
        return inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
    }

    public void setUser(User user){
        this.user = user;
    }

    public void init() {
        session = Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            return;
        }

        initUserAccount();
    }

    private void initUserAccount() {
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
            }

            @Override
            public void success(Result<User> userResult) {
                setUser(userResult.data);
            }
        });
    }
}
