package com.test.bx.app.robot;


import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PurchasingOrderHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.ReturnCommoditySheetHttpBO;
import com.bx.erp.bo.ReturnCommoditySheetSQLiteBO;
import com.bx.erp.bo.WarehousingHttpBO;
import com.bx.erp.bo.WarehousingSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.PurchasingOrderHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.VipCategorySQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipCategoryHttpEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.event.WarehousingHttpEvent;
import com.bx.erp.event.WarehousingSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Brand;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCategory;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.test.bx.app.robot.client.ClientHandler;
import com.test.bx.app.robot.client.RobotClient;
import com.test.bx.app.robot.program.Program;
import com.test.bx.app.robot.protocol.Header;

import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShopRobotTest extends BaseHttpAndroidTestCase {
    protected long lTimeout = 30 * 1000; //记录超时的时间
    public static RetailTradePromoting retailTradePromoting;

    protected List<RetailTrade> retailTradeList = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static ReturnCommoditySheetSQLiteEvent returnCommoditySheetSQLiteEvent = null;
    private static ReturnCommoditySheetSQLiteBO returnCommoditySheetSQLiteBO = null;
    private static ReturnCommoditySheetHttpBO returnCommoditySheetHttpBO = null;
    private static ReturnCommoditySheetHttpEvent returnCommoditySheetHttpEvent = null;
    private static WarehousingSQLiteEvent warehousingSQLiteEvent = null;
    private static WarehousingSQLiteBO warehousingSQLiteBO = null;
    private static WarehousingHttpEvent warehousingHttpEvent = null;
    private static WarehousingHttpBO warehousingHttpBO = null;
    private static PurchasingOrderHttpEvent purchasingOrderHttpEvent = null;
    private static PurchasingOrderHttpBO purchasingOrderHttpBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;


    private String syncPosIDs;//保存同步的Pos的ID，用于feedback时传参    下面定义的变量也是一样的作用
    private String syncRetailTradeIDs;
    private String syncSmallSheetIDs;
    private String syncVipCategoryIDs;
    private String syncVipIDs;
    private String syncPromotionIDS;

    private int count;//call 普通Action的RN，返回总数
    private int barcodesRunTimes = 1;//需要运行runTimes次，才能把条形码全部同步下来
    private int commodityRunTimes = 1;//同上
    public static boolean bStopClient = false;
    public static volatile boolean bProgramIsRunning = false;
    public static IoSession clientSession;
    public static volatile int activitySequence = 0;

    public static List<BaseModel> tempRetailTradeList = new ArrayList<BaseModel>();
    public static List<SmallSheetFrame> tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();
    public static List<RetailTradeAggregation> tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    private static CommodityPresenter commodityPresenter = null;

    private RobotConfig robotConfig;

    public static SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default2);

    public static boolean bIsDone = false;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();
        tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }
        if (commodityPresenter == null) {
            commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        }
        //
        Robot robot = new Robot();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        event.onEvent();
        //开机同步的情况下才会执行以下代码
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                //
                Robot.retrieveNVipCategory();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) throws InterruptedException {
        //开机同步的情况下才会执行以下代码
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0);//...
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done) {
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    if (tempSmallSheetFrameList.size() > 0) {
                        tempSmallSheetFrameList.remove(0);
                        if (tempSmallSheetFrameList.size() == 0) {
                            //上传RetailTrade
                            try {
                                retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                                retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                                if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                    Assert.assertTrue("查找临时零售单失败！", false);
                                }

                                lTimeout = 60;
                                while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                                        retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
                                    Thread.sleep(1000);
                                }
                                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                                        retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                                    System.out.println("上传临时临时单同步数据失败！原因：超时！");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            if (tempRetailTradeList != null) {
//                                if (tempRetailTradeList.size() > 0) {
////                                    Robot.uploadRetailTradeListTempData(tempRetailTradeList);
//                                } else {
//                                    //上传收银汇总
//                                    Robot.uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
//                            }
//                            }
                        } else {
                            //上传tempSmallSheetFrameList的第一个元素
                            Robot.uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                        }
                    }
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                List<SmallSheetFrame> smallSheetList = (List<SmallSheetFrame>) event.getListMasterTable();
                //
                if (smallSheetList != null) {
                    if (smallSheetList.size() > 0) {
                        for (int i = 0; i < smallSheetList.size(); i++) {
                            syncSmallSheetIDs = syncSmallSheetIDs + "," + smallSheetList.get(i).getID();
                        }
                        syncSmallSheetIDs = syncSmallSheetIDs.substring(1, syncSmallSheetIDs.length());
                        //
                        Robot.feedbackSmallSheet(syncSmallSheetIDs);
                    }
                } else {
                    Robot.retrieveNVipCategory();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {  //...
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                //
                Robot.retrieveNSmallSheet();
            }
        }
    }

    public List<RetailTrade> getRetailTradeList() {
        return retailTradeList;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################ShopRobotTest onRetailTradeSQLiteEvent");
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Robot.retrieveNSmallSheet();
            event.setId(Robot.Event_ID_Robot);
        }
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC && event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setId(0); //...
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            List<RetailTrade> retailTradeList = (List<RetailTrade>) event.getListMasterTable();
            //
            if (retailTradeList != null) {
                if (retailTradeList.size() > 0) {
                    for (int i = 0; i < retailTradeList.size(); i++) {
                        syncRetailTradeIDs = syncRetailTradeIDs + "," + retailTradeList.get(i).getID();
                    }
                    syncRetailTradeIDs = syncRetailTradeIDs.substring(1, syncRetailTradeIDs.length());
                    //
                    Robot.feedbackRetailTrade(syncRetailTradeIDs);
                    event.setId(Robot.Event_ID_Robot);
                }
            } else {
                Robot.retrieveNSmallSheet();
            }
        }
    }

//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
//        event.onEvent();
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                if (tempRetailTradeAggregationList.size() > 0) {
                    tempRetailTradeAggregationList.remove(0);
                    //
                    if (tempRetailTradeAggregationList.size() > 0) {
                        //继续上传收银汇总
                        Robot.uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                    } else {
//                        if (!Robot.UPLOAD.equals(event.getString2())) {
                        Robot.syncTime();
//                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_NTP) {
//            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                Ntp ntp = (Ntp) event.getBaseModel1();
                NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
            }
            Barcodes barcodes = new Barcodes();
            barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            Robot.retrieveNCBarcodes(barcodes);
        } else {
            System.out.println("ShopRobot未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getHttpBO().getHttpEvent().getCount() != null) {
                if (!"".equals(event.getHttpBO().getHttpEvent().getCount())) {
                    Barcodes barcodes = new Barcodes();
                    barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                    count = Integer.valueOf(event.getHttpBO().getHttpEvent().getCount());
                    System.out.println("Barcodes的总数为：" + count);
                    int totalPageIndex = count % Integer.valueOf(barcodes.getPageSize()) != 0 ? count / Integer.valueOf(barcodes.getPageSize()) + 1 : count / Integer.valueOf(barcodes.getPageSize());//查询条形码需要totalPageIndex页才能查完
                    if (barcodesRunTimes < totalPageIndex) {
                        barcodes.setPageIndex(String.valueOf(++barcodesRunTimes));
                        Robot.retrieveNCBarcodes(barcodes);
                    } else {
                        //同步Brand
                        Brand brand = new Brand();
                        brand.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                        brand.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                        Robot.retrieveNCBrand(brand);
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步CommodityCategory
                CommodityCategory commodityCategory = new CommodityCategory();
                commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                Robot.retrieveNCCommodityCategory(commodityCategory);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步PackageUnit
                PackageUnit packageUnit = new PackageUnit();
                packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                Robot.retrieveNCPackageUnit(packageUnit);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步Commodity
                Commodity commodity = new Commodity();
                commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
                Robot.retrieveNCCommodity(commodity);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) { //...FeedbackCommodity 之前被删除。
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                //
                Robot.retrieveNPos();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getHttpBO().getHttpEvent() != null) {
                if (!"".equals(event.getHttpBO().getHttpEvent().getCount())) {
                    Commodity commodity = new Commodity();
                    commodity.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                    count = Integer.valueOf(event.getHttpBO().getHttpEvent().getCount());
                    System.out.println("商品总数为：" + count);
                    int totalPageIndex = count % Integer.valueOf(commodity.getPageSize()) != 0 ? count / Integer.valueOf(commodity.getPageSize()) + 1 : count / Integer.valueOf(commodity.getPageSize());//查询商品需要totalPageIndex页才能查完
                    if (commodityRunTimes < totalPageIndex) {
                        commodity.setPageIndex(String.valueOf(++commodityRunTimes));
                        Robot.retrieveNCCommodity(commodity);

                    } else {
                        //同步ConfigGeneral
                        Robot.retrieveNCConfigGeneral();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) throws InterruptedException {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                //同步POS
                Robot.retrieveNPos();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                //
                Robot.retrieveNSmallSheet();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLieEvent(PosSQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                List<Pos> posList = (List<Pos>) event.getListMasterTable();
                //
                if (posList != null) {
                    if (posList.size() > 0) {
                        for (int i = 0; i < posList.size(); i++) {
                            syncPosIDs = syncPosIDs + "," + posList.get(i).getID();
                        }
                        syncPosIDs = syncPosIDs.substring(1, syncPosIDs.length());
                        //
                        Robot.feedbackPos(syncPosIDs);
                    }
                } else {
                    Robot.retrieveNSmallSheet();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategoryHttpEvent(VipCategoryHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                //
                Robot.retrieveNVip();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                List<VipCategory> vipCategpryList = (List<VipCategory>) event.getListMasterTable();
                //
                if (vipCategpryList != null) {
                    if (vipCategpryList.size() > 0) {
                        for (int i = 0; i < vipCategpryList.size(); i++) {
                            syncVipCategoryIDs = syncVipCategoryIDs + "," + vipCategpryList.get(i).getID();
                        }
                        syncVipCategoryIDs = syncVipCategoryIDs.substring(1, syncVipCategoryIDs.length());
                        //
                        Robot.feedbackVipCategory(syncVipCategoryIDs);
                    }
                } else {
                    Robot.retrieveNVip();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipHttpEvent(VipHttpEvent event) throws InterruptedException {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                // 同步促销
                Robot.retrieveNPromotion();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipSQLiteEvent(VipSQLiteEvent event) throws InterruptedException {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                List<Vip> vipList = (List<Vip>) event.getListMasterTable();
                //
                if (vipList != null) {
                    if (vipList.size() > 0) {
                        for (int i = 0; i < vipList.size(); i++) {
                            syncVipIDs = syncVipIDs + "," + vipList.get(i).getID();
                        }
                        syncVipIDs = syncVipIDs.substring(1, syncVipIDs.length());
                        //
                        Robot.feedbackVip(syncVipIDs);
                    }
                } else {
                    Robot.retrieveNPromotion();
//                event.setString1(Robot.ALLDONE);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                // 同步促销
                System.out.println("开机启动同步完成~@@@@");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        event.onEvent();
        if (event.getId() == Robot.ROBOT_EVENT_ID_STARTUP_SYNC) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                System.out.println("开启同步完成！");
            }
        } else {
            event.setId(0); //...
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                List<Promotion> promotionList = (List<Promotion>) event.getListMasterTable();
                //
                if (promotionList != null) {
                    if (promotionList.size() > 0) {
                        for (int i = 0; i < promotionList.size(); i++) {
                            syncPromotionIDS = syncPromotionIDS + "," + promotionList.get(i).getID();
                        }
                        syncPromotionIDS = syncPromotionIDS.substring(1, syncPromotionIDS.length());
                        //
                        Robot.feedbackPromotion(syncPromotionIDS);
                    }
                } else {
                    System.out.println("开机启动同步完成~!!!!!");
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        event.onEvent();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
//        event.onEvent();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommodityHttpEvent(CommodityHttpEvent event) {
//        event.onEvent();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        event.onEvent();
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) throws InterruptedException {
//        event.onEvent();
//        List<RetailTrade> willUploadRetailTradeList = (List<RetailTrade>) event.getListMasterTable();
//        if (willUploadRetailTradeList.size() > 0) {
//            willUploadRetailTradeList.remove(0);
//            //
//            if (willUploadRetailTradeList.size() > 0) {
//                //继续上传零售单
//                uploadRetailTrade(willUploadRetailTradeList.get(0));
//            } else {
//                Robot.isDone = true;
//            }
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReturnCommoditySheetHttpEvent(ReturnCommoditySheetHttpEvent event) {
        if (event.getId() == Robot.Event_ID_Robot) {
            event.onEvent();
        } else {
            System.out.println("RetailTradeAggregationHttpEvent未处理的EventID");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWarehousingHttpEvent(WarehousingHttpEvent event) {
        if (event.getId() == Robot.Event_ID_Robot) {
            event.onEvent();
        } else {
            System.out.println("RetailTradeAggregationHttpEvent未处理的EventID");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReturnCommoditySheetSQLiteEvent(ReturnCommoditySheetSQLiteEvent event) {
        if (event.getId() == Robot.Event_ID_Robot) {
            event.onEvent();
        } else {
            System.out.println("RetailTradeAggregationHttpEvent未处理的EventID");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPurchasingOrderHttpEvent(PurchasingOrderHttpEvent event) {
        if (event.getId() == Robot.Event_ID_Robot) {
            event.onEvent();
        } else {
            System.out.println("RetailTradeAggregationHttpEvent未处理的EventID");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == Robot.Event_ID_Robot) {
            event.onEvent();
        } else {
            System.out.println("RetailTradeAggregationHttpEvent未处理的EventID");
        }
    }


    // 早上8:00 收银员登录
    // 填写准备金
    // 开始收银（随机生成售卖的商品和数量，数量随机，商品随机，件数随机）
    // 晚上8:00 收银员交班 登出POS
    @Test(timeout = 20000)
    public void testShopRobot() throws Exception {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Pos机器人启动");
        Thread tc = new Thread() {
            public void run() {
                RobotClient minaClient = new RobotClient();
                minaClient.init();
                while (true) {
                    if (bStopClient) {
                        break;
                    }
                }
            }
        };
        tc.start();
        while(true) {
            if (bProgramIsRunning) {
                System.out.println("------Pos机器人开始执行机器餐！-------");
                //读取配置文件，设置连接的服务器和是否进行沙箱环境支付
                Program.setRobotStartDatetime(new Date());
                //...模拟BX配置机器和第一次开机同步  TODO 这里是旧的机器人代码，要重构
                int YEAR = 2018;
                int MONTH = new Random().nextInt(12) + 1; // Not 7
                int DAY = new Random().nextInt(30) + 1;   // final定义的时间是用来限制的日期。 如：机器 人模拟销售后会对这个日期进行对比。如果模拟销售的日期等于定义的这个日期则结束
                int HOUR = 8;
                int MINUTE = 00;
                int SECOND = 00;
                Calendar calStart = Calendar.getInstance();
                calStart.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
                //
                System.out.println("开店时间：" + YEAR + "-" + MONTH + "-" + DAY);
                boolean bRunInRandomMode = false; // 工作在非随机模式下
                Robot r = new Robot(calStart.getTime(), calEnd.getTime(), robotConfig, bRunInRandomMode);
                if (!r.run()) {
                    System.out.println(r.getErrorInfo());
                    Assert.assertFalse("机器人运行出错！错误信息：" + r.getErrorInfo(), true);
                }
                Header header = new Header();
                header.setCommand(Header.EnumCommandType.ECT_PosCloseConnection.getIndex());
                header.setBodyLength(0);
                StringBuilder sb = new StringBuilder();
                sb.append(header.toBufferString());
                clientSession.write(sb.toString());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("关闭客户端");
                clientSession.closeNow();// TODO 不是收到服务器的指令才结束的，需要重构
                break;
            } else {
                Thread.sleep(500);
            }
        }
    }

    /**
     * 随机选取几个可能收银的商品，包括组合商品、单品、多包装商品，不包括已经删除的商品
     *
     * @param maxNO 最大的商品数目
     * @return
     */
    public static List<Commodity> getCommodityList(int maxNO, boolean forPurchasingOnly, StringBuilder sbError, boolean bRunInRandomMode) {
        Map<String, Commodity> map = new HashMap<>();  //key=F_ID, value=Commodity
        Commodity commodity = new Commodity();
        // 售卖指定商品
        if (!bRunInRandomMode) {
//            long commodityIDArr[] = {RobotConfig.maxCommodityID + 1};
            long commodityIDArr[] = {2};
            for (int i = 0; i < commodityIDArr.length; i++) {
                commodity.setID(commodityIDArr[i]);
                Commodity commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                if (commodity1 == null || commodity1.getStatus() == Commodity.EnumStatusCommodity.ESC_Deleted.getIndex()) { //防止拿到已经删除的商品或SQLite错误
                    sbError.append("本地SQLite没有找到ID为：" + commodityIDArr[i] + "的商品；");
                    return null;
                } else {
                    map.put(String.valueOf(commodity1.getID()), commodity1);
                }
            }
        }
        // 售卖随机商品
        else {
            assert maxNO > 0;
            Random r = new Random();
            int commodityNO = r.nextInt(maxNO);
            if (commodityNO <= 0) {
                commodityNO = 1;
            }
            for (int i = 0; i < commodityNO; i++) {
                //...在本地SQLite中读取商品。R1，传递商品ID。
                commodity.setID(Long.valueOf(r.nextInt(100) + 1));
                Commodity commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                if (commodity1 == null || commodity1.getStatus() == Commodity.EnumStatusCommodity.ESC_Deleted.getIndex()) { //防止拿到已经删除的商品或SQLite错误
                    i -= 1;
                    continue;
                }
                if (forPurchasingOnly) { //采购商品只能是单品
                    while (true) {//直到找到单品为止
                        if (commodity1 != null && (commodity1.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex())) {
                            map.put(String.valueOf(commodity1.getID()), commodity1);
                            break;
                        } else {
                            commodity.setID(Long.valueOf(r.nextInt(100) + 1));
                            commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                        }
                    }
                } else if (commodity1 != null) {
                    map.put(String.valueOf(commodity1.getID()), commodity1);//这里的商品可能是单品、多包装商品、组合商品、服务型商品
                }
                //... 如果只获取一个商品，然后商品ID不存在的情况下该如何处理
            }
        }

        return new ArrayList<>(map.values());
    }

    //

    /**
     * 购买商品随机
     *
     * @param listComm 购买的商品的列表
     * @param maxNO    购买一个商品的最大数量
     */
    public static List<RetailTradeCommodity> getRetailTradeCommodityList(List<Commodity> listComm, int maxNO, RetailTrade retailTrade, StringBuilder sbError, boolean bRunInRandomMode) throws ParseException {
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        // 指定商品
        if (!bRunInRandomMode) {
            int totalPrices = 0; //记录这批零售单商品的总价格
            long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("零售单商品表查找本地最大ID失败！");
                return null;
            }
            int commodityNoArr[] = {200, 2, 3};
            for (int i = 0; i < listComm.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                int commodityNO = commodityNoArr[i];
                if (commodityNO <= 0) {
                    commodityNO = 1;
                }
                retailTradeCommodity.setID(maxTextIDInSQLite + i + 1);
                retailTradeCommodity.setPriceOriginal((double) listComm.get(i).getPriceRetail());
                retailTradeCommodity.setNOCanReturn(commodityNO);
                retailTradeCommodity.setCommodityID(listComm.get(i).getID().intValue());
                retailTradeCommodity.setNO(commodityNO);
                retailTradeCommodity.setDiscount(1);
                retailTradeCommodity.setPriceSpecialOffer(0);
                retailTradeCommodity.setPriceVIPOriginal(0);
                retailTradeCommodity.setBarcodeID(1);
                retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTradeCommodity.setTradeID(retailTrade.getID());
                retailTradeCommodity.setPriceReturn((double) listComm.get(i).getPriceRetail()); //...

                totalPrices += commodityNO * listComm.get(i).getPriceRetail();
                retailTradeCommodityList.add(retailTradeCommodity);
                // ...
                listComm.get(i).setCommodityQuantity(commodityNO);
            }
            // xxxx促销
            retailTradePromoting = new RetailTradePromoting();
            PromotionCalculator promotionCalculator = new PromotionCalculator();
            PromotionPresenter promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
            RetailTrade retailTradeAfterPromoted = promotionCalculator.sell(listComm, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
            retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeAfterPromoted.getListSlave1();
            for (int i = 0; i < retailTradeCommodityList.size(); i++) {
                retailTradeCommodityList.get(i).setID(maxTextIDInSQLite + i + 1);
                retailTradeCommodityList.get(i).setTradeID(retailTrade.getID());
                retailTradeCommodityList.get(i).setBarcodeID(1);
            }
            retailTradeCommodityList.get(0).setAmountWeChat(retailTradeAfterPromoted.getAmount());//把这批零售单商品的总价格返回出去
        }
        // 随机商品
        else {
            //...
            Random r = new Random();
            int totalPrices = 0; //记录这批零售单商品的总价格

            long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("零售单商品表查找本地最大ID失败！");

                return null;
            }
            for (int i = 0; i < listComm.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                int commodityNO = r.nextInt(maxNO);
                if (commodityNO <= 0) {
                    commodityNO = 1;
                }
                retailTradeCommodity.setID(maxTextIDInSQLite + i + 1);
                retailTradeCommodity.setPriceOriginal((double) listComm.get(i).getPriceRetail());
                retailTradeCommodity.setNOCanReturn(commodityNO);
                retailTradeCommodity.setCommodityID(listComm.get(i).getID().intValue());
                retailTradeCommodity.setNO(commodityNO);
                retailTradeCommodity.setDiscount(1);
                retailTradeCommodity.setPriceSpecialOffer(0);
                retailTradeCommodity.setPriceVIPOriginal(0);
                retailTradeCommodity.setBarcodeID(1);
                retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTradeCommodity.setTradeID(retailTrade.getID());
                retailTradeCommodity.setPriceReturn(1); //...

                totalPrices += commodityNO * listComm.get(i).getPriceRetail();
                retailTradeCommodityList.add(retailTradeCommodity);
            }
            //
            retailTradeCommodityList.get(0).setAmountWeChat(totalPrices);//把这批零售单商品的总价格返回出去
        }
        return retailTradeCommodityList;
    }

    //TODO 要创建新类放本函数
    public static List<RetailTradeCommodity> getRetailTradeCommodityListInExcelMode(RetailTrade retailTrade, StringBuilder sbError, boolean bRunInRandomMode) throws ParseException {
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        // 指定商品
        if (!bRunInRandomMode) {
            int totalPrices = 0; //记录这批零售单商品的总价格
            long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("零售单商品表查找本地最大ID失败！");
                return null;
            }
            int relativeCommodityIDFromExcel;
            for (int i = 0; i < retailTrade.getListSlave1().size(); i++) {
                RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTrade.getListSlave1().get(i);
                relativeCommodityIDFromExcel = retailTradeCommodity.getCommodityID();
                // 日报表也要自己创建商品
                String commAndBarcodeIDs = ClientHandler.bodyInfoForCreateRt[i];
                String[] commAndBarcodeIdArr = commAndBarcodeIDs.split(",");
                int commodityID = Integer.parseInt(commAndBarcodeIdArr[0]);
                int barcodeID = Integer.parseInt(commAndBarcodeIdArr[1]);
                retailTradeCommodity.setCommodityID(commodityID);
                retailTradeCommodity.setBarcodeID(barcodeID);
//                retailTradeCommodity.setCommodityID(Program.maxCommodityID + relativeCommodityIDFromExcel);
//                retailTradeCommodity.setBarcodeID(Program.maxBarcodeID + relativeCommodityIDFromExcel);
                retailTradeCommodity.setID(maxTextIDInSQLite + i);
                retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTradeCommodity.setTradeID(retailTrade.getID());
                totalPrices += retailTradeCommodity.getNO() * retailTradeCommodity.getPriceReturn();
                retailTradeCommodityList.add(retailTradeCommodity);
            }
            retailTradeCommodityList.get(0).setAmountWeChat(totalPrices);//把这批零售单商品的总价格返回出去
        }
        return retailTradeCommodityList;
    }

}
