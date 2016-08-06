package com.quandoo.azizbekian.ui.main;

import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.misc.DbCleanerJobService;
import com.quandoo.azizbekian.misc.ErrorCause;
import com.quandoo.azizbekian.misc.SearchMenuRunnable;
import com.quandoo.azizbekian.ui.base.BaseActivity;
import com.quandoo.azizbekian.ui.dialog.ErrorDialogFragment;
import com.quandoo.azizbekian.ui.tables.TablesActivity;
import com.quandoo.azizbekian.utils.FragmentUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on Aug 01, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.quandoo.azizbekian.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    public static final String KEY_SEARCH_QUERY = "search_query";

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Inject MainPresenter mMainPresenter;
    @Inject CustomersAdapter mCustomersAdapter;

    @BindView(R.id.recycler_customers) RecyclerView customersRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Nullable SearchView mSearchView;
    @Nullable String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activityComponent().inject(this);

        mMainPresenter.create(this);
    }

    @Override protected void onResume() {
        super.onResume();
        mMainPresenter.resume();
    }

    @Override protected void onDestroy() {
        mMainPresenter.detachView(isChangingConfigurations());
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override public boolean onPrepareOptionsMenu(Menu menu) {

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        // SearchView doesn't retain it's state after orientation change
        // have to handle it the bad way (╯°□°）╯︵ ┻━┻
        boolean isQueryExists = !TextUtils.isEmpty(mSearchQuery);

        if (isQueryExists) {
            // Calling directly doesn't take effect
            // Custom runnable class in order to refrain from context leakage
            new Handler(Looper.getMainLooper()).post(new SearchMenuRunnable(mSearchView, mSearchQuery));
        }

        RxSearchView.queryTextChangeEvents(mSearchView)
                .skip(isQueryExists ? 0 : 1)
                .subscribe(searchViewQueryTextEvent -> {
                    mMainPresenter.queryTextChanged(searchViewQueryTextEvent.queryText().toString());
                });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (isChangingConfigurations() && null != mSearchView && null != mSearchView.getQuery()) {
            outState.putString(KEY_SEARCH_QUERY, mSearchView.getQuery().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(KEY_SEARCH_QUERY)) {
            mSearchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
        }
    }

    @Override public void setupRecyclerView() {
        customersRecyclerView.setHasFixedSize(true);
        customersRecyclerView.setAdapter(mCustomersAdapter);
    }

    @Override public void showCustomers(List<Customer> customers) {
        mCustomersAdapter.animateTo(customers);
        customersRecyclerView.scrollToPosition(0);
    }

    @Override public void showCustomersEmpty() {
        mCustomersAdapter.setCustomers(Collections.<Customer>emptyList());
        mCustomersAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_customers, Toast.LENGTH_SHORT).show();
    }

    @Override public void showError(@ErrorCause int errorCause) {
        Fragment fr = FragmentUtils.findByTag(this, ErrorDialogFragment.TAG);
        if (null == fr) {
            fr = ErrorDialogFragment.newFragment(errorCause);
            FragmentUtils.add(this, fr, ErrorDialogFragment.TAG);
        }
    }

    @Override public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override public void launchTablesActivity(Customer customer) {
        TablesActivity.launch(this, customer);
    }

    @Override public void startDbCleanService() {
        JobInfo.Builder cleaningJob = new JobInfo.Builder(1, new ComponentName(getPackageName(),
                DbCleanerJobService.class.getName()));
        JobInfo cleaningJobInfo = cleaningJob.setPeriodic(MainPresenter.PERIOD_TIME).build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(cleaningJobInfo);
    }

    @Override public void stopDbCleanService() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
    }

}
