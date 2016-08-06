package com.quandoo.azizbekian.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.ui.tables.TablesPresenter;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class ConfirmDialogFragment extends DialogFragment {

    public static final String TAG = ErrorDialogFragment.class.getSimpleName();

    private static final String KEY_CUSTOMER = "customer";
    private static final String KEY_POSITION = "position";

    public static DialogFragment newFragment(Customer customer, int position,
                                             TablesPresenter tablesPresenter) {
        ConfirmDialogFragment fr = new ConfirmDialogFragment();
        Bundle b = new Bundle();
        b.putParcelable(KEY_CUSTOMER, customer);
        b.putInt(KEY_POSITION, position);
        fr.setArguments(b);
        fr.setTablesPresenter(tablesPresenter);
        return fr;
    }

    private void setTablesPresenter(TablesPresenter tablesPresenter) {
        mTablesPresenter = tablesPresenter;
    }

    private TablesPresenter mTablesPresenter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

        String firstName = "unknown";
        String lastName = "";
        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(KEY_CUSTOMER)) {
            Customer customer = bundle.getParcelable(KEY_CUSTOMER);
            firstName = customer.customerFirstName();
            lastName = customer.customerLastName();
        }

        return new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.dialog_confirm_title))
                .setMessage(String.format(getString(R.string.dialog_confirm_message), firstName, lastName))
                .setPositiveButton(R.string.dialog_action_ok, (dialogInterface, i) -> {
                    mTablesPresenter.reserveTable(getArguments().getInt(KEY_POSITION, 0));
                })
                .setNegativeButton(R.string.dialog_action_no, (dialogInterface, i) -> {
                    dismiss();
                }).create();
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles bug https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
