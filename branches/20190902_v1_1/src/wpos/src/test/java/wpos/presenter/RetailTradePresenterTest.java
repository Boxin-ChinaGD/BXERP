package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.RetailTradeHttpBO;
import wpos.event.BaseEvent;
import wpos.event.RetailTradeHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.Shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static wpos.utils.Shared.UNIT_TEST_TimeOut;


public class RetailTradePresenterTest extends BasePresenterTest {
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradePresenter staticRetailTradePresenter = null;

    private static final int Event_ID_RetailTradePresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();
        staticRetailTradePresenter = (RetailTradePresenter) applicationContext.getBean("retailTradePresenter");
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_RetailTradePresenterTest);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_RetailTradePresenterTest);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);

        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradePresenterTest) {
            System.out.println("#########################################################RetailTradePresenterTest onRetailTradeSQLiteEvent");
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError); // PresenterTest??????????????????????????????session???null??????Event???????????????
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
                long tmpRowID = staticRetailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime,RetailTrade.class);
                Assert.assertTrue(staticRetailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "?????????????????????");
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
                rtcInput.setCommodityName("xxxxxxxxxxxxxx");
                rtcInput.setNO(ran.nextInt(999) + 1);
                rtcInput.setPriceOriginal(ran.nextDouble() + 1d);
                rtcInput.setDiscount(0.5d);
                rtcInput.setBarcodeID(1);
                rtcInput.setPriceReturn(6.66);
                rtcInput.setPriceVIPOriginal(3.22);
                rtcInput.setNOCanReturn(rtcInput.getNO());

                retailTradeCommodities.add(rtcInput);
            }

            Map<String, RetailTradeCommodity> map = new HashMap<String, RetailTradeCommodity>();
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

            Map<String, RetailTradeCommodity> map = new HashMap<String, RetailTradeCommodity>();
            for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodities) {
                map.put(String.valueOf(retailTradeCommodity.getCommodityID()), retailTradeCommodity);
            }

            return new ArrayList<RetailTradeCommodity>(map.values());
        }
    }

    /**
     * ?????????????????????????????????????????????????????????test_a1_DeleteAllSync
     */
    @Test
    public void test_a1_DeleteAllSync() {
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        retailTradeCommodityPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
    }

    @Test
    public void test_a1_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<RetailTrade> list = (List<RetailTrade>) DataInput.getRetailTradeList();
        List<?> rtCreateList = retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1????????????,??????:????????????????????????!");
        //
        for (int i = 0; i < rtCreateList.size(); i++) {
            RetailTrade r = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) rtCreateList.get(i));
            r.setListSlave1(null);
            ((BaseModel) rtCreateList.get(i)).setListSlave1(null); // ??????CreateN??????????????????????????????
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
            Assert.assertTrue(r.compareTo((BaseModel) rtCreateList.get(i)) == 0, "create?????????");
        }

    }

    @Test
    public void test_a2_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????????????????
        List<RetailTrade> list = (List<RetailTrade>) DataInput.getRetailTradeList();
        List<RetailTrade> retailTradeCreateList = createRetailTradeList(list);

        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreateList);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
    }

    @Test
    public void test_a3_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        List<RetailTrade> list2 = (List<RetailTrade>) DataInput.getRetailTradeList();
        list2.get(0).setLogo(null);
        retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list2);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNObjectSync?????????");
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case
        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        RetailTrade rtCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
        //
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(retailTradeCreate.compareTo(rtCreate) == 0, "createObjectSync?????????");

        List<?> retailtradeList = retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        boolean isCreate = false;
        for (Object o : retailtradeList) {
            RetailTrade rt = (RetailTrade) o;
            if (retailTradeCreate.compareTo(rt) == 0) {
                isCreate = true;
                break;
            }
        }
        Assert.assertTrue(isCreate, "????????????????????????????????????????????????????????????????????????");
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case: ????????????ID
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailtradeCreated = createRetailTrade(retailTrade);

        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreated);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!");
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        // ??????null????????????????????????
        RetailTrade bm2 = DataInput.getRetailTrade();
        bm2.setLogo(null);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "CreateSync bm2????????????,??????:???????????????????????????EC_NoError!");
    }


    @Test
    public void test_b4_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //????????????????????????????????????????????????????????????
        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        RetailTradePresenter.bInTestMode = true;
        RetailTrade retailtradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        RetailTradePresenter.bInTestMode = false;
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
        retailtradeCreate.setIgnoreIDInComparision(true);
        Assert.assertTrue(retailtradeCreate.compareTo(bmCreateSync) == 0, "createObjectSync?????????");
        //
        List<?> retailtradeList = retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        for (Object o : retailtradeList) {
            RetailTrade rt = (RetailTrade) o;
            Assert.assertTrue(retailtradeCreate.compareTo(rt) != 0, "?????????????????????????????????????????????????????????????????????");
        }

        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreate);
        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeCreate);
        Assert.assertTrue(retailTradeR1 == null, "???????????????????????????????????????");
    }

    @Test
    public void test_c1_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????case
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() >= 0, "RetrieveNSync??????????????????????????????>=0!");
        //?????????????????? //...????????????case??????????????????????????????
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() >= 0, "????????????RetrieveNSync??????????????????????????????>=0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
            }
        }
    }

    @Test
    public void test_c2_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
        //????????????????????????,??????????????????
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s");
        retailTrade.setConditions(new String[]{"0", "11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");

        //??????Case???????????????????????????
        retailTrade.setSql("where F_Remark = %s and F_Logo = %s");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList == null, "?????????List???size?????????0!");

        //??????Case???????????????????????????
        retailTrade.setSql("where F_Remark = %s and F_Logo = %s");
        retailTrade.setConditions(new String[]{"11111", "11"});
        retailTrade.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() > 0, "?????????List???size?????????0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
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
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");

        RetailTrade retailTrade2 = DataInput.getRetailTrade();
        retailTrade2.setSaleDatetime(date1);
        retailTrade2.setDatetimeStart(date2);
        retailTrade2.setPos_ID(1);
        retailTrade2.setStaffID(4);
        createRetailTrade(retailTrade2);// ????????????retailTrade

        RetailTrade r = new RetailTrade();
        r.setSql("where F_POS_ID = %s and F_StaffID = %s and F_SaleDatetime between %s and %s");
        r.setConditions(new String[]{"1", "4", workTimeStart, workTimeEnd});
        r.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, r);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() > 0, "????????????RetrieveNSync??????????????????????????????>0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
            }
        }
        deleteRetailTrade(retailTradeList);
    }

    @Test
    public void test_c4_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //?????????????????? //...????????????case??????????????????????????????
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() >= 0, "????????????RetrieveNSync??????????????????????????????>=0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
            }
        }
    }

    @Test
    public void test_c5_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setQueryKeyword("");
        retailTrade.setSourceID(1); //????????????0?????????
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() >= 0, "????????????RetrieveNSync??????????????????????????????>=0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
            }
        }
    }


    @Test
    public void test_c6_RetrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //?????????????????? //...????????????case??????????????????????????????
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s");
        retailTrade.setConditions(new String[]{"11111"});
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        Assert.assertTrue(retailTradeList.size() >= 0, "????????????RetrieveNSync??????????????????????????????>=0!");
        for (int i = 0; i < retailTradeList.size(); i++) {
            if (retailTradeList.get(i).getStatus() != RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                Assert.assertTrue(retailTradeList.get(i).getListSlave1() != null && retailTradeList.get(i).getListSlave1().size() > 0, "????????????");
            }
        }
    }

    public void deleteRetailTrade(List<RetailTrade> list) {
        for (RetailTrade retailTrade : list) {
            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync?????????????????????");
        }
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case1
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade rtCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync????????????????????????!");
        //
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(c.compareTo(rtCreate) == 0, "update?????????");
    }

    @Test
    public void test_d2_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTrade.setID(null);
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(c == null, "retrieve1?????????");
    }

    @Test
    public void test_e1_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????case
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        RetailTrade c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(c != null, "???????????????");
        //
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync?????????");
        //
        c = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(c == null, "???????????????");
    }

    @Test
    public void test_e2_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID????????????????????????
        RetailTrade retailTrade1 = new RetailTrade();
        retailTrade1.setID(999999);
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteObjectSync?????????");
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(retailTradeList != null && retailTradeList.size() > 0, "retrieveNObjectSync???????????????list????????????");
        //
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync?????????????????????");
        //
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(retailTradeList == null || retailTradeList.size() == 0, "retrieveNObjectSync???????????????list????????????");
    }

    @Test
    public void test_g1_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????Case
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
            Assert.assertTrue(false, "createAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
        RetailTrade retailTradeCreate = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        //
        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(retailTradeR1.compareTo(retailTradeCreate) == 0, "update?????????");
    }

    @Test
    public void test_g2_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????????????????
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
            Assert.assertTrue(false, "createAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createAsync?????????????????????");
    }

    @Test
    public void test_g3_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
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
            Assert.assertTrue(false, "createAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createAsync?????????????????????");
    }

    @Test
    public void test_h1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<RetailTrade> retailTradeList = DataInput.getRetailTradeList();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
        retailTradePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
        //
        List<RetailTrade> retailTradeCreateList = (List<RetailTrade>) retailTradeSQLiteEvent.getListMasterTable();
        for (RetailTrade retailTrade : retailTradeCreateList) {
            RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            rt.setIgnoreSlaveListInComparision(true);
            //
            RetailTrade rtClone = (RetailTrade) retailTrade.clone();
//            rtClone.setListSlave1(null);
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
            Assert.assertTrue(rt.compareTo(rtClone) == 0, "createN?????????");
        }
    }

    @Test
    public void test_h2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????????????????
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
            Assert.assertTrue(false, "createNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNAsync?????????????????????");
    }

    @Test
    public void test_h3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????null???????????????null
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
            Assert.assertTrue(false, "createNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNAsync?????????????????????");
    }

    @Test
    public void test_i_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //... ??????????????????HTTP??????,??????PresenterTest??????????????????Junit?????????????????????
        //Case: ??????case 1
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
//            Assert.assertTrue("retrieveNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus() + "???????????????" + retailTradeSQLiteEvent.getLastErrorCode() + "??????????????????" + retailTradeSQLiteEvent.getLastErrorMessage(), false);
//        }
//        Assert.assertTrue("retrieveNAsync????????????????????????", retailTradeSQLiteEvent.getListMasterTable().size() > 0);
//        Assert.assertTrue("retrieveNAsync?????????????????????", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError); //
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
//            Assert.assertTrue("retrieveAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus(), false);
//        }
//        Assert.assertTrue("retrieveAsync?????????????????????", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("retrieveNAsync????????????????????????????????????", retailTradeSQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_i2_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //????????????????????????,?????????????????????
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s");
        retailTrade.setConditions(new String[]{"0", "11111"});
        retailTrade.setQueryKeyword("");
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "retrieveNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNAsync?????????????????????");
    }

    @Test
    public void test_i3_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //???sqlite???????????????retailTrade???????????????????????????????????????????????????????????????
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
            Assert.assertTrue(false, "createAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");

        //??????Case???????????????????????????
        retailTrade = new RetailTrade();
        retailTrade.setSql("where F_Remark = %s and F_Logo = %s");
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
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNAsync?????????????????????");

        //??????Case???????????????????????????,????????????????????????httpBO????????????
        retailTrade.setSql("where F_Remark = %s and F_Logo = %s");
        retailTrade.setConditions(new String[]{"11111", "11"});
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        // ???session  retailTradeSQLiteEvent.getStatus() = EES_SQLite_Done
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus() + "???????????????" + retailTradeSQLiteEvent.getLastErrorCode() + "??????????????????" + retailTradeSQLiteEvent.getLastErrorMessage());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync?????????????????????");
    }

    @Test
    public void test_j1_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        //?????????Case
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "DeleteAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteAsync?????????????????????");

        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteAsync?????????");
        Assert.assertTrue(retailTradeR1 == null, "???????????????");
    }

    @Test
    public void test_j2_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //????????????ID,??????????????????
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
            Assert.assertTrue(false, "DeleteAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "DeleteAsync?????????????????????");
    }

    @Test
    public void test_j3_DeleteAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null
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
            Assert.assertTrue(false, "DeleteAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "DeleteAsync?????????????????????");
    }

    @Test
    public void test_k_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case??????????????????????????????????????????
        RetailTrade bm = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync???????????????????????????");
        //
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        RetailTrade rt = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done, "retrieve1Async????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Async??????????????????????????????????????????");
        Assert.assertTrue(rt != null, "retrieve1Async???????????????RetailTrade?????????");
        //
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync???????????????????????????");
        //
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
        retailTradePresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rt, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        rt = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done, "retrieve1Async????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Async??????????????????????????????????????????");
        Assert.assertTrue(rt == null, "retrieve1Async???????????????RetailTrade?????????");
    }

    @Test
    public void test_k2_retrieve1Async() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
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
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "retrieve1Async????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1Async??????????????????????????????????????????");
        Assert.assertTrue(rt == null, "retrieve1Async???????????????RetailTrade?????????");
    }

    @Test
    public void test_l_updateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<BaseModel> retailTradeList = new ArrayList<BaseModel>();
        for (int i = 0; i < 5; i++) {
            retailTradeList.add(DataInput.getRetailTrade());
        }
        List<RetailTrade> rtCreateList = new ArrayList<RetailTrade>();
        for (int i = 0; i < retailTradeList.size(); i++) {
            RetailTrade retailTrade = (RetailTrade) retailTradeList.get(i);
            RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
            RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync???????????????????????????????????????");
            Assert.assertTrue(retailTradeCreate.compareTo(comm1Retrieve1) == 0, "CreateSync????????????,??????:???????????????????????????????????????!");
            rtCreateList.add(retailTradeCreate);
        }

        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rtCreateList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "updateNAsync?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateNAsync?????????????????????");
    }

    @Test
    public void test_m1_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //Case: ??????case
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????????????????");
        //
        retailTradeCreate.setRemark("5555555555555555555");
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        retailTradePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update?????????????????????");
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync????????????????????????????????????" + retailTradePresenter.getLastErrorCode());
        Assert.assertTrue(rt.compareTo(retailTradeCreate) == 0, "update?????????");
    }

    @Test
    public void test_m2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???update????????????null????????????null
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
            Assert.assertTrue(false, "Update?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "Update?????????????????????");
    }

    @Test
    public void test_m3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???update??????????????????
        RetailTrade retailTrade = DataInput.getRetailTrade();

        retailTrade.setID(88888);
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        Assert.assertTrue(rt == null, "update?????????");
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
            Assert.assertTrue(false, "Update?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        }
        System.out.println("????????????" + retailTradeSQLiteEvent.getLastErrorCode());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update?????????????????????????????????" + retailTradeSQLiteEvent.getLastErrorCode());
        //
        rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????????????????" + retailTradePresenter.getLastErrorCode());
        Assert.assertTrue(rt == null, "update?????????");
    }

    @Test
    public void test_n_createMasterSlaveAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
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
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveAsync??????????????????????????????????????????");
        RetailTrade retailTradeCreate = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradeCreate.compareTo(retailTradeR1) == 0, "???????????????RetailTrade?????????????????????");

        //??????case????????????????????????retailTrade
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createMasterSlaveAsync??????????????????????????????????????????");

        //??????case???????????????null??????????????????null
        retailTradeCreate = DataInput.getRetailTrade();
        retailTradeCreate.setPaymentAccount(null);
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveAsync??????????????????????????????????????????");
    }

    @Test
    public void test_o_createReplacerAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case???
        RetailTrade retailTradeOld = DataInput.getRetailTrade();
        //??????bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveAsync??????????????????????????????????????????");
        RetailTrade retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        RetailTrade retailTradeR1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTrade.compareTo(retailTradeR1) == 0, "???????????????RetailTrade?????????????????????");
        //??????createReplacerObjectAsync
        RetailTrade retailTradeNew = DataInput.getRetailTrade();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createReplacerAsync??????RetailTrade?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync??????RetailTrade???????????????????????????");
        RetailTrade retailTrade2 = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        //??????bmNew??????????????????
        RetailTrade retailTrade2R1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade2);
        Assert.assertTrue(retailTrade2.compareTo(retailTrade2R1) == 0, "???????????????RetailTrade?????????????????????");
        //??????bmOld??????????????????
        RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????????????????????????????????");
        Assert.assertTrue(retailTrade1 == null, "createReplacerAsync?????????????????????");

        //??????case???bmNew??????
        retailTradeOld = DataInput.getRetailTrade();
        //??????bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveAsync??????????????????????????????????????????");
        retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        RetailTrade retailTrade3R1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTrade.compareTo(retailTrade3R1) == 0, "???????????????RetailTrade?????????????????????");
        //??????createReplacerObjectAsync
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, retailTrade2R1, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, //
                "createReplacerAsync??????RetailTrade?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync??????RetailTrade???????????????????????????");

        //??????case???bmNew???null
        retailTradeOld = DataInput.getRetailTrade();
        //??????bmOld
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createMasterSlaveAsync????????????????????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveAsync??????????????????????????????????????????");
        retailTrade = (RetailTrade) retailTradeSQLiteEvent.getTmpMasterTableObj();
        //
        RetailTrade retailTrade4R1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTrade.compareTo(retailTrade4R1) == 0, "???????????????RetailTrade?????????????????????");
        //??????createReplacerObjectAsync
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createReplacerAsync??????RetailTrade?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync??????RetailTrade???????????????????????????");

        //??????case???bmOld???null
        retailTradeNew = DataInput.getRetailTrade();
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeNew, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createReplacerAsync??????RetailTrade?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync??????RetailTrade???????????????????????????");

        //??????case???bmOld?????????????????????
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
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createReplacerAsync??????RetailTrade?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync??????RetailTrade???????????????????????????");
    }

    @Test
    public void test_p_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<RetailTrade> retailTradeList = (List<RetailTrade>) DataInput.getRetailTradeList();
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsyncC?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC???????????????????????????");
        //
        List<RetailTrade> retailTradeList1 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeList1.size() == retailTradeList.size(), "SQLite????????????????????????");

        //??????case????????????list???null
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsyncC?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC???????????????????????????");
        //
        List<RetailTrade> retailTradeList2 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeList2.size() == 0, "SQLite????????????????????????");

        //??????case????????????????????????list
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, retailTradeList, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsyncC?????????retailTradeSQLiteEvent???????????????" + retailTradeSQLiteEvent.getStatus());
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC???????????????????????????");
        //
        List<RetailTrade> retailTradeList3 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeList3.size() == retailTradeList.size(), "SQLite????????????????????????");
    }

    @Test
    public void test_q_createReplacerNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<RetailTrade> retailTradeOldList = (List<RetailTrade>) DataInput.getRetailTradeList();
        List<RetailTrade> retailTradeOldList2 = new ArrayList<RetailTrade>();
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        RetailTrade retailTradeOld = new RetailTrade();
        //???????????????
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            retailTradeOld = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList.get(i));
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retailTrade craeteObjectSync???????????????????????????");
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
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createReplacerNAsync??????");
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerNAsync???????????????????????????");
        //
        for (int i = 0; i < retailTradeOldList2.size(); i++) {
            RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList2.get(i));
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync???????????????????????????");
            Assert.assertTrue(retailTrade == null, "retrieve1Sync???????????????????????????null");
        }

        //??????case???bmNewList???null
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerNAsync_Done);
        retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList2, null, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createReplacerNAsync??????");
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerNAsync???????????????????????????");
        Assert.assertTrue(retailTradeSQLiteEvent.getListMasterTable() == null, "createReplacerNAsync newList???null????????????????????????");

        //??????case???bmOldList???null
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerNAsync_Done);
        retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeNewList, retailTradeSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createReplacerNAsync??????");
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerNAsync???????????????????????????");
    }

    private RetailTrade createRetailTrade(RetailTrade retailTrade) {
        RetailTrade rtCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
        rt.setIgnoreIDInComparision(true);
        Assert.assertTrue(rt.compareTo(retailTrade) == 0, "update?????????");

        return rt;
    }

    private List<RetailTrade> createRetailTradeList(List<RetailTrade> list) {
        //??????case
        List<RetailTrade> rtCreateList = (List<RetailTrade>) retailTradePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, list);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1????????????,??????:????????????????????????!");
        //
        List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
        for (int i = 0; i < rtCreateList.size(); i++) {
            RetailTrade r = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtCreateList.get(i));
            r.setListSlave1(null);
            rtCreateList.get(i).setListSlave1(null); // ??????CreateN??????????????????????????????
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????????????????");
            Assert.assertTrue(r.compareTo(rtCreateList.get(i)) == 0, "create?????????");

            retailTradeList.add(r);
        }

        return retailTradeList;
    }

    /**
     * ????????????retailTrade???????????????
     */
//    @Test
//    public void test_retrieveNRetailTrade() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//        Integer total = retailTradePresenter.retrieveCount();
//        System.out.println("retailTrade???????????????" + total);
//        Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
//    }

}
