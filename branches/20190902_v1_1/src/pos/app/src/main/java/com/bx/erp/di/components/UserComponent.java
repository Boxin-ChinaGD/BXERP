package com.bx.erp.di.components;

import com.bx.erp.di.PerActivity;
import com.bx.erp.di.modules.ActivityModule;
import com.bx.erp.di.modules.UserModule;
import com.bx.erp.view.activity.UserActivity;
import com.bx.erp.view.fragment.UserFragment;

import dagger.Component;

/**
 * Created by Ellen on 2018/06/20.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class})
public interface UserComponent extends ActivityComponent {
  void inject(UserActivity activity);

  void inject(UserFragment fragment);
}
