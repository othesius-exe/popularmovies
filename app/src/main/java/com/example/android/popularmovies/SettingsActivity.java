package com.example.android.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 *
 */

public class SettingsActivity extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_main);
        ListPreference listPreference = (ListPreference) findPreference("Sort");
        CharSequence currentSort = listPreference.getEntry();
        String currentValue = listPreference.getValue();
    }

    private void bindPreferenceSummaryToalu(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange
                (preference, PreferenceManager.getDefaultSharedPreferences
                        (preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String prefValue = newValue.toString();

        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(prefValue);

        if (index >= 0) {
            preference.setSummary(listPreference.getEntries()[index]);
        } else {
            preference.setSummary(prefValue);
        }

        return true;
    }
}
