package com.bx.erp.mapper;

import com.bx.erp.di.PerActivity;
import com.bx.erp.model.UserModel;

import javax.inject.Inject;

/**
 * Created by Ellen on 2018/06/20.
 */
@PerActivity
public class UserModelMapper {
  @Inject public UserModelMapper() {}
  public UserModel transform(String userName, String userId) {
    UserModel userModel = new UserModel();
    userModel.setID(userId);
    userModel.setName(userName);
    return userModel;
  }
}
