package com.bx.erp.event;


import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Warehousing;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class WarehousingHttpEvent extends BaseHttpEvent {
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
//                case ERT_Warehousing_RetrieveNC:
//                    handleRetrieveNC(jsonObject);
//                    break;
                case ERT_Warehousing_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_ApproveWarehousing:
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
//
//    private void handleRetrieveNC(JSONObject jsonObject) {
//        try {
//            JSONArray jaCommodity = jsonObject.getJSONArray("commList");
//            List<BaseModel> commoditylist = (List<BaseModel>) new Commodity().parseN(jaCommodity);
//
//            setListMasterTable(commoditylist);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
//    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            String joWarehousing = jsonObject.getString(BaseModel.JSON_OBJECT_KEY);
            Warehousing warehousing = new Warehousing();
            BaseModel baseModel = warehousing.parse1(joWarehousing);

            if (baseModel != null) {
                setBaseModel1(baseModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步warehousin失败。失败原因：" + e.getMessage());
        }
    }
}
