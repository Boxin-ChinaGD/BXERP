package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PosSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.presenter.PosPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PosJUnitTest extends BaseHttpAndroidTestCase {
    private static List<Pos> posList;
    private static Long conflictID;
    private static Pos pos = new Pos();
    private Pos pos1 = new Pos();
    private Pos pos2 = new Pos();
    private static long lID = 0l;

    PosPresenter presenter = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosSQLiteBO posSQLiteBO = null;
    private static PosHttpBO posHttpBO = null;

    private static PosSQLiteEvent posSQLiteEvent = null;
    private static PosHttpEvent posHttpEvent = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    //
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int Event_ID_PosJunitTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getPosPresenter();
        }
        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(Event_ID_PosJunitTest);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(Event_ID_PosJunitTest);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_PosJunitTest);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_PosJunitTest);
        }
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_PosJunitTest);
        }
        if (posSQLiteBO == null) {
            posSQLiteBO = new PosSQLiteBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (posHttpBO == null) {
            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }

        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        posSQLiteEvent.setTimeout(30);
        posSQLiteEvent.setEventProcessed(false);
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

    public static class DataInput {
        private static Pos posInput = null;

        public static final Pos getPos() throws CloneNotSupportedException {
            posInput = new Pos();
            posInput.setStatus(0);
            posInput.setShopID(1);
            posInput.setPos_SN("SN" + System.currentTimeMillis() % 1000000);
            posInput.setReturnObject(1);
            posInput.setCompanySN(Constants.MyCompanySN);
            posInput.setSalt(getFakedSalt());
            posInput.setPasswordInPOS("000000");

            return posInput;
        }
    }

    /**
     * company的key值目前需要32个数字或大写字母的组合，使用UUID进行生成
     */
    public static String getFakedSalt() {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().replace("-", "");
        return key.toUpperCase();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }

        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            pos1 = (Pos) event.getBaseModel1();

        }

        //从服务器获取的Pos信息，存到本地SQLite。
        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            pos2 = (Pos) event.getBaseModel1();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    @Test
    public void test_a1_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos的List
        posList = new ArrayList<Pos>();
        for (int i = 0; i < 5; i++) {
            Pos Pos = DataInput.getPos();
            posList.add(Pos);
        }
        //将Pos的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        conflictID = posList.get(1).getID();
        //
        Assert.assertTrue("createNSync返回的错误码不正确!，错误码：" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            Pos Pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的PosList没有完全插入成功", Pos.compareTo(posList.get(i)) == 0);
        }
    }

    @Test
    public void test_a2_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:非空字段为空
        List<Pos> posList1 = new ArrayList<Pos>();
        for (int i = 0; i < 5; i++) {
            Pos pos = DataInput.getPos();
            pos.setPos_SN(null);
            posList1.add(pos);
        }
        //将Pos的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList1);
        //
        Assert.assertTrue("createNSync非空字段为空, 返回的错误码应该为OtherError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_a3_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //创建一个Pos的List
        posList = new ArrayList<Pos>();
        for (int i = 0; i < 5; i++) {
            Pos Pos = DataInput.getPos();
            posList.add(Pos);
        }
        //将Pos的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        conflictID = posList.get(1).getID();

        //异常Case:主键冲突
        List<Pos> PosList2 = new ArrayList<Pos>();
        for (int i = 0; i < 5; i++) {
            Pos Pos = DataInput.getPos();
            Pos.setID(conflictID);
            PosList2.add(Pos);
        }
        //将Pos的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, PosList2);
        //
        Assert.assertTrue("createNSync主键冲突, 返回的错误码应该为OtherError：" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Pos失败!", pos.compareTo(pos) == 0);
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:非空字段为空
        //设置非空字段为null
        Pos pos = DataInput.getPos();
        pos.setPos_SN(null);
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Pos失败!", pos.compareTo(pos) == 0);

        conflictID = pos.getID();

        //异常Case:主键冲突
        //设置ID为使用过的ID
        pos.setID(conflictID);
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_c1_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //修改属性的值
        Pos pos = DataInput.getPos();
        pos.setPos_SN("修改测试");
        pos.setID(createPos(pos).getID());
        //将修改后的comm Update到本地SQLite
        presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("updateSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos posR1 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update失败!", posR1.compareTo(pos) == 0);
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case:update ID不存在的数据
        Pos Pos1 = DataInput.getPos();
        //设置SQLite不存在的ID
        Pos1.setID(9999999l);
        //验证ID不存在SQLite中
        Pos Pos2 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Pos1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", Pos2 == null);
        //设置需要Update的字段的值
        Pos1.setPos_SN("this is the MyPosSN after second update " + (int) (Math.random() * 10000));
        //将修改后的Pos update到本地SQLite
        presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Pos1);
        //
        Assert.assertTrue("updateSync ID不存在的字段!错误码应该为NoError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d1_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Pos pos = DataInput.getPos();
        pos = createPos(pos);

        Pos posR1 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("retrieve1 返回的错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1失败!", posR1 != null && posR1.compareTo(pos) == 0);
    }

    @Test
    public void test_d2_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case2:查找不存在的ID的数据
        Pos pos = DataInput.getPos();
        pos.setID(998999l);
        Pos pos1 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("retrieve1 查找不存在的ID的数据返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1 不存在的ID的数据失败!", pos1 == null);
    }

    @Test
    public void test_e1_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 根据条件查询
        Pos pos = DataInput.getPos();
        pos = createPos(pos);

        pos.setSql("where F_ID = ?");
        pos.setConditions(new String[]{String.valueOf(pos.getID())});
        List<Pos> conditionposList1 = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", conditionposList1.size() == 1);
    }

    @Test
    public void test_e2_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //RN之前删除所有，防止干扰
        presenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync失败，错误码不正确：" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Pos> posList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posList.add(DataInput.getPos());
        }

        createNPos(posList);

        //正常Case2: 查询所有
        List<Pos> allposList = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        Assert.assertTrue("retrieveN的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 应该要有数据返回", allposList.size() == 10);
    }

    @Test
    public void test_e3_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case1: 根据条件查询, 条件中的字段不存在
        Pos pos = DataInput.getPos();
        pos.setSql("where F_A = ?");
        pos.setConditions(new String[]{"不存在"});
        List<Pos> conditionposList2 = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件中的字段不存在,返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_e4_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case2: 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素
        Pos pos = DataInput.getPos();
        pos.setSql(" where F_POS_SN = ?");
        pos.setConditions(new String[]{"BX", "SP"});
        List<Pos> conditionposList3 = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素,返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_e5_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case3: 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据
        Pos pos = DataInput.getPos();
        pos.setSql("where F_ID = ? and F_ShopID = ?");
        pos.setConditions(new String[]{"1"});
        List<Pos> conditionposList4 = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 返回的错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据", conditionposList4.size() == 0);
    }

    @Test
    public void test_f1_deleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("deleteSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos Pos1 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("deleteSync失败!", Pos1 == null);
    }

    @Test
    public void test_f2_deleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case: 删除ID不存在的数据
        Pos pos2 = DataInput.getPos();
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Pos Pos2 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("deleteSync失败!", Pos2 != null);
        Long posR1ID = Pos2.getID();
        //
        pos2.setID(9993422l);
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("deleteSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        pos2.setID(posR1ID);
        Pos Pos3 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("deleteSync失败!", Pos3 != null);
    }

    @Test
    public void test_h1_createAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos posC = DataInput.getPos();
        //
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, posC);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createAsync返回的错误码不正确!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posC);
            lID = pos.getID();
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入Pos失败!", pos.compareTo(posC) == 0);
        }

    }

    @Test
    public void test_h2_createAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:非空字段为空
        posSQLiteEvent.setEventProcessed(false);
        //设置非空字段为null
        pos.setPos_SN(null);
        //
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        }
    }

    @Test
    public void test_h3_createAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Pos pos = DataInput.getPos();
        Pos posCreated = createPos(pos);
        conflictID = posCreated.getID();

        //异常Case:主键冲突
        posSQLiteEvent.setEventProcessed(false);
        //设置ID为使用过的ID
        pos.setID(conflictID);
        //将Pos插入到本地SQLite
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
        posSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_i1_updateAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        System.out.println("-------------------------------------Update1");
        //
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Pos失败!", pos.compareTo(pos) == 0);

        pos.setPos_SN("this is the MyPosSN after updateAsync " + (int) (Math.random() * 10000));
        pos.setID(pos.getID());
        //
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Pos pos1 = new Pos();
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("updateAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("updateSync返回的错误码不正确!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            pos1 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("Update失败!", pos1.compareTo(pos) == 0);
        }
    }

    @Test
    public void test_i2_updateAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case:update非空字段为null
        System.out.println("-------------------------------------Update2");
        Pos pos = DataInput.getPos();

        posSQLiteEvent.setEventProcessed(false);
        //修改属性的值
        pos.setPos_SN(null);
        //将修改后的Pos Update到本地SQLite
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("updateAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("updateSync 非空字段为null!", posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        }
    }

    @Test
    public void test_i3_updateAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();


        //正常Case:update ID不存在的数据
        System.out.println("-------------------------------------Update3");
        Pos pos1 = DataInput.getPos();
        pos1 = createPos(pos1);

        posSQLiteEvent.setEventProcessed(false);
        Pos pos2 = DataInput.getPos();
        //设置SQLite不存在的ID
        pos2.setID(1000000l);
        //验证ID不存在SQLite中
        Pos pos3 = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", pos3 == null);
        //设置需要Update的字段的值
        pos1.setPos_SN("this is the MyPosSN after second update " + (int) (Math.random() * 10000));
        //将修改后的Pos update到本地SQLite
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue("updateAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("updateSync ID不存在的字段!错误码不正确：" + posSQLiteEvent.getLastErrorCode(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
    }

    public void test_j_HttpRetrieveNSync () throws Exception {
        Shared.printTestMethodStartInfo();

        //登录pos1，staff1
        System.out.println("-------------------- pos1登录");
        if (!Shared.login(1l, Shared.OP_Phone, Shared.PASSWORD_DEFAULT, BaseModel.EnumBoolean.EB_Yes.getIndex(), Shared.nbr_CompanySN, posLoginHttpBO, staffLoginHttpBO, BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //因为POSgetToken后会将这里的session给清空掉。所以需要先保留登录的sessionID
        String sessionID = GlobalController.getInstance().getSessionID();
        //模拟网页创建pos。
        Pos p = DataInput.getPos();
        p.setID(0);
        p.setPasswordInPOS("000000");
        p.setPwdEncrypted("000000");
        System.out.println("-------------------- pos1调用getToken");
        posLoginHttpBO.getHttpEvent().setPwdEncrypted(null);
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.posGetToken(p);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0 && posLoginHttpBO.getHttpEvent().getPwdEncrypted() == null) {
            Thread.sleep(1000);
        }
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos getToken超时!", false);
        }
        GlobalController.getInstance().setSessionID(sessionID);
        System.out.println("-------------------- pos1第一次调用create方法");
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue("pos创建失败！", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos创建超时！", false);
        }

        Assert.assertTrue("请求创建Pos之后，服务器返回的错误码不正确", posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        logOut();

        //登录pos2，staff2
        System.out.println("-------------------- pos2登录");
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //调用RN，进行同步，
        System.out.println("-------------------- pos2第一次调用RN方法");
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("pos创建失败！", false);
        }
        //
        lTimeOut = 50;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("pos创建超时！", false);
        }
        Assert.assertTrue("同步到本地，返回的错误码不正确,错误信息为：" + posSQLiteEvent.getLastErrorMessage(), posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        List<Pos> posList = (List<Pos>) posSQLiteEvent.getListMasterTable();
        Assert.assertTrue("ListMasterTable为空", posList.size() > 0);
        System.out.println("第一次RN后同步了的posList为：" + posList);
        String posIDs = "";
        for (int i = 0; i < posList.size(); i++) {
            posIDs = posIDs + "," + posList.get(i).getID();
        }
        posIDs = posIDs.substring(1, posIDs.length());
        System.out.println("把posList分割后得到的BrandIDs = " + posIDs);

        System.out.println("-------------------- pos2第一次调用feedback方法");
        //feedback告诉服务器已经同步
        if (!posHttpBO.feedback(posIDs)) {
            Assert.assertTrue("feedback失败！", false);
        }
        lTimeOut = 50;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("feedback请求超时！！", false);
        }
    }

    //全部下载
    public void test_k_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //本地SQLite增加一条数据。全部同步后不存在
        pos = DataInput.getPos();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        Pos p = new Pos();
        p.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        p.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        p.setShopID(BaseSQLiteBO.INVALID_ID);
        posSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!posHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue("同步失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("请求服务器同步Pos超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Pos> posList = (List<Pos>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Pos> list = (List<Pos>) posHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != posList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }

    /**
     * 查找本地的SQLite看是否有pos的明文密码，如果没有则发送请求到nbr，否则直接登录
     *
     * @throws InterruptedException
     */
    @Test
    public void test_l_retrieve1BySN() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("------------------------------ 创建一个POS机 ------------------------------");
        if (!Shared.login(1l,Shared.OP_Phone,Shared.PASSWORD_DEFAULT,BaseModel.EnumBoolean.EB_Yes.getIndex(),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex() )){
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        Pos p = DataInput.getPos();
        p.setID(0);
        p.setPasswordInPOS("000000");
        p.setPwdEncrypted("000000");
        //
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue("pos创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos创建超时！", false);
        }
        p = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("请求创建Pos之后，服务器返回的错误码不正确", posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        System.out.println("------------------------------ case1:pos表无数据，POS通过SN请求服务器获取身份。检查POS表中是否存在自己的数据");
        Constants.MyPosSN = p.getPos_SN();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("本地查找POS明文密码超时！", false);
        }
        if (pos1 != null) {
            Assert.assertTrue("当本地SQLite中的pos没有明文密码时，返回的POS应该为null", pos1.getPasswordInPOS() != null && !"".equals(pos1.getPasswordInPOS()));
        } else {
            //没有Pos的身份证（明文密码），需要请求服务器，得到该pos机的身份证
            posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);

            Pos pos = new Pos();
            pos.setCompanySN("668866");
            posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, pos);
            lTimeOut = 60;
            while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.assertTrue("在服务器查找POS明文密码超时！", false);
            }

            pos2 = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
            if (pos2 != null) {
                //...Pos doLoginAsync
                Assert.assertTrue("在POS机本地没有明文密码的时候说明是第一次登录，请求服务器可以返回明文密码！", pos2.getPasswordInPOS() != null && !"".equals(pos2.getPasswordInPOS()));
            } else {
                Assert.assertTrue("服务器无法识别该POS机", false);
            }
        }
    }

    private Pos createPos(Pos pos) {
        //正常Case
        //创建一个Pos 将Pos插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Pos失败!", pos.compareTo(pos) == 0);

        return pos;
    }

    private void createNPos(List<Pos> posList) {
        //正常Case
        //将Pos的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        //
        Assert.assertTrue("createNSync返回的错误码不正确!，错误码：" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            Pos Pos = (Pos) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的PosList没有完全插入成功", Pos.compareTo(posList.get(i)) == 0);
        }
    }

}
