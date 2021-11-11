package com.test.bx.app.robot.program;


import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.view.activity.BaseActivity;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class PosLoginEx extends ProgramEx {

    protected Integer posID = 2; // 测试多台pos机器人同时登录的时候posID不能相同，否则会登录失败
    protected String companySN = "668866";
    protected String posPasswordInPOS = "000000";
    protected String staffPasswordInPOS = "000000";
    protected String staffPhone = "15854320895";

    private PosLoginHttpBO posLoginHttpBO;
    //
    private LogoutHttpBO logoutHttpBO;
    //
    private StaffLoginHttpBO staffLoginHttpBO;

    public boolean run(StringBuilder errorInfo) throws InterruptedException {
        Staff staff = new Staff();
        staff.setPhone(staffPhone);
        staff.setPasswordInPOS(posPasswordInPOS);
        StringBuilder stringBuilder = new StringBuilder();
        //
        if(!doPosLogin(staff, stringBuilder)) {
            return false;
        }
        return true;
    }

    private boolean doPosLogin(BaseModel bm, StringBuilder sbError) throws InterruptedException {
        Constants.posID = (int) posID;
        Constants.MyCompanySN = companySN;
        //1.pos登录
        Pos pos = new Pos();
        pos.setID(posID);
        pos.setPasswordInPOS(posPasswordInPOS);
        com.bx.erp.bo.PosLoginHttpBO pbo = posLoginHttpBO;
        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        pbo.setPos(pos);
        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        pbo.loginAsync();
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while (lTimeOut-- > 0) {
            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("pos登录超时!");
            sbError.append("pos登录超时!");
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
            Staff staff = (Staff) bm;
            StaffLoginHttpBO sbo = staffLoginHttpBO;
            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            sbo.setStaff(staff);
            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
            sbo.loginAsync();
            //
            lTimeOut = UNIT_TEST_TimeOut;
            while (lTimeOut-- > 0) {
                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                System.out.println("STAFF登录超时!");
                sbError.append("STAFF登录超时!");
                return false;
            }
        } else {
            System.out.println("pos登录失败！");
            sbError.append("pos登录失败！");
            return false;
        }
        return true;
    }

    public PosLoginEx(PosLoginHttpBO posLoginHttpBO, StaffLoginHttpBO staffLoginHttpBO, LogoutHttpBO logoutHttpBO) {
        super();
        this.posLoginHttpBO = posLoginHttpBO;
        this.staffLoginHttpBO = staffLoginHttpBO;
        this.logoutHttpBO = logoutHttpBO;
    }

    private boolean doStaffLogout(StringBuilder sbError) {
        if (!logoutHttpBO.logoutAsync()) {
            sbError.append("退出登录失败! ");
            return false;
        }
        System.out.println("成功退出登录");
        return true;
    }
}
