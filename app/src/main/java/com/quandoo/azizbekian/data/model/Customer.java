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
public abstract class Customer implements Parcelable {

    public abstract String customerFirstName();

    public abstract String customerLastName();

    public abstract int id();

    public static Customer create(String first, String last, int id) {
        return new AutoValue_Customer(first, last, id);
    }

    public static TypeAdapter<Customer> typeAdapter(Gson gson) {
        return new AutoValue_Customer.GsonTypeAdapter(gson);
    }
}
