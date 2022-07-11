package com.bx.erp.di.components;

import android.app.Activity;

import com.bx.erp.di.PerActivity;
import com.bx.erp.di.modules.ActivityModule;

import dagger.Component;

/**
 * Created by Ellen on 2018/06/20.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
  //Exposed to sub-graphs.
  Activity activity();
}
