package com.quandoo.azizbekian.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.quandoo.azizbekian.QuandooApplication;
import com.quandoo.azizbekian.injection.component.ActivityComponent;
import com.quandoo.azizbekian.injection.component.ConfigPersistentComponent;
import com.quandoo.azizbekian.injection.component.DaggerConfigPersistentComponent;
import com.quandoo.azizbekian.injection.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "activity_id";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creates ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(QuandooApplication.getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) sComponentsMap.remove(mActivityId);
        mActivityComponent = null;
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

}
