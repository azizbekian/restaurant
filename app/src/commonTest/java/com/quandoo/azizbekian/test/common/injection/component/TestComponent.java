package com.quandoo.azizbekian.test.common.injection.component;

import com.quandoo.azizbekian.injection.component.AppComponent;
import com.quandoo.azizbekian.test.common.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {

}
