package com.bx.erp.retrofit;

import com.bx.erp.model.HttpRespTempModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIBaiduTemp {
  @GET("s") Call<HttpRespTempModel> searchBaidu(@Query("wd") String keyword);
}
