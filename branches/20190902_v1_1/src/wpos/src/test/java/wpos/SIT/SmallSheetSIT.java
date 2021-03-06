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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * 1.POS1??????
     * 2.??????SmallSheet
     * 3.POS1???????????????POS2??????
     * 4.POS2 RN ??????POS1?????????SmallSheet
     * 5.POS2 feedback
     * 6.POS2 RN ???????????????
     */
    @Test
    public void testSmallSheet() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.POS1??????
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //2.??????SmallSheet
        createSmallSheet("pos1???????????????SmallSheet");

        // ???????????????????????????, ??????????????????
//        //3.POS1???????????????POS2??????
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
//        //POS2 RN ???????????????
//        retrieveNSmallSheet(false);
    }

    private void createSmallSheet(String logo) throws Exception {
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");

        SmallSheetFrame ssfOld = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        ssfOld.setLogo(logo);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue(false,"???????????????");
        }

        //??????????????????
        long lTimeout = 50;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        System.out.println(smallSheetSQLiteBO.getSqLiteEvent().getStatus());
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"???????????????????????????????????????sqlList????????????" + smallSheetSQLiteBO.getSqLiteEvent().getStatus() + "???????????????" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorCode() + ",???????????????" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage());
        }

        createSmallSheet = (SmallSheetFrame) smallSheetHttpBO.getHttpEvent().getBaseModel1();//???????????????????????????SmallSheet
        Assert.assertTrue(createSmallSheet != null,"");

        //?????????????????????????????????ID????????????FrameID??????????????????????????????        //????????????ssfOld????????????master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue( ssfOld.compareTo(createSmallSheet) == 0,"??????????????????????????????????????????????????????");
    }

    private void retrieveNSmallSheet(boolean ifDo) throws InterruptedException {
        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue( false,"retrieveNAsync??????");
        }
        long lTimeOut = 50;
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"RetrieveN?????????" + smallSheetSQLiteEvent.printErrorInfo());
        }

        smallSheetList = (List<SmallSheetFrame>) smallSheetHttpEvent.getListMasterTable();
        if (ifDo) {
            Assert.assertTrue( smallSheetList != null,"RetrieveN???????????????????????????null!");
            Assert.assertTrue( smallSheetList.get(smallSheetList.size() - 1).compareTo(createSmallSheet) == 0,"?????????SmallSheet???????????????SmallSheet????????????");
        } else {
            Assert.assertTrue( smallSheetList.size() == 0,"feedback?????????RetrieveN???????????????????????????");
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
            Assert.assertTrue( false,"feedback?????????");
        }
        Assert.assertTrue(smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"feedback?????????");
    }


    /*
    ????????????
     */
    @Test
    public void testEverything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //????????????????????????
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");

        SmallSheetFrame ssf = SmallSheetJUnit.DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        List<SmallSheetText> sstList = (List<SmallSheetText>) ssf.getListSlave1();
        ssf = (SmallSheetFrame) smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        ssf.setListSlave1(sstList);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"CreateSync bm1????????????,??????:????????????????????????!");
        //
        SmallSheetFrame ssf1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setSql("where F_FrameID = %s");
        smallSheetText.setConditions(new String[]{String.valueOf(ssf1Retrieve1.getID())});
        smallSheetText.setFrameId(Long.valueOf(ssf1Retrieve1.getID()));
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetText);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");

        ssf1Retrieve1.setListSlave1(textList);
        //
        ssf.setIgnoreSyncTypeInComparision(true);
        Assert.assertTrue( ssf.compareTo(ssf1Retrieve1) == 0,"CreateSync????????????,??????:???????????????????????????????????????!");

        //POS????????????????????????Action????????????????????????
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        smallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
            Assert.assertTrue( false,"??????RetrieveN????????????");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"?????????????????????SmallSheet??????!");
        }

        //???????????????????????????sqlite??????????????????,
        List<SmallSheetFrame> rtList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSyncC????????????,??????:????????????????????????!");
        Assert.assertTrue( rtList != null,"?????????????????????????????????");

        List<SmallSheetFrame> list = (List<SmallSheetFrame>) smallSheetHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue( list != null,"??????????????????????????????");
        System.out.println("??????????????????" + rtList + "_______?????????????????????" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue( false,"??????????????????????????????????????????????????????SQLite??????????????????");
        }
    }
}
