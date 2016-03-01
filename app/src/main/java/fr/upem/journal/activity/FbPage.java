package fr.upem.journal.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FBPagePagerAdapter;
import fr.upem.journal.adapter.FbPagerAdapter;

public class FbPage extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.fb_page);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_page);
        viewPager.setAdapter(new FBPagePagerAdapter(getSupportFragmentManager(), FbPage.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_page);
        tabLayout.setupWithViewPager(viewPager);

    }
}
