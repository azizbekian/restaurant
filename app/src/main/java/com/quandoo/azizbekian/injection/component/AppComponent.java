package com.quandoo.azizbekian.injection.component;

import android.content.Context;

import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.injection.ApplicationContext;
import com.quandoo.azizbekian.injection.module.BuildTypeModule;
import com.quandoo.azizbekian.injection.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on Aug 01, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
@Component(modules = {AppModule.class, BuildTypeModule.class})
public interface AppComponent {

    /**
     * @return The {@link com.quandoo.azizbekian.BaseApplication BaseApplication} context.
     */
    @ApplicationContext Context getApplicationContext();

    DataManager getDataManager();

}
