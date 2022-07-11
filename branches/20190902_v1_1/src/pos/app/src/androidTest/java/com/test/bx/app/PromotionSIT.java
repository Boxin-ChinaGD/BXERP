package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.Promotion.EnumStatusPromotion;
import com.bx.erp.model.Promotion.EnumTypePromotion;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.PromotionScopePresenter;
import com.bx.erp.promotion.BasePromotion;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

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

public class PromotionSIT extends BaseHttpAndroidTestCase {
    private static PromotionPresenter promotionPresenter = null;
    private static CommodityPresenter commodityPresenter = null;
    private static PromotionSQLiteBO promotionSqLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionSQLiteEvent promotionSqLiteEvent = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;
    //
    private static PromotionScopePresenter promotionScopePresenter = null;

    private static final int Event_ID_PromotionSITTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (promotionPresenter == null) {
            promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        }
        if (promotionSqLiteEvent == null) {
            promotionSqLiteEvent = new PromotionSQLiteEvent();
            promotionSqLiteEvent.setId(Event_ID_PromotionSITTest);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(Event_ID_PromotionSITTest);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSqLiteEvent, promotionHttpEvent);
        }
        if (promotionSqLiteBO == null) {
            promotionSqLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSqLiteEvent, promotionHttpEvent);
        }
        promotionSqLiteEvent.setHttpBO(promotionHttpBO);
        promotionSqLiteEvent.setSqliteBO(promotionSqLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSqLiteBO);
        //
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_PromotionSITTest);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(Event_ID_PromotionSITTest);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_PromotionSITTest);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
//        commodityHttpEvent = GlobalController.getInstance().getCommodityHttpEvent();
//        commoditySQLiteEvent = GlobalController.getInstance().getCommoditySQLiteEvent();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_PromotionSITTest);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_PromotionSITTest);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (promotionScopePresenter == null) {
            promotionScopePresenter = GlobalController.getInstance().getPromotionScopePresenter();
        }

        //1,2步，登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.同步商品信息
        System.out.println("调用同步商品方法");
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);//...将来优化成，所有测试启动前，先将所有商品同步
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);
        Shared.retrieveNCBaseModel(commodity, commodityHttpBO, commoditySQLiteEvent, commoditySQLiteBO);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    private int count;//call 普通action，返回的商品总数
    private int commodityRunTimes = 1;//需要运行runTimes次，才能把商品全部同步下来
    private int barcodesRunTimes = 1;//需要运行runTimes次，才能把条形码全部同步下来

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEventEvent(CommoditySQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_a_promotion() throws Exception {
        Shared.printTestMethodStartInfo();

        //创建促销集合
        List<BaseModel> promotionList = new ArrayList<>();
        //添加一个满10打8折的活动
        Promotion promotion1 = DataInput.getDiscountPromotionAllCommodity2();
        promotion1.setID(1L);
        promotion1.setDatetimeStart(DatetimeUtil.addDays(new Date(), -1));
        promotionList.add(promotion1);

        //查出参加促销活动的商品
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        List<Commodity> commodityList = new ArrayList<Commodity>();
        Commodity baseModel = new Commodity();
        baseModel.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        baseModel.setSql("where F_id in (?,?,?,?)");
        baseModel.setConditions(new String[]{"162", "161", "160", "159"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, baseModel);
        Assert.assertTrue("从本地取出的Commodity数据为空", commodityList.size() > 0);
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(1);
        }
        //计算零售单的应收款、退货价

        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        RetailTrade sellRetailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        /*
        商品A的价格是30，商品B的价格是43，商品C的价格是38，商品D的价格是38
        参与的促销活动规则全场满10打八折，所以算出来的应收款应该是（30 + 43 + 38*2）* 0.8 = 119.19999694824219
        */
        double afterPromotion = sellRetailTrade.getAmount();
        double beforePromotion = (commodityList.get(3).getPriceRetail() + commodityList.get(2).getPriceRetail() + commodityList.get(1).getPriceRetail() + commodityList.get(0).getPriceRetail()) * 0.8;
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion, beforePromotion));
        Assert.assertTrue("验证失败，促销计算的价格为:" + afterPromotion + ",手动计算的价格为:" + beforePromotion, GeneralUtil.sub(afterPromotion, beforePromotion) <= baseModel.TOLERANCE);
        System.out.println("Case2 --------------------------------------------------------------------------------------------------------------");
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(2);
        }
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        RetailTrade sellRetailTrade2 = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting2);
        System.out.println(sellRetailTrade2.getAmount());

        /*
        商品A的价格是30，商品B的价格是43，商品C的价格是38，商品D的价格是38
        参与的促销活动规则全场满10打八折，所以算出来的应收款应该是（30*2 + 43*2 + 38*4）* 0.8 = 238.39999389648438
        */
        Assert.assertTrue("验证失败", sellRetailTrade2.getAmount() - (commodityList.get(3).getPriceRetail() * 2 + commodityList.get(2).getPriceRetail() * 2 + commodityList.get(1).getPriceRetail() * 2 + commodityList.get(0).getPriceRetail() * 2) * 0.8 <= baseModel.TOLERANCE);

        System.out.println("Case3 --------------------------------------------------------------------------------------------------------------");
        //创建一个指定商品参与活动的促销
        Promotion promotion2 = DataInput.getDiscountPromotionSpecifiedCommodity2();
        promotion2.setCommodityIDs("161,162");
        promotion2.setID(2L);
        promotion2.setScope(1);//设置指定商品促销
        promotion2.setDatetimeStart(DatetimeUtil.addDays(new Date(), -1));
        //创建从表
        PromotionScope promotionScope = new PromotionScope();
        promotionScope.setCommodityID(161);
        PromotionScope promotionScope2 = new PromotionScope();
        promotionScope2.setCommodityID(162);
        List<PromotionScope> promotionScopeList = new ArrayList<PromotionScope>();
        promotionScopeList.add(promotionScope);
        promotionScopeList.add(promotionScope2);
        promotion2.setListSlave1(promotionScopeList);
        //
        promotionList.add(promotion2);
        //
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(4);
            System.out.println("ssssssssssssss"+commodity1);
        }
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        RetailTrade sellRetailTrade3 = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting3);
        System.out.println("系统计算的价格"+sellRetailTrade3.getAmount());
        /*
        商品A的价格是30，商品B的价格是43，商品C的价格是38，商品D的价格是38
        参与的促销活动：指定商品满200打七折，全场满10打八折，所以算出来的应收款应该是（30*4 + 43*4 + （38*8 * 0.7））* 0.8 = 403.8399963378906
        */
        System.out.println("商品A的价格是"+commodityList.get(3).getPriceRetail()+"商品B的价格是"+commodityList.get(2).getPriceRetail()+"商品C的价格是"+commodityList.get(1).getPriceRetail()+"    "+commodityList.get(0).getPriceRetail());
        System.out.println("应收款:" + String.valueOf( (commodityList.get(3).getPriceRetail() * 4 + commodityList.get(2).getPriceRetail() * 4 + (commodityList.get(1).getPriceRetail() * 4 + commodityList.get(0).getPriceRetail() * 4) * 0.7) * 0.8));
        Assert.assertTrue("验证失败", sellRetailTrade3.getAmount() - (commodityList.get(1).getPriceRetail() * 4 + commodityList.get(0).getPriceRetail() * 4 + (commodityList.get(3).getPriceRetail() * 4 + commodityList.get(2).getPriceRetail() * 4) * 0.7) * 0.8 <= baseModel.TOLERANCE);

        System.out.println("Case4 ---------------------------------------------------------------------------------------------------------------- ");

        RetailTrade sellRetailTrade4 = promotionCalculator.sell(commodityList, null, retailTradePromoting3);
        System.out.println(sellRetailTrade4.getAmount());
        /*
        商品A的价格是30，商品B的价格是43，商品C的价格是38，商品D的价格是38
        没有任何促销活动：所以算出来的应收款应该是 30*4 + 43*4 + （38*8） = 596
        */
        Assert.assertTrue("验证失败", sellRetailTrade4.getAmount() - (commodityList.get(3).getPriceRetail() * 4 + commodityList.get(2).getPriceRetail() * 4 + commodityList.get(1).getPriceRetail() * 4 + commodityList.get(0).getPriceRetail() * 4) <= baseModel.TOLERANCE);
    }


    /**
     * 1,2步，登录POS和Staff
     * <p>
     * 普通商品：164 零售价：38
     * 多包装商品：165 零售单：38
     * 服务商品：166 零售价：38
     * 组合商品：171 零售价：100
     *
     * @throws Exception
     */
    @Test
    public void test_b_promotion() throws Exception {
        Shared.printTestMethodStartInfo();

        //创建促销集合
        List<BaseModel> promotionList = new ArrayList<>();
        //添加一个满10打8折的活动
        Promotion promotion1 = DataInput.getDiscountPromotionAllCommodity2();
        promotion1.setID(1L);
        promotion1.setDatetimeStart(DatetimeUtil.addDays(new Date(), -1));
        promotionList.add(promotion1);

        //查出参加促销活动的商品
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        List<Commodity> commodityList = new ArrayList<Commodity>();
        Commodity baseModel = new Commodity();
        baseModel.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        baseModel.setSql("where F_id in (?,?,?,?)");
        baseModel.setConditions(new String[]{"164", "165", "166", "171"});
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, baseModel);
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(1);
        }

        caseLog("单品参与促销");
        List<Commodity> normalCommodityList = new ArrayList<>();
        normalCommodityList.add(commodityList.get(0));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        RetailTrade sellRetailTrade = promotionCalculator.sell(normalCommodityList, promotionList, retailTradePromoting);
        /*
        普通商品零售价：￥38
        参与的促销活动规则全场满10打八折，所以算出来的应收款应该是38 * 0.8 = 30.4
        */
        double afterPromotion = sellRetailTrade.getAmount();
        double beforePromotion = commodityList.get(0).getPriceRetail() * 0.8;
        Assert.assertTrue("验证失败。促销计算后的价格(系统计算的价格：" + afterPromotion + ") - 促销手动计算的价格(手动计算的价格：" + commodityList.get(0).getPriceRetail() + " * 0.8) = " + GeneralUtil.sub(afterPromotion, beforePromotion),
                GeneralUtil.sub(afterPromotion, beforePromotion) <= baseModel.TOLERANCE);

        caseLog("多包装商品不参与促销");
        List<Commodity> pakacgeCommodityList = new ArrayList<>();
        pakacgeCommodityList.add(commodityList.get(1));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        RetailTrade sellRetailTrade2 = promotionCalculator.sell(pakacgeCommodityList, promotionList, retailTradePromoting2);
        /*
        多包装商品零售价：￥38 不参与促销，应收款为：￥38
        */
        double afterPromotion2 = sellRetailTrade2.getAmount();
        double beforePromotion2 = pakacgeCommodityList.get(0).getPriceRetail();
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion2, beforePromotion2));
        Assert.assertTrue("验证失败", GeneralUtil.sub(afterPromotion2, beforePromotion2) <= baseModel.TOLERANCE);

        caseLog("服务商品不参与促销");
        List<Commodity> serviceCommodityList = new ArrayList<>();
        serviceCommodityList.add(commodityList.get(2));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        RetailTrade sellRetailTrade3 = promotionCalculator.sell(serviceCommodityList, promotionList, retailTradePromoting3);
        /*
        服务商品零售价：￥38 不参与促销，应收款为：￥38
        */
        double afterPromotion3 = sellRetailTrade3.getAmount();
        double beforePromotion3 = serviceCommodityList.get(0).getPriceRetail();
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion3, beforePromotion3));
        Assert.assertTrue("验证失败", GeneralUtil.sub(afterPromotion3, beforePromotion3) <= baseModel.TOLERANCE);

        caseLog("组合商品不参与促销");
        List<Commodity> mutipleCommodityList = new ArrayList<>();
        mutipleCommodityList.add(commodityList.get(3));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting4 = new RetailTradePromoting();
        RetailTrade sellRetailTrade4 = promotionCalculator.sell(mutipleCommodityList, promotionList, retailTradePromoting4);
        /*
        组合商品零售价：￥100 不参与促销，应收款为：￥100
        */
        double afterPromotion4 = sellRetailTrade4.getAmount();
        double beforePromotion4 = mutipleCommodityList.get(0).getPriceRetail();
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion4, beforePromotion4));
        Assert.assertTrue("验证失败", GeneralUtil.sub(afterPromotion4, beforePromotion4) <= baseModel.TOLERANCE);

        caseLog("售卖多包装、服务商品、组合商品不参与促销");
        List<Commodity> mixCommodityList1 = new ArrayList<>();
        mixCommodityList1.add(commodityList.get(1));
        mixCommodityList1.add(commodityList.get(2));
        mixCommodityList1.add(commodityList.get(3));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting5 = new RetailTradePromoting();
        RetailTrade sellRetailTrade5 = promotionCalculator.sell(mixCommodityList1, promotionList, retailTradePromoting5);
        /*
        多包装商品、服务商品、组合商品零售价分别为：￥38 ￥38 ￥100 不参与促销，应收款为：￥38 + 38 + 100 = 176
        */
        double afterPromotion5 = sellRetailTrade5.getAmount();
        double beforePromotion5 = mixCommodityList1.get(0).getPriceRetail() + mixCommodityList1.get(1).getPriceRetail() + mixCommodityList1.get(2).getPriceRetail();
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion5, beforePromotion5));
        Assert.assertTrue("验证失败", GeneralUtil.sub(afterPromotion5, beforePromotion5) <= baseModel.TOLERANCE);

        caseLog("售卖单品、组合商品");
        List<Commodity> mixCommodityList2 = new ArrayList<>();
        mixCommodityList2.add(commodityList.get(0));
        mixCommodityList2.add(commodityList.get(3));
        //
        //计算零售单的应收款、退货价
        RetailTradePromoting retailTradePromoting6 = new RetailTradePromoting();
        RetailTrade sellRetailTrade6 = promotionCalculator.sell(mixCommodityList2, promotionList, retailTradePromoting6);
        /*
        普通商品、组合商品零售价分别为：￥38 ￥100 不参与促销，应收款为：38*0.8 + 100 = 130.4
        */
        double afterPromotion6 = sellRetailTrade6.getAmount();
        double beforePromotion6 = mixCommodityList2.get(0).getPriceRetail() * 0.8 + mixCommodityList2.get(1).getPriceRetail();
        System.out.println("促销计算后的价格 - 促销手动计算的价格 = " + GeneralUtil.sub(afterPromotion6, beforePromotion6));
        Assert.assertTrue("验证失败", GeneralUtil.sub(afterPromotion6, beforePromotion6) <= baseModel.TOLERANCE);

    }


    public static class DataInput {
        private static Promotion promotionInput = null;
        private static Random r = new Random();

        //全场商品111满100元打9折的促销
        public static final Promotion getDiscountPromotionSpecifiedCommodity() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场商品满100打9折");
            promotionInput.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(100);
            promotionInput.setExcecutionAmount(8);
            promotionInput.setExcecutionDiscount(0.9);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        //全场商品111满120元减20元的促销活动
        public static final Promotion getCashReducingPromotionAllCommodity() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场商品满120减20");
            promotionInput.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(120);
            promotionInput.setExcecutionAmount(20);
            promotionInput.setExcecutionDiscount(1);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        //指定商品进行满减促销(如指定商品A满200元减40元)
        public static final Promotion getCashReducingPromotionSpecifiedCommodity() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("指定商品满100-40");
            promotionInput.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(100);
            promotionInput.setExcecutionAmount(40);
            promotionInput.setExcecutionDiscount(1);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        //指定商品进行满金额折扣促销(如，指定商品A满200打7折)
        protected static final Promotion getDiscountPromotionSpecifiedCommodity2() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("指定商品满200打7折");
            promotionInput.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(200);
            promotionInput.setExcecutionAmount(40);
            promotionInput.setExcecutionDiscount(0.7f);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        //全场打9折
        public static final Promotion getDiscountPromotionAllCommodity() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场打9折");
            promotionInput.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(1);
            promotionInput.setExcecutionAmount(8);
            promotionInput.setExcecutionDiscount(0.9f);
            promotionInput.setScope(0);
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        //全场满10打8折
        public static final Promotion getDiscountPromotionAllCommodity2() throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场满10打8折");
            promotionInput.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(10);
            promotionInput.setExcecutionAmount(1);
            promotionInput.setExcecutionDiscount(0.8f);
            promotionInput.setScope(0);
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }


    }
}