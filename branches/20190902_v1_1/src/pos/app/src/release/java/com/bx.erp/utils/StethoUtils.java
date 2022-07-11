package com.bx.erp.utils;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * debug模式下有stetho,正式包不加入
 */
public class StethoUtils {

  public static void init(Context context) {
  }

  public static void addStethoInterceptor(OkHttpClient.Builder builder) {
  }
}
