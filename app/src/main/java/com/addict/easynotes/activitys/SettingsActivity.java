package com.addict.easynotes.activitys;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.addict.easynotes.R;
import com.addict.easynotes.utils.ToastUtils;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }


    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preferences);

//            findPreference("about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    if (preference.getKey().equals("about")) {
//                        ToastUtils.showShort("this is about");
//                    }
//                    return true;
//                }
//            });
            findPreference("about").setOnPreferenceClickListener(mPreferenceClickListener);
            bindPreferenceSummaryToValue(findPreference("gravity"));

        }

        private static Preference.OnPreferenceClickListener mPreferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("about")) {
                    ToastUtils.showShort("this is about");
                }
                return true;
            }
        };

        private static Preference.OnPreferenceChangeListener mPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                } else {
                    preference.setSummary(stringValue);
                }

                return true;
            }
        };

        private static void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes
            preference.setOnPreferenceChangeListener(mPreferenceChangeListener);
            // Trigger the listener immediately with the current value
            mPreferenceChangeListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }


    }
}
