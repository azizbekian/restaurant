package com.quandoo.azizbekian.ui.tables;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.quandoo.azizbekian.QuandooApplication;
import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.injection.ConfigPersistent;
import com.quandoo.azizbekian.ui.base.BasePresenter;
import com.quandoo.azizbekian.utils.ErrorUtils;
import com.quandoo.azizbekian.utils.NetworkUtils;
import com.quandoo.azizbekian.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@ConfigPersistent
public class TablesPresenter extends BasePresenter<TablesMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Customer mCustomer;

    @Nullable private List<Table> mTables;

    @Inject
    public TablesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override public void create(TablesMvpView mvpView) {
        super.create(mvpView);

        getMvpView().setupRecyclerView();
        loadTables();
    }

    @Override public void detachView(boolean isChangingConfigurations) {
        super.detachView(isChangingConfigurations);
        if (!isChangingConfigurations && null != mSubscription) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    public void loadTables() {
        if (null == mTables) { /* if hasn't received data */
            final boolean isConnected = NetworkUtils.isConnected(QuandooApplication.getContext());
            if (null == mSubscription || mSubscription.isUnsubscribed()) {
                // fetch only if we're not subscribed, otherwise could trigger another network call
                // whilst the first hasn't been completed (after orientation change)
                getMvpView().showProgressBar(true);
                mSubscription = mDataManager.getTables(true)
                        .compose(RxUtils.applyIoToMainThreadSchedulers())
                        .subscribe(tables -> {
                            if (isViewAttached()) {
                                getMvpView().showProgressBar(false);
                                // when fetched empty data from db
                                if (tables.isEmpty() && !isConnected) {
                                    getMvpView().showError(ErrorUtils.ERROR_INTERNET);
                                } else {
                                    mTables = tables;
                                    getMvpView().showTables(mTables);
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
            getMvpView().showTables(mTables);
        }
    }

    public void setCustomer(Intent intent) {
        if (!intent.getExtras().containsKey(TablesActivity.KEY_CUSTOMER)) {
            throw new RuntimeException(Customer.class.getSimpleName() + " object is expected as " +
                    "input to " + TablesActivity.class.getSimpleName() + ".");
        }

        mCustomer = intent.getExtras().getParcelable(TablesActivity.KEY_CUSTOMER);
    }

    @SuppressWarnings("ConstantConditions")
    public void reserveTable(int position) {
        mDataManager.updateRestaurantTable(position, false);
        mDataManager.getTables(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tables -> {
                    if (isViewAttached()) {
                        mTables = tables;
                        getMvpView().showTables(tables);
                    }
                }, throwable -> {
                    Timber.e("Error updating database: %s", throwable.getMessage());
                });
    }

    @SuppressWarnings("ConstantConditions")
    public void onTableClicked(int position) {
        if (!mTables.get(position).isAvailable()) {
            getMvpView().onReservedTableClicked(position);
        } else {
            getMvpView().onAvailableTableClicked(mCustomer, position);
        }
    }

}
