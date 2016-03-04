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

public class TwitterUserInfoFragment extends android.support.v4.app.Fragment {

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

    private class TweetListActivity extends ListActivity {

        List<Long> tweetIds = Arrays.asList(503435417459249153L, 510908133917487104L, 473514864153870337L, 477788140900347904L);
        final TweetViewFetchAdapter adapter =
                new TweetViewFetchAdapter<CompactTweetView>(TweetListActivity.this);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_twitter);
                setListAdapter(adapter);
                adapter.setTweetIds(tweetIds, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        String[] tweets = new String[result.data.size()];
                        int count = 0;
                        for(Tweet tweet : result.data){
                            tweets[count] = tweet.toString();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TweetListActivity.this,
                                android.R.layout.simple_list_item_1, tweets);
                        ((ListView) findViewById(R.id.leftDrawer)).setAdapter(adapter);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast.makeText(...).show();
                    }
                });
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
        try {
            if(user == null){
                throw new NullPointerException("User not instantiate");
            }
            new TimelineActivity(user.name).onCreate(savedInstanceState);
        }
        catch (Exception e){

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
        TwitterAuthToken authToken = session.getAuthToken();
        getUserAccount();
    }

    private void getUserAccount() {
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



}
