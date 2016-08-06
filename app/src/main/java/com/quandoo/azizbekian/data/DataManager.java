package com.quandoo.azizbekian.data;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;

import java.util.List;

import rx.Observable;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public interface DataManager {

    /**
     * Fetches {@link Customer}s from either network or local storage.
     *
     * @param fromLocal Shows the desire of client from what source to fetch the data. If {@code true} -
     *                  the data would be fetched from local storage (if it exists, otherwise will
     *                  trigger a network call). If false - triggers a network call.
     */
    Observable<List<Customer>> getCustomers(boolean fromLocal);

    /**
     * Fetches {@link Table}s from either network or local storage.
     *
     * @param fromLocal Shows the desire of client from what source to fetch the data. If {@code true} -
     *                  the data would be fetched from local storage (if it exists, otherwise will
     *                  trigger a network call). If false - triggers a network call.
     */
    Observable<List<Table>> getTables(boolean fromLocal);

    /**
     * Updates the {@link Table} at {@code position} to value {@code isAvailable}.
     *
     * @param isAvailable If true - the table is available. False otherwise.
     */
    void updateRestaurantTable(int position, boolean isAvailable);

    /**
     * Clears all local data.
     */
    void clearLocalData();

    /**
     * @return Long value, representing the time in milliseconds, when last time the local data was
     * fetched. Returns {@link System#currentTimeMillis()} when there's no info about latest retrieval.
     */
    long latestUpdateTime();

}
