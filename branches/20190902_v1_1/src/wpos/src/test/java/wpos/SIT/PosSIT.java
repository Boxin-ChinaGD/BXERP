package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.model.promotion.Promotion;
import wpos.presenter.*;
import wpos.utils.MD5Util;
import wpos.utils.Shared;

import javax.annotation.Resource;

import static wpos.utils.Shared.UNIT_TEST_TimeOut;

public class PosSIT extends BaseHttpTestCase {

//    private static CompanyPresenter companyPresenter;
    @Resource
    private CompanyHttpBO companyHttpBO;
    @Resource
    private CompanySQLiteBO companySQLiteBO;
    @Resource
    private CompanyHttpEvent companyHttpEvent;
    @Resource
    private CompanySQLiteEvent companySQLiteEvent;
    @Resource
    private ShopHttpBO shopHttpBO;
    @Resource
    private ShopHttpEvent shopHttpEvent;

//    private static PosPresenter posPresenter;
    @Resource
    private PosHttpBO posHttpBO;
    @Resource
    private PosSQLiteBO posSQLiteBO;
    @Resource
    private PosHttpEvent posHttpEvent;
    @Resource
    private PosSQLiteEvent posSQLiteEvent;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

//    private static BarcodesPresenter barcodesPresenter;
    @Resource
    private BarcodesHttpBO barcodesHttpBO;
    @Resource
    private BarcodesSQLiteBO barcodesSQLiteBO;
    @Resource
    private BarcodesHttpEvent barcodesHttpEvent;
    @Resource
    private BarcodesSQLiteEvent barcodesSQLiteEvent;

//    private static BrandPresenter brandPresenter;
    @Resource
    private BrandHttpBO brandHttpBO;
    @Resource
    private BrandSQLiteBO brandSQLiteBO;
    @Resource
    private BrandHttpEvent brandHttpEvent;
    @Resource
    private BrandSQLiteEvent brandSQLiteEvent;

//    private static CommodityCategoryPresenter commodityCategoryPresenter;
    @Resource
    private CommodityCategoryHttpBO commodityCategoryHttpBO;
    @Resource
    private CommodityCategorySQLiteBO commodityCategorySQLiteBO;
    @Resource
    private CommodityCategoryHttpEvent commodityCategoryHttpEvent;
    @Resource
    private CommodityCategorySQLiteEvent commodityCategorySQLiteEvent;

//    private static PackageUnitPresenter packageUnitPresenter;
    @Resource
    private PackageUnitHttpBO packageUnitHttpBO;
    @Resource
    private PackageUnitSQLiteBO packageUnitSQLiteBO;
    @Resource
    private PackageUnitHttpEvent packageUnitHttpEvent;
    @Resource
    private PackageUnitSQLiteEvent packageUnitSQLiteEvent;

//    private static CommodityPresenter commodityPresenter;
    @Resource
    private CommodityHttpBO commodityHttpBO;
    @Resource
    private CommoditySQLiteBO commoditySQLiteBO;
    @Resource
    private CommodityHttpEvent commodityHttpEvent;
    @Resource
    private CommoditySQLiteEvent commoditySQLiteEvent;
    @Resource
    private PromotionHttpBO promotionHttpBO;
    @Resource
    private PromotionSQLiteBO promotionSQLiteBO;
    @Resource
    private PromotionSQLiteEvent promotionSQLiteEvent;
    @Resource
    private ConfigGeneralHttpBO configGeneralHttpBO;
    @Resource
    private ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;

    private static final int Event_ID_PosSITTest = 10000;

    Company company = new Company();
    Pos pos = new Pos();

    private String STATUS = "";
    private String DONE = "DONE";
    private String staffPhone = "13888888888";
    private String staffPasswordInPos = "000000";

    private int count;//call ??????Action???RN???????????????
    private int barcodesRunTimes = 1;//????????????runTimes??????????????????????????????????????????
    private int commodityRunTimes = 1;//??????

    @BeforeClass
    public void setUp() {
        super.setUp();
        //
//        companyPresenter = GlobalController.getInstance().getCompanyPresenter();
        companyHttpEvent.setId(Event_ID_PosSITTest);
        companySQLiteEvent.setId(Event_ID_PosSITTest);
        companyHttpBO.setHttpEvent(companyHttpEvent);
        companyHttpBO.setSqLiteEvent(companySQLiteEvent);
        companySQLiteBO.setHttpEvent(companyHttpEvent);
        companySQLiteBO.setSqLiteEvent(companySQLiteEvent);
        companyHttpEvent.setHttpBO(companyHttpBO);
        companyHttpEvent.setSqliteBO(companySQLiteBO);
        companySQLiteEvent.setHttpBO(companyHttpBO);
        companySQLiteEvent.setSqliteBO(companySQLiteBO);
        //
        shopHttpEvent.setId(Event_ID_PosSITTest);
        shopHttpBO.setHttpEvent(shopHttpEvent);
        shopHttpEvent.setHttpBO(shopHttpBO);
        //
//        posPresenter = GlobalController.getInstance().getPosPresenter();
        posHttpEvent.setId(Event_ID_PosSITTest);
        posSQLiteEvent.setId(Event_ID_PosSITTest);
        posHttpBO.setHttpEvent(posHttpEvent);
        posHttpBO.setSqLiteEvent(posSQLiteEvent);
        posSQLiteBO.setHttpEvent(posHttpEvent);
        posSQLiteBO.setSqLiteEvent(posSQLiteEvent);
        posHttpEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        //
        posLoginHttpEvent.setId(Event_ID_PosSITTest);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(Event_ID_PosSITTest);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        barcodesHttpEvent.setId(Event_ID_PosSITTest);
        barcodesSQLiteEvent.setId(Event_ID_PosSITTest);
        barcodesHttpBO.setHttpEvent(barcodesHttpEvent);
        barcodesHttpBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesSQLiteBO.setHttpEvent(barcodesHttpEvent);
        barcodesSQLiteBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        //
//        brandPresenter = GlobalController.getInstance().getBrandPresenter();
        brandHttpEvent.setId(Event_ID_PosSITTest);
        brandSQLiteEvent.setId(Event_ID_PosSITTest);
        brandHttpBO.setHttpEvent(brandHttpEvent);
        brandHttpBO.setSqLiteEvent(brandSQLiteEvent);
        brandSQLiteBO.setHttpEvent(brandHttpEvent);
        brandSQLiteBO.setSqLiteEvent(brandSQLiteEvent);
        brandHttpEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        //
//        commodityCategoryPresenter = GlobalController.getInstance().getCommodityCategoryPresenter();
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
        }
        commodityCategoryHttpEvent.setId(Event_ID_PosSITTest);
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
        }
        commodityCategorySQLiteEvent.setId(Event_ID_PosSITTest);
        commodityCategoryHttpBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategoryHttpBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategorySQLiteBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategorySQLiteBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        //
//        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        packageUnitHttpEvent.setId(Event_ID_PosSITTest);
        packageUnitSQLiteEvent.setId(Event_ID_PosSITTest);
        packageUnitHttpBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitHttpBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitSQLiteBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitSQLiteBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        //
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        commodityHttpEvent.setId(Event_ID_PosSITTest);
        commoditySQLiteEvent.setId(Event_ID_PosSITTest);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        ;
        //
        promotionSQLiteEvent.setId(Event_ID_PosSITTest);
        promotionHttpBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        ;
        //
        configGeneralSQLiteEvent.setId(Event_ID_PosSITTest);
        configGeneralHttpBO.setSqLiteEvent(configGeneralSQLiteEvent);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        //
        logoutHttpEvent.setId(Event_ID_PosSITTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    public static class DataInput {
        private static Company companyInput = null;
        private static Shop shopInput = null;
        private static Pos posInput = null;

        private static final Company getCompany() throws CloneNotSupportedException {
            companyInput = new Company();
            companyInput.setBusinessLicenseSN("FB2" + (int) ((Math.random() * 9 + 1) * 10));
            companyInput.setBusinessLicensePicture("nbr_" + (int) ((Math.random() * 9 + 1) * 10) + ".jpg");
            companyInput.setBossPhone("152007" + (int) ((Math.random() * 9 + 1) * 10000));
            companyInput.setDbName("db" + (int) ((Math.random() * 9 + 1) * 100));
            companyInput.setBossWechat("w" + (int) ((Math.random() * 9 + 1) * 10000));
            companyInput.setKey(String.valueOf(((Math.random() * 9 + 1) * 10000)));
            companyInput.setBossPassword("000000");
            companyInput.setName("name" + (int) ((Math.random() * 9 + 1) * 1000));
            companyInput.setBossName("bname" + (int) ((Math.random() * 9 + 1) * 10));
            companyInput.setDbUserName(companyInput.DBUserName_Default);
            companyInput.setDbUserPassword(companyInput.DBUserPassword_Default);

            return (Company) companyInput.clone();
        }

        private static final Shop getShop() {
            shopInput = new Shop();
            shopInput.setName("BX" + (int) ((Math.random() * 9 + 1) * 10000));
            shopInput.setAddress("address" + (int) ((Math.random() * 9 + 1) * 10000));
            shopInput.setStatus(0);
            shopInput.setLongitude(123.321d);
            shopInput.setLatitude(321.123d);
            shopInput.setKey("123456");

            return (Shop) shopInput.clone();
        }

        public static final Pos getPos() throws CloneNotSupportedException, InterruptedException {
            posInput = new Pos();
            posInput.setPwdEncrypted("");
            posInput.setStatus(Pos.EnumStatusPos.ESP_Active.getIndex());
            posInput.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
            Thread.sleep(1);
            posInput.setPasswordInPOS("000000");
            posInput.setShopID(1);
            posInput.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT));
            posInput.setReturnObject(1);
            Thread.sleep(1);

            return (Pos) posInput.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCompanyHttpEvent(CompanyHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCompanySQLiteEvent(CompanySQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }

        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            company = (Company) event.getBaseModel1();
            if (company == null) {
                //???????????????????????????SN???POS????????????
                callRetrieve1BySN();
            } else {
                Constants.MyCompanySN = company.getSN();
                //?????????????????????pos????????????
                System.out.println("????????????company SN???????????????Pos????????????");
                retrievePasswordInPosInSQLite();
            }
        }

        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Company c = (Company) event.getBaseModel1();
            System.out.println("???????????????SN???" + c.getSN());
            Constants.MyCompanySN = c.getSN();
            //?????????????????????pos????????????
            System.out.println("???????????????company SN?????????????????????Pos????????????");
            retrievePasswordInPosInSQLite();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onShopHttpEvent(ShopHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }

        if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue(false,"????????????????????????");
        }

        if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_POS_Retrieve1BySN && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //???????????????????????????
            Company c = (Company) event.getBaseModel2();
            insertCompanyInformation(c);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }

        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Pos p = (Pos) event.getBaseModel1();
            if (p != null) {
                //??????????????????????????????
                pos = p;
                STATUS = DONE;
            } else {
                //??????POS????????????????????????????????????????????????????????????SQLite
                Pos p1 = (Pos) posHttpEvent.getBaseModel1();
                if (p1 == null) {
                    callRetrieve1BySN();
                } else {
                    createReplacerPos(p1);
                }
            }
        }

        if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            pos = (Pos) event.getBaseModel1();
            STATUS = DONE;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (barcodesHttpEvent.getCount() != null && !"".equals(barcodesHttpEvent.getCount())) {
            Barcodes barcodes = new Barcodes();
            barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            count = Integer.parseInt(barcodesHttpEvent.getCount());
            System.out.println("Barcodes????????????" + count);
            int totalPageIndex = count % Integer.valueOf(barcodes.getPageSize()) != 0 ? count / Integer.valueOf(barcodes.getPageSize()) + 1 : count / Integer.valueOf(barcodes.getPageSize());
            if (barcodesRunTimes < totalPageIndex) {
                barcodes.setPageIndex(String.valueOf(++barcodesRunTimes));
                retrieveNCBarcodes(barcodes);
            } else {
                //??????Brand
                Brand brand = new Brand();
                brand.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                brand.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                retrieveNCBrand(brand);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            //??????CommodityCategory
            CommodityCategory commodityCategory = new CommodityCategory();
            commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            retrieveNCCommodityCategory(commodityCategory);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQliteEvent(CommodityCategorySQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            //??????PackageUnit
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            retrieveNCPackageUnit(packageUnit);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitSQliteEvent(PackageUnitSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            //??????Commodity
            Commodity commodity = new Commodity();
            commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            commodity.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            retrieveNCCommodity(commodity);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (commodityHttpEvent.getCount() != null && !"".equals(commodityHttpEvent.getCount())) {
            Commodity commodity = new Commodity();
            commodity.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            count = Integer.valueOf(commodityHttpEvent.getCount());
            System.out.println("??????????????????" + count);
            int totalPageIndex = count % Integer.valueOf(commodity.getPageSize()) != 0 ? count / Integer.valueOf(commodity.getPageSize()) + 1 : count / Integer.valueOf(commodity.getPageSize());//??????????????????totalPageIndex???????????????
            if (commodityRunTimes < totalPageIndex) {
                commodity.setPageIndex(String.valueOf(++commodityRunTimes));
                retrieveNCCommodity(commodity);
            } else {
                //??????Promotion.
                Promotion promotion = new Promotion();
                promotion.setSubStatusOfStatus(4);
                promotion.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                promotion.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                retrieveNCPromotion(promotion); //...
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }

        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

            System.out.println("??????????????????????????????????????????????????????");
            retrieveNConfigGeneral();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_PosSITTest) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            //??????POS????????????????????????
            deletePosAsync(pos);
        }
    }

    /**
     * 1.???????????????????????????????????????
     * 2.??????POS???
     * 3.????????????????????????Company SN???POS???????????????,?????????????????????????????????Company???SN???POS???????????????
     * 4.??????????????????
     * 5.??????????????????
     *
     * @throws InterruptedException
     */
//    @Test
//    public void testPos() throws InterruptedException, CloneNotSupportedException {
//        Shared.printTestMethodStartInfo();
//
//        //1.????????????
//        System.out.println("--------------------------1.????????????---------------------------");
//        //
//        Shared.login(1l, "13185246281", "000000", 1, "668866");
//        Company company = DataInput.getCompany();
//        //
//        companyHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!companyHttpBO.createAsync(company)) {
//            Assert.assertTrue("??????create Company?????????", false);
//        }
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (companyHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (companyHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("create Company?????????", false);
//        }
//        Assert.assertTrue("create Company??????????????????????????????", companyHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Company c = (Company) companyHttpEvent.getBaseModel1();
//        Assert.assertTrue("????????????????????????????????????null", c != null);
//        //
//        Long companyID = c.getID();
//        long shopID = Long.parseLong(companyHttpEvent.getString1());
//
//        //2.????????????company?????????boss?????????company SN?????????
//        System.out.println("-------------------------2.????????????company?????????boss?????????company SN??????-----------------------");
//        String bossPhone = c.getBossPhone();
//        String bossPassword = c.getBossPassword();
//        Shared.login(1, bossPhone, bossPassword, 0, c.getSN());
//
//        //2.??????POS???,?????????????????????
//        System.out.println("------------------------------3.??????POS???,?????????????????????--------------------------------");
//        //
//        Pos pos1 = DataInput.getPos();
//        pos1.setShopID(shopID);
//        pos1.setInt1(1);
//        Constants.MyPosSN = pos1.getPos_SN();
//        System.out.println("POS???SN5555555555555555555555555555555555555555555 ???" + Constants.MyPosSN);
//        Pos p = createPos(pos1);
//        //???????????????
//        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
//        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        posSQLiteBO.createAsync(Constants.INVALID_CASE_ID, p);
//        lTimeOut = UNIT_TEST_TimeOut;
//        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("??????POS??????????????????", false);
//        }
//
//        //3.????????????????????????Company SN???POS???????????????,?????????????????????????????????Company???SN???POS???????????????
//        System.out.println("-------------4.????????????????????????Company SN???POS???????????????,?????????????????????????????????Company???SN???POS???????????????------------");
//        retrieveCompanyInSQLite();
//        lTimeOut = 60;
//        while (!DONE.equals(STATUS) && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (!DONE.equals(STATUS)) {
//            Assert.assertTrue("?????????????????????SN???POS????????????????????????", false);
//        }
//
//        //4.??????????????????
//        System.out.println("-------------------------5.??????????????????--------------------------");
//        //POS??????
//        posLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        posLoginHttpEvent.setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
//        posLoginHttpBO.setPos(pos);
//        posLoginHttpBO.loginAsync();
//        lTimeOut = 60;
//        while (posLoginHttpEvent.getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
//            if (posLoginHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpEvent.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
//        if (posLoginHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("pos???????????????", false);
//        }
//        //Staff??????
//        staffLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        staffLoginHttpEvent.setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
//        Staff currentStaff = new Staff();
//        currentStaff.setPhone(staffPhone);
//        currentStaff.setPasswordInPOS(staffPasswordInPos);
//        currentStaff.setInt3(1);
//        staffLoginHttpBO.setCurrentStaff(currentStaff);
//        staffLoginHttpBO.loginAsync();
//        lTimeOut = 60;
//        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
//            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
//        if (staffLoginHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("Staff???????????????", false);
//        }
//
//        //5.??????????????????
//        System.out.println("---------------------------6.??????????????????---------------------------");
//        Barcodes barcodes = new Barcodes();
//        barcodes.setPageSize("15");
//        barcodes.setPageIndex(BaseModel.FIRST_PAGE_Index_Default);
//    }

    /**
     * 1.pos,OP??????
     * 2.??????POS1
     * 3.????????????????????????POS1????????????
     * 4.??????POS1??????int1 ??? 1
     * 5.????????????
     * 6.??????????????????????????????????????????????????????
     * 7.???????????????POS???Company?????????
     */
    @Test
    public void test_a_getPosPasswordByCompanySN() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1.pos,OP??????
        if (!Shared.login(1,Shared.OP_Phone,Shared.PASSWORD_DEFAULT, BaseModel.EnumBoolean.EB_Yes.getIndex(),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex())) {
            System.out.println(posLoginHttpBO.getHttpEvent().getStatus() + "\t" + staffLoginHttpBO.getHttpEvent().getStatus());
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //2.??????POS1
        Pos pos = DataInput.getPos();
        pos.setCompanySN("668866");
        pos.setReturnObject(1);
        Pos createPos = createPos(pos);

        //3.????????????????????????POS1????????????
        Constants.MyPosSN = createPos.getPos_SN();
        Pos bm = new Pos();
        bm.setCompanySN(pos.getCompanySN());
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_ToDo,"????????????????????????");

        //4.??????POS1??????int1 ??? 1
        createPos.setPasswordInPOS(((Pos) posHttpEvent.getBaseModel1()).getPasswordInPOS());
        createPos.setResetPasswordInPos(1);
        if (!Shared.login(createPos, pos.getCompanySN(), posLoginHttpBO, staffLoginHttpBO)) {  //...
            Assert.assertTrue(false,"POS???????????????");
        }

        //5.????????????
        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("??????????????????! ");
        }
        lTimeOut = UNIT_TEST_TimeOut;
        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_ToDo,"??????????????????!");

        //6.??????????????????????????????????????????????????????
        Constants.MyPosSN = createPos.getPos_SN();
        bm = new Pos();
        bm.setCompanySN(pos.getCompanySN());
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_ToDo,"????????????????????????");
        System.out.println(posHttpEvent.getLastErrorCode());
        Assert.assertTrue(posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError,"?????????????????????????????????????????????????????????,????????????OtherError");
    }

    /**
     * 1.pos,OP??????
     * 2.??????POS1
     * 3.???????????????????????????????????????POS1????????????
     */
    @Test
    public void test_b_getPosPasswordByCompanySN() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1.pos,OP??????
        if (!Shared.login(1,Shared.OP_Phone,Shared.PASSWORD_DEFAULT, BaseModel.EnumBoolean.EB_Yes.getIndex(),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //2.??????POS1
        Pos pos = DataInput.getPos();
        pos.setCompanySN("668866");
        pos.setInt1(1);
        Pos createPos = createPos(pos);

        //3.????????????????????????POS1????????????
        Constants.MyPosSN = "";
        Pos bm = new Pos();
        bm.setCompanySN(pos.getCompanySN());
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done,"????????????????????????");
        Assert.assertTrue(posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData,"???????????????????????????????????????");
    }

    @Test
    public void test_c_getPosPasswordByCompanySN() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1.pos,OP??????
        if (!Shared.login
                (1,Shared.OP_Phone,Shared.PASSWORD_DEFAULT,BaseModel.EnumBoolean.EB_Yes.getIndex
                        (),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //2.??????POS1
        Pos pos = DataInput.getPos();
        pos.setCompanySN("668866");
        pos.setInt1(1);
        Pos createPos = createPos(pos);

        //3.????????????????????????POS1????????????
        Constants.MyPosSN = "       ";
        Pos bm = new Pos();
        bm.setCompanySN(pos.getCompanySN());
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done,"????????????????????????");
        Assert.assertTrue(posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData,"???????????????????????????????????????");
    }

    @Test
    public void test_d_getPosPasswordByCompanySN() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1.pos,OP??????
        if (!Shared.login
                (1,Shared.OP_Phone,Shared.PASSWORD_DEFAULT,BaseModel.EnumBoolean.EB_Yes.getIndex
                        (),Shared.nbr_CompanySN,posLoginHttpBO,staffLoginHttpBO,BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //2.??????POS1
        Pos pos = DataInput.getPos();
        pos.setCompanySN("668866");
        pos.setInt1(1);
        Pos createPos = createPos(pos);

        //3.????????????????????????POS1????????????
        Constants.MyPosSN = "aaaaaaa";
        Pos bm = new Pos();
        bm.setCompanySN(pos.getCompanySN());
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(posHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done,"????????????????????????");
        Assert.assertTrue(posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData,"???????????????????????????????????????");
    }

    private Pos createPos(Pos pos) throws InterruptedException {
        //POS create
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, pos)) {
            Assert.assertTrue(false,"create Pos?????????");
        }
        long lTimeOut = 60;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"create pos?????????");
        }
        Assert.assertTrue(posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"create pos???????????????????????????????????????posHttpEvent.getLastErrorCode() = " + posHttpEvent.getLastErrorCode());
        //
        Pos p = (Pos) posHttpEvent.getBaseModel1();
        Assert.assertTrue(p != null,"????????????????????????????????????null");
        posHttpEvent.setBaseModel1(null);
        return p;
    }

    /**
     * ?????????????????????SN????????????
     *
     * @throws InterruptedException
     */
    private void retrieveCompanyInSQLite() throws InterruptedException {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companySQLiteBO.retrieveNASync(BaseSQLiteBO.INVALID_CASE_ID, null);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            System.out.println("????????????Company?????????");
        }
    }

    /**
     * ????????????SN???pos SN????????????????????????????????????POS???????????????
     *
     * @throws InterruptedException
     */
    private void callRetrieve1BySN() throws InterruptedException {
        Pos bm = new Pos();
        bm.setCompanySN("668866");
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, bm);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"????????????company SN???POS??????????????????????????????");
        }
    }

    /**
     * ???????????????????????????
     *
     * @param c
     * @throws InterruptedException
     */
    private void insertCompanyInformation(Company c) throws InterruptedException {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, c);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"???company???????????????????????????");
        }
    }

    /**
     * ???????????????POS???????????????
     *
     * @throws InterruptedException
     */
    private void retrievePasswordInPosInSQLite() throws InterruptedException {
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false,"????????????POS?????????????????????");
        }
    }

    /**
     * ???????????????pos???????????????
     *
     * @param p
     * @throws InterruptedException
     */
    private void createReplacerPos(Pos p) throws InterruptedException {
        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        posSQLiteBO.createReplacerAsync(BaseSQLiteBO.INVALID_CASE_ID, p);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"????????????POS?????????????????????");
        }
    }

    private void retrieveNCBarcodes(Barcodes barcodes) throws InterruptedException {
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.assertTrue(false,"??????barcodes?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????Barcodes?????????");
        }
    }

    private void retrieveNCBrand(Brand brand) throws InterruptedException {
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!brandHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false,"??????Brand?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????Brand?????????");
        }
    }

    private void retrieveNCCommodityCategory(CommodityCategory commodityCategory) throws InterruptedException {
        commodityCategorySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false,"??????CommodityCategory?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????CommodityCategory?????????");
        }
    }

    private void retrieveNCPackageUnit(PackageUnit packageUnit) throws InterruptedException {
        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            Assert.assertTrue(false,"??????PackageUnit?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????PackageUnit?????????");
        }
    }

    private void retrieveNCCommodity(Commodity commodity) throws InterruptedException {
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue(false,"??????Commodity?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????Commodity?????????");
        }
    }

    private void retrieveNCPromotion(Promotion promotion) throws InterruptedException {
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!promotionHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue(false,"??????Promotion?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????Promotion?????????");
        }
    }

    private void retrieveNConfigGeneral() throws InterruptedException {
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false,"??????ConfigGeneral?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????ConfigGeneral??????");
        }
    }

    private void deletePosAsync(Pos pos) throws InterruptedException {
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, pos)) {
            Assert.assertTrue(false,"??????Pos?????????");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"??????Pos?????????");
        }
    }
}
