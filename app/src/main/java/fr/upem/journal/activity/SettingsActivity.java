package fr.upem.journal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fr.upem.journal.R;
import fr.upem.journal.fragment.SettingsFragment;
import fr.upem.journal.service.NotificationService;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener spcListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("PREFS", key + " has changed");

                if(key.equals(getResources().getString(R.string.prefNotificationActiveKey))) {
                    // start service to update alarm
                    Intent intent = new Intent(SettingsActivity.this, NotificationService.class);
                    startService(intent);
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(spcListener);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
