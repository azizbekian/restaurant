package com.quandoo.azizbekian.misc;

import android.app.Activity;
import android.app.Application;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Bundle;

/**
 * Is needed to stop the job scheduler, because it is not guaranteed that the activity will
 * receive {@link Activity#onDestroy()} when user swipes the app from the recents list. In that
 * case job scheduler would run indeterminate until draining user's battery. When the number of
 * running activity reaches 0 (this means user minimized the app while being on the MainActivity),
 * we stop the service. The service would start again when activity is resumed.
 * <p>
 * Created on Aug 05, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class QuandooApplicationCallbacks implements Application.ActivityLifecycleCallbacks {

    private Application mApplication;

    public QuandooApplicationCallbacks(Application application) {
        mApplication = application;
    }

    private static int sRunningActivities = 0;

    @Override public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override public void onActivityStarted(Activity activity) {
        ++sRunningActivities;
    }

    @Override public void onActivityResumed(Activity activity) {

    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {
        if (--sRunningActivities == 0) {
            JobScheduler jobScheduler = (JobScheduler) mApplication.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();
        }
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override public void onActivityDestroyed(Activity activity) {

    }

}
