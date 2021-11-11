package com.test.bx.app;

import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarcodesJUnit extends BaseHttpAndroidTestCase {
    private static BarcodesPresenter presenter = null;
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static List<Barcodes> List;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesJUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getBarcodesPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
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
        if (sqLiteEvent == null) {
            sqLiteEvent = new BarcodesSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (httpEvent == null) {
            httpEvent = new BarcodesHttpEvent();
            httpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Barcodes BarcodesInput = null;

        protected static final Barcodes getBarcodesInput() throws CloneNotSupportedException, ParseException {
            BarcodesInput = new Barcodes();
            BarcodesInput.setBarcode("1111" + System.currentTimeMillis() % 1000000);
            BarcodesInput.setCommodityID(1);
            BarcodesInput.setSyncType("C");
            BarcodesInput.setInt1(1);//...
            BarcodesInput.setOperatorStaffID(1);// 员工ID
            BarcodesInput.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            return (Barcodes) BarcodesInput.clone();
        }

        protected static final List<BaseModel> getBarcodesList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> BarcodesList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                BarcodesList.add(getBarcodesInput());
            }
            return BarcodesList;
        }
    }

    @Test
    public void test_a_RetrieveNEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //1,2步，登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        createBarcodes();

        //POS2,STAFF2登陆
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        Assert.assertTrue("不存在需要同步的数据", retrieveNBarcodes().size() > 0);
    }

    private void createBarcodes() throws Exception {
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, Barcodes)) {
            Assert.assertTrue("创建失败!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Barcodes超时!", false);
        }
    }

    private List<Barcodes> retrieveNBarcodes() throws InterruptedException {
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("同步失败", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Barcodes同步超时!", false);
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        return barcodesList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        createBarcodes();

        //POS2,STAFF2登陆
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        List = retrieveNBarcodes();
        Assert.assertTrue("不存在需要同步的数据", List.size() > 0);

        String BarcodesIDs = "";
        for (int i = 0; i < List.size(); i++) {
            BarcodesIDs = BarcodesIDs + "," + List.get(i).getID();
        }
        BarcodesIDs = BarcodesIDs.substring(1, BarcodesIDs.length());

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!httpBO.feedback(BarcodesIDs)) {
            Assert.assertTrue("同步失败", false);
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Barcodes Feedback超时!", false);
        }
        //调用RN，返回无数据
        System.out.println("第二次调用RN方法");
        List<Barcodes> xbarcodesList = retrieveNBarcodes();
        Assert.assertTrue("feedback失败", xbarcodesList == null);
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createAsync返回的错误码不正确!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入Barcodes失败!", c.compareTo(Barcodes) == 0);
        }

        //异常Case:主键冲突
        sqLiteEvent.setEventProcessed(false);
        //将Barcodes插入到本地SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 查询所有
        System.out.println("-------------------------------------RN2");
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        sqLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("retrieveN失败!原因:超时", false);
        }
        Assert.assertTrue("retrieveN的错误码不正确!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 应该要有数据返回", sqLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes的List
        List<Barcodes> BarcodesList = new ArrayList<Barcodes>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
            BarcodesList.add(Barcodes);
        }
        //将Barcodes的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList);
        Long conflictID = BarcodesList.get(1).getID();
        //
        Assert.assertTrue("createNSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的BarcodesList没有完全插入成功", Barcodes.compareTo(BarcodesList.get(i)) == 0);
        }

        //异常Case:主键冲突
        List<Barcodes> BarcodesList2 = new ArrayList<Barcodes>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
            Barcodes.setID(conflictID);
            BarcodesList2.add(Barcodes);
        }
        //将Barcodes的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList2);
        //
        Assert.assertTrue("createNSync主键冲突, 返回的错误码应该为OtherError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Barcodes失败!", c.compareTo(Barcodes) == 0);

        //异常Case:主键冲突
        //将已有的Barcodes插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Barcodes失败!", c.compareTo(Barcodes) == 0);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //创建一个Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Barcodes失败!", c.compareTo(Barcodes) == 0);

        //删除一个Barcodes
        BaseModel bm1 = BarcodesJUnit.DataInput.getBarcodesInput();
        bm1.setID(c.getID());
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = BarcodesJUnit.DataInput.getBarcodesInput();
        bm2.setID(c.getID());
        BaseModel bm3 = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("该对象未被删除", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = BarcodesJUnit.DataInput.getBarcodesList();
        if (!presenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败!", false);
        }

        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("CreateNAsync返回错误码不正确", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            List<Barcodes> BarcodesList = (List<Barcodes>) sqLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                Barcodes c = (Barcodes) list.get(i);
                Assert.assertTrue("CreateNAsync的数据与原数据不符", c.compareTo(BarcodesList.get(i)) == 0);
            }
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //本地SQLite增加一条数据。全部同步后不存在
        Barcodes c = BarcodesJUnit.DataInput.getBarcodesInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");

        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);

        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.assertTrue("同步失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步Barcodes超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Barcodes> barcodesList = (List<Barcodes>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Barcodes> list = (List<Barcodes>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != Integer.valueOf(barcodes.getPageSize())) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }
}
