package com.example.pettypaw_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class Preference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Load_alarm();
    }

    private void Load_alarm(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        ListPreference LP = (ListPreference)findPreference("alarm");

        LP.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object o) {

                String items = (String)o;
                if(prefs.getKey().equals("alarm")) {
                    switch (items) {
                        case "7":
                            Toast.makeText(getApplicationContext(), "7시간 후에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "5":
                            Toast.makeText(getApplicationContext(), "5시간 후에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "3":
                            Toast.makeText(getApplicationContext(), "3시간 후에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "1":
                            Toast.makeText(getApplicationContext(), "1시간 후에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        Load_alarm();
        super.onResume();
    }
}
