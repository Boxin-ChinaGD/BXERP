package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.ConfigGeneralSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BXConfigGeneral;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.utils.Shared.*;

/**
 * Created by BOXIN on 2019/7/12.
 */

public class BXConfigGeneralPresenterTest extends BasePresenterTest{

    private static final int Event_ID_BXConfigGeneralPresenterTest = 10000;

    @BeforeClass
    public static void beforeClass() {
        printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        printTestClassEndInfo();
    }

//    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        EventBus.getDefault().register(this);

        if (bxConfigGeneralSQLiteEvent != null) {
            bxConfigGeneralSQLiteEvent.setId(Event_ID_BXConfigGeneralPresenterTest);
        }
    }

//    @Override
    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == Event_ID_BXConfigGeneralPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    public static class DataInput {
        private static BXConfigGeneral bxConfigGeneral = null;

        protected static final BXConfigGeneral getBXConfigGeneral() throws CloneNotSupportedException {
            bxConfigGeneral = new BXConfigGeneral();
            bxConfigGeneral.setName(BXConfigGeneral.Config_SmallSheetNumber);
            bxConfigGeneral.setValue(BXConfigGeneral.Config_SmallSheetNumber_Value);
            bxConfigGeneral.setSyncDatetime(new Date());

            return (BXConfigGeneral) bxConfigGeneral.clone();
        }

        protected static final List<?> getBXConfigGeneralList() throws CloneNotSupportedException {
            List<BaseModel> bxConfigGeneralList = new ArrayList<BaseModel>();
            for (int i = 0; i < 5; i++) {
                bxConfigGeneralList.add(getBXConfigGeneral());
            }
            return bxConfigGeneralList;
        }
    }

    @Test
    public void test_a_createSync() throws CloneNotSupportedException, ParseException {
        printTestMethodStartInfo();

        //正常case
        BXConfigGeneral cg = DataInput.getBXConfigGeneral();
        BXConfigGeneral cgCreated = (BXConfigGeneral) bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "BXCreateSync bm1测试失败,原因:返回错误码不正确!");
        cg.setIgnoreIDInComparision(true);
        Assert.assertTrue(cg.compareTo(cgCreated) == 0, "BXCreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //case：重复插入
        bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "BXCreateSync bm1测试失败,原因:返回错误码不正确!");

        //异常case：插入的非空字段为null（插入失败）
        BXConfigGeneral bm2 = DataInput.getBXConfigGeneral();
        bm2.setID(cgCreated.getID());
        bm2.setName(null);
        bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b_retrieve1Sync() throws CloneNotSupportedException, ParseException {
        printTestMethodStartInfo();

        //正常case
        BXConfigGeneral cg = DataInput.getBXConfigGeneral();
        cg.setID(1);
        //
        BXConfigGeneral cgRetrieve1 = (BXConfigGeneral) bxConfigGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1测试失败,原因:返回错误码不正确!");

        //异常case：根据ID查找，ID为null（找不到数据）
        cg.setID(null);
        cgRetrieve1 = (BXConfigGeneral) bxConfigGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1错误码不正确！");
        Assert.assertTrue(cgRetrieve1 == null, "retrieve1失败！");
    }

    @Test
    public void test_c_retrieveNSync() throws CloneNotSupportedException, ParseException {
        printTestMethodStartInfo();

        BXConfigGeneral cg = DataInput.getBXConfigGeneral();
        BXConfigGeneral cgCreated = (BXConfigGeneral) bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "BXCreateSync bm1测试失败,原因:返回错误码不正确!");
        cg.setIgnoreIDInComparision(true);
        Assert.assertTrue(cg.compareTo(cgCreated) == 0, "BXCreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //正常case
        List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(bxConfigGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");
        //
        BXConfigGeneral bxConfigGeneral = new BXConfigGeneral();
        bxConfigGeneral.setSql("where F_Value = %s");
        bxConfigGeneral.setConditions(new String[]{"10"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确!");
        Assert.assertTrue(bxConfigGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");

        //异常Case：一个通配符多个值
        bxConfigGeneral.setSql("where F_Value = %s");
        bxConfigGeneral.setConditions(new String[]{"10", "123"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确!");

        //异常Case：多个通配符一个值
        bxConfigGeneral.setSql("where F_Value = %s and F_Name = %s");
        bxConfigGeneral.setConditions(new String[]{"10"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确!");

        //Case：多个通配符多个值
        bxConfigGeneral.setSql("where F_Value = '%s' and F_Name = '%s'");
        bxConfigGeneral.setConditions(new String[]{"10", "SmallSheetNumber"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确!");
        Assert.assertTrue(bxConfigGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");
    }

    @Test
    public void test_d_createNSync() throws CloneNotSupportedException {
        printTestMethodStartInfo();

        //正常case
        List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        List<BXConfigGeneral> bxConfigGeneralListC = (List<BXConfigGeneral>) bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");
        for (int i = 0; i < bxConfigGeneralList.size(); i++) {
            Assert.assertTrue(bxConfigGeneralList.get(i).compareTo(bxConfigGeneralListC.get(i)) == 0, "retrieve1Sync失败！");

        }

        //case：重复插入
        bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");

        //异常case：插入的非空字段为null（插入失败）
        bxConfigGeneralList = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        bxConfigGeneralList.get(0).setValue(null);
        bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue(bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");
    }


    @Test
    public void test_f_refreshByServerDataAsyncC() throws CloneNotSupportedException, InterruptedException {
        printTestMethodStartInfo();

        //清空数据，以防数据污染
        bxConfigGeneralPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //正常Case
        List<BaseModel> bxConfigGeneralList = (List<BaseModel>) DataInput.getBXConfigGeneralList();
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList, bxConfigGeneralSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, (List<BaseModel>) bxConfigGeneralSQLiteEvent.getListMasterTable(), bxConfigGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常case：插入的非空字段为null
        List<BaseModel> cgList = new ArrayList<BaseModel>();
        List<BXConfigGeneral> bxConfigGeneralList1 = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        for (BXConfigGeneral bxConfigGeneral : bxConfigGeneralList1) {
            bxConfigGeneral.setValue(null);
            cgList.add(bxConfigGeneral);
        }
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList1, bxConfigGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(bxConfigGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");
    }

    /**
     * 查询本地T_BXConfigGeneral表的总条数
     */
    @Test
    public void test_retrieveNBXConfigGeneral() throws Exception {
        printTestMethodStartInfo();

//        BXConfigGeneralPresenter BXConfigGeneralPresenter = GlobalController.getInstance().getBXConfigGeneralPresenter();
        Integer total = bxConfigGeneralPresenter.retrieveCount();
        System.out.println("T_BXConfigGeneral表总条数：" + total);
        Assert.assertTrue(total > INVALID_CASE_TOTAL, "查询异常！");
    }

}
