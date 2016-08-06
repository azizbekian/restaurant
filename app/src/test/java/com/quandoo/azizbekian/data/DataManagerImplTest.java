package com.quandoo.azizbekian.data;

import com.quandoo.azizbekian.data.local.DatabaseHelper;
import com.quandoo.azizbekian.data.local.PreferencesHelper;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.quandoo.azizbekian.data.remote.ApiInteractor;
import com.quandoo.azizbekian.test.common.TestDataFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

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
@RunWith(MockitoJUnitRunner.class)
public class DataManagerImplTest {

    @Mock ApiInteractor mMockApiInteractor;
    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock PreferencesHelper mMockPreferencesHelper;
    private DataManagerImpl mDataManager;

    @Before
    public void setUp() throws Exception {
        mDataManager = new DataManagerImpl(mMockApiInteractor, mMockDatabaseHelper,
                mMockPreferencesHelper);
    }

    @Test
    public void getCustomers_fromLocalTrueDbNotEmpty_fromDb() {
        when(mMockDatabaseHelper.isCustomersEmpty()).thenReturn(false);
        when(mMockDatabaseHelper.getCustomers())
                .thenReturn(Observable.just(TestDataFactory.MOCK_CUSTOMER_LIST));

        TestSubscriber<List<Customer>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getCustomers(true).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(TestDataFactory.MOCK_CUSTOMER_LIST));
        customerSubscriber.assertCompleted();
    }

    @Test
    public void getCustomers_fromLocalTrueDbEmpty_fromNetwork() {
        when(mMockDatabaseHelper.isCustomersEmpty()).thenReturn(true);
        when(mMockApiInteractor.getCustomers())
                .thenReturn(Observable.just(TestDataFactory.MOCK_CUSTOMER_LIST));

        TestSubscriber<List<Customer>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getCustomers(true).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(TestDataFactory.MOCK_CUSTOMER_LIST));
        customerSubscriber.assertCompleted();
    }

    @Test
    public void getCustomers_fromLocalFalseOkCustomers_fromNetwork() {
        when(mMockApiInteractor.getCustomers())
                .thenReturn(Observable.just(TestDataFactory.MOCK_CUSTOMER_LIST));

        TestSubscriber<List<Customer>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getCustomers(false).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(TestDataFactory.MOCK_CUSTOMER_LIST));
        customerSubscriber.assertCompleted();
        verify(mMockDatabaseHelper).setCustomers(TestDataFactory.MOCK_CUSTOMER_LIST);
    }

    @Test
    public void getCustomers_fromLocalFalseEmptyCustomers_fromNetwork() {
        when(mMockApiInteractor.getCustomers())
                .thenReturn(Observable.just(Collections.emptyList()));

        TestSubscriber<List<Customer>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getCustomers(false).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(Collections.emptyList()));
        customerSubscriber.assertCompleted();
        verify(mMockDatabaseHelper, never()).setCustomers(Collections.emptyList());
    }

    @Test
    public void getTables_fromLocalTrueDbNotEmpty_fromDb() {
        when(mMockDatabaseHelper.isRestaurantTablesEmpty()).thenReturn(false);
        when(mMockDatabaseHelper.getRestaurantTables())
                .thenReturn(Observable.just(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST));

        TestSubscriber<List<Table>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getTables(true).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST));
        customerSubscriber.assertCompleted();
    }

    @Test
    public void getTables_fromLocalTrueDbEmpty_fromNetwork() {
        when(mMockDatabaseHelper.isRestaurantTablesEmpty()).thenReturn(true);
        when(mMockApiInteractor.getTables())
                .thenReturn(Observable.just(Collections.emptyList()));

        TestSubscriber<List<Table>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getTables(true).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(Collections.emptyList()));
        customerSubscriber.assertCompleted();
    }

    @Test
    public void getTables_fromLocalFalseOkCustomers_fromNetwork() {
        when(mMockApiInteractor.getTables())
                .thenReturn(Observable.just(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST));

        TestSubscriber<List<Table>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getTables(false).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST));
        customerSubscriber.assertCompleted();
        verify(mMockDatabaseHelper).setRestaurantTables(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST);
        verify(mMockPreferencesHelper).putLatestUpdateTimeMillis();
    }

    @Test
    public void getTables_fromLocalFalseEmptyCustomers_fromNetwork() {
        when(mMockApiInteractor.getTables())
                .thenReturn(Observable.just(Collections.emptyList()));

        TestSubscriber<List<Table>> customerSubscriber = new TestSubscriber<>();
        mDataManager.getTables(false).subscribe(customerSubscriber);
        customerSubscriber.assertNoErrors();
        customerSubscriber.assertReceivedOnNext(Collections.singletonList(Collections.emptyList()));
        customerSubscriber.assertCompleted();
        verify(mMockDatabaseHelper, never()).setRestaurantTables(TestDataFactory.MOCK_RESTAURANT_TABLE_LIST);
        verify(mMockPreferencesHelper, never()).putLatestUpdateTimeMillis();
    }

    @Test
    public void updateRestaurantTable() {
        int anyInt = anyInt();
        boolean anyBoolean = anyBoolean();
        mDataManager.updateRestaurantTable(anyInt, anyBoolean);
        verify(mMockDatabaseHelper).updateRestaurantTable(anyInt + 1, anyBoolean);
    }

    @Test
    public void clearLocalData() {
        mDataManager.clearLocalData();
        verify(mMockDatabaseHelper).clearTables();
        verify(mMockPreferencesHelper).clear();
        verify(mMockPreferencesHelper).putLatestUpdateTimeMillis();
    }

    @Test
    public void latestUpdateTime() {
        mDataManager.latestUpdateTime();
        verify(mMockPreferencesHelper).getLatestUpdateTimeMillis();
    }

}