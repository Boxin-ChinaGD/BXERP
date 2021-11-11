package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.CreateStaff;
import com.bx.erp.http.request.DeleteStaff;
import com.bx.erp.http.request.FeedBack;
import com.bx.erp.http.request.RetrieveNStaff;
import com.bx.erp.http.request.RetrieveNStaffC;
import com.bx.erp.http.request.RetrieveResignedStaff;
import com.bx.erp.http.request.StaffPing;
import com.bx.erp.http.request.StaffResetPassword;
import com.bx.erp.http.request.UpdateStaff;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Staff;
import com.bx.erp.view.activity.BaseActivity;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class StaffHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public StaffHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行StaffHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));
        Staff staff = (Staff) bm;
        RequestBody body = new FormBody.Builder()
                .add(staff.field.getFIELD_NAME_phone(), staff.getPhone())
                .add(staff.field.getFIELD_NAME_name(), staff.getName())
                .add(staff.field.getFIELD_NAME_pwdEncrypted(), staff.getPwdEncrypted())
                .add(staff.field.getFIELD_NAME_departmentID(), String.valueOf(staff.getDepartmentID()))
                .add(staff.field.getFIELD_NAME_shopID(), String.valueOf(staff.getShopID()))
                .add(staff.field.getFIELD_NAME_isFirstTimeLogin(), String.valueOf(staff.getIsFirstTimeLogin()))
                .add(staff.field.getFIELD_NAME_roleID(), String.valueOf(staff.getRoleID()))//...
                .add(staff.field.getFIELD_NAME_returnSalt(), String.valueOf(staff.getReturnSalt())) //iReturnSalt
                //不知道具体意思，先注释
//                .add(staff.getFIELD_NAME_int3(), String.valueOf(staff.getInt3()))//...
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateStaff();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建Staff...");

        return true;
    }
    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行StaffHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        Staff staff = (Staff) bm;
        RequestBody body = new FormBody.Builder()
                .add(staff.field.getFIELD_NAME_phone(), staff.getPhone())
                .add(staff.field.getFIELD_NAME_ICID(), staff.getICID())
                .add(staff.field.getFIELD_NAME_weChat(), staff.getWeChat())
                .add(staff.field.getFIELD_NAME_name(), staff.getName())
                .add(staff.field.getFIELD_NAME_roleID(), String.valueOf(staff.getRoleID()))
                .add(staff.field.getFIELD_NAME_departmentID(), String.valueOf(staff.getDepartmentID()))
                .add(staff.field.getFIELD_NAME_shopID(), String.valueOf(staff.getShopID()))
                .add(staff.field.getFIELD_NAME_ID(), String.valueOf(staff.getID()))
                .add(staff.field.getFIELD_NAME_status(), String.valueOf(staff.getStatus()))
                .add(staff.field.getFIELD_NAME_roleID(), String.valueOf(staff.getRoleID())) //iRoleID
                //不知道具体意思，先注释
//                .add(staff.getFIELD_NAME_int3(), String.valueOf(staff.getInt3())) //iReturnSalt
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_Update)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdateStaff();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);


        log.info("正在发送Update请求到服务器...");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行StaffHttpBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_RetrieveN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNStaff();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        log.info("正在发送RN请求到服务器...");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行StaffHttpBO的deleteAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_Delete + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new DeleteStaff();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        return true;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行StaffHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_FeedbackURL_FRONT + feedbackIDs + Staff.HTTP_FeedbackURL_BEHIND)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new FeedBack();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("通知服务器该POS机已经同步了Staff. ID: " + feedbackIDs);

        return true;
    }

    public void pingEx() throws Exception {
        log.info("正在执行StaffHttpBO的pingEx");
        httpEvent.setEventProcessed(false);
        Request request = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_Ping)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        HttpRequestUnit hru1 = new StaffPing();
        hru1.setRequest(request);
        hru1.setTimeout(TIME_OUT);
        hru1.setbPostEventToUI(true);
        hru1.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru1);

        log.info("正在向服务器请求保持Session连接...");
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行StaffHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        RequestBody body = new FormBody.Builder().build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_RetrieveNC + bm.getPageIndex() + Staff.HTTP_STAFF_PageSize + bm.getPageSize())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNStaffC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送RN请求到服务器...");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    public boolean resetMyPassword(Staff staff) {
        log.info("正在执行StaffHttpBO的resetMyPassword，currentStaff=" + staff);

        RequestBody body = new FormBody.Builder()
                .add(staff.field.getFIELD_NAME_ID(), String.valueOf(BaseActivity.retailTradeAggregation.getStaffID())) //TODO
                .add(staff.field.getFIELD_NAME_phone(), staff.getPhone())
                .add(staff.field.getFIELD_NAME_oldPwdEncrypted(), staff.getOldPwdEncrypted())
                .add(staff.field.getFIELD_NAME_newPwdEncrypted(), staff.getNewPwdEncrypted())
                .build();
        Request request = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_ResetMyPassword)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new StaffResetPassword();
        hru.setRequest(request);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Staff修改密码的请求");
        return true;
    }

    public boolean retrieveResigned(BaseModel bm) {
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Staff.HTTP_STAFF_RetrieveResigned + "1" + Staff.HTTP_STAFF_PageSize + BaseHttpBO.PAGE_SIZE_MAX) //...
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveResignedStaff();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送查询离职员工请求到服务器...");

        return true;
    }
}
