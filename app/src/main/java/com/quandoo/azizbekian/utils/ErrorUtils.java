package com.quandoo.azizbekian.utils;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public final class ErrorUtils {

    public ErrorUtils() {
        throw new RuntimeException("Unable to instantiate class " + getClass().getSimpleName());
    }

    /**
     * An error cause by absence of internet connection.
     */
    public static final int ERROR_INTERNET = 0;

    /**
     * An error cause by backend issue.
     */
    public static final int ERROR_SERVER = 1;

}
