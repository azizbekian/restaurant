package com.quandoo.azizbekian.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created on Aug 02, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@AutoValue
public abstract class Table implements Parcelable {

    public abstract boolean isAvailable();

    public static Table create(boolean isAvailable) {
        return new AutoValue_Table(isAvailable);
    }

    public static TypeAdapter<Table> typeAdapter(Gson gson) {
        return new AutoValue_Table.GsonTypeAdapter(gson);
    }
}
