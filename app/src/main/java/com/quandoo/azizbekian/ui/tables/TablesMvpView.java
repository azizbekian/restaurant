package com.quandoo.azizbekian.ui.tables;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.ui.base.MvpView;
import com.quandoo.azizbekian.misc.ErrorCause;

import java.util.List;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public interface TablesMvpView extends MvpView {

    void setupRecyclerView();

    void showTables(List<Table> tables);

    void showError(@ErrorCause int errorCause);

    void showProgressBar(boolean show);

    void onReservedTableClicked(int position);

    void onAvailableTableClicked(Customer customer, int position);

}
