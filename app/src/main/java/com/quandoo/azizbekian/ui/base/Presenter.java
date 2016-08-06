package com.quandoo.azizbekian.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 * <p>
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@SuppressWarnings("unused")
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView(boolean isChangingConfigurations);

    void create(V mvpView);

    void start();

    void resume();

    void pause();

    void stop();

    void destroy();
}

