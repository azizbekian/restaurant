package com.quandoo.azizbekian.data;

import com.quandoo.azizbekian.data.local.DatabaseHelper;
import com.quandoo.azizbekian.data.local.PreferencesHelper;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.data.remote.ApiInteractor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
public class DataManagerImpl implements DataManager {

    private final ApiInteractor mApiInteractor;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManagerImpl(ApiInteractor apiInteractor,
                           DatabaseHelper databaseHelper,
                           PreferencesHelper preferencesHelper) {
        mApiInteractor = apiInteractor;
        mDatabaseHelper = databaseHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public Observable<List<Customer>> getCustomers(boolean fromLocal) {
        if (fromLocal && !mDatabaseHelper.isCustomersEmpty()) {
            return mDatabaseHelper.getCustomers();
        } else return mApiInteractor.getCustomers()
                .concatMap(customers -> {
                    if (null != customers && !customers.isEmpty()) {
                        mDatabaseHelper.setCustomers(customers);
                    }
                    return Observable.just(customers);
                });
    }

    @Override
    public Observable<List<Table>> getTables(boolean fromLocal) {
        if (fromLocal && !mDatabaseHelper.isRestaurantTablesEmpty()) {
            return mDatabaseHelper.getRestaurantTables();
        } else return mApiInteractor.getTables()
                .concatMap(tables -> {
                    if (null != tables && !tables.isEmpty()) {
                        mDatabaseHelper.setRestaurantTables(tables);
                        mPreferencesHelper.putLatestUpdateTimeMillis();
                    }
                    return Observable.just(tables);
                });

    }

    @Override public void updateRestaurantTable(int position, boolean isAvailable) {
        mDatabaseHelper.updateRestaurantTable(position + 1 /* db is 1-based, not 0-based */, isAvailable);
    }

    @Override public void clearLocalData() {
        mDatabaseHelper.clearTables();
        mPreferencesHelper.clear();
        mPreferencesHelper.putLatestUpdateTimeMillis();
    }

    @Override public long latestUpdateTime() {
        return mPreferencesHelper.getLatestUpdateTimeMillis();
    }

}
