package fr.upem.journal.fragment;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import fr.upem.journal.R;
import fr.upem.journal.activity.TwitterActivity;

public class TwitterUserInfoFragment extends android.support.v4.app.Fragment {

    /**
     * Class who take the username and get tweets
     */
    private class TimelineActivity extends ListActivity {
        private final String user;

        TimelineActivity(String user){
            this.user = user;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_twitter_user_info);

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName(this.user)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            setListAdapter(adapter);
        }
    }

    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView userName_tv;
    private TwitterSession session;
    private User user;

    public static TwitterUserInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TwitterUserInfoFragment fragment = new TwitterUserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve the USER_ID and USER_NAME in data stocked by android
        if( savedInstanceState != null && savedInstanceState.containsKey("USER_ID") && savedInstanceState.containsKey("USER_NAME") ){
            session = new TwitterSession( new TwitterAuthToken( TwitterActivity.TWITTER_KEY , TwitterActivity.TWITTER_SECRET ), savedInstanceState.getLong("USER_ID"), savedInstanceState.getString("USER_NAME") );
            new TimelineActivity(session.getUserName()).onCreate(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
       // userName_tv = (TextView) view.findViewById(R.id.twitter_userName_tv);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        session = Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            return ;
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

/*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
*/
}
