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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }

    }

    @Test
    public void test_a_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Pos> posList = DataInput.getPosList();
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：重复插入（插入失败）
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        posList = (List<Pos>) DataInput.getPosList();
        posList.get(0).setPos_SN(null);
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        Assert.assertTrue("createNObjectSync失败！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：重复插入（插入失败）
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        pos = DataInput.getPOS();
        pos.setPos_SN(null);
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("正常Case,插入再Update");
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos.setShopID(2);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c2_UpdateSync() throws Exception {
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        caseLog("异常case：update的对象为原来的对象");
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", (posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError));
        Assert.assertTrue("update失败！", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c3_UpdateSync() throws Exception {
        caseLog("异常case：update不允许为null的数据为null");
        Pos pos = DataInput.getPOS();
        Pos p = createPos(pos);

        p.setPos_SN(null);
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateObjectSync错误码不正确！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c4_UpdateSync() throws Exception {
        caseLog("异常case：update不存在的数据");
        Pos pos = DataInput.getPOS();
        pos.setID(99999l);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("updateObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);// update时，如果输入对象不存在，会创建这个对象。如果对象存在，则会更新对象内容并替换对象
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", p.compareTo(pos) == 0);
    }

    @Test
    public void test_c5_UpdateSync() throws Exception {
        caseLog("异常case：字段验证不通过");
        Pos pos = DataInput.getPOS();
        pos.setID(0l);
        //
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找的对象与插入的对象不一致！", b.compareTo(pos) == 0);
    }

    @Test
    public void test_d2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常Case2
        Pos pos1 = new Pos();
        pos1.setID(0l);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查找失败！", b == null);
    }

    @Test
    public void test_d3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Pos pos2 = new Pos();
        pos2.setID(null);
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找失败！不应该有对象返回", p == null);
    }

    @Test
    public void test_d4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Pos pos3 = new Pos();
        pos3.setID(-1);
        Pos b = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos3);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查找失败！", b == null);
    }

    @Test
    public void test_e_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null", posList != null && posList.size() > 0);
        //
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos.setSql("where F_POS_SN = ?");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null", posList != null && posList.size() > 0);

        //异常Case：一个通配符多个值
        pos.setSql("where F_POS_SN = ?");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync错误码不正确！", posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：多个通配符一个值
        pos.setSql("where F_POS_SN = ? and F_ID = ?");
        pos.setConditions(new String[]{pos.getPos_SN()});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：多个通配符多个值
        pos.setSql("where F_POS_SN = ? and F_ID = ?");
        pos.setConditions(new String[]{pos.getPos_SN(), "T109179800011"});
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        Assert.assertTrue("retrieveNSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", p != null);
        //
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("deleteObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", p == null);
    }

    @Test
    public void test_f2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        Pos pos1 = new Pos();
        pos1.setID(0l);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("deleteObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_f3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Pos pos = new Pos();
        pos.setID(null);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("deleteObjectSync失败，错误码不正确：" + posPresenter.getLastErrorCode(), posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_g1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
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
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync失败！,compareTo错误", c.compareTo(pos) == 0);
    }

    @Test
    public void test_g2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        Pos pos = DataInput.getPOS();

        //正常Case
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
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos c = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync失败！,compareTo错误", c.compareTo(pos) == 0);

        //异常case：重复插入（插入失败）
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
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！，错误码：" + posSQLiteEvent.getLastErrorCode(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_h1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: 正常case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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
            Assert.assertTrue("Update超时", false);
        }
        Assert.assertTrue("Update错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", p.compareTo(pos) == 0);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update超时", false);
        }
        Assert.assertTrue("Update错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_h3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：update不存在的数据
        Pos pos = DataInput.getPOS();
        pos.setID(9999999l);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", p == null);
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
            Assert.assertTrue("Update超时", false);
        }
        Assert.assertTrue("Update错误码不正确！，错误码：" + posSQLiteEvent.getLastErrorCode(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", pos.compareTo(p) == 0);
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
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync应该查询到所有的pos", posSQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_j_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Pos pos = DataInput.getPOS();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", p != null);
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
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", p == null);

        //异常case：删除不存在的ID（不会出现异常）
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
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：删除的对象ID为null（不会出现异常）
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
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
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
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieve1Async错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_l_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：重复插入（插入失败）
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_m_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
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
        Assert.assertTrue("refreshByServerDataAsync超时！", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, liseBM, posSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

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
        lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", posSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_n_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list必须有值", posList != null && posList.size() > 0);
        //
        posPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list必须有值", posList == null || posList.size() == 0);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createOrReplaceAsync！", false);
        }
        Assert.assertTrue("createOrReplaceAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", pos.getShopID() == 2);

        //替换回原来的数据，为了不影响后面的测试
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
            Assert.assertTrue("createOrReplaceAsync！", false);
        }
        Assert.assertTrue("createOrReplaceAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", pos.getShopID() == 1);

        //创建不存在的数据
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
            Assert.assertTrue("createOrReplaceAsync！", false);
        }
        Assert.assertTrue("createOrReplaceAsync错误码不正确！", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", pos2.getShopID() == 2 && pos2.getPos_SN().equals("t123"));

        //创建和验证成功后删除数据
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("deleteObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1ObjectSync失败！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", pos3 == null);
    }

    private Pos createPos(Pos pos){
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", p.compareTo(pos) == 0);

        return p;
    }

    /**
     * 查询本地POS表的总条数
     */
    @Test
    public void test_retrieveNPos() throws Exception {
        Shared.printTestMethodStartInfo();

        PosPresenter posPresenter = GlobalController.getInstance().getPosPresenter();
        Integer total = posPresenter.retrieveCount();
        System.out.println("POS表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}