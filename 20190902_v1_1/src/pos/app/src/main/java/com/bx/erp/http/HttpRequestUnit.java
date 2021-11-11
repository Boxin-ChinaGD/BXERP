package com.bx.erp.http;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public abstract class HttpRequestUnit implements Callback {
    private Logger log = Logger.getLogger(this.getClass());
    public final long TIME_OUT = GlobalController.HTTP_REQ_Timeout * 1000;

    public void setTimeout(long l) {
//        if (l <= 0) {
//            throw new RuntimeException("超时值必须大于0！");
//        }
        this.lTimeout = GlobalController.HTTP_REQ_Timeout * 1000;
    }

    public long getTimeout() {
        if (lTimeout != 0L) {
            return lTimeout;
        }
        return TIME_OUT;
    }

    /**
     * 从请求发起到产生响应消耗的时间如果超过这个时间，则不会再处理网络返回的数据。服务器可能处理了此请求，也可能没有处理此请求
     */
    protected long lTimeout = GlobalController.HTTP_REQ_Timeout * 1000;

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    protected Date dateStart;
    protected Date dateEnd;

    protected boolean bPostEventToUI;

    public void setbPostEventToUI(boolean bPostEventToUI) {
        this.bPostEventToUI = bPostEventToUI;
    }

    public BaseHttpEvent getEvent() {
        return event;
    }

    public void setEvent(BaseHttpEvent event) {
        this.event = event;
    }

    protected BaseHttpEvent event;

    protected Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    /**
     * RetrieveNC，后面带C的请求的是普通Action。不带C请求的是SyncAction
     */
    public enum EnumRequestType {
        ERT_RetailTrade_Create("ERT_RetailTrade_Create", 0), //
        ERT_Barcodes_RetrieveN("ERT_Barcodes_RetrieveN", 1),
        ERT_Barcodes_Retrieve1("ERT_Barcodes_Retrieve1", 2),
        ERT_SmallSheet_Create("ERT_SmallSheet_Create", 3),
        ERT_Commodity_RetrieveN("ERT_Commodity_RetrieveN", 4),
        ERT_Commodity_Retrieve1("ERT_Commodity_Retrieve1", 5),
        ERT_RetailTrade_RetrieveN("ERT_RetailTrade_RetrieveN", 6),
        ERT_RetailTradeCommodity_RetrieveN("ERT_RetailTradeCommodity_RetrieveN", 7),
        ERT_SmallSheet_RetrieveN("ERT_SmallSheet_RetrieveN", 8),
        ERT_RequestSmallSheetText("ERT_RequestSmallSheetText", 9),
        ERT_SmallSheet_Update("ERT_SmallSheet_Update", 10),
        ERT_SmallSheet_Delete("ERT_SmallSheet_Delete", 11),
        ERT_GetToken("ERT_GetToken", 12),
        ERT_RetrieveN("ERT_RetrieveN", 13),
        ERT_NtpSync("ERT_NtpSync", 14),
        ERT_CommodityInfo_RetrieveN("ERT_CommodityInfo_RetrieveN", 15),
        ERT_PurchasingOrderInfo_Create("ERT_PurchasingOrderInfo_Create", 16),
        ERT_ApprovePurchasingOrder("ERT_ApprovePurchasingOrder", 17),
        ERT_Warehousing_Create("ERT_Warehousing_Create", 18),
        ERT_CommEx_RetrieveN("ERT_CommEx_RetrieveN", 19),
        ERT_ApproveWarehousing("ERT_ApproveWarehousing", 20),
        ERT_CreateReturnCommoditySheet("ERT_CreateReturnCommoditySheet", 21),
        ERT_ApproveReturnCommoditySheet("ERT_ApproveReturnCommoditySheet", 22),
        ERT_FeedBack("ERT_FeedBack", 23),
        ERT_PosLogin("ERT_PosLogin", 24),
        ERT_StaffLogin("ERT_StaffLogin", 25),
        ERT_RetailTradeAggregation_Create("ERT_RetailTradeAggregation_Create", 26),
        ERT_RetailTradeAggregation_RetrieveN("ERT_RetailTradeAggregation_RetrieveN", 27),
        ERT_RetailTradeAggregation_Delete("ERT_RetailTradeAggregation_Delete", 28),
        ERT_StaffLogout("ERT_StaffLogout", 29),
        ERT_Staff_Create("ERT_Staff_Create", 30),
        ERT_Staff_RetrieveN("ERT_Staff_RetrieveN", 31),
        ERT_StaffGetToken("ERT_StaffGetToken", 32),
        ERT_PosGetToken("ERT_PosGetToken", 32),
        ERT_Brand_RetrieveN("ERT_Brand_RetrieveN", 33),
        ERT_Brand_Create("ERT_Brand_Create", 34),
        ERT_Brand_Update("ERT_Brand_Update", 35),
        ERT_RetailTrade_Delete("ERT_RetailTrade_Delete", 36),
        ERT_Commodity_Create("ERT_Commodity_Create", 37),
        ERT_Brand_RetrieveNC("ERT_Brand_RetrieveNC", 38),
        ERT_Commodity_RetrieveNC("ERT_Commodity_RetrieveNC", 39),
        ERT_Pos_Create("ERT_Pos_Create", 40),
        ERT_Pos_RetrieveN("ERT_Pos_RetrieveN", 41),
        ERT_Pos_RetrieveNC("ERT_Pos_RetrieveNC", 42),
        ERT_Pos_CreateSync("ERT_Pos_CreateSync", 43),
        ERT_CommodityCategory_RetrieveN("ERT_CommodityCategory_RetrieveN", 44),
        ERT_CommodityCategory_Create("ERT_CommodityCategory_Create", 45),
        ERT_CommodityCategory_Update("ERT_CommodityCategory_Update", 46),
        ERT_CommodityCategory_RetrieveNC("ERT_CommodityCategory_RetrieveNC", 47),
        ERT_Commodity_RetrieveInventoryC("ERT_Commodity_RetrieveInventoryC", 48),
        ERT_Barcodes_RetrieveNC("ERT_Barcodes_RetrieveNC", 49),
        ERT_Barcodes_Create("ERT_Barcodes_Create", 50),
        ERT_RetailTrade_RetrieveNC("ERT_RetailTrade_RetrieveNC", 51),
        ERT_SmallSheet_RetrieveNC("ERT_SmallSheet_RetrieveNC", 52),
        ERT_Staff_RetrieveNC("ERT_Staff_RetrieveNC", 53),
        ERT_RetailTrade_RetrieveNOldRetailTrade("ERT_RetailTrade_RetrieveNOldRetailTrade", 54),
        ERT_PackageUnit_RetrieveNC("ERT_PackageUnit_RetrieveNC", 55),
        ERT_ConfigCacheSize_RetrieveN("ERT_ConfigCacheSize_RetrieveN", 56),
//        ERT_VipCategory_RetrieveN("ERT_VipCategory_RetrieveN", 57),s
        ERT_VipCategory_Create("ERT_VipCategory_Create", 58),
        ERT_VipCategory_Update("ERT_VipCategory_Update", 59),
        ERT_VipCategory_RetrieveNC("ERT_VipCategory_RetrieveNC", 60),
        ERT_Staff_Update("ERT_Staff_Update", 61),
        ERT_Staff_Delete("ERT_Staff_Delete", 62),
        ERT_Vip_Create("ERT_Vip_Create", 63),
        ERT_Vip_Update("ERT_Vip_Update", 64),
        ERT_Vip_Delete("ERT_Vip_Delete", 65),
        ERT_Vip_RetrieveN("ERT_Vip_RetrieveN", 66),
        ERT_Commodity_Retrieve1C("ERT_Commodity_Retrieve1C", 67),
        ERT_Warehousing_RetrieveNC("ERT_Warehousing_RetrieveNC", 68),
        ERT_ConfigGeneral_RetrieveNC("ERT_ConfigGeneral_RetrieveNC", 69),
        ERT_ConfigGeneral_Update("ERT_ConfigGeneral_Update", 70),
        ERT_ConfigCacheSize_RetrieveNC("ERT_ConfigCacheSize_RetrieveNC", 71),
        ERT_Vip_RetrieveNC("ERT_Vip_RetrieveNC", 72),
        ERT_Promotion_RetrieveNC("ERT_Promotion_RetrieveNC", 73),
        ERT_Promotion_RetrieveN("ERT_Promotion_RetrieveN", 74),
        ERT_Promotion_Update("ERT_Promotion_Update", 75),
        ERT_Promotion_Create("ERT_Promotion_Create", 76),
        ERT_RetailTrade_CreateN("ERT_RetailTrade_CreateN", 77),
        ERT_WXPay_Refund("ERT_WXPay_Refund", 78),
        ERT_WXPay_MicroPay("ERT_WXPay_MicroPay", 79),
        ERT_Vip_RetrieveNVipConsumeHistory("ERT_Vip_RetrieveNVipConsumeHistory", 80),
        ERT_PurchasingOrder_Retrieve1("ERT_PurchasingOrder_Retrieve1", 81),
        ERT_POS_Retrieve1BySN("ERT_POS_Retrieve1BySN", 82),
        ERT_RetailTradePromoting_Create("ERT_RetailTradePromoting_Create", 83),
        ERT_POS_Delete("ERT_POS_Delete", 84),
        ERT_Shop_Create("ERT_Shop_Create", 85),
        ERT_Shop_Delete("ERT_Shop_Delete", 86),
        ERT_Company_Delete("ERT_Company_Delete", 87),
//        ERT_Company_Create("ERT_Company_Create", 88),
        ERT_RetailTradePromoting_CreateN("ERT_RetailTradePromoting_CreateN", 89),
        ERT_Staff_ResetPassword("ERT_Staff_ResetPassword", 90),
        ERT_Company_RetrieveN("ERT_Company_RetrieveN", 91),
        ERT_WXPay_Reverse("ERT_WXPay_Reverse", 92),
        ERT_WXPay_OrderQuery("ERT_WXPay_OrderQuery", 93),
        ERT_Company_UploadBusinessLicensePicture("ERT_Company_UploadBusinessLicensePicture", 94),
        ERT_Staff_RetrieveResigned("ERT_Staff_RetrieveResigned", 95),
        ERT_BXConfigGeneral_RetrieveNC("ERT_BXConfigGeneral_RetrieveNC", 96),
        ERT_WxCoupon_RetrieveNC("ERT_WxCoupon_RetrieveNC", 97),
        ERT_Vip_RetrieveNByMobileOrVipCardSN("ERT_Vip_RetrieveNByMobileOrVipCardSN", 98),
        ERT_Staff_Ping("ERT_Staff_Ping", 99),
        ERT_Coupon_RetrieveNC("ERT_Coupon_RetrieveNC", 100),
        ERT_CouponCode_RetrieveNC("ERT_CouponCode_RetrieveNC", 101);

        private String name;
        private int index;

        private EnumRequestType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumRequestType c : EnumRequestType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

    }

    public abstract EnumRequestType getEnumRequestType();

    @Override
    public void onFailure(Call call, IOException e) {
        this.event.setEventProcessed(true); //终止UI层的等待
        //...
        log.info(" Http onFailure，向服务器发送请求失败。异常信息：" + e.getMessage());
        dateEnd = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference); //用于计算超时
        if (isTimeout(call)) {
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
        } else {
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
        }
        this.event.setLastErrorMessage(BaseHttpBO.ERROR_MSG_Network);
        //
        GlobalController.getInstance().setSessionID(null);
        //
        EventBus.getDefault().post(event);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        log.info("请求后返回的response：" + response);
        dateEnd = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        if (isTimeout(call)) {
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
            return;
        }
        log.info(" Http onResponse，收到服务器的响应");
        if (NtpHttpBO.bForNtpOnly) {// 在同步的时候设置POS接收到答复的时间,用于POS机与服务器同步时间
            event.setData(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
            NtpHttpBO.bForNtpOnly = false;
        }
        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        if (bPostEventToUI) {
            event.setRequestType(getEnumRequestType());
            event.setResponseData(response.body().string());
            event.setResponse(response);
            //...设置Event状态？？？
            EventBus.getDefault().post(event);
        }
    }

    protected boolean isTimeout(Call call) {
        long aa = dateEnd.getTime() - dateStart.getTime();
        if (dateEnd.getTime() - dateStart.getTime() >= lTimeout) {
            log.info("http请求超时。请求信息：" + this.toString() + "    \r\n\tCall=" + call.request().toString());
            if (call != null) {
                log.info("\t\t\tCall=" + call.request().toString());
            }
            return true;
        }
        return false;
    }

    public boolean hasHttpResponse() {
        if (dateStart != null && dateEnd != null && dateEnd.getTime() >= dateStart.getTime()) {
            return true;
        }
        return false;
    }
}
