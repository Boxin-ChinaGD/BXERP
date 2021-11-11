package com.bx.erp.event;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.MD5Utils;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.view.activity.BaseActivity;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class StaffLoginHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    protected String staffLoginStatus; // staff的登录状态：首次登录、登录失败、登录成功

    public String getStaffLoginStatus() {
        return staffLoginStatus;
    }

    public void setStaffLoginStatus(String staffLoginStatus) {
        this.staffLoginStatus = staffLoginStatus;
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("Staff网络请求错误，" + lastErrorCode);
                GlobalController.getInstance().setSessionID(null);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Redirect) {
                setStaffLoginStatus(Staff.FIRST_LOGIN);
            } else if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                setStaffLoginStatus(Staff.LOGIN_FAILURE);
                break;
            }
            //
            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                switch (getRequestType()) {
                    case ERT_StaffGetToken:
                        httpBO.setPwdEncrypted(getPwdEncrypted());
                        break;
                    case ERT_StaffLogin:
                        log.info("Staff登录成功。会话=" + getTempSessionID());
                        GlobalController.getInstance().setSessionID(getTempSessionID());
                        log.info("staff登录后服务器返回的数据：" + getResponseData());
                        try {
                            handleLogin(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("Staff解析出错：" + e.getMessage());
                            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        break;
                }
            } else if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Redirect) {
                switch (getRequestType()) {
                    case ERT_StaffLogin:
                        log.info("staff首次登录，需要进行密码的修改！服务器返回的数据为：" + getResponseData());
                        try {
                            JSONObject jsonObject1 = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
                            if( BaseActivity.retailTradeAggregation == null){
                                BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
                                log.debug("当前的BaseActivity.retailTradeAggregation为NULL");
                            }
                            BaseActivity.retailTradeAggregation.setStaffID(jsonObject1.getInt(new Staff().field.getFIELD_NAME_ID()));
                            Constants.getCurrentStaff().setName(jsonObject1.getString(new Staff().field.getFIELD_NAME_name()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            log.info("Staff解析出错：" + e.getMessage());
                            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        break;
                }
            } else {
                GlobalController.getInstance().setSessionID(null);//POS已经登录成功，但是只要staff get token失败，整个登录都会失败。下次登录会请求新的会话而不是保持旧的会话
            }
        } while (false);

        //让调用者不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    /**
     * 有网情况下登录成功，初始化收银汇总的部分字段
     */
    private void initRetailTradeAggregation(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject1 = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
        JSONObject jsonObject2 = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY2);

        BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
        BaseActivity.retailTradeAggregation.setStaffID(jsonObject1.getInt(new Staff().field.getFIELD_NAME_ID()));
        BaseActivity.retailTradeAggregation.setWorkTimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        System.out.println("收银员登录成功，该收银员开始工作的时间是" + BaseActivity.retailTradeAggregation.getWorkTimeStart().toString());
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//下班时间的初始值比上班时间多一秒

        Constants.getCurrentStaff().setName(jsonObject1.getString(new Staff().field.getFIELD_NAME_name()));
        Constants.submchid = jsonObject2.getString(new Company().field.getFIELD_NAME_submchid());
    }

    private void handleLogin(JSONObject jsonObject) {
        try {
            StaffPresenter staffPresenter = GlobalController.getInstance().getStaffPresenter();

            Staff staff = Constants.getCurrentStaff();

            Staff staff1 = new Staff();
            staff1.setParesSyncDatetime(false);
            Staff s = (Staff) staff1.parse1(jsonObject.getString(BaseModel.JSON_OBJECT_KEY));

            String md5 = MD5Utils.MD5(staff.getPasswordInPOS() + Constants.SHADOW);
            s.setSalt(md5);
            //...
            if (s.getRoleID() != Constants.preSale_Role_ID) {
                BaseModel bm = staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, s);
                log.info("登录后更新的staff：" + bm);
            }
            if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                Constants.setCurrentStaff(s);
                setStaffLoginStatus(Staff.LOGIN_SUCCESS);
                initRetailTradeAggregation(jsonObject);
            } else {
                setStaffLoginStatus(Staff.LOGIN_FAILURE);
            }
            setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步Staff失败，失败原因：" + e.getMessage());
        }
    }
}
