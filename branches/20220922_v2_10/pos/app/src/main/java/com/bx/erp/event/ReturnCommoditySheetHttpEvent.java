package com.bx.erp.event;


import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReturnCommoditySheetHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public ReturnCommoditySheetHttpEvent() {

    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_CreateReturnCommoditySheet:
                    handleCreate(jsonObject);
                    break;
                case ERT_ApproveReturnCommoditySheet:
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
            JSONObject joSmallSheetFrame = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            ReturnCommoditySheet rcs = new ReturnCommoditySheet();
            if (rcs.parse1(joSmallSheetFrame.toString()) == null) {
                log.info("CREATE:Failed to parse smallsheet!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_ReturnCommoditySheet_Create得到的对象是：" + rcs.toString());
                setBaseModel1(rcs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步失败，失败原因：" + e.getMessage());
        }
    }
}
