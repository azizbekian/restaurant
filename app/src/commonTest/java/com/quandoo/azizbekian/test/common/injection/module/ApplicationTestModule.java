package com.quandoo.azizbekian.test.common.injection.module;

import android.app.Application;
import android.content.Context;

import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.data.DataManagerImpl;
import com.quandoo.azizbekian.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Module
public class ApplicationTestModule {

    private final Application mApplication;

    public ApplicationTestModule(Application application) {
        mApplication = application;
    }

    @Provides Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton DataManager provideDataManager() {
        return mock(DataManagerImpl.class);
    }

}
