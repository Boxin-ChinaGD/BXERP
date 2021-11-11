
package com.test.bx.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SubCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.CommodityShopInfoPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

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
import java.util.List;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;
import static com.test.bx.app.presenter.CommodityPresenterTest.DataInput.getCommodityShopInfo;


public class CommodityJUnit extends BaseHttpAndroidTestCase {
    private static List<Commodity> commodityList;
    private static Long conflictID;
    private static Commodity commodity = null;
    private static long lID = 0l;

    CommodityPresenter presenter = null;

    CommodityShopInfoPresenter commodityShopInfoPresenter = null;

    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;

    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;

    private static CommodityPresenter commodityPresenter;

    private static List<Commodity> commoditySyncList = new ArrayList<Commodity>();

    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static final int EVENT_ID_CommodityJUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getCommodityPresenter();
        }

        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();

        commodityShopInfoPresenter = GlobalController.getInstance().getCommodityShopInfoPresenter();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_CommodityJUnit);
//            commoditySQLiteEvent.setTimeout(30 * 1000);//?
//            commoditySQLiteEvent.setEventProcessed(false);//?
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
//        logoutHttpEvent.setSqliteBO(null);//?
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_CommodityJUnit);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
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
        private static Commodity commodityInput = null;

        protected static final Commodity getCommodity() throws CloneNotSupportedException {
            commodityInput = new Commodity();
            commodityInput.setBarcode("11231311" + System.currentTimeMillis() % 1000000);
            commodityInput.setStatus(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            commodityInput.setName("可乐薯片哟" + System.currentTimeMillis() % 1000000);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("箱");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceRetail(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceVIP(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceWholesale(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(Math.abs(new Random().nextInt(18) + 1));
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1) + 1));//
            commodityInput.setRefCommodityID(0);
            commodityInput.setRefCommodityMultiple(0);
            commodityInput.setTag("111");
            commodityInput.setNO(Math.abs(new Random().nextInt(18000)));
            commodityInput.setType(0);
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark(""); // 期初值备注
            commodityInput.setReturnObject(1);
            commodityInput.setOperatorStaffID(1); // StaffID
            commodityInput.setPropertyValue1("自定义内容1");
            commodityInput.setPropertyValue2("自定义内容2");
            commodityInput.setPropertyValue3("自定义内容3");
            commodityInput.setPropertyValue4("自定义内容4");
            commodityInput.setCreateDatetime(new Date());
            commodityInput.setUpdateDatetime(new Date());
            commodityInput.setCreateDate(new Date());
            commodityInput.setProviderIDs("1");

            commodityInput.setMultiPackagingInfo(commodityInput.getBarcode() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" +
                    commodityInput.getPriceRetail() + ";" + commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
            List<CommodityShopInfo> commodityShopInfoList = new ArrayList<>();
            commodityShopInfoList.add(getCommodityShopInfo());
            commodityInput.setListSlave2(commodityShopInfoList);
            return (Commodity) commodityInput.clone();
        }

        public static final Commodity getSubCommodity() throws CloneNotSupportedException, InterruptedException, ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
            String date = sdf.format(new Date());
            commodityInput = new Commodity();
            commodityInput.setStatus(Commodity.EnumStatusCommodity.ESC_Normal.getIndex());
            commodityInput.setName("可乐薯片南瓜" + System.currentTimeMillis() % 1000000);
            // Thread.sleep(500);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("箱");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble()));
            commodityInput.setPriceRetail(10.00d);
            commodityInput.setPriceVIP(10.00d);
            commodityInput.setPriceWholesale(10.00d);
            // commodityInput.setRatioGrossMargin(Math.abs(new Random().nextDouble()));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(0);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(0);
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(0);//
            commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
            commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
            commodityInput.setTag("111");
            commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
            // commodityInput.setNOAccumulated(0);
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark(""); // 期初值备注
            commodityInput.setReturnObject(1); // 返回对象的值
            commodityInput.setBarcode("122111" + System.currentTimeMillis() % 1000000);
            // Thread.sleep(500);
            commodityInput.setPropertyValue1("自定义内容1");
            commodityInput.setPropertyValue2("自定义内容2");
            commodityInput.setPropertyValue3("自定义内容3");
            commodityInput.setPropertyValue4("自定义内容4");
            commodityInput.setCreateDate(sdf.parse(date));
            commodityInput.setCreateDatetime(sdf.parse(date));
            commodityInput.setUpdateDatetime(sdf.parse(date));
            commodityInput.setProviderIDs("1");

            List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();

            SubCommodity subCommodity1 = new SubCommodity();
            subCommodity1.setSubCommodityNO(2);
            subCommodity1.setSubCommodityID(2);
            subCommodity1.setPrice(3);
            subCommodities.add(subCommodity1);

            SubCommodity subCommodity2 = new SubCommodity();
            subCommodity2.setSubCommodityNO(3);
            subCommodity2.setSubCommodityID(3);
            subCommodity2.setPrice(3);
            subCommodities.add(subCommodity2);

            commodityInput.setListSlave1(subCommodities);
            return (Commodity) commodityInput.clone();
        }

        public static final Commodity getServiceCommodity() throws Exception {
            commodityInput = new Commodity();
            commodityInput.setStatus(Commodity.EnumStatusCommodity.ESC_Normal.getIndex());
            commodityInput.setName("薯片" + System.currentTimeMillis() % 1000000);
            Thread.sleep(1000);
            commodityInput.setShortName("商品");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setBrandID(1);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
            commodityInput.setPriceRetail(10); // 零售价
            commodityInput.setPriceVIP(10); // 会员价
            commodityInput.setPurchasingUnit("");
            commodityInput.setPriceWholesale(10); // 批发价
            // commodityInput.setRatioGrossMargin(Math.abs(new Random().nextDouble()));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Commodity.DEFAULT_VALUE_PurchaseFlag);//
            commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
            commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
            // commodityInput.setIsGift(Math.abs(new Random().nextInt(1)));
            commodityInput.setTag("111");
            commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
            commodityInput.setOperatorStaffID(1);
            // commodityInput.setNOAccumulated(Math.abs(new Random().nextInt(18000)));
            commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
            commodityInput.setStartValueRemark("");
            Thread.sleep(1000);
            commodityInput.setPropertyValue1("自定义属性1");
            commodityInput.setPropertyValue2("自定义属性2");
            commodityInput.setPropertyValue3("自定义属性3");
            commodityInput.setPropertyValue4("自定义属性4");
            commodityInput.setReturnObject(1); // 返回对象的值
            commodityInput.setProviderIDs("1");

            return (Commodity) commodityInput.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityJUnit) {
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
        //创建一个Commodity的List
        commodityList = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodityList.add(commodity);
        }
        //将Commodity的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        conflictID = commodityList.get(1).getID();
        //
        Assert.assertTrue("createNSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            Commodity commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的commodityList没有完全插入成功", commodity.compareTo(commodityList.get(i)) == 0);
        }
    }

    @Test
    public void test_a2_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:非空字段为空
        List<Commodity> commodityList1 = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodity.setUpdateDatetime(null);
            commodityList1.add(commodity);
        }
        //将Commodity的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList1);
        //
        Assert.assertTrue("createNSync非空字段为空, 返回的错误码应该为OtherError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_a3_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //异常Case:主键冲突
        List<Commodity> commodityList2 = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodity.setID(conflictID);
            commodityList2.add(commodity);
        }
        //将Commodity的List插入到本地SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList2);
        //
        Assert.assertTrue("createNSync主键冲突, 返回的错误码应该为OtherError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Commodity
        Commodity commodity = DataInput.getCommodity();
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Commodity失败!", commodity.compareTo(commodity) == 0);
    }

    @Test
    public void test_b2_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //创建一个Commodity
        Commodity commodity = DataInput.getCommodity();
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Commodity失败!", commodity.compareTo(commodity) == 0);

        //异常Case:非空字段为空
        //设置非空字段为null
        Commodity commodity2 = DataInput.getCommodity();
        commodity2.setUpdateDatetime(null);
        //将commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity2);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b3_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:主键冲突
        //设置ID为使用过的ID
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(conflictID);
        //将commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_c1_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Commodity commodity = DataInput.getCommodity();
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //正常Case
        //修改属性的值
        commodity.setUpdateDatetime(new Date());
        commodity.setPropertyValue4("this is the propertyValue4 after update " + (int) (Math.random() * 10000));
        //将修改后的comm Update到本地SQLite
        presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("updateSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity c = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update失败!", c.compareTo(commodity) == 0);
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //异常Case:update非空字段为null
        Commodity commodity = DataInput.getCommodity();
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //修改属性的值
        commodity.setUpdateDatetime(null);
        //将修改后的commodity Update到本地SQLite
        presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        //
        Assert.assertTrue("updateSync 非空字段为null!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_c3_updateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        //正常Case:update ID不存在的数据
        Commodity commodity1 = DataInput.getCommodity();
        //设置SQLite不存在的ID
        commodity1.setID(0l);
        //验证ID不存在SQLite中
        Commodity commodity2 = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("设置的ID存在于SQLite中!", commodity2 == null);
        //设置需要Update的字段的值
        commodity1.setPropertyValue4("this is the propertyValue4 after second update " + (int) (Math.random() * 10000));
        //将修改后的commodity update到本地SQLite
        presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        //
        Assert.assertTrue("updateSync ID不存在的字段!错误码应该为NoError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d1_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Commodity bmCreateSync = DataInput.getCommodity();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //正常Case1
        Commodity commodity1 = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        //
        Assert.assertTrue("retrieve1 返回的错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1失败!", commodity1 != null && commodity1.compareTo(bmCreateSync) == 0);
    }

    @Test
    public void test_d2_retrieve1Sync() throws CloneNotSupportedException {
        //正常Case2:查找不存在的ID的数据
        Commodity bmCreateSync = DataInput.getCommodity();
        bmCreateSync.setID(-1l);
        Commodity commodity2 = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        //
        Assert.assertTrue("retrieve1 查找不存在的ID的数据返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1 不存在的ID的数据失败!", commodity2 == null);
    }

    // @Test 与Present中的重复
    public void e_retrieveNSync() {
        Shared.printTestMethodStartInfo();

        //正常Case1: 跟具条件查询
        List<Commodity> commodityList = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < 5; i++) {
            commodityList.get(i).setMnemonicCode("111");
            Commodity c = commodityList.get(i);
            c.setOperatorStaffID(1);
            if (c.getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
                continue;
            }
            presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            Assert.assertTrue("update本地SQLite中的字段的值时返回错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Commodity commodity = new Commodity();
        //
        commodity.setSql("where F_MnemonicCode = ?");
        commodity.setConditions(new String[]{"111"});
        List<Commodity> conditionCommodityList1 = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        //
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", conditionCommodityList1.size() != 0);
        //正常Case2: 查询所有
        List<Commodity> allCommodityList = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        Assert.assertTrue("retrieveN的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 应该要有数据返回", allCommodityList.size() != 0);

        //异常Case1: 根据条件查询, 条件中的字段不存在
        commodity.setSql("where F_A = ?");
        commodity.setConditions(new String[]{"不存在"});
        List<Commodity> conditionCommodityList2 = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件中的字段不存在,返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

        //异常Case2: 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素
        commodity.setSql(" where F_MnemonicCode = ?");
        commodity.setConditions(new String[]{"BX", "SP"});
        List<Commodity> conditionCommodityList3 = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素,返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

        //异常Case3: 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据
        commodity.setSql("where F_MnemonicCode = ? and F_Tag = ?");
        commodity.setConditions(new String[]{"111"});
        List<Commodity> conditionCommodityList4 = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 返回的错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据", conditionCommodityList4.size() == 0);

        //异常Case4: 根据条件查询, 条件语句中有两个字段, 条件数组中也有两个元素
        commodity.setSql("where F_MnemonicCode = ? and F_Tag = ?");
        commodity.setConditions(new String[]{"111", "111"});
        List<Commodity> conditionCommodityList5 = (List<Commodity>) presenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        //
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 条件数组中也有两个元素, 返回的错误码不正确", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 根据条件查询, 条件语句中有两个字段, 条件数组中也有两个元素, 应该能找到数据", conditionCommodityList5.size() != 0);
    }

    @Test
    public void test_f_deleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Commodity
        Commodity commodity = DataInput.getCommodity();
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createSync返回的错误码不正确!" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("deleteSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Commodity commodity1 = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("deleteSync失败!", commodity1 == null);

        //正常Case: 删除ID不存在的数据
        Commodity commodity2 = DataInput.getCommodity();
        commodity2.setID(-1l);
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("createSync返回的错误码不正确!" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity2);
        Assert.assertTrue("deleteSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    @Test
    public void test_g_createNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建Commodity的List
        commodityList = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodityList.add(commodity);
        }
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commoditySQLiteBO.createNAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);
        conflictID = commodityList.get(1).getID();
        //

        //
        if (!waitForEventProcessed(commoditySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败！原因:超时", false);
        } else {
            Assert.assertTrue("CreateNAsync返回的错误码不正确", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            for (int i = 0; i < 5; i++) {
                Commodity commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList.get(i));
                Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                Assert.assertTrue("插入的commodityList没有完全插入成功", commodity.compareTo(commodityList.get(i)) == 0);
            }
        }

        //异常Case:非空字段为空
        commoditySQLiteEvent.setEventProcessed(false);
        List<Commodity> commodityList1 = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodity.setUpdateDatetime(null);
            commodityList1.add(commodity);
        }
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commoditySQLiteBO.createNAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList1);
        //
        if (!waitForEventProcessed(commoditySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败! 原因: 超时", false);
        } else {
            Assert.assertTrue("createNAsync返回的错误码不正确", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }

        //异常Case:主键冲突
        commoditySQLiteEvent.setEventProcessed(false);
        List<Commodity> commodityList2 = new ArrayList<Commodity>();
        for (int i = 0; i < 5; i++) {
            Commodity commodity = DataInput.getCommodity();
            commodity.setUpdateDatetime(null);
            commodity.setID(conflictID);
            commodityList2.add(commodity);
        }
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
        commoditySQLiteBO.createNAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityList2);
        //
        if (!waitForEventProcessed(commoditySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败! 原因: 超时", false);
        } else {
            Assert.assertTrue("createNAsync返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_h1_createAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //正常Case创建一个Commodity
        Commodity commodity = DataInput.getCommodity();
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        //
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h2_createAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //异常Case:非空字段为空
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventProcessed(false);
        //设置非空字段为null
        commodity.setName(null);
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("错误码不正确：", commodityHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        } else {
            Assert.assertTrue("Commodity创建成功！，应该创建失败", false);
        }
        //
    }

    @Test
    public void test_h3_createAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //异常Case:主键冲突,重复创建
        Commodity commodity = DataInput.getCommodity();
        commoditySQLiteEvent.setEventProcessed(false);
        //将commodity插入到本地SQLite
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        // 重复请求创建
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated);
    }

    @Test
    public void test_h4_createAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //正常Case创建一个多包装Commodity
        Commodity commodity = DataInput.getCommodity();
        commodity.setType(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
        commodity.setRefCommodityID(0);
        commodity.setRefCommodityMultiple(0);
        commodity.setNO(0);
        commodity.setProviderIDs("1");
        Commodity commodity1 = DataInput.getCommodity();
        commodity1.setRefCommodityID(1);
        commodity1.setRefCommodityMultiple(2);
        commodity.setMultiPackagingInfo(commodity.getBarcode() + "," + commodity1.getBarcode() + ";" + commodity.getPackageUnitID() + "," + commodity1.getPackageUnitID() + ";" +
                commodity.getRefCommodityMultiple() + "," + commodity1.getRefCommodityMultiple() + ";" +
                commodity.getPriceRetail() + "," + commodity1.getPriceRetail() + ";" +
                commodity.getPriceVIP() + "," + commodity1.getPriceVIP() + ";" +
                commodity.getPriceWholesale() + "," + commodity.getPriceWholesale() + ";" +
                commodity.getName() + "," + commodity1.getName() + ";");
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        //
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h5_createAsync() throws CloneNotSupportedException, InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case创建一个组合商品Commodity
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        String barcodes = "254521454628769759";
        Commodity subCommodity = DataInput.getSubCommodity();
        JSONObject jsonObject = (JSONObject) JSON.toJSON(subCommodity);
        JSONObject json2 = (JSONObject) JSON.parse(JSONObject.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println("Commodity ===== : " + json2);
        //
        subCommodity.setShelfLife(-1);
        subCommodity.setOperatorStaffID(1);
        subCommodity.setPurchaseFlag(-1);
        subCommodity.setRuleOfPoint(-1);
        subCommodity.setMultiPackagingInfo(barcodes + ";1;1;1;8;8;" + "组合商品A" + System.currentTimeMillis() % 1000000 + ";");//
        subCommodity.setSubCommodityInfo(json2.toString());//
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        //
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseSQLiteBO.CASE_Commodity_CreateComposition, subCommodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    // @Test 暂无该HTTP请求
    public void i1_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        System.out.println("-------------------------------------Update1");
        Commodity commodity = createCommodity();
        commodity.setOperatorStaffID(1);
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        if (!commoditySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity更新失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("commodity更新超时！", false);
        }

        Assert.assertTrue("updateSync返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update失败!", commodity.compareTo(commodity) == 0);
    }

    // @Test 暂无该HTTP请求
    public void i2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case:update非空字段为null
        System.out.println("-------------------------------------Update2");
        commoditySQLiteEvent.setEventProcessed(false);
        //修改属性的值
        commodity.setUpdateDatetime(null);
        //将修改后的commodity Update到本地SQLite
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        if (!commoditySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity更新失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("commodity更新超时！", false);
        }

        Assert.assertTrue("updateSync返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    // @Test 暂无该HTTP请求
    public void i3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case:update ID不存在的数据
        System.out.println("-------------------------------------Update3");
        commoditySQLiteEvent.setEventProcessed(false);
        Commodity commodity1 = DataInput.getCommodity();
        //设置SQLite不存在的ID
        commodity1.setID(1000000l);
        //验证ID不存在SQLite中
        Commodity commodity2 = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", commodity2 == null);
        //设置需要Update的字段的值
        commodity1.setPropertyValue4("this is the propertyValue4 after second update " + (int) (Math.random() * 10000));
        //将修改后的commodity update到本地SQLite
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
        if (!commoditySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity更新失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("commodity更新超时！", false);
        }

        Assert.assertTrue("updateSync返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        commodity = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j1_retrieve1Async() throws
            CloneNotSupportedException, InterruptedException {

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(1l);
        // 查询
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async);
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.retrieve1AsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity查询失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity查询超时！", false);
        }
        Assert.assertTrue("请求查询commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("服务器返回的Commodiy为空！", commodityHttpBO.getHttpEvent().getBaseModel1() != null);
    }

    @Test
    public void test_j2_retrieve1Async() throws
            CloneNotSupportedException, InterruptedException {
        commoditySQLiteBO.getHttpEvent().setBaseModel1(null);
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Commodity commodity = DataInput.getCommodity();
        commodity.setID(9999999l);
        // 查询不存在的一个Commodity
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async);
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.retrieve1AsyncC(BaseSQLiteBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity查询失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity查询超时！", false);
        }
        Assert.assertTrue("错误码不正确：", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        Assert.assertTrue("服务器返回的Commodiy不为空！应该为空", commodityHttpBO.getHttpEvent().getBaseModel1() == null);
    }


    @Test
    public void test_k1_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 跟具条件查询
        System.out.println("-------------------------------------RN1");
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync搜索到的数据数量应该>=0!", commodityList.size() >= 0);
        for (int i = 0; i < commodityList.size(); i++) {
            commodityList.get(i).setMnemonicCode("222");
            if (commodityList.get(i).getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
                continue;
            }
            CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
            commodityShopInfo.setSql("where F_CommodityID = ?");
            commodityShopInfo.setConditions(new String[]{String.valueOf(commodityList.get(i).getID())});
            List<CommodityShopInfo> commodityShopInfos = (List<CommodityShopInfo>) commodityShopInfoPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_CommodityShopInfo_RetrieveNByConditions, commodityShopInfo);
            commodityList.get(i).setListSlave2(commodityShopInfos);
            presenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList.get(i));
            Assert.assertTrue("update本地SQLite中的字段的值时返回错误码不正确" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        //
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_MnemonicCode = ?");
        commodity.setConditions(new String[]{"222"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        //
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", commoditySQLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_k2_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //正常Case2: 查询所有
        System.out.println("-------------------------------------RN2");
        commoditySQLiteEvent.setEventProcessed(false);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", commoditySQLiteEvent.getListMasterTable().size() != 0);
    }


    @Test
    public void test_k3_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case1: 根据条件查询, 条件中的字段不存在
        System.out.println("-------------------------------------RN3");
        commoditySQLiteEvent.setEventProcessed(false);
        //
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_A = ?");
        commodity.setConditions(new String[]{"不存在"});
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        System.out.println("xxxxxxx   "+ commoditySQLiteEvent.hashCode());
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        System.out.println("xxxxxxx   "+ commoditySQLiteEvent.hashCode());
        Assert.assertTrue("retrieveN 根据条件查询, 条件中的字段不存在,返回的错误码不正确!错误码：" + commoditySQLiteEvent.getLastErrorCode(), commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_k4_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //异常Case2: 根据条件查询, 条件语句中只有一个字段, 但是条件数组中有两个元素
        System.out.println("-------------------------------------RN4");
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_MnemonicCode = ?");
        commodity.setConditions(new String[]{"BX", "SP"});
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!" + commoditySQLiteBO.getSqLiteEvent().getLastErrorCode(), commoditySQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_k5_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case3: 根据条件查询, 条件语句中有两个字段, 但是条件数组总只有一个元素, 查不到数据
        System.out.println("-------------------------------------RN5");
        commoditySQLiteEvent.setEventProcessed(false);
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_MnemonicCode = ? and F_Tag = ?");
        commodity.setConditions(new String[]{"222"});
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!" + commoditySQLiteEvent.getLastErrorCode(), commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", commoditySQLiteEvent.getListMasterTable().size() == 0);
    }

    @Test
    public void test_k6_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case4: 根据条件查询, 条件语句中有两个字段, 条件数组中也有两个元素
        System.out.println("-------------------------------------RN6");
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        commoditySQLiteEvent.setEventProcessed(false);
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_MnemonicCode = ? and F_Tag = ?");
        commodity.setConditions(new String[]{commodityList.get(0).getMnemonicCode(), commodityList.get(0).getTag()});
        //
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", commoditySQLiteEvent.getListMasterTable().size() != 0);
    }

    /**
     * 1.pos1登录 staff登录
     * 2.模拟创建一个commodity
     * 3.pos2登录 staff登录
     * 4.发送RN请求，应该返回刚刚创建的commodity
     */
    @Test
    public void test_l_HttpRetrieveNSync() throws
            CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //1,登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //2.模拟创建一个commodity
        Commodity commodity = DataInput.getCommodity();
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        logOut();

        //2步，登录POS和Staff
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.发送RN请求，应该返回刚刚创建的commodity
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求同步Commodity超时！", false);
        }
        //
        if (commoditySQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("同步服务器返回的商品失败" + commoditySQLiteBO.getSqLiteEvent().getLastErrorCode(), false);
        }
        List<Commodity> commodityList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("同步RN不应该没有数据返回", commodityList.size() > 0);
    }

    /**
     * 1.POS2 staff登录，暂停同步线程
     * 2.pos2 feedback
     * 3.pos2 发出RN请求，没有需要同步的数据返回
     */
    @Test
    public void test_m_feedback() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //2.pos2 feedback
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, commoditySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会返回所有的数据", commoditySQLiteEvent.getListMasterTable().size() > 0);
        List<Commodity> commodityLists = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
        String commodityIDs = "";
        for (int i = 0; i < commodityLists.size(); i++) {
            commodityIDs = commodityIDs + "," + commodityLists.get(i).getID();
        }
        commodityIDs = commodityIDs.substring(1, commodityIDs.length());
        commodityHttpBO.feedback(commodityIDs);
        //等待
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        //3.pos2 发出RN请求，没有需要同步的数据返回
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求同步Commodity超时！", false);
        }
        //
        List<Commodity> commodityList1 = (List<Commodity>) commodityHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("同步RN应该没有数据返回", commodityList1.size() == 0);
    }

    /*
        1.POS staff登录，暂停同步线程
        2.POS创建商品和条形码
        3.根据条形码查找商品的库存
     */
    @Test
    public void test_o_retrieveInventory() throws
            InterruptedException, CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1.pos2 staff登录，暂停同步线程
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //创建Commodity
        Commodity commodity = DataInput.getCommodity();
        commodity.setNO(10);

        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("返回对象为空!", commodityHttpBO.getHttpEvent().getBaseModel1() != null);
        Commodity bmFromEvent = (Commodity) commodityHttpBO.getHttpEvent().getBaseModel1();
//        //...
//        bmFromEvent.setID(null);
//        bmFromEvent.setOperatorStaffID(3);
//        commodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmFromEvent);
//        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Commodity cR1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmFromEvent);
//        Assert.assertTrue("retrieve1ObjectSync错误码不正确！", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("update失败！", cR1.compareTo(bmFromEvent) == 0);

        logOut();

        //让SQLite中有刚创建的商品数据
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.发送R1请求，应该返回刚刚创建的commodity
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async);
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.retrieve1AsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmFromEvent)) {
            Assert.assertTrue("Commodity查询失败！", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity查询超时！", false);
        }
        //
        Assert.assertTrue("服务器返回的错误码不正确！", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("同步RN不应该没有数据返回", commodityHttpBO.getHttpEvent().getBaseModel1() != null);


        Commodity c = new Commodity();
        c.setBarcode(commodity.getBarcode());
//        Commodity c = (Commodity) commodityHttpBO.getHttpEvent().getBaseModel1();
        //POS发送retrieveInventory请求。获得返回的数据
        //... 该方法会将返回的数据重新更新到本地，导致checkUPdate不通过
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, c);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&
                commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        List<Commodity> commList = (List<Commodity>) commodityHttpBO.getHttpEvent().getListMasterTable();

        Assert.assertTrue("创建的商品和查找出的不一样", commList.get(0).getNO() == 0); //新创建的商品是会把库存设置为0
    }

    //@Test POS端暂无该HTTP请求且pos端中暂不支持删除商品
    public void p_deleteAsync() {
        //...现阶段只是测试使用暂不实现 TODO
    }

    public Commodity createCommodity() throws Exception {
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //正常Case创建一个Commodity
        Commodity commodity = DataInput.getCommodity();
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        //
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("服务器返回的对象为空！", commodityHttpBO.getHttpEvent().getBaseModel1() != null);
        Commodity commodity1 = (Commodity) commodityHttpBO.getHttpEvent().getBaseModel1();
        return commodity1;
    }
}