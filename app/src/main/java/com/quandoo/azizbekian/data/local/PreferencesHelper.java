package com.quandoo.azizbekian.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.quandoo.azizbekian.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created on Aug 04, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "quandoo_prefs";

    public static final String KEY_LATEST_UPDATE_TIME_MILLIS = "latest_update_time_millis";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putLatestUpdateTimeMillis() {
        mPref.edit()
                .putLong(KEY_LATEST_UPDATE_TIME_MILLIS, System.currentTimeMillis())
                .apply();
    }

    public long getLatestUpdateTimeMillis() {
        return mPref.getLong(KEY_LATEST_UPDATE_TIME_MILLIS, System.currentTimeMillis());
    }

    /**
     * Clears shared preferences.
     */
    public void clear() {
        mPref.edit().clear().apply();
    }
}
