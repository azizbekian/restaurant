package com.quandoo.azizbekian.ui.main;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.ui.base.MvpView;
import com.quandoo.azizbekian.misc.ErrorCause;

import java.util.List;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public interface MainMvpView extends MvpView {

    void setupRecyclerView();

    void showCustomers(List<Customer> customers);

    void showCustomersEmpty();

    void showError(@ErrorCause int errorCause);

    void showProgressBar(boolean show);

    void launchTablesActivity(Customer customer);

    void startDbCleanService();

    void stopDbCleanService();

}
