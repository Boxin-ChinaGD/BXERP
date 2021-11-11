package com.bx.erp.event;

import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Vip;
import com.bx.erp.model.wx.WxVip;
import com.bx.erp.model.wx.WxVipCardDetail;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VipHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public VipHttpEvent() {
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
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_Vip_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_Vip_Update:
                    handleUpdate(jsonObject);
                    break;
                case ERT_Vip_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_Vip_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                case ERT_Vip_RetrieveNVipConsumeHistory:
                    handleRetrieveNVipConsumeHistory(jsonObject);
                    break;
                case ERT_Vip_RetrieveNByMobileOrVipCardSN:
                    handleRetrieveNByMobileOrVipCardSN(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    setRequestType(HttpRequestUnit.EnumRequestType.ERT_FeedBack);
                    break;
                case ERT_Vip_Delete:
                    setSyncType(BasePresenter.SYNC_Type_D);
//                    if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
//                        if (sqliteBO != null) {
//                            sqliteBO.onResultDelete(getBaseModel1());
//                        }
//                    } else {
//                        sqliteBO.onResultDelete(null);
//                    }
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
            JSONArray jaVip = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> vipList = new Vip().parseN(jaVip);
            if (vipList.size() == 0) {
                log.info("retrieveN：服务器没有需要同步的vip数据返回!");
            } else {
                log.info("retrieveN得到的对象列表是：" + vipList.toString());
            }
            setListMasterTable(vipList);
            if (sqliteBO != null) {
                sqliteBO.onResultUpdateN(vipList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步VIP失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveNVipConsumeHistory(JSONObject jsonObject) {
        try {
            JSONArray jaVip = jsonObject.getJSONArray("retailTradeList");
            List<?> retailTradeList = new RetailTrade().parseN(jaVip);
            if (retailTradeList.size() == 0) {
                log.info("retrieveN：该会员没有消费记录!");
            } else {
                log.info("retrieveN得到的对象列表是、、：" + retailTradeList.toString());
                setListMasterTable(retailTradeList);

                //目前未做VIP，所以先不理
//                GlobalController.getInstance().getRetailTradePresenter().createOrReplaceNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, GlobalController.getInstance().getRetailTradeSQLiteEvent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步VIP失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joVip = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Vip vip = new Vip();
            setSyncType(BasePresenter.SYNC_Type_C);
            if (vip.parse1(joVip.toString()) == null) {
                log.info("Create: Failed to parse vip!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Vip_Create得到的对象是：" + vip.toString());
                setBaseModel1(vip);

                if (sqliteBO != null) {
                    sqliteBO.onResultCreate(vip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步VIP失败，失败原因：" + e.getMessage());
        }
    }

    private void handleUpdate(JSONObject jsonObject) {
        try {
            JSONObject joVip = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Vip vip = new Vip();
            if (vip.parse1(joVip.toString()) == null) {
                log.info("Create: Failed to parse vip!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Vip_Update得到的对象是：" + vip.toString());
                setBaseModel1(vip);
                //pos机不允许修改vip，模拟网页修改即可
//                if (sqliteBO != null) {
//                    sqliteBO.onResultUpdate(vip);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步VIP失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaVip = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> vipList = new Vip().parseN(jaVip);
            if (vipList.size() == 0) {
                log.info("retrieveN：服务器没有需要同步的vip数据返回!");

                setListMasterTable(vipList);
                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("retrieveN得到的对象列表是：" + vipList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < vipList.size(); i++) {
                    if (vipList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = vipList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderVipList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < vipList.size(); j++) {
                        if (i == vipList.get(j).getSyncSequence()) {
                            orderVipList.add(vipList.get(j));
                        }
                    }
                }
                setListMasterTable(orderVipList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderVipList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步VIP失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveNByMobileOrVipCardSN(JSONObject jsonObject) {
        try {
            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                JSONArray jaVip = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
                if (jaVip.length() > 0) {
                    Vip vip = (Vip) new Vip().parse1(jaVip.getJSONObject(0).toString());
                    setBaseModel1(vip);
                } else {
                    setBaseModel1(null);
                }
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("查询会员相关信息失败，失败原因：" + e.getMessage());
        }
    }
}
