package com.bx.erp.event;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CouponHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误：" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_Coupon_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                default:
                    log.info("Not yet implemented!");
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);

        //让调用者不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaCoupon = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            List<BaseModel> couponList = (List<BaseModel>) new Coupon().parseN(jaCoupon);
            setCount(count);
            if (couponList.size() == 0) {
                log.info("RetrieveNC:服务器中没有数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                setCount(null);
            }
            log.info("retrieveNC得到的对象列表是：" + couponList.toString());

            setListMasterTable(couponList);
            if (sqliteBO != null) {
                sqliteBO.onResultRetrieveNC(couponList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的Coupon出错,错误信息：" + e.getMessage());
        }
    }
}
