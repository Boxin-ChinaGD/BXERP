package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.model.*;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.listener.Subscribe;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static wpos.model.BaseModel.EnumSubUseCaseID.ESUC_String;

public class CommodityPresenterTest extends BasePresenterTest{
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

    @BeforeClass
    public void setUp() {
        super.setUp();
        if (commoditySQLiteEvent != null) {
            commoditySQLiteEvent.setId(Event_ID_CommdoityPresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    public static class DataInput {
        private static Commodity commodityInput = null;

        public static final Commodity getCommodity() throws CloneNotSupportedException {
            commodityInput = new Commodity();
            commodityInput.setStatus(0);
            commodityInput.setName("可乐薯片西瓜" + new Random().nextInt(100));
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("箱");
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
            commodityShopInfo.setnOStart(Commodity.NO_START_Default); // ...常量
            commodityShopInfo.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
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
                c.setID(200 - i);
                commList.add(c);
            }
            return commList;
        }
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == Event_ID_CommdoityPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Test
    public void test_a1_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常创建单品Case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityList = (List<Commodity>) commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1测试失败,原因:返回错误码不正确!");
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
            Assert.assertTrue(c.compareTo(commodity) == 0, "create失败！");
        }
    }

    @Test
    public void test_a2_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常创建单品Case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityList = (List<Commodity>) commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1测试失败,原因:返回错误码不正确!");
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
            Assert.assertTrue(c.compareTo(commodity) == 0, "create失败！");
        }

        //case：重复插入
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");

    }

    @Test
    public void test_a3_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commodityList.get(0).setTag(null);
        commodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_b1_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "update失败！");
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "update失败！");
        //case：重复插入
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setTag(null);
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b4_CreateSync() throws CloneNotSupportedException {
        // 创建多包装商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        //
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "update失败！");
    }

    @Test
    public void test_b5_CreateSync() throws CloneNotSupportedException {
        // 创建多包装商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        //
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "update失败！");
        //case：重复插入
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b6_CreateSync() throws CloneNotSupportedException {
        // 创建多包装商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        bmCreateSync.setRefCommodityID(1);
        bmCreateSync.setRefCommodityMultiple(2);
        bmCreateSync.setNO(0);
        bmCreateSync.setTag(null);
        // 必要字段为 null
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_b7_CreateSync() throws CloneNotSupportedException {
        // 创建组合商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        //
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "create失败！");
    }

    @Test
    public void test_b8_CreateSync() throws CloneNotSupportedException {
        // 创建组合商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        //
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "update失败！");
        // 重复创建
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_b9_CreateSync() throws CloneNotSupportedException {
        // 创建组合商品
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setShelfLife(0);
        bmCreateSync.setPurchaseFlag(0);
        bmCreateSync.setRuleOfPoint(0);
        bmCreateSync.setMnemonicCode(null);
        bmCreateSync.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        // 必要字段为null的case
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateComposition, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_b10_CreateSync() throws CloneNotSupportedException {
        // 创建服务型商品
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
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_b11_CreateSync() throws CloneNotSupportedException {
        // 创建服务型商品
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
        int i = 1000000000;
        bmCreateSync = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(bmCreateSync) == 0, "create失败！");
        // 重复创建
        commodityPresenter.createObjectSync(BaseSQLiteBO.CASE_Commodity_CreateService, bmCreateSync);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_b12_CreateSync() throws CloneNotSupportedException {
        // 创建服务型商品，必要字段为NULL
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
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
    }


    @Test
    public void test_c1_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case,插入再Update
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity)  commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        commodity.setStartValueRemark("1");
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_c2_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
        //异常case：update的对象为原来的对象
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_c3_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：update不允许为null的数据为null
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        commodity.setTag(null);
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
    }

    @Test
    public void test_c4_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //异常case：update不存在的数据
        commodity.setID(100000);
        commodity.setTag("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        commodityPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateObjectSync错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c == null, "update失败！");
    }

    @Test
    public void test_d_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(commodityList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");
        //根据条件查询 //...将来增加case：输入更多的查询条件
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Status = '%s'");
        commodity.setConditions(new String[]{"0"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(commodityList.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>=0!");

        //异常Case：一个通配符多个值
        commodity.setSql("where F_Status = '%s'");
        commodity.setConditions(new String[]{"0", "111"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常Case：多个通配符一个值
        commodity.setSql("where F_PricingType = '%s' and F_Tag = '%s'");
        commodity.setConditions(new String[]{"1"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常Case：多个通配符多个值
        commodity.setSql("where F_PricingType = '%s' and F_Tag = '%s'");
        commodity.setConditions(new String[]{"1", "1"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(commodityList.size() >= 0, "范围的List的size不应该小于0!");
    }

    @Test
    public void test_e1_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync返回错误码不正确!");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_e2_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(null);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(commodity == null, "retrieve1失败！");
    }

    @Test
    public void test_e3_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(-1);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(commodity == null, "retrieve1失败！");
    }

    @Test
    public void test_f1_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(c != null, "查询失败！");
        //
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync失败！");
        //
        c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(c == null, "查询失败！");
    }

    @Test
    public void test_f2_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID
        Commodity commodity1 = new Commodity();
        commodity1.setID(0);
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_f3_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除ID为null
        Commodity commodity1 = new Commodity();
        commodity1.setID(null);
        commodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_g1_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //正常Case
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_g2_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
        //case：重复插入
        commodity.setOperatorStaffID(1);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g3_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        Commodity commodity = DataInput.getCommodity();
        commodity.setTag(null);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g4_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //正常多包装Case
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_g5_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //正常多包装
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
        //case：重复插入
        commodity.setOperatorStaffID(1);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g6_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 必要字段为null的多包装Case
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
        commodity.setRefCommodityID(1);
        commodity.setRefCommodityMultiple(2);
        commodity.setNO(0);
        commodity.setMnemonicCode(null);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g7_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 正常创建组合商品
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "createAsync失败！compareTo失败");
    }

    @Test
    public void test_g8_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 正常创建组合商品
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "createAsync失败！compareTo失败");
        // 重复创建
        commodity.setOperatorStaffID(1);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g9_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 必要字段为null
        Commodity commodity = DataInput.getCommodity();
        commodity.setShelfLife(0);
        commodity.setPurchaseFlag(0);
        commodity.setRuleOfPoint(0);
        commodity.setMnemonicCode(null);
        commodity.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g10_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 正常创建服务型商品
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "createAsync失败！compareTo失败");
    }

    @Test
    public void test_g11_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 正常创建服务型商品
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "createAsync失败！compareTo失败");
        // 重复创建
        commodity.setOperatorStaffID(1);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.createObjectAsync(BaseSQLiteBO.CASE_Commodity_CreateService, commodity, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g12_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        // 创建服务型商品,必要字段为null
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        ;
    }

    @Test
    public void test_h1_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync错误码不正确！");
        //
        commodity.setTag("5555555555555555555");
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_h2_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync错误码不正确！");
        //异常case：update不允许为null的数据为null
        commodity.setTag(null);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
    }

    @Test
    public void test_h3_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 多包装正常case
        Commodity commodity = DataInput.getCommodity();
        commodity = (Commodity) commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync错误码不正确！");
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
    }

    @Test
    public void test_h4_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 多包装不存在的数据
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        commodity.setID(10000000);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
//        Assert.assertTrue(c == null, "update失败！");
    }

    @Test
    public void test_h5_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 多包装正常case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_h6_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 多包装不存在的数据
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging);
        commodity.setPriceVIP(5.55f);
        commodity.setID(10000000);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c == null, "update失败！");
    }

    @Test
    public void test_h7_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 组合商品正常case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(6.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_h8_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 组合商品必要字段为NUll
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setMnemonicCode(null);
        commodity.setPriceRetail(6.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
    }

    @Test
    public void test_h9_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 组合商品数据不存在
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateComposition);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(6.55f);
        commodity.setID(1000000);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_Commodity_UpdatePrice, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c == null, "update失败！");
    }

    @Test
    public void test_h10_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常服务型商品case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(7.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
    }

    @Test
    public void test_h11_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 服务型商品必要字段为null的case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setMnemonicCode(null);
        commodity.setPriceRetail(8.55f);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
    }

    @Test
    public void test_h12_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 服务型商品不存在的case
        Commodity commodity = createCommodity(BaseSQLiteBO.CASE_Commodity_CreateService);
        commodity.setOperatorStaffID(1);
        commodity.setPriceRetail(8.55f);
        commodity.setID(1000000);
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        commodityPresenter.updateObjectAsync(BaseSQLiteBO.CASE_UpdateCommodityOfService, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c == null, "update失败！");
    }

    @Test
    public void test_i1_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case 1
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(commoditySQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync会返回所有的数据");
        List<Commodity> commodityLists = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
        //
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(ESUC_String);
        commodity.setSql("where F_Name = '%s'");
        commodity.setConditions(new String[]{commodityLists.get(0).getName()});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveAsync错误码不正确！");
        Assert.assertTrue(commoditySQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync会有查询到符合条件的数据");
        //
        commodity.setSubUseCaseID(ESUC_String);
        commodity.setSql("where F_Name = '%s'");
        commodity.setConditions(new String[]{"~12313*"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);

        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveAsync错误码不正确！");
    }

    @Test
    public void test_i2_RetrieveNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case：一个通配符多个值
        Commodity commodity = createCommodity(BaseSQLiteBO.INVALID_CASE_ID);
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodity.setSql("where F_Status = '%s'");
        commodity.setConditions(new String[]{"0", "111"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        //
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
        commodity.setSql("where F_Status = '%s'");
        commodity.setConditions(new String[]{"-2"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNAsync错误码不正确！");
    }


    @Test
    public void test_i3_RetrieveNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case：多个通配符一个值
        Commodity commodity = createCommodity(BaseSQLiteBO.INVALID_CASE_ID);
        commodity.setSql("where F_ID = '%s' and Name = '%s'");
        commodity.setConditions(new String[]{"1"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNAsync错误码不正确！");

        //异常Case：多个通配符多个值
        commodity.setSql("where F_ID = '%s' and F_Name = '%s'");
        commodity.setConditions(new String[]{String.valueOf(commodity.getID()), commodity.getName()});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
    }

    @Test
    public void test_j_CreateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Commodity> commodityList = (List<Commodity>) DataInput.getCommodityList();
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
        //
        commodityList = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
        for (Commodity commodity : commodityList) {
            Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
            Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
        }

        //case：重复插入
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");

        commodityList = (List<Commodity>) DataInput.getCommodityList();
        for (Commodity commodity : commodityList) {
            commodity.setTag(null);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commodityPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_k_refreshByServerDataAsync() throws
            CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        List<BaseModel> commodityList = (List<BaseModel>) DataInput.getCommodityList();
        for (BaseModel commodity : commodityList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        commodityPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //Case：重复插入(查找到重复后会删除再进行插入)
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        commodityPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常case：插入的非空字段为null
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
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");
    }

    @Test
    public void test_l_refreshByServerDataAsyncC() throws
            CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        List<BaseModel> commodityList = (List<BaseModel>) DataInput.getCommodityListByID();
        for (BaseModel commodity : commodityList) {
            commodity.setSyncType(BasePresenter.SYNC_Type_C);
        }
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commodityPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commodityPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodityList, commoditySQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常case：插入的非空字段为null
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
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "refreshByServerDataAsync错误码不正确!");
    }


    public Commodity createCommodity(int baseSQLiteBOCase) throws InterruptedException, CloneNotSupportedException {
        //正常Case
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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //
        commodity = (Commodity) commoditySQLiteEvent.getBaseModel1();
        Commodity c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(c.compareTo(commodity) == 0, "update失败！");
        return c;
    }

    /**
     * 查询本地Commodity表的总条数
     */
    @Test
    public void test_retrieveNCommodity() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = commodityPresenter.retrieveCount();
        System.out.println("commodity表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}
