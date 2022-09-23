package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.model.promotion.Promotion;
import wpos.model.promotion.PromotionCalculator;
import wpos.presenter.*;
import wpos.utils.DatetimeUtil;
import wpos.utils.GeneralUtil;
import wpos.utils.Shared;
import wpos.utils.WXPayUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

//支付和退款的测试
public class TradeSIT extends BaseHttpTestCase {
    private static PromotionSQLiteBO promotionSqLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static WXPayHttpBO wxPayHttpBO = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    //
    private Map<String, String> microPayResponse = null;//微信支付responseData
    private Map<String, String> refundResponse = null;//微信退款responseData
    private RetailTrade returnRetailTradeWithRefundNO = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static WXPayHttpEvent wxPayHttpEvent = null;

    private static final int EVENT_ID_TradeSIT = 10000;
    private Promotion promotion2;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        //
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_TradeSIT);
        }
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_TradeSIT);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_TradeSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        //
        if (promotionSqLiteBO == null) {
            promotionSqLiteBO = new PromotionSQLiteBO(promotionSQLiteEvent, promotionHttpEvent);
            promotionSqLiteBO.setPromotionPresenter(promotionPresenter);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(promotionSQLiteEvent, promotionHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(commoditySQLiteEvent, commodityHttpEvent);
            commoditySQLiteBO.setCommodityPresenter(commodityPresenter);
        }
        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(null, wxPayHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
            retailTradeSQLiteBO.setRetailTradePresenter(retailTradePresenter);
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_TradeSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        logoutHttpEvent.setId(EVENT_ID_TradeSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSqLiteBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSqLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);

    }

    @Override
    @AfterClass
    public void tearDown() {
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
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();

            microPayResponse = event.getMicroPayResponse();
            refundResponse = event.getRefundResponse();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("TradeSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，TradeSIT.onRetailTradeHttpEvent()不用处理");
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            System.out.println("#########################################################TradeSIT onRetailTradeSQLiteEvent");
            event.onEvent();
            if (BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync == event.getEventTypeSQLite()) {
                returnRetailTradeWithRefundNO = (RetailTrade) event.getBaseModel1();
                System.out.println("临时退货零售单退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    //支付:1.创建4种促销.(满减，满折，全场,单一商品)并令其生效
//    2.通过商品和数量，促销方式计算出最低成本的零售单。
//    3.通过零售单进行支付
//    4.上传零售单
    @Test
    public void testPayment() throws Exception {
        Shared.printTestMethodStartInfo();

        //本测试使用到的商品。均是单品(创建促销只能使用单品，不能是组合商品和多包装)
        final String commodityAID = "119";
        final String commodityBID = "1";
        final String commodityCID = "2";
        final String commodityDID = "3";
        final String commodityEID = "27";
        final String commodityFID = "28";
        final String commodityGID = "29";

        if(!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            System.out.println(posLoginHttpBO.getHttpEvent().getStatus() + "\t" + staffLoginHttpBO.getHttpEvent().getStatus());
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        syncTime(EVENT_ID_TradeSIT);
        downloadCommodity();
//        1.创建4种促销.(满减，满折，全场,单一商品)并令其生效,然后进行支付和退款
        System.out.println("------------- case 1: 当前促销（商品119满120元减20元的促销活动），进行支付和退款");
        Commodity commodity = new Commodity();

        Promotion promotion = PromotionSIT.DataInput.getCashReducingPromotionAllCommodity();
        promotion.setCommodityIDs(commodityAID);
        List<Promotion> promotions1 = new ArrayList<Promotion>();
        promotions1.add(promotion);
        // TODO 下面报错，这里barcode为null，会报错
        RetailTrade retailTrade1 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityBID, commodityCID, commodityDID), createPromotion(promotions1));
        //商品ID：119，总金额500 - 20; 商品ID:1 总金额120； 商品ID:2 总金额：35； 商品ID：3，总金额0.125   通过促销后该零售单总金额为：635.125，四舍五入取两位为635.13
        Assert.assertTrue(Math.abs(retailTrade1.getAmount() - 635.13) < commodity.TOLERANCE, "通过促销计算的零售单金额错误！" + retailTrade1.getAmount() + "!=635.13");
        //获取通过促销创建的零售单
        paymentAndRefund(retailTrade1);
        //
        System.out.println("-------------  case2: 当前促销(商品119满100元打9折的促销)，进行支付和退款");
        Promotion promotion2 = PromotionSIT.DataInput.getDiscountPromotionSpecifiedCommodity();
        promotion2.setCommodityIDs(commodityAID);
        List<Promotion> promotions2 = new ArrayList<Promotion>();
        promotions2.add(promotion2);
        RetailTrade retailTrade2 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityBID, commodityCID, commodityDID), createPromotion(promotions2));
        //商品ID：119，总金额500; 商品ID:1 总金额120； 商品ID:2 总金额：35； 商品ID：3，总金额0.125     优惠后的价格为 589.6125(取两位小数)
        Assert.assertTrue(Math.abs(retailTrade2.getAmount() - 589.61) < commodity.TOLERANCE, "商品111满100元打9折的促销金额计算错误！");
        paymentAndRefund(retailTrade2);
        //
        System.out.println("-------------  case3: 当前促销(全场商品打9折的促销),进行支付和退款");
        Promotion promotion3 = PromotionSIT.DataInput.getDiscountPromotionAllCommodity();
        List<Promotion> promotions3 = new ArrayList<Promotion>();
        promotions3.add(promotion3);
        RetailTrade retailTrade3 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityGID, commodityDID), createPromotion(promotions3));
        //商品119的总金额为500，商品28的总金额为120，商品29的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125    促销全场九折，优惠后的价格为666.1125(取两位小数)
        Assert.assertTrue(Math.abs(retailTrade3.getAmount() - 666.11) < commodity.TOLERANCE, "全场商品打9折的促销金额计算错误！");
        paymentAndRefund(retailTrade3);

        System.out.println("------------- case4: 当前无促销进行支付和退款");
        RetailTrade retailTrade4 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityGID, commodityDID), null);
        //商品119的总金额为500，商品28的总金额为120，商品29的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125(取两位小数)
        Assert.assertTrue(Math.abs(retailTrade4.getAmount() - 740.13) < commodity.TOLERANCE, "全场商品无促销金额计算错误！");
        paymentAndRefund(retailTrade4);

        System.out.println("------------- case5: 指定商品28，商品27进行满100减40的促销.下进行微信支付和退款");
        Promotion promotion5 = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        promotion5.setCommodityIDs(commodityFID + "," + commodityEID);
        List<Promotion> promotions5 = new ArrayList<Promotion>();
        promotions5.add(promotion5);
        RetailTrade retailTrade5 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityEID, commodityDID), createPromotion(promotions5));
        //商品119的总金额为500，商品28的总金额为120，商品27的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125    促销指定商品28和27满120减40，优惠后的价格为700.125(取两位小数)
        Assert.assertTrue(Math.abs(retailTrade5.getAmount() - 700.13) < commodity.TOLERANCE, "指定商品28，商品27进行满100减40的促销金额计算错误！");
        paymentAndRefund(retailTrade5);

        System.out.println("------------- case6: 指定商品A满100减40，商品B满100打九折下进行微信支付和退款");
        List<Promotion> promotions6 = new ArrayList<Promotion>();
        Promotion promotion6_1 = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        promotion6_1.setCommodityIDs(commodityFID);
        promotions6.add(promotion6_1);

        Promotion promotion6_2 = PromotionSIT.DataInput.getDiscountPromotionSpecifiedCommodity();
        promotion6_2.setCommodityIDs(commodityAID);
        promotions6.add(promotion6_2);
        RetailTrade retailTrade6 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityEID, commodityDID), createPromotion(promotions6));
        //商品119的总金额为500，商品28的总金额为120，商品27的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125     优惠后的价格为630.125(取两位小数)(取两位小数)
        Assert.assertTrue(Math.abs(retailTrade6.getAmount() - 630.11) < commodity.TOLERANCE, "指定商品A满100减40，商品B满100打9折的促销金额计算错误！");
        paymentAndRefund(retailTrade6);

        System.out.println("------------- case7: 当前促销有指定商品，全场等满折满减活动");
        List<Promotion> promotions7 = new ArrayList<Promotion>();
        Promotion promotion7_1 = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        promotion7_1.setCommodityIDs(commodityFID);
        promotions7.add(promotion7_1);
        //
        Promotion promotion7_2 = PromotionSIT.DataInput.getDiscountPromotionAllCommodity();
        promotions7.add(promotion7_2);
        //
        Promotion promotion7_3 = PromotionSIT.DataInput.getCashReducingPromotionAllCommodity();
        promotion7_3.setCommodityIDs(commodityAID);
        promotions7.add(promotion7_3);

        RetailTrade retailTrade7 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityEID, commodityDID), createPromotion(promotions7));
        //商品119的总金额为500，商品28的总金额为120，商品27的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125
        // 促销方案有三种.会计算出最优的：promotion7_1满减的零售单优惠价为：320.125； promotion7_2满折的零售单优惠价为：324.1125，promotion7_3满减的零售单优惠价为340.125(取两位小数)
        System.out.println("零售单的价格：" + retailTrade7.getAmount());

        Assert.assertTrue(Math.abs(retailTrade7.getAmount() - 630.11) < commodity.TOLERANCE, "当前促销有指定商品，全场等满折满减的促销金额计算错误！");
        paymentAndRefund(retailTrade7);

        System.out.println("------------- case 8: 当前促销为指定商品28满200元打40元促销，满足不了促销阈值无法进行优惠");
        List<Promotion> promotions8 = new ArrayList<Promotion>();
        Promotion promotion8 = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        promotion8.setCommodityIDs(commodityFID);
        promotion8.setExcecutionThreshold(200);
        promotions8.add(promotion8);

        RetailTrade retailTrade8 = getCalculationPromotionOfRetailTrade(getCommodityList(commodityAID, commodityFID, commodityEID, commodityDID), createPromotion(promotions8));
        //商品119的总金额为500，商品28的总金额为120，商品27的总金额为120，商品3的总金额为0.125      此零售单的原单价为740.125. 因指定商品不满足金额无法进行促销
        System.out.println("零售单的价格：" + retailTrade8.getAmount());

        Assert.assertTrue(Math.abs(retailTrade8.getAmount() - 740.13) < commodity.TOLERANCE, "当前促销为指定商品28满200元打40元促销金额计算错误！");
        paymentAndRefund(retailTrade8);
    }


//    7.当前促销有指定商品，全场等满折满减活动


    //    1.POS和staff登录
//    2.沙箱环境下根据零售单金额进行微信支付
//    3.创建SQLite零售单
//    4.上传零售单
//    5.查询零售单
//    6.根据查询到的零售单创建退货零售单
//    7.根据退货零售单微信交易单号进行退款申请
//    8.更新退货零售单(update退款交易单号)
//    9.上传退货零售单
    //进行支付和退款
    protected void paymentAndRefund(RetailTrade retailTrade) throws Exception {
        retailTrade.setSourceID(-1);//通过促销计算出的零售单SourceID是0，而上传零售单后会成-1.不设置成-1会compareto失败
        RetailTrade microPayRT = (RetailTrade) retailTrade.clone();
        microPayRT.setAmount(WXPayUtil.formatAmount(microPayRT.getAmount()));
        wxMicroPay(microPayRT);

        // 3.创建SQLite零售单
        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? Constants.submchid : microPayResponse.get("sub_mch_id"));
        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
//        retailTrade.setWxRefundDesc(wxData.get("refund_desc") == null ? "" : wxData.get("refund_desc"));
        RetailTrade retailTradeR1Condition = new RetailTrade();
        retailTradeR1Condition.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        RetailTrade temRetailTrade = createTmpRetailTrade(retailTrade);

        // 4.上传零售单
        createRetailTrade(temRetailTrade);


        // 5.查询零售单
        retailTradeR1Condition.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        retailTradeR1Condition.setQueryKeyword(retailTrade.getSn());
        retailTradeR1Condition.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTradeR1Condition.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        List<RetailTrade> rtList = retrieveRetailTrade(retailTradeR1Condition);

        // 6.根据查询到的零售单创建退货零售单(选择全部商品和计算金额)
        RetailTrade returnRetailTrade = createReturnCommRetailTrade(rtList.get(0));
        System.out.println("本地退货零售单：" + returnRetailTrade);
        System.out.println("本地退货零售单退款单号：" + returnRetailTrade.getWxRefundNO());

        // 7.根据退货零售单微信交易单号进行退款申请
        RetailTrade rt = (RetailTrade) returnRetailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxRefund(rt);

        // 8.更新退货零售单(update退款交易单号)
        returnRetailTradeWithRefundNO = updateRetailTrade(returnRetailTrade);
        System.out.println("微信退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());

        // 9.上传退货零售单
        returnRetailTradeWithRefundNO.setDatetimeStart(new Date());
        returnRetailTradeWithRefundNO.setDatetimeEnd(new Date());
        createRetailTrade(returnRetailTradeWithRefundNO);
    }

    //通过促销计算得出零售单
    protected RetailTrade getCalculationPromotionOfRetailTrade(List<Commodity> commodities, List<BaseModel> promotions) throws ParseException {
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        PromotionCalculator promotionCalculator = new PromotionCalculator();

        RetailTrade sellRetailTrade = promotionCalculator.sell(commodities, promotions, retailTradePromoting);
        retailTradePromoting.getID();
        System.out.println("--------- 通过促销计算出的零售单是：" + sellRetailTrade);

        RetailTrade retailTradeNew = RetailTradeSIT.DataInput.getRetailTrade(retailTradePresenter, retailTradeCommodityPresenter);
        retailTradeNew.setPaymentType(4); // 微信支付
        retailTradeNew.setAmountCash(0.000000d);
        retailTradeNew.setAmountWeChat(sellRetailTrade.getAmount());
        retailTradeNew.setDatetimeStart(new Date());
        retailTradeNew.setDatetimeEnd(new Date());
//        retailTradeNew.set

        retailTradeNew.setAmount(sellRetailTrade.getAmount());


        int maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        List<RetailTradeCommodity> retailTradeCommoditieOlds = (List<RetailTradeCommodity>) sellRetailTrade.getListSlave1();
        for (int i = 0; i < retailTradeCommoditieOlds.size(); i++) {
            Random r = new Random();
            RetailTradeCommodity retailTradeCommodityOld = retailTradeCommoditieOlds.get(i);
            retailTradeCommodityOld.setTradeID((long) retailTradeNew.getID());
            retailTradeCommodityOld.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodityOld.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodityOld.setID(maxTextIDInSQLite + i);
            retailTradeCommodityOld.setBarcodeID(1);
            retailTradeCommodityOld.setDiscount(2);
        }
        retailTradeNew.setListSlave1(retailTradeCommoditieOlds);

        return retailTradeNew;
    }

    //     根据零售单金额进行微信支付操作
    public void wxMicroPay(RetailTrade retailTrade) throws InterruptedException {
        WXPayInfo wxPayInfo = new WXPayInfo(); // 由于是沙箱环境，wxPayInfo传过去也是没意义的
        wxPayInfo.setAuth_code("134617607342397775");
        wxPayInfo.setTotal_fee(GeneralUtil.formatToShow(retailTrade.getAmount()));
        // 沙箱支付不稳定，如果失败则尝试多一次
        int tryMoreTime = 1;
        do {
            wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            wxPayHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_WXPay_MicroPay);
            if (!wxPayHttpBO.microPayAsync(wxPayInfo)) {
                Assert.assertTrue(false, "调用microPayAsync失败！");
            }
            long lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.assertTrue(false, "微信支付超时...");
            }
        } while(tryMoreTime-- > 0 && wxPayHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        Assert.assertTrue(wxPayHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "微信支付失败！" + wxPayHttpBO.getHttpEvent().printErrorInfo());
    }

    //     根据零售单金额进行微信支付操作
    public void wxRefund(RetailTrade retailTradeCreated) throws InterruptedException {
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!wxPayHttpBO.refundAsync(retailTradeCreated)) {
            Assert.assertTrue(false, "调用refundAsync失败！");
        }

        long lTimeOut2 = 60;

        while (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut2-- > 0) {
            Thread.sleep(1000);
        }

        if (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "微信退款超时...");
        }
    }

    //获取商品List
    public List<Commodity> getCommodityList(String id1, String id2, String id3, String id4) {
        Commodity baseModel = new Commodity();
        List<Commodity> commodityList = new ArrayList<Commodity>();

        baseModel.setSql("where F_id in (%s,%s,%s,%s)");
        baseModel.setConditions(new String[]{id1, id2, id3, id4});
        baseModel.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, baseModel);
        if (commodityList == null || commodityList.size() == 0) {
            Assert.assertTrue(false, "SQLite查找商品为空，不足以接下来的测试！");
        }
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(10);
        }

        return commodityList;
    }

    //同步所有的商品。
    public void downloadCommodity() throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);//...将来优化成，所有测试启动前，先将所有商品同步

        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue(false, "调用同步商品失败!");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData, "请求服务器同步Commodity超时!");

        Assert.assertTrue(commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "同步所有的商品失败！");
        Assert.assertTrue(commoditySQLiteEvent.getListMasterTable() != null, "同步SQLite的商品为空！");

        int total = Integer.valueOf(commoditySQLiteBO.getHttpEvent().getCount());
        int count = total / Integer.valueOf(commodity.getPageSize());
//        int remainder = total % Integer.valueOf(commodity.getPageSize());

        for (int i = 1; i <= count; i++) {
            //4.同步商品信息
            System.out.println("第一次调用同步商品方法");
            commodity.setPageIndex(String.valueOf(i + 1));
            commodity.setPageSize(Shared.PAGE_SIZE_DEFAULT_MAX);//...将来优化成，所有测试启动前，先将所有商品同步

            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                Assert.assertTrue(false, "同步失败!");
            }
            lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                    commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                    commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                Assert.assertTrue(false, "请求服务器同步Commodity超时!");
            }
        }
    }

    //网页模拟创建一个促销活动，并让其生效返回
    public List<BaseModel> createPromotion(List<Promotion> promotions) throws Exception {
        List<BaseModel> promotionList = new ArrayList<>();
        for (int i = 0; i < promotions.size(); i++) {
            Promotion promotion = promotions.get(i);
            promotion.setInt1(1);
            promotionHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
                Assert.assertTrue(false, "创建失败!");
            }
            long lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.assertTrue(false, "请求服务器创建Promotion超时!");
            }

            Assert.assertTrue(promotionHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建Promotion失败！");

            BaseModel baseModel = promotionHttpBO.getHttpEvent().getBaseModel1();
            Assert.assertTrue(baseModel != null, "服务器返回的创建对象是空,或是解析失败！");
            Promotion p = (Promotion) baseModel;
            Date dt1 = DatetimeUtil.addDays(new Date(), -1);// new Date(date.getTime() - 100000000L);//得到昨天的日期
            p.setDatetimeStart(dt1);

            promotionList.add(p);
        }

        System.out.println(promotionList);

        return promotionList;
    }

    //获取零售单对象
//    private RetailTrade getRetailTrade() throws CloneNotSupportedException, ParseException {
//        long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(Constants.FIELD_NAME_syncDatetime);
//        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(Constants.FIELD_NAME_syncDatetime);
//        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//
//        RetailTrade retailTradeOld = RetailTradeJUnit.DataInput.getRetailTrade(maxFrameIDInSQLite, maxTextIDInSQLite);
//        return retailTradeOld;
//    }

    //上传零售单
    public void createRetailTrade(RetailTrade retailTrade) throws Exception {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(retailTrade);//用于待会删除的临时零售单
        retailTradeHttpBO.getHttpEvent().setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
        retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
        retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
        if (!retailTradeHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue(false, "创建失败！");
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
            if (retailTradeHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated) {
                RetailTrade master = (RetailTrade) retailTradeHttpBO.getHttpEvent().getBaseModel1();
                Assert.assertTrue(master != null, "零售单对象为空！");

                retailTrade.setIgnoreIDInComparision(true);
                if ((double) Math.round(retailTrade.getAmount() * 10000) / 10000 - master.getAmount() < BaseModel.TOLERANCE) {
                    master.setAmount(retailTrade.getAmount());
                }
                Assert.assertTrue(retailTrade.compareTo(master) == 0, "服务器返回的对象和上传的对象不相等！");
            }
        }

        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "测试失败！原因：超时");
        }

        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！" + retailTradeSQLiteBO.getSqLiteEvent().printErrorInfo());
        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue(master != null, "零售单对象为空！");

        retailTrade.setIgnoreIDInComparision(true);
        if ((double) Math.round(retailTrade.getAmount() * 10000) / 10000 - master.getAmount() < BaseModel.TOLERANCE) {
            master.setAmount(retailTrade.getAmount());
        }
        Assert.assertTrue(retailTrade.compareTo(master) == 0, "服务器返回的对象和上传的对象不相等！");
    }

    //上传临时的零售单，仅保存在本地中
    public RetailTrade createTmpRetailTrade(RetailTrade retailTradeOld) throws CloneNotSupportedException {
        retailTradeOld.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
        retailTradeOld.setAmount(GeneralUtil.sumN(retailTradeOld.getAmountCash(), retailTradeOld.getAmountAlipay(), retailTradeOld.getAmountWeChat(), //
                retailTradeOld.getAmount1(), retailTradeOld.getAmount2(), retailTradeOld.getAmount3(), retailTradeOld.getAmount4(), retailTradeOld.getAmount5()));
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld)) {
            Assert.assertTrue(false, "创建临时数据失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "创建本地零售单超时...");
        }

        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "craeteMasterSlave返回的错误码不正确！");
        //
        RetailTrade retailTradeCreate = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "R1返回的错误码不正确！");
        Assert.assertTrue(retailTrade != null && retailTrade.compareTo(retailTradeOld) == 0, "插入临时数据失败！");

        return retailTrade;
    }

    //创建临时退货单
    public RetailTrade createReturnCommRetailTrade(RetailTrade returnCommRetailTrade) {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
//        double totalMoney = 0.00;
        //
        //
//        for (int i = 0; i < returnCommRetailTrade.getListSlave1().size(); i++) {
//            ((List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1()).get(i).setIsGift(-1);
//            totalMoney += ((List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1()).get(i).getPriceOriginal() * Integer.valueOf(((List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1()).get(i).getNO());
//        }
        //
//        returnCommRetailTrade.setAmount(totalMoney);
        returnCommRetailTrade.setSourceID(returnCommRetailTrade.getID().intValue());
        returnCommRetailTrade.setID(retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class));// 本地创建retailtrade pos_sn
        returnCommRetailTrade.setLocalSN(retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class));
        returnCommRetailTrade.setDatetimeStart(new Date());
        returnCommRetailTrade.setDatetimeEnd(new Date());

        List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1();
        for (int i = 0; i < rtcList.size(); i++) {
            rtcList.get(i).setID(retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class) + i);
        }

        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, returnCommRetailTrade)) {
            Assert.assertTrue(false, "创建本地退货零售单失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "创建本地退货零售单超时...");
        }

        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建本地退货零售单失败");
        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1() != null, "创建本地退货零售单位null");

        return (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
    }

    //更新零售单的退款交易单号
    public RetailTrade updateRetailTrade(RetailTrade returnRetailTrade) {
        returnRetailTrade.setWxRefundNO(refundResponse.get("out_refund_no"));//退款交易单号
        System.out.println("----------" + refundResponse.get("out_refund_no"));
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        if (!retailTradeSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, returnRetailTrade)) {
            Assert.assertTrue(false, "插入退款单号失败！！");
        }
        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "插入退款单号失败！！");

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "更新本地零售单超时...");
        }

        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "修改临时零售单失败！");
        RetailTrade updateReturnRetailTrade = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue(updateReturnRetailTrade != null, "更新本地零售单返回null");

        return updateReturnRetailTrade;
    }

    //查询旧零售单
    public List<RetailTrade> retrieveRetailTrade(RetailTrade retailTrade) {
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
            Assert.assertTrue(false, "查询旧零售单失败！！");
        }

        long lTimeOut2 = 60;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut2-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "查询旧单超时..." + retailTradeSQLiteBO.getSqLiteEvent().getStatus());
        }

        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "查询零售单错误：");
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradeSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(rtList != null, "零售单为null!!!");

        System.out.println("零售单list：" + rtList);
        return rtList;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_TradeSIT) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                    event.setRequestType(null);
                    Ntp ntp = (Ntp) event.getBaseModel1();
                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
                }
            } else {
                NtpHttpBO.TimeDifference = 0;
            }
        } else {
        }
    }
}
