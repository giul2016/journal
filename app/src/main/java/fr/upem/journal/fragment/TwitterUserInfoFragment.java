package fr.upem.journal.fragment;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import fr.upem.journal.R;
import fr.upem.journal.activity.TwitterActivity;

/*public class TwitterUserInfoFragment extends android.support.v4.app.Fragment {


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

    private static class TimelineActivity extends ListFragment {
            private String user;

            public TimelineActivity(String str){
                this.user = str;
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                        .query("#"+user)
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                        .setTimeline(searchTimeline)
                        .build();
                setListAdapter(adapter);
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
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

}*/

public class TwitterUserInfoFragment extends ListFragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(user == null){
            return ;
        }

       /* final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(user.name)
                .build();*/
       /* final CollectionTimeline searchTimeline = new CollectionTimeline.Builder()
                .id(569961150045896704L)
                .build();*/
       /* final UserTimeline searchTimeline = new UserTimeline.Builder()
                .screenName(user.name)
                .build();*/
     //   final UserTimeline searchTimeline = new UserTimeline.Builder().userId(user.id).build();

       /* final CollectionTimeline searchTimeline = new CollectionTimeline.Builder()
                .id(707899176923897856L)
                .build();*/
        final CollectionTimeline searchTimeline = new CollectionTimeline.Builder()
                .id(user.getId())
                .build();


        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(searchTimeline)
                .build();

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
        init();
        return view;
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
