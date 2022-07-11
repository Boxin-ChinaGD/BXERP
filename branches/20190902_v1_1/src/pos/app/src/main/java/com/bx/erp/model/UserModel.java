package com.bx.erp.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ellen on 2018/06/20.
 * ①
 * Keep 用于gson解析,不加上Keep 正式包混淆后字段名变成了a,b,等导致解析异常.
 * proguard-rules.pro 里面添加 <code>-keepclasseswithmembers @android.support.annotation.Keep public class ** {*;}</code> 和Keep配合,
 * 也可以用SerializedName 重命名或保证混淆正常的做法.
 * transient 关键字用于 不解析该字段 如本类里的temp字段.
 * 更多用法请自行搜索
 *
 * ②
 * Serializable 用于activity等组件间数据传递,也可以用Parcelable的方式...注意有些类型不能用Serializable
 *
 */
@Keep
public class UserModel implements Serializable {

  @SerializedName("_id")
  private String ID;
  private String name;

  private transient boolean temp;

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
