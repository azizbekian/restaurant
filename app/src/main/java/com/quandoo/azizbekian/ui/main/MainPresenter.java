package com.quandoo.azizbekian.ui.main;

import android.support.annotation.Nullable;

import com.quandoo.azizbekian.QuandooApplication;
import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.injection.ConfigPersistent;
import com.quandoo.azizbekian.ui.base.BasePresenter;
import com.quandoo.azizbekian.utils.ErrorUtils;
import com.quandoo.azizbekian.utils.NetworkUtils;
import com.quandoo.azizbekian.utils.RxUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    public static final int PERIOD_TIME = 10 * 60 * 1000; /* 10 minutes */

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Nullable private List<Customer> mCustomers;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override public void create(MainMvpView mvpView) {
        super.create(mvpView);

        getMvpView().setupRecyclerView();
        loadCustomers();
        clearDataIfNeeded();
    }

    /**
     * Removes retained data if the {@value MainPresenter#PERIOD_TIME} seconds have elapsed since
     * latest data retrieval.
     */
    private void clearDataIfNeeded() {
        if (mDataManager.latestUpdateTime() + PERIOD_TIME < System.currentTimeMillis()) {
            mDataManager.clearLocalData();
        }
    }

    @Override public void resume() {
        super.resume();

        getMvpView().startDbCleanService();
    }

    @Override public void detachView(boolean isChangingConfigurations) {
        if (!isChangingConfigurations) getMvpView().stopDbCleanService();

        super.detachView(isChangingConfigurations);
        // if the activity is finishing -> release resources
        if (null != mSubscription && !isChangingConfigurations) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    /**
     * Loads customer list.
     */
    public void loadCustomers() {
        if (null == mCustomers) { /* if hasn't received data */
            final boolean isConnected = NetworkUtils.isConnected(QuandooApplication.getContext());
            if (null == mSubscription || mSubscription.isUnsubscribed()) {
                // fetch only if we're not subscribed, otherwise could trigger another network call
                // whilst the first hasn't been completed (after orientation change)
                getMvpView().showProgressBar(true);
                mSubscription = mDataManager.getCustomers(!isConnected)
                        .compose(RxUtils.applyIoToMainThreadSchedulers())
                        .subscribe(customers -> {
                            if (isViewAttached()) {
                                getMvpView().showProgressBar(false);
                                // when fetched empty data from db
                                if (customers.isEmpty() && !isConnected) {
                                    getMvpView().showError(ErrorUtils.ERROR_INTERNET);
                                } else {
                                    mCustomers = customers;
                                    showCustomers(mCustomers);
                                }
                            }
                        }, throwable -> {
                            Timber.e("%s", throwable.getMessage());
                            if (isViewAttached()) {
                                getMvpView().showProgressBar(false);
                                getMvpView().showError(isConnected ? ErrorUtils.ERROR_SERVER
                                        : ErrorUtils.ERROR_INTERNET);
                            }
                        });
            } else if (null != mSubscription || !mSubscription.isUnsubscribed()) {
                if (!isConnected) getMvpView().showError(ErrorUtils.ERROR_SERVER);
                    // Corner case for showing progress bar: network request has been made user rotates
                    // device -> the progress bar should be shown
                else getMvpView().showProgressBar(true);
            }
        } else {
            showCustomers(mCustomers);
        }

    }

    public void queryTextChanged(String query) {
        getMvpView().showCustomers(filter(mCustomers, query));
    }

    private void showCustomers(List<Customer> customers) {
        if (customers.isEmpty()) getMvpView().showCustomersEmpty();
        else getMvpView().showCustomers(customers);
    }

    public void onCustomerClicked(Customer customer) {
        getMvpView().stopDbCleanService(); /* stop cleaning db, resume when came back from TablesActivity */
        getMvpView().launchTablesActivity(customer);
    }

    /**
     * Filters the {@code customers} with the {@code query}.
     */
    public static List<Customer> filter(List<Customer> customers, String query) {
        if (null == customers || customers.size() == 0) return Collections.<Customer>emptyList();
        query = query.toLowerCase();

        final List<Customer> filteredModelList = new ArrayList<>();
        for (Customer model : customers) {
            final String firstName = model.customerFirstName().toLowerCase();
            final String lastName = model.customerLastName().toLowerCase();
            if (firstName.contains(query) || lastName.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
