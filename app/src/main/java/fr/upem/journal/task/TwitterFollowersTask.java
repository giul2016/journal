package fr.upem.journal.task;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.upem.journal.activity.TwitterActivity;
import fr.upem.journal.adapter.TwitterPagerAdapter;
import fr.upem.journal.service.TwitterFollowersService;

/**
 * Asynchronous class for wait the list of followers and display it in UI.
 */
public class TwitterFollowersTask extends AsyncTask<TwitterActivity,Void,Void> {
    public static final AtomicBoolean FOLLOWERS_DOWNLOADED = new AtomicBoolean(false);
    private final ViewPager viewPager;
    private final TwitterFollowersService service;
    private final FragmentManager fm;
    private final TabLayout tabLayout;

    /**
     * Constructor of TwitterFollowersTask
     *
     * @param viewPager the frame
     * @param tabLayout layout content
     * @param service followers service
     * @param fm manager of fragment
     */
    public TwitterFollowersTask(ViewPager viewPager,TabLayout tabLayout, TwitterFollowersService service, FragmentManager fm){
        this.viewPager = viewPager;
        this.service = service;
        this.tabLayout = tabLayout;
        this.fm = fm;
    }

    @Override
    protected Void doInBackground(final TwitterActivity... params) {
        if(params.length != 1){
            return null;
        }
        while (!FOLLOWERS_DOWNLOADED.get()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        params[0].runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(new TwitterPagerAdapter(service, fm));
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return null;
    }
}
