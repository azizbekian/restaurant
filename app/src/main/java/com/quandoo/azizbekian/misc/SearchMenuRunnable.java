package com.quandoo.azizbekian.misc;

import android.support.v7.widget.SearchView;

import java.lang.ref.WeakReference;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class SearchMenuRunnable implements Runnable {

    private WeakReference<SearchView> mSearchViewWeakReference;
    private String mSearchQuery;

    public SearchMenuRunnable(SearchView searchView, String searchQuery) {
        mSearchViewWeakReference = new WeakReference<>(searchView);
        mSearchQuery = searchQuery;
    }

    @Override public void run() {
        if (null != mSearchViewWeakReference.get()) {
            SearchView searchView = mSearchViewWeakReference.get();
            searchView.setIconified(false);
            searchView.setQuery(mSearchQuery, true);
            searchView.clearFocus();
        }
    }
}
