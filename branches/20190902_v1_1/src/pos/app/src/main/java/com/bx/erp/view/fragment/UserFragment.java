package com.bx.erp.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.bx.erp.di.components.UserComponent;
import com.bx.erp.model.UserModel;
import com.bx.erp.presenter.UserPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Created by Ellen on 2018/06/20.
 */

public class UserFragment extends BaseFragment {

  @Inject UserPresenter mUserPresenter;
  private String mUserId;

  @Override
  protected int setView() {
    return 0;
  }

  @Override
  protected void init(View view) {

  }

  @Override
  protected void initData(Bundle savedInstanceState) {

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(UserComponent.class).inject(this);
  }

  private void requestData() {
    // get data. this is just a demo.
    showLoading(); // 先显示loading再获取数据.否则可能loading不能dismiss
    mUserPresenter.testGetUserInfo(mUserId);
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe public void onUserInfoResp(UserModel userModel) {
    // TODO: show user info
    dismissLoading();
  }
}
