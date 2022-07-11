package com.bx.erp.event;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StaffHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());
    private StaffPresenter staffPresenter;

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("Staff网络请求错误，" + lastErrorCode);
                GlobalController.getInstance().setSessionID(null);
                break;
            }
            //
            if (getRequestType() == HttpRequestUnit.EnumRequestType.ERT_Staff_Ping){
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_StaffLogin:
                    log.info("Staff登录成功。会话=" + getTempSessionID());
                    GlobalController.getInstance().setSessionID(getTempSessionID());
                    break;
                case ERT_StaffGetToken:
//                    GlobalController.getInstance().getStaffLoginHttpBO().setPwdEncrypted(getString2());
                    break;
                case ERT_Staff_Create:
                    log.info("请求服务器创建Staff");
                    handleCreate(jsonObject);
                    break;
                case ERT_Staff_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_Staff_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                case ERT_Staff_RetrieveResigned:
                    handleRetrieveResigned(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    break;
                case ERT_Staff_Update:
                    log.info("请求修改Staff成功！！");
                    break;
                case ERT_Staff_Delete:
                    log.info("请求删除Staff成功！！");
                    break;
                case ERT_Staff_ResetPassword:
                    //需要Update本地的密码
                    if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                        log.error("在服务器修改密码失败！");
                    }
                    else {
                        log.info("在服务器修改密码成功！");
                    }
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

    private void handleRetrieveResigned(JSONObject jsonObject) {
        try {
            String resignedStaffIDs = jsonObject.getString(Staff.RESIGNED_ID);
            log.info("需要同步的离职员工的ID=" + resignedStaffIDs);
            sqliteBO.getSqLiteEvent().setData(resignedStaffIDs);
            sqliteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Staff_RetrieveResigned);
            if (sqliteBO != null) {
                sqliteBO.onResultRetrieveN(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步已离职员工ID失败：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joRetailTrade = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Staff rt = new Staff();
            if (rt.parse1(joRetailTrade.toString()) == null) {
                log.info("CREATE:Failed to parse RetailTrade!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Staff_Create得到的对象是：" + rt.toString());

                setBaseModel1(rt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步staff失败，错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> rtList = (List<BaseModel>) new Staff().parseN(jaStaff);
            if (rtList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回Staff数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(rtList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(null);
                }
            } else {
                log.info("ERT_Staff_RetrieveNC得到的对象列表是: " + rtList.toString());

                setListMasterTable(rtList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                if (sqliteBO != null) {
                    staffPresenter = GlobalController.getInstance().getStaffPresenter();
                    Staff staff = new Staff();
                    for (int i = 0; i < rtList.size(); i++) {
                        Staff s = (Staff) rtList.get(i);
                        staff.setID(s.getID());
                        Staff staff2 = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
                        if (staff2 != null) {
                            s.setSalt(staff2.getSalt());    //全部同步的时候不需要把原先保存的密码给删除了。（因为服务器返回staff前是会把密码都清空）
                        }
                    }
                    sqliteBO.onResultRetrieveNC(rtList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步staff失败，错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> staffList = new Staff().parseN(jaStaff);
            if (staffList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回Staff数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(staffList);

                sqliteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Staff_RetrieveNAsync);
                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Staff_RetrieveN得到的对象列表是: " + staffList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < staffList.size(); i++) {
                    if (staffList.get(i).getSyncSequence() > maxSyncSequence) { //...
                        maxSyncSequence = staffList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderStaffList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < staffList.size(); j++) {
                        if (i == staffList.get(j).getSyncSequence()) {
                            orderStaffList.add(staffList.get(j));
                        }
                    }
                }
                setListMasterTable(orderStaffList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderStaffList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步staff失败，错误信息：" + e.getMessage());
        }
    }
}
