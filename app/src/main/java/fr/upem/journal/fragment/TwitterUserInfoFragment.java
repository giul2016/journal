package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import fr.upem.journal.R;
import fr.upem.journal.task.TwitterUserTask;

/**
 * TwitterUserInfoFragment display the user's data in layout
 */
public class TwitterUserInfoFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Twitter.getApiClient(Twitter.getSessionManager().getActiveSession()).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        TwitterUserTask.CURRENT_USER.set(userResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }

                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new TwitterUserTask(this).execute();
    }
}
