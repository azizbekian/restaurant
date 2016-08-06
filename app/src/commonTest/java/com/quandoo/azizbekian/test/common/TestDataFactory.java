package com.quandoo.azizbekian.test.common;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class TestDataFactory {

    public static final List<Customer> MOCK_CUSTOMER_LIST = new ArrayList<>();

    static {
        MOCK_CUSTOMER_LIST.add(Customer.create("Andrea", "Apriedi", 1));
        MOCK_CUSTOMER_LIST.add(Customer.create("Bernie", "Butchers", 2));
        MOCK_CUSTOMER_LIST.add(Customer.create("Ciel", "Chatter", 3));
    }

    public static final List<Table> MOCK_RESTAURANT_TABLE_LIST = new ArrayList<>();

    static {
        MOCK_RESTAURANT_TABLE_LIST.add(Table.create(true));
        MOCK_RESTAURANT_TABLE_LIST.add(Table.create(false));
    }

}
