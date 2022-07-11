package com.test.bx.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.CouponCodeHttpBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.bo.CouponSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.coupon.CouponCalculator;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.CouponCodeHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.CouponPresenter;
import com.bx.erp.presenter.RetailTradeCouponPresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.presenter.VipPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.Shared;

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

import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_NoError;

/**
 * 遗留问题：
 * 1、促销可以指定商品，但是，促销开始的时间已经被代码强制修改为服务器时间的前一天，以使促销生效。
 * 2、Vip是insertTable.sql中写死的，领券也是
 * 3、优惠券无法指定商品。指定商品的优惠券里，商品的F_ID是确定的，但是，商品是动态创建的，F_ID不确定，Vip是写死的，领的券也必须是写死的。为了达到测试目的，在内存中生成一个来做测试。如果能
 * 模拟会员领券，则将来可以改掉本缺陷。
 * 4、使用的促销是写死的，而且只传递1个促销给促销计算器：
 * promotionList = new ArrayList<>();
 * promotionList.add(promotion);
 * <p>
 * retailTradePromoting = new RetailTradePromoting();
 * RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
 */
public class VipConsumeSIT extends BaseHttpAndroidTestCase {
    private Commodity normalCommodity_1;
    private Commodity normalCommodity_2;
    private Commodity multiPackagingCommodity_1;
    private Commodity combinationCommodity_1;
    private Commodity serviceCommodity_1;

    /**
     * *_P_1代表是只进行促销计算的商品
     * P代表促销
     */
    private Commodity normalCommodity_P_1;
    private Commodity normalCommodity_P_2;
    private Commodity multiPackagingCommodity_P_1;
    private Commodity combinationCommodity_P_1;
    private Commodity serviceCommodity_P_1;

    /**
     * *_C_1代表是只进行优惠券计算的商品
     * C代表优惠券
     */
    private Commodity normalCommodity_C_1;
    private Commodity normalCommodity_C_2;
    private Commodity multiPackagingCommodity_C_1;
    private Commodity combinationCommodity_C_1;
    private Commodity serviceCommodity_C_1;


    private List<Commodity> commodityList;
    private List<BaseModel> promotionList;
    private RetailTradePromoting retailTradePromoting;
    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    //    private CouponCode couponCode;
    private Vip vip;
    private Coupon cashCoupon;//全场满减优惠券
    private Coupon discountCoupon;//全场满折优惠券
    private Random random = new Random(1);

    private static VipPresenter vipPresenter = null;
    private static VipHttpBO vipHttpBO = null;
    private static VipSQLiteBO vipSQLiteBO = null;
    private static VipHttpEvent vipHttpEvent = null;
    private static VipSQLiteEvent vipSQLiteEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;

    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionSQLiteBO promotionSQLiteBO = null;

    private static CouponCodeHttpBO couponCodeHttpBO = null;
    private static CouponCodeHttpEvent couponCodeHttpEvent = null;

    private static CouponHttpBO couponHttpBO = null;
    private static CouponHttpEvent couponHttpEvent = null;

    private static CouponSQLiteBO couponSQLiteBO = null;
    private static CouponSQLiteEvent couponSQLiteEvent = null;

    private RetailTradeCouponPresenter retailTradeCouponPresenter;
    private RetailTradePromotingPresenter retailTradePromotingPresenter;
    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
    private static CouponPresenter couponPresenter = null;

    private static final int EVENT_ID_VipConsumeSIT = 10000;

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

        vipPresenter = GlobalController.getInstance().getVipPresenter();
        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        vipHttpEvent.setHttpBO(vipHttpBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        //pos登录
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //员工登录
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_VipConsumeSIT);
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
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);

        if (couponCodeHttpEvent == null) {
            couponCodeHttpEvent = new CouponCodeHttpEvent();
            couponCodeHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (couponCodeHttpBO == null) {
            couponCodeHttpBO = new CouponCodeHttpBO(GlobalController.getInstance().getContext(), null, couponCodeHttpEvent);
        }
        couponCodeHttpEvent.setHttpBO(couponCodeHttpBO);

        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(GlobalController.getInstance().getContext(), null, couponHttpEvent);
        }
        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_VipConsumeSIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        retailTradeCouponPresenter = GlobalController.getInstance().getRetailTradeCouponPresenter();
        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
        retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
        couponPresenter = GlobalController.getInstance().getCouponPresenter();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        System.out.println("---------------------- ASYNC方式接收到event");
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponCodeHttpEvent(CouponCodeHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        System.out.println("xxxxxxxxxxxxxx   进入了" + event);
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_VipConsumeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    //查询本地商品
    private Commodity retrieveCommodity(Commodity comm) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodity.setSql("where F_Name = ?");
        commodity.setConditions(new String[]{comm.getName()});
        return (Commodity) BaseCommodityTest.retrieveNSyncInSQLite(commoditySQLiteBO, commoditySQLiteEvent, commodity);
    }

    //同步所有创建过的商品
    private void syncCommodity() throws InterruptedException {
        logOut();
//        2步，登录POS和Staff
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.发送RN请求，应该返回刚刚创建的commodity
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求同步Commodity超时！", false);
        }
        //
        if (commoditySQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("同步服务器返回的商品失败", false);
        }
        List<Commodity> commodityList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("同步RN不应该没有数据返回", commodityList.size() > 0);
    }

    /**
     * 创建测试要用的商品、促销、优惠券
     */
    private void createTestingData() throws Exception {
//---------------------------------- consumeWithoutPromotionAndCoupon 方法所使用的商品 ---------------------------------------------
        //创建 普通商品1
        normalCommodity_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_1);//nbr中创建
        //创建 普通商品2
        normalCommodity_2 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_2);//nbr中创建

        //创建 多包装商品1
        Commodity commodity1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        commodity1.setName("多包装商品的单品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        multiPackagingCommodity_1.setName("多包装商品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_1.setRefCommodityID(10);
        multiPackagingCommodity_1.setRefCommodityMultiple(2);
        commodity1.setMultiPackagingInfo(commodity1.getBarcode() + "," + multiPackagingCommodity_1.getBarcode() + ";" + commodity1.getPackageUnitID() + "," + multiPackagingCommodity_1.getPackageUnitID() + ";" +
                commodity1.getRefCommodityMultiple() + "," + multiPackagingCommodity_1.getRefCommodityMultiple() + ";" +
                commodity1.getPriceRetail() + "," + multiPackagingCommodity_1.getPriceRetail() + ";" +
                commodity1.getPriceVIP() + "," + multiPackagingCommodity_1.getPriceVIP() + ";" +
                commodity1.getPriceWholesale() + "," + multiPackagingCommodity_1.getPriceWholesale() + ";" +
                commodity1.getName() + "," + multiPackagingCommodity_1.getName() + ";");
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, commodity1);//nbr中创建

        //创建 组合商品1
        combinationCommodity_1 = BaseCommodityTest.DataInput.getCombinationCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        combinationCommodity_1.setMultiPackagingInfo(System.currentTimeMillis() + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "组合商品A" + System.currentTimeMillis() % 1000000 + ";");//
        JSONObject jsonObject = (JSONObject) JSON.toJSON(combinationCommodity_1);
        JSONObject json2 = (JSONObject) JSON.parse(JSONObject.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss.SSS"));
        combinationCommodity_1.setSubCommodityInfo(json2.toString());//
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, combinationCommodity_1);//nbr中创建

        //创建 服务商品1
        serviceCommodity_1 = BaseCommodityTest.DataInput.getServiceCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, serviceCommodity_1);//nbr中创建

//---------------------------------- consumeWithPromotionButNoCoupon_* 方法所使用的商品 ---------------------------------------------

        //创建 普通商品1
        normalCommodity_P_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_P_1);//nbr中创建
        //创建 普通商品2
        normalCommodity_P_2 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_P_2);//nbr中创建

        //创建 多包装商品1
        Commodity commodity2 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        commodity2.setName("多包装商品的单品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_P_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        multiPackagingCommodity_P_1.setName("多包装商品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_P_1.setRefCommodityID(10);
        multiPackagingCommodity_P_1.setRefCommodityMultiple(2);
        commodity2.setMultiPackagingInfo(commodity2.getBarcode() + "," + multiPackagingCommodity_P_1.getBarcode() + ";" + commodity2.getPackageUnitID() + "," + multiPackagingCommodity_P_1.getPackageUnitID() + ";" +
                commodity2.getRefCommodityMultiple() + "," + multiPackagingCommodity_P_1.getRefCommodityMultiple() + ";" +
                commodity2.getPriceRetail() + "," + multiPackagingCommodity_P_1.getPriceRetail() + ";" +
                commodity2.getPriceVIP() + "," + multiPackagingCommodity_P_1.getPriceVIP() + ";" +
                commodity2.getPriceWholesale() + "," + commodity2.getPriceWholesale() + ";" +
                commodity2.getName() + "," + multiPackagingCommodity_P_1.getName() + ";");
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, commodity2);//nbr中创建

        //创建 组合商品1
        combinationCommodity_P_1 = BaseCommodityTest.DataInput.getCombinationCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        combinationCommodity_P_1.setMultiPackagingInfo(System.currentTimeMillis() + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "组合商品A" + System.currentTimeMillis() % 1000000 + ";");//
        jsonObject = (JSONObject) JSON.toJSON(combinationCommodity_P_1);
        json2 = (JSONObject) JSON.parse(JSONObject.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss.SSS"));
        combinationCommodity_P_1.setSubCommodityInfo(json2.toString());//
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, combinationCommodity_P_1);//nbr中创建

        //创建 服务商品1
        serviceCommodity_P_1 = BaseCommodityTest.DataInput.getServiceCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, serviceCommodity_P_1);//nbr中创建

//---------------------------------- consumeWithoutPromotionButUseCoupon_* 方法所使用的商品 ---------------------------------------------
        //创建 普通商品1
        normalCommodity_C_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_C_1);//nbr中创建
        //创建 普通商品2
        normalCommodity_C_2 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, normalCommodity_C_2);//nbr中创建

        //创建 多包装商品1
        Commodity commodity3 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        commodity3.setName("多包装商品的单品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_C_1 = BaseCommodityTest.DataInput.getNormalCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        multiPackagingCommodity_C_1.setName("多包装商品" + System.currentTimeMillis() % 1000000);
        multiPackagingCommodity_C_1.setRefCommodityID(10);
        multiPackagingCommodity_C_1.setRefCommodityMultiple(2);
        commodity3.setMultiPackagingInfo(commodity3.getBarcode() + "," + multiPackagingCommodity_C_1.getBarcode() + ";" + commodity3.getPackageUnitID() + "," + multiPackagingCommodity_C_1.getPackageUnitID() + ";" +
                commodity3.getRefCommodityMultiple() + "," + multiPackagingCommodity_C_1.getRefCommodityMultiple() + ";" +
                commodity3.getPriceRetail() + "," + multiPackagingCommodity_C_1.getPriceRetail() + ";" +
                commodity3.getPriceVIP() + "," + multiPackagingCommodity_C_1.getPriceVIP() + ";" +
                commodity3.getPriceWholesale() + "," + commodity3.getPriceWholesale() + ";" +
                commodity3.getName() + "," + multiPackagingCommodity_C_1.getName() + ";");
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, commodity3);//nbr中创建

        //创建 组合商品1
        combinationCommodity_C_1 = BaseCommodityTest.DataInput.getCombinationCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        combinationCommodity_C_1.setMultiPackagingInfo(System.currentTimeMillis() + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "组合商品A" + System.currentTimeMillis() % 1000000 + ";");//
        jsonObject = (JSONObject) JSON.toJSON(combinationCommodity_C_1);
        json2 = (JSONObject) JSON.parse(JSONObject.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss.SSS"));
        combinationCommodity_C_1.setSubCommodityInfo(json2.toString());//
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, combinationCommodity_C_1);//nbr中创建

        //创建 服务商品1
        serviceCommodity_C_1 = BaseCommodityTest.DataInput.getServiceCommodity(Double.valueOf(GeneralUtil.formatToShow(random.nextInt(100) + random.nextDouble())));
        BaseCommodityTest.createSyncViaHttp(commodityHttpBO, commoditySQLiteBO, serviceCommodity_C_1);//nbr中创建


//-------------------------------------------进行同步操作------------------------------------------------------------------
        //同步所有创建过的商品到本地
        syncCommodity();

        //同步商品后查询商品，需要获取商品id，否则无法计算价格，所以进行retrieveCommodity:在本地搜索商品
        normalCommodity_1 = retrieveCommodity(normalCommodity_1);
        normalCommodity_2 = retrieveCommodity(normalCommodity_2);
        multiPackagingCommodity_1 = retrieveCommodity(multiPackagingCommodity_1);
        combinationCommodity_1 = retrieveCommodity(combinationCommodity_1);
        serviceCommodity_1 = retrieveCommodity(serviceCommodity_1);

        normalCommodity_P_1 = retrieveCommodity(normalCommodity_P_1);
        normalCommodity_P_2 = retrieveCommodity(normalCommodity_P_2);
        multiPackagingCommodity_P_1 = retrieveCommodity(multiPackagingCommodity_P_1);
        combinationCommodity_P_1 = retrieveCommodity(combinationCommodity_P_1);
        serviceCommodity_P_1 = retrieveCommodity(serviceCommodity_P_1);

        normalCommodity_C_1 = retrieveCommodity(normalCommodity_C_1);
        normalCommodity_C_2 = retrieveCommodity(normalCommodity_C_2);
        multiPackagingCommodity_C_1 = retrieveCommodity(multiPackagingCommodity_C_1);
        combinationCommodity_C_1 = retrieveCommodity(combinationCommodity_C_1);
        serviceCommodity_C_1 = retrieveCommodity(serviceCommodity_C_1);

        //设置商品数量
        normalCommodity_1.setCommodityQuantity(1);
        normalCommodity_2.setCommodityQuantity(1);
        multiPackagingCommodity_1.setCommodityQuantity(1);
        combinationCommodity_1.setCommodityQuantity(1);
        serviceCommodity_1.setCommodityQuantity(1);

        normalCommodity_P_1.setCommodityQuantity(1);
        normalCommodity_P_2.setCommodityQuantity(1);
        multiPackagingCommodity_P_1.setCommodityQuantity(1);
        combinationCommodity_P_1.setCommodityQuantity(1);
        serviceCommodity_P_1.setCommodityQuantity(1);

        normalCommodity_C_1.setCommodityQuantity(1);
        normalCommodity_C_2.setCommodityQuantity(1);
        multiPackagingCommodity_C_1.setCommodityQuantity(1);
        combinationCommodity_C_1.setCommodityQuantity(1);
        serviceCommodity_C_1.setCommodityQuantity(1);

        //搜索VIP，拿到VIP所拥有的优惠券
        retrieveVipAndCoupon();
    }

    /**
     * 消费1单（单品1、单品2、多包装商品1、服务商品1、组合商品1和2），无促销（单品1、单品2不参与促销）、不使用优惠券。结果验证：应付款？
     */
    private void consumeWithoutPromotionAndCoupon() throws CloneNotSupportedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_1);
        commodityList.add(normalCommodity_2);
        commodityList.add(combinationCommodity_1);
        commodityList.add(multiPackagingCommodity_1);
        commodityList.add(serviceCommodity_1);

        retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, null, retailTradePromoting);
        Assert.assertTrue("零售单中没有商品参与促销，计算过程为空！", retailTradePromoting.getListSlave1().size() == 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        double manuallyCalculateAmount = normalCommodity_1.getPriceRetail() + normalCommodity_2.getPriceRetail() + multiPackagingCommodity_1.getPriceRetail() + combinationCommodity_1.getPriceRetail() + serviceCommodity_1.getPriceRetail();
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + manuallyCalculateAmount);
        Assert.assertTrue("计算价格不正确", GeneralUtil.formatToShow(systemCalculateAmount).equals(GeneralUtil.formatToShow(manuallyCalculateAmount)));
    }

    /**
     * 4、消费1单（vip单品1、vip单品2、多包装商品、服务商品、组合商品），有促销（单品2）、不使用优惠券。结果验证：应付款？
     * 满减促销/4.1a 指定范围
     */
    private void consumeWithPromotionButNoCoupon_1() throws CloneNotSupportedException, InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_P_1);
        commodityList.add(normalCommodity_P_2);
        commodityList.add(combinationCommodity_P_1);
        commodityList.add(multiPackagingCommodity_P_1);
        commodityList.add(serviceCommodity_P_1);

        double reducingOfAmount = (double) random.nextInt(1000) / 100.000000d + 1.000000d;

        //创建一个指定商品 * 减 * 的促销
        Promotion promotion = BasePrommotionTest.DataInput.getCashReducingOfAmountPromotionOnSpecifiedCommodity(normalCommodity_P_2.getPriceRetail() - random.nextInt(10), reducingOfAmount, String.valueOf(normalCommodity_P_2.getID()));
        promotion = BasePrommotionTest.createSyncViaHttp(promotion, promotionHttpEvent, promotionHttpBO);

        //创建一个促销集合
        promotionList = new ArrayList<>();
        promotionList.add(promotion);

        retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("零售单中有商品参与促销，计算过程不应该为空！", retailTradePromoting.getListSlave1().size() != 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        double selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + (normalCommodity_P_2.getPriceRetail() - reducingOfAmount) + multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + selfCalculateAmount);
        Assert.assertTrue("计算价格不正确", GeneralUtil.formatToShow(systemCalculateAmount).equals(GeneralUtil.formatToShow(selfCalculateAmount)));
    }

    /**
     * 4、消费1单（vip单品1、vip单品3、vip单品4、多包装商品、服务商品、组合商品），有促销（全场）、不使用优惠券。结果验证：应付款？
     * 满减促销/4.1b 全场
     */
    private void consumeWithPromotionButNoCoupon_2() throws CloneNotSupportedException, InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_P_1);
        commodityList.add(normalCommodity_P_2);
        commodityList.add(combinationCommodity_P_1);
        commodityList.add(multiPackagingCommodity_P_1);
        commodityList.add(serviceCommodity_P_1);

        double reducingOfAmount = (double) random.nextInt(1000) / 100.000000d + 1.000000d;

        //创建一个全场满 * 减 * 的促销
        Promotion promotion = BasePrommotionTest.DataInput.getCashReducingOfAmountPromotionOnAllCommodity(normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail() - random.nextInt(10), reducingOfAmount);
        promotion = BasePrommotionTest.createSyncViaHttp(promotion, promotionHttpEvent, promotionHttpBO);

        //创建一个促销集合
        promotionList = new ArrayList<>();
        promotionList.add(promotion);

        retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("零售单中有商品参与促销，计算过程不应该为空！", retailTradePromoting.getListSlave1().size() != 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        double selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail() + multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail() - reducingOfAmount;
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + selfCalculateAmount);
        Assert.assertTrue("计算价格不正确", GeneralUtil.formatToShow(systemCalculateAmount).equals(GeneralUtil.formatToShow(selfCalculateAmount)));
    }

    /**
     * 4、消费1单（vip单品1、vip单品3、vip单品4、多包装商品、服务商品、组合商品），有促销（单品2）、不使用优惠券。结果验证：应付款？
     * 满折促销/4.2a 指定范围
     */
    private void consumeWithPromotionButNoCoupon_3() throws CloneNotSupportedException, InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_P_1);
        commodityList.add(normalCommodity_P_2);
        commodityList.add(combinationCommodity_P_1);
        commodityList.add(multiPackagingCommodity_P_1);
        commodityList.add(serviceCommodity_P_1);

        double DiscountOfAmount = (random.nextInt(10) + 0.1) / 10;

        //创建一个指定商品(normalCommodity_P_2)满*打*折的促销
        Promotion promotion = BasePrommotionTest.DataInput.getDiscountOfAmountPromotionOnSpecifiedCommodity(normalCommodity_P_2.getPriceRetail() - random.nextInt(10), DiscountOfAmount, String.valueOf(normalCommodity_P_2.getID()));
        promotion = BasePrommotionTest.createSyncViaHttp(promotion, promotionHttpEvent, promotionHttpBO);

        //创建一个促销集合
        promotionList = new ArrayList<>();
        promotionList.add(promotion);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail() +
                multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("零售单中有商品参与促销，计算过程不应该为空！", retailTradePromoting.getListSlave1().size() != 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + (normalCommodity_P_2.getPriceRetail() * DiscountOfAmount) +
                multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + selfCalculateAmount);
        Assert.assertTrue("计算价格不正确", GeneralUtil.formatToShow(systemCalculateAmount).equals(GeneralUtil.formatToShow(selfCalculateAmount)));
    }

    /**
     * 4、消费1单（vip单品1、vip单品3、vip单品4、多包装商品、服务商品、组合商品），有促销（全场）、不使用优惠券。结果验证：应付款？
     * 满折促销/4.2a 全场范围
     */
    private void consumeWithPromotionButNoCoupon_4() throws CloneNotSupportedException, InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_P_1);
        commodityList.add(normalCommodity_P_2);
        commodityList.add(combinationCommodity_P_1);
        commodityList.add(multiPackagingCommodity_P_1);
        commodityList.add(serviceCommodity_P_1);

        double DiscountOfAmount = (random.nextInt(10) + 0.1) / 10;

        //创建一个满 * 打9折的促销
        Promotion promotion = BasePrommotionTest.DataInput.getDiscountOfAmountPromotionOnAllCommodity(normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail() - random.nextInt(10), DiscountOfAmount);
        promotion = BasePrommotionTest.createSyncViaHttp(promotion, promotionHttpEvent, promotionHttpBO);

        //创建一个促销集合
        promotionList = new ArrayList<>();
        promotionList.add(promotion);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail() + multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("零售单中有商品参与促销，计算过程不应该为空！", retailTradePromoting.getListSlave1().size() != 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        selfCalculateAmount = (normalCommodity_P_1.getPriceRetail() + normalCommodity_P_2.getPriceRetail()) * DiscountOfAmount + multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + selfCalculateAmount);
        Assert.assertTrue("计算价格不正确", GeneralUtil.formatToShow(systemCalculateAmount).equals(GeneralUtil.formatToShow(selfCalculateAmount)));
    }

    //搜索VIP，拿到VIP所拥有的优惠券
    private void retrieveVipAndCoupon() throws InterruptedException {
        //先同步所有的coupon到本地
        BaseCouponTest.retrieveNSyncViaHttp(couponSQLiteEvent, couponHttpBO, couponHttpEvent);

        vip = new Vip();
        vip.setCategory(1);
        vip.setMobile("15876767551");
        vip = BaseVipTest.retrieve1SyncViaHttp(vip, vipHttpBO, vipHttpEvent, vipSQLiteEvent);

        //搜索所有的优惠券
        CouponCode couponCode = new CouponCode();
        couponCode.setVipID(vip.getID().intValue());
        couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        List<CouponCode> couponList = BaseCouponCodeTest.retrieveNSync(couponCode, couponCodeHttpEvent, couponCodeHttpBO);
        System.out.println("xxxxxxxxx 优惠券列表: " + couponList);

        couponCode = couponList.get(1);
        Coupon queryCoupon = new Coupon();
        queryCoupon.setID(Long.valueOf(couponCode.getCouponID()));
        //获取到满10减5优惠券
        cashCoupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
        System.out.println("cashCoupon:  " + cashCoupon);

        couponCode = couponList.get(0);
        queryCoupon = new Coupon();
        queryCoupon.setID(Long.valueOf(couponCode.getCouponID()));
        //获取到满10打9折优惠券
        discountCoupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
        System.out.println("discountCoupon:  " + discountCoupon);
    }

    /**
     * 5、消费1单（单品9、多包装商品、服务商品、组合商品），无促销、使用优惠券。
     * 满减优惠券 5.1a 指定范围
     */
    private void consumeWithoutPromotionButUseCoupon_1() throws InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_C_1);
        commodityList.add(normalCommodity_C_2);
        commodityList.add(combinationCommodity_C_1);
        commodityList.add(multiPackagingCommodity_C_1);
        commodityList.add(serviceCommodity_C_1);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, null, retailTradePromoting);
        retailTradeSell.setID(4567L);
//        retailTradeSell.setVipID(vip.getID().intValue());
        Assert.assertTrue("零售单中无商品参与促销，计算过程应该为空！", retailTradePromoting.getListSlave1().size() == 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        System.out.println("未使用优惠券时系统计算的价格： " + systemCalculateAmount);

        double reducingOfAmount = (double) random.nextInt(1000) / 100.000000d + 1.000000d;

        Date date = new Date();
        Date dt1 = new Date(date.getTime() - 100000000L);//得到昨天的日期
        Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

        Coupon coupon = new Coupon();
        coupon.setID(1L);
        coupon.setStatus(0);
        coupon.setType(Coupon.EnumCouponCardType.ECCT_CASH.getIndex());//满减
        coupon.setBonus(0);//不需要积分领取
        coupon.setLeastAmount(normalCommodity_C_2.getPriceRetail() - random.nextInt(10));//起用金额
        coupon.setReduceAmount(reducingOfAmount);//减免金额
        coupon.setDiscount(1);//打折额度
        coupon.setTitle("测试创建满10减5优惠券");
        coupon.setColor("Color010");
        coupon.setDescription("测试专用");
        coupon.setPersonalLimit(10);
        coupon.setWeekDayAvailable(0);//没有星期限制
        coupon.setBeginTime("00:00:00");
        coupon.setEndTime("23:59:59");
        coupon.setBeginDateTime(dt1);
        coupon.setEndDateTime(dt2);
        coupon.setQuantity(100);//库存数量
        coupon.setRemainingQuantity(99);//当前剩余库存数量
        coupon.setScope(1);//0=全部商品，1=部分商品（这些商品记录在T_CouponScope中）

        CouponScope couponScope = new CouponScope();
        couponScope.setID(coupon.getID());
        couponScope.setCommodityID(normalCommodity_C_2.getID().intValue());//限定的商品id
        couponScope.setCommodityName(normalCommodity_C_2.getName());

        double amount = CouponCalculator.calculateAmountUsingCoupon(coupon, systemCalculateAmount, commodityList, retailTradeSell);
        retailTradeSell.setAmount(amount);
        selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + (normalCommodity_C_2.getPriceRetail() - reducingOfAmount) + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();

        System.out.println("手动计算使用优惠券后的价格： " + selfCalculateAmount);
        System.out.println("使用优惠券后系统计算的价格： " + retailTradeSell.getAmount());
        Assert.assertTrue("计算价格不一致！", GeneralUtil.formatToShow(selfCalculateAmount).equals(GeneralUtil.formatToShow(retailTradeSell.getAmount())));
    }

    /**
     * 5、消费1单（单品9、多包装商品、服务商品、组合商品），无促销、使用优惠券。
     * 满减优惠券 5.1a 全场范围
     */
    private void consumeWithoutPromotionButUseCoupon_2() throws InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_C_1);
        commodityList.add(normalCommodity_C_2);
        commodityList.add(combinationCommodity_C_1);
        commodityList.add(multiPackagingCommodity_C_1);
        commodityList.add(serviceCommodity_C_1);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, null, retailTradePromoting);
        retailTradeSell.setID(4567L);
//        retailTradeSell.setVipID(vip.getID().intValue());
        Assert.assertTrue("零售单中无商品参与促销，计算过程应该为空！", retailTradePromoting.getListSlave1().size() == 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        System.out.println("未使用优惠券时系统计算的价格： " + systemCalculateAmount);

        if (couponPresenter.getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("通过优惠券查找它是属于哪种优惠券时失败！", false);
        } else {
            double amount = CouponCalculator.calculateAmountUsingCoupon(cashCoupon, systemCalculateAmount, commodityList, retailTradeSell);
            retailTradeSell.setAmount(amount);
            selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail() - 5;

            System.out.println("手动计算使用优惠券后的价格： " + selfCalculateAmount);
            System.out.println("使用优惠券后系统计算的价格： " + retailTradeSell.getAmount());
            Assert.assertTrue("计算价格不一致！", GeneralUtil.formatToShow(selfCalculateAmount).equals(GeneralUtil.formatToShow(retailTradeSell.getAmount())));
        }
    }

    /**
     * 5、消费1单（单品9、多包装商品、服务商品、组合商品），无促销、使用优惠券。
     * 满折优惠券 5.2a 指定范围
     */
    private void consumeWithoutPromotionButUseCoupon_3() {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_C_1);
        commodityList.add(normalCommodity_C_2);
        commodityList.add(combinationCommodity_C_1);
        commodityList.add(multiPackagingCommodity_C_1);
        commodityList.add(serviceCommodity_C_1);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, null, retailTradePromoting);
        retailTradeSell.setID(4567L);
        Assert.assertTrue("零售单中无商品参与促销，计算过程应该为空！", retailTradePromoting.getListSlave1().size() == 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        System.out.println("未使用优惠券时系统计算的价格： " + systemCalculateAmount);

        double DiscountOfAmount = (random.nextInt(10) + 0.1) / 10;

        Date date = new Date();
        Date dt1 = new Date(date.getTime() - 100000000L);//得到昨天的日期
        Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

        Coupon coupon = new Coupon();
        coupon.setID(1L);
        coupon.setStatus(0);
        coupon.setType(Coupon.EnumCouponCardType.ECCT_DISCOUNT.getIndex());//满折
        coupon.setBonus(0);//不需要积分领取
        coupon.setLeastAmount(normalCommodity_C_2.getPriceRetail() - random.nextInt(10));//起用金额
        coupon.setReduceAmount(0);//减免金额
        coupon.setDiscount(DiscountOfAmount);//打折额度
        coupon.setTitle("测试创建满十打九折优惠券");
        coupon.setColor("Color010");
        coupon.setDescription("测试专用");
        coupon.setPersonalLimit(10);
        coupon.setWeekDayAvailable(0);//没有星期限制
        coupon.setBeginTime("00:00:00");
        coupon.setEndTime("23:59:59");
        coupon.setBeginDateTime(dt1);
        coupon.setEndDateTime(dt2);
        coupon.setQuantity(100);//库存数量
        coupon.setRemainingQuantity(99);//当前剩余库存数量
        coupon.setScope(1);//0=全部商品，1=部分商品（这些商品记录在T_CouponScope中）

        CouponScope couponScope = new CouponScope();
        couponScope.setID(coupon.getID());
        couponScope.setCommodityID(normalCommodity_C_2.getID().intValue());//限定的商品id
        couponScope.setCommodityName(normalCommodity_C_2.getName());

        ArrayList<CouponScope> couponScopes = new ArrayList<>();
        couponScopes.add(couponScope);
        coupon.setListSlave1(couponScopes);

        double amount = CouponCalculator.calculateAmountUsingCoupon(coupon, systemCalculateAmount, commodityList, retailTradeSell);
        retailTradeSell.setAmount(amount);
        selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + (normalCommodity_C_2.getPriceRetail() * DiscountOfAmount) + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();

        System.out.println("手动计算使用优惠券后的价格： " + selfCalculateAmount);
        System.out.println("使用优惠券后系统计算的价格： " + retailTradeSell.getAmount());
        Assert.assertTrue("计算价格不一致！", GeneralUtil.formatToShow(selfCalculateAmount).equals(GeneralUtil.formatToShow(retailTradeSell.getAmount())));
    }

    /**
     * 5、消费1单（单品9、多包装商品、服务商品、组合商品），无促销、使用优惠券。
     * 满折优惠券5.2b 全场
     */
    private void consumeWithoutPromotionButUseCoupon_4() {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_C_1);
        commodityList.add(normalCommodity_C_2);
        commodityList.add(combinationCommodity_C_1);
        commodityList.add(multiPackagingCommodity_C_1);
        commodityList.add(serviceCommodity_C_1);

        retailTradePromoting = new RetailTradePromoting();
        double selfCalculateAmount = normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail();
        System.out.println("计算前商品价格" + selfCalculateAmount);
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, null, retailTradePromoting);
        retailTradeSell.setID(4567L);
        Assert.assertTrue("零售单中无商品参与促销，计算过程应该为空！", retailTradePromoting.getListSlave1().size() == 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        System.out.println("未使用优惠券时系统计算的价格： " + systemCalculateAmount);

        if (couponPresenter.getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("通过优惠券查找它是属于哪种优惠券时失败！", false);
        } else {
            double amount = CouponCalculator.calculateAmountUsingCoupon(discountCoupon, systemCalculateAmount, commodityList, retailTradeSell);
            retailTradeSell.setAmount(amount);
            selfCalculateAmount = (normalCommodity_C_1.getPriceRetail() + normalCommodity_C_2.getPriceRetail() + combinationCommodity_C_1.getPriceRetail() + multiPackagingCommodity_C_1.getPriceRetail() + serviceCommodity_C_1.getPriceRetail()) * 0.9;

            System.out.println("手动计算使用优惠券后的价格： " + selfCalculateAmount);
            System.out.println("使用优惠券后系统计算的价格： " + retailTradeSell.getAmount());
            Assert.assertTrue("计算价格不一致！", GeneralUtil.formatToShow(selfCalculateAmount).equals(GeneralUtil.formatToShow(retailTradeSell.getAmount())));
        }
    }

    /**
     * //        6、消费1单（单品、多包装商品、服务商品、组合商品），有促销、使用优惠券。
     */
    private void consumeWithPromotionWithCoupon() throws CloneNotSupportedException, InterruptedException {
        commodityList = new ArrayList<>();
        commodityList.add(normalCommodity_P_1);
        commodityList.add(normalCommodity_P_2);
        commodityList.add(combinationCommodity_P_1);
        commodityList.add(multiPackagingCommodity_P_1);
        commodityList.add(serviceCommodity_P_1);

        double reducingOfAmount = (double) random.nextInt(1000) / 100.000000d + 1.000000d;

        //创建一个指定商品(normalCommodity_P_2)满10减5的促销
        Promotion promotion = BasePrommotionTest.DataInput.getCashReducingOfAmountPromotionOnSpecifiedCommodity(normalCommodity_P_2.getPriceRetail() - random.nextInt(10), reducingOfAmount, String.valueOf(normalCommodity_P_2.getID()));
        promotion = BasePrommotionTest.createSyncViaHttp(promotion, promotionHttpEvent, promotionHttpBO);

        //创建一个促销集合
        promotionList = new ArrayList<>();
        promotionList.add(promotion);

        retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTradeSell = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("零售单中有商品参与促销，计算过程不应该为空！", retailTradePromoting.getListSlave1().size() != 0);
        double systemCalculateAmount = retailTradeSell.getAmount();//系统计算后的价格
        double selfCalculateAmount = normalCommodity_P_1.getPriceRetail() + (normalCommodity_P_2.getPriceRetail() - reducingOfAmount) + multiPackagingCommodity_P_1.getPriceRetail() + combinationCommodity_P_1.getPriceRetail() + serviceCommodity_P_1.getPriceRetail();
        System.out.println("系统计算后的价格为：" + systemCalculateAmount);
        System.out.println("手动的计算价格为：" + selfCalculateAmount);

        double amount = CouponCalculator.calculateAmountUsingCoupon(discountCoupon, systemCalculateAmount, commodityList, retailTradeSell);
        retailTradeSell.setAmount(amount);
        //使用优惠券后手动计算的价格
        selfCalculateAmount = selfCalculateAmount * 0.9;

        System.out.println("手动计算使用优惠券后的价格： " + selfCalculateAmount);
        System.out.println("使用优惠券后系统计算的价格： " + retailTradeSell.getAmount());
        Assert.assertTrue("计算价格不一致！", GeneralUtil.formatToShow(selfCalculateAmount).equals(GeneralUtil.formatToShow(retailTradeSell.getAmount())));
    }

    @Test
    public void test_pay() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录pos与staff
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(),
                Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //创建测试需要的优惠券、促销、商品
        createTestingData();

        //不使用优惠券以及无促销
        consumeWithoutPromotionAndCoupon();

        //有指定商品满减促销，无优惠券
        consumeWithPromotionButNoCoupon_1();

        //有全场商品满减促销，无优惠券
        consumeWithPromotionButNoCoupon_2();

        //有指定商品满折促销，无优惠券
        consumeWithPromotionButNoCoupon_3();

        //有全场商品满折促销，无优惠券
        consumeWithPromotionButNoCoupon_4();

        //无促销，有指定是商品满减优惠券
        consumeWithoutPromotionButUseCoupon_1();

        //无促销，有全场商品满减优惠券
        consumeWithoutPromotionButUseCoupon_2();

        //无促销，有指定商品满折优惠券
        consumeWithoutPromotionButUseCoupon_3();

        //无促销，有全场商品满折优惠券
        consumeWithoutPromotionButUseCoupon_4();

        //有促销，有优惠券
        consumeWithPromotionWithCoupon();
    }
}
