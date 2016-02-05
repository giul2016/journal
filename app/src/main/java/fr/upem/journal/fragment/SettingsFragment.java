package fr.upem.journal.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import fr.upem.journal.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
