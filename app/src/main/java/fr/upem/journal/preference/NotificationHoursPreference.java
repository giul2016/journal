package fr.upem.journal.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.upem.journal.R;

/**
 * Created by Bastien on 06/03/2016.
 */
public class NotificationHoursPreference extends MultiSelectListPreference {

    private static final Set<String> entries = new HashSet<>();

    public NotificationHoursPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> defaultValues = defaultSharedPreferences.getStringSet(context.getResources().getString(R.string.prefNotificationHoursKey), new HashSet<>(Arrays.asList("8", "12", "20")));
        for(String hour : defaultValues) {
            entries.add(hour + ":00");
        }

        updateEntries();
    }

    public static void addHour(String hour) {
        entries.add(hour);
    }

    public static void updateValues(Context context) {
        Set<String> values = new HashSet<>();
        //values.clear();
        for(String hour : entries) {
            Log.d("SELECT", "hour : "+hour + " ("+hour.split(":")[0]+")");
            values.add(hour.split(":")[0]);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(context.getResources().getString(R.string.prefNotificationHoursKey), values).apply();
    }

    private void updateEntries() {
        CharSequence[] entriesCS = new CharSequence[entries.size()];
        CharSequence[] valuesCS = new CharSequence[entries.size()];

        int i = 0;
        for(String hour : entries) {
            entriesCS[i] = hour;
            valuesCS[i] = hour.split(":")[0];
            i++;
        }

        setEntries(entriesCS);
        setEntryValues(valuesCS);
    }

    @Override
    protected View onCreateDialogView() {

        updateValues(getContext());

        updateEntries();
        Log.d("SELECT", "on create dialog");

        return super.onCreateDialogView();
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        updateValues(getContext());

        updateEntries();
        Log.d("SELECT", "on bind dialog");
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        Log.d("SELECT", "dialog closed");
    }
}
