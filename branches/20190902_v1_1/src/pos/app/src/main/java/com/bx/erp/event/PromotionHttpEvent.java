package com.bx.erp.event;


import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class
PromotionHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }

            String rsp = getResponseData();
            log.info("服务器返回的数据：" + rsp);

            JSONObject jsonObject = null;
            try {
                jsonObject = parseError(getResponseData());
                if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                log.info("解析失败，失败原因：" + e.getMessage());
                break;
            }

            switch (getRequestType()) {
                case ERT_Promotion_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_Promotion_RetrieveN:
                    handleRetrieveN(rsp, jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    break;
                case ERT_Promotion_RetrieveNC:
                    handleRetrieveNC(rsp, jsonObject);
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

//    private void handleUpdate(JSONObject jsonObject) {
//        try {
//            JSONObject joPromotion = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//            Promotion Promotion = new Promotion();
//            if (Promotion.parse1(joPromotion.toString()) == null) {
//                log.info("UPDATE:Failed to parse Promotion!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                log.info("ERT_Promotion_Update得到的对象是：" + Promotion.toString());
//
//                setBaseModel1(Promotion);
//
//                if (sqliteBO != null) {
//                    sqliteBO.onResultUpdate(Promotion);
//                }
//            }
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
//    }

    private void handleRetrieveN(String rsp, JSONObject jsonObject) {
        try {
            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());

            JSONArray jaPromotion = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> pList = (List<BaseModel>) new Promotion().parseN(jaPromotion);
            if (pList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回Promotion数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(pList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Promotion_RetrieveN得到的对象列表是: " + pList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < pList.size(); i++) {
                    if (pList.get(i).getSyncSequence() > maxSyncSequence) { //...
                        maxSyncSequence = pList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的list
                List<BaseModel> orderPromotionList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < pList.size(); j++) {
                        if (i == pList.get(j).getSyncSequence()) {
                            orderPromotionList.add(pList.get(j));
                        }
                    }
                }   //... 参考ComparatorBaseModel，实现更加灵活的比较器
                setListMasterTable(orderPromotionList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderPromotionList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步promotion失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(String rsp, JSONObject jsonObject) {
        try {
            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());

            JSONArray jaPromotion = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> rtList = (List<BaseModel>) new Promotion().parseN(jaPromotion);
            if (rtList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回Promotion数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(rtList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(null);
                }
            } else {
                log.info("ERT_Promotion_RetrieveNC得到的对象列表是: " + rtList.toString());

                setListMasterTable(rtList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                // 如果BaseModel2为空，则调用全部同步方法
                if (getBaseModel2() == null) {
                    if (sqliteBO != null) {
                        sqliteBO.onResultRetrieveNC(rtList);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的promotion失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joPromotion = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Promotion b = new Promotion();
            if (b.parse1(joPromotion.toString()) == null) {
                log.info("CREATE:Failed to parse Promotion!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Promotion_Create得到的对象是：" + b.toString());

                setBaseModel1(b);
            }
            if (sqliteBO != null) {
                sqliteBO.onResultCreate(b);
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步promotion失败，失败原因：" + e.getMessage());
        }
    }
}
