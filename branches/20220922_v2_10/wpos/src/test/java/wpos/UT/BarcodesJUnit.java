package wpos.UT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarcodesJUnit extends BaseHttpTestCase {
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static java.util.List<Barcodes> List;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesJUnit = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
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

        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(sqLiteEvent, httpEvent);
            sqLiteBO.setBarcodesPresenter(barcodesPresenter);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(sqLiteEvent, httpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);

        logoutHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        createBarcodes();

        //POS2,STAFF2登陆
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        Assert.assertTrue(retrieveNBarcodes().size() > 0, "不存在需要同步的数据");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    private void createBarcodes() throws Exception {
        Barcodes Barcodes = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, Barcodes)) {
            Assert.fail("创建失败!");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("请求服务器创建Barcodes超时!");
        }
    }

    private List<Barcodes> retrieveNBarcodes() throws InterruptedException {
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.fail("同步失败");
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("Barcodes同步超时!");
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        return barcodesList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        createBarcodes();
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
        //POS2,STAFF2登陆
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        List = retrieveNBarcodes();
        Assert.assertTrue(List.size() > 0, "不存在需要同步的数据");

        String BarcodesIDs = "";
        for (Barcodes barcodes : List) {
            BarcodesIDs = BarcodesIDs + "," + barcodes.getID();
        }
        BarcodesIDs = BarcodesIDs.substring(1);

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!httpBO.feedback(BarcodesIDs)) {
            Assert.fail("同步失败");
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("Barcodes Feedback超时!");
        }
        //调用RN，返回无数据
        System.out.println("第二次调用RN方法");
        List<Barcodes> xbarcodesList = retrieveNBarcodes();
        Assert.assertNull(xbarcodesList, "feedback失败");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("createAsync测试失败!原因:超时");
        } else {
            barcodes = (wpos.model.Barcodes) sqLiteEvent.getBaseModel1();
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createAsync返回的错误码不正确!");
            Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
            Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
            Assert.assertEquals(c.compareTo(barcodes), 0, "插入Barcodes失败!");
        }

        //异常Case:主键冲突
        sqLiteEvent.setEventProcessed(false);
        //将Barcodes插入到本地SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("createAsync测试失败!原因:超时");
        } else {
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createSync非空字段为空, 返回的错误码应该为OtherError!");
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
            Assert.fail("retrieveN失败!原因:超时");
        }
        Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieveN的错误码不正确!");
        Assert.assertTrue(sqLiteEvent.getListMasterTable().size() != 0, "retrieveN 应该要有数据返回");
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes的List
        List<Barcodes> BarcodesList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = DataInput.getBarcodesInput();
            BarcodesList.add(Barcodes);
        }
        //将Barcodes的List插入到本地SQLite
        BarcodesList = (java.util.List<Barcodes>) barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList);
        int conflictID = BarcodesList.get(1).getID();
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createNSync返回的错误码不正确!");
        //
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i));
            Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
            Assert.assertEquals(Barcodes.compareTo(BarcodesList.get(i)), 0, "插入的BarcodesList没有完全插入成功");
        }

        //异常Case:主键冲突
        List<Barcodes> BarcodesList2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = DataInput.getBarcodesInput();
            Barcodes.setID(conflictID);
            BarcodesList2.add(Barcodes);
        }
        //将Barcodes的List插入到本地SQLite
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList2);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createNSync主键冲突, 返回的错误码应该为OtherError");
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "插入Barcodes失败!");

        //异常Case:主键冲突
        //将已有的Barcodes插入到本地SQLite
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createSync非空字段为空, 返回的错误码应该为OtherError!");
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "插入Barcodes失败!");
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //创建一个Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //将Barcodes插入到本地SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "插入Barcodes失败!");

        //删除一个Barcodes
        BaseModel bm1 = DataInput.getBarcodesInput();
        bm1.setID(c.getID());
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm2 = DataInput.getBarcodesInput();
        bm2.setID(c.getID());
        BaseModel bm3 = barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertNull(bm3, "该对象未被删除");
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getBarcodesList();
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);

        if (!barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.fail("CreateNAsync测试失败!");
        }

        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("CreateNAsync测试失败!原因:超时");
        } else {
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateNAsync返回错误码不正确");

            List<Barcodes> BarcodesList = (List<Barcodes>) sqLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                Barcodes c = (Barcodes) list.get(i);
                c.setIgnoreIDInComparision(true);
                Assert.assertEquals(c.compareTo(BarcodesList.get(i)), 0, "CreateNAsync的数据与原数据不符");
            }
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //本地SQLite增加一条数据。全部同步后不存在
        Barcodes c = DataInput.getBarcodesInput();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");

        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);

        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.fail("同步失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("请求服务器同步Barcodes超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");

        List<Barcodes> list = (List<Barcodes>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != Integer.valueOf(barcodes.getPageSize())) {
            Assert.fail("全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }
}
