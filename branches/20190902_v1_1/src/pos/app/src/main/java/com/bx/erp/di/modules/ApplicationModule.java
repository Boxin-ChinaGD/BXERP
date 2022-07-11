package com.bx.erp.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bx.erp.AppApplication;
import com.bx.erp.BuildConfig;
import com.bx.erp.helper.Constants;
import com.bx.erp.utils.StethoUtils;
import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
  private final AppApplication application;

  public ApplicationModule(AppApplication application) {
    this.application = application;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return this.application;
  }


  @Provides @Singleton OkHttpClient provideOkHttpClient(Context context) {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override public void log(@NonNull String message) {
        Log.d("OkHttp", message);
      }
    });
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    StethoUtils.addStethoInterceptor(builder);
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(logging);
    }
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new ChuckInterceptor(context))
            .connectTimeout(50 * 1000, TimeUnit.MILLISECONDS)
            .readTimeout(50 * 1000, TimeUnit.MILLISECONDS)
            .writeTimeout(50 * 1000, TimeUnit.MILLISECONDS)
            .build();
    return okHttpClient;
  }


  @Provides @Singleton public Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().client(client)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }


  @Provides @Singleton public Gson provideGson() {
    return new Gson();
  }
  //获取可写数据库
//  @Provides @Singleton public DaoSession provideDaoSession() {
//    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, COMMODITY_TABLE_NAME);
//    Database db = helper.getWritableDb();
//    DaoSession daoSession = new DaoMaster(db).newSession();
//    return daoSession;
//  }
}
