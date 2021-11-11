package wpos.bo;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.CreateCommodity;
import wpos.http.request.FeedBack;
import wpos.http.request.Retrieve1CommodityC;
import wpos.http.request.RetrieveCommodityInventoryC;
import wpos.http.request.RetrieveNCommodity;
import wpos.http.request.RetrieveNCommodityC;
import wpos.model.BaseModel;
import wpos.model.Commodity;
import wpos.model.CommodityType;
import wpos.model.ErrorInfo;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("commodityHttpBO")
@Scope("prototype")
public class CommodityHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public CommodityHttpBO(){}

    public CommodityHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行CommodityHttpBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        Commodity commodity = (Commodity) bm;

        RequestBody body = new FormBody.Builder()
                .add(commodity.field.getFIELD_NAME_multiPackagingInfo(), commodity.getMultiPackagingInfo()) //...原String1
                .add(commodity.field.getFIELD_NAME_priceRetail(), String.valueOf(commodity.getPriceRetail()))
                .add(commodity.field.getFIELD_NAME_name(), commodity.getName())
                .add(commodity.field.getFIELD_NAME_shortName(), commodity.getShortName())
                .add(commodity.field.getFIELD_NAME_specification(), commodity.getSpecification())
                .add(commodity.field.getFIELD_NAME_packageUnitID(), String.valueOf(commodity.getPackageUnitID()))
                .add(commodity.field.getFIELD_NAME_purchasingUnit(), commodity.getPurchasingUnit())
                .add(commodity.field.getFIELD_NAME_brandID(), String.valueOf(commodity.getBrandID()))
                .add(commodity.field.getFIELD_NAME_categoryID(), String.valueOf(commodity.getCategoryID()))
                .add(commodity.field.getFIELD_NAME_mnemonicCode(), commodity.getMnemonicCode())
                .add(commodity.field.getFIELD_NAME_pricingType(), String.valueOf(commodity.getPricingType()))
                .add(commodity.field.getFIELD_NAME_priceVIP(), String.valueOf(commodity.getPriceVIP()))
                .add(commodity.field.getFIELD_NAME_priceWholesale(), String.valueOf(commodity.getPriceWholesale()))
                .add(commodity.field.getFIELD_NAME_canChangePrice(), String.valueOf(commodity.getCanChangePrice()))
                .add(commodity.field.getFIELD_NAME_ruleOfPoint(), String.valueOf(commodity.getRuleOfPoint()))
                .add(commodity.field.getFIELD_NAME_picture(), commodity.getPicture())
                .add(commodity.field.getFIELD_NAME_shelfLife(), String.valueOf(commodity.getShelfLife()))
                .add(commodity.field.getFIELD_NAME_returnDays(), String.valueOf(commodity.getReturnDays()))
                .add(commodity.field.getFIELD_NAME_purchaseFlag(), String.valueOf(commodity.getPurchaseFlag()))
                .add(commodity.field.getFIELD_NAME_refCommodityID(), String.valueOf(commodity.getRefCommodityID()))
                .add(commodity.field.getFIELD_NAME_refCommodityMultiple(), String.valueOf(commodity.getRefCommodityMultiple()))
                .add(commodity.field.getFIELD_NAME_tag(), commodity.getTag())
                .add(commodity.field.getFIELD_NAME_NO(), String.valueOf(commodity.getNO()))
                .add(commodity.field.getFIELD_NAME_nOStart(), String.valueOf(commodity.getnOStart()))
                .add(commodity.field.getFIELD_NAME_purchasingPriceStart(), String.valueOf(commodity.getPurchasingPriceStart()))
                .add(commodity.field.getFIELD_NAME_type(), String.valueOf(commodity.getType()))
                .add(commodity.field.getFIELD_NAME_providerIDs(), commodity.getProviderIDs()) //... providerIDs
                .add(commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex() ? commodity.field.getFIELD_NAME_subCommodityInfo() : "", commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex() ? commodity.getSubCommodityInfo() : "")
                .add(commodity.field.getFIELD_NAME_returnObject(), String.valueOf(commodity.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Commodity.HTTP_Commodity_CREATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateCommodity();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送创建Commodity的请求...");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityHttpBO的retrieve1AsyncC，bm=" + (bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Commodity.HTTP_Commodity_Retrieve1C + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new Retrieve1CommodityC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求查询一个Commodity");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int useCaseID, BaseModel bm) {
        //...现阶段只是测试使用暂不实现 TODO
        return false;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityHttpBO的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Commodity.HTTP_Commodity_RETRIEVEN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNCommodity();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求需要同步的Commodity");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        //...现阶段只是测试使用暂不实现 TODO
        return false;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行CommodityHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        httpEvent.setEventProcessed(false);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Commodity.commodityfeedbackURL_FRONT + feedbackIDs + Commodity.commodityfeedbackURL_BEHIND)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new FeedBack();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setHttpBO(this);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("通知服务器改POS机已经同步了commodityID：" + feedbackIDs);

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityHttpBO的retrieveNAsyncC，bm=" + (bm == null ? null : bm.toString()));
        // 判断SessionID是否为null，如果为null，返回false
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        Request req = null;
        HttpRequestUnit hru = null;
        httpEvent.setEventProcessed(false);

        switch (iUseCaseID) {
            case CASE_Commodity_RetrieveInventory:
                Commodity commodity = (Commodity) bm;

                req = new Request.Builder()
                        .url(Configuration.HTTP_IP + Commodity.HTTP_Commodity_retrieveInventory + commodity.getBarcode()
                                + Commodity.HTTP_Commodity_RETRIEVENC_ShopID + Constants.shopID
                        )
                        .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                        .build();

                hru = new RetrieveCommodityInventoryC();
                hru.setRequest(req);
                hru.setTimeout(TIME_OUT);
                hru.setbPostEventToUI(true);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                httpEvent.setHttpBO(this);
                hru.setEvent(httpEvent);
                HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                log.info("正在请求服务器返回CommodityID和CommodityNO");
                break;
            default:
                RequestBody body = new FormBody.Builder()
                        .build();
                req = new Request.Builder()
                        .url(Configuration.HTTP_IP //
                                + Commodity.HTTP_Commodity_RETRIEVENC_PageIndex + bm.getPageIndex() //
                                + Commodity.HTTP_Commodity_RETRIEVENC_PageSize + bm.getPageSize() //
                                + Commodity.HTTP_Commodity_RETRIEVENC_TypeStatus //
                        )
                        .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                        .post(body)
                        .build();
                hru = new RetrieveNCommodityC();
                hru.setRequest(req);
                hru.setTimeout(TIME_OUT);
                hru.setbPostEventToUI(true);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                httpEvent.setHttpBO(this);
                hru.setEvent(httpEvent);
                HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                log.info("正在请求服务器返回Commodity");
                break;
        }
        return true;
    }
}
