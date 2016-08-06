package com.quandoo.azizbekian.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPersistent {
}
