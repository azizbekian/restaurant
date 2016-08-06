package com.quandoo.azizbekian.ui.tables;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.misc.ErrorCause;
import com.quandoo.azizbekian.misc.GridDividerDecoration;
import com.quandoo.azizbekian.ui.base.BaseActivity;
import com.quandoo.azizbekian.ui.dialog.ConfirmDialogFragment;
import com.quandoo.azizbekian.ui.dialog.ErrorDialogFragment;
import com.quandoo.azizbekian.utils.FragmentUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class TablesActivity extends BaseActivity implements TablesMvpView {

    public static final String KEY_CUSTOMER = "customer";

    public static void launch(Activity activity, Customer customer) {
        Intent intent = new Intent(activity, TablesActivity.class);
        intent.putExtra(KEY_CUSTOMER, customer);

        activity.startActivity(intent);
    }

    @BindView(R.id.recycler_tables) RecyclerView tablesRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindInt(R.integer.span_tables) int tablesSpan;

    @Inject TablesPresenter mTablesPresenter;
    @Inject TablesAdapter mTablesAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        ButterKnife.bind(this);
        activityComponent().inject(this);

        mTablesPresenter.create(this);
        mTablesPresenter.setCustomer(getIntent());
    }

    @Override public void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, tablesSpan);
        tablesRecyclerView.setHasFixedSize(true);
        tablesRecyclerView.setLayoutManager(layoutManager);
        tablesRecyclerView.setAdapter(mTablesAdapter);
        tablesRecyclerView.addItemDecoration(new GridDividerDecoration(this));
    }

    @Override public void showTables(List<Table> tables) {
        mTablesAdapter.setTables(tables);
        mTablesAdapter.notifyDataSetChanged();
    }

    @Override public void showError(@ErrorCause int errorCause) {
        Fragment fr = FragmentUtils.findByTag(this, ErrorDialogFragment.TAG);
        if (null == fr) {
            FragmentUtils.showDialog(this, ErrorDialogFragment.newFragment(errorCause),
                    ErrorDialogFragment.TAG);
        }
    }

    @Override public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override public void onReservedTableClicked(int position) {
        Toast.makeText(this, String.format(getString(R.string.toast_reserved), position),
                Toast.LENGTH_SHORT).show();
    }

    @Override public void onAvailableTableClicked(Customer customer, int position) {
        DialogFragment fr = FragmentUtils.findByTag(this, ConfirmDialogFragment.TAG);
        if (null == fr) {
            fr = ConfirmDialogFragment.newFragment(customer, position, mTablesPresenter);
        }
        FragmentUtils.add(this, fr, ConfirmDialogFragment.TAG);
    }

}
