package wpos.UT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.BrandSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.Brand;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;

public class BrandJUnitTest extends BaseHttpTestCase {
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;


    private static List<Brand> List;

    private static BaseModel bmCreateSync = null;
    private static BaseModel bmCreateAsync = null;
    private static BaseModel bmUpdateAsync = null;
    private static List<BaseModel> bmCreateNAsync = null;

    private static int createAsyncID = 0;
    private static final int EVENT_ID1_CreateAsync = 1;
    private static final int EVENT_ID2_CreateAsync = 2;
    private static final int EVENT_ID3_CreateAsync = 3;
    private static final int EVENT_ID4_CreateAsync = 4;

    private static final int EVENT_ID1_UpdateAsync = 5;
    private static final int EVENT_ID2_UpdateAsync = 6;
    private static final int EVENT_ID3_UpdateAsync = 7;

    private static final int EVENT_ID1_DeleteAsync = 13;

    private static final int EVENT_ID_CreateNAsync = 11;

    private static final long Timeout = 30 * 1000;

    private static final int EVENT_ID_BrandJUnitTest = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BrandJUnitTest);
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
            brandSQLiteEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        logoutHttpEvent.setId(EVENT_ID_BrandJUnitTest);
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(brandSQLiteEvent, brandHttpEvent);
            brandSQLiteBO.setBrandPresenter(brandPresenter);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(brandSQLiteEvent, brandHttpEvent);
        }
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
    }

    @AfterClass
    @Override
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
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Brand brandInput = null;

        protected static final Brand getBrandInput() throws CloneNotSupportedException {
            brandInput = new Brand();
            brandInput.setName("Game" + System.currentTimeMillis() % 1000000);
            brandInput.setReturnObject(1);

            return (Brand) brandInput.clone();
        }

        protected static final List<BaseModel> getBrandList() throws CloneNotSupportedException {
            List<BaseModel> commList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                commList.add(getBrandInput());
            }
            return commList;
        }
    }

    @Test
    public void test_a_RetrieveNEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2步，登录POS和Staff
        if(!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            System.out.println(posLoginHttpBO.getHttpEvent().getStatus() + "\t" + staffLoginHttpBO.getHttpEvent().getStatus());
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //4.模拟网页创建一个brand
        System.out.println("第一次调用create方法");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "创建失败!");
        }

        long lTimeOut = 30;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
        //POS2,STAFF2登陆
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }

        lTimeOut = 50;
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }
        List<Brand> brandList = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(brandList.size() > 0, "ListMasterTable为空");

        List = brandList;
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2步，登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO), "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        String brandIDs = "";
        for (int i = 0; i < List.size(); i++) {
            brandIDs = brandIDs + "," + List.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "同步失败");
        }
        long lTimeOut = 30;
        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback超时!");
        }
        //调用RN，返回无数据
        // 重置状态
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        System.out.println("第一次调用RN方法");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand同步超时!");
        }

        List<Brand> brandList = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(brandList == null, "feedback失败");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_c1_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: 正常case
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_CreateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        if (!brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！");
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！原因:超时");
        } else {
            Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError == event.getLastErrorCode(), "Create bm1时错误码应该为：EC_NoError，Create失败");
            Assert.assertTrue(bmCreateAsync != null, "插入数据后对象不应该为Null！");
            BaseModel bmCreate = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            createAsyncID = bmCreate.getID();
            bm1.setIgnoreIDInComparision(true);
            Assert.assertTrue(bm1.compareTo(bmCreate) == 0, "插入后的对象与查找到的对象不一致，插入失败！");
            System.out.println("插入的数据是：" + bmCreate);
        }
    }

    @Test
    public void test_c2_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //case：设置某一非空字段为空，字段检验不通过
        brandSQLiteEvent.setId(EVENT_ID2_CreateAsync);
        Brand bm2 = DataInput.getBrandInput();
        bm2.setName(null);
        if (!brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, brandSQLiteEvent)) {
            Assert.assertTrue(true, "CreateAsync bm2测试失败！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "CreateAsync bm2测试失败！当前错误码为：" + brandSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_c_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //case：主键冲突
        brandSQLiteEvent.setId(EVENT_ID3_CreateAsync);
        Brand bm3 = DataInput.getBrandInput();
        bm3.setID(createAsyncID);
        if (!brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm3测试失败！");
        }

        if (!waitForEventProcessed(brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm3测试失败！原因:超时");
        } else {
            Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError != brandSQLiteEvent.getLastErrorCode(), "Create bm3时错误码不应该为: Ec_NoError,name字段不能有字符,Create失败");
        }
        //注意：创建成功的还要保证数据正确
    }

    @Test
    public void test_d_UpdateEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: 正常case
        brandSQLiteEvent.setTimeout(Timeout);
        brandSQLiteEvent.setId(EVENT_ID1_CreateAsync);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        if (!brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！");
        }

        if (!waitForEventProcessed(brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！原因:超时");
        } else {
            Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError == brandSQLiteEvent.getLastErrorCode(), "Create bm1时错误码应该为：EC_NoError，Create失败");
            Assert.assertTrue(bmCreateAsync != null, "插入数据后对象不应该为Null！");
            BaseModel bmCreate = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            bm1.setID(bmCreate.getID());
            Assert.assertTrue(bm1.compareTo(bmCreate) == 0, "插入后的对象与查找到的对象不一致，插入失败！");
            System.out.println("插入的数据是：" + bmCreate);
        }

        //正确的修改
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(bm1.getID());
        bm2.setName("雪碧薯条柠檬1");
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        if (!brandPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, brandSQLiteEvent)) {
            Assert.assertTrue(false, "UpdateAsync bm1测试失败！");
        }

        if (!waitForEventProcessed(brandSQLiteEvent)) {
            Assert.assertTrue(false, "UpdateAsync bm1测试失败！原因:超时");
        } else {
            Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError.equals(brandSQLiteEvent.getLastErrorCode()), "Update bm1时错误码应该为：EC_NoError，Update失败");
            BaseModel bmUpdate = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
            Assert.assertTrue(bm2.compareTo(bmUpdate) == 0, "更新后的对象与查找到的对象不一致，插入失败！");
            System.out.println("成功修改对象后：" + bmUpdate);
        }

        //修改非空字段为空
        brandSQLiteEvent.setId(EVENT_ID2_UpdateAsync);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        Brand bm3 = DataInput.getBrandInput();
        bm3.setID(bm2.getID());
        bm3.setName(null);
        brandPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, brandSQLiteEvent);
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "UpdateAsync bm3测试失败， 期望的错误码不正确！当前错误码为：" + brandSQLiteEvent.getLastErrorCode());


        //不传递ID去进行修改
        brandSQLiteEvent.setId(EVENT_ID3_UpdateAsync);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        Brand bm4 = DataInput.getBrandInput();
        bm3.setName("lalalalalala");
        brandPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, brandSQLiteEvent);
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "UpdateAsync bm4测试失败， 期望的错误码不正确！当前错误码为：" + brandSQLiteEvent.getLastErrorCode());

    }

    @Test
    public void test_e_DeleteEx() throws Exception {
        Shared.printTestMethodStartInfo();

        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_CreateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        if (!brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！");
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！原因:超时");
        } else {
            Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError == event.getLastErrorCode(), "Create bm1时错误码应该为：EC_NoError，Create失败");
            Assert.assertTrue(bmCreateAsync != null, "插入数据后对象不应该为Null！");
            BaseModel bmCreate = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            bm1.setID(bmCreate.getID());
            Assert.assertTrue(bm1.compareTo(bmCreate) == 0, "插入后的对象与查找到的对象不一致，插入失败！");
            System.out.println("插入的数据是：" + bmCreate);
        }

        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_DeleteAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(bm1.getID());
        if (!brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, event)) {
            Assert.assertTrue(false, "DeleteAsync bm1测试失败！");
        }
        //
        if (!waitForEventProcessed(event)) {
            Assert.assertTrue(false, "DeleteAsync bm2测试失败！原因:超时");
        } else {
            BaseModel bmUpdate = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            Assert.assertTrue(bmUpdate == null, "Delete bm2时错误码不应该为：EC_NoError，Delete失败");
        }
    }

    @Test
    public void test_e2_DeleteEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //ID 为负数
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(-1);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event);
        Assert.assertTrue(event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "UpdateAsync bm4测试失败， 期望的错误码不正确！当前错误码为：" + event.getLastErrorCode());
    }

    @Test
    public void test_e3_DeleteEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //ID 为负数
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(null);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event);
        Assert.assertTrue(event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "UpdateAsync bm4测试失败， 期望的错误码不正确！当前错误码为：" + event.getLastErrorCode());
    }


    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getBrandList();
        List<?> brandCreateList = brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);

        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1测试失败,原因:返回错误码不正确!");
        //
        boolean isExist;
        for (BaseModel bm : list) {
            isExist = false;
            Brand brand = (Brand) bm;
            for (Object o : brandCreateList) {
                Brand brandCreate = (Brand) o;
                if (brand.getName().equals(brandCreate.getName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Assert.assertTrue(false, "CreateNSync测试失败,原因:所插入数据与查询到的不一致!");
            }
        }
    }

    @Test
    public void test_g1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        Brand brandCreate = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);


        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Brand brand1Retrieve1 = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandCreate);
        System.out.println("输出：同步请求创建的Brand是：：：" + brand1Retrieve1);
        //
        bmCreateSync.setIgnoreIDInComparision(true);
        Assert.assertTrue(bmCreateSync.compareTo(brand1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        //
        bmCreateSync.setID(brand1Retrieve1.getID());
    }

    @Test
    public void test_g2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        Brand brandCreate = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);


        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Brand brand1Retrieve1 = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandCreate);
        System.out.println("输出：同步请求创建的Brand是：：：" + brand1Retrieve1);
        //
        bmCreateSync.setIgnoreIDInComparision(true);
        Assert.assertTrue(bmCreateSync.compareTo(brand1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        //
        bmCreateSync.setID(brand1Retrieve1.getID());
        //异常case: 插入重复ID
        bmCreateSync.setID(brand1Retrieve1.getID());
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        System.out.println("输出：同步请求创建的Brand是" + bmCreateSync);

        Assert.assertTrue(brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        Brand brandCreate = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Brand brand1Retrieve1 = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandCreate);
        System.out.println("输出：同步请求创建的Brand是：：：" + brand1Retrieve1);
        //
        bmCreateSync.setIgnoreIDInComparision(true);
        Assert.assertTrue(bmCreateSync.compareTo(brand1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        //
        bmCreateSync.setID(brand1Retrieve1.getID());
        // 插入null作为非空字段的值
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(brand1Retrieve1.getID());
        bm2.setName(null);
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);

        Assert.assertTrue(brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_h1_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        System.out.println("输出：ID是：：：" + bmCreateSync.getID());
        bm1.setID(bmCreateSync.getID());
        bm1.setName("update之后的Name");
        if (brandPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1测试失败,原因:返回错误码不正确!");
            //
            Brand comm1Rerieve1 = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
            //
            Assert.assertTrue(bm1.compareTo(comm1Rerieve1) == 0, "UpdateSync测试失败,原因:查询到的数据没有更新!");
        } else {
            Assert.assertTrue(false, "Updateync测试失败!");
        }


    }

    @Test
    public void test_h2_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        System.out.println("输出：ID是：：：" + bmCreateSync.getID());
        bm1.setID(bmCreateSync.getID());
        bm1.setName("update之后的Name");
        //异常Case: Update非空字段为null
        bm1.setName(null);
        if (brandPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            Assert.assertTrue(false, "UpdateSync测试失败!");
        }

    }


    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(bmCreateSync.getID());
        brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm2 = DataInput.getBrandInput();
        bm2.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm3 = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "Retrieve1 测试失败,返回的basemodel不为null");
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException {
        BaseModel bm1 = DataInput.getBrandInput();
        bm1.setID(bmCreateSync.getID());
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm2 = DataInput.getBrandInput();
        bm2.setID(bmCreateSync.getID());
        BaseModel bm3 = brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "该对象未被删除");
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setId(EVENT_ID_CreateNAsync);
        brandSQLiteEvent.setTimeout(Timeout);
        List<BaseModel> list = DataInput.getBrandList();
        if (!brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateNAsync测试失败!");
        }

        if (!waitForEventProcessed(brandSQLiteEvent)) {
            Assert.assertTrue(false, "CreateNAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNAsync返回错误码不正确");

            List<?> brandCreateList = BrandJUnitTest.brandSQLiteEvent.getListMasterTable();
            boolean isExist;
            for (BaseModel bm : list) {
                isExist = false;
                Brand brand = (Brand) bm;
                for (Object o : brandCreateList) {
                    Brand brandCreate = (Brand) o;
                    if (brand.getName().equals(brandCreate.getName())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    Assert.assertTrue(false, "CreateNSync测试失败,原因:所插入数据与查询到的不一致!");
                }
            }
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //本地SQLite增加一条数据。全部同步后不存在
        bmCreateSync = DataInput.getBrandInput();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        Brand b = new Brand();
        b.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        b.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!brandHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, b)) {
            Assert.assertTrue(false, "同步失败！");
        }

        long lTimeOut = 30;
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "请求服务器同步Brand超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Brand> brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");

        List<Brand> list = (List<Brand>) brandHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != brandList.size()) {
            Assert.assertTrue(false, "全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandEvent(BrandSQLiteEvent event) {
        switch (event.getEventTypeSQLite()) {
            case ESET_Brand_CreateAsync:
                switch (event.getId()) {
                    case EVENT_ID1_CreateAsync:
                    case EVENT_ID2_CreateAsync:
                    case EVENT_ID3_CreateAsync:
                        bmCreateAsync = event.getBaseModel1();
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("未定义的事件！");
                }
                break;
            case ESET_Brand_CreateNAsync:
                switch (event.getId()) {
                    case EVENT_ID_CreateNAsync:
                        bmCreateNAsync = (List<BaseModel>) event.getListMasterTable();
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("未定义的事件！");
                }
                break;
            case ESET_Brand_UpdateAsync:
                switch (event.getId()) {
                    case EVENT_ID1_UpdateAsync:
                    case EVENT_ID2_UpdateAsync:
                    case EVENT_ID3_UpdateAsync:
                        bmUpdateAsync = event.getBaseModel1();
                        Assert.assertTrue(bmUpdateAsync != null, "更新的对象不能为Null！");
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("未定义的事件！");
                }
                break;
            case ESET_Brand_DeleteAsync:
                switch (event.getId()) {
                    case EVENT_ID1_DeleteAsync:
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("未定义的事件！");
                }
                break;
        }
    }

}
