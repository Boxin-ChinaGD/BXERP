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
import wpos.http.request.ApproveReturnCommoditySheet;
import wpos.http.request.CreateReturnCommoditySheet;
import wpos.model.BaseModel;
import wpos.model.Commodity;
import wpos.model.ReturnCommoditySheet;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("returnCommoditySheetHttpBO")
@Scope("prototype")
public class ReturnCommoditySheetHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public ReturnCommoditySheetHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public ReturnCommoditySheetHttpBO(){}

    public boolean approveAsync(BaseModel bm) {
        log.info("正在执行ReturnCommoditySheetHttpBO的approveAsync，bm=" +(bm == null ? null : bm.toString()));

        RequestBody body = new FormBody.Builder()
                .add(bm.field.getFIELD_NAME_ID(), String.valueOf(bm.getID()))
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + ReturnCommoditySheet.HTTP_ReturnCommoditySheety_APPROVE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new ApproveReturnCommoditySheet();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Approve_ReturnCommoditySheet请求到服务器....");

        return true;
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

    private final String KEY_COMMIDS = "commIDs";
    private final String KEY_RCSCNOS = "rcscNOs";
    private final String KEY_COMMPRICES = "commPrices";
    private final String KEY_RCSCSPECIFICATIONS = "rcscSpecifications";
    private final String KEY_BARCODEIDS = "barcodeIDs";

    @Override
    protected boolean doCreateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行ReturnCommoditySheetHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        ReturnCommoditySheet rcs = (ReturnCommoditySheet) bm;
        StringBuffer commIDs = new StringBuffer();
        StringBuffer rcscNOs = new StringBuffer();
        StringBuffer commPrices = new StringBuffer();
        StringBuffer rcscSpecifications = new StringBuffer();
        StringBuffer barcodeIDs = new StringBuffer();
        List<Commodity> commodityList = (List<Commodity>) rcs.getListSlave1();
        for (int i = 0; i < commodityList.size(); i++) {
            Commodity commodity = commodityList.get(i);
            commIDs.append(commodity.getID() + ",");
            rcscNOs.append(commodity.getNO() + ",");
            commPrices.append(commodity.getPriceRetail() + ",");
            rcscSpecifications.append(commodity.getSpecification() + ",");
            barcodeIDs.append(commodity.getBarcodeID() + ","); //条形码ID放入int1中
        }

        RequestBody body = new FormBody.Builder()
                .add(KEY_COMMIDS, commIDs.toString())
                .add(KEY_RCSCNOS, rcscNOs.toString())
                .add(KEY_COMMPRICES, commPrices.toString())
                .add(KEY_RCSCSPECIFICATIONS, rcscSpecifications.toString())
                .add(rcs.field.getFIELD_NAME_providerID(), String.valueOf(rcs.getProviderID()))
                .add(KEY_BARCODEIDS, barcodeIDs.toString())
                .build();

        Request request = new Request.Builder()
                .url(Configuration.HTTP_IP + rcs.HTTP_ReturnCommoditySheety_CREATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateReturnCommoditySheet();
        hru.setRequest(request);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求创建ReturnCommoditySheet...");

        return true;
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
        return false;
    }
}
