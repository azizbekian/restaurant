package com.quandoo.azizbekian.injection.component;

import com.quandoo.azizbekian.injection.PerActivity;
import com.quandoo.azizbekian.injection.module.ActivityModule;
import com.quandoo.azizbekian.ui.dialog.ConfirmDialogFragment;
import com.quandoo.azizbekian.ui.main.MainActivity;
import com.quandoo.azizbekian.ui.tables.TablesActivity;

import dagger.Subcomponent;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(TablesActivity tablesActivity);

    void inject(ConfirmDialogFragment fr);

}
