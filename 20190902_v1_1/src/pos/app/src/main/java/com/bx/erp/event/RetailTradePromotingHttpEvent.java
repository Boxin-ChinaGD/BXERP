package com.bx.erp.event;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RetailTradePromotingHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                setLastErrorCode(lastErrorCode);
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                break;
            }
            switch (getRequestType()) {
                case ERT_RetailTradePromoting_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_RetailTradePromoting_CreateN:
                    handleCreateN(jsonObject);
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

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joPromotion = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            RetailTradePromoting rt = new RetailTradePromoting();
            if (rt.parse1(joPromotion.toString()) == null) {
                log.info("CREATE:Failed to parse RetailTradePromoting!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_RetailTradePromoting_Create得到的对象是：" + rt.toString());
                setBaseModel1(rt);
            }
            if (sqliteBO != null) {
                sqliteBO.onResultCreate(rt);
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreateN(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
            List<BaseModel> retailTradePromotingList = new ArrayList<>();
            retailTradePromotingList = (List<BaseModel>) retailTradePromoting.parseN(jsonArray);
            if (retailTradePromotingList == null) {
                log.info("CreateN: Failed to parse RetailTradePromoting!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_RetailTradePromoting_CreateN得到的对象是：" + retailTradePromotingList.toString());

                setListMasterTable(retailTradePromotingList);

                if (sqliteBO != null) {
                    sqliteBO.onResultCreateN(retailTradePromotingList);
                }
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步失败，失败原因：" + e.getMessage());
        }
    }
}
