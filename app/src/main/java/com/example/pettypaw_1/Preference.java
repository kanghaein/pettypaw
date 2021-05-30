package com.example.pettypaw_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

// 푸시 알림 받을 날짜 설정
public class Preference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Load_alarm();
    }

    private void Load_alarm() {
        // prefs.xml에서 "alarm"으로 설정한 key 받아온다.
        ListPreference LP = (ListPreference) findPreference("alarm");

        //ListPreference를 이용하여 푸시 알림 받을 날짜를 선택
        LP.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object o) {
                // 각 날짜 선택할 때마다 알려준다.
                String items = (String) o;
                if (prefs.getKey().equals("alarm")) {
                    switch (items) {
                        case "7":
                            Toast.makeText(getApplicationContext(), "7일 전에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "5":
                            Toast.makeText(getApplicationContext(), "5일 전에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "3":
                            Toast.makeText(getApplicationContext(), "3일 전에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                        case "1":
                            Toast.makeText(getApplicationContext(), "1일 전에 푸시알람", Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                return true;
            }
        });
    }

    // 액티비티 활성화된 상태에서 Load_alarm() 함수 호출
    @Override
    protected void onResume() {
        Load_alarm();
        super.onResume();
    }
}