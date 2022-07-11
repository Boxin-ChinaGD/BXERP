package com.bx.erp.event;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.wx.coupon.WxCoupon;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WxCouponHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || ErrorInfo.EnumErrorCode.EC_NoError != getLastErrorCode()) {
                break;
            }
            switch (getRequestType()) {
                case ERT_WxCoupon_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                default:
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);
        //让调用者不要再等待
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            if (jsonArray.length() != 0) {
                List<WxCoupon> wxCouponList = (List<WxCoupon>) new WxCoupon().parseN(BaseSQLiteBO.INVALID_CASE_ID, jsonArray);
                setListMasterTable(wxCouponList);
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }
}
