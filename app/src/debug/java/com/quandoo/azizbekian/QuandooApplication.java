package com.quandoo.azizbekian;

import com.facebook.stetho.Stetho;

/**
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class QuandooApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initStetho();
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

}
