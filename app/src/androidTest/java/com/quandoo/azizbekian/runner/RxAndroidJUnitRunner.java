package com.quandoo.azizbekian.runner;

import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnitRunner;

import com.quandoo.azizbekian.util.RxIdlingExecutionHook;
import com.quandoo.azizbekian.util.RxIdlingResource;

import rx.plugins.RxJavaPlugins;

/**
 * Runner that registers a Espresso Idling resource that handles waiting for RxJava Observables to finish.
 * <p>
 * <b>WARNING</b>
 * <p>
 * Using this runner will block the tests if the application uses long-lived hot Observables such us event buses, etc.
 * <p>
 * <p>
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class RxAndroidJUnitRunner extends AndroidJUnitRunner {

    @Override

    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        RxIdlingResource rxIdlingResource = new RxIdlingResource();
        RxJavaPlugins.getInstance()
                .registerObservableExecutionHook(new RxIdlingExecutionHook(rxIdlingResource));
        Espresso.registerIdlingResources(rxIdlingResource);
    }
}