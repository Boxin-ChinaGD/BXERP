package wpos.base;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import wpos.event.PosSQLiteEvent;
import wpos.event.PromotionSQLiteEvent;
import wpos.event.SmallSheetTextSQLiteEvent;
import wpos.event.UI.*;
import wpos.http.HttpRequestUnit;
import wpos.model.ErrorInfo;
import wpos.model.promotion.PromotionScope;
import wpos.presenter.*;
import wpos.utils.Shared;

import java.util.UUID;

public class BaseTestCase extends BaseTestNGSpringContextTest {

    public VipPresenter vipPresenter;
    public BarcodesPresenter barcodesPresenter;
    public BrandPresenter brandPresenter;
    public BXConfigGeneralPresenter bxConfigGeneralPresenter;
    public SmallSheetTextPresenter smallSheetTextPresenter;
    public SmallSheetFramePresenter smallSheetFramePresenter;
    public StaffPresenter staffPresenter;
    public static CommodityPresenter commodityPresenter;
    public CompanyPresenter companyPresenter;
    public ConfigGeneralPresenter configGeneralPresenter;
    public PackageUnitPresenter packageUnitPresenter;
    public PosPresenter posPresenter;
    public PromotionPresenter promotionPresenter;
    public PromotionScopePresenter promotionScopePresenter;
    public CouponPresenter couponPresenter;

    public SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    public SmallSheetTextSQLiteEvent smallSheetTextSQLiteEvent;
    public BrandSQLiteEvent brandSQLiteEvent;
    public BarcodesSQLiteEvent barcodesSQLiteEvent;
    public BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent;
    public CommoditySQLiteEvent commoditySQLiteEvent;
    public CompanySQLiteEvent companySQLiteEvent;
    public ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    public PackageUnitSQLiteEvent packageUnitSQLiteEvent;
    public PosSQLiteEvent posSQLiteEvent;
    public PromotionSQLiteEvent promotionSQLiteEvent;
    public RememberLoginStaffPresenter rememberLoginStaffPresenter;
    public RetailTradeAggregationPresenter retailTradeAggregationPresenter;
    public static RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    public static RetailTradePresenter retailTradePresenter;
    public RetailTradePromotingPresenter retailTradePromotingPresenter;
    public RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
    public RetailTradeCouponPresenter retailTradeCouponPresenter;
    public CommodityCategoryPresenter commodityCategoryPresenter;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        initPresenter();
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
    }

    /**
     * 等待http请求返回响应数据。
     *
     * @param hru
     * @return true，没有超时；false，超时。
     */
    protected boolean waitForHttpResponse(HttpRequestUnit hru) {
        long lTimeOut = hru.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1000 * 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (hru.hasHttpResponse()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 等待UI层处理完事件。一般情况下，必须先通过waitForHttpResponse()等待http请求返回响应数据，才调用本函数等待UI处理完返回的数据
     */
    protected boolean waitForEventProcessed(HttpRequestUnit hru) {
        long lTimeOut = hru.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1000 * 1;
            } catch (InterruptedException e) {
                hru.getEvent().setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
                e.printStackTrace();
            }
            if (hru.getEvent().isEventProcessed()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 等待UI层处理完事件。一般情况下，必须先通过waitForHttpResponse()等待http请求返回响应数据，才调用本函数等待UI处理完返回的数据
     */
    protected boolean waitForEventProcessed(BaseSQLiteEvent event) {
        long lTimeOut = event.getTimeout();
        while (lTimeOut > 0) {
            try {
                Thread.sleep(1000);
                lTimeOut -= 1l;
            } catch (InterruptedException e) {
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
                e.printStackTrace();
            }
            if (event.isEventProcessed()) {
                return true;
            }
        }
        return false;
    }

    public static void caseLog(String s) {
        System.out.println("\n---------------------------------------" + s + "-----------------------------------------------");
    }

    /**
     * 生成随机DB名称。它必须是以字母或下划线开头，和JAVA的变量命名一样
     */
    public static String GenerateDBName() {
        String dbName = "nbr_test_" + UUID.randomUUID().toString().substring(1, 7);
        System.out.println("生成的DB name为：" + dbName);
        return dbName;
    }

    /**
     * 生成一个合法的手机号码，以13开头，共11位
     */
    protected static String getValidStaffPhone() throws InterruptedException {
        String str = "%09d";
        String phone = String.format(str, System.currentTimeMillis() % 1000000000);
        Thread.sleep(1);
        return "13" + phone;
    }

    private void initPresenter() {
        vipPresenter = (VipPresenter) applicationContext.getBean("vipPresenter");
        barcodesPresenter = (BarcodesPresenter) applicationContext.getBean("barcodesPresenter");
        brandPresenter = (BrandPresenter) applicationContext.getBean("brandPresenter");
        bxConfigGeneralPresenter = (BXConfigGeneralPresenter) applicationContext.getBean("bXConfigGeneralPresenter");
        staffPresenter = (StaffPresenter) applicationContext.getBean("staffPresenter");
        smallSheetTextPresenter = (SmallSheetTextPresenter) applicationContext.getBean("smallSheetTextPresenter");
        smallSheetFramePresenter = (SmallSheetFramePresenter) applicationContext.getBean("smallSheetFramePresenter");
        commodityPresenter = (CommodityPresenter) applicationContext.getBean("commodityPresenter");
        companyPresenter = (CompanyPresenter) applicationContext.getBean("companyPresenter");
        configGeneralPresenter = (ConfigGeneralPresenter) applicationContext.getBean("configGeneralPresenter");
        packageUnitPresenter = (PackageUnitPresenter) applicationContext.getBean("packageUnitPresenter");
        posPresenter = (PosPresenter) applicationContext.getBean("posPresenter");
        promotionPresenter = (PromotionPresenter) applicationContext.getBean("promotionPresenter");
        promotionScopePresenter = (PromotionScopePresenter) applicationContext.getBean("promotionScopePresenter");
        couponPresenter = (CouponPresenter) applicationContext.getBean("couponPresenter");
        commodityCategoryPresenter = (CommodityCategoryPresenter) applicationContext.getBean("commodityCategoryPresenter");

        companySQLiteEvent = (CompanySQLiteEvent) applicationContext.getBean("companySQLiteEvent");
        barcodesSQLiteEvent = (BarcodesSQLiteEvent) applicationContext.getBean("barcodesSQLiteEvent");
        brandSQLiteEvent = (BrandSQLiteEvent) applicationContext.getBean("brandSQLiteEvent");
        bxConfigGeneralSQLiteEvent = (BXConfigGeneralSQLiteEvent) applicationContext.getBean("bXConfigGeneralSQLiteEvent");
        commoditySQLiteEvent = (CommoditySQLiteEvent) applicationContext.getBean("commoditySQLiteEvent");
        configGeneralSQLiteEvent = (ConfigGeneralSQLiteEvent) applicationContext.getBean("configGeneralSQLiteEvent");
        packageUnitSQLiteEvent = (PackageUnitSQLiteEvent) applicationContext.getBean("packageUnitSQLiteEvent");
        posSQLiteEvent = (PosSQLiteEvent) applicationContext.getBean("posSQLiteEvent");
        promotionSQLiteEvent = (PromotionSQLiteEvent) applicationContext.getBean("promotionSQLiteEvent");
        rememberLoginStaffPresenter = (RememberLoginStaffPresenter) applicationContext.getBean("rememberLoginStaffPresenter");
        retailTradeAggregationPresenter = (RetailTradeAggregationPresenter) applicationContext.getBean("retailTradeAggregationPresenter");
        retailTradeCommodityPresenter = (RetailTradeCommodityPresenter) applicationContext.getBean("retailTradeCommodityPresenter");
        retailTradePresenter = (RetailTradePresenter) applicationContext.getBean("retailTradePresenter");
        retailTradePromotingPresenter = (RetailTradePromotingPresenter) applicationContext.getBean("retailTradePromotingPresenter");
        retailTradePromotingFlowPresenter = (RetailTradePromotingFlowPresenter) applicationContext.getBean("retailTradePromotingFlowPresenter");
        smallSheetSQLiteEvent = (SmallSheetSQLiteEvent) applicationContext.getBean("smallSheetSQLiteEvent");
        smallSheetTextSQLiteEvent = (SmallSheetTextSQLiteEvent) applicationContext.getBean("smallSheetTextSQLiteEvent");
        retailTradeCouponPresenter = (RetailTradeCouponPresenter) applicationContext.getBean("retailTradeCouponPresenter");
    }
}
