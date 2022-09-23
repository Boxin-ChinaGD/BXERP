package com.test.bx.app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LoginHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class SmallSheetSIT extends BaseHttpAndroidTestCase {
    private static SmallSheetFramePresenter framePresenter = null;
    private static SmallSheetTextPresenter textPresenter = null;
    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private static SmallSheetHttpEvent smallSheetHttpEvent = null;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    private static LoginHttpBO loginBO = null;
    private SmallSheetFrame createSmallSheet = null;
    private List<SmallSheetFrame> smallSheetList = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private String ids = "";
    private static final int EVENT_ID_SmallSheetSIT = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (framePresenter == null) {
            framePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        }
        if (textPresenter == null) {
            textPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(EVENT_ID_SmallSheetSIT);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 1.POS1登录
     * 2.创建SmallSheet
     * 3.POS1退出登录，POS2登录
     * 4.POS2 RN 返回POS1创建的SmallSheet
     * 5.POS2 feedback
     * 6.POS2 RN 不返回数据
     */
    @Test
    public void testSmallSheet() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.POS1登录
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //2.创建SmallSheet
        createSmallSheet("pos1第一次创建SmallSheet");

        // 小票格式不用同步了, 直接下载全部
//        //3.POS1退出登录，POS2登录
//        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
//
//        //4.POS2 RN
//        retrieveNSmallSheet(true);
//
//        //5.POS2 feedback
//        for (SmallSheetFrame smallSheet : smallSheetList) {
//            ids = ids + "," + smallSheet.getID();
//        }
//        ids = ids.substring(1, ids.length());
//        feedback(ids);
//
//        //POS2 RN 不返回数据
//        retrieveNSmallSheet(false);
    }

    private void createSmallSheet(String logo) throws Exception {
        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame ssfOld = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        ssfOld.setLogo(logo);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("创建失败！", false);
        }

        //等待处理完毕
        long lTimeout = 50;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        System.out.println(smallSheetSQLiteBO.getSqLiteEvent().getStatus());
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("测试失败！原因：超时。当前sqlList的状态：" + smallSheetSQLiteBO.getSqLiteEvent().getStatus() + "，错误码：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorCode() + ",错误信息：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), false);
        }

        createSmallSheet = (SmallSheetFrame) smallSheetHttpBO.getHttpEvent().getBaseModel1();//创建后服务器返回的SmallSheet
        Assert.assertTrue("", createSmallSheet != null);

        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue("服务器返回的对象和上传的对象不相等！", ssfOld.compareTo(createSmallSheet) == 0);
    }

    private void retrieveNSmallSheet(boolean ifDo) throws InterruptedException {
        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("retrieveNAsync失败", false);
        }
        long lTimeOut = 50;
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("RetrieveN超时！" + smallSheetSQLiteEvent.printErrorInfo(), false);
        }

        smallSheetList = (List<SmallSheetFrame>) smallSheetHttpEvent.getListMasterTable();
        if (ifDo) {
            Assert.assertTrue("RetrieveN得到的数据不应该为null!", smallSheetList != null);
            Assert.assertTrue("创建的SmallSheet与查询到的SmallSheet不一致！", smallSheetList.get(smallSheetList.size() - 1).compareTo(createSmallSheet) == 0);
        } else {
            Assert.assertTrue("feedback之后，RetrieveN不应该有数据返回！", smallSheetList.size() == 0);
        }
    }

    private void feedback(String ids) throws InterruptedException {
        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        smallSheetHttpBO.feedback(ids);
        long lTimeOut = 50;
        while (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("feedback超时！", false);
        }
        Assert.assertTrue("feedback失败！", smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }


    /*
    全部同步
     */
    @Test
    public void testEverything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //本地创建一条小票
        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame ssf = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        ssf = (SmallSheetFrame) framePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame ssf1Retrieve1 = (SmallSheetFrame) framePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setSql("where F_FrameID = ?");
        smallSheetText.setConditions(new String[]{String.valueOf(ssf1Retrieve1.getID())});
        smallSheetText.setFrameId(ssf1Retrieve1.getID());
        List<SmallSheetText> textList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetText);
        Assert.assertTrue("错误码不正确！", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        ssf1Retrieve1.setListSlave1(textList);
        //
        ssf.setIgnoreSyncTypeInComparision(true);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", ssf.compareTo(ssf1Retrieve1) == 0);

        //POS去请求全部同步的Action并更新本地服务器
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        smallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步SmallSheet超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<SmallSheetFrame> rtList = (List<SmallSheetFrame>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSyncC测试失败,原因:返回错误码不正确!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("本地是数据不应该为空！", rtList != null);

        List<SmallSheetFrame> list = (List<SmallSheetFrame>) smallSheetHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("服务器返回的数据为空", list != null);
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }
}
