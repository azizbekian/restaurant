package com.quandoo.azizbekian.injection.component;

import com.quandoo.azizbekian.injection.ConfigPersistent;
import com.quandoo.azizbekian.injection.module.ActivityModule;

import dagger.Component;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@ConfigPersistent
@Component(dependencies = AppComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

}
