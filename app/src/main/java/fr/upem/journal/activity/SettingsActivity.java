package fr.upem.journal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.upem.journal.R;
import fr.upem.journal.fragment.SettingsFragment;
import fr.upem.journal.preference.NotificationHoursPreference;
import fr.upem.journal.service.NotificationService;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("PREFS", key + " has changed");

        String prefNotificationHoursKey = getResources().getString(R.string.prefNotificationHoursKey);
        String prefNotificationActiveKey = getResources().getString(R.string.prefNotificationActiveKey);

        if(key.equals("time_picker")) {

            String selectedHourString = sharedPreferences.getString("time_picker", "8:00");
            NotificationHoursPreference.addHour(selectedHourString);
            NotificationHoursPreference.updateValues(this);

            // start service to update alarm
            Intent intent = new Intent(SettingsActivity.this, NotificationService.class);
            startService(intent);
        }

        if(key.equals(prefNotificationActiveKey) || key.equals(prefNotificationHoursKey)) {

            Set<String> notifHours = sharedPreferences.getStringSet(prefNotificationHoursKey, new HashSet<>(Arrays.asList("8", "12", "20")));
            if(notifHours.isEmpty()) {
                // if there is no hour selected, notifications are disabled
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(prefNotificationActiveKey, false);
                editor.apply();

                // refresh activity
                finish();
                startActivity(getIntent());
            }

            // start service to update alarm
            Intent intent = new Intent(SettingsActivity.this, NotificationService.class);
            startService(intent);
        }
    }
}
