package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Pos;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PosJUnitTest extends BaseHttpTestCase {
    private static List<Pos> posList;
    private static Integer conflictID;
    private static Pos pos = new Pos();
    private Pos pos1 = new Pos();
    private Pos pos2 = new Pos();
    private static long lID = 0l;

//    PosPresenter posPresenter = null;

    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosSQLiteBO posSQLiteBO;
    @Resource
    private PosHttpBO posHttpBO;
    @Resource
    private PosSQLiteEvent posSQLiteEvent;
    @Resource
    private PosHttpEvent posHttpEvent;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_PosJunitTest = 10000;

    @BeforeClass
    public void setUp(){
        super.setUp();
//        if (posPresenter == null) {
//            posPresenter = GlobalController.getInstance().getPosPresenter();
//        }
        posSQLiteEvent.setId(Event_ID_PosJunitTest);
        posHttpEvent.setId(Event_ID_PosJunitTest);
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        posSQLiteBO.setHttpEvent(posHttpEvent);
        posSQLiteBO.setSqLiteEvent(posSQLiteEvent);
        posHttpBO.setHttpEvent(posHttpEvent);
        posHttpBO.setSqLiteEvent(posSQLiteEvent);
        //
        logoutHttpEvent.setId(Event_ID_PosJunitTest);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        //
        staffLoginHttpEvent.setId(Event_ID_PosJunitTest);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);

        posLoginHttpEvent.setId(Event_ID_PosJunitTest);
        posSQLiteEvent.setTimeout(30);
        posSQLiteEvent.setEventProcessed(false);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
    }

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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PosJunitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        conflictID = posList.get(1).getID();
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createNSync返回的错误码不正确!，错误码：" + posPresenter.getLastErrorCode());
        //
        for (int i = 0; i < 5; i++) {
            Pos Pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(i));
            Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
            Assert.assertTrue(Pos.compareTo(posList.get(i)) == 0,"插入的PosList没有完全插入成功");
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
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList1);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"createNSync非空字段为空, 返回的错误码应该为OtherError");
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
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        conflictID = posList.get(1).getID();

        //异常Case:主键冲突
        List<Pos> PosList2 = new ArrayList<Pos>();
        for (int i = 0; i < 5; i++) {
            Pos Pos = DataInput.getPos();
            Pos.setID(conflictID);
            PosList2.add(Pos);
        }
        //将Pos的List插入到本地SQLite
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, PosList2);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"createNSync主键冲突, 返回的错误码应该为OtherError：" + posPresenter.getLastErrorCode());
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");
        //
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(pos.compareTo(pos) == 0,"插入Pos失败!");
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:非空字段为空
        //设置非空字段为null
        Pos pos = DataInput.getPos();
        pos.setPos_SN(null);
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"createSync非空字段为空, 返回的错误码应该为OtherError!");
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");
        //
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(pos.compareTo(pos) == 0,"插入Pos失败!");

        conflictID = pos.getID();

        //异常Case:主键冲突
        //设置ID为使用过的ID
        pos.setID(conflictID);
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"createSync非空字段为空, 返回的错误码应该为OtherError!");
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
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"updateSync返回的错误码不正确!");
        //
        Pos posR1 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(posR1.compareTo(pos) == 0,"Update失败!");
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case:update ID不存在的数据
        Pos Pos1 = DataInput.getPos();
        //设置SQLite不存在的ID
        Pos1.setID(9999999);
        //验证ID不存在SQLite中
        Pos Pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Pos1);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(Pos2 == null,"设置的ID存在于SQLite中!");
        //设置需要Update的字段的值
        Pos1.setPos_SN("this is the MyPosSN after second update " + (int) (Math.random() * 10000));
        //将修改后的Pos update到本地SQLite
        posPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Pos1);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"updateSync ID不存在的字段!错误码应该为NoError");
    }

    @Test
    public void test_d1_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Pos pos = DataInput.getPos();
        pos = createPos(pos);

        Pos posR1 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1 返回的错误码不正确");
        Assert.assertTrue(posR1 != null && posR1.compareTo(pos) == 0,"retrieve1失败!");
    }

    @Test
    public void test_d2_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case2:查找不存在的ID的数据
        Pos pos = DataInput.getPos();
        pos.setID(998999);
        Pos pos1 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1 查找不存在的ID的数据返回的错误码不正确!");
        Assert.assertTrue(pos1 == null,"retrieve1 不存在的ID的数据失败!");
    }

    @Test
    public void test_e1_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 根据条件查询
        Pos pos = DataInput.getPos();
        pos = createPos(pos);

        pos.setSql("where F_ID = %s");
        pos.setConditions(new String[]{String.valueOf(pos.getID())});
        List<Pos> conditionposList1 = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"根据条件retrieveN 返回的错误码不正确!");
        Assert.assertTrue(conditionposList1.size() == 1,"根据条件retrieveN 应该要有数据返回!");
    }

    @Test
    public void test_e2_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //RN之前删除所有，防止干扰
        posPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"deleteNObjectSync失败，错误码不正确：" + posPresenter.getLastErrorCode());

        List<Pos> posList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posList.add(DataInput.getPos());
        }

        createNPos(posList);

        //正常Case2: 查询所有
        List<Pos> allposList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieveN的错误码不正确!");
        Assert.assertTrue(allposList.size() == 10,"retrieveN 应该要有数据返回");
    }

    @Test
    public void test_e3_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case1: 根据条件查询, 条件中的字段不存在
        Pos pos = DataInput.getPos();
        pos.setSql("where F_A = %s");
        pos.setConditions(new String[]{"不存在"});
        List<Pos> conditionposList2 = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"retrieveN 根据条件查询, 条件中的字段不存在,返回的错误码不正确!");
    }

    @Test
    public void test_e4_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case2: 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素
        Pos pos = DataInput.getPos();
        pos.setSql(" where F_POS_SN = '%s'");
        pos.setConditions(new String[]{"BX", "SP"});
        List<Pos> conditionposList3 = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"retrieveN 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素,返回的错误码不正确!");
    }

    @Test
    public void test_e5_retrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case3: 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据
        Pos pos = DataInput.getPos();
        pos.setSql("where F_ID = %s and F_ShopID = %s");
        pos.setConditions(new String[]{"1"});
        List<Pos> conditionposList4 = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 返回的错误码不正确");
        Assert.assertTrue(conditionposList4 == null,"retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据");
    }

    @Test
    public void test_f1_deleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Pos
        Pos pos = DataInput.getPos();
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");
        //
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"deleteSync返回的错误码不正确!");
        //
        Pos Pos1 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        //
        Assert.assertTrue(Pos1 == null,"deleteSync失败!");
    }

    @Test
    public void test_f2_deleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case: 删除ID不存在的数据
        Pos pos2 = DataInput.getPos();
        //将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");

        Pos Pos2 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(Pos2 != null,"deleteSync失败!");
        Integer posR1ID = Pos2.getID();
        //
        pos2.setID(9993422);
        posPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"deleteSync返回的错误码不正确!");

        pos2.setID(posR1ID);
        Pos Pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(Pos3 != null,"deleteSync失败!");
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
            Assert.assertTrue(false,"createAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createAsync返回的错误码不正确!");
            pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posC);
            lID = pos.getID();
            Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
            Assert.assertTrue(pos.compareTo(posC) == 0,"插入Pos失败!");
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
            Assert.assertTrue(false,"createAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"createSync非空字段为空, 返回的错误码应该为OtherError!");
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
            Assert.assertTrue(false,"createAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"createSync非空字段为空, 返回的错误码应该为OtherError!");
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
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(pos.compareTo(pos) == 0,"插入Pos失败!");

        pos.setPos_SN("this is the MyPosSN after updateAsync " + (int) (Math.random() * 10000));
        pos.setID(pos.getID());
        //
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Pos pos1 = new Pos();
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue(false,"updateAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"updateSync返回的错误码不正确!");
            pos1 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
            Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
            Assert.assertTrue(pos1.compareTo(pos) == 0,"Update失败!");
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
            Assert.assertTrue(false,"updateAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField,"updateSync 非空字段为null!");
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
        pos2.setID(1000000);
        //验证ID不存在SQLite中
        Pos pos3 = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos2);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(pos3 == null,"设置的ID存在于SQLite中!");
        //设置需要Update的字段的值
        pos1.setPos_SN("this is the MyPosSN after second update " + (int) (Math.random() * 10000));
        //将修改后的Pos update到本地SQLite
        posSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
        posSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, pos1);
        //
        if (!waitForEventProcessed(posSQLiteEvent)) {
            Assert.assertTrue(false,"updateAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"updateSync ID不存在的字段!错误码不正确：" + posSQLiteEvent.getLastErrorCode());
        }
    }

    public void test_j_HttpRetrieveNSync () throws Exception {
        Shared.printTestMethodStartInfo();

        //登录pos1，staff1
        System.out.println("-------------------- pos1登录");
        if (!Shared.login(1, Shared.OP_Phone, Shared.PASSWORD_DEFAULT, BaseModel.EnumBoolean.EB_Yes.getIndex(), Shared.nbr_CompanySN, posLoginHttpBO, staffLoginHttpBO, BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
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
            Assert.assertTrue(false,"pos getToken超时!");
        }
        GlobalController.getInstance().setSessionID(sessionID);
        System.out.println("-------------------- pos1第一次调用create方法");
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue(false,"pos创建失败！");
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"pos创建超时！");
        }

        Assert.assertTrue(posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"请求创建Pos之后，服务器返回的错误码不正确");

        logOut();

        //登录pos2，staff2
        System.out.println("-------------------- pos2登录");
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //调用RN，进行同步，
        System.out.println("-------------------- pos2第一次调用RN方法");
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false,"pos创建失败！");
        }
        //
        lTimeOut = 50;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"pos创建超时！");
        }
        Assert.assertTrue(posSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"同步到本地，返回的错误码不正确,错误信息为：" + posSQLiteEvent.getLastErrorMessage());
        List<Pos> posList = (List<Pos>) posSQLiteEvent.getListMasterTable();
        Assert.assertTrue(posList.size() > 0,"ListMasterTable为空");
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
            Assert.assertTrue(false,"feedback失败！");
        }
        lTimeOut = 50;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"feedback请求超时！！");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    //全部下载
    public void test_k_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //本地SQLite增加一条数据。全部同步后不存在
        pos = DataInput.getPos();
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);

        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"CreateSync bm1测试失败,原因:返回错误码不正确!");

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        Pos p = new Pos();
        p.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        p.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        p.setShopID(BaseSQLiteBO.INVALID_ID);
        posSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!posHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue(false,"同步失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                posSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false,"请求服务器同步Pos超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Pos> posList = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSync测试失败,原因:返回错误码不正确!");

        List<Pos> list = (List<Pos>) posHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != posList.size()) {
            Assert.assertTrue(false,"全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
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
        if (!Shared.login(1,Shared.OP_Phone,Shared.PASSWORD_DEFAULT,BaseModel.EnumBoolean.EB_Yes.getIndex(),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex() )){
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        Pos p = DataInput.getPos();
        p.setID(0);
        p.setPasswordInPOS("000000");
        p.setPwdEncrypted("000000");
        //
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue(false,"pos创建失败！");
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"pos创建超时！");
        }
        p = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue(posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"请求创建Pos之后，服务器返回的错误码不正确");

        System.out.println("------------------------------ case1:pos表无数据，POS通过SN请求服务器获取身份。检查POS表中是否存在自己的数据");
        Constants.MyPosSN = p.getPos_SN();
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false,"本地查找POS明文密码超时！");
        }
        if (pos1 != null) {
            Assert.assertTrue(pos1.getPasswordInPOS() != null && !"".equals(pos1.getPasswordInPOS()),"当本地SQLite中的pos没有明文密码时，返回的POS应该为null");
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
                Assert.assertTrue(false,"在服务器查找POS明文密码超时！");
            }

            pos2 = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
            if (pos2 != null) {
                //...Pos doLoginAsync
                Assert.assertTrue(pos2.getPasswordInPOS() != null && !"".equals(pos2.getPasswordInPOS()),"在POS机本地没有明文密码的时候说明是第一次登录，请求服务器可以返回明文密码！");
            } else {
                Assert.assertTrue(false,"服务器无法识别该POS机");
            }
        }
    }

    private Pos createPos(Pos pos) {
        //正常Case
        //创建一个Pos 将Pos插入到本地SQLite
        posPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createSync返回的错误码不正确!");
        //
        pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
        Assert.assertTrue(pos.compareTo(pos) == 0,"插入Pos失败!");

        return pos;
    }

    private void createNPos(List<Pos> posList) {
        //正常Case
        //将Pos的List插入到本地SQLite
        posPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList);
        //
        Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"createNSync返回的错误码不正确!，错误码：" + posPresenter.getLastErrorCode());
        //
        for (int i = 0; i < 5; i++) {
            Pos Pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(i));
            Assert.assertTrue(posPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1返回的错误码不正确!");
            Assert.assertTrue(Pos.compareTo(posList.get(i)) == 0,"插入的PosList没有完全插入成功");
        }
    }
}
