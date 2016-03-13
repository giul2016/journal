package fr.upem.journal.service;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.upem.journal.task.TwitterFollowersTask;
import retrofit.http.GET;
import retrofit.http.Query;


public class TwitterFollowersService {
    /**
     * Followers contain the list of friends
     */
    private class Followers {
        @SerializedName("users")
        public final List<User> users;

        public Followers(List<User> users) {
            this.users = users;
        }
    }

    /**
     * JournalTwitterApiClient extends TwitterApiClient for generate the GET request to twitter service
     */
    private class JournalTwitterApiClient extends TwitterApiClient {
        public JournalTwitterApiClient(TwitterSession session) {
            super(session);
        }

        /**
         * Get the result of GET request
         * @return CustomService, the result of GET request
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }

    /**
     * Interface of GET request to twitter
     */
    interface CustomService {
        @GET("/1.1/friends/list.json")
        void show(@Query("user_id") Long userId, @Query("screen_name") String
                var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2, @Query("count") Integer var3, Callback<Followers> cb);
    }

    private List<User> users = new ArrayList<>();

    /**
     * Constructor of TwitterFollowersService
     * @param session, the current session
     */
    public TwitterFollowersService(TwitterSession session) {

        JournalTwitterApiClient client = new JournalTwitterApiClient(session);
        client.getCustomService().show(session.getUserId(), null, true, true, 100, new Callback<Followers>() {
            @Override
            public void success(Result<Followers> result) {
                for (User user : result.data.users) {
                    users.add(user);
                }
                TwitterFollowersTask.FOLLOWERS_DOWNLOADED.set(true);
            }

            @Override
            public void failure(TwitterException e) {
                Log.e("Twitter followers", "Failure of request");
            }
        });
    }

    /**
     * Get current user's list of followers
     * @return unmodifiable list of followers
     */
    public List<User> getFollowers() {
        return Collections.unmodifiableList(users);
    }

}