package com.bx.erp.event;

import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommodityHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public CommodityHttpEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession || getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                break;
            }

            //
            switch (getRequestType()) {
                case ERT_Commodity_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_Commodity_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    setRequestType(HttpRequestUnit.EnumRequestType.ERT_FeedBack);
                    break;
                case ERT_Commodity_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                case ERT_Commodity_RetrieveInventoryC:
                    handleRetrieveInventoryC(jsonObject);
                    break;
                case ERT_Commodity_Retrieve1C:
                    handleRetrieve1C(jsonObject);
                    break;
                default:
                    log.info("Not yet imlemented");
                    throw new RuntimeException("Not yet implemented");
            }
        } while (false);

        //让调用着不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieve1C(JSONObject jsonObject) {
        try {
            String json = jsonObject.getString(BaseModel.JSON_OBJECT_KEY);
            BaseModel commodity = new Commodity().parse1C(json);

            log.info("服务器返回的商品是：" + commodity);
            setBaseModel1(commodity);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析服务器返回的信息失败,错误消息：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaCommodity = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> commodityList = (List<BaseModel>) new Commodity().parseN(jaCommodity);
            if (commodityList.size() == 0) {
                log.info("retrieveN:服务器没有需要同步的commodity数据返回！");

                setListMasterTable(commodityList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("retrieveN得到的对象列表是：" + commodityList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < commodityList.size(); i++) {
                    System.out.println(commodityList.get(i).getSyncSequence());
                    if (commodityList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = commodityList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderCommodityList = new ArrayList<BaseModel>();
                for (int i = 0; i < maxSyncSequence; i++) {
                    for (int j = 0; j < commodityList.size(); j++) {
                        if (i + 1 == commodityList.get(j).getSyncSequence()) {
                            orderCommodityList.add(commodityList.get(j));
                        }
                    }
                } //...参考ComparatorBaseModel,实现更加灵活的比较器
                setListMasterTable(orderCommodityList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderCommodityList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步commodityList失败,错误消息：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaCommodity = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            List<BaseModel> commodityList = new Commodity().parseNC(jaCommodity);
            if (commodityList.size() == 0) {
                log.info("retrieveNC:服务器中没有数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(commodityList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(null);
                }
            } else {
                log.info("retrieveNC得到的对象列表是：" + commodityList.toString());

                setListMasterTable(commodityList);
                setCount(count);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(commodityList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的commodity失败,错误消息：" + e.getMessage());
        }
    }

    private void handleRetrieveInventoryC(JSONObject jsonObject) {
        try {
            JSONArray commodityArray = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> commodityList = new ArrayList<BaseModel>();
            if (commodityArray.length() <= 0) {
                log.info("输入的条形码没有对应的商品！");

                setListMasterTable(commodityList);
                ((CommoditySQLiteBO) sqliteBO).onResultRetrieveInventoryC(null); // TODO 这里破坏了代码体系
            } else {
                for (int i = 0; i < commodityArray.length(); i++) {
                    JSONObject commObject = commodityArray.getJSONObject(i);
                    Commodity commodity = new Commodity();
                    commodity.setID(commObject.getLong(commodity.field.getFIELD_NAME_ID()));
                    commodity.setNO(commObject.getInt(commodity.field.getFIELD_NAME_NO()));
                    commodity.setBarcode(commObject.getString(commodity.field.getFIELD_NAME_barcodes()));//...
                    commodityList.add(commodity);
                }

                setListMasterTable(commodityList);
                if (!(((CommoditySQLiteBO) sqliteBO).onResultRetrieveInventoryC(commodityList))) { // TODO 这里破坏了代码体系
                    setLastErrorCode(sqliteBO.getSqLiteEvent().getLastErrorCode());
                    setLastErrorMessage(sqliteBO.getSqLiteEvent().getLastErrorMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析服务器返回的信息失败,错误消息：" + e.getMessage());
        }
    }


    private void handleCreate(JSONObject jsonObject) {
        try {
            String json = jsonObject.getString(BaseModel.JSON_OBJECT_KEY);
            BaseModel commodity = new Commodity().parse1C(json);

            log.info("服务器返回的商品是：" + commodity);
            setBaseModel1(commodity);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析服务器返回的信息失败,错误消息：" + e.getMessage());
        }
    }
}
