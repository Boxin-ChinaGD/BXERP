package com.bx.erp.retrofit;

import com.bx.erp.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ellen on 2018/06/20.
 */

public interface APIUserService {
  @GET("mobile-api/center") Call<UserModel> getUserInfo(@Query("user_id") String userId);
}
