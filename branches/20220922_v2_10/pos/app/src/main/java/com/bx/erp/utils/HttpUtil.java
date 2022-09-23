package com.bx.erp.utils;

import android.support.annotation.Nullable;

import com.bx.erp.helper.Constants;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtil {
//    protected static Map<Integer, String> mapSessionPos = new HashMap<Integer, String>();
//
//    protected static Map<String, String> mapSessionStaff = new HashMap<String, String>();
//
//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public static String getPosSession(int pos_ID, String pwd) {
//        String session = null;
//
//        lock.writeLock().lock();
//        session = doGetPosSession(pos_ID, pwd);
//        lock.writeLock().unlock();
//
//        return session;
//    }
//
//    @Nullable
//    private static String doGetPosSession(int pos_ID, String pwd) {
//        if (mapSessionPos.get(pos_ID) != null) {
//            return mapSessionPos.get(pos_ID);
//        }
//
//        OkHttpClient client = new OkHttpClient();
//        try {
//            Pos pos = new Pos();
//            pos.setID(Long.valueOf(pos_ID));
//            RequestBody body = new FormBody.Builder().add(pos.getFIELD_NAME_ID(), String.valueOf(pos_ID)).build();
//            Request request = new Request.Builder().url(Constants.HTTP_IP + "pos/getTokenEx.bx").post(body).build();
//            Response response = client.newCall(request).execute();
//            //解析modulus和exponent
//            String responseData = response.body().string();
//            JSONObject jsonObject = new JSONObject(responseData);
//            String str = jsonObject.getString("rsa");
//            JSONObject rsaData = new JSONObject(str);
//            String modulus = rsaData.getString("modulus");
//            String exponent = rsaData.getString("exponent");
//            //解析session
//            Headers headers = response.headers();
//            List<String> cookies = headers.values("Set-Cookie");
//            String session = cookies.get(0);
//            String ssID = session.substring(0, session.indexOf(";"));
//            //生成公钥
//            modulus = new BigInteger(modulus, 16).toString();
//            exponent = new BigInteger(exponent, 16).toString();
//
//            RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
//
//            // ..加密密码
//            String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
//            pos.setPwdEncrypted(pwdEncrypted);
//
//            RequestBody loginBody = new FormBody.Builder().add(pos.getFIELD_NAME_ID(), String.valueOf(pos.getID())).add(pos.getFIELD_NAME_pwdEncrypted(), pos.getPwdEncrypted()).build();
//
//            Request requestLogin = new Request.Builder().url(Constants.HTTP_IP + "pos/loginEx.bx").addHeader(Constants.COOKIE, ssID).post(loginBody).build();
//            Response responseError = client.newCall(requestLogin).execute();
//
//            Headers headers1 = responseError.headers();
//            List<String> c = headers.values("Set-Cookie");
//            String session1 = c.get(0);
//            String sessionID = session1.substring(0, session1.indexOf(";"));
//            mapSessionPos.put(pos_ID, sessionID);
//
//            return sessionID;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

//    public static String getStaffSession(String sessionIDPos, String phone, String pwd) {
//        String session = null;
//
//        lock.writeLock().lock();
//        session = doGetStaffSession(sessionIDPos, phone, pwd);
//        lock.writeLock().unlock();
//
//        return session;
//    }

//    @Nullable
//    private static String doGetStaffSession(String sessionIDPos, String phone, String pwd) {
//        if (mapSessionStaff.get(phone) != null) {
//            return mapSessionStaff.get(phone);
//        }
//
//        OkHttpClient client = new OkHttpClient();
//        try {
//            Staff currentStaff = new Staff();
//            currentStaff.setPhone(phone);
//            RequestBody body = new FormBody.Builder().add(currentStaff.getFIELD_NAME_phone(), phone).build();
//            Request request = new Request.Builder().url(Constants.HTTP_IP + "currentStaff/getTokenEx.bx").addHeader(Constants.COOKIE, sessionIDPos).post(body).build();
//            Response response = client.newCall(request).execute();
//            //解析modulus和exponent
//            String responseData = response.body().string();
//            JSONObject jsonObject = new JSONObject(responseData);
//            String str = jsonObject.getString("rsa");
//            JSONObject rsaData = new JSONObject(str);
//            String modulus = rsaData.getString("modulus");
//            String exponent = rsaData.getString("exponent");
//            //生成公钥
//            modulus = new BigInteger(modulus, 16).toString();
//            exponent = new BigInteger(exponent, 16).toString();
//
//            RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
//
//            String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
//            currentStaff.setPwdEncrypted(pwdEncrypted);
//
//            RequestBody loginBody = new FormBody.Builder().add(currentStaff.getFIELD_NAME_phone(), String.valueOf(currentStaff.getPhone())).add(currentStaff.getFIELD_NAME_pwdEncrypted(), currentStaff.getPwdEncrypted()).build();
//
//            Request requestLogin = new Request.Builder().url(Constants.HTTP_IP + "currentStaff/loginEx.bx").addHeader(Constants.COOKIE, sessionIDPos).post(loginBody).build();
//            Response responseError = client.newCall(requestLogin).execute();
//
//            mapSessionStaff.put(phone, sessionIDPos);
//
//            return sessionIDPos;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
