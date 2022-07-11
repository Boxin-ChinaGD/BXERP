package com.bx.erp.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Ellen on 2018/06/20.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {}