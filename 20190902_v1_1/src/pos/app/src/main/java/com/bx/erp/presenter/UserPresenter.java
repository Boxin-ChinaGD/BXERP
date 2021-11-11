package com.bx.erp.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bx.erp.di.PerActivity;
import com.bx.erp.mapper.UserModelMapper;
import com.bx.erp.model.HttpRespTempModel;
import com.bx.erp.model.UserModel;
import com.bx.erp.retrofit.APIBaiduTemp;
import com.bx.erp.retrofit.APIUserService;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ellen on 2018/06/20.
 */
@PerActivity public class UserPresenter{

  private Retrofit mRetrofit;
  private UserModelMapper mUserMapper;

  @Inject public UserPresenter(Retrofit mRetrofit, UserModelMapper userMapper) {
    this.mRetrofit = mRetrofit;
    this.mUserMapper = userMapper;
  }

  /**
   * 这里只是一个关于网络请求的demo
   */
  public void testGetUserInfo(String userId) {
    mRetrofit.create(APIUserService.class).getUserInfo(userId).enqueue(new Callback<UserModel>() {
      @Override public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        if (response.isSuccessful()) {
          // TODO: 处理正确数据情况
          EventBus.getDefault().post(response.body());
        } else {
          // TODO:  restful api 中的其他异常的错误,需要自行处理
          int code = response.code();
          ResponseBody errorBody = response.errorBody();
          String url = call.request().url().toString();
          Object object = postRequestError(mRetrofit, errorBody, url);
          //EventBus.getDefault().post(object);
        }
      }

      @Override public void onFailure(Call<UserModel> call, Throwable t) {
        // TODO: 处理异常情况,可能是网络原因或者解析出错
      }
    });
  }



  private Object postRequestError(@NonNull Retrofit retrofit, ResponseBody errorBody, String requestUrlPath) {
    // TODO Object 类可以修改
    try {
      Converter<ResponseBody, Object> converter =
          retrofit.responseBodyConverter(Object.class, new Annotation[0]);
      if (null != errorBody) {
        Object errorEntity = converter.convert(errorBody); // 错误的文本信息
        // 后续自行处理
        return errorEntity;
      }
    } catch (Exception e) {
      e.printStackTrace();
      // 后续自行处理
    }
    return null;
  }

  public void testSearchBaidu(String keyword) {
    mRetrofit.create(APIBaiduTemp.class)
        .searchBaidu(keyword)
        .enqueue(new Callback<HttpRespTempModel>() {
          @Override public void onResponse(Call<HttpRespTempModel> call, Response<HttpRespTempModel> response) {
            if (response.isSuccessful()) {
              // TODO: 处理正确数据情况
              EventBus.getDefault().post(response.body());
            } else {
              // TODO:  restful api 中的其他异常的错误,需要自行处理
              int code = response.code();
              ResponseBody errorBody = response.errorBody();
              String url = call.request().url().toString();
              Object object = postRequestError(mRetrofit, errorBody, url);
              EventBus.getDefault().post(object);
            }
          }

          @Override public void onFailure(Call<HttpRespTempModel> call, Throwable t) {
            // TODO: 处理异常情况,可能是网络原因或者解析出错
            Log.e(UserPresenter.class.getSimpleName(), "parse error", t);
          }
        });
  }
}
