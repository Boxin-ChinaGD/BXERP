package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.PosPresenter;
import com.bx.erp.utils.Shared;
import com.test.bx.app.model.PosTest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class PosPresenterTest extends BaseAndroidTestCase {
    private static PosPresenter posPresenter;
    private static PosSQLiteEvent posSQLiteEvent;

    private static final int Event_ID_PosPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        posPresenter = GlobalController.getInstance().getPosPresenter();
        if(posSQLiteEvent == null){
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(Event_ID_PosPresenterTest);
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
        protected static final Pos getPOS() throws Exception {
            Pos p = new Pos();
            p.setSalt("123456");
            Thread.sleep(1);
            p.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
            Thread.sleep(1);
            p.setShopID(new Random().nextInt(9) + 1);
            p.setPasswordInPOS("123456");
            p.setPwdEncrypted("123456");
            p.setShopID(1);
            p.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            return (Pos) p.clone();
        }

        protected static final List<Pos> getPosList() throws Exception {
            List<Pos> posList = new ArrayList<Pos>();
            for (int i = 0; i < 10; i++) {
                posList.add(getPOS());
            }
            return posList;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == Event_ID_PosPresenterTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());        }

    }

    @Test
    public void test_a_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = DataInput.getPosList();
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case?????????????????????????????????
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null??????????????????
        posList = (List<Pos>) DataInput.getPosList();
        posList.get(0).setPos_SN(null);
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createNObjectSync?????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case?????????????????????????????????
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null??????????????????
        pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("??????Case,?????????Update");
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos.setShopID(2);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c2_UpdateSync() throws Exception {
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        caseLog("??????case???update???????????????????????????");
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", (posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError));
        Assert.assertTrue("update?????????", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c3_UpdateSync() throws Exception {
        caseLog("??????case???update????????????null????????????null");
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        p.setPos_SN(null);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateObjectSync?????????????????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c4_UpdateSync() throws Exception {
        caseLog("??????case???update??????????????????");
        Pos pos = DataInput.getPOS();
        pos.setID(99999l);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);// update????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c5_UpdateSync() throws Exception {
        caseLog("??????case????????????????????????");
        Pos pos = DataInput.getPOS();
        pos.setID(0l);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case1
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????????????????????????????", b.compareTo(pos) == 0);
    }

    @Test
    public void test_d2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????Case2
        Pos pos1 = new Pos();
        pos1.setID(0l);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("???????????????", b == null);
    }

    @Test
    public void test_d3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
        Pos pos2 = new Pos();
        pos2.setID(null);
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????????????????????????????", p == null);
    }

    @Test
    public void test_d4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID??????????????????????????????????????????
        Pos pos3 = new Pos();
        pos3.setID(-1);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos3);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("???????????????", b == null);
    }

    @Test
    public void test_e_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????list????????????null", posList != null && posList.size() > 0);
        //
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos.setSql("where F_POS_SN = ?");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????list????????????null", posList != null && posList.size() > 0);

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = ?");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync?????????????????????", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = ? and F_ID = ?");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = ? and F_ID = ?");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", p != null);
        //
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("deleteObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", p == null);
    }

    @Test
    public void test_f2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID????????????????????????
        Pos pos1 = new Pos();
        pos1.setID(0l);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("deleteObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_f3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null
        Pos pos = new Pos();
        pos.setID(null);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("deleteObjectSync??????????????????????????????" + posPresenter.getLastErrorCode(), posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_g1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        Pos pos = DataInput.getPOS();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????,compareTo??????", c.compareTo(pos) == 0);
    }

    @Test
    public void test_g2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        Pos pos = DataInput.getPOS();

        //??????Case
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????,compareTo??????", c.compareTo(pos) == 0);

        //??????case?????????????????????????????????
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g3_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case???????????????????????????null??????????????????
        Pos pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync????????????????????????????????????" + posSQLiteEvent.getLastErrorCode(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_h1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: ??????case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos.setPos_SN("11111");
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", p.compareTo(pos) == 0);
    }

    @Test
    public void test_h2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???update????????????null????????????null
        Pos pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_h3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???update??????????????????
        Pos pos = DataInput.getPOS();
        pos.setID(9999999l);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", p == null);
        //
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update????????????????????????????????????" + posSQLiteEvent.getLastErrorCode(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", pos.compareTo(p) == 0);
    }

    @Test
    public void test_i_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????pos", posSQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_j_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", p != null);
        //
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("deleteAsync?????????", false);
        }
        Assert.assertTrue("deleteAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", p == null);

        //??????case?????????????????????ID????????????????????????
        Pos pos1 = new Pos();
        pos1.setID(99999l);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction&& lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("deleteAsync?????????", false);
        }
        Assert.assertTrue("deleteAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case??????????????????ID???null????????????????????????
        Pos pos2 = DataInput.getPOS();
        pos2.setID(null);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos2, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done&& //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("deleteAsync?????????", false);
        }
        Assert.assertTrue("deleteAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_k_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();

        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.retrieve1ObjectAsync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieve1Async?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_l_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = (List<Pos>) DataInput.getPosList();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case?????????????????????????????????
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null??????????????????
        posList = (List<Pos>) DataInput.getPosList();
        for (Pos pos : posList) {
            pos.setPos_SN(null);
        }
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_m_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        List<Pos> posList = (List<Pos>) PosPresenterTest.DataInput.getPosList();
        List<BaseModel> liseBM = new ArrayList<>();
        for (BaseModel pos : posList) {
            pos.setSyncType(BasePresenter.SYNC_Type_C);
            liseBM.add(pos);
        }

        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData  &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction&& lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????(??????????????????????????????????????????)
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null
        List<BaseModel> cList = new ArrayList<BaseModel>();
        List<Pos> commList = (List<Pos>) PosPresenterTest.DataInput.getPosList();
        for (Pos pos : commList) {
            pos.setSyncType(BasePresenter.SYNC_Type_C);
            pos.setPos_SN(null);
            cList.add(pos);
        }
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, cList, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_n_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync???????????????list????????????", posList != null && posList.size() > 0);
        //
        posPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync???????????????list????????????", posList == null || posList.size() == 0);
    }

    @Test
    public void test_o_createOrReplaceAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //?????????????????????????????????
        Pos pos = DataInput.getPOS();
        pos.setPos_SN("T109179800011");
        pos.setShopID(2);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createOrReplaceAsync???", false);
        }
        Assert.assertTrue("createOrReplaceAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", pos.getShopID() == 2);

        //?????????????????????????????????????????????????????????
        pos.setShopID(1);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createOrReplaceAsync???", false);
        }
        Assert.assertTrue("createOrReplaceAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", pos.getShopID() == 1);

        //????????????????????????
        Pos pos1 = DataInput.getPOS();
        pos1.setPos_SN("t123");
        pos1.setShopID(2);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createOrReplaceAsync???", false);
        }
        Assert.assertTrue("createOrReplaceAsync?????????????????????", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", pos2.getShopID() == 2 && pos2.getPos_SN().equals("t123"));

        //????????????????????????????????????
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("deleteObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1ObjectSync?????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", pos3 == null);
    }

    private Pos createPos(Pos pos){
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", p.compareTo(pos) == 0);

        return p;
    }

    /**
     * ????????????POS???????????????
     */
    @Test
    public void test_retrieveNPos() throws Exception {
        Shared.printTestMethodStartInfo();

        PosPresenter posPresenter = GlobalController.getInstance().getPosPresenter();
        Integer total = posPresenter.retrieveCount();
        System.out.println("POS???????????????" + total);
        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }

}