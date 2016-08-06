package com.quandoo.azizbekian.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quandoo.azizbekian.injection.ApplicationContext;

import javax.inject.Inject;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quandoo.db";
    public static final int DATABASE_VERSION = 1;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Db.CustomersTable.CREATE);
            db.execSQL(Db.RestaurantTablesTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
