package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.CommodityPresenter;
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

import static com.bx.erp.model.BaseModel.EnumSubUseCaseID.ESUC_Int;
import static com.bx.erp.model.BaseModel.EnumSubUseCaseID.ESUC_String;
import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class CommodityPresenterTest extends BaseAndroidTestCase {
    private static CommodityPresenter commodityPresenter;
    private static CommoditySQLiteEvent commoditySQLiteEvent;

    private static final int Event_ID_CommdoityPresenterTest = 10000;

    public CommodityPresenterTest() throws CloneNotSupportedException {
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_CommdoityPresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    public static class DataInput {
        private static Commodity commodityInput = null;

        public static final Commodity getCommodity() throws CloneNotSupportedException {
            commodityInput = new Commodity();
            commodityInput.setStatus(0);
            commodityInput.setName("??????????????????" + new Random().nextInt(100));
            commodityInput.setShortName("??????");
            commodityInput.setSpecification("???");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("???");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble()));
            commodityInput.setPriceRetail(Math.abs(new Random().nextDouble()));
            commodityInput.setPriceVIP(Math.abs(new Random().nextDouble()));
            commodityInput.setPriceWholesale(Math.abs(new Random().nextDouble()));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(Math.abs(new Random().nextInt(18)));
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setCreateDate(new Date());
            commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1)));//
            commodityInput.setRefCommodityID(0);
            commodityInput.setRefCommodityMultiple(0);
            commodityInput.setTag("111");
            commodityInput.setNO(Math.abs(new Random().nextInt(18000)));
            commodityInput.setType(0);
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark("asdad");
            commodityInput.setPropertyValue1(new String("1"));
            commodityInput.setPropertyValue2(new String("2"));
            commodityInput.setPropertyValue3(new String("3"));
            commodityInput.setPropertyValue4(new String("4"));
            commodityInput.setCreateDatetime(new Date());
            commodityInput.setUpdateDatetime(new Date());
            commodityInput.setBarcode("122111" + System.currentTimeMillis() % 1000000);
            commodityInput.setOperatorStaffID(1);
            commodityInput.setShelfLife(1);
            commodityInput.setMultiPackagingInfo(commodityInput.getBarcode() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" +
                    commodityInput.getPriceRetail() + ";" + commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
            List<CommodityShopInfo> commodityShopInfoList = new ArrayList<>();
            commodityShopInfoList.add(getCommodityShopInfo());
            commodityInput.setListSlave2(commodityShopInfoList);
            return (Commodity) commodityInput.clone();
        }

        public static final CommodityShopInfo getCommodityShopInfo() throws CloneNotSupportedException {
            CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
            commodityShopInfo.setCommodityID(1);
            commodityShopInfo.setShopID(2);
            commodityShopInfo.setPriceRetail(12);
            commodityShopInfo.setnOStart(Commodity.NO_START_Default); // ...??????
            commodityShopInfo.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...??????
            return (CommodityShopInfo) commodityShopInfo.clone();
        }

        protected static final List<?> getCommodityList() throws CloneNotSupportedException {
            List<BaseModel> commList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                commList.add(getCommodity());
            }
            return commList;
        }

        protected static final List<?> getCommodityListByID() throws CloneNotSupportedException {
            List<BaseModel> commList = new ArrayList<BaseModel>();
            Commodity c = new Commodity();
            for (int i = 0; i < 10; i++) {
                c = getCommodity();
                c.setID((long) 200 - i);
                commList.add(c);
            }
            return commList;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == Event_ID_CommdoityPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());        }
    }

    @Test
    public void test_a1_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????????????????Case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue("CreateNSync1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("create?????????", c.compareTo(commodity) == 0);
        }
    }

    @Test
    public void test_a2_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????????????????Case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue("CreateNSync1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("create?????????", c.compareTo(commodity) == 0);
        }

        //??????case?????????????????????????????????
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue("createNObjectSync?????????", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

    }

    @Test
    public void test_a3_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????case???????????????????????????null??????????????????
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityList.get(0).setTag(null);
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue("createNObjectSync?????????", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case
        Commodity bmCreateSync = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(bmCreateSync) == 0);
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????Case
        Commodity bmCreateSync = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(bmCreateSync) == 0);
        //??????case?????????????????????????????????
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setTag(null);
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm2????????????,??????:???????????????????????????EC_NoError!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b4_CreateSync() throws CloneNotSupportedException {
        // ?????????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(bmCreateSync) == 0);
    }

    @Test
    public void test_b5_CreateSync() throws CloneNotSupportedException {
        // ?????????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(bmCreateSync) == 0);
        //??????case?????????????????????????????????
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b6_CreateSync() throws CloneNotSupportedException {
        // ?????????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        bmCreateSync.setTag(null);
        // ??????????????? null
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b7_CreateSync() throws CloneNotSupportedException {
        // ??????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("create?????????", c.compareTo(bmCreateSync) == 0);
    }

    @Test
    public void test_b8_CreateSync() throws CloneNotSupportedException {
        // ??????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(bmCreateSync) == 0);
        // ????????????
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b9_CreateSync() throws CloneNotSupportedException {
        // ??????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setMnemonicCode(null);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        // ???????????????null???case
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b10_CreateSync() throws CloneNotSupportedException {
        // ?????????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchasingUnit("");
        bmCreateSync.setNO(0);
        bmCreateSync.setnOStart(-1);
        bmCreateSync.setPurchasingPriceStart(-1);
        bmCreateSync.setRefCommodityID(0);
        bmCreateSync.setRefCommodityMultiple(0);
        bmCreateSync.setLatestPricePurchase(-1);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b11_CreateSync() throws CloneNotSupportedException {
        // ?????????????????????
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchasingUnit("");
        bmCreateSync.setNO(0);
        bmCreateSync.setnOStart(-1);
        bmCreateSync.setPurchasingPriceStart(-1);
        bmCreateSync.setRefCommodityID(0);
        bmCreateSync.setRefCommodityMultiple(0);
        bmCreateSync.setLatestPricePurchase(-1);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("create?????????", c.compareTo(bmCreateSync) == 0);
        // ????????????
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b12_CreateSync() throws CloneNotSupportedException {
        // ???????????????????????????????????????NULL
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchasingUnit("");
        bmCreateSync.setNO(0);
        bmCreateSync.setnOStart(-1);
        bmCreateSync.setPurchasingPriceStart(-1);
        bmCreateSync.setRefCommodityID(0);
        bmCreateSync.setRefCommodityMultiple(0);
        bmCreateSync.setLatestPricePurchase(-1);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setMnemonicCode(null);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        //
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }


    @Test
    public void test_c1_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case,?????????Update
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        commodity.setStartValueRemark("1");
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("updateObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_c2_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
        //??????case???update???????????????????????????
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("updateObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_c3_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case???update????????????null????????????null
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        commodity.setTag(null);
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("updateObjectSync?????????????????????", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c4_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //??????case???update??????????????????
        commodity.setID(100000l);
        commodity.setTag("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("updateObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_d_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync??????????????????????????????>=0!", commodityList.size() >= 0);
        //?????????????????? //...????????????case??????????????????????????????
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Status = ?");
        commodity.setConditions(new String[]{"0"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????RetrieveNSync??????????????????????????????>=0!", commodityList.size() >= 0);

        //??????Case???????????????????????????
        commodity.setSql("where F_Status = ?");
        commodity.setConditions(new String[]{"0", "111"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????????????????
        commodity.setSql("where F_PricingType = ? and F_Tag = ?");
        commodity.setConditions(new String[]{"1"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????List???size?????????0!", commodityList.size() == 0);

        //??????Case???????????????????????????
        commodity.setSql("where F_PricingType = ? and F_Tag = ?");
        commodity.setConditions(new String[]{"1", "1"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????List???size???????????????0!", commodityList.size() >= 0);
    }

    @Test
    public void test_e1_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case1
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createObjectSync????????????????????????!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_e2_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(null);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1?????????", commodity == null);
    }

    @Test
    public void test_e3_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID??????????????????????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(-1l);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1?????????", commodity == null);
    }

    @Test
    public void test_f1_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????case
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", c != null);
        //
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("deleteObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", c == null);
    }

    @Test
    public void test_f2_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID
        Commodity commodity1 = new Commodity();
        commodity1.setID(0l);
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue("deleteObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_f3_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID???null
        Commodity commodity1 = new Commodity();
        commodity1.setID(null);
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue("deleteObjectSync?????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_g1_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????Case
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_g2_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
        //??????case?????????????????????????????????
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g3_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setTag(null);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g4_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //???????????????Case
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_g5_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //???????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
        //??????case?????????????????????????????????
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g6_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ???????????????null????????????Case
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commodity.setMnemonicCode(null);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g7_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????compareTo??????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_g8_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????compareTo??????", c.compareTo(commodity) == 0);
        // ????????????
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g9_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ???????????????null
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setMnemonicCode(null);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g10_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ???????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchasingUnit("");
        commodity.setNO(0);
        commodity.setnOStart(-1);
        commodity.setPurchasingPriceStart(-1);
        commodity.setRefCommodityID(0);
        commodity.setRefCommodityMultiple(0);
        commodity.setLatestPricePurchase(-1);
        commodity.setPurchaseFlag(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????compareTo??????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_g11_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ???????????????????????????
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchasingUnit("");
        commodity.setNO(0);
        commodity.setnOStart(-1);
        commodity.setPurchasingPriceStart(-1);
        commodity.setRefCommodityID(0);
        commodity.setRefCommodityMultiple(0);
        commodity.setLatestPricePurchase(-1);
        commodity.setPurchaseFlag(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createAsync?????????compareTo??????", c.compareTo(commodity) == 0);
        // ????????????
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateService, commodity, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g12_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // ?????????????????????,???????????????null
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchasingUnit("");
        commodity.setNO(0);
        commodity.setnOStart(-1);
        commodity.setPurchasingPriceStart(-1);
        commodity.setRefCommodityID(0);
        commodity.setRefCommodityMultiple(0);
        commodity.setLatestPricePurchase(-1);
        commodity.setPurchaseFlag(0);
        commodity.setMnemonicCode(null);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
        ;
    }

    @Test
    public void test_h1_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????case
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        commodity.setTag("5555555555555555555");
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_h2_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????case
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //??????case???update????????????null????????????null
        commodity.setTag(null);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h3_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????case
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        commodity.setID(10000000l);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_h4_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????????????????
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        commodity.setID(10000000l);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_h5_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_h6_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????????????????
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        commodity.setID(10000000l);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_h7_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????????????????case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(6.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_h8_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????????????????NUll
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setMnemonicCode(null);
        commodity.setPriceRetail(6.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h9_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????????????????
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(6.55f);
        commodity.setID(1000000l);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_h10_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ?????????????????????case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(7.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_h11_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????????????????????????????null???case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setMnemonicCode(null);
        commodity.setPriceRetail(8.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h12_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ???????????????????????????case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(8.55f);
        commodity.setID(1000000l);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("Update??????", false);
        }
        Assert.assertTrue("Update?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c == null);
    }

    @Test
    public void test_i1_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????case 1
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????", commoditySQLiteEvent.getListMasterTable().size() > 0);
        List<Commodity> commodityLists = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
        //
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(ESUC_String);
        commodity.setSql("where F_Name = ?");
        commodity.setConditions(new String[]{commodityLists.get(0).getName()});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveAsync?????????", false);
        }
        Assert.assertTrue("retrieveAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", commoditySQLiteEvent.getListMasterTable().size() > 0);
        //
        commodity.setSubUseCaseID(ESUC_String);
        commodity.setSql("where F_Name = ?");
        commodity.setConditions(new String[]{"~12313*"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);

        Assert.assertTrue("retrieveAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_i2_RetrieveNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????Case???????????????????????????
        Commodity commodity = createCommodity(BaseSQLiteBO.INVALID_CASE_ID);
        commodity.setSubUseCaseID(ESUC_Int);
        commodity.setSql("where F_Status = ?");
        commodity.setConditions(new String[]{"0", "111"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
        //
        commodity.setSubUseCaseID(ESUC_Int);
        commodity.setSql("where F_Status = ?");
        commodity.setConditions(new String[]{"-2"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        Assert.assertTrue("retrieveNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    @Test
    public void test_i3_RetrieveNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //??????Case???????????????????????????
        Commodity commodity = createCommodity(BaseSQLiteBO.INVALID_CASE_ID);
        commodity.setSql("where F_ID = ? and Name = ?");
        commodity.setConditions(new String[]{"1"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        Assert.assertTrue("retrieveNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

        //??????Case???????????????????????????
        commodity.setSql("where F_ID = ? and F_Name = ?");
        commodity.setConditions(new String[]{String.valueOf(commodity.getID()), commodity.getName()});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync?????????", false);
        }
        Assert.assertTrue("retrieveNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_CreateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
        }

        //??????case?????????????????????????????????
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        commodityList = (List<Commodity>) DataInput.getCommodityList();
        for (Commodity commodity : commodityList) {
            commodity.setTag(null);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_k_refreshByServerDataAsync() throws
            CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????Case
        List<BaseModel> commodityList = (List<BaseModel>) DataInput.getCommodityList();
        for (BaseModel commodity : commodityList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        commodityPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????(??????????????????????????????????????????)
        for (BaseModel commodity : commodityList) {
            List<CommodityShopInfo> commodityShopInfos = (List<CommodityShopInfo>) commodity.getListSlave2();
            for(CommodityShopInfo commodityShopInfo : commodityShopInfos) {
                commodityShopInfo.setID(null);
                commodityShopInfo.setCommodityID(commodity.getID().intValue());
            }
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        commodityPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!" + commoditySQLiteEvent.getLastErrorCode(), commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null
        List<BaseModel> cList = new ArrayList<BaseModel>();
        List<Commodity> commList = (List<Commodity>) DataInput.getCommodityList();
        for (Commodity commodity : commList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
            commodity.setTag(null);
            cList.add(commodity);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        commodityPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, cList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_l_refreshByServerDataAsyncC() throws
            CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????Case
        List<BaseModel> commodityList = (List<BaseModel>) DataInput.getCommodityListByID();
        for (BaseModel commodity : commodityList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commodityPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Case???????????????(??????????????????????????????????????????)
        for (BaseModel commodity : commodityList) {
            List<CommodityShopInfo> commodityShopInfos = (List<CommodityShopInfo>) commodity.getListSlave2();
            for(CommodityShopInfo commodityShopInfo : commodityShopInfos) {
                commodityShopInfo.setID(null);
                commodityShopInfo.setCommodityID(commodity.getID().intValue());
            }
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commodityPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!" + commoditySQLiteEvent.getLastErrorCode(), commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case???????????????????????????null
        List<BaseModel> cList = new ArrayList<BaseModel>();
        List<Commodity> commList = (List<Commodity>) DataInput.getCommodityListByID();
        for (Commodity commodity : commList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
            commodity.setTag(null);
            cList.add(commodity);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commodityPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, cList, commoditySQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync?????????", commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync??????????????????!", commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    public Commodity createCommodity(int baseSQLiteBOCase) throws InterruptedException, CloneNotSupportedException {
        //??????Case
        Commodity commodity = DataInput.getCommodity();
        switch (baseSQLiteBOCase) {
            case BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging:
                commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
                commodity.setRefCommodityID(1);
                commodity.setRefCommodityMultiple(2);
                commodity.setNO(0);
                break;
            case BaseSQLiteBO.CASE_Commodity_CreateService:
                commodity.setShelfLife(0);
                commodity.setPurchasingUnit("");
                commodity.setNO(0);
                commodity.setnOStart(-1);
                commodity.setPurchasingPriceStart(-1);
                commodity.setRefCommodityID(0);
                commodity.setRefCommodityMultiple(0);
                commodity.setLatestPricePurchase(-1);
                commodity.setPurchaseFlag(0);
                commodity.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
                break;
            case BaseSQLiteBO.CASE_Commodity_CreateComposition:
                commodity.setShelfLife(0);
                commodity.setPurchaseFlag(0);
                commodity.setRuleOfPoint(0);
                commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
                break;
        }
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(baseSQLiteBOCase, commodity, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync?????????", false);
        }
        Assert.assertTrue("createAsync?????????????????????", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update?????????", c.compareTo(commodity) == 0);
        return c;
    }

    /**
     * ????????????Commodity???????????????
     */
    @Test
    public void test_retrieveNCommodity() throws Exception {
        Shared.printTestMethodStartInfo();

        CommodityPresenter commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        Integer total = commodityPresenter.retrieveCount();
        System.out.println("commodity???????????????" + total);
        Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }

}
