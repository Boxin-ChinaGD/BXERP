package com.bx.erp.event;


import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PurchasingOrder;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchasingOrderHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误：" + lastErrorCode);
                break;
            }
            setBaseModel1(null);
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_PurchasingOrder_Retrieve1:
                    handleRetrieve1(jsonObject);
                    break;
                case ERT_PurchasingOrderInfo_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_ApprovePurchasingOrder:
                    break;
                default:
                    log.info("Not yet implemented!");
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);

        //让调用者不要再等待
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieve1(JSONObject jsonObject) {
//        try {
//            JSONObject json = jsonObject.getJSONObject("purchasingOrderList");
//            PurchasingOrderInfo po = new PurchasingOrderInfo();
//            if (po.parse1(json.toString()) == null) {
//                log.info("CREATE:Failed to parse VipCategory!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                log.info("ERT_PurchasingOrderInfo_Create得到的对象是：" + po.toString());
//
//                setBaseModel1(po);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
        //解析jsonObject， 商品，采购订单。

        try {
            JSONObject jsom = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            PurchasingOrder po = new PurchasingOrder();
            if (po.parse1(jsom.toString()) == null) {
                log.info("RETRIEVE1:Failed to parse purchasing!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                setBaseModel1(po);
                log.info("ERT_PurchasingOrder_Retrieve1得到的对象是：" + po.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject json = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            PurchasingOrder po = new PurchasingOrder();
            if (po.parse1(json.toString()) == null) {
                log.info("CREATE:Failed to parse  PurchasingOrderCategory!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_PurchasingOrderInfo_Create得到的对象是：" + po.toString());

                setBaseModel1(po);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析失败，失败原因：" + e.getMessage());
        }
    }
}
