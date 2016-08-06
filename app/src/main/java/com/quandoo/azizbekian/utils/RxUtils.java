package com.quandoo.azizbekian.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public final class RxUtils {

    private RxUtils() {
        throw new RuntimeException("Unable to instantiate " + RxUtils.class.getSimpleName());
    }

    /**
     * {@link Observable.Transformer} that transforms the source observable to subscribe in the
     * io thread and observe on the Android's UI thread.
     */
    @SuppressWarnings("unchecked")
    private static final Observable.Transformer IO_TO_MAIN_THREAD_SCHEDULER_TRANSFORMER =
            observable -> ((Observable) observable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

    /**
     * Get {@link Observable.Transformer} that transforms the source observable to subscribe in
     * the io thread and observe on the Android's UI thread.
     *
     * @return {@link Observable.Transformer}
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applyIoToMainThreadSchedulers() {
        return (Observable.Transformer<T, T>) IO_TO_MAIN_THREAD_SCHEDULER_TRANSFORMER;
    }

}
