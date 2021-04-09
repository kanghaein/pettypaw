package com.example.pettypaw_1;

import androidx.preference.PreferenceFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import android.widget.Toast;

public class Preference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

    }
}

