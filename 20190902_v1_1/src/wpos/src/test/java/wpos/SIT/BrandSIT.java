package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BrandSQLiteEvent;
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.Brand;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.List;

public class BrandSIT extends BaseHttpTestCase {
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BrandSIT = 10000;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(EVENT_ID_BrandSIT);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(brandSQLiteEvent, brandHttpEvent);
            brandSQLiteBO.setBrandPresenter(brandPresenter);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(brandSQLiteEvent, brandHttpEvent);
        }
        logoutHttpEvent.setId(EVENT_ID_BrandSIT);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
    }

    @Override
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        event.onEvent();
    }


    public static class DataInput {
        private static Brand brandInput = null;

        protected static final Brand getBrandInput() throws CloneNotSupportedException {
            brandInput = new Brand();
            brandInput.setName("Game" + System.currentTimeMillis() % 1000000);
            brandInput.setReturnObject(1);

            return (Brand) brandInput.clone();
        }
    }

    /*
    1.pos登录
    2.staff登录
    3.暂停同步线程
    4.模拟网页创建一个brand
    5.调用RN(有同步数据)
    6.调用feedbackEx。
    7.调用RN（无同步数据）
    8.模拟网页创建一个brand
    9.恢复线程, 线程是否顺利执行
    10.退出登录
     */

    @Test
    public void testBrand() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1,2步，登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //4.模拟网页创建一个brand
        System.out.println("第一次调用create方法");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "创建失败!" + brandHttpBO.getHttpEvent().getLastErrorCode());
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }

        //RN前登陆另一台POS，使其有同步数据
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }

        lTimeOut = 50;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }
        List<Brand> brandList = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(brandList.size() > 0, "ListMasterTable为空");
        System.out.println("第一次RN后同步了的brandList为：" + brandList);
        String brandIDs = "";
        for (int i = 0; i < brandList.size(); i++) {
            brandIDs = brandIDs + "," + brandList.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());
        System.out.println("把BrandList分割后得到的BrandIDs = " + brandIDs);

        //6.调用feedbackEx。
        System.out.println("第一次调用feedback方法");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "同步失败");
        }
        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback超时!");
        }

//        7.调用RN（无同步数据）
        System.out.println("第二次调用RN方法");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }

//        8.模拟网页创建一个brand
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        lTimeOut = 30;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }

//        9.退出登录
        //
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "退出登录失败! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "退出超时!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "退出登录,服务器返回的错误码不正确");
    }

    //1）POS1：通过普通Action创建Brand A和Brand B->收到服务器响应，错误码正确->仍然通过普通Action修改Brand B->收到服务器响应，错误码正确。
    // 2）POS2：开机->通过SyncRN同步Brand A和修改后的Brand B->Feedback。
    @Test
    public void testSIT2() throws Exception {
        Shared.printTestMethodStartInfo();
        //登录pos1和staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //pos1创建Brand_A
        System.out.println("-------------- pos1第一次调用create方法");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }
        Brand b = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue(false, "创建的和预期的值不一样");
        }

        //pos1创建Brand_B
        System.out.println("---------------- pos1第二次调用create方法");
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }
        Brand b2 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue(false, "创建的和预期的值不一样");
        }

        //pos1修改Brand_B
        System.out.println("-------------------- pos1第一次调用update方法");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器修改Brand超时!");
        }
        Brand b3 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue(false, "修改的和预期的值不一样");
        }

        //pos2和staff2登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //调用RN同步Brand_A和修改后的Brand_B
        System.out.println("-------------------- pos2第一次调用RN方法");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败!");
        }

        //检查错误码
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }
        List<Brand> list = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list为空");
        System.out.println("第一次RN后同步了的brandList为：" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2第一次调用feedback方法");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "同步失败");
        }

        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback超时!");
        }

        //9.退出登录
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "退出登录失败! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "创建超时!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "退出登录,服务器返回的错误码不正确");

    }

    @Test
    public void testSIT3() throws Exception {
        Shared.printTestMethodStartInfo();
        //登录pos1和staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //pos1创建Brand_A
        System.out.println("-------------- pos1第一次调用create方法");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }
        Brand b = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue(false, "创建的和预期的值不一样");
        }

        //pos1创建Brand_B
        System.out.println("---------------- pos1第二次调用create方法");
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }
        Brand b2 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue(false, "创建的和预期的值不一样");
        }

        //pos1修改Brand_B
        System.out.println("-------------------- pos1第一次调用update方法");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器修改Brand超时!");
        }
        Brand b3 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue(false, "修改的和预期的值不一样");
        }

        //pos1修改brandB
        System.out.println("-------------------- pos1第二次调用update方法");
        Brand brand4 = DataInput.getBrandInput();
        brand4.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand4)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器修改Brand超时!");
        }
        Brand b4 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand4.setIgnoreIDInComparision(true);
        if (brand4.compareTo(b4) != 0) {
            Assert.assertTrue(false, "修改的和预期的值不一样");
        }

        //pos2和staff2登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //调用RN同步Brand_A和修改后的Brand_B
        System.out.println("-------------------- pos2第一次调用RN方法");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败!");
        }

        //检查错误码
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }
        List<Brand> list = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list为空");
        System.out.println("第一次RN后同步了的brandList为：" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2第一次调用feedback方法");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "同步失败");
        }

        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback超时!");
        }

        //9.退出登录
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "退出登录失败! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "创建超时!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "退出登录,服务器返回的错误码不正确");

    }
}