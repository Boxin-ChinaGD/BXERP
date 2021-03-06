package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.PosSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Pos;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PosPresenterTest extends BasePresenterTest {
    private static final int Event_ID_PosPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();

        if (posSQLiteEvent != null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(Event_ID_PosPresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == Event_ID_PosPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }

    }

    @Test
    public void test_a_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = DataInput.getPosList();
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");

        //??????case?????????????????????????????????
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createObjectSync?????????");

        //??????case???????????????????????????null??????????????????
        posList = (List<Pos>) DataInput.getPosList();
        posList.get(0).setPos_SN(null);
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
    }

    @Test
    public void test_b_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");

        //??????case?????????????????????????????????
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createObjectSync?????????");

        //??????case???????????????????????????null??????????????????
        pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
    }

    @Test
    public void test_c1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.caseLog("??????Case,?????????Update");
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
        //
        pos.setShopID(2);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync?????????????????????");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p.compareTo(pos) == 0, "update?????????");
    }

    @Test
    public void test_c2_UpdateSync() throws Exception {
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);
        pos.setID(p.getID());

        Shared.caseLog("??????case???update???????????????????????????");
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync?????????????????????");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue((posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError), "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p.compareTo(pos) == 0, "update?????????");
    }

    @Test
    public void test_c3_UpdateSync() throws Exception {
        Shared.caseLog("??????case???update????????????null????????????null");
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        p.setPos_SN(null);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync?????????????????????");
    }

    @Test
    public void test_c4_UpdateSync() throws Exception {
        Shared.caseLog("??????case???update??????????????????");
        Pos pos = DataInput.getPOS();
        pos.setID(99999);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync?????????????????????");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);// update????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p.compareTo(pos) == 0, "update?????????");
    }

    @Test
    public void test_c5_UpdateSync() throws Exception {
        Shared.caseLog("??????case????????????????????????");
        Pos pos = DataInput.getPOS();
        pos.setID(0);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????????????????");
    }

    @Test
    public void test_d1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case1
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b.compareTo(pos) == 0, "?????????????????????????????????????????????");
    }

    @Test
    public void test_d2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????Case2
        Pos pos1 = new Pos();
        pos1.setID(0);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b == null, "???????????????");
    }

    @Test
    public void test_d3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
        Pos pos2 = new Pos();
        pos2.setID(null);
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(p == null, "???????????????????????????????????????");
    }

    @Test
    public void test_d4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID??????????????????????????????????????????
        Pos pos3 = new Pos();
        pos3.setID(-1);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos3);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b == null, "???????????????");
    }

    @Test
    public void test_e_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
        Assert.assertTrue(posList != null && posList.size() > 0, "?????????list????????????null");
        //
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        pos.setSql("where F_POS_SN = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
        Assert.assertTrue(posList != null && posList.size() > 0, "?????????list????????????null");

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync?????????????????????");

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = '%s' and F_ID = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");

        //??????Case???????????????????????????
        pos.setSql("where F_POS_SN = '%s' and F_ID = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
    }

    @Test
    public void test_f1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(p != null, "???????????????");
        //
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync?????????");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(p == null, "???????????????");
    }

    @Test
    public void test_f2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID????????????????????????
        Pos pos1 = new Pos();
        pos1.setID(0);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync?????????");
    }

    @Test
    public void test_f3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null
        Pos pos = new Pos();
        pos.setID(null);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync??????????????????????????????" + posPresenter.getLastErrorCode());
    }

    @Test
    public void test_g1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        Pos pos = DataInput.getPOS();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(c.compareTo(pos) == 0, "createAsync?????????,compareTo??????");
    }

    @Test
    public void test_g2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        Pos pos = DataInput.getPOS();

        //??????Case
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(c.compareTo(pos) == 0, "createAsync?????????,compareTo??????");

        //??????case?????????????????????????????????
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createAsync?????????????????????");
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync????????????????????????????????????" + posSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_h1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: ??????case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????????????????");
        //
        pos.setPos_SN("11111");
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update??????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update?????????????????????");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p.compareTo(pos) == 0, "update?????????");
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update??????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "Update?????????????????????");
    }

    @Test
    public void test_h3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???update??????????????????
        Pos pos = DataInput.getPOS();
        pos.setID(9999999);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p == null, "update?????????");
        //
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update??????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update????????????????????????????????????" + posSQLiteEvent.getLastErrorCode());
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p == null, "update?????????");
    }

    @Test
    public void test_i_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync?????????????????????");
        Assert.assertTrue(posSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync????????????????????????pos");
    }

    @Test
    public void test_j_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(p != null, "???????????????");
        //
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "deleteAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync?????????????????????");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(p == null, "???????????????");

        //??????case?????????????????????ID?????????????????????
        Pos pos1 = new Pos();
        pos1.setID(99999);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "deleteAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync?????????????????????");

        //??????case??????????????????ID???null????????????????????????
        Pos pos2 = DataInput.getPOS();
        pos2.setID(null);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos2, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "deleteAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync?????????????????????");
    }

    @Test
    public void test_k_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();

        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.retrieve1ObjectAsync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Async?????????????????????");
    }

    @Test
    public void test_l_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Pos> posList = (List<Pos>) DataInput.getPosList();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");

        //??????case?????????????????????????????????
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNAsync?????????????????????");

        //??????case???????????????????????????null??????????????????
        posList = (List<Pos>) DataInput.getPosList();
        for (Pos pos : posList) {
            pos.setPos_SN(null);
        }
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        posPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, posList, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
    }

    @Test
    public void test_m_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        List<Pos> posList = (List<Pos>) PosPresenterTest.DataInput.getPosList();
        List<BaseModel> liseBM = new ArrayList<BaseModel>();
        for (BaseModel pos : posList) {
            pos.setSyncType(BasePresenter.SYNC_Type_C);
            liseBM.add(pos);
        }

        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync?????????");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????!");

        //??????Case???????????????(??????????????????????????????????????????)
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync?????????");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????!");

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
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync?????????");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????!");
    }

    @Test
    public void test_n_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(posList != null && posList.size() > 0, "retrieveNObjectSync???????????????list????????????");
        //
        posPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync?????????????????????");
        //
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(posList == null || posList.size() == 0, "retrieveNObjectSync???????????????list????????????");
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createOrReplaceAsync???");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync?????????????????????");

        pos = (Pos) posSQLiteEvent.getBaseModel1();
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(pos.getShopID() == 2, "???????????????");

        //?????????????????????????????????????????????????????????
        pos.setShopID(1);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createOrReplaceAsync???");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync?????????????????????");

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(pos.getShopID() == 1, "???????????????");

        //????????????????????????
        Pos pos1 = DataInput.getPOS();
        pos1.setPos_SN("t123");
        pos1.setShopID(2);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createOrReplaceAsync???");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync?????????????????????");
        //
        pos1 = (Pos) posSQLiteEvent.getBaseModel1();
        Pos pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(pos2.getShopID() == 2 && pos2.getPos_SN().equals("t123"), "???????????????");

        //????????????????????????????????????
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync?????????");
        //
        Pos pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(pos3 == null, "???????????????");
    }

    private Pos createPos(Pos pos) {
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(p.compareTo(pos) == 0, "update?????????");

        return p;
    }

    /**
     * ????????????POS???????????????
     */
    @Test
    public void test_retrieveNPos() throws Exception {
        Shared.printTestMethodStartInfo();

//        PosPresenter posPresenter = GlobalController.getInstance().getPosPresenter();
        Integer total = posPresenter.retrieveCount();
        System.out.println("POS???????????????" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "???????????????");
    }

}