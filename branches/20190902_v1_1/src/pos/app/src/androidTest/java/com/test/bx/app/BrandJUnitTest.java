package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.bo.BrandSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Brand;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BrandPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BrandJUnitTest extends BaseHttpAndroidTestCase {
    private static BrandPresenter presenter = null;
    private static BrandSQLiteBO sqLiteBO = null;
    private static BrandHttpBO httpBO = null;
    private static BrandSQLiteEvent sqLiteEvent = null;
    private static BrandHttpEvent httpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;


    private static List<Brand> List;

    private static BaseModel bmCreateSync = null;
    private static BaseModel bmCreateAsync = null;
    private static BaseModel bmUpdateAsync = null;
    private static List<BaseModel> bmCreateNAsync = null;

    private static long createAsyncID = 0l;
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

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getBrandPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BrandJUnitTest);
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
            sqLiteEvent = new BrandSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (httpEvent == null) {
            httpEvent = new BrandHttpEvent();
            httpEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_BrandJUnitTest);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
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
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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

        //1,2????????????POS???Staff
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        //4.????????????????????????brand
        System.out.println("???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue("????????????!", false);
        }

        long lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }

        //POS2,STAFF2??????
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }

        lTimeOut = 50;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }
        List<Brand> brandList = (List<Brand>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("ListMasterTable??????", brandList.size() > 0);

        List = brandList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        String brandIDs = "";
        for (int i = 0; i < List.size(); i++) {
            brandIDs = brandIDs + "," + List.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //??????Feedback,
        System.out.println("???????????????feedback??????");
        if (!httpBO.feedback(brandIDs)) {
            Assert.assertTrue("????????????", false);
        }
        long lTimeOut = 30;
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Brand Feedback??????!", false);
        }
        //??????RN??????????????????
        System.out.println("???????????????RN??????");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }

        List<Brand> brandList = (List<Brand>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("feedback??????", brandList == null);
    }

    @Test
    public void test_c1_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: ??????case
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_CreateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        if (!presenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event)) {
            Assert.assertTrue("CreateAsync bm1???????????????", false);
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("CreateAsync bm1?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", ErrorInfo.EnumErrorCode.EC_NoError == event.getLastErrorCode());
            Assert.assertTrue("?????????????????????????????????Null???", bmCreateAsync != null);
            BaseModel bmCreate = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            createAsyncID = bmCreate.getID();
            Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm1.compareTo(bmCreate) == 0);
            System.out.println("?????????????????????" + bmCreate);
        }
    }

    @Test
    public void test_c2_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //case?????????????????????????????????????????????????????????
        sqLiteEvent.setId(EVENT_ID2_CreateAsync);
        Brand bm2 = DataInput.getBrandInput();
        bm2.setName(null);
        if (!presenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, sqLiteEvent)) {
            Assert.assertTrue("CreateAsync bm2???????????????", true);
        }
        Assert.assertTrue("CreateAsync bm2????????????????????????????????????" + sqLiteEvent.getLastErrorCode(), sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_c_CreateEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //case???????????????
        sqLiteEvent.setId(EVENT_ID3_CreateAsync);
        Brand bm3 = DataInput.getBrandInput();
        bm3.setID(createAsyncID);
        if (!presenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, sqLiteEvent)) {
            Assert.assertTrue("CreateAsync bm3???????????????", false);
        }

        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("CreateAsync bm3?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Create bm3????????????????????????: Ec_NoError,name?????????????????????,Create??????", ErrorInfo.EnumErrorCode.EC_NoError != sqLiteEvent.getLastErrorCode());
        }
        //????????????????????????????????????????????????
    }

    @Test
    public void test_d_UpdateEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: ??????case
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_CreateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        if (!presenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event)) {
            Assert.assertTrue("CreateAsync bm1???????????????", false);
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("CreateAsync bm1?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", ErrorInfo.EnumErrorCode.EC_NoError == event.getLastErrorCode());
            Assert.assertTrue("?????????????????????????????????Null???", bmCreateAsync != null);
            BaseModel bmCreate = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm1.compareTo(bmCreate) == 0);
            System.out.println("?????????????????????" + bmCreate);
        }

        //???????????????
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(bm1.getID());
        bm2.setName("??????????????????1");
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        if (!presenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, event)) {
            Assert.assertTrue("UpdateAsync bm1???????????????", false);
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("UpdateAsync bm1?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Update bm1????????????????????????EC_NoError???Update??????", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
            BaseModel bmUpdate = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
            Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm2.compareTo(bmUpdate) == 0);
            System.out.println("????????????????????????" + bmUpdate);
        }

        //????????????????????????
        event.setId(EVENT_ID2_UpdateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        Brand bm3 = DataInput.getBrandInput();
        bm3.setID(bm2.getID());
        bm3.setName(null);
        presenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, event);
        Assert.assertTrue("UpdateAsync bm3??????????????? ???????????????????????????????????????????????????" + event.getLastErrorCode(), event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);


        //?????????ID???????????????
        event.setId(EVENT_ID3_UpdateAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
        Brand bm4 = DataInput.getBrandInput();
        bm3.setName("lalalalalala");
        presenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, event);
        Assert.assertTrue("UpdateAsync bm4??????????????? ???????????????????????????????????????????????????" + event.getLastErrorCode(), event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

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
        if (!presenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event)) {
            Assert.assertTrue("CreateAsync bm1???????????????", false);
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("CreateAsync bm1?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", ErrorInfo.EnumErrorCode.EC_NoError == event.getLastErrorCode());
            Assert.assertTrue("?????????????????????????????????Null???", bmCreateAsync != null);
            BaseModel bmCreate = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm1.compareTo(bmCreate) == 0);
            System.out.println("?????????????????????" + bmCreate);
        }

        event.setTimeout(Timeout);
        event.setId(EVENT_ID1_DeleteAsync);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(bm1.getID());
        if (!presenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, event)) {
            Assert.assertTrue("DeleteAsync bm1???????????????", false);
        }
        //
        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("DeleteAsync bm2?????????????????????:??????", false);
        } else {
            BaseModel bmUpdate = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
            Assert.assertTrue("Delete bm2???????????????????????????EC_NoError???Delete??????", bmUpdate == null);
        }
    }

    @Test
    public void test_e2_DeleteEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //ID ?????????
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(-1L);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        presenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event);
        Assert.assertTrue("UpdateAsync bm4??????????????? ???????????????????????????????????????????????????" + event.getLastErrorCode(), event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_e3_DeleteEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //ID ?????????
        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setTimeout(Timeout);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(null);
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        presenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, event);
        Assert.assertTrue("UpdateAsync bm4??????????????? ???????????????????????????????????????????????????" + event.getLastErrorCode(), event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getBrandList();
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);

        Assert.assertTrue("CreateNSync1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < list.size(); i++) {
            Assert.assertTrue("CreateNSync????????????,??????:???????????????????????????????????????!", list.get(i).compareTo(presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list.get(i))) == 0);
        }
    }

    @Test
    public void test_g1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);


        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Brand brand1Retrieve1 = (Brand) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        bmCreateSync = brand1Retrieve1;
        System.out.println("??????????????????????????????Brand????????????" + brand1Retrieve1);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", bmCreateSync.compareTo(brand1Retrieve1) == 0);
    }

    @Test
    public void test_g2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);


        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Brand brand1Retrieve1 = (Brand) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        bmCreateSync = brand1Retrieve1;
        System.out.println("??????????????????????????????Brand????????????" + brand1Retrieve1);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", bmCreateSync.compareTo(brand1Retrieve1) == 0);
        //??????case: ????????????ID
        bmCreateSync.setID(brand1Retrieve1.getID());
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        System.out.println("??????????????????????????????Brand???" + bmCreateSync);

        Assert.assertTrue("CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!", presenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        bmCreateSync = DataInput.getBrandInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);


        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Brand brand1Retrieve1 = (Brand) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        bmCreateSync = brand1Retrieve1;
        System.out.println("??????????????????????????????Brand????????????" + brand1Retrieve1);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", bmCreateSync.compareTo(brand1Retrieve1) == 0);
        // ??????null????????????????????????
        Brand bm2 = DataInput.getBrandInput();
        bm2.setID(brand1Retrieve1.getID());
        bm2.setName(null);
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);

        Assert.assertTrue("CreateSync bm2????????????,??????:???????????????????????????EC_NoError!", presenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h1_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        System.out.println("?????????ID????????????" + bmCreateSync.getID());
        bm1.setID(bmCreateSync.getID());
        bm1.setName("update?????????Name");
        if (presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            Assert.assertTrue("UpdateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            Brand comm1Rerieve1 = (Brand) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
            //
            Assert.assertTrue("UpdateSync????????????,??????:??????????????????????????????!", bm1.compareTo(comm1Rerieve1) == 0);
        } else {
            Assert.assertTrue("Updateync????????????!", false);
        }


    }

    @Test
    public void test_h2_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        System.out.println("?????????ID????????????" + bmCreateSync.getID());
        bm1.setID(bmCreateSync.getID());
        bm1.setName("update?????????Name");
        //??????Case: Update???????????????null
        bm1.setName(null);
        if (presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            Assert.assertTrue("UpdateSync????????????!", false);
        }

    }


    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Brand bm1 = DataInput.getBrandInput();
        bm1.setID(bmCreateSync.getID());
        presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getBrandInput();
        bm2.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm3 = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Retrieve1 ????????????,?????????basemodel??????null", bm3 == null);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException {
        BaseModel bm1 = DataInput.getBrandInput();
        bm1.setID(bmCreateSync.getID());
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getBrandInput();
        bm2.setID(bmCreateSync.getID());
        BaseModel bm3 = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        BrandSQLiteEvent event = new BrandSQLiteEvent();
        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        event.setId(EVENT_ID_CreateNAsync);
        event.setTimeout(Timeout);
        List<BaseModel> list = DataInput.getBrandList();
        if (!presenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, event)) {
            Assert.assertTrue("CreateNAsync????????????!", false);
        }

        if (!waitForEventProcessed(event)) {
            Assert.assertTrue("CreateNAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("CreateNAsync????????????????????????", event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            for (int i = 0; i < list.size(); i++) {
                Assert.assertTrue("CreateNAsync???????????????????????????", list.get(i).compareTo(presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateNAsync.get(i))) == 0);
            }
        }
    }

    //????????????
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //??????SQLite?????????????????????????????????????????????
        bmCreateSync = DataInput.getBrandInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");
        Brand b = new Brand();
        b.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        b.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, b)) {
            Assert.assertTrue("???????????????", false);
        }

        long lTimeOut = 30;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }

        //???????????????????????????sqlite??????????????????,
        List<Brand> brandList = (List<Brand>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Brand> list = (List<Brand>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != brandList.size()) {
            Assert.assertTrue("??????????????????????????????????????????????????????SQLite??????????????????", false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandEvent(BrandSQLiteEvent event) {
        switch (event.getEventTypeSQLite()) {
            case ESET_Brand_CreateAsync:
                switch (event.getId()) {
                    case EVENT_ID1_CreateAsync:
                        bmCreateAsync = event.getBaseModel1();
                        event.setEventProcessed(true);
                        break;
                    case EVENT_ID2_CreateAsync:
                        bmCreateAsync = event.getBaseModel1();
                        event.setEventProcessed(true);
                        break;
                    case EVENT_ID3_CreateAsync:
                        bmCreateAsync = event.getBaseModel1();
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("?????????????????????");
                }
                break;
            case ESET_Brand_CreateNAsync:
                switch (event.getId()) {
                    case EVENT_ID_CreateNAsync:
                        bmCreateNAsync = (List<BaseModel>) event.getListMasterTable();
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("?????????????????????");
                }
                break;
            case ESET_Brand_UpdateAsync:
                switch (event.getId()) {
                    case EVENT_ID1_UpdateAsync:
                        bmUpdateAsync = event.getBaseModel1();
                        Assert.assertTrue("????????????????????????Null???", bmUpdateAsync != null);
                        event.setEventProcessed(true);
                        break;
                    case EVENT_ID2_UpdateAsync:
                        bmUpdateAsync = event.getBaseModel1();
                        Assert.assertTrue("????????????????????????Null???", bmUpdateAsync != null);
                        event.setEventProcessed(true);
                        break;
                    case EVENT_ID3_UpdateAsync:
                        bmUpdateAsync = event.getBaseModel1();
                        Assert.assertTrue("????????????????????????Null???", bmUpdateAsync != null);
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("?????????????????????");
                }
                break;
            case ESET_Brand_DeleteAsync:
                switch (event.getId()) {
                    case EVENT_ID1_DeleteAsync:
                        event.setEventProcessed(true);
                        break;
                    default:
                        throw new RuntimeException("?????????????????????");
                }
                break;
        }
    }

}
