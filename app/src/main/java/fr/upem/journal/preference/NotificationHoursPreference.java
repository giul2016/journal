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
    public static final HashSet<String> DEF_VALUES = new HashSet<>(Arrays.asList("8", "12", "20"));

    public NotificationHoursPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> defaultValues = defaultSharedPreferences.getStringSet(context.getResources().getString(R.string.prefNotificationAllHoursKey), DEF_VALUES);
        for(String hour : defaultValues) {
            entries.add(hour + ":00");
        }

        updateEntries();
    }

    public static void addHour(Context context, String hour) {
        entries.add(hour);
        String allHoursKey = context.getResources().getString(R.string.prefNotificationAllHoursKey);
        String selectedHoursKey = context.getResources().getString(R.string.prefNotificationSelectedHoursKey);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Set<String> values = sharedPreferences.getStringSet(allHoursKey, DEF_VALUES);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        values.add(hour.split(":")[0]);
        editor.putStringSet(allHoursKey, values).apply();

        values = sharedPreferences.getStringSet(selectedHoursKey, DEF_VALUES);
        editor = sharedPreferences.edit();
        values.add(hour.split(":")[0]);
        editor.putStringSet(selectedHoursKey, values).apply();
    }

    public static void updateValues(Context context, Set<String> values) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(context.getResources().getString(R.string.prefNotificationSelectedHoursKey), values).apply();
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

        //updateValues(getContext(), getValues());

        updateEntries();
        Log.d("SELECT", "on create dialog");

        return super.onCreateDialogView();
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        updateValues(getContext(), getValues());

        Log.d("SELECT", "dialog closed");
    }
}
