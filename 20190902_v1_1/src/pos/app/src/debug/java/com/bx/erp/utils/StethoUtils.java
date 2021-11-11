package com.bx.erp.utils;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * debug模式下有stetho,正式包不加入
 */
public class StethoUtils {

  public static void init(Context context) {
    Stetho.initializeWithDefaults(context);
  }

  public static void addStethoInterceptor(OkHttpClient.Builder builder) {
    builder.addNetworkInterceptor(new StethoInterceptor());
  }
}
