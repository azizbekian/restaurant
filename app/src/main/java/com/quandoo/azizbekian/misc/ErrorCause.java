package com.quandoo.azizbekian.misc;

import android.support.annotation.IntDef;

import com.quandoo.azizbekian.utils.ErrorUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ErrorUtils.ERROR_INTERNET, ErrorUtils.ERROR_SERVER})
public @interface ErrorCause {
}