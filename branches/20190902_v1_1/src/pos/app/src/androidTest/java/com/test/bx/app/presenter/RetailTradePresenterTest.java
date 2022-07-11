package com.test.bx.app.presenter;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class RetailTradePresenterTest extends BaseAndroidTestCase {
    private static RetailTradePresenter retailTradePresenter;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;

    private static final int Event_ID_RetailTradePresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_RetailTradePresenterTest);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_RetailTradePresenterTest);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);

        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradePresenterTest) {
            System.out.println("#########################################################RetailTradePresenterTest onRetailTradeSQLiteEvent");
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError); // PresenterTest不会请求服务器，所以session为null，在Event中会将状态
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
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
        private static RetailTrade retailTrade = new RetailTrade();

        protected static final RetailTrade getRetailTrade() {
            Random ran = new Random();

            try {
                long tmpRowID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                retailTrade.setLocalSN((int) tmpRowID);
                retailTrade.setPos_ID(1);
                retailTrade.setSaleDatetime(new Date());
                retailTrade.setLogo("11");
                retailTrade.setStaffID(1);
                retailTrade.setPaymentType(1);
                retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
                retailTrade.setPaymentAccount("12");
                retailTrade.setRemark("11111");
                retailTrade.setSourceID(-1);
                retailTrade.setAmount(2222.2d);
                retailTrade.setAmountCash(retailTrade.getAmount());
                retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
                retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTrade.setSaleDatetime(new Date());
                retailTrade.setListSlave1(getRetailTradeCommodityList(tmpRowID));
                retailTrade.setDatetimeStart(new Date());
                retailTrade.setDatetimeEnd(new Date());
                retailTrade.setSn(retailTrade.generateRetailTradeSN(1));
                retailTrade.setShopID(2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return (RetailTrade) retailTrade.clone();
        }

        protected static final List<RetailTrade> getRetailTradeList() throws Exception {
            List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
            for (int i = 0; i < 10; i++) {
                retailTradeList.add(getRetailTrade());
            }
            return retailTradeList;
        }

        protected static List<RetailTradeCommodity> getRetailTradeCommodityList(long tradeID) {
            List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
            Random ran = new Random();
            for (int i = 0; i < 3; i++) {
                RetailTradeCommodity rtcInput = new RetailTradeCommodity();
                rtcInput.setTradeID(tradeID);
                rtcInput.setCommodityID(i + 1);
                rtcInput.setNO(ran.nextInt(999) + 1);
                rtcInput.setPriceOriginal(ran.nextDouble() + 1d);
                rtcInput.setDiscount(0.5d);
                rtcInput.setBarcodeID(1);
                rtcInput.setPriceReturn(6.66);
                rtcInput.setPriceVIPOriginal(3.22);
                rtcInput.setNOCanReturn(rtcInput.getNO());

                retailTradeCommodities.add(rtcInput);
            }

            Map<String, RetailTradeCommodity> map = new HashMap<>();
            for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodities) {
                map.put(String.valueOf(retailTradeCommodity.getCommodityID()), retailTradeCommodity);
            }

            return new ArrayList<RetailTradeCommodity>(map.values());
        }

        public static List<RetailTradeCommodity> getRetailTradeCommodityList() {
            List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
            Random ran = new Random();
            for (int i = 0; i < 3; i++) {
                RetailTradeCommodity rtcInput = new RetailTradeCommodity();
                rtcInput.setCommodityID(ran.nextInt(5) + 1);
                rtcInput.setNO(ran.nextInt(999) + 1);
                rtcInput.setPriceOriginal(ran.nextInt(999) + 1);
                rtcInput.setDiscount(0.5f);
                rtcInput.setBarcodeID(1);
                rtcInput.setPriceReturn(6.66);
                rtcInput.setNOCanReturn(rtcInput.getNO());

                retailTradeCommodities.add(rtcInput);
            }

            Map<String, RetailTradeCommodity> map = new HashMap<>();
            for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodities) {
                map.put(String.valueOf(retailTradeCommodity.getCommodityID()), retailTradeCommodity);
            }

            return new ArrayList<RetailTradeCommodity>(map.values());
        }
    }

    /**
     * 需要在测试前清空表时，请重命名函数名为test_a1_DeleteAllSync
     */
    @Test
    public void test_a1_DeleteAllSync() {
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        retailTradeCommodityPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
    }

    @Test
    public void test_a1_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<RetailTrade> list = (List<RetailTrade>) DataInput.getRetailTradeList();
        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);
        Assert.assertTrue("CreateNSync1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < list.size(); i++) {
            RetailTrade r = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list.get(i));
            r.setListSlave1(null);
            list.get(i).setListSlave1(null); // 因为CreateN方法不会插入从表信息
            //
            Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("create失败！", r.compareTo(list.get(i)) == 0);
        }

    }

    @Test
    public void test_a2_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：重复插入（插入失败）
        List<RetailTrade> list = (List<RetailTrade>) DataInput.getRetailTradeList();
        createRetailTradeList(list);

        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);
        Assert.assertTrue("createNObjectSync失败！", retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_a3_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        List<RetailTrade> list2 = (List<RetailTrade>) DataInput.getRetailTradeList();
        list2.get(0).setLogo(null);
        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list2);
        Assert.assertTrue("createNObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createObjectSync失败！", retailTradeCreate.compareTo(bmCreateSync) == 0);

        List<?> retailtradeList = retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        boolean isCreate = false;
        for (Object o : retailtradeList) {
            RetailTrade rt = (RetailTrade) o;
            if (retailTradeCreate.compareTo(rt) == 0) {
                isCreate = true;
                break;
            }
        }
        Assert.assertTrue("查询失败，查询临时零售单时没有查到刚创建的零售单", isCreate);
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case: 插入重复ID
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailtradeCreated = createRetailTrade(retailTrade);

        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreated);
        Assert.assertTrue("CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!", retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        // 插入null作为非空字段的值
        RetailTrade bm2 = DataInput.getRetailTrade();
        bm2.setLogo(null);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b4_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //模拟上传零售单时还未全部成功创建从表信息
        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        RetailTradePresenter.bInTestMode = true;
        RetailTrade retailtradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        RetailTradePresenter.bInTestMode = false;
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createObjectSync失败！", retailtradeCreate.compareTo(bmCreateSync) == 0);
        //
        List<?> retailtradeList = retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        for (Object o : retailtradeList) {
            RetailTrade rt = (RetailTrade) o;
            Assert.assertTrue("由于未创建完成从表，所以不应该查到临时零售单！", retailtradeCreate.compareTo(rt) != 0);
        }

        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreate);
        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreate);
        Assert.assertTrue("没有正确删除创建的零售单！", retailTradeR1 == null);
    }

    @Test
    public void test_c1_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync搜索到的数据数量应该>=0!", retailTradeList.size() >= 0);
        //根据条件查询 //...将来增加case：输入更多的查询条件
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ?");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>=0!", retailTradeList.size() >= 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
    }

    @Test
    public void test_c2_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
        //异常Case：一个通配符多个值
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ?");
        retailTrade.setConditions(new String[]{"0", "11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：多个通配符一个值
        retailTrade.setSql("where F_Remark = ? and F_Logo = ?");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("范围的List的size应该为0!", retailTradeList.size() == 0);

        //异常Case：多个通配符多个值
        retailTrade.setSql("where F_Remark = ? and F_Logo = ?");
        retailTrade.setConditions(new String[]{"11111", "11"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("范围的List的size应该为0!", retailTradeList.size() > 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
//
    }

    @Test
    public void test_c3_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        Date date1 = sdf.parse("2019/07/01 10:00:00");
        Date date2 = sdf.parse("2019/07/01 10:30:00");

        String workTimeStart = String.valueOf(date1.getTime());
        String workTimeEnd = String.valueOf(date2.getTime());
//
        RetailTrade rt = DataInput.getRetailTrade();
        rt.setPos_ID(1);
        rt.setStaffID(4);
        rt.setSaleDatetime(date1);
        retailTradePresenter.createObjectSync(-1, rt);
        Assert.assertTrue("createObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade retailTrade2 = DataInput.getRetailTrade();
        retailTrade2.setSaleDatetime(date1);
        retailTrade2.setDatetimeStart(date2);
        retailTrade2.setPos_ID(1);
        retailTrade2.setStaffID(4);
        createRetailTrade(retailTrade2);// 创建一个retailTrade

        RetailTrade r = new RetailTrade();
        r.setSql("where F_POS_ID = ? and F_StaffID = ? and F_SaleDatetime between ? and ?");
        r.setConditions(new String[]{"1", "4", workTimeStart, workTimeEnd});
        r.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, r);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>0!", retailTradeList.size() > 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
        deleteRetailTrade(retailTradeList);
    }

    @Test
    public void test_c4_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //根据条件查询 //...将来增加case：输入更多的查询条件
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ?");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>=0!", retailTradeList.size() >= 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
    }

    @Test
    public void test_c5_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setQueryKeyword("");
        retailTrade.setSourceID(1); //任意大于0的源单
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>=0!", retailTradeList.size() >= 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
    }


    @Test
    public void test_c6_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //根据条件查询 //...将来增加case：输入更多的查询条件
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ?");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>=0!", retailTradeList.size() >= 0);
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue("从表为空", retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0);
            }
        }
    }

    public void deleteRetailTrade(List<RetailTrade> list) {
        for (RetailTrade retailTrade : list) {
            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue("deleteObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("createObjectSync返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", c.compareTo(retailTrade) == 0);
    }

    @Test
    public void test_d2_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTrade.setID(null);
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1失败！", c == null);
    }

    @Test
    public void test_e1_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("createObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", c != null);
        //
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("deleteObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", c == null);
    }

    @Test
    public void test_e2_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        RetailTrade retailTrade1 = new RetailTrade();
        retailTrade1.setID(999999l);
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
        Assert.assertTrue("deleteObjectSync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list必须有值", retailTradeList != null && retailTradeList.size() > 0);
        //
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list不能有值", retailTradeList == null || retailTradeList.size() == 0);
    }

    @Test
    public void test_g1_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        RetailTrade retailTrade = DataInput.getRetailTrade();
//        retailTrade.setAmount(-10);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", retailTrade1.compareTo(retailTrade) == 0);
    }

    @Test
    public void test_g2_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：重复插入（插入失败）
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTrade.setID(createRetailTrade(retailTrade).getID());

        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g3_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTrade.setLogo(null);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<RetailTrade> retailTradeList = DataInput.getRetailTradeList();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
        retailTradePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (RetailTrade retailTrade : retailTradeList) {
            RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            //
            RetailTrade rtClone = (RetailTrade) retailTrade.clone();
            rtClone.setListSlave1(null);
            //
            Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("createN失败！", rt.compareTo(rtClone) == 0);
        }
    }

    @Test
    public void test_h2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：重复插入（插入失败）
        List<RetailTrade> retailTradeList = DataInput.getRetailTradeList();
        retailTradeList = createRetailTradeList(retailTradeList);
        for (RetailTrade retailTrade : retailTradeList) {
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
        }

        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
        retailTradePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：不允许为null的字段设为null
        List<RetailTrade> retailTradeList = DataInput.getRetailTradeList();
        for (RetailTrade retailTrade : retailTradeList) {
            retailTrade.setLogo(null);
        }
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
        retailTradePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_i_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //... 此测试会进行HTTP请求,不在PresenterTest的范畴内。在Junit相关测试就已有
        //Case: 正常case 1
//        RetailTrade retailTrade = DataInput.getRetailTrade();
//        createRetailTrade(retailTrade);
//        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeSQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
//                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
//            Assert.assertTrue("retrieveNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus() + "，错误码：" + retailTradeSQLiteEvent.getLastErrorCode() + "，错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage(), false);
//        }
//        Assert.assertTrue("retrieveNAsync会返回所有的数据", retailTradeSQLiteEvent.getListMasterTable().size() > 0);
//        Assert.assertTrue("retrieveNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError); //
//        //
//        RetailTrade retailTrade1 = new RetailTrade();
//        retailTrade1.setSql("where F_Remark = ?");
//        retailTrade1.setConditions(new String[]{retailTrade.getRemark()});
//        retailTrade1.setQueryKeyword("");
//        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade1, retailTradeSQLiteEvent);
//        lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
//            Assert.assertTrue("retrieveAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
//        }
//        Assert.assertTrue("retrieveAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", retailTradeSQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_i2_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case：一个通配符多个值
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ?");
        retailTrade.setConditions(new String[]{"0", "11111"});
        retailTrade.setQueryKeyword("");
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_i3_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //往sqlite中添加一个retailTrade，使本测试函数的测试结果不依赖于其他测试；
        RetailTrade retailTrade = DataInput.getRetailTrade();
//        retailTrade.setAmount(-10);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);


        //异常Case：多个通配符一个值
        retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = ? and F_Logo = ?");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("retrieveNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //正常Case：多个通配符多个值
        retailTrade.setSql("where F_Remark = ? and F_Logo = ?");
        retailTrade.setConditions(new String[]{"11111", "11"});
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        // 无session  retailTradeSQLiteEvent.getStatus() = EES_SQLite_Done
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("retrieveNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus() + "，错误码：" + retailTradeSQLiteEvent.getLastErrorCode() + "，错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage(), false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j1_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //正常的Case
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("DeleteAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Vip v = (Vip) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("DeleteAsync失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", v == null);
    }

    @Test
    public void test_j2_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //不存在的ID,不会抛出异常
        RetailTrade rt1 = DataInput.getRetailTrade();
        rt1.setID(Shared.SQLITE_ID_Infinite);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rt1, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("DeleteAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j3_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        RetailTrade rt2 = DataInput.getRetailTrade();
        rt2.setID(null);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rt2, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("DeleteAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("DeleteAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_k_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case：先创建、查找；再删除、查找
        RetailTrade bm = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue("createObjectSync返回的错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        RetailTrade rt = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue("retrieve1Async查询零售单超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("retrieve1Async查询零售单返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1Async查询插入的RetailTrade失败！", rt != null);
        //
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
        Assert.assertTrue("deleteObjectSync返回的错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
        retailTradePresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rt, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        rt = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue("retrieve1Async查询零售单超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("retrieve1Async查询零售单返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1Async查询插入的RetailTrade失败！", rt == null);
    }

    @Test
    public void test_k2_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        retailTradeSQLiteEvent.setBaseModel1(null);

        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade bm = createRetailTrade(retailTrade);

        bm.setID(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
        retailTradePresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        RetailTrade rt = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue("retrieve1Async查询零售单超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("retrieve1Async查询零售单返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1Async查询插入的RetailTrade失败！", rt == null);
    }

    @Test
    public void test_l_updateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> retailTradeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            retailTradeList.add(DataInput.getRetailTrade());
        }
        for (int i = 0; i < retailTradeList.size(); i++) {
            RetailTrade retailTrade = (RetailTrade) retailTradeList.get(i);
            retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            //
            Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);
        }

        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("updateNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("updateNAsync错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_m1_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: 正常case
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("createObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        retailTrade.setRemark("5555555555555555555");
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("Update错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！，错误码：" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", rt.compareTo(retailTrade) == 0);
    }

    @Test
    public void test_m2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：update不允许为null的数据为null
        RetailTrade rt = DataInput.getRetailTrade();
        RetailTrade retailTrade = createRetailTrade(rt);

        retailTrade.setPaymentAccount(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("Update错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_m3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：update不存在的数据
        RetailTrade retailTrade = DataInput.getRetailTrade();

        retailTrade.setID(88888l);
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", rt == null);
        //
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
        }
        System.out.println("错误码：" + retailTradeSQLiteEvent.getLastErrorCode());
        Assert.assertTrue("Update错误码不正确！错误码：" + retailTradeSQLiteEvent.getLastErrorCode(), retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！错误码：" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", rt == null);
    }

    @Test
    public void test_n_createMasterSlaveAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        RetailTrade rt = DataInput.getRetailTrade();
        //
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rt, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTrade retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("查询出来的RetailTrade与插入的不一致", retailTrade.compareTo(rt) == 0);
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            Assert.assertTrue("查询出来的retailTradeCommodity与插入的不一致", retailTradeCommodityList.get(i).compareTo((BaseModel) rt.getListSlave1().get(i)) == 0);
        }

        //异常case：重复创建一样的retailTrade
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：不允许为null的字段设置为null
        retailTrade = DataInput.getRetailTrade();
        retailTrade.setPaymentAccount(null);
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_o_createReplacerAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case：
        RetailTrade retailTradeOld = DataInput.getRetailTrade();
        //插入bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTrade retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("查询出来的RetailTrade与插入的不一致", retailTrade.compareTo(retailTradeOld) == 0);
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            Assert.assertTrue("查询出来的retailTradeCommodity与插入的不一致", retailTradeCommodityList.get(i).compareTo((BaseModel) retailTradeOld.getListSlave1().get(i)) == 0);
        }
        //调用createReplacerObjectAsync
        RetailTrade retailTradeNew = DataInput.getRetailTrade();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerAsync替换RetailTrade超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createReplacerAsync替换RetailTrade返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTrade retailTrade2 = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        //判断bmNew是否插入成功
        retailTrade2 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade2);
        Assert.assertTrue("查询出来的RetailTrade与插入的不一致", retailTrade2.compareTo(retailTradeNew) == 0);
        retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade2.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            Assert.assertTrue("查询出来的retailTradeCommodity与插入的不一致", retailTradeCommodityList.get(i).compareTo((BaseModel) retailTradeNew.getListSlave1().get(i)) == 0);
        }
        //判断bmOld是否删除成功
        RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld);
        Assert.assertTrue("查询零售单返回错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createReplacerAsync删除旧数据失败", retailTrade1 == null);

        //异常case：bmNew重复
        retailTradeOld = DataInput.getRetailTrade();
        //插入bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("查询出来的RetailTrade与插入的不一致", retailTrade.compareTo(retailTradeOld) == 0);
        retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            Assert.assertTrue("查询出来的retailTradeCommodity与插入的不一致", retailTradeCommodityList.get(i).compareTo((BaseModel) retailTradeOld.getListSlave1().get(i)) == 0);
        }
        //调用createReplacerObjectAsync
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerAsync替换RetailTrade超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createReplacerAsync替换RetailTrade返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：bmNew为null
        retailTradeOld = DataInput.getRetailTrade();
        //插入bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createMasterSlaveAsync创建主从表超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createMasterSlaveAsync创建主从表返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("查询出来的RetailTrade与插入的不一致", retailTrade.compareTo(retailTradeOld) == 0);
        retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            Assert.assertTrue("查询出来的retailTradeCommodity与插入的不一致", retailTradeCommodityList.get(i).compareTo((BaseModel) retailTradeOld.getListSlave1().get(i)) == 0);
        }
        //调用createReplacerObjectAsync
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerAsync替换RetailTrade超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createReplacerAsync替换RetailTrade返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：bmOld为null
        retailTradeNew = DataInput.getRetailTrade();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerAsync替换RetailTrade超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createReplacerAsync替换RetailTrade返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：bmOld不存在数据库中
        retailTradeNew = DataInput.getRetailTrade();
        retailTradeOld = DataInput.getRetailTrade();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerAsync替换RetailTrade超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createReplacerAsync替换RetailTrade返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_p_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<RetailTrade> retailTradeList = (List<RetailTrade>) DataInput.getRetailTradeList();
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsyncC超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsyncC返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<RetailTrade> retailTradeList1 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("SQLite中的数据量不正确", retailTradeList1.size() == retailTradeList.size());

        //异常case：插入的list为null
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsyncC超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsyncC返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<RetailTrade> retailTradeList2 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("SQLite中的数据量不正确", retailTradeList2.size() == 0);

        //异常case：重复插入相同的list
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsyncC超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsyncC返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<RetailTrade> retailTradeList3 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("SQLite中的数据量不正确", retailTradeList3.size() == retailTradeList.size());
    }

    @Test
    public void test_q_createReplacerNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<RetailTrade> retailTradeOldList = (List<RetailTrade>) DataInput.getRetailTradeList();
        List<RetailTrade> retailTradeOldList2 = new ArrayList<RetailTrade>();
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        RetailTrade retailTradeOld = new RetailTrade();
        //插入主从表
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            retailTradeOld = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList.get(i));
            Assert.assertTrue("retailTrade craeteObjectSync返回的错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            retailTradeOldList2.add(retailTradeOld);
        }
        //
        List<RetailTrade> retailTradeNewList = (List<RetailTrade>) DataInput.getRetailTradeList();
        for (int i = 0; i < retailTradeOldList2.size(); i++) {
            retailTradeNewList.get(i).setLocalSN(retailTradeOldList2.get(i).getLocalSN());
            retailTradeNewList.get(i).setPos_ID(retailTradeOldList2.get(i).getPos_ID());
            retailTradeNewList.get(i).setSaleDatetime(retailTradeOldList2.get(i).getSaleDatetime());
        }
        //
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerNAsync_Done);
        retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList2, retailTradeNewList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerNAsync超时", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createReplacerNAsync返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < retailTradeOldList2.size(); i++) {
            RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList2.get(i));
            Assert.assertTrue("retrieve1Sync返回的错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("retrieve1Sync查出来的数据应该为null", retailTrade == null);
        }

        //异常case：bmNewList为null
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerNAsync_Done);
        retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList2, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerNAsync超时", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createReplacerNAsync返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createReplacerNAsync newList为null时，不做任何操作", retailTradeSQLiteEvent.getListMasterTable() == null);

        //异常case：bmOldList为null
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerNAsync_Done);
        retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeNewList, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createReplacerNAsync超时", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createReplacerNAsync返回的错误码不正确", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    private RetailTrade createRetailTrade(RetailTrade retailTrade) {
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", rt.compareTo(retailTrade) == 0);

        return rt;
    }

    private List<RetailTrade> createRetailTradeList(List<RetailTrade> list) {
        //正常case
        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);
        Assert.assertTrue("CreateNSync1测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<RetailTrade> retailTradeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RetailTrade r = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list.get(i));
            r.setListSlave1(null);
            list.get(i).setListSlave1(null); // 因为CreateN方法不会插入从表信息
            //
            Assert.assertTrue("retrieve1ObjectSync错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("create失败！", r.compareTo(list.get(i)) == 0);

            retailTradeList.add(r);
        }

        return retailTradeList;
    }

    /**
     * 查询本地retailTrade表的总条数
     */
    @Test
    public void test_retrieveNRetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        Integer total = retailTradePresenter.retrieveCount();
        System.out.println("retailTrade表总条数：" + total);
        Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
