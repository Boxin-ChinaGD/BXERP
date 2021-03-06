package com.test.bx.app.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.VipPresenter;
import com.base.BaseAndroidTestCase;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class VipPresenterTest extends BaseAndroidTestCase {
    private static VipPresenter vipPresenter;
    private static VipSQLiteEvent vipSQLiteEvent;

    private static final int Event_ID_VipPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        vipPresenter = GlobalController.getInstance().getVipPresenter();
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_VipPresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);

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

    public static class DataInput {
        private static Vip vip = new Vip();

        protected static final Vip getVip() throws Exception {
            Random ran = new Random();
            vip = new Vip();
            vip.setMobile("1212" + String.valueOf(System.currentTimeMillis()).substring(6));
            vip.setCardID(1);
//            vip.setICID("320803199707" + String.valueOf(System.currentTimeMillis()).substring(7));
            vip.setICID(Shared.getValidICID());
            Thread.sleep(1);
            vip.setName(Shared.generateCompanyName(ran.nextInt(10) + 1));
            Thread.sleep(1);
            vip.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
            vip.setConsumeTimes(0);
            vip.setConsumeAmount(0);
            vip.setDistrict("??????");
            vip.setCategory(1);
            vip.setBirthday(new Date());
            vip.setBonus(0);
            vip.setLastConsumeDatetime(new Date());
            vip.setLocalPosSN("123456");
            return (Vip) vip.clone();
        }

        protected static final List<BaseModel> getVipList() throws Exception {
            List<BaseModel> vipList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                vipList.add(getVip());
            }
            return vipList;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == Event_ID_VipPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());        }
    }

    @Test
    public void test_i_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<BaseModel> vipList = DataInput.getVipList();
        vipPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipList);

        Assert.assertTrue("CreateNSync1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < vipList.size(); i++) {
            Assert.assertTrue("CreateNSync????????????,??????:???????????????????????????????????????!", vipList.get(i).compareTo(vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipList.get(i))) == 0);
        }
    }

    @Test
    public void test_a1_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Vip vip1Retrieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", bmCreateSync.compareTo(vip1Retrieve1) == 0);
    }

    @Test
    public void test_a2_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case: ????????????ID
        Vip vip = DataInput.getVip();
        Vip bmCreateSync = createVip(vip);

        bmCreateSync.setID(bmCreateSync.getID());
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_a3_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        // ??????null????????????????????????
        Vip bm2 = DataInput.getVip();
        Vip bmCreateSync = createVip(bm2);

        bm2.setID(bmCreateSync.getID());
        bm2.setMobile(null);
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);

        Assert.assertTrue("CreateSync bm2????????????,??????:???????????????????????????EC_NoError!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip bm1 = DataInput.getVip();
        bm1.setID(bmCreateSync.getID());
        bm1.setMobile("13144496181");

        //???????????????case???update??????????????????
        vipPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("UpdateSync bm1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip comm1Rerieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1ObjectSync??????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update", bm1.compareTo(comm1Rerieve1) == 0);
    }

    @Test
    public void test_b2_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????Case: Update???????????????null

        Vip bm1 = DataInput.getVip();
        bm1.setMobile(null);
        vipPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("updateObjectSync?????????????????????", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_c_RetrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Vip> VipList = (List<Vip>) vipPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("RetrieveNSync??????????????????????????????>=0!", VipList.size() >= 0);

        //?????????????????? //...????????????case??????????????????????????????
        Vip vip = new Vip();
        vip.setSql("where F_CardID = ?");
        vip.setConditions(new String[]{"1"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        VipList = (List<Vip>) vipPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, vip);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("????????????RetrieveNSync??????????????????????????????>=0!", VipList.size() >= 0);
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????case
        Vip bm1 = new Vip();
        bm1.setID(bmCreateSync.getID());
        vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d2_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //ID????????????case
        BaseModel bm2 = DataInput.getVip();
        bm2.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm3 = vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Retrieve1 ????????????,?????????basemodel??????null", bm3 == null);
    }

    @Test
    public void test_e_DeleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("???????????????", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip bm1 = new Vip();
        bm1.setID(bmCreateSync.getID());
        vipPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("deleteObjectSync ????????????,??????????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getVip();
        bm2.setID(bmCreateSync.getID());
        BaseModel bm3 = vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", bm3 == null);
    }

    @Test
    public void test_a1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bm = DataInput.getVip();
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        //????????????case
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("create?????????", bm.compareTo(v) == 0);
    }

    @Test
    public void test_a2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //????????????case(????????????)
        Vip vip = DataInput.getVip();
        Vip bmCreated = createVip(vip);
        vip.setID(bmCreated.getID());

        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, vip, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_a3_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        Vip bm4 = DataInput.getVip();
        bm4.setMobile(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //????????????.
        Vip bm = DataInput.getVip();
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bm.setID(bmCreateSync.getID());
        bm.setMobile("????????????Mobile");
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", bm.compareTo(v) == 0);
    }

    @Test
    public void test_b2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //model???ID??????
        Vip bm2 = DataInput.getVip();
        bm2.setMobile("???ID????????????");
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????????????????
        Vip vip = DataInput.getVip();
        Vip bmCreateSync = createVip(vip);
        vip.setID(bmCreateSync.getID());
        vip.setMobile(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, vip, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b4_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //???model
        Vip bm4 = null;
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        try {
            vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, vipSQLiteEvent);
        } catch (Exception e) {
            Assert.assertTrue("???????????????", "Attempt to invoke virtual method 'java.lang.String com.bx.erp.model.BaseModel.checkUpdate(int)' on a null object reference".equals(e.getMessage()));
        }
    }

    @Test
    public void test_c_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //???????????????Case
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????", vipSQLiteEvent.getListMasterTable().size() > 0);
    }

    // TODO ???????????????????????????
//    @Test
//    public void test_c2_RetrieveNAsync() throws InterruptedException {
//        Shared.printTestMethodStartInfo();
//        //??????????????????
//        Vip v = new Vip();
//        v.setSql("where F_Status = ?");
//        v.setConditions(new String[]{"0"});
//        v.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v, vipSQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            Assert.assertTrue("retrieveNAsync?????????", false);
//        }
//        Assert.assertTrue("retrieveNAsync?????????????????????" + vipSQLiteEvent.printErrorInfo(), vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("retrieveNAsync????????????????????????????????????", vipSQLiteEvent.getListMasterTable().size() > 0);
//    }

    @Test
    public void test_c3_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //?????????case,????????????????????????
        Vip v2 = new Vip();
        v2.setSql("where F_Status = ?");
        v2.setConditions(new String[]{"1", "0"});
        v2.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c4_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        ////??????Case???????????????????????????
        Vip v3 = new Vip();
        v3.setSql("where F_Status = ? and F_POS_SN = ?");
        v3.setConditions(new String[]{"1"});
        v3.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v3, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", vipSQLiteEvent.getListMasterTable() == null);
    }

    @Test
    public void test_c5_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????Case???????????????????????????
        Vip v4 = new Vip();
        v4.setSql("where F_CardID = ? and F_LocalPosSN = ?");
        v4.setConditions(new String[]{"1", "0"});
        v4.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v4, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????" + vipSQLiteEvent.printErrorInfo(), vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", vipSQLiteEvent.getListMasterTable().size() >= 0);
    }

    @Test
    public void test_d_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //?????????Case
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync?????????", false);
        }
        Assert.assertTrue("DeleteAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("DeleteAsync?????????", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", v == null);
    }

    @Test
    public void test_d2_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //????????????ID,??????????????????
        Vip v1 = DataInput.getVip();
        v1.setID(Shared.SQLITE_ID_Infinite);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, v1, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync?????????", false);
        }
        Assert.assertTrue("DeleteAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d3_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null
        Vip v2 = DataInput.getVip();
        v2.setID(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, v2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync?????????", false);
        }
        Assert.assertTrue("DeleteAsync?????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_j_CreateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        List<BaseModel> BarcodesList = DataInput.getVipList();
        vipPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync?????????", false);
        }
        Assert.assertTrue("CreateNAsync????????????????????????", vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        for (int i = 0; i < BarcodesList.size(); i++) {
            Assert.assertTrue("CreateNAsync???????????????????????????", BarcodesList.get(i).compareTo(vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i))) == 0);
        }
    }

    private Vip createVip(Vip vip) {
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vip);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Vip vip1Retrieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vip);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", vip.compareTo(vip1Retrieve1) == 0);

        return vip1Retrieve1;
    }

    /**
     * ????????????VIP???????????????
     */
    @Test
    public void test_retrieveNVip() throws Exception {
        Shared.printTestMethodStartInfo();

        VipPresenter vipPresenter = GlobalController.getInstance().getVipPresenter();
        Integer total = vipPresenter.retrieveCount();
        System.out.println("VIP???????????????" + total);
        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }

}
