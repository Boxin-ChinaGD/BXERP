package com.base;

import android.test.AndroidTestCase;

import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.ErrorInfo;

import java.util.UUID;

public class BaseAndroidTestCase extends AndroidTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 等待http请求返回响应数据。
     *
     * @param hru
     * @return true，没有超时；false，超时。
     */
    protected boolean waitForHttpResponse(HttpRequestUnit hru) {
        long lTimeOut = hru.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1000 * 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (hru.hasHttpResponse()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 等待UI层处理完事件。一般情况下，必须先通过waitForHttpResponse()等待http请求返回响应数据，才调用本函数等待UI处理完返回的数据
     */
    protected boolean waitForEventProcessed(HttpRequestUnit hru) {
        long lTimeOut = hru.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1000 * 1;
            } catch (InterruptedException e) {
                hru.getEvent().setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
                e.printStackTrace();
            }
            if (hru.getEvent().isEventProcessed()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 等待UI层处理完事件。一般情况下，必须先通过waitForHttpResponse()等待http请求返回响应数据，才调用本函数等待UI处理完返回的数据
     */
    protected boolean waitForEventProcessed(BaseSQLiteEvent event) {
        long lTimeOut = event.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1l;
            } catch (InterruptedException e) {
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
                e.printStackTrace();
            }
            if (event.isEventProcessed()) {
                return true;
            }
        }
        return false;
    }

    public static void caseLog(String s) {
        System.out.println("\n---------------------------------------" + s + "-----------------------------------------------");
    }

    /** 生成随机DB名称。它必须是以字母或下划线开头，和JAVA的变量命名一样 */
    public static String GenerateDBName() {
        String dbName = "nbr_test_" + UUID.randomUUID().toString().substring(1, 7);
        System.out.println("生成的DB name为：" + dbName);
        return dbName;
    }

    /** 生成一个合法的手机号码，以13开头，共11位 */
    protected static String getValidStaffPhone() throws InterruptedException {
        String str = "%09d";
        String phone = String.format(str, System.currentTimeMillis() % 1000000000);
        Thread.sleep(1);
        return "13" + phone;
    }
}
