package com.bx.erp.view.fragment;

import android.os.Bundle;

/**
 * Created by Ellen on 2018/06/20.
 * 一般与view pager配套使用.
 * 懒加载模式(当fragment第一次被用户见到的时候才执行fetchData方法)
 */

public abstract class LazyFragment extends BaseFragment {
  protected boolean isViewInitiated;
  protected boolean isVisibleToUser;
  protected boolean isDataInitiated;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    isViewInitiated = true;
    prepareFetchData();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    this.isVisibleToUser = isVisibleToUser;
    prepareFetchData();
  }

  public abstract void fetchData();

  public boolean prepareFetchData() {
    return prepareFetchData(false);
  }

  public boolean prepareFetchData(boolean forceUpdate) {
    if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
      fetchData();
      isDataInitiated = true;
      return true;
    }
    return false;
  }
}
