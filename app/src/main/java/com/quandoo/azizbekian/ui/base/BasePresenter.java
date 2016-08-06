package com.quandoo.azizbekian.ui.base;

import android.support.annotation.CheckResult;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView().
 * <p>
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView(boolean isChangingConfigurations) {
        mMvpView = null;
    }

    @CheckResult
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    @Override public void create(T mvpView) {
        attachView(mvpView);
    }

    @Override public void start() {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void stop() {

    }

    @Override public void destroy() {

    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
