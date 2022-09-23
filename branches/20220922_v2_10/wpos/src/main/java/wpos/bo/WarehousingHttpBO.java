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
import wpos.http.request.ApproveWarehousing;
import wpos.http.request.CreateWarehousing;
import wpos.model.BaseModel;
import wpos.model.Warehousing;
import wpos.model.WarehousingCommodity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("warehousingHttpBO")
@Scope("prototype")
public class WarehousingHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public WarehousingHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public WarehousingHttpBO(){}

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
        log.info("正在执行WarehousingHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        //...getwarehousingCommList.   定义String。 for循环，拆分数据。StringBuild commPrices; for(CommList){  commPrices.append(comm.getPrice) }
        httpEvent.setEventProcessed(false);
        Warehousing warehousing = (Warehousing) bm;
        List<WarehousingCommodity> warehousingCommodities = (List<WarehousingCommodity>) warehousing.getListSlave1();
        StringBuilder commPrices = new StringBuilder();
        StringBuilder commNOs = new StringBuilder();
        StringBuilder amounts = new StringBuilder();
        StringBuilder commIDs = new StringBuilder();
        StringBuilder barcodeIDS = new StringBuilder();
        StringBuilder commodityNames = new StringBuilder();
        for (int i = 0; i < warehousingCommodities.size(); i++) {
            WarehousingCommodity warehousingCommodity = warehousingCommodities.get(i);
            commPrices.append(String.valueOf(warehousingCommodity.getPrice()) + ",");
            commNOs.append(String.valueOf(warehousingCommodity.getNO()) + ",");
            amounts.append(String.valueOf(warehousingCommodity.getAmount()) + ",");
            commIDs.append(String.valueOf(warehousingCommodity.getCommodityID() + ","));
            commodityNames.append(String.valueOf(warehousingCommodity.getCommodityName() + ","));
            barcodeIDS.append(warehousingCommodity.getBarcodeID() + ",");
        }
        RequestBody body = new FormBody.Builder()
                .add("commPrices", commPrices.toString())
                .add("commNOs", commNOs.toString())
                .add("amounts", amounts.toString())
                .add("barcodeIDs", barcodeIDS.toString()) //... shopRobotTest
                .add("commodityNames", commodityNames.toString())
                .add("commIDs", commIDs.toString())
                .add(warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))
                .add(warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()))
                .add(warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + warehousing.HTTP_WAREHOUSING_CREATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateWarehousing();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setHttpBO(this);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Create_Warehousing请求到服务器....");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int useCaseID, BaseModel bm) {
        log.info("正在执行WarehousingHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (useCaseID) {
            case CASE_Warehousing_Approve:
                httpEvent.setEventProcessed(false);
                Warehousing warehousing = (Warehousing) bm;
                RequestBody body = new FormBody.Builder()
                        .add(warehousing.field.getFIELD_NAME_isModified(), String.valueOf(warehousing.getIsModified()))
                        .add(warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))
                        .add(warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))
                        .build();

                Request req = new Request.Builder()
                        .url(Configuration.HTTP_IP + warehousing.HTTP_WAREHOUSING_APPROVE)
                        .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                        .post(body)
                        .build();
                HttpRequestUnit hru = new ApproveWarehousing();
                hru.setRequest(req);
                hru.setTimeout(TIME_OUT);
                hru.setbPostEventToUI(true);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                httpEvent.setHttpBO(this);
                hru.setEvent(httpEvent);
                HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                log.info("正在发送Approve_Warehousing请求到服务器....");

                return true;
            default:
                return false;
        }
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
//        httpEvent.setEventProcessed(false);
//
//        Warehousing warehousing = (Warehousing) bm;
//        RequestBody body = new FormBody.Builder()
//                .add(warehousing.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))
//                .build();
//        Request req = new Request.Builder()
//                .url(Constants.HTTP_IP + warehousing.HTTP_WAREHOUSING_RETRIEVENCOMM)
//                .addHeader(Constants.COOKIE, GlobalController.getInstance().getStaffLoginHttpBO().getSessionID())
//                .post(body)
//                .build();
//        HttpRequestUnit hru = new RetrieveNCommWarehousing();
//        hru.setRequest(req);
//        hru.setTimeout(TIME_OUT);
//        hru.setbPostEventToUI(true);
//        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        httpEvent.setHttpBO(this);
//        hru.setEvent(httpEvent);
//        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);
//
//        log.info("正在发送RN Warehousing请求到服务器....");

        return false;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
