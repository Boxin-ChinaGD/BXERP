package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.DeleteCompany;
import com.bx.erp.http.request.RetrieveNCompany;
import com.bx.erp.http.request.UploadBLPCompany;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


public class CompanyHttpBO extends BaseHttpBO{
    private Logger log = Logger.getLogger(this.getClass());
    
    public CompanyHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    protected boolean doCreateNSync(int iUseCaseID, List<BaseModel> list) {
        return false;
    }

    @Override
    protected boolean doCreateNAsync(int iUseCaseID, List<BaseModel> list) {
        return false;
    }

    @Override
    protected boolean doCreateAsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doCreateAsync(int iUseCaseID, BaseModel bm) {
//        log.info("正在执行CompanyHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));
//
//        Company c = (Company) bm;
//
//        RequestBody body = new FormBody.Builder()
//                .add(c.field.getFIELD_NAME_name(), c.getName())
//                .add(c.field.getFIELD_NAME_businessLicenseSN(), c.getBusinessLicenseSN())//
//                .add(c.field.getFIELD_NAME_businessLicensePicture(), c.getBusinessLicensePicture())//
//                .add(c.field.getFIELD_NAME_bossName(), c.getBossName())
//                .add(c.field.getFIELD_NAME_bossPhone(), c.getBossPhone())//
//                .add(c.field.getFIELD_NAME_bossPassword(), c.getBossPassword())
//                .add(c.field.getFIELD_NAME_bossWechat(), c.getBossWechat())//
//                .add(c.field.getFIELD_NAME_dbName(), c.getDbName())//
//                .add(c.field.getFIELD_NAME_key(), c.getKey())//
//                .add(c.field.getFIELD_NAME_status(), String.valueOf(c.getStatus()))
//                .add(c.field.getFIELD_NAME_dbUserName(), c.getDbUserName())
//                .add(c.field.getFIELD_NAME_dbUserPassword(), c.getDbUserPassword())
//                .add(c.field.getFIELD_NAME_returnObject(), String.valueOf(c.getReturnObject()))
////                .add(c.getFIELD_NAME_int3(), "1")//POS模拟创建Company使用到的
//                .build();
//
//        Request req = new Request.Builder()
//                .url(Configuration.HTTP_IP + c.HTTP_Company_Create)
//                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
//                .post(body)
//                .build();
//        HttpRequestUnit hru = new CreateCompany();
//        hru.setRequest(req);
//        hru.setTimeout(TIME_OUT);
//        hru.setbPostEventToUI(true);
//        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        hru.setEvent(httpEvent);
//        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);
//
//        log.info("正在发送创建Company的请求...");
//
//        return true;

        return  false;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
       return false;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CompanyHttpBO的deleteAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + new Company().HTTP_Company_Delete + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new DeleteCompany();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送删除Company的请求...");

        return true;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
