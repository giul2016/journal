package fr.upem.journal.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.InputStream;

import fr.upem.journal.R;

/**
 * Created by TTTH on 3/1/2016.
 */
public class TwitterUserInfoFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView userName_tv;
    private TwitterSession session;

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
        userName_tv = (TextView) view.findViewById(R.id.twitter_userName_tv);

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
        TwitterAuthToken authToken = session.getAuthToken();
//        Log.e("session", session.toString());
//        Log.e("auth tocken", authToken.toString());
        String token = authToken.token;
        String secret = authToken.secret;
        if (session != null) {
            Twitter.getApiClient(session).getAccountService()
                    .verifyCredentials(true, false, new Callback<User>() {

                        @Override
                        public void failure(TwitterException e) {

                        }

                        @Override
                        public void success(Result<User> userResult) {

                            User user = userResult.data;
                            String twitterImage = user.profileImageUrl;

                            try {
//                                Log.e("imageurl", user.profileImageUrl);
//                                Log.e("name", user.name);
//                                //Log.d("email",user.email);
//                                Log.e("des", user.description);
//                                Log.e("followers ", String.valueOf(user.followersCount));
//                                Log.e("createdAt", user.createdAt);

                                userName_tv.setText(user.name);
                                new DownloadImageTask((ImageView) getView().findViewById(R.id.twitter_user_picture_iv))
                                        .execute(user.profileImageUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
        }
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
