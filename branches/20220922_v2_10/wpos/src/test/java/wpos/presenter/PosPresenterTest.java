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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }

    }

    @Test
    public void test_a_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Pos> posList = DataInput.getPosList();
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");

        //异常case：重复插入（插入失败）
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createObjectSync失败！");

        //异常case：插入的非空字段为null（插入失败）
        posList = (List<Pos>) DataInput.getPosList();
        posList.get(0).setPos_SN(null);
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_b_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");

        //异常case：重复插入（插入失败）
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createObjectSync失败！");

        //异常case：插入的非空字段为null（插入失败）
        pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_c1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.caseLog("正常Case,插入再Update");
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        pos.setShopID(2);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p.compareTo(pos) == 0, "update失败！");
    }

    @Test
    public void test_c2_UpdateSync() throws Exception {
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);
        pos.setID(p.getID());

        Shared.caseLog("异常case：update的对象为原来的对象");
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue((posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError), "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p.compareTo(pos) == 0, "update失败！");
    }

    @Test
    public void test_c3_UpdateSync() throws Exception {
        Shared.caseLog("异常case：update不允许为null的数据为null");
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        p.setPos_SN(null);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
    }

    @Test
    public void test_c4_UpdateSync() throws Exception {
        Shared.caseLog("异常case：update不存在的数据");
        Pos pos = DataInput.getPOS();
        pos.setID(99999);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);// update时，如果输入对象不存在，会创建这个对象。如果对象存在，则会更新对象内容并替换对象
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p.compareTo(pos) == 0, "update失败！");
    }

    @Test
    public void test_c5_UpdateSync() throws Exception {
        Shared.caseLog("异常case：字段验证不通过");
        Pos pos = DataInput.getPOS();
        pos.setID(0);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync错误码不正确！");
    }

    @Test
    public void test_d1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b.compareTo(pos) == 0, "查找的对象与插入的对象不一致！");
    }

    @Test
    public void test_d2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常Case2
        Pos pos1 = new Pos();
        pos1.setID(0);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查找失败！");
    }

    @Test
    public void test_d3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Pos pos2 = new Pos();
        pos2.setID(null);
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(p == null, "查找失败！不应该有对象返回");
    }

    @Test
    public void test_d4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Pos pos3 = new Pos();
        pos3.setID(-1);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos3);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查找失败！");
    }

    @Test
    public void test_e_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(posList != null && posList.size() > 0, "查询的list不应该为null");
        //
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        pos.setSql("where F_POS_SN = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(posList != null && posList.size() > 0, "查询的list不应该为null");

        //异常Case：一个通配符多个值
        pos.setSql("where F_POS_SN = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确！");

        //异常Case：多个通配符一个值
        pos.setSql("where F_POS_SN = '%s' and F_ID = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");

        //异常Case：多个通配符多个值
        pos.setSql("where F_POS_SN = '%s' and F_ID = '%s'");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
    }

    @Test
    public void test_f1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(p != null, "查询失败！");
        //
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync失败！");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(p == null, "查询失败！");
    }

    @Test
    public void test_f2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        Pos pos1 = new Pos();
        pos1.setID(0);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_f3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Pos pos = new Pos();
        pos.setID(null);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败，错误码不正确：" + posPresenter.getLastErrorCode());
    }

    @Test
    public void test_g1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
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
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(pos) == 0, "createAsync失败！,compareTo错误");
    }

    @Test
    public void test_g2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        Pos pos = DataInput.getPOS();

        //正常Case
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
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(pos) == 0, "createAsync失败！,compareTo错误");

        //异常case：重复插入（插入失败）
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
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g3_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
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
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync错误码不正确！，错误码：" + posSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_h1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: 正常case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync错误码不正确！");
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
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        pos = (Pos) posSQLiteEvent.getBaseModel1();
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p.compareTo(pos) == 0, "update失败！");
    }

    @Test
    public void test_h2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：update不允许为null的数据为null
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
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "Update错误码不正确！");
    }

    @Test
    public void test_h3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：update不存在的数据
        Pos pos = DataInput.getPOS();
        pos.setID(9999999);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p == null, "update失败！");
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
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！，错误码：" + posSQLiteEvent.getLastErrorCode());
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p == null, "update失败！");
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
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(posSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync应该查询到所有的pos");
    }

    @Test
    public void test_j_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(p != null, "查询失败！");
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
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(p == null, "查询失败！");

        //异常case：删除不存在的ID（会出现异常）
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
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");

        //异常case：删除的对象ID为null（不会出现异常）
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
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync错误码不正确！");
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
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Async错误码不正确！");
    }

    @Test
    public void test_l_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
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
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");

        //异常case：重复插入（插入失败）
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
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNAsync错误码不正确！");

        //异常case：插入的非空字段为null（插入失败）
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
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_m_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
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
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常case：插入的非空字段为null
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
        Assert.assertTrue(posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");
    }

    @Test
    public void test_n_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(posList != null && posList.size() > 0, "retrieveNObjectSync查询出来的list必须有值");
        //
        posPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync错误码不正确！");
        //
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(posList == null || posList.size() == 0, "retrieveNObjectSync查询出来的list必须有值");
    }

    @Test
    public void test_o_createOrReplaceAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //存在的数据，删除后替换
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
            Assert.assertTrue(false, "createOrReplaceAsync！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync错误码不正确！");

        pos = (Pos) posSQLiteEvent.getBaseModel1();
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(pos.getShopID() == 2, "查询失败！");

        //替换回原来的数据，为了不影响后面的测试
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
            Assert.assertTrue(false, "createOrReplaceAsync！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync错误码不正确！");

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(pos.getShopID() == 1, "查询失败！");

        //创建不存在的数据
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
            Assert.assertTrue(false, "createOrReplaceAsync！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createOrReplaceAsync错误码不正确！");
        //
        pos1 = (Pos) posSQLiteEvent.getBaseModel1();
        Pos pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(pos2.getShopID() == 2 && pos2.getPos_SN().equals("t123"), "查询失败！");

        //创建和验证成功后删除数据
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync失败！");
        //
        Pos pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(pos3 == null, "查询失败！");
    }

    private Pos createPos(Pos pos) {
        pos = (Pos) posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(p.compareTo(pos) == 0, "update失败！");

        return p;
    }

    /**
     * 查询本地POS表的总条数
     */
    @Test
    public void test_retrieveNPos() throws Exception {
        Shared.printTestMethodStartInfo();

//        PosPresenter posPresenter = GlobalController.getInstance().getPosPresenter();
        Integer total = posPresenter.retrieveCount();
        System.out.println("POS表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}