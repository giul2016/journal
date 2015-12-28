package fr.upem.journal;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NewsFeedActivity extends AppCompatActivity {

    NewsFeedFragment newsFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentManager fragmentManager = getFragmentManager();
        newsFeedFragment = (NewsFeedFragment) fragmentManager.findFragmentById(R.id.newsFeedFragment);
        fragmentManager.beginTransaction().replace(R.id.newsFeedFragment, newsFeedFragment).commit();
    }

}
