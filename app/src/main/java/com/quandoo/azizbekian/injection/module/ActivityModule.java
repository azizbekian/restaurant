package com.quandoo.azizbekian.injection.module;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.quandoo.azizbekian.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private FragmentActivity mActivity;

    public ActivityModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides FragmentActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext Context providesContext() {
        return mActivity;
    }

}
