package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.UT.SmallSheetJUnit;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.SmallSheetFrame;
import wpos.model.SmallSheetText;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.List;

public class SmallSheetSIT extends BaseHttpTestCase {
//    private SmallSheetFramePresenter smallSheetFramePresenter = null;
//    private SmallSheetTextPresenter smallSheetTextPresenter = null;
    @Resource
    private SmallSheetHttpBO smallSheetHttpBO;
    @Resource
    private SmallSheetSQLiteBO smallSheetSQLiteBO;
    @Resource
    private SmallSheetHttpEvent smallSheetHttpEvent;
    @Resource
    private SmallSheetSQLiteEvent smallSheetSQLiteEvent;
//    @Resource
//    private LoginHttpBO loginBO;
    private static SmallSheetFrame createSmallSheet = null;
    private static List<SmallSheetFrame> smallSheetList = null;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;
    private String ids = "";
    private static final int EVENT_ID_SmallSheetSIT = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();

//        if (framePresenter == null) {
//            framePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        }
//        if (textPresenter == null) {
//            textPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//        }
        //
        posLoginHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        smallSheetSQLiteEvent.setId(EVENT_ID_SmallSheetSIT);
        smallSheetHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        smallSheetSQLiteBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetSQLiteBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetHttpBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetHttpBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_SmallSheetSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        SmallSheetFrame ssfOld = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        ssfOld.setLogo(logo);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue(false,"创建失败！");
        }

        //等待处理完毕
        long lTimeout = 50;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        System.out.println(smallSheetSQLiteBO.getSqLiteEvent().getStatus());
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"测试失败！原因：超时。当前sqlList的状态：" + smallSheetSQLiteBO.getSqLiteEvent().getStatus() + "，错误码：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorCode() + ",错误信息：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage());
        }

        createSmallSheet = (SmallSheetFrame) smallSheetHttpBO.getHttpEvent().getBaseModel1();//创建后服务器返回的SmallSheet
        Assert.assertTrue(createSmallSheet != null,"");

        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue( ssfOld.compareTo(createSmallSheet) == 0,"服务器返回的对象和上传的对象不相等！");
    }

    private void retrieveNSmallSheet(boolean ifDo) throws InterruptedException {
        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue( false,"retrieveNAsync失败");
        }
        long lTimeOut = 50;
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"RetrieveN超时！" + smallSheetSQLiteEvent.printErrorInfo());
        }

        smallSheetList = (List<SmallSheetFrame>) smallSheetHttpEvent.getListMasterTable();
        if (ifDo) {
            Assert.assertTrue( smallSheetList != null,"RetrieveN得到的数据不应该为null!");
            Assert.assertTrue( smallSheetList.get(smallSheetList.size() - 1).compareTo(createSmallSheet) == 0,"创建的SmallSheet与查询到的SmallSheet不一致！");
        } else {
            Assert.assertTrue( smallSheetList.size() == 0,"feedback之后，RetrieveN不应该有数据返回！");
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
            Assert.assertTrue( false,"feedback超时！");
        }
        Assert.assertTrue(smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"feedback失败！");
    }


    /*
    全部同步
     */
    @Test
    public void testEverything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //本地创建一条小票
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        SmallSheetFrame ssf = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        List<SmallSheetText> sstList = (List<SmallSheetText>) ssf.getListSlave1();
        ssf = (SmallSheetFrame) smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        ssf.setListSlave1(sstList);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        SmallSheetFrame ssf1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setSql("where F_FrameID = %s");
        smallSheetText.setConditions(new String[]{String.valueOf(ssf1Retrieve1.getID())});
        smallSheetText.setFrameId(Long.valueOf(ssf1Retrieve1.getID()));
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetText);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        ssf1Retrieve1.setListSlave1(textList);
        //
        ssf.setIgnoreSyncTypeInComparision(true);
        Assert.assertTrue( ssf.compareTo(ssf1Retrieve1) == 0,"CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //POS去请求全部同步的Action并更新本地服务器
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        smallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
            Assert.assertTrue( false,"调用RetrieveN失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"请求服务器同步SmallSheet超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<SmallSheetFrame> rtList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSyncC测试失败,原因:返回错误码不正确!");
        Assert.assertTrue( rtList != null,"本地是数据不应该为空！");

        List<SmallSheetFrame> list = (List<SmallSheetFrame>) smallSheetHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue( list != null,"服务器返回的数据为空");
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue( false,"全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
    }
}
