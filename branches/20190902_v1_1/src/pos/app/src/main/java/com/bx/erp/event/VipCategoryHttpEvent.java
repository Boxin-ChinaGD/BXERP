package com.bx.erp.event;

import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.VipCategory;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VipCategoryHttpEvent extends BaseHttpEvent {
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
                case ERT_VipCategory_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_VipCategory_Update:
                    handleUpdate(jsonObject);
                    break;
                case ERT_VipCategory_RetrieveNC:
                    handleRetrieveNC(jsonObject);
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

    private void handleUpdate(JSONObject jsonObject) {
        try {
            JSONObject joCategory = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            VipCategory vipCategory = new VipCategory();
            if (vipCategory.parse1(joCategory.toString()) == null) {
                log.info("UPDATE:Failed to parse VipCategory!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_VipCategory_Update得到的对象是：" + vipCategory.toString());

                setBaseModel1(vipCategory);

                if (sqliteBO != null) {
                    sqliteBO.onResultUpdate(vipCategory);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步vipCategory失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> vipCategoryList = new VipCategory().parseN(jaStaff);
            if (vipCategoryList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回VipCategory数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(vipCategoryList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_VipCategory_RetrieveN得到的对象列表是: " + vipCategoryList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < vipCategoryList.size(); i++) {
                    if (vipCategoryList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = vipCategoryList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据syncSequence升序排列的List
                List<BaseModel> orderVipCategoryList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < vipCategoryList.size(); j++) {
                        if (i == vipCategoryList.get(j).getSyncSequence()) {
                            orderVipCategoryList.add(vipCategoryList.get(j));
                        }
                    }
                }
                setListMasterTable(orderVipCategoryList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderVipCategoryList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步vipCategory失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaCategory = jsonObject.getJSONArray("vipCategoryList");
            List<BaseModel> vipCategoryList = new VipCategory().parseN(jaCategory);

            setListMasterTable(vipCategoryList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_VipCategory_RetrieveNC得到的对象列表是: " + vipCategoryList.toString());

            sqliteBO.onResultRetrieveNC(vipCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步vipCategory失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joCategory = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            VipCategory vc = new VipCategory();
            if (vc.parse1(joCategory.toString()) == null) {
                log.info("CREATE:Failed to parse VipCategory!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_VipCategory_Create得到的对象是：" + vc.toString());

                setBaseModel1(vc);
            }
//            if (sqliteBO != null) {
//                sqliteBO.onResultCreate(vc);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步vipCategory失败，失败原因：" + e.getMessage());
        }
    }
}
