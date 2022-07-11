package com.bx.erp.view.activity;

import android.os.Bundle;

import com.bx.erp.BuildConfig;
import com.bx.erp.di.HasComponent;
import com.bx.erp.di.components.DaggerUserComponent;
import com.bx.erp.di.components.UserComponent;

public class UserActivity extends BaseActivity implements HasComponent {
  private UserComponent userComponent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO set content view
    initializeInjector();
    // TODO: inject
    userComponent.inject(this);
  }

  private void initializeInjector() {
    this.userComponent = DaggerUserComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .build();
  }

  @Override public Object getComponent() {
    return userComponent;
  }
}
