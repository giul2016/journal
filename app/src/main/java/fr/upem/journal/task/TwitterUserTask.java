package fr.upem.journal.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import fr.upem.journal.R;
import fr.upem.journal.fragment.TwitterUserInfoFragment;

/**
 * TwitterUserTask wait that current user has ready, and print data in UI
 */
public class TwitterUserTask extends AsyncTask<Void,Void,Void>{
    public static final AtomicReference<User> CURRENT_USER = new AtomicReference<>(null);
    private final TwitterUserInfoFragment main;

    /**
     * Constructor of TwitterUserTask
     * @param main the fragment who call this constructor
     */
    public TwitterUserTask(TwitterUserInfoFragment main){
        this.main = main;
    }

    @Override
    protected Void doInBackground(Void... params) {


        while(TwitterUserTask.CURRENT_USER.get() == null){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        User user = TwitterUserTask.CURRENT_USER.get();

        final StringBuilder list = new StringBuilder();
        list.append("Your name : ").append(user.name).append('\n');
        list.append("Your screen Name : ").append(user.screenName).append('\n');
        list.append("Description : ").append( (user.description.isEmpty())?"no data":user.description ).append('\n');
        list.append("Number of friends : ").append(user.friendsCount).append('\n');
        list.append("Number of followers : ").append(user.followersCount).append('\n');

        try {
            final Bitmap image = getBitmapFromURL(user.profileImageUrl);
            main.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) main.getView().findViewById(R.id.imageView)).setImageBitmap( image );
                }
            });
        } catch (IOException e) {
        }

        main.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ((TextView) main.getView().findViewById(R.id.test)).setText(list.toString());
            }
        });


        return null;
    }

    /**
     * Download the image from the URL taken in arguments
     *
     * @param src the URL
     * @return an Bitmap object
     * @throws IOException if the URL or download failed
     */
    private static Bitmap getBitmapFromURL(String src) throws IOException {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
}
