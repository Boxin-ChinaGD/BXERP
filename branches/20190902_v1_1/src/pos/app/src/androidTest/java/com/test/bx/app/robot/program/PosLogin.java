package com.test.bx.app.robot.program;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffField;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.PoiUtils;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.RobotConfig;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.protocol.Header;

import junit.framework.Assert;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

/**
 * Created by WPNA on 2019/10/12.
 */

public class PosLogin extends Program {

    protected Integer posID = 2; // 测试多台pos机器人同时登录的时候posID不能相同，否则会登录失败
    protected String companySN = "668866";
    protected String posPasswordInPOS = "000000";
    protected String staffPasswordInPOS = "000000";
    protected String staffPhone = "15854320895";
    //
    private PosLoginHttpBO posLoginHttpBO;
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    private LogoutHttpBO logoutHttpBO;
    //
    private StaffLoginHttpBO staffLoginHttpBO;
    private StaffLoginHttpEvent staffLoginHttpEvent;

    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    public static final int Event_ID_PosLogin = 100000;
    //

    @Override
    protected void generateReport() {

    }

    public PosLogin(Date startDatetime, Date endDatetime, final RobotConfig rc, PosLoginHttpBO posLoginHttpBO, StaffLoginHttpBO staffLoginHttpBO, LogoutHttpBO logoutHttpBO, boolean bRunInRandomMode) {
        super(startDatetime, endDatetime, rc, bRunInRandomMode);
        this.posLoginHttpBO = posLoginHttpBO;
        this.staffLoginHttpBO = staffLoginHttpBO;
        this.logoutHttpBO = logoutHttpBO;

        initBOAndEvent();
    }

    private void initBOAndEvent() {
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(Event_ID_PosLogin);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_PosLogin);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
    }

    public enum EnumPosLogin {
        EPL_PosLogin("EPL_PosLogin", 0),
        EPL_PosLogout("EPL_PosLogout", 1); //


        private String name;
        private int index;

        private EnumPosLogin(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumPosLogin ept : EnumPosLogin.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }
    }

    @Override
    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws InterruptedException, ParseException {
        if (!bRunInRandomMode) { // TODO 可能要修改成队列的形式
            while (!queueIn.isEmpty()) {
                ProgramUnit pu = queueIn.peek();
                if (ShopRobotTest.activitySequence >= pu.getNo()) {
                    // 如果已经到了
                    pu = queueIn.poll();
                    switch (EnumPosLogin.values()[pu.getOperationType()]) {
                        case EPL_PosLogin:
                            if (!doPosLogin(pu.getBaseModelIn1(), sbError)) {
                                sbError.append("登录失败");
                                return false;
                            }
                            break;
                        case EPL_PosLogout:
                            if (doPosLogout(sbError)) {
                                return false;
                            }
                            break;
                        default:
                            break;
                    }
                    queueOut.offer(pu);
//                    if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//                        Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
//                    }
                    // 登录后session不为null，才能查询SQLite数据库
                    new RobotConfig().load();
                    // 通知nbr机器人
                    System.out.println("Pos进行登录：时间：" + ShopRobotTest.sdf.format(new Date()));
                    Header header = new Header();
                    header.setActivitySequence(pu.getNo());
                    header.setCommand(Header.EnumCommandType.ECT_DonePosLogin.getIndex());
                    header.setPosName("1111");
                    StringBuilder sb = new StringBuilder();
                    pu.setDatetimeStart(new Date());
                    // Staff declares multiple JSON fields named passwordInPOS
                    pu.setBaseModelIn1(null);
                    String body = pu.toJson(pu);
                    header.setBodyLength(body.length());
                    sb.append(header.toBufferString());
                    sb.append(body);
                    ShopRobotTest.clientSession.write(sb.toString());
                }
                // 活动还没到
                else {
                    break;
                }
            }
        } else {
            if (canRunNow(currentDatetime)) {
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
                    return false;
                }

                if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    //2.staff登录
                    Staff staff = new Staff();
                    staff.setPhone(staffPhone);
                    staff.setPasswordInPOS(staffPasswordInPOS);
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
                        return false;
                    }
                } else {
                    System.out.println("pos登录失败！");
                    return false;
                }
                return true;
            }
        }
        return true;
    }


    @Override
    protected boolean canRunNow(Date d) {
        // 一定可以运行
        return true;
    }

    @Override
    public void resetForNextDay() {

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
            return false;
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

    private boolean doPosLogout(StringBuilder sbError) {
        if (!logoutHttpBO.logoutAsync()) {
            sbError.append("退出登录失败! ");
            return false;
        }
        return true;
    }

    @Override
    protected boolean doLoadProgramUnit() {
        if (!bRunInRandomMode) {
            List<String> listRowTitle = mapBaseModels.get("登录信息").get("ID"); // 标题
            Map<String, List<String>> mapMachineMeal = mapBaseModels.get(Config.KEY_NAME_MyMeal);
            for (List<String> rowMachineMeal : mapMachineMeal.values()) {
                if ("登录信息".equals(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
                    // todo 以下从内存中的数据转换为model时，应该移到新类
                    ProgramUnit pu = new ProgramUnit();
                    List<String> staffInfoList = mapBaseModels.get(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_SheetTableName.getIndex())).get(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_MasterTable.getIndex()));
                    Map<String, Object> params = new HashMap<String, Object>();
                    for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
                        if (staffInfoList.get(columnNO) == null || staffInfoList.get(columnNO).equals("null")) {
                            params.put(listRowTitle.get(columnNO), staffInfoList.get(columnNO));
                        } else {
                            params.put(listRowTitle.get(columnNO), "\"" + staffInfoList.get(columnNO) + "\"");
                        }
                    }
                    //
                    params.put("returnObject", "1");
                    params.put("passwordInPOS", "000000");
                    params.put("syncSequence", null);
                    params.put("isFirstTimeLogin", 0);
                    params.put("syncType", null);
                    params.put("roleID", 3);
                    params.put("syncSequence", 0);
                    Staff staff = new Staff();
                    staff = (Staff) staff.parse1(params.toString());
                    staff.setPasswordInPOS("000000");
                    pu.setBaseModelIn1(staff);
                    pu.setNo(Integer.parseInt(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_ProgramUnitNO.getIndex())));
                    pu.setOperationType(Integer.parseInt(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_OperationType.getIndex())));
                    queueIn.offer(pu);
                }
            }
        }
        return true;
    }

    private boolean feedSyncDataProgram(Program[] programs) {
        ProgramUnit pu = new ProgramUnit();
        pu.setDatetimeStart(DatetimeUtil.addMinutes(getRobotStartDatetime(), 0)); // 这个时间同步
        pu.setOperationType(SyncData.EnumSyncDataType.SDT_SmallSheet.getIndex());
        feed(pu, programs[EnumProgramType.EPT_SyncData.getIndex()]);
        ProgramUnit pu2 = new ProgramUnit();
        pu2.setDatetimeStart(DatetimeUtil.addMinutes(getRobotStartDatetime(), 0)); //
        pu2.setOperationType(SyncData.EnumSyncDataType.SDT_RetailTradeAggregation.getIndex());
        feed(pu2, programs[EnumProgramType.EPT_SyncData.getIndex()]);
        ProgramUnit pu3 = new ProgramUnit();
        pu3.setDatetimeStart(DatetimeUtil.addMinutes(getRobotStartDatetime(), 0)); //
        pu3.setOperationType(SyncData.EnumSyncDataType.SDT_RetailTrade.getIndex());
        feed(pu3, programs[EnumProgramType.EPT_SyncData.getIndex()]);
        ProgramUnit pu4 = new ProgramUnit();
        pu4.setDatetimeStart(DatetimeUtil.addMinutes(getRobotStartDatetime(), 0)); //
        pu4.setOperationType(SyncData.EnumSyncDataType.SDT_Commodity.getIndex());
        feed(pu4, programs[EnumProgramType.EPT_SyncData.getIndex()]);
        return true;
    }

}
