package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;

import java.util.List;

public abstract class BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());
    protected final long TIME_OUT = GlobalController.HTTP_REQ_Timeout * 1000;

    public static final String ERROR_MSG_Network = "网络错误,请检查网络连接!";

    /**
     * 用于HttpBO请求服务器时一下特殊CASE的字段检查
     */
    public static final int CASE_CheckModel_Normal = 12306;
    public static final int CASE_CheckStaff_ResetMyPassword = CASE_CheckModel_Normal + 1;

    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_Default).create();
    }

    public static final String COOKIE = "cookie";

    /**
     * TODO 待review
     */
    public static final int INVALID_CASE_ID = -100000001;
    public static final int CASE_Normal = 10086;
    public static final int CASE_PurchasingOrder_Approve = CASE_Normal + 1;
    public static final int CASE_Warehousing_Approve = CASE_Normal + 2;
    public static final int CASE_Commodity_RetrieveInventory = CASE_Normal + 3;
    public static final int CASE_Commodity_CreateComposition = CASE_Normal + 4;
    public static final int CASE_Commodity_CreateMultiPackaging = CASE_Normal + 5;
    public static final int CASE_Commodity_CreateService = CASE_Normal + 6;
    public static final int CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC= CASE_Normal + 7;
    public static final int CASE_Vip_retrieveNByMobileOrVipCardSNEx = CASE_Normal + 8;

    /**
     * 服务器端页码是从1开始的，不是从0开始
     */
    public static final String FIRST_PAGE_Index_Default = "1";
    /**
     * 显示列表时默认显示的条数
     */
    public static final String PAGE_SIZE_Default = "10";
    /**
     * 一页一页下载时的page size。一般用在数据较多时，分批下载
     */
    public static final String PAGE_SIZE_LoadPageByPage = "50";
    /**
     * 下载所有数据时的Page size。一般用在下载数据不多的表，比如下载所有商品类别
     */
    public static final String PAGE_SIZE_LoadAll = "100000000";

    /**
     *  限制每次下载数据的最大值
     */
    public static String PAGE_SIZE_MAX = "50";

    protected Context ctx;
    protected BaseSQLiteEvent sqLiteEvent;

    public BaseHttpEvent getHttpEvent() {
        return httpEvent;
    }

    public void setHttpEvent(BaseHttpEvent httpEvent) {
        this.httpEvent = httpEvent;
    }

    protected BaseHttpEvent httpEvent;


    public String getPwdEncrypted() {
        return pwdEncrypted;
    }

    public void setPwdEncrypted(String pwdEncrypted) {
        this.pwdEncrypted = pwdEncrypted;
    }

    protected String pwdEncrypted;

    /**
     * 向服务器发送http请求:创建N个对象
     */
    protected abstract boolean doCreateNSync(int iUseCaseID, List<BaseModel> list);

    public boolean createNSync(int iUseCaseID, List<BaseModel> list) {
        if (list != null){
            for(BaseModel bm : list){
                if (!checkCreate(iUseCaseID, bm)){
                    return false;
                }
            }
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doCreateNSync(iUseCaseID, list);
    }

    /**
     * 向服务器发送http请求:创建N个对象
     */
    protected abstract boolean doCreateNAsync(int iUseCaseID, List<BaseModel> list);

    public boolean createNAsync(int iUseCaseID, List<BaseModel> list){
        if (list != null){
            for(BaseModel bm : list){
                if (!checkCreate(iUseCaseID, bm)){
                    return false;
                }
            }
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doCreateNAsync(iUseCaseID, list);
    }

    /**
     * 向服务器发送http请求:创建对象。C代表调用的是普通Action
     */
    protected abstract boolean doCreateAsyncC(int iUseCaseID, BaseModel bm);

    public boolean createAsyncC(int iUseCaseID, BaseModel bm){
        if (!checkCreate(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doCreateAsyncC(iUseCaseID, bm);
    }

    /**
     * 向服务器发送http请求:创建对象
     */
    protected abstract boolean doCreateAsync(int iUseCaseID, BaseModel bm);

    public boolean createAsync(int iUseCaseID, BaseModel bm){
        if (!checkCreate(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doCreateAsync(iUseCaseID, bm);
    }

     /**
     * 向服务器发送http请求:修改对象
     */
    protected abstract boolean doUpdateAsync(int iUseCaseID, BaseModel bm);

    public boolean updateAsync(int iUseCaseID, BaseModel bm){
        if (!checkUpdate(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            httpEvent.setLastErrorMessage(ERROR_MSG_Network);
            return false;
        }
        return doUpdateAsync(iUseCaseID, bm);
    }

    /**
     * 向服务器发送Http请求: 查找所有需要同步对象
     */
    protected abstract boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm);

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm){
        if (!checkRetrieveN(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doRetrieveNAsync(iUseCaseID, bm);
    }

    /**
     * 向服务器发送Http请求: 删除对象
     */
    protected abstract boolean doDeleteAsync(int iUseCaseID, BaseModel bm);

    public boolean deleteAsync(int iUseCaseID, BaseModel bm){
        if (!checkDelete(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doDeleteAsync(iUseCaseID, bm);
    }

    protected Context context;

    /**
     * 想服务器发送Http请求: feedback 告诉服务器已经同步了哪些frame
     */
    public abstract boolean feedback(String feedbackIDs);

    /*
    向服务器发送Http请求：查找所有的对象
     */
    protected abstract boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm);

    public boolean retrieveNAsyncC(int iUseCaseID, BaseModel bm){
        if (!checkRetrieveN(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doRetrieveNAsyncC(iUseCaseID, bm);
    }

    protected abstract boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm);

    public boolean retrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        if (!checkRetrieve1(iUseCaseID, bm)){
            return false;
        }
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        return doRetrieve1AsyncC(iUseCaseID, bm);
    }

    protected boolean checkCreate(int iUseCaseID, BaseModel bm){
        return doCheckCreate(iUseCaseID, bm);
    }

    protected boolean doCheckCreate(int iUseCaseID, BaseModel bm){
        String err = bm.checkCreate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            sqLiteEvent.setLastErrorMessage(err);
            sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
            sqLiteEvent.setEventProcessed(true);

            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            httpEvent.setLastErrorMessage(err);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_NoAction);
            httpEvent.setEventProcessed(true);
            log.debug("本对象为：" + bm);
            return false;
        }
        return true;
    }

    protected boolean checkUpdate(int iUseCaseID, BaseModel bm){
        return doCheckUpdate(iUseCaseID, bm);
    }

    protected boolean doCheckUpdate(int iUseCaseID, BaseModel bm){
        String err = bm.checkUpdate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            sqLiteEvent.setLastErrorMessage(err);
            sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
            sqLiteEvent.setEventProcessed(true);

            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            httpEvent.setLastErrorMessage(err);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_NoAction);
            httpEvent.setEventProcessed(true);
            log.debug("本对象为：" + bm);
            return false;
        }
        return true;
    }

    protected boolean checkDelete(int iUseCaseID, BaseModel bm){
        return doCheckDelete(iUseCaseID, bm);
    }

    protected boolean doCheckDelete(int iUseCaseID, BaseModel bm){
        if(bm != null){
            String err = bm.checkDelete(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(err);
                sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                sqLiteEvent.setEventProcessed(true);

                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(err);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_NoAction);
                httpEvent.setEventProcessed(true);
                log.debug("本对象为：" + bm);
                return false;
            }
        }
        return true;
    }

    protected boolean checkRetrieve1(int iUseCaseID, BaseModel bm){
        return doCheckRetrieve1(iUseCaseID, bm);
    }

    protected boolean doCheckRetrieve1(int iUseCaseID, BaseModel bm){
        if(bm != null){
            String err = bm.checkRetrieve1(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(err);
                sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                sqLiteEvent.setEventProcessed(true);

                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(err);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_NoAction);
                httpEvent.setEventProcessed(true);
                log.debug("本对象为：" + bm);
                return false;
            }
        }
        return true;
    }

    protected boolean checkRetrieveN(int iUseCaseID, BaseModel bm){
        return doCheckRetrieveN(iUseCaseID, bm);
    }

    protected boolean doCheckRetrieveN(int iUseCaseID, BaseModel bm){
        if (bm != null){
            String err = bm.checkRetrieveN(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(err);
                sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                sqLiteEvent.setEventProcessed(true);

                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(err);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_NoAction);
                httpEvent.setEventProcessed(true);
                log.debug("本对象为：" + bm);
                return false;
            }
        }
        return true;
    }
}
