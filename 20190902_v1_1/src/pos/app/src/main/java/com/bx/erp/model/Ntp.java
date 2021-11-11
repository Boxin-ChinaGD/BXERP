
package com.bx.erp.model;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Ntp extends BaseModel {
    @Transient
    public static final String HTTP_Ntp_SyncTime = "ntp/syncEx.bx?t1=";

    public static final NtpField field = new NtpField();

    protected long t1; // 同步者发送同步请求的时间

//    public String getFIELD_NAME_t1() {
//        return "t1";
//    }
//
//    @Transient
//    protected String FIELD_NAME_t1;

    protected long t2; // 同步者发送同步请求到达的时间

//    public String getFIELD_NAME_t2() {
//        return "t2";
//    }
//
//    @Transient
//    protected String FIELD_NAME_t2;

    protected long t3; // 被同步者返回请求时的时间

//    public String getFIELD_NAME_t3() {
//        return "t3";
//    }
//
//    @Transient
//    protected String FIELD_NAME_t3;


    protected long t4; // 同步者接受到响应的时间

//    public String getFIELD_NAME_t4() {
//        return "t4";
//    }
//
//    @Transient
//    protected String FIELD_NAME_t4;


    @Keep
    public Ntp(long t1, long t2,
               long t3, long t4) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    @Generated(hash = 556238327)
    public Ntp() {
    }

    public long getT1() {
        return t1;
    }

    public void setT1(long t1) {
        this.t1 = t1;
    }

    public long getT2() {
        return t2;
    }

    public void setT2(long t2) {
        this.t2 = t2;
    }

    public long getT3() {
        return t3;
    }

    public void setT3(long t3) {
        this.t3 = t3;
    }

    public long getT4() {
        return t4;
    }

    public void setT4(long t4) {
        this.t4 = t4;
    }

    @Override
    public String toString() {
        return "Ntp{" +
                "t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                '}';
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行Ntp的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            t1 = jo.getLong("t1");
            t2 = jo.getLong("t2");
            t3 = jo.getLong("t3");
            t4 = jo.getLong("t4");
            return this;
        } catch (JSONException e) {
            System.out.println("执行doParse1出现异常，错误信息=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Ntp的parse1，s=" + s);

        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("执行parse1出现异常，错误信息=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }

//    public void setFIELD_NAME_t1(String FIELD_NAME_t1) {
//        this.FIELD_NAME_t1 = FIELD_NAME_t1;
//    }
//
//    public void setFIELD_NAME_t2(String FIELD_NAME_t2) {
//        this.FIELD_NAME_t2 = FIELD_NAME_t2;
//    }
//
//    public void setFIELD_NAME_t3(String FIELD_NAME_t3) {
//        this.FIELD_NAME_t3 = FIELD_NAME_t3;
//    }
//
//    public void setFIELD_NAME_t4(String FIELD_NAME_t4) {
//        this.FIELD_NAME_t4 = FIELD_NAME_t4;
//    }

}
