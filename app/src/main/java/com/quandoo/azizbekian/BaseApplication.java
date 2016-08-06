package com.quandoo.azizbekian;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.quandoo.azizbekian.injection.component.AppComponent;
import com.quandoo.azizbekian.injection.component.DaggerAppComponent;
import com.quandoo.azizbekian.injection.module.AppModule;
import com.quandoo.azizbekian.misc.QuandooApplicationCallbacks;

import timber.log.Timber;

/**
 * Created on Aug 01, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class BaseApplication extends MultiDexApplication {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        plantTimber();
        registerActivityLifecycleCallbacks(new QuandooApplicationCallbacks(this));
    }

    private void plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static AppComponent getComponent() {
        return sAppComponent;
    }

    public static Context getContext() {
        return sAppComponent.getApplicationContext();
    }

    public void setComponent(AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    public static QuandooApplication get(Context context) {
        return (QuandooApplication) context.getApplicationContext();
    }

}
