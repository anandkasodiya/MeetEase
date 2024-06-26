package com.example.meetease.appUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(VariableBag.preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setKeyValueString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getKeyValueString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public void removePreferenceValue(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearPreferenceValue() {
        editor.clear();
        editor.commit();
    }

    public void setKeyValueBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getKeyValueBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
