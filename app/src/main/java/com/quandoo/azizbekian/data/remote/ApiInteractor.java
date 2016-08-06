package com.quandoo.azizbekian.data.remote;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
public class ApiInteractor {

    private QuandooApi.Customers mCustomersApi;
    private QuandooApi.Tables mTablesApi;

    @Inject
    public ApiInteractor(QuandooApi.Customers customersApi, QuandooApi.Tables tablesApi) {
        mCustomersApi = customersApi;
        mTablesApi = tablesApi;
    }

    public Observable<List<Customer>> getCustomers() {
        return mCustomersApi.getCustomers();
    }

    public Observable<List<Table>> getTables() {
        return mTablesApi.getTables();
    }
}
