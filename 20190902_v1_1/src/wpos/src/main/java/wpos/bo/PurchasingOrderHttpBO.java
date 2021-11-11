package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.ApprovePurchasingOrder;
import wpos.http.request.CreatePurchasingOrderInfo;
import wpos.http.request.Retrieve1PurchasingOrder;
import wpos.model.BaseModel;
import wpos.model.PurchasingOrder;
import wpos.model.PurchasingOrderCommodity;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("purchasingOrderHttpBO")
@Scope("prototype")
public class PurchasingOrderHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());
    
    public PurchasingOrderHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public PurchasingOrderHttpBO(){}

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
        log.info("正在执行PurchasingOrderHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        PurchasingOrder purchasingOrder = (PurchasingOrder) bm;
        List<PurchasingOrderCommodity> purchasingOrderCommodities = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        StringBuilder commIDs = new StringBuilder();
        StringBuilder commNOs = new StringBuilder();
        StringBuilder priceSuggestions = new StringBuilder();
        StringBuilder barcodeIDs = new StringBuilder();

        for (int i = 0; i < purchasingOrderCommodities.size(); i++) {
            PurchasingOrderCommodity purchasingCommodity = purchasingOrderCommodities.get(i);
            commIDs.append(purchasingCommodity.getCommodityID() + ",");
            commNOs.append(purchasingCommodity.getCommodityNO() + ",");
            priceSuggestions.append(purchasingCommodity.getPriceSuggestion() + ",");
            barcodeIDs.append(purchasingCommodity.getBarcodeID() + ",");
        }

        RequestBody body = new FormBody.Builder()
                .add(PurchasingOrder.KEY_COMMIDS, commIDs.toString())
                .add(PurchasingOrder.KEY_NOS, commNOs.toString())
                .add(PurchasingOrder.KEY_PRICESUGGESTIONS, priceSuggestions.toString())
                .add(PurchasingOrder.KEY_BARCODEIDS, barcodeIDs.toString())
                .add(purchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))
                .add(purchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark() == null ? "" : purchasingOrder.getRemark())
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + purchasingOrder.HTTP_PURCHASINGORDER_CREATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();

        HttpRequestUnit hru = new CreatePurchasingOrderInfo();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Create_PurchasingOrder请求到服务器....");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int useCaseID, BaseModel bm) {
        log.info("正在执行PurchasingOrderHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (useCaseID) {
            case CASE_PurchasingOrder_Approve:
                //  定义需要的从表数据
                String commIDs = "";
                String barcodeIDs = "";
                String NOs = "";
                String priceSuggestions = "";

                PurchasingOrder po = (PurchasingOrder) bm;
                PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
                // 遍历从表，将需要的数据拼接上去
                for (Object o : po.getListSlave1()) {
                    poc = (PurchasingOrderCommodity) o;

                    commIDs += poc.getCommodityID() + ",";
                    barcodeIDs += poc.getBarcodeID() + ",";
                    NOs += poc.getCommodityNO() + ",";
                    priceSuggestions += poc.getPriceSuggestion() + ",";
                }

                RequestBody body = new FormBody.Builder()
                        .add(po.field.getFIELD_NAME_ID(), String.valueOf(bm.getID()))
                        .add(po.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))
                        .add(po.field.getFIELD_NAME_remark(), String.valueOf(po.getRemark()))
                        .add(PurchasingOrder.KEY_COMMIDS, commIDs)
                        .add(PurchasingOrder.KEY_BARCODEIDS, barcodeIDs)
                        .add(PurchasingOrder.KEY_NOS, NOs)
                        .add(PurchasingOrder.KEY_PRICESUGGESTIONS, priceSuggestions)
                        .build();

                Request req = new Request.Builder()
                        .url(Configuration.HTTP_IP + PurchasingOrder.HTTP_PURCHASINGORDER_APPROVE)
                        .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                        .post(body)
                        .build();
                HttpRequestUnit hru = new ApprovePurchasingOrder();
                hru.setRequest(req);
                hru.setTimeout(TIME_OUT);
                hru.setbPostEventToUI(true);
                httpEvent.setEventProcessed(false);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                hru.setEvent(httpEvent);
                HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                log.info("正在发送Approve_PurchasingOrder请求到服务器....");

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
        return false;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PurchasingOrderHttpBO的retrieve1AsyncC，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + PurchasingOrder.HTTP_PURCHASINGORDER_RETRIEVE1 + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();

        HttpRequestUnit hru = new Retrieve1PurchasingOrder();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送R1请求到服务器...");

        return true;
    }
}
