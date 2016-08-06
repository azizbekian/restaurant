package com.quandoo.azizbekian.ui.main;

import android.content.Context;

import com.quandoo.azizbekian.BaseApplication;
import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.test.common.RxSchedulersOverrideRule;
import com.quandoo.azizbekian.test.common.TestDataFactory;
import com.quandoo.azizbekian.utils.ErrorUtils;
import com.quandoo.azizbekian.utils.NetworkUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@RunWith(PowerMockRunner.class)
@SuppressWarnings("ResourceType")
public class MainPresenterTest {

    @Mock MainMvpView mMockMainMvpView;
    @Mock DataManager mMockDataManager;
    @Mock BaseApplication mMockBaseApplication;
    private MainPresenter mMainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setup() {
        mMainPresenter = new MainPresenter(mMockDataManager);
        mMainPresenter.attachView(mMockMainMvpView);
    }

    @After
    public void tearDown() {
        mMainPresenter.detachView(false);
    }

    @Test @PrepareForTest({BaseApplication.class, NetworkUtils.class})
    public void loadCustomers_okResponse_showCustomers() {

        when(mMockDataManager.getCustomers(anyBoolean()))
                .thenReturn(Observable.just(TestDataFactory.MOCK_CUSTOMER_LIST));

        mockStaticMethods(true);

        mMainPresenter.loadCustomers();
        verify(mMockMainMvpView).showCustomers(TestDataFactory.MOCK_CUSTOMER_LIST);
        verify(mMockMainMvpView, never()).showCustomersEmpty();
        verify(mMockMainMvpView, never()).showError(anyInt());
    }

    @Test @PrepareForTest({BaseApplication.class, NetworkUtils.class})
    @SuppressWarnings("unchecked")
    public void loadCustomers_noInternet_showError() {

        when(mMockDataManager.getCustomers(anyBoolean()))
                .thenReturn(Observable.just(Collections.<Customer>emptyList()));

        mockStaticMethods(false);

        mMainPresenter.loadCustomers();
        verify(mMockMainMvpView).showError(ErrorUtils.ERROR_INTERNET);
        verify(mMockMainMvpView, never()).showCustomers(any());
        verify(mMockMainMvpView, never()).showCustomersEmpty();
    }

    @Test @PrepareForTest({BaseApplication.class, NetworkUtils.class})
    @SuppressWarnings("unchecked")
    public void loadCustomers_internetAvailable_showCustomersEmpty() {

        when(mMockDataManager.getCustomers(anyBoolean()))
                .thenReturn(Observable.just(Collections.<Customer>emptyList()));

        mockStaticMethods(true);

        mMainPresenter.loadCustomers();
        verify(mMockMainMvpView).showCustomersEmpty();
        verify(mMockMainMvpView, never()).showError(anyInt());
        verify(mMockMainMvpView, never()).showCustomers(any());
    }

    /**
     * Mocks static methods, that check whether internet connection is available.
     *
     * @param isDataConnectionAvailable If true - data connection is available. False otherwise.
     */
    private void mockStaticMethods(boolean isDataConnectionAvailable) {
        PowerMockito.mockStatic(BaseApplication.class);
        PowerMockito.when(BaseApplication.getContext()).thenReturn(Mockito.mock(Context.class));
        PowerMockito.mockStatic(NetworkUtils.class);
        PowerMockito.when(NetworkUtils.isConnected(any())).thenReturn(isDataConnectionAvailable);
    }
}