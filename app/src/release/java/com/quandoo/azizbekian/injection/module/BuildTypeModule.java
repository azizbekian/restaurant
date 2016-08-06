package com.quandoo.azizbekian.injection.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Module
public class BuildTypeModule {

    @Provides @Singleton
    public OkHttpClient.Builder provideOkHttpClient() {
        return new OkHttpClient.Builder();
    }

}
