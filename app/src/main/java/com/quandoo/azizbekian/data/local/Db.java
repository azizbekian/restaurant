package com.quandoo.azizbekian.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.data.model.Table;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class Db {

    public Db() {
    }

    public static final String PLACEHOLDER_SELECT_EVERYTHING_FROM = "SELECT * FROM %s";
    public static final String PLACEHOLDER_SELECT_COUNT_FROM = "SELECT COUNT(*) FROM %s";

    public static class CustomersTable {
        public static final String TABLE_NAME = "customers";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                        COLUMN_LAST_NAME + " TEXT NOT NULL " +
                        " ); ";

        public static ContentValues toContentValues(Customer customer) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, customer.id());
            values.put(COLUMN_FIRST_NAME, customer.customerFirstName());
            values.put(COLUMN_LAST_NAME, customer.customerLastName());
            return values;
        }

        public static Customer parseCursor(Cursor cursor) {
            return Customer.create(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        }
    }

    public static class RestaurantTablesTable {
        public static final String TABLE_NAME = "restaurant_tables";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IS_AVAILABLE = "is_available";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_IS_AVAILABLE + " INTEGER NOT NULL, " +
                        COLUMN_ID + " INTEGER PRIMARY KEY " +
                        " ); ";

        public static ContentValues toContentValues(Table restaurantTable) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_AVAILABLE, restaurantTable.isAvailable());
            return values;
        }

        public static Table parseCursor(Cursor cursor) {
            return Table.create(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_AVAILABLE)) == 1);
        }
    }
}
