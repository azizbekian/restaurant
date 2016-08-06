package com.quandoo.azizbekian.data.local;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.CheckResult;
import android.support.v4.database.DatabaseUtilsCompat;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    /**
     * Saves {@code customers} in the databases.
     */
    public void setCustomers(final Collection<Customer> customers) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        try {
            mDb.delete(Db.CustomersTable.TABLE_NAME, null);
            for (Customer customer : customers) {
                mDb.insert(Db.CustomersTable.TABLE_NAME,
                        Db.CustomersTable.toContentValues(customer),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * Saves {@code tables} in the databases.
     */
    public void setRestaurantTables(final Collection<Table> tables) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        try {
            mDb.delete(Db.RestaurantTablesTable.TABLE_NAME, null);
            for (Table table : tables) {
                mDb.insert(Db.RestaurantTablesTable.TABLE_NAME,
                        Db.RestaurantTablesTable.toContentValues(table),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * Updates the {@link Table} at {@code position} to value {@code isAvailable}.
     *
     * @param position    The position of the value in the table.
     * @param isAvailable If true - the table is available. False otherwise.
     */
    public void updateRestaurantTable(int position, boolean isAvailable) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        try {
            mDb.update(Db.RestaurantTablesTable.TABLE_NAME,
                    Db.RestaurantTablesTable.toContentValues(Table.create(isAvailable)),
                    Db.RestaurantTablesTable.COLUMN_ID + " = " + position);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * Returns a {@link List} of {@link Customer}s from the database.
     */
    @CheckResult public Observable<List<Customer>> getCustomers() {
        return mDb.createQuery(Db.CustomersTable.TABLE_NAME,
                String.format(Db.PLACEHOLDER_SELECT_EVERYTHING_FROM, Db.CustomersTable.TABLE_NAME))
                .mapToList(Db.CustomersTable::parseCursor);
    }

    /**
     * Returns a {@link List} of {@link Table}s from the database.
     */
    @CheckResult public Observable<List<Table>> getRestaurantTables() {
        return mDb.createQuery(Db.RestaurantTablesTable.TABLE_NAME,
                String.format(Db.PLACEHOLDER_SELECT_EVERYTHING_FROM, Db.RestaurantTablesTable.TABLE_NAME))
                .mapToList(Db.RestaurantTablesTable::parseCursor);
    }

    /**
     * Drops {@link com.quandoo.azizbekian.data.local.Db.CustomersTable#TABLE_NAME} and
     * {@link com.quandoo.azizbekian.data.local.Db.RestaurantTablesTable#TABLE_NAME} tables.
     */
    @SuppressWarnings("all")
    public void clearTables() {
        mDb.delete(Db.CustomersTable.TABLE_NAME, null, null);
        mDb.delete(Db.RestaurantTablesTable.TABLE_NAME, null, null);
    }

    /**
     * Checks for the emptiness of customers table.
     *
     * @return True - if the table is empty. False otherwise.
     */
    @CheckResult public boolean isCustomersEmpty() {
        Cursor cursor = mDb.query(String.format(Db.PLACEHOLDER_SELECT_COUNT_FROM,
                Db.CustomersTable.TABLE_NAME));
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    /**
     * Checks for the emptiness of restaurants table.
     *
     * @return True - if the table is empty. False otherwise.
     */
    @CheckResult public boolean isRestaurantTablesEmpty() {
        Cursor cursor = mDb.query(String.format(Db.PLACEHOLDER_SELECT_COUNT_FROM,
                Db.RestaurantTablesTable.TABLE_NAME));
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

}
