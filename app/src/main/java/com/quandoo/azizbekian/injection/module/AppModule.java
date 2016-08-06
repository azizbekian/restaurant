package com.quandoo.azizbekian.injection.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.data.DataManagerImpl;
import com.quandoo.azizbekian.data.local.DatabaseHelper;
import com.quandoo.azizbekian.data.local.PreferencesHelper;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.data.remote.ApiInteractor;
import com.quandoo.azizbekian.data.remote.QuandooApi;
import com.quandoo.azizbekian.injection.ApplicationContext;
import com.ryanharter.auto.value.gson.AutoValueGsonTypeAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on Aug 01, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Module
public class AppModule {

    private static final int DURATION_CONNECTION_TIMEOUT = 10;
    private static final int DURATION_WRITE_TIMEOUT = 10;
    private static final int DURATION_READ_TIMEOUT = 30;

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides @ApplicationContext
    public Context provideAppContext() {
        return mContext;
    }

    @Provides @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpBuilder) {
        okHttpBuilder.connectTimeout(DURATION_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DURATION_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DURATION_READ_TIMEOUT, TimeUnit.SECONDS);
        return okHttpBuilder.build();
    }

    @Provides @Singleton
    public QuandooApi.Customers provideCustomersApi(OkHttpClient okHttpClient) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
                .create();

        return createRetrofit(okHttpClient, gson).create(QuandooApi.Customers.class);
    }

    @Provides @Singleton
    public QuandooApi.Tables provideTablesApi(OkHttpClient okHttpClient) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Table.class, (JsonDeserializer<Table>)
                        (json, typeOfT, context) -> Table.create(json.getAsBoolean()))
                .create();

        return createRetrofit(okHttpClient, gson).create(QuandooApi.Tables.class);
    }

    @Provides @Singleton
    public DataManager provideDataManager(ApiInteractor apiInteractor,
                                          DatabaseHelper databaseHelper,
                                          PreferencesHelper preferencesHelper) {
        return new DataManagerImpl(apiInteractor, databaseHelper, preferencesHelper);
    }

    /**
     * Constructs {@link Retrofit} instance with provided {@code okHttpClient} and {@code gson}.
     */
    private Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(QuandooApi.ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

}
