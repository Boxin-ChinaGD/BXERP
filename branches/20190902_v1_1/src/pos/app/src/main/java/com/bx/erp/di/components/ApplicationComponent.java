package com.bx.erp.di.components;

import android.content.Context;

import com.bx.erp.di.modules.ApplicationModule;
import com.bx.erp.view.activity.BaseActivity;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);

  //Exposed to sub-graphs.
  Context provideContext();
  OkHttpClient provideOkHttpClient();
  Retrofit provideRetrofit();
  Gson provideGson();
//  DaoSession provideDaoSession();
}
