package com.bx.erp.event;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class CommodityCategoryHttpEvent extends BaseHttpEvent {
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
                case ERT_CommodityCategory_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_CommodityCategory_Create:
//                    handleCreate(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    break;
                case ERT_CommodityCategory_Update:
                    handleUpdate(jsonObject);
                    break;
                case ERT_CommodityCategory_RetrieveNC:
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
            CommodityCategory commodityCategory = new CommodityCategory();
            if (commodityCategory.parse1(joCategory.toString()) == null) {
                log.info("UPDATE:Failed to parse commodityCategory!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Category_Update得到的对象是：" + commodityCategory.toString());

                setBaseModel1(commodityCategory);

                if (sqliteBO != null) {
                    sqliteBO.onResultUpdate(commodityCategory);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析commodityCategory出错，错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> categoryList = new CommodityCategory().parseN(jaStaff);
            if (categoryList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回Category数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(categoryList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Category_RetrieveN得到的对象列表是: " + categoryList.toString());

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderedCategoryList = new ArrayList<BaseModel>();
                for (int i = 0; i < categoryList.size(); i++) {
                    orderedCategoryList.add(categoryList.get(i));
                }
                //将orderedCategoryList的元素按升序排序
                Collections.sort(orderedCategoryList, new Comparator<BaseModel>() {
                    @Override
                    public int compare(BaseModel o1, BaseModel o2) { // 按照字段F_SyncSequence升序排序
                        return o1.getSyncSequence() - o2.getSyncSequence();
                    }
                });
                setListMasterTable(orderedCategoryList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderedCategoryList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步commodityCategory出错，错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaCategory = jsonObject.getJSONArray("categoryList");
            List<BaseModel> categoryList = new CommodityCategory().parseN(jaCategory);

            setListMasterTable(categoryList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_Category_RetrieveNC得到的对象列表是: " + categoryList.toString());

            sqliteBO.onResultRetrieveNC(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析出错，错误信息：" + e.getMessage());
        }
    }

//    private void handleCreate(JSONObject jsonObject) {
//        try {
//            JSONObject joCategory = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//            CommodityCategory c = new CommodityCategory();
//            if (c.parse1(joCategory.toString()) == null) {
//                System.out.println("CREATE:Failed to parse CommodityCategory!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                System.out.println("ERT_Category_Create得到的对象是：" + c.toString());
//
//                setBaseModel1(c);
//            }
//            if (sqliteBO != null) {
//                sqliteBO.onResultCreate(c);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
//    }
}
