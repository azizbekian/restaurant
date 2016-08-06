package com.quandoo.azizbekian.data.remote;


import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public interface QuandooApi {

    String ENDPOINT = "https://s3-eu-west-1.amazonaws.com/";

    interface Customers {
        /**
         * Fetches the list of customers.
         */
        @GET("/quandoo-assessment/customer-list.json") Observable<List<Customer>> getCustomers();
    }

    interface Tables {
        /**
         * Fetches the list of tables.
         */
        @GET("/quandoo-assessment/table-map.json") Observable<List<Table>> getTables();
    }

}
