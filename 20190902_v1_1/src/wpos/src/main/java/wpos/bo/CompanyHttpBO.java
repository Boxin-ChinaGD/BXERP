package wpos.bo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.DeleteCompany;
import wpos.model.BaseModel;
import wpos.model.Company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import okhttp3.Request;

@Component("companyHttpBO")
@Scope("prototype")
public class CompanyHttpBO extends BaseHttpBO{
    private Log log = LogFactory.getLog(this.getClass());
    
    public CompanyHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public CompanyHttpBO() {
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
//                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
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
