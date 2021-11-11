package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.utils.StringUtils;

import java.util.List;
@Component("retailTradeHttpEvent")
@Scope("prototype")
public class RetailTradeHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                sqliteBO.getSqLiteEvent().setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }

            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //即使错误码是EC_Duplicated，那也应该调用下面的handlexxx()方法
//                if (getLastErrorCode().toString() == ErrorInfo.EnumErrorCode.EC_Duplicated.toString()) {
//                    //解析服务器返回的对象
//                    JSONObject joRetailTrade = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//                    RetailTrade rt = new RetailTrade();
//                    if (rt.parse1(joRetailTrade.toString()) == null) {
//                        log.info("CREATE:Failed to parse RetailTrade!");
//                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//                    } else {
//                        log.info("ERT_RetailTrade_Create得到的对象是：" + rt.toString());
//                        setBaseModel1(rt);
//                        if (sqliteBO != null) {
//                            sqliteBO.onResultCreate(rt);
//                        }
//                    }
//                }
            //这个IF是否也应该去除，批量上传零售单的时候。
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError && lastErrorCode != ErrorInfo.EnumErrorCode.EC_Duplicated && lastErrorCode != ErrorInfo.EnumErrorCode.EC_PartSuccess) {
                break;
            }

            switch (getRequestType()) {
                case ERT_RetailTrade_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_RetailTrade_CreateN:
                    handleCreateN(jsonObject);
                    break;
                case ERT_RetailTrade_RetrieveN:
//                    handleRetrieveN(getResponseData(), jsonObject);
                    break;
//                case ERT_RetailTrade_Delete:
//                    if (sqliteBO != null) {
//                        sqliteBO.onResultDelete(null);
//                    }
//                    break;
//                case ERT_FeedBack:
//                    if (sqliteBO != null) {
//                        sqliteBO.onResultFeedback();
//                    }
//                    setRequestType(HttpRequestUnit.EnumRequestType.ERT_FeedBack);
//                    break;
                case ERT_RetailTrade_RetrieveNC:
                    handleRetrieveNC(getResponseData(), jsonObject);
                    break;
                case ERT_RetailTrade_RetrieveNOldRetailTrade:
                    handleRetrieveNOldRetailTrade(jsonObject);
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

    /**
     * 处理查询旧零售单返回的网络数据
     */
    private void handleRetrieveNOldRetailTrade(JSONObject jsonObject) {
        try {
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            setCount(count);
            JSONArray jaRetailTrade = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> rtList = (List<BaseModel>) new RetailTrade().parseN(jaRetailTrade);
            if (rtList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回RetailTrade数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(rtList);
            } else {
                log.info("ERT_RetailTrade_RetrieveNOldRetailTrade得到的对象列表是: " + rtList.toString());

                setListMasterTable(rtList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                if (sqliteBO != null) {
                    sqliteBO.onResultUpdateN(rtList);
                }
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步零售单失败，失败原因：" + e.getMessage());
        }
    }

//    private void handleRetrieveN(String rsp, JSONObject jsonObject) {
//        try {
//            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());
//
//            JSONArray jaRetailTrade = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
//            List<BaseModel> rtList = (List<BaseModel>) new RetailTrade().parseN(jaRetailTrade);
//            if (rtList.size() == 0) {
//                log.info("RETRIEVE N:服务器没有返回RetailTrade数据！");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//
//                setListMasterTable(rtList);
//
//                if (sqliteBO != null) {
//                    sqliteBO.onResultRetrieveN(null);
//                }
//            } else {
//                log.info("ERT_RetailTrade_RetrieveN得到的对象列表是: " + rtList.toString());
//
//                int maxInt2 = 0;
//                for (int i = 0; i < rtList.size(); i++) {
//                    if (rtList.get(i).getInt2() > maxInt2) {
//                        maxInt2 = rtList.get(i).getInt2();
//                    }
//                }
//
//                //生成一个根据int2升序排列的list
//                List<BaseModel> orderRetailTradeList = new ArrayList<BaseModel>();
//                for (int i = 0; i < maxInt2; i++) {
//                    for (int j = 0; j < rtList.size(); j++) {
//                        if (i + 1 == rtList.get(j).getInt2()) {
//                            orderRetailTradeList.add(rtList.get(j));
//                        }
//                    }
//                }   //... 参考ComparatorBaseModel，实现更加灵活的比较器
//                setListMasterTable(orderRetailTradeList);
//
//                if (sqliteBO != null) {
//                    sqliteBO.onResultRetrieveN(orderRetailTradeList);
//                }
//            }
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            log.info("同步零售单失败，失败原因：" + e.getMessage());
//        }
//    }

    private void handleRetrieveNC(String rsp, JSONObject jsonObject) {
        try {
            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());

            JSONArray jaRetailTrade = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> rtList = (List<BaseModel>) new RetailTrade().parseN(jaRetailTrade);
            if (rtList.size() == 0) { //可能rtList=null，但是服务器的数据不会错，所以这里正常情况下不可能为null
                log.info("RETRIEVE N:服务器没有返回RetailTrade数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(rtList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(null);
                }
            } else {
                log.info("ERT_RetailTrade_RetrieveNC得到的对象列表是: " + rtList.toString());

                setListMasterTable(rtList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                // 如果BaseModel2为空，则调用全部同步方法
                if (getBaseModel2() == null) {
                    if (sqliteBO != null) {
                        sqliteBO.onResultRetrieveNC(rtList);
                    }
                }
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步零售单失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joRetailTrade = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            RetailTrade rt = new RetailTrade();
            if (rt.parse1(joRetailTrade.toString()) == null) {
                log.info("CREATE:Failed to parse RetailTrade!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_RetailTrade_Create得到的对象是：" + rt.toString());

                setBaseModel1(rt);

                if (sqliteBO != null) {
                    sqliteBO.onResultCreate(rt);
                }
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步零售单失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreateN(JSONObject jsonObject) {
        try {
            //
            JSONArray joRetailTrades = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            if (joRetailTrades != null) {
                RetailTrade rt = new RetailTrade();
                List<BaseModel> retailTradeList = (List<BaseModel>) rt.parseN(joRetailTrades);
                if (retailTradeList == null) {
                    log.info("CREATEN:Failed to parse RetailTrade!");
                    setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                } else {
                    log.info("ERT_RetailTrade_CreateN得到的对象是:" + retailTradeList.toString());

                    setListMasterTable(retailTradeList);

                    if (sqliteBO != null) {
                        sqliteBO.onResultCreateN(retailTradeList);
                    }
                }
            }
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步零售单失败，失败原因：" + e.getMessage());
        }
    }
}
