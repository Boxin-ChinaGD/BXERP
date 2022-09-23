package wpos.presenter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import wpos.common.GlobalController;
import wpos.event.PosSQLiteEvent;
import wpos.event.PromotionSQLiteEvent;
import wpos.event.SmallSheetTextSQLiteEvent;
import wpos.event.UI.*;

import java.util.UUID;

public class BasePresenterTest extends BaseTestNGSpringContextTest {

    protected static boolean bMapperIsInitialized = false;

    VipPresenter vipPresenter;
    BarcodesPresenter barcodesPresenter;
    BrandPresenter brandPresenter;
    BXConfigGeneralPresenter bxConfigGeneralPresenter;
    SmallSheetTextPresenter smallSheetTextPresenter;
    SmallSheetFramePresenter smallSheetFramePresenter;
    StaffPresenter staffPresenter;
    CommodityPresenter commodityPresenter;
    CompanyPresenter companyPresenter;
    ConfigGeneralPresenter configGeneralPresenter;
    PackageUnitPresenter packageUnitPresenter;
    PosPresenter posPresenter;
    PromotionPresenter promotionPresenter;
    CouponPresenter couponPresenter;

    SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    SmallSheetTextSQLiteEvent smallSheetTextSQLiteEvent;
    BrandSQLiteEvent brandSQLiteEvent;
    BarcodesSQLiteEvent barcodesSQLiteEvent;
    BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent;
    CommoditySQLiteEvent commoditySQLiteEvent;
    CompanySQLiteEvent companySQLiteEvent;
    ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    PackageUnitSQLiteEvent packageUnitSQLiteEvent;
    PosSQLiteEvent posSQLiteEvent;
    PromotionSQLiteEvent promotionSQLiteEvent;
    RememberLoginStaffPresenter rememberLoginStaffPresenter;
    RetailTradeAggregationPresenter retailTradeAggregationPresenter;
    RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    RetailTradePresenter retailTradePresenter;
    RetailTradePromotingPresenter retailTradePromotingPresenter;
    RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;

    @BeforeClass
    public void setUp() {
        super.setUp();
        if (!bMapperIsInitialized) {
            GlobalController.init();
            initPresenter();
            // initMapMapper();
            bMapperIsInitialized = true;
        }
    }

    @AfterClass
    public void tearDown() {
        bMapperIsInitialized = false;
        super.tearDown();
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
        couponPresenter = (CouponPresenter) applicationContext.getBean("couponPresenter");

        barcodesSQLiteEvent = (BarcodesSQLiteEvent) applicationContext.getBean("barcodesSQLiteEvent");
        brandSQLiteEvent = (BrandSQLiteEvent) applicationContext.getBean("brandSQLiteEvent");
        bxConfigGeneralSQLiteEvent = (BXConfigGeneralSQLiteEvent) applicationContext.getBean("bXConfigGeneralSQLiteEvent");
        commoditySQLiteEvent = (CommoditySQLiteEvent) applicationContext.getBean("commoditySQLiteEvent");
        companySQLiteEvent = (CompanySQLiteEvent) applicationContext.getBean("companySQLiteEvent");
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
}
