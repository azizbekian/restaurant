package com.quandoo.azizbekian.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.misc.ErrorCause;
import com.quandoo.azizbekian.utils.ErrorUtils;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class ErrorDialogFragment extends DialogFragment {

    public static final String TAG = ErrorDialogFragment.class.getSimpleName();

    private static final String KEY_ERROR_CAUSE = "error_cause";

    public static DialogFragment newFragment(@ErrorCause int errorCause) {
        ErrorDialogFragment fr = new ErrorDialogFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_ERROR_CAUSE, errorCause);
        fr.setArguments(b);
        return fr;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

        String message = getString(R.string.dialog_error_message_server);
        if (null != getArguments()) {
            @ErrorCause int errorCause = getArguments().getInt(KEY_ERROR_CAUSE, ErrorUtils.ERROR_SERVER);
            switch (errorCause) {

                case ErrorUtils.ERROR_INTERNET:
                    message = getString(R.string.dialog_error_message_internet);
                    break;

                case ErrorUtils.ERROR_SERVER:
                    // Already defined
                    break;
            }
        }

        return new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null).create();
    }
}
