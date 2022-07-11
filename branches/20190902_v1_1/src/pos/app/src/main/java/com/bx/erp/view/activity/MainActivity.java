//package com.bx.erp.view.activity;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.RemoteException;
//import android.support.annotation.RequiresApi;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.AppApplication;
//import com.bx.erp.R;
//import com.bx.erp.bo.BaseHttpBO;
//import com.bx.erp.bo.BaseSQLiteBO;
//import com.bx.erp.bo.CommodityHttpBO;
//import com.bx.erp.bo.CommoditySQLiteBO;
//import com.bx.erp.bo.NtpHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
//import com.bx.erp.bo.RetailTradeHttpBO;
//import com.bx.erp.bo.RetailTradeSQLiteBO;
//import com.bx.erp.bo.StaffHttpBO;
//import com.bx.erp.bo.VipHttpBO;
//import com.bx.erp.bo.VipSQLiteBO;
//import com.bx.erp.bo.WXPayHttpBO;
//import com.bx.erp.cache.LocalCache;
//import com.bx.erp.common.ActivityController;
//import com.bx.erp.common.DayEndGenerateRetailTradeAggregationReceiver;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.BrandHttpEvent;
//import com.bx.erp.event.CommodityHttpEvent;
//import com.bx.erp.event.PosHttpEvent;
//import com.bx.erp.event.PosSQLiteEvent;
//import com.bx.erp.event.RetailTradeAggregationHttpEvent;
//import com.bx.erp.event.RetailTradeEvent;
//import com.bx.erp.event.RetailTradeHttpEvent;
//import com.bx.erp.event.ReturnCommodityHttpEvent;
//import com.bx.erp.event.StaffHttpEvent;
//import com.bx.erp.event.UI.BaseSQLiteEvent;
//import com.bx.erp.event.UI.BrandSQLiteEvent;
//import com.bx.erp.event.UI.CommoditySQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
//import com.bx.erp.event.UI.VipSQLiteEvent;
//import com.bx.erp.event.VipHttpEvent;
//import com.bx.erp.event.WXPayHttpEvent;
//import com.bx.erp.helper.Constants;
//import com.bx.erp.helper.ScanGun;
//import com.bx.erp.http.HttpRequestManager;
//import com.bx.erp.http.HttpRequestManager.EnumDomainType;
//import com.bx.erp.http.HttpRequestUnit;
//import com.bx.erp.model.Barcodes;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Commodity;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.PackageUnit;
//import com.bx.erp.model.RetailTrade;
//import com.bx.erp.model.RetailTradeAggregation;
//import com.bx.erp.model.RetailTradeCommodity;
//import com.bx.erp.model.RetailTradePromoting;
//import com.bx.erp.model.RetailTradePromotingFlow;
//import com.bx.erp.model.Vip;
//import com.bx.erp.model.wx.WxVip;
//import com.bx.erp.model.wx.WxVipCardDetail;
//import com.bx.erp.presenter.BarcodesPresenter;
//import com.bx.erp.presenter.CommodityPresenter;
//import com.bx.erp.presenter.PackageUnitPresenter;
//import com.bx.erp.presenter.PromotionPresenter;
//import com.bx.erp.presenter.RetailTradeCommodityPresenter;
//import com.bx.erp.presenter.RetailTradePresenter;
//import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
//import com.bx.erp.presenter.RetailTradePromotingPresenter;
//import com.bx.erp.promotion.PromotionCalculator;
//import com.bx.erp.utils.DatetimeUtil;
//import com.bx.erp.utils.FieldFormat;
//import com.bx.erp.utils.GeneralUtil;
//import com.bx.erp.utils.NetworkUtils;
//import com.bx.erp.utils.StringUtils;
//import com.bx.erp.utils.WXPayUtil;
//import com.bx.erp.view.adapter.CommodityRecyclerViewAdapter;
//import com.bx.erp.view.adapter.DialogCheckListCommodityRecyclerViewAdapter;
//import com.bx.erp.view.adapter.DialogCheckListOrderRecyclerViewAdapter;
//import com.bx.erp.view.adapter.DialogCommodityRecyclerViewAdapter;
//import com.silencedut.taskscheduler.TaskScheduler;
//import com.sunmi.printerhelper.utils.AidlUtil;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
//@TargetApi(Build.VERSION_CODES.CUPCAKE)
//public class MainActivity extends BaseActivity implements View.OnClickListener {
//    private static Logger log = Logger.getLogger(MainActivity.class);
//
//    /**
//     * 最短的退货时间。必须在这个时间经过之后，才能对零售单进行退货。以秒为单位
//     */
//    private static final int MIN_Time_InSecond_ReturnRetailTrade = 120;
//
//    private static NetworkUtils networkUtils = new NetworkUtils();
//
//    private List<Object> objectList = new ArrayList<Object>();
//
//    private PromotionCalculator promotionCalculator = new PromotionCalculator();
//    private BarcodesPresenter barcodesPresenter;
//    private RetailTradePresenter retailTradePresenter;
//    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
//    private CommodityPresenter commodityPresenter;
//    private PackageUnitPresenter packageUnitPresenter;
//    private PromotionPresenter promotionPresenter;
//    private RetailTradePromotingPresenter retailTradePromotingPresenter;
//    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
//
//    private static RetailTradeHttpBO retailTradeHttpBO = null;
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
//    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
//
//    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
//    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
//
//    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
//    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
//
//    private static VipHttpBO vipHttpBO = null;
//    private static VipSQLiteBO vipSQLiteBO = null;
//    private static VipHttpEvent vipHttpEvent = null;
//    private static VipSQLiteEvent vipSQLiteEvent = null;
//
//    private static WXPayHttpBO wxPayHttpBO = null;
//    private static WXPayHttpEvent wxPayHttpEvent = null;
//
//    private static CommodityHttpBO commodityHttpBO = null;
//    private static CommoditySQLiteBO commoditySQLiteBO = null;
//    private static CommodityHttpEvent commodityHttpEvent = null;
//    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
//
//    private static ReturnCommodityHttpEvent returnCommodityHttpEvent = null;
//
//    private static StaffHttpBO staffHttpBO = null;
//    private static StaffHttpEvent staffHttpEvent = null;
//
////    private HttpRequestScheduler hrs;
//
//    @BindView(R.id.rv_commodity)
//    RecyclerView rvCommodity;
//    @BindView(R.id.scan_barcode_text)
//    TextView scan_barcode_text;
//    @BindView(R.id.scan_barcode_search)
//    ImageView scan_barcode_search;
//    @BindView(R.id.choose_client)
//    TextView choose_client;
//    @BindView(R.id.balance_tv)
//    TextView balance_tv;
//    @BindView(R.id.show_client_name)
//    TextView show_client_name;
//    @BindView(R.id.client_more_consumption)
//    ImageView client_more_consumption;
//    @BindView(R.id.discount)
//    Spinner discount_sp;
//    @BindView(R.id.total_money)
//    TextView total_money;
//    @BindView(R.id.remove_commodity)
//    TextView remove_commodity;
//    @BindView(R.id.bill)
//    TextView bill;//取单
//    @BindView(R.id.list)
//    TextView list;//挂单
//    @BindView(R.id.btnQueryRetailTradeList)
//    TextView check_list;//查单
//    @BindView(R.id.set_up)
//    TextView set_up;
//    @BindView(R.id.inventory)
//    TextView inventory;
//    @BindView(R.id.coupons)
//    TextView coupons;
//    @BindView(R.id.logout)
//    TextView logout;
//    @BindView(R.id.show_time)
//    TextView show_time;
//    @BindView(R.id.commodity_quantity)
//    TextView commodityQuantity;//主页显示商品条数
//    @BindView(R.id.last_amount)
//    TextView lastRetailTradeAmount;
//    @BindView(R.id.last_changemoney)
//    TextView lastRetailTradeChangeMoney;
//    @BindView(R.id.last_paymenttype)
//    TextView lastRetailTradePaymentType;
//    @BindView(R.id.print_last_retailTrade)
//    TextView printLastRetailTrade;
//    //
//    TextView reprintSmallSheet; // 重打小票
//    TextView returnCommodity;
//    TextView returnAmount;//在查单UI当点击退货按钮，显示该View，然后设置文本内容“应退金额”
//    TextView checkListOriginalPrice;//查单UI中的“原价总金额”
//    TextView checkListDiscount;//查单中的折扣“折扣”
//    TextView checkListPaymentMethod;//查单中的支付方式
//    TextView checkListReceivable;//查单中的应收金额
//    TextView checkListNetReceipts;//查单中的实收金额
//    TextView confirm_returnCommodity;
//    ImageView lastPage;//查单界面，“上一页”按钮
//    ImageView nextPage;//查单界面，“下一页”按钮
//    TextView currentPage;//查单界面，当前第几页
//    TextView totalPage;//查单界面，共有多少页
//    int iTotalPage;//用于记录查单时，展示的总页数
//    TextView tvQueryRetailTrade;
//    View checkListDialogView;//用于点击隐藏软键盘
//    List<List<RetailTrade>> showCheckListRetailTradeList = new ArrayList<>();//将查询到的所有零售单list进行分成数个size为10的list，用于在分页的时候直接调用
//    //记录上次选择的起始日期，结束日期的年月日
//    private int lastSelectedYear_check_list_startDate;//
//    private int lastSelectedMonth_check_list_startDate;
//    private int lastSelectedDate_check_list_startDate;
//    private int lastSelectedYear_check_list_endDate;
//    private int lastSelectedMonth_check_list_endDate;
//    private int lastSelectedDate_check_list_endDate;
//    /**
//     * 标识是否首次检测到网络发生了故障。如果是首次，则要重新初始化零售单列表。否则，直接从SQLite中搜索零售单显示到零售单列表中
//     */
//    boolean bNetworkDisconnectedForTheFirstTime = true;
//
//    List<RetailTrade> retrieveNRetailTradeList = new ArrayList<>();
//
//    /**
//     * 记录选择的零售单的位置。
//     */
//    private int positionOfRetailTradeSelected = -1;
//    private String discount_String;
//    private RecyclerView rv_alertdialog_commodity;
//    private RetailTrade rtRetrieveCondition;//查询零售单，在该对象设置条件，传到NBR
//    int returnCommodity_select = 0;//查单中的退货按钮未点击
//    boolean isOnLongClick = false;//是否长按退货商品数量框
//    private List<RetailTrade> checkListRetailTradeList = new ArrayList<>();//用于保存查单时返回的符合条件的零售单，用于分页查询
//    /**
//     * 促销计算后的零售单，用临时变量存起来，因为用户未一定拿这个对象去结算。
//     */
//    private RetailTrade tempCalculatedRetailTrade = null;
//
//    List<Commodity> commodityListAfterQuery = new ArrayList<Commodity>();
//    EditText search_commodity_et;
//    ImageView ivDelete_all;
//    TextView stock;
//    TextView tv_inventory_search;
//    View search_view;
//    CommodityRecyclerViewAdapter commodityRecyclerViewAdapter;
//    DialogCommodityRecyclerViewAdapter dialogCommodityRecyclerViewAdapter;
//    DialogCheckListOrderRecyclerViewAdapter dialogCheckListOrderRecyclerViewAdapter;
//    DialogCheckListCommodityRecyclerViewAdapter dialogCheckListCommodityRecyclerViewAdapter;
//    RecyclerView check_list_order_rv;
//    RecyclerView check_list_commodity_rv;
//    private String recordkeyNumbers = "";//用来存储reserve_et的值，记录元素个数以及内容，从而在输入数字的时候结合此变量刚进行判断
//    //    public int showReserveAndCommodity = 0;//当showReserve==0，准备金的alertdialog显示，为0则相反
//    EditText reserve_et;//准备金的alertdialog的准备金额编辑框
//    TextView phone;
//
//    private List<RetailTradeCommodity> retailTradeCommodityAfterConfirmReturn = new ArrayList<RetailTradeCommodity>();//查单界面查询到的零售单商品保存在该List
//    List<RetailTrade> showRetailTradeList = new ArrayList<>();//用于显示查找出的零售单
//    //用于记录退货时候选中的零售单，因为如果不这么做，点击之后修改了数量，一种情况是点击另一张单，回到原来的零售单，数量是改变之后的值，
//    // 还有一种就是，只是修改显示的值，但是没有办法拿到。所以需用一个retailTrade用于记录选中的单，当选择另一张单的时候就进行初始化
//    RetailTrade retailTradeAfterConfirmReturn;
//    private RetailTrade retailTradeSelected; //用于记录选中的零售单。
//    private RetailTradePromoting retailTradePromoting;
//    private int commNumber = 0;//用于记录退货时选中的零售单商品的原数量
//    private int noCanReturn = 0;//用于记录每个零售单商品的可退货数量
//    private int remainingQuantity;//当退货数量为最大可退货数量的时候，剩余的数量
//    private double dAmountToReturnToCustomer = 0.000000d;
//
//
//    //退出交班时，传递到交班Activity的数据
////    private int tradeNo = 0;//交易单数
////    private String amount;//营业额
////    private String cashAmount;//现金支付
////    private String wechatAmount;//微信收入
////    private String alipayAmount;//支付宝支付
//    private String workTimeEnd;//交班时间
////    private double cashInAmount; //单次交易使用现金支付的金额
////    private double wechatInAmount; //单次交易使用微信支付的金额
////    private double alipayInAmount; //单次交易使用支付宝支付的金额
//
//    private RetailTrade returnRetailTrade; // 临时退货零售单
//    private String wxOutRefundNO;//商户退款单号
//    private String wxOutRefundErrorMsg;//商户退款失败信息
//
//    private double total_money_double = 0.000000d;//总计金额的double数据（用于上传到服务器，和保存到本地SQLite，不需要对小数位进行操作）
////    List<Double> total_money_list = new ArrayList<>();//总计金额的list，用在交班的时候算营业额，当点击结算完成，就会统计总收入有多少
////    List<Double> cash_income_list = new ArrayList<>();//现金收入的list，用在交班的时候算现金总收入，同上
////    List<Double> wechat_income_list = new ArrayList<>();//微信收入的list，用在交班的时候算微信总收入，同上
////    List<Double> alipay_income_list = new ArrayList<>();//支付宝收入的list，用在交班的时候算支付宝总收入，同上
//
//    private ScanGun scanGun = null;
//    /**
//     * 零售单商品数量是否可以编辑(界面上的"+"和"-"是否显示)
//     */
//    private boolean isRetailTradeCommodityNumberEditable;
//
//    //查询Vip部分
//    private Vip vip = null;
//    AlertDialog vip_dialog = null;
//
//    int screenWidth;
//    int screenHeight;
//
//    private int currentIndex = 1;//当前正在下载的零售单的PageIndex
//    /**
//     * 标明现在是否正在退货。
//     * 当收银员点击退货按钮后，set为true。
//     * 当退货结束或超时，set为false。
//     */
//    private boolean bIsReturningCommodity;
//    /**
//     * 等待退货结束的最迟时间。如果超过这个时间，证明超时了
//     */
//    private Date waitingEndDatetime;
//
//    // 售卖商品列表中数量的默认最小值
//    private String Default_CommodityMinNumber = "1";
//    // 售卖商品列表中数量的默认最大值
//    private int Default_CommodityMaxNumber = 9999;
//    //上一次点击搜索按钮时输入的有效长度的单据号
//    private String lastQueryKeyWordBySearchButton = "";
//    //上一次点击搜索按钮时输入的起始时间
//    private Date lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
//    //上一次点击搜索按钮时输入的结束时间
//    private Date lastDatetimeEndBySearchButton = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
//
//    /*
//    * 本地广播(本APP范围内)使用的组件
//    * */
//    private IntentFilter intentFilter;
//    private DayEndGenerateRetailTradeAggregationReceiver dayEndGenerateRetailTradeAggregationReceiver;
//    private LocalBroadcastManager localBroadcastManager;
//
//    private Timer timerPing;
//    private TimerTask timerTaskPing;
//
//    private Date getDefaultSyncDatetime() {
//        try {
//            return Constants.getDefaultSyncDatetime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
//        log.info("#########################################################MainActivity onRetailTradeSQLiteEvent");
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent(); //
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                retrieveNRetailTradeList = (List<RetailTrade>) event.getListMasterTable();
//
//                int count = Integer.parseInt(retailTradeHttpEvent.getCount());
//                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);//查询零售单需要totalPageIndex页才能查完
//
//                closeLoadingDialog(loadingDailog);
//                Message message = new Message();
//                message.what = 3;
//                handler.sendMessage(message);
//            }
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done) {
//                closeLoadingDialog(loadingDailog);
//            }
//
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade) {
//            System.out.println("该event在POSTING中已经处理：" + BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("MainActivity.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，此处MainActivity.onRetailTradeSQLiteEvent1()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onRetailTradeSQLiteEvent1(RetailTradeSQLiteEvent event) throws InterruptedException {
//        System.out.println("#########################################################POSTING");
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                if (!updateRetailTradeAggregation((RetailTrade) event.getBaseModel1())) {
//                    //TODO 以后根据用户反应再作处理
//                }
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            log.info("该event在MAIN中已经处理：" + BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("MainActivity.onRetailTradeSQLiteEvent1()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，此处MainActivity.onRetailTradeSQLiteEvent1()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    /**
//     * 创建退货单后，更新本地收银汇总
//     */
//    private boolean updateRetailTradeAggregation(RetailTrade rt) {
//        System.out.println("创建退货单成功后，更新SQLite的收银汇总");
//        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//        //未发生变化的数据
//        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
//        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//        retailTradeAggregation.setPosID(Constants.posID);
//        retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO());/*销售单数*/
//        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//        //发生变化的数据：
//        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
//        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前退货单的生成时间*/
//        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
//        retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));/*现金收入*/
//        retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));/*微信收入*/
//        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
//        //单数不用更新
//
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
//        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
//            log.error("创建退货单后，准备更新零售单汇总失败！");
//            return false;
//        }
//        long lTimeOut = TIME_OUT;
//        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            log.error("Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
//            return false;
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("Update错误码不正确！" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
//            return false;
//        } else {
//            // 创建退货单后，收银汇总数据需要减去相应的钱。
//            BaseActivity.retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));
//            BaseActivity.retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));
//            BaseActivity.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTradeAggregation.getWechatAmount()));
//            BaseActivity.retailTradeAggregation.setWorkTimeEnd(date);
//        }
//
//        return true;
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                System.out.println("向服务器上传收收银汇总成功");
//            } else {
//                System.out.println("向服务器上传收收银汇总失败");
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_RetailTrade_RetrieveNOldRetailTrade && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                if (event.getListMasterTable() == null || event.getListMasterTable().size() == 0) {
//                    Message message = new Message();
//                    message.what = 4;
//                    handler.sendMessage(message);
//                }
//            }
//
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onBrandHttpEvent(BrandHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPosSQLiteEvent(PosSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPosHttpEvent(PosHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
//                closeLoadingDialog(loadingDailog);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommodityHttpEvent(CommodityHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetrieveNRerailTrade(RetailTradeEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            List<RetailTrade> list = event.getRetailTradeList();
//            Toast.makeText(appApplication, list + "", Toast.LENGTH_SHORT).show();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onVipHttpEvent(VipHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_Vip_RetrieveNByMobileOrVipCardSN && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                if (event.getListMasterTable() == null || event.getListMasterTable().size() != 3) {
//                    vip = new Vip();
//                    vip.setName("不存在");
//                }
//                // 将List中的会员相关信息存放到LocalCache中, 0：vip  1：WxVip  2：wxVipCardDetail
//                LocalCache.vip = (Vip) event.getListMasterTable().get(0);
//                LocalCache.wxVip = (WxVip) event.getListMasterTable().get(1);
//                LocalCache.wxVipCardDetail = (WxVipCardDetail) event.getListMasterTable().get(2);
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onVipSQLiteEvent(VipSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateByServerDataNAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                if (event.getListMasterTable().size() > 0) {
//                    vip = (Vip) event.getListMasterTable().get(0);
//                }
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onWXPayHttpEvent(WXPayHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if ("WXRefund".equals(event.getWxPayStatus())) {
//                wxOutRefundNO = event.getRefundResponse().get("out_refund_no");
//                event.setWxPayStatus("");
//            } else if ("WXRefundFail".equals(event.getWxPayStatus())) {
//                wxOutRefundErrorMsg = event.getLastErrorMessage(); // 微信返回的错误信息
//                event.setWxPayStatus("");
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onReturnCommodityEvent(ReturnCommodityHttpEvent event) {
//        log.info("----------------------------------------------onReturnCommodityEvent1");
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            log.info(event.getStatus());
//            closeLoadingDialog(loadingDailog);
//            String msg = "";
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
//                if (event.getMessageBeforeEventPosted().length() > 0) {
//                    closeLoadingDialog(loadingDailog);
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("警告")
//                            .setMessage(event.getMessageBeforeEventPosted())
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // RecyclerView 刷新，初始化
//                                    dialogCheckListOrderRecyclerViewAdapter = new DialogCheckListOrderRecyclerViewAdapter(null, getApplicationContext());
//                                    check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter);
//                                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                                    //
//                                    clearRetailTradeCommodityList();
//                                    //show 零售单
//                                    showRetailTrade(retrieveNRetailTradeList);
//                                }
//                            })
//                            .create().show();
//                }
//            } else {
//                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
//                    msg = "微信退款失败！" + wxOutRefundErrorMsg;
//                } else if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
//                    msg = "退货单创建失败！";
//                }
//            }
//            Message message = new Message();
//            message.obj = msg;
//            message.what = 7;
//            handler.sendMessage(message);
//
//
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
//            event.onEvent();
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
//                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                    BaseActivity.retailTradeAggregation.setID(event.getBaseModel1().getID());
//                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseActivity.retailTradeAggregation);
//                } else {
//                    log.error("创建临时收银汇总失败！");
//                }
//            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync) {
//                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                    System.out.println("更新收银汇总成功！收银汇总=" + event.getBaseModel1());
//                } else {
//                    log.error("更新收银汇总失败！");
//                }
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        localBroadcastManager = LocalBroadcastManager.getInstance(this);//获取localBroadcastManager实例
//
//        ButterKnife.bind(this);
//        if (BaseActivity.showCommList.size() == 0) {
//            showTextInT1Host("", "欢迎惠顾");
//        } else {
//            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
//                total_money_double = GeneralUtil.sum(total_money_double, BaseActivity.showCommList.get(i).getSubtotal());
//            }
//            showTextInT1Host("合计：", "￥" + GeneralUtil.formatToShow(total_money_double));
//        }
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        screenWidth = dm.widthPixels;//屏幕宽度
//        screenHeight = dm.heightPixels;//屏幕高度
//
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
//        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
//        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
//        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
//        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
//        retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
//
//        //当此EditText获得焦点的时候，软键盘不弹出
//        scan_barcode_text.setShowSoftInputOnFocus(false);
//        scan_barcode_text.setFocusableInTouchMode(false);
//
//        initBOAndEvent();
////        DaggerCommonComponent.builder()
////                .applicationComponent(getApplicationComponent())
////                .activityModule(getActivityModule())
////                .build()
////                .inject(this);
//
////        if (BuildConfig.DEBUG) {
////            addTestCommodity();
////        }
//
//        createReserveDialog();
//        registerDayEndGenerateRetailTradeAggregationBroadcast();
//        scan_barcode_text.setOnClickListener(this);
//        scan_barcode_search.setOnClickListener(this);
//        choose_client.setOnClickListener(this);
//        balance_tv.setOnClickListener(this);
//        remove_commodity.setOnClickListener(this);
//        bill.setOnClickListener(this);
//        list.setOnClickListener(this);
//        check_list.setOnClickListener(this);
//        set_up.setOnClickListener(this);
//        inventory.setOnClickListener(this);
//        coupons.setOnClickListener(this);
//        logout.setOnClickListener(this);
//        client_more_consumption.setOnClickListener(this);
//        printLastRetailTrade.setOnClickListener(this);
//
//        if (BaseActivity.showCommList != null) {
//            showCommodity(BaseActivity.showCommList);
//        }
//        initScanGun();
//
//        ((AppApplication) getApplication()).startSyncThread();
//
//        timerPing = new Timer();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        ActivityController.setCurrentActivity(this);
//        if (intent != null) {
//            setIntent(intent);
//            if (getIntent().getIntExtra("isPaid", 0) == 1) {
//                BaseActivity.showCommList.clear();
//                showCommodity(BaseActivity.showCommList);
//                total_money_double = 0.000000d;
//                commodityQuantity.setText("商品数量：" + "0" + " 件");//初始话商品数量UI
//                retailTradePromoting = new RetailTradePromoting();// 促销过程重置
//                BaseActivity.retailTrade = null;
//                total_money.setText("0.00");
//                // 上一单信息
//                lastRetailTradeAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeAmount));
//                lastRetailTradeChangeMoney.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeChangeMoney));
//                lastRetailTradePaymentType.setText(BaseActivity.lastRetailTradePaymentType);
//            } else {
//                // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
//                System.out.println("没支付就退回来主页面");
//            }
//        }
//    }
//
//    /**
//     * 收银员跨天上班时，在23:59:30上传今天的收银汇总，在第二天的00:00:00后，重新创建一张收银汇总到本地sqlite中
//     */
//    private void registerDayEndGenerateRetailTradeAggregationBroadcast() {
//        Date targetTime = DatetimeUtil.mergeDateAndTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), Constants.START_TIME_UploadRetailTradeAggregation, Constants.DATE_FORMAT_Default);
//        Timer timer = new Timer(true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
//                if (BaseActivity.retailTradeAggregation.getID() != null) {
//                    intentFilter = new IntentFilter();
//                    intentFilter.addAction(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
//                    dayEndGenerateRetailTradeAggregationReceiver = new DayEndGenerateRetailTradeAggregationReceiver();
//                    localBroadcastManager.registerReceiver(dayEndGenerateRetailTradeAggregationReceiver, intentFilter);//注册向本地所有activity发送弹出EndDayAlertDialog消息的本地广播监听器
//
//                    Intent intent = new Intent(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
//                    localBroadcastManager.sendBroadcast(intent);
//
//                    BaseActivity.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                    BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
//                    //更新本地的临时收银汇总
//                    if (updateRetailTradeAggregationAtDayEnd()) {
//                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
//                            log.info("在接近第二天的时候且有网络，上传收银汇总");
//                            uploadRetailTradeAggregation(retailTradeAggregation);
//                        } else {
//                            log.info("无网络进行跨天上班，不上传收银汇总");
//                        }
//                        //
//                        printRetailTradeAggregation();
//                    } else {
//                        log.error("跨天上班时，更新今天本地的收银汇总失败！");
//                    }
//                } else {
//                    //现在本地中创建一张今日空的收银汇总如果有网则上传。
//                    RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//                    retailTradeAggregation.setPosID(Constants.posID);
//                    retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//                    retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//                    retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                    if (createRetailTradeAggregationInSqlite(retailTradeAggregation)) {
//                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
//                            uploadRetailTradeAggregation(retailTradeAggregation);
//                        } else {
//                            log.info("没输入准备金且无网络进行跨天上班,不上传收银汇总");
//                        }
//                    } else {
//                        log.error("用户不输入准备金进行跨天上班,自动在本地生成今日的收银汇总失败！");
//                    }
//                    //
//                    GlobalController.getInstance().setSessionID(null);
//                    log.info("sessionID已清除");
//                    //
//                    ((AppApplication) getApplication()).exitFromSyncThread();
//                    //
//                    initialization();//点击登录后会调用，所以这里不call也行
//                    //
//                    ActivityController.finishAll();
//                    //
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }, targetTime);
//    }
//
//    public static class ResponseDayEndGenerateRetailTradeAggregationReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            log.debug("处理广播事件");
//            //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
//            //这里不判断也可以，因为发广播前判断过了
//            if (BaseActivity.retailTradeAggregation.getID() != null) {
//                if (intent.getAction().equals(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation)) {
//                    log.debug("处理广播事件：" + DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
//                    createRetailTradeAggregationForNextDay();
//                }
//            }
//        }
//    }
//
//    private boolean createRetailTradeAggregationInSqlite(RetailTradeAggregation retailTradeAggregation) {
//        RetailTradeAggregation retailTradeAggregationCreate = (RetailTradeAggregation) retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
//        if (retailTradeAggregationCreate != null && retailTradeAggregationCreate.getID() != null) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean updateRetailTradeAggregationAtDayEnd() {
//        RetailTradeAggregation updateRetailTradeAggregation = null;
//        try {
//            updateRetailTradeAggregation = (RetailTradeAggregation) BaseActivity.retailTradeAggregation.clone();
//            updateRetailTradeAggregation.setAmount(GeneralUtil.sum(updateRetailTradeAggregation.getCashAmount(), updateRetailTradeAggregation.getWechatAmount()));
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//            log.error("进行跨天上班时，更新本地收银汇总前获取数据失败,错误消息=" + e.getMessage());
//            return false;
//        }
//        //
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
//        return retailTradeAggregationSQLiteBO.updateSync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradeAggregation);
//    }
//
//    private void printRetailTradeAggregation() {
//        //打印跨天上班汇总小票
//        try {
//            AidlUtil.getInstance().printText("交班确认表", 35, false, 17, false);
//            AidlUtil.getInstance().printText("注意：此交班小票为跨日期上班系统自动生成，请妥善保管以便对账。", 20, false, 17, false);
//            AidlUtil.getInstance().printDivider("-");
//            AidlUtil.getInstance().printText("上班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
//            AidlUtil.getInstance().printText("下班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeEnd()), 25, false, 51, false);
//            AidlUtil.getInstance().printText("交班人：" + Constants.getCurrentStaff().getName(), 30, false, 51, false);
//            AidlUtil.getInstance().linewrap(1);
//            AidlUtil.getInstance().printText("------收银汇总------", 35, false, 17, false);
//            AidlUtil.getInstance().printText("交易单数：" + BaseActivity.retailTradeAggregation.getTradeNO(), 30, false, 51, false);
//            AidlUtil.getInstance().printText("营业额：" + BaseActivity.retailTradeAggregation.getAmount(), 30, false, 51, false);
//            AidlUtil.getInstance().printText("准备金：" + BaseActivity.retailTradeAggregation.getReserveAmount(), 30, false, 51, false);
//            AidlUtil.getInstance().printText("现金收入：" + BaseActivity.retailTradeAggregation.getCashAmount(), 30, false, 51, false);
//            AidlUtil.getInstance().printText("微信收入：" + BaseActivity.retailTradeAggregation.getWechatAmount(), 30, false, 51, false);
////                            AidlUtil.getInstance().printText("支付宝收入：" + showAlipayAmount.getText().toString(), 30, false, 51, false);
//            AidlUtil.getInstance().printDivider("-");
//            AidlUtil.getInstance().printText("交班人签名：", 30, false, 51, false);
//            AidlUtil.getInstance().linewrap(8);
//            AidlUtil.getInstance().cutPaper();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 当收银员跨天上班，到了第二天后，往sqlite插入一条收银汇总
//     */
//    private static void createRetailTradeAggregationForNextDay() {
//        Date tmr = DatetimeUtil.get2ndDayStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//        if (!DatetimeUtil.isAfterDate(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), tmr, 0)) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        //初始化 BaseActivity.retailTradeAggregation的部分数据
//        if (BaseActivity.retailTradeAggregation != null) {
//            BaseActivity.retailTradeAggregation.setWorkTimeStart(tmr);
//            BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
//            BaseActivity.retailTradeAggregation.setTradeNO(0);
//            BaseActivity.retailTradeAggregation.setAmount(0.000000d);
//            BaseActivity.retailTradeAggregation.setCashAmount(0.000000d);
//            BaseActivity.retailTradeAggregation.setWechatAmount(0.000000d);
//            //
//            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//            retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//            retailTradeAggregation.setPosID(Constants.posID);
//            retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//            retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
//            retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
//            //
//            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//            if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
//                log.error("跨天上班时，创建零售单汇总失败");//TODO 未处理结果事件
//            }
//        }
//    }
//
//    /**
//     * 用户设置ReserveAmount后，创建收银汇总到本地sqlite中
//     */
//    private void createRetailTradeAggregation() {
//        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
//        //准备金被点确定之前，day end 收银汇总 dialog已经弹出来。当dialog消失时，如果不设置work time start为第2天的时间，会导致work time start是前一天的时间。
//        //这不符合我们的业务逻辑且在上传时，因为day end dialog结束时已经创建了一个retailTradeAggregation在SQLite中，现在用户点击确定后，又创建了一个retailTradeAggregation。这种情况无法避免但是暂时可以接受。
//        //我们规定收银汇总必须是当天的，不可以跨天。
//        BaseActivity.retailTradeAggregation.setWorkTimeStart(DatetimeUtil.addSecond(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), 1));
//        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
//        //
//        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
//        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
//        retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
//        //
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
//            log.error("创建零售单汇总失败");
//        }
//    }
//
//    /* 初始化扫码枪 */
//    private void initScanGun() {
//        // 设置key事件最大间隔，默认20ms，部分低端扫码枪效率低
//        ScanGun.setMaxKeysInterval(50);
//        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onScanFinish(String scanResult) {
//                //扫条形码
//                commodityListAfterQuery.clear();
//                scan_barcode_text.setText("");
//                log.info("扫码枪扫描到的数据：" + scanResult);
////                List<Commodity> commodityListAfterQuery = retrieveNCommodityInSQLite(scanResult);
//                if (scanResult == null || scanResult.equals("")) {
//                    log.info("扫描到的条形码为空");
//                    return;
//                }
//                scan_barcode_text.setText(scanResult);
//                if (scanResult.length() > BaseActivity.FUZZY_QUERY_LENGTH) {
//                    commodityListAfterQuery = retrieveNCommodityInSQLite(true, scanResult);
//                } else {
//                    Toast.makeText(MainActivity.this, "条形码数字至少为" + String.valueOf(BaseActivity.FUZZY_QUERY_LENGTH + 1) + "位", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                log.info("根据条形码查出数据：" + commodityListAfterQuery.size() + "扫码枪扫描到的数据：" + scanResult);
//                log.info("showCommList：" + BaseActivity.showCommList);
//                if (commodityListAfterQuery == null || commodityListAfterQuery.size() == 0) {
//                    Toast.makeText(MainActivity.this, "未找到商品", Toast.LENGTH_SHORT).show();
//                } else {
//                    //搜到的商品多于一个，或只有一个但是和scanResult不完全一致
//                    if (commodityListAfterQuery.size() > 1 || !commodityListAfterQuery.get(0).getBarcode().equals(scanResult)) {
//                        AlertDialog.Builder commodity_builder = null;
//                        View commodity_view = null;
//                        AlertDialog commodity_dialog = null;
//                        objectList = createAlertDialog(commodity_builder, commodity_view, commodity_dialog, 900, 500, R.layout.choose_commodity_dialog);
//                        WindowManager.LayoutParams params = ((Dialog) objectList.get(2)).getWindow().getAttributes();
//                        params.width = (int) (screenWidth * 0.5);
//                        params.height = (int) (screenHeight * 0.68);
//                        ((Dialog) objectList.get(2)).getWindow().setAttributes(params);
//
//                        commodity_view = (View) objectList.get(1);
//                        rv_alertdialog_commodity = commodity_view.findViewById(R.id.rv_alertdialog_commodity);
//                        rv_alertdialog_commodity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//                        tv_inventory_search = commodity_view.findViewById(R.id.tv_inventory_search);
////                            search_view = commodity_view.findViewById(R.id.search_view);
//                        tv_inventory_search.setVisibility(View.GONE);
////                            search_view.setVisibility(View.GONE);
//                        stock = commodity_view.findViewById(R.id.stock);
//                        search_commodity_et = commodity_view.findViewById(R.id.search_commodity_et);
//                        search_commodity_et.setText(scanResult);
//                        search_commodity_et.setEnabled(false);
//
//                        dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter(commodityListAfterQuery, getApplicationContext());
//                        rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
//                        //
//                        dialogCommodityRecyclerViewAdapter.setOnItemListener(new DialogCommodityRecyclerViewAdapter.OnItemListener() {
//                            @Override
//                            public void onClick(DialogCommodityRecyclerViewAdapter.MyViewHolder holder, int position) {
//                                dialogCommodityRecyclerViewAdapter.setDefSelect(position);
//                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                                if (commodityListAfterQuery.get(position).isSelect) {
//                                    //选中状态点击之后改为未选中
//                                    commodityListAfterQuery.get(position).isSelect = false;
//                                    stock.setText("0");
//                                    dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                                } else {
//                                    //未选中状态点击之后改为选中
//                                    commodityListAfterQuery.get(position).isSelect = true;
//                                    stock.setText(String.valueOf(commodityListAfterQuery.get(position).getNO()));
//                                    dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//
//                        TextView cancel_tv = commodity_view.findViewById(R.id.cancel_tv);
//                        cancel_tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                log.debug("点击取消按钮");
//                                ((Dialog) objectList.get(2)).dismiss();
//                                commodityListAfterQuery.clear();
//                            }
//                        });
//
//                        TextView add_tv = commodity_view.findViewById(R.id.add_tv);
//                        add_tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                log.debug("增加商品");
//                                Commodity dialogCommodity = new Commodity();
//                                dialogCommodity.setID(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getID());
//                                dialogCommodity.setBarcode(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
//                                //
//                                isCommodityExistInUI(dialogCommodity);
//                                //
//                                showCommodity(BaseActivity.showCommList);
//                                ((Dialog) objectList.get(2)).dismiss();
//                            }
//                        });
//                    } else { //只有一个候选商品，且它的条形码和scanResult完全一致
//                        Commodity commodity = new Commodity();
//                        commodity.setID(commodityListAfterQuery.get(0).getID());
//                        commodity.setBarcode(commodityListAfterQuery.get(0).getBarcode());
//                        //
//                        isCommodityExistInUI(commodity);
//                        //
//                        showCommodity(BaseActivity.showCommList);
////                            ((Dialog) objectList.get(2)).dismiss();
//                    }
//                }
//            }
//        });
//
//        discount_sp.setEnabled(false); // TODO 设置界面中的自定义折扣的控件Spinner不可使用(该功能暂未实现)
//        discount_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                discount_String = (String) discount_sp.getSelectedItem();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        new TimeThread().start();
//        // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
//        retailTradePromoting = new RetailTradePromoting();
//        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//        if (BaseActivity.retailTrade != null) {
//            total_money.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//        } else {
//            total_money.setText("0.00");
//        }
//        lastRetailTradeAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeAmount));
//        lastRetailTradeChangeMoney.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeChangeMoney));
//        lastRetailTradePaymentType.setText(BaseActivity.lastRetailTradePaymentType);
//    }
//
//    private void initBOAndEvent() {
//        if (retailTradeSQLiteEvent == null) {
//            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (retailTradeHttpEvent == null) {
//            retailTradeHttpEvent = new RetailTradeHttpEvent();
//            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (retailTradeSQLiteBO == null) {
//            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        if (retailTradeHttpBO == null) {
//            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
//
//        if (vipHttpEvent == null) {
//            vipHttpEvent = new VipHttpEvent();
//            vipHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (vipSQLiteEvent == null) {
//            vipSQLiteEvent = new VipSQLiteEvent();
//            vipSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (vipHttpBO == null) {
//            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
//        }
//        if (vipSQLiteBO == null) {
//            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
//        }
//        vipHttpEvent.setHttpBO(vipHttpBO);
//        vipHttpEvent.setSqliteBO(vipSQLiteBO);
//        vipSQLiteEvent.setHttpBO(vipHttpBO);
//        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
//
//        if (wxPayHttpEvent == null) {
//            wxPayHttpEvent = new WXPayHttpEvent();
//            wxPayHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (wxPayHttpBO == null) {
//            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
//        }
//        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
//
//        if (commoditySQLiteEvent == null) {
//            commoditySQLiteEvent = new CommoditySQLiteEvent();
//            commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (commodityHttpEvent == null) {
//            commodityHttpEvent = new CommodityHttpEvent();
//            commodityHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (commoditySQLiteBO == null) {
//            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, null);
//        }
//        if (commodityHttpBO == null) {
//            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
//        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
//        commodityHttpEvent.setHttpBO(commodityHttpBO);
//        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
//
//        if (returnCommodityHttpEvent == null) {
//            returnCommodityHttpEvent = new ReturnCommodityHttpEvent();
//            returnCommodityHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//
//        if (retailTradeAggregationSQLiteEvent == null) {
//            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
//            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (retailTradeAggregationHttpEvent == null) {
//            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
//            retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (retailTradeAggregationSQLiteBO == null) {
//            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, null);
//        }
//        if (retailTradeAggregationHttpBO == null) {
//            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        }
//        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        if (staffHttpEvent == null) {
//            staffHttpEvent = new StaffHttpEvent();
//            staffHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (staffHttpBO == null) {
//            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), null, staffHttpEvent);
//        }
//        staffHttpEvent.setHttpBO(staffHttpBO);
//    }
//
//    /**
//     * 创建填写准备金的Dialog
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void createReserveDialog() {
//        AlertDialog.Builder reserve_builder = null;
//        View reserve_view = null;
//        AlertDialog reserve_dialog = null;
//        Display defaultDisplay = getWindowManager().getDefaultDisplay();
//        Point point = new Point();
//        defaultDisplay.getSize(point);
//        int dialogWith = (int) (point.x * 0.6);
//        int dialogHeight = (int) (point.y * 0.7);
//        System.out.println("dialogWith:" + dialogWith + "dialogHeight:" + dialogHeight);
//        objectList = createAlertDialog(reserve_builder, reserve_view, reserve_dialog, dialogWith, dialogHeight, R.layout.alertdialog_reserve);
//        reserve_dialog = (AlertDialog) objectList.get(2);
//        reserve_dialog.setCancelable(false);
//        reserve_view = (View) objectList.get(1);
//        reserve_et = reserve_view.findViewById(R.id.reserve_et);
//        reserve_et.setShowSoftInputOnFocus(false);
//        reserve_view.findViewById(R.id.number_0).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_1).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_2).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_3).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_4).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_5).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_6).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_7).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_8).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_9).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_point).setOnClickListener(this);
//        reserve_view.findViewById(R.id.number_delete).setOnClickListener(this);
//        reserve_view.findViewById(R.id.confirm_reserve).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean DecimalPointOnErrorPosition = reserve_et.getText().toString().indexOf(".") == reserve_et.getText().toString().length() - 2
//                        || reserve_et.getText().toString().indexOf(".") == reserve_et.getText().toString().length() - 1;
//                if (reserve_et.getText().toString().indexOf(".") != -1 && DecimalPointOnErrorPosition) {
//                    reserve_et.setError("请输入整数或者保留小数点后两位的小数");
//                } else if ("".equals(reserve_et.getText().toString()) || null == reserve_et.getText().toString()) {
//                    reserve_et.setError("准备金不允许为空");
//                } else if (Double.valueOf(reserve_et.getText().toString()) > 10000d) {
//                    reserve_et.setError("输入的准备金不能大于10000");
//                } else if (reserve_et.getText().toString().startsWith(".")) {
//                    reserve_et.setError("错误的金额格式，请重新输入正确的准备金额");
//                } else {
//                    BaseActivity.retailTradeAggregation.setReserveAmount(Double.valueOf(reserve_et.getText().toString()));
//                    ((Dialog) objectList.get(2)).dismiss();
//                    createRetailTradeAggregation(); //TODO 将来这里要加一个转圈圈
//                }
//            }
//        });
//    }
//
//    /**
//     * 记录选中的待购商品
//     */
//    final int[] POSITION_CommodityToRemove = {BaseSQLiteBO.INVALID_INT_ID};
//
//    public void showCommodity(final List<Commodity> list) {
//        scan_barcode_text.setText("");
//        scan_barcode_text.setHint("扫描条形码（Ctrl+Q）");
//        //计算商品总数量，并在UI展示
//        int iCommodityQuantity = 0;
//        for (int i = 0; i < list.size(); i++) {
//            iCommodityQuantity += list.get(i).getCommodityQuantity();
//        }
//        commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");
//
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).setNumber(i + 1);
//        }
//        rvCommodity.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
//        rvCommodity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        commodityRecyclerViewAdapter = new CommodityRecyclerViewAdapter(list, getApplicationContext());
//
//        //每次刷新RecyclerView，item之间的间距都会增大，以下两行代码解决此问题
//        RecyclerView.ItemDecoration itemDecoration = rvCommodity.getItemDecorationAt(0);
//        rvCommodity.removeItemDecoration(itemDecoration);
//        rvCommodity.setAdapter(commodityRecyclerViewAdapter);
//        retailTradePromoting = new RetailTradePromoting();
//        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//        if (BaseActivity.retailTrade != null) {
//            total_money.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//        } else {
//            total_money.setText("0.00");
//        }
//
//        commodityRecyclerViewAdapter.setOnItemListener(new CommodityRecyclerViewAdapter.OnItemListener() {
//            @Override
//            public void onClick(CommodityRecyclerViewAdapter.MyViewHolder holder, final int position) {
//                commodityRecyclerViewAdapter.setDefSelect(position);
//                commodityRecyclerViewAdapter.notifyDataSetChanged();
//                if (list.get(position).isSelect) {
//                    //选中状态点击之后改为未选中
//                    list.get(position).isSelect = false;
//                    POSITION_CommodityToRemove[0] = BaseSQLiteBO.INVALID_INT_ID;
//                    commodityRecyclerViewAdapter.notifyDataSetChanged();
//                } else {
//                    //未选中状态点击之后改为选中
//                    list.get(position).isSelect = true;
//                    commodityRecyclerViewAdapter.notifyDataSetChanged();
//                    POSITION_CommodityToRemove[0] = position;
//                    log.info("商品信息：" + list.get(position).toString());
//                }
//            }
//        });
//        remove_commodity.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onClick(View view) {
//                if (POSITION_CommodityToRemove[0] != BaseSQLiteBO.INVALID_INT_ID) {
//                    commodityRecyclerViewAdapter.removeItem(POSITION_CommodityToRemove[0]);
//                    retailTradePromoting = new RetailTradePromoting();
//                    BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//                    if (BaseActivity.retailTrade != null) {
//                        total_money.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//                    } else {
//                        total_money.setText("0.00");
//                    }
//                    final CommodityRecyclerViewAdapter mAdapter = new CommodityRecyclerViewAdapter(list, getApplicationContext());
//                    rvCommodity.setAdapter(mAdapter);
//                    if (list.size() != POSITION_CommodityToRemove[0]) {
//                        list.get(POSITION_CommodityToRemove[0]).isSelect = true;
//                    }
//                    mAdapter.notifyDataSetChanged();
//                    //在remove商品后增加点击事件，因为remove后要改变编号，然后需要重新写一个点击事件，否则点击无效果
//                    mAdapter.setOnItemListener(new CommodityRecyclerViewAdapter.OnItemListener() {
//                        @Override
//                        public void onClick(CommodityRecyclerViewAdapter.MyViewHolder holder, final int position) {
//                            mAdapter.setDefSelect(position);
//                            mAdapter.notifyDataSetChanged();
//                            if (list.get(position).isSelect) {
//                                //选中状态点击之后改为未选中
//                                list.get(position).isSelect = false;
//                                mAdapter.notifyDataSetChanged();
//                            } else {
//                                //未选中状态点击之后给为选中
//                                list.get(position).isSelect = true;
//                                mAdapter.notifyDataSetChanged();
//                                POSITION_CommodityToRemove[0] = position;
////                                Toast.makeText(MainActivity.this, "brand:" + list.get(position).getBrandID(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    mainCommodityRecyclerViewItemLongClick(mAdapter, list);
//
//                    //计算商品总数量，并在UI展示
//                    int iCommodityQuantity = 0;
//                    for (int i = 0; i < list.size(); i++) {
//                        iCommodityQuantity += list.get(i).getCommodityQuantity();
//                    }
//                    commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");
//                    POSITION_CommodityToRemove[0] = -1;
//                } else {
//                    Toast.makeText(MainActivity.this, "未选中商品", Toast.LENGTH_SHORT).show();
//                }
//                //
//                retailTradePromoting = new RetailTradePromoting();
//                BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//                if (BaseActivity.retailTrade != null) {
//                    total_money.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//                } else {
//                    total_money.setText("0.00");
//                }
//                //副屏部分
//                isShowToCustomer(getApplicationContext());
//                //T1副屏
//                showInT1Host();
//                // ...等待完善
//                showCommodity(BaseActivity.showCommList);
//            }
//        });
//
//        mainCommodityRecyclerViewItemLongClick(commodityRecyclerViewAdapter, list);
//    }
//
//    /**
//     * 主页面，商品列表，长按某个商品可以修改其购买数量
//     *
//     * @param list
//     */
//    private void mainCommodityRecyclerViewItemLongClick(final CommodityRecyclerViewAdapter commodityRecyclerViewAdapter, final List<Commodity> list) {
//        commodityRecyclerViewAdapter.setOnItemLongListener(new CommodityRecyclerViewAdapter.OnItemLongListener() {
//            @Override
//            public void onLongClick(CommodityRecyclerViewAdapter.MyViewHolder holder, final int position) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                View view = View.inflate(getApplicationContext(), R.layout.edit_commodity_dialog, null);
//                builder.setView(view);
//                builder.setCancelable(true);
//                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        System.out.println("showCommList:" + showCommList);
//                        showCommList.get(position).isSelect = true;
//                        commodityRecyclerViewAdapter.setDefSelect(position);
//                        commodityRecyclerViewAdapter.notifyDataSetChanged();
//                        POSITION_CommodityToRemove[0] = position;
//                    }
//                });
//                final AlertDialog dialog = builder.create();
//                dialog.show();
//                dialog.getWindow().setLayout(900, 800);
//
//                TextView commodityName = view.findViewById(R.id.commodity_name);
//                TextView unit = view.findViewById(R.id.unit);
//                TextView unit_price = view.findViewById(R.id.original_price);
//                final TextView discount = view.findViewById(R.id.discount);
//                final TextView after_discount = view.findViewById(R.id.after_discount);
//                final EditText number = view.findViewById(R.id.number);
//                TextView add_number = view.findViewById(R.id.add_number);
//                TextView reduce_number = view.findViewById(R.id.reduce_number);
//                final TextView subtotal_money = view.findViewById(R.id.subtotal_money);
//                TextView confirm = view.findViewById(R.id.confirm);
//                TextView cancel = view.findViewById(R.id.cancel);
//                number.setSelection(number.getText().toString().length());
//
//                commodityRecyclerViewAdapter.setDefSelect(position);
//                commodityRecyclerViewAdapter.notifyDataSetChanged();
//                if (list.get(position).isSelect == false) {
//                    //未选中状态点击之后给为选中
//                    list.get(position).isSelect = true;
//                    commodityRecyclerViewAdapter.notifyDataSetChanged();
//                    POSITION_CommodityToRemove[0] = position;
//                }
//                commodityName.setText(list.get(position).getName());
//                unit.setText(list.get(position).getPackageUnit());
//                unit_price.setText(GeneralUtil.formatToShow(list.get(position).getPriceRetail()));
//                if (list.get(position).getPriceRetail() > BaseModel.TOLERANCE) {
//                    discount.setText(GeneralUtil.formatToShow(GeneralUtil.mul(GeneralUtil.div(list.get(position).getAfter_discount(), list.get(position).getPriceRetail(), 2), 100)));
//                } else {
//                    //如果是0元商品则折扣这里直接显示 100.00%
//                    discount.setText(GeneralUtil.formatToShow(100d));
//                }
//                after_discount.setText(GeneralUtil.formatToShow(list.get(position).getAfter_discount()));
//                number.setText(String.valueOf(list.get(position).getCommodityQuantity()));
//
//                Double number_subtotal_money = GeneralUtil.mul(Double.valueOf(after_discount.getText().toString()), Double.valueOf(number.getText().toString()));//折后单价乘以数量
//                String toShow_number_subtotal_money = GeneralUtil.formatToShow(number_subtotal_money);
//                subtotal_money.setText(toShow_number_subtotal_money);
//                add_number.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (number.getText().toString().trim().equals("")) {
//                            Toast.makeText(MainActivity.this, "商品数量必须大于0", Toast.LENGTH_SHORT).show();
//                            number.setText(Default_CommodityMinNumber);
//                        } else if (Integer.valueOf(number.getText().toString()) > Default_CommodityMaxNumber - 1) {
//                            Toast.makeText(MainActivity.this, "商品数量已超过最大允许值", Toast.LENGTH_SHORT).show();
//                            number.setText(String.valueOf(Default_CommodityMaxNumber));
//                        } else {
//                            number.setText(String.valueOf(Integer.valueOf(number.getText().toString()) + 1));
//                        }
//                    }
//                });
//                reduce_number.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if ("1".equals(number.getText().toString())) {
////                            number.setText("1");
////                        } else {
////                            number.setText(String.valueOf(Integer.valueOf(number.getText().toString()) - 1));
////                        }
//                        // 判断当前数值是否为空字符串
//                        if (number.getText().toString().trim().equals("")) {
//                            number.setText(Default_CommodityMinNumber);
//                            Toast.makeText(MainActivity.this, "商品数量必须大于0", Toast.LENGTH_SHORT).show();
//                        }
//                        if (Integer.valueOf(number.getText().toString()) > 1) {
//                            number.setText(String.valueOf(Integer.valueOf(number.getText().toString()) - 1));
//                        }
//                    }
//                });
//                number.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        tempCalculatedRetailTrade = null;
//                        if ("".equals(number.getText().toString()) || Integer.valueOf(number.getText().toString()) <= 0) {
//                            subtotal_money.setText("0.00");
//                            discount.setText("0.00");
//                            after_discount.setText("0.00");
//                        } else if (Integer.valueOf(number.getText().toString()) > Default_CommodityMaxNumber) {
//                            number.setText("9999");
//                            Toast.makeText(MainActivity.this, "商品数量已超过最大允许值", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            List<Commodity> listCommodity = new ArrayList();
//                            try {
//                                for (Commodity commodity : list) {
//                                    listCommodity.add((Commodity) commodity.clone());
//                                }
//                            } catch (CloneNotSupportedException e) {
//                                e.printStackTrace();
//                            }
//                            listCommodity.get(position).setCommodityQuantity(Integer.valueOf(number.getText().toString()));
//                            listCommodity.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
//
//                            retailTradePromoting = new RetailTradePromoting();
////                            BaseActivity.retailTrade = promotionCalculator.sell(listCommodity, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//                            tempCalculatedRetailTrade = promotionCalculator.sell(listCommodity, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//                            subtotal_money.setText(GeneralUtil.formatToShow(Integer.valueOf(number.getText().toString()) * listCommodity.get(position).getAfter_discount()));
//                            if (listCommodity.get(position).getPriceRetail() > BaseModel.TOLERANCE) {
//                                discount.setText(GeneralUtil.formatToShow(GeneralUtil.mul(GeneralUtil.div(listCommodity.get(position).getAfter_discount(), listCommodity.get(position).getPriceRetail(), 2), 100)));
//                            } else {
//                                //如果是0元商品则折扣这里直接显示 100.00%
//                                discount.setText(GeneralUtil.formatToShow(100d));
//                            }
//                            after_discount.setText(GeneralUtil.formatToShow(listCommodity.get(position).getAfter_discount()));
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//                    @Override
//                    public void onClick(View v) {
//                        if (tempCalculatedRetailTrade != null) {
//                            BaseActivity.retailTrade = (RetailTrade) tempCalculatedRetailTrade.clone();
//                            tempCalculatedRetailTrade = null;
//                        }
//                        if ("".equals(number.getText().toString()) || Integer.valueOf(number.getText().toString()) <= 0) { // 用户不能输入1个或多个空格
//                            number.setText("1");
//                        }
//                        if (!"".equals(number.getText().toString()) && Integer.valueOf(number.getText().toString()) > 0) {
//                            //更新了某个字段之后，需要重新导入数据到recyclerview,由于是虚拟数据所以没有更新
//                            list.get(position).setCommodityQuantity(Integer.valueOf(number.getText().toString()));
//                            list.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
//                            retailTradePromoting = new RetailTradePromoting();
//                            BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//                            if (BaseActivity.retailTrade != null) {
//                                total_money.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//                            } else {
//                                total_money.setText("0.00");
//                            }
//                            //副屏部分
//                            isShowToCustomer(getApplicationContext());
//                            //T1副屏
//                            showInT1Host();
//                            // ...等待完
//                            //
//                            commodityRecyclerViewAdapter.notifyDataSetChanged();
//                            //计算商品总数量，并在UI展示
//                            int iCommodityQuantity = 0;
//                            for (int i = 0; i < list.size(); i++) {
//                                iCommodityQuantity += list.get(i).getCommodityQuantity();
//                            }
//                            commodityQuantity.setText("商品数量：" + iCommodityQuantity + "件");
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(MainActivity.this, "需要填写大于0的商品数量", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
//    }
//
//    private void searchCommodityByBarcode() {
//        log.debug("弹出搜索商品窗口");
//        commodityListAfterQuery.clear();
//
//        AlertDialog.Builder commodity_builder = null;
//        View commodity_view = null;
//        AlertDialog commodity_dialog = null;
//        objectList = createAlertDialog(commodity_builder, commodity_view, commodity_dialog, (int) (screenWidth * 0.75), (int) (screenHeight * 0.66), R.layout.choose_commodity_dialog);
//
//        commodity_view = (View) objectList.get(1);
//        rv_alertdialog_commodity = commodity_view.findViewById(R.id.rv_alertdialog_commodity);
//        rv_alertdialog_commodity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));//设置布局管理器，这里选择用竖直的列表
//
//        tv_inventory_search = commodity_view.findViewById(R.id.tv_inventory_search);
////                search_view = commodity_view.findViewById(R.id.search_view);
//        tv_inventory_search.setVisibility(View.GONE);
////                search_view.setVisibility(View.GONE);
//        stock = commodity_view.findViewById(R.id.stock);
//        search_commodity_et = commodity_view.findViewById(R.id.search_commodity_et);
//        search_commodity_et.addTextChangedListener(new TextWatcher() {//只要搜索框不为空，就会把下面展示商品的recyclerview加一列(库存)
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if ("".equals(search_commodity_et.getText().toString())) {
//                    ivDelete_all.setVisibility(View.INVISIBLE);
//                } else if (!"".equals(search_commodity_et.getText().toString())) {
//                    ivDelete_all.setVisibility(View.VISIBLE);
//                }
//                search_commodity_et.setSelection(search_commodity_et.getText().length());
//                //根据输入的条形码在Barcode查找对应的商品的id，根据ID在Commodity查找商品
//                String commodityBarcodes = search_commodity_et.getText().toString();
//                if (commodityBarcodes.length() > BaseActivity.FUZZY_QUERY_LENGTH) {
//                    log.debug("根据条形进行商品搜索，条形码：" + commodityBarcodes);
//                    commodityListAfterQuery = retrieveNCommodityInSQLite(true, commodityBarcodes);
//                    dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter(commodityListAfterQuery, getApplicationContext());
//                    rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
//                    //
//                    dialogCommodityRecyclerViewAdapter.setOnItemListener(new DialogCommodityRecyclerViewAdapter.OnItemListener() {
//                        @Override
//                        public void onClick(DialogCommodityRecyclerViewAdapter.MyViewHolder holder, int position) {
//                            dialogCommodityRecyclerViewAdapter.setDefSelect(position);
//                            dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                            if (commodityListAfterQuery.get(position).isSelect) {
//                                //选中状态点击之后改为未选中
//                                commodityListAfterQuery.get(position).isSelect = false;
//                                stock.setText("0");
//                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                            } else {
//                                //未选中状态点击之后给为选中
//                                commodityListAfterQuery.get(position).isSelect = true;
//                                stock.setText(String.valueOf(commodityListAfterQuery.get(position).getNO()));
//                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        ivDelete_all = commodity_view.findViewById(R.id.delete_all);
//        ivDelete_all.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View view) {
//                // 刷新EditText的焦点，使小键盘能正常弹出
//                search_commodity_et.setFocusable(false);
//                search_commodity_et.setFocusableInTouchMode(false);
//                search_commodity_et.setFocusable(true);
//                search_commodity_et.setFocusableInTouchMode(true);
//                //
//                search_commodity_et.setText("");
//                commodityListAfterQuery.clear();
//                dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter(commodityListAfterQuery, getApplicationContext());
//                rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
//                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
//            }
//        });
//
//        search_commodity_et.setText(scan_barcode_text.getText().toString());
//        TextView cancel_tv1 = commodity_view.findViewById(R.id.cancel_tv);
//        cancel_tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                log.debug("点击取消按钮");
//                ((Dialog) objectList.get(2)).dismiss();
//                commodityListAfterQuery.clear();
//            }
//        });
//
//
//        TextView add_tv1 = commodity_view.findViewById(R.id.add_tv);
//        add_tv1.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onClick(View view) {
//                if (commodityListAfterQuery.size() > 0) {
//                    //如果有选中商品
//                    if (commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).isSelect) {
//                        Commodity dialogCommodity = new Commodity();
//                        dialogCommodity.setID(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getID());
//                        dialogCommodity.setBarcode(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
//                        //
//                        isCommodityExistInUI(dialogCommodity);
//                        scan_barcode_text.setHint("条形码：" + commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
//                        //
//                        showCommodity(BaseActivity.showCommList);
//                        // 恢复原先选中的商品为选中状态。之前如果没有选中的话，下面的代码也不会有事
//                        commodityRecyclerViewAdapter.setDefSelect(POSITION_CommodityToRemove[0]);
//                        commodityRecyclerViewAdapter.notifyDataSetChanged();
//                        //
//                        ((Dialog) objectList.get(2)).dismiss();
//                    } else {
//                        Toast.makeText(MainActivity.this, "请选择商品", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "无可添加到待售列表的商品", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        View chooseCommodityDialogView = commodity_view.findViewById(R.id.choose_commodity_dialog_view);
//        chooseCommodityDialogView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//        });
//    }
//
//
//    /**
//     * User-Agent: Fiddler
//     * Host: 192.168.1.20:8080
//     * Content-Type: application/x-www-form-ulencoded
//     */
//    @TargetApi(Build.VERSION_CODES.N)
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.scan_barcode_text:
//            case R.id.scan_barcode_search:  //点击打开输入条形码的对话框，输入条形码，查找对应的商品
//                searchCommodityByBarcode();
//                break;
//            case R.id.choose_client:
//                chooseClient();
//                break;
//            case R.id.client_more_consumption:
//                if (false) { // TODO 该功能商未完善故暂不跑该段代码
//                    AlertDialog.Builder client_consumption_builder = null;
//                    View client_consumption_view = null;
//                    AlertDialog client_consumption_dialog = null;
//                    objectList = createAlertDialog(client_consumption_builder, client_consumption_view, client_consumption_dialog, 1450, 800, R.layout.check_client_consumption_dialog);
//                }
//                break;
//            case R.id.balance_tv:
//                balance();
//                break;
//            case R.id.bill:
//                bill();
//                break;
//            case R.id.list:
//                new AlertDialog.Builder(this)
//                        .setTitle("提示")
//                        .setMessage("是否确定挂单？")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .create().show();
//                break;
//            case R.id.btnQueryRetailTradeList:
//                queryRetailTradeList();
//                break;
//            case R.id.set_up:
//                Intent set_up_intent = new Intent(MainActivity.this, SetupActivity.class);
//                startActivity(set_up_intent);
//                break;
//            case R.id.inventory:
//                AlertDialog.Builder inventory_builder = null;
//                View inventory_view = null;
//                AlertDialog inventory_dialog = null;
//                objectList = createAlertDialog(inventory_builder, inventory_view, inventory_dialog, 500, 600, R.layout.inventory_dialog);
//
//                inventory_view = (View) objectList.get(1);
//                TextView tvInventory = inventory_view.findViewById(R.id.retrieve_inventory);
//                tvInventory.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent inventory_intent = new Intent(MainActivity.this, RetrieveCommodityInventoryActivity.class);
//                        startActivity(inventory_intent);
//                    }
//                });
//                break;
//            case R.id.coupons:
//                Toast toast = Toast.makeText(MainActivity.this, "该功能尚未实现", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                // TODO: 票卷核销功能暂时未实现，目前上线1.0版本暂时不开放该代码
//                /*AlertDialog.Builder coupons_builder = null;
//                View coupons_view = null;
//                AlertDialog coupons_dialog = null;
//                objectList = createAlertDialog(coupons_builder, coupons_view, coupons_dialog, 500, 800, R.layout.coupon_dialog);
//
//                coupons_view = (View) objectList.get(1);
//                coupons_dialog = (AlertDialog) objectList.get(2);
//                coupons_dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext_rounded_background);
//                TextView coupon_code = coupons_view.findViewById(R.id.coupons_code);
//                TextView benefit_info = coupons_view.findViewById(R.id.benefit_info);
//                TextView use = coupons_view.findViewById(R.id.use);
//                TextView cancel2 = coupons_view.findViewById(R.id.cancel);
//                //通过扫描条码显示的优惠信息
//                String coupon_information = "1.徐昂你弄啥蛋奶酥2.萨达所大所大3.不能代表腹背受敌发吧4.按时打算你绑定币5.不能代表腹背受敌发吧6.按时打算你绑定币7.asdasdasdasasd8.aaosidhoahduew";
//                *//*
//                找到字符串中最大的数
//                 *//*
//                String regEx = "[^0-9]";
//                Pattern p = Pattern.compile(regEx);
//                Matcher m = p.matcher(coupon_information);
//                int biggestIndex = Integer.valueOf((m.replaceAll("").trim()).substring((m.replaceAll("").trim()).length() - 1));//优惠信息中最后一点
//                if (biggestIndex == 1) {
//                    benefit_info.setText(coupon_information);
//                } else {
//                    *//*
//                通过for循环分割字符串
//                 *//*
//                    String temp;
//                    String text;
//                    text = coupon_information.split("2")[0];
//                    temp = coupon_information.split("2")[1];
//                    for (int i = 3; i <= biggestIndex; i++) {
//                        text = text + "\n" + (i - 1) + temp.split(i + "")[0];
//                        temp = temp.split(i + "")[1];
//                    }
//                    text = text + "\n" + biggestIndex + temp;
//                    benefit_info.setText(text);
//                }
//
//                use.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ((Dialog) objectList.get(2)).dismiss();
//                        onRestart();
//                    }
//                });
//                cancel2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ((Dialog) objectList.get(2)).dismiss();
//                        onRestart();
//                    }
//                });*/
//                break;
//            case R.id.logout:
//                new AlertDialog.Builder(this)
//                        .setTitle("提示")
//                        .setMessage("是否退出并交班？")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // 获取当前时间
//                                Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
//                                workTimeEnd = Constants.getSimpleDateFormat().format(date);
//                                Intent change_shifts_intent = new Intent(MainActivity.this, RetailTradeAggregationActivity.class);
//                                change_shifts_intent.putExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd(), workTimeEnd);
//                                startActivity(change_shifts_intent);
//                            }
//                        })
//                        .create().show();
//                break;
//            case R.id.number_0:
//                clickKeyboardNumber("0");
//                break;
//            case R.id.number_1:
//                clickKeyboardNumber("1");
//                break;
//            case R.id.number_2:
//                clickKeyboardNumber("2");
//                break;
//            case R.id.number_3:
//                clickKeyboardNumber("3");
//                break;
//            case R.id.number_4:
//                clickKeyboardNumber("4");
//                break;
//            case R.id.number_5:
//                clickKeyboardNumber("5");
//                break;
//            case R.id.number_6:
//                clickKeyboardNumber("6");
//                break;
//            case R.id.number_7:
//                clickKeyboardNumber("7");
//                break;
//            case R.id.number_8:
//                clickKeyboardNumber("8");
//                break;
//            case R.id.number_9:
//                clickKeyboardNumber("9");
//                break;
//            case R.id.number_point:
//                if (reserve_et.getText().toString().indexOf(".") == -1) {
//                    if ("".equals(reserve_et.getText().toString()) || null == reserve_et.getText().toString()) {
//                        reserve_et.setText("0.");
//                        recordkeyNumbers = "0.";
//                    } else {
//                        clickKeyboardNumber(".");
//                    }
//                }
//                break;
//            case R.id.number_delete:
//                recordkeyNumbers = reserve_et.getText().toString();
//                if ("".equals(reserve_et.getText().toString()) || null == reserve_et.getText().toString()) {
//                    return;
//                } else {
//                    reserve_et.setText(reserve_et.getText().toString().substring(0, reserve_et.getText().length() - 1));
//                    recordkeyNumbers = recordkeyNumbers.substring(0, recordkeyNumbers.length() - 1);
//                }
//                reserve_et.setSelection(reserve_et.getText().length());
//                break;
//            case R.id.telephone_number_1:
//                phone.setText(phone.getText().toString() + "1");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_2:
//                phone.setText(phone.getText().toString() + "2");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_3:
//                phone.setText(phone.getText().toString() + "3");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_4:
//                phone.setText(phone.getText().toString() + "4");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_5:
//                phone.setText(phone.getText().toString() + "5");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_6:
//                phone.setText(phone.getText().toString() + "6");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_7:
//                phone.setText(phone.getText().toString() + "7");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_8:
//                phone.setText(phone.getText().toString() + "8");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_9:
//                phone.setText(phone.getText().toString() + "9");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_0:
//                phone.setText(phone.getText().toString() + "0");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_line:
//                phone.setText(phone.getText().toString() + "-");
//                choose_client.setText(phone.getText().toString());
//                break;
//            case R.id.telephone_number_delete:
//                if ("".equals(phone.getText().toString()) || null == phone.getText().toString()) {
//                    return;
//                } else {
//                    phone.setText(phone.getText().toString().substring(0, phone.getText().length() - 1));
//                    choose_client.setText(phone.getText().toString());
//                }
//                break;
//            case R.id.check_client:
//                String phoneNumber = phone.getText().toString();
//                //wait UI
//                if (!"".equals(phoneNumber)) {
//                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//
//                    //call server vipAction
//                    Vip vip = new Vip();
////                    vip.setCategory(BaseSQLiteBO.INVALID_CASE_ID);
//                    vip.setMobile(phoneNumber);
//                    if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) { // TODO
//                        log.info("根据电话号码查询Vip失败！");
//                    }
//                } else {
//                    Toast.makeText(appApplication, "请输入要查询的电话号码", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.print_last_retailTrade:
//                //打印小票
//                try {
//                    if (lastRetailTrade == null) {
//                        Toast.makeText(appApplication, "暂没有订单", Toast.LENGTH_SHORT).show();
//                    } else {
//                        printSmallSheet(lastRetailTrade);
//                    }
//                } catch (Exception e) {
//                    log.info("打印小票异常：" + e.getMessage());
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 进行微信退款操作。这是阻塞操作
//     */
//    public boolean returnMoney(RetailTrade retailTrade, double wxAmountToReturn) {
//        long lTimeOut = TIME_OUT;
//
//        RetailTrade returnRetailTrade = (RetailTrade) retailTrade.clone();
//        returnRetailTrade.setAmount(WXPayUtil.formatAmount(wxAmountToReturn));
//        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        wxPayHttpBO.refundAsync(returnRetailTrade);
//        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                log.info("mainActivityRefund异常:" + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.error("微信退款超时....");
//            return false;
//        }
//
//        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("微信退款错误码错误：" + wxPayHttpEvent.getLastErrorCode());
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 弹出查单dialog
//     */
//    private void queryRetailTradeList() {
//        AlertDialog.Builder check_list_builder = null;
//        View check_list_view = null;
//        AlertDialog check_list_dialog = null;
//        objectList = createAlertDialog(check_list_builder, check_list_view, check_list_dialog, 1200, 780, R.layout.check_list_dialog);
//        WindowManager.LayoutParams params = ((Dialog) objectList.get(2)).getWindow().getAttributes();
//        params.width = (int) (screenWidth * 0.9);
//        params.height = (int) (screenHeight * 0.9);
//        ((Dialog) objectList.get(2)).getWindow().setAttributes(params);
//        ((Dialog) objectList.get(2)).setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                leaveReturnCommodityMode();
//            }
//        });
//
//        check_list_view = (View) objectList.get(1);
//        check_list_order_rv = check_list_view.findViewById(R.id.check_list_order_rv);
//        check_list_commodity_rv = check_list_view.findViewById(R.id.order_information_rv);
//        final EditText check_list_sn = check_list_view.findViewById(R.id.check_list_sn);
//        //
//        final TextView check_list_startDate = check_list_view.findViewById(R.id.check_list_startDate);
//        check_list_startDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        final TextView check_list_endDate = check_list_view.findViewById(R.id.check_list_endDate);
//        check_list_endDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        checkListOriginalPrice = check_list_view.findViewById(R.id.original_price);
//        checkListReceivable = check_list_view.findViewById(R.id.receivable);
//        checkListNetReceipts = check_list_view.findViewById(R.id.net_receipts);
//        checkListDiscount = check_list_view.findViewById(R.id.discount);
//        checkListPaymentMethod = check_list_view.findViewById(R.id.payment_method);
//        /**
//         * 搜索按钮
//         */
//        tvQueryRetailTrade = check_list_view.findViewById(R.id.check_list_search);
//        confirm_returnCommodity = check_list_view.findViewById(R.id.confirm_return_goods);
//        confirm_returnCommodity.setVisibility(View.GONE);
//        returnAmount = check_list_view.findViewById(R.id.return_amount);
//        returnAmount.setVisibility(View.GONE);
//        returnCommodity = check_list_view.findViewById(R.id.return_goods);
//        reprintSmallSheet = check_list_view.findViewById(R.id.reprint_SmallSheet);
//        check_list_order_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        check_list_commodity_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        TextView returnMoney = check_list_view.findViewById(R.id.return_money);
//        //
//        lastPage = check_list_view.findViewById(R.id.last_page);
//        nextPage = check_list_view.findViewById(R.id.next_page);
//        currentPage = check_list_view.findViewById(R.id.current_page);
//        totalPage = check_list_view.findViewById(R.id.total_page);
//        lastPage.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                //防止用户快速连续点击，查完零售单后会把lastPage设为可点击状态
//                lastPage.setClickable(false);
//                //
//                Toast.makeText(getApplicationContext(), "上一页", Toast.LENGTH_SHORT).show();
//                rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
//                int iCurrentPage = Integer.parseInt(currentPage.getText().toString());
//                if (iCurrentPage <= iTotalPage && iCurrentPage >= 1) {
//                    currentPage.setText(String.valueOf(iCurrentPage - 1)); // 页数 - 1
//                }
//                //获取单据号查询条件
//                //lastQueryKeyWordBySearchButton上一次点击搜索按钮时输入的有效长度的单据号
//                rtRetrieveCondition.setQueryKeyword(lastQueryKeyWordBySearchButton);
//                //获取时间查询条件
//                //上一次点击搜索按钮时输入的起始时间
//                rtRetrieveCondition.setDatetimeStart(lastDatetimeStartBySearchButton);
//                //上一次点击搜索按钮时输入的结束时间
//                rtRetrieveCondition.setDatetimeEnd(lastDatetimeEndBySearchButton);
//                rtRetrieveCondition.setPageIndex(String.valueOf(iCurrentPage - 1)); // 页数 - 1
//                rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
//                //查询零售单
//                queryRetailTrade(rtRetrieveCondition);
//                //
//                clearRetailTradeCommodityList();
//            }
//        });
//
//        nextPage.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                //防止用户快速连续点击，查完零售单后会把nexPage设为可点击状态
//                nextPage.setClickable(false);
//                //
//                Toast.makeText(getApplicationContext(), "下一页", Toast.LENGTH_SHORT).show();
//                rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
//                int iCurrentPage = Integer.parseInt(currentPage.getText().toString());
//                if (iCurrentPage < iTotalPage) {
//                    currentPage.setText(String.valueOf(iCurrentPage + 1)); // 页数 + 1
//                }
//                //获取单据号查询条件
//                //lastQueryKeyWordBySearchButton上一次点击搜索按钮时输入的有效长度的单据号
//                rtRetrieveCondition.setQueryKeyword(lastQueryKeyWordBySearchButton);
//                //获取时间查询条件
//                //上一次点击搜索按钮时输入的起始时间
//                rtRetrieveCondition.setDatetimeStart(lastDatetimeStartBySearchButton);
//                //上一次点击搜索按钮时输入的结束时间
//                rtRetrieveCondition.setDatetimeEnd(lastDatetimeEndBySearchButton);
//                rtRetrieveCondition.setPageIndex(String.valueOf(iCurrentPage + 1)); // 页数 + 1
//                rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
//                //查询零售单
//                queryRetailTrade(rtRetrieveCondition);
//                //
//                clearRetailTradeCommodityList();
//            }
//        });
//        // 刚打开查单页面时，设置上一页，下一页按钮不可用
//        lastPage.setClickable(false);
//        nextPage.setClickable(false);
//
//        for (int i = 0; i < showRetailTradeList.size(); i++) {
//            showRetailTradeList.get(i).setNum(i + 1);
//        }
//
//        //点击退货按钮returnCommodity_select=1，按钮变红，在商品的recyclerview可以修改商品的数量
//        returnCommodity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                log.info("开始退货");
//                if (retailTradeSelected == null) {
//                    Toast.makeText(getApplicationContext(), "请选择要退货的零售单", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 判断该零售单是否超过一年
//                Calendar c = Calendar.getInstance();
//                c.setTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                c.add(Calendar.YEAR, -1);
//                Date date = c.getTime();
//                if (date.getTime() > retailTradeSelected.getSaleDatetime().getTime()) {
//                    Toast.makeText(getApplicationContext(), "该零售单已经超过一年，不能退货！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 判断该零售单创建时间是否超过120s,没有120s则不让退货
//                if (new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime() - retailTradeSelected.getSaleDatetime().getTime() < MIN_Time_InSecond_ReturnRetailTrade * 1000) {
//                    Toast.makeText(getApplicationContext(), "订单结算中，请稍后退货！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 检查是否已经创建退货单
//                RetailTrade retailTrade = new RetailTrade();
//                retailTrade.setSourceID(Integer.valueOf(String.valueOf(retailTradeSelected.getID())));
//                List<RetailTrade> returnRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned, retailTrade);
//                if (returnRetailTradeList != null && returnRetailTradeList.size() > 0) {
//                    Toast.makeText(getApplicationContext(), "该零售单已经退货过了，不能重复退货！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 改变退货按钮的状态
//                setReturnCommodityButtonStyle();
//            }
//        });
//
//        confirm_returnCommodity.setOnClickListener(new View.OnClickListener() { //确认退货，开始处理退货
//            @Override
//            public void onClick(View v) {
//                confirm_returnCommodity.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                    }
//                });
//                if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
//                    confirmReturnCommodity();
//                } else {
//                    // 断网情况下，当且仅当纯现金支付，才可以退货。其它支付方式不可以退货
//                    if (retailTradeSelected.payViaCashOnly()) {
//                        confirmReturnCommodity();
//                    } else {
//                        showRetailTradeCommodity(retailTradeSelected);// 重新加载选中的零售单从表。注意查看列表有无残留其它零售单的商品
//                        returnAmount.setText("应退金额：￥0.00");
//                        returnAmount.setVisibility(View.INVISIBLE);
//                        confirm_returnCommodity.setVisibility(View.INVISIBLE);
//                        Message message = new Message();
//                        message.what = 11;
//                        handler.sendMessage(message);
//                    }
//                }
//            }
//        });
//
//        //点击重打小票按钮(用ID查)
//        reprintSmallSheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (retailTradeSelected != null) {
//                    retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//                    RetailTrade retailTrade = new RetailTrade();
//                    retailTrade.setID(retailTradeSelected.getID());
//                    RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
//                    if (retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade1 == null) {
//                        log.error("retailTradePresenter.retrieve1ObjectSync失败,错误码=" + retailTradePresenter.getLastErrorCode());
//                        Toast.makeText(appApplication, "找不到零售单", Toast.LENGTH_SHORT).show();
//                    }
//
//                    try {
//                        printSmallSheet(retailTrade1);
//                    } catch (Exception e) {
//                        log.info("打印小票异常：" + e.getMessage());
//                    }
//                } else {
//                    Toast.makeText(appApplication, "请选择零售单", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        check_list_startDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (check_list_startDate.getText().toString().equals("")) { //第一次选择日期，获取当前时间
//                    Calendar ca = Calendar.getInstance();
//                    lastSelectedYear_check_list_startDate = ca.get(Calendar.YEAR);
//                    lastSelectedMonth_check_list_startDate = ca.get(Calendar.MONTH);
//                    lastSelectedDate_check_list_startDate = ca.get(Calendar.DAY_OF_MONTH);
//                }
//                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String strMonth = String.valueOf(month + 1);
//                        String strDate = String.valueOf(dayOfMonth);
//                        if (month + 1 < 10) {
//                            strMonth = "0" + strMonth;
//                        }
//                        if (dayOfMonth < 10) {
//                            strDate = "0" + dayOfMonth;
//                        }
//                        lastSelectedYear_check_list_startDate = year;
//                        lastSelectedMonth_check_list_startDate = month;
//                        lastSelectedDate_check_list_startDate = dayOfMonth;
//                        check_list_startDate.setText(year + "/" + strMonth + "/" + strDate);
//                    }
//                }, lastSelectedYear_check_list_startDate, lastSelectedMonth_check_list_startDate, lastSelectedDate_check_list_startDate);
//                DatePicker datePicker = datePickerDialog.getDatePicker();
//                try {
//                    if (!"".equals(check_list_endDate.getText().toString())) {
//                        datePicker.setMaxDate((Constants.getSimpleDateFormat3().parse(check_list_endDate.getText().toString())).getTime());
//                        datePicker.setMinDate(Constants.getDefaultSyncDatetime().getTime());
//                    } else {
//                        datePicker.setMaxDate(Calendar.getInstance().getTime().getTime());
//                        datePicker.setMinDate(Constants.getDefaultSyncDatetime().getTime());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                datePickerDialog.show();
//            }
//        });
//
//        check_list_endDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (check_list_endDate.getText().toString().equals("")) { //第一次选择日期，获取当前时间
//                    Calendar ca = Calendar.getInstance();
//                    lastSelectedYear_check_list_endDate = ca.get(Calendar.YEAR);
//                    lastSelectedMonth_check_list_endDate = ca.get(Calendar.MONTH);
//                    lastSelectedDate_check_list_endDate = ca.get(Calendar.DAY_OF_MONTH);
//                }
//                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String strMonth = String.valueOf(month + 1);
//                        String strDate = String.valueOf(dayOfMonth);
//                        if (month + 1 < 10) {
//                            strMonth = "0" + strMonth;
//                        }
//                        if (dayOfMonth < 10) {
//                            strDate = "0" + dayOfMonth;
//                        }
//                        lastSelectedYear_check_list_endDate = year;
//                        lastSelectedMonth_check_list_endDate = month;
//                        lastSelectedDate_check_list_endDate = dayOfMonth;
//                        check_list_endDate.setText(year + "/" + strMonth + "/" + strDate);
//                    }
//                }, lastSelectedYear_check_list_endDate, lastSelectedMonth_check_list_endDate, lastSelectedDate_check_list_endDate);
//                DatePicker datePicker = datePickerDialog.getDatePicker();
//                try {
//                    if (!"".equals(check_list_startDate.getText().toString())) {
//                        datePicker.setMaxDate(Calendar.getInstance().getTime().getTime());
//                        datePicker.setMinDate((Constants.getSimpleDateFormat3().parse(check_list_startDate.getText().toString())).getTime());
//                    } else {
//                        datePicker.setMaxDate(Calendar.getInstance().getTime().getTime());
//                        datePicker.setMinDate(Constants.getDefaultSyncDatetime().getTime());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                datePickerDialog.show();
//            }
//        });
//
//        tvQueryRetailTrade.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                tvQueryRetailTrade.setClickable(false);
//                // 隐藏键盘
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                rtRetrieveCondition = new RetailTrade();
//                try {
//                    checkListRetailTradeList = new ArrayList<RetailTrade>();
//                    showCheckListRetailTradeList = new ArrayList<List<RetailTrade>>();
//                    Date endDate = null;
//                    if (!"".equals(check_list_endDate.getText().toString())) {
//                        Calendar calendar = Calendar.getInstance();//日历对象
//                        calendar.setTime(Constants.getSimpleDateFormat3().parse(check_list_endDate.getText().toString()));
//                        calendar.add(Calendar.DATE, 1);
////                                endDate = Constants.getSimpleDateFormat3().parse(Constants.getSimpleDateFormat3().format(calendar.getTime()));
//                        endDate = calendar.getTime();
//                    }
//                    if (FieldFormat.checkRetailTradeRetrieveNBySN(check_list_sn.getText().toString())) {
//                        //lastQueryKeyWordBySearchButton记录上一次点击搜索按钮时输入的有效的单据号，点击上/下按钮需要用到
//                        lastQueryKeyWordBySearchButton = check_list_sn.getText().toString();
//                        rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
//                        rtRetrieveCondition.setDatetimeStart("".equals(check_list_startDate.getText().toString()) ? Constants.getDefaultSyncDatetime() : Constants.getSimpleDateFormat3().parse(check_list_startDate.getText().toString()));
//                        rtRetrieveCondition.setDatetimeEnd("".equals(check_list_endDate.getText().toString()) ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : endDate);
//                        lastDatetimeStartBySearchButton = rtRetrieveCondition.getDatetimeStart();
//                        lastDatetimeEndBySearchButton = rtRetrieveCondition.getDatetimeEnd();
//                        rtRetrieveCondition.setPageIndex(String.valueOf(currentIndex));
//                        rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
//                        //查询零售单
//                        currentPage.setText("1"); // 由于是点击搜索按钮，所以初始化页码为1
//                        queryRetailTrade(rtRetrieveCondition);
//                    } else {
//                        Toast.makeText(appApplication, RetailTrade.FIELD_ERROR_sn_ForQuery, Toast.LENGTH_SHORT).show();
//                        tvQueryRetailTrade.setClickable(true);
//                        // 初始化
//                        currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);//查单界面，当前第几页
//                        totalPage.setText("0");//查单界面，共有多少页
//                        iTotalPage = 0;//用于记录查单时，展示的总页数
//                        // RecyclerView 刷新，初始化
//                        dialogCheckListOrderRecyclerViewAdapter = new DialogCheckListOrderRecyclerViewAdapter(null, getApplicationContext());
//                        check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter);
//                        dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                    clearRetailTradeCommodityList();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        checkListDialogView = check_list_view.findViewById(R.id.check_list_dialog_view);
//        checkListDialogView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//        });
//    }
//
//    /**
//     * 控制退货按钮的状态
//     */
//    private void setReturnCommodityButtonStyle() {
//        if (returnCommodity_select == 0) {
//            //当选择的零售单为退货单的时候，不能进入退货模式
//            if (retailTradeAfterConfirmReturn != null) {
//                if (retailTradeAfterConfirmReturn.getSourceID() == -1) { //TODO hardcode
//                    returnCommodity.setBackgroundResource(R.drawable.button_rounded_background2);
//                    returnCommodity.setTextColor(Color.WHITE);
//                    confirm_returnCommodity.setVisibility(View.VISIBLE);
//                    //当点击退货按钮后，弹出未亮起的确认退货按钮，当时商品数量发生改变的时候，确认退货按钮点亮
//                    confirm_returnCommodity.setBackgroundResource(R.drawable.button_rounded_background1);
//                    confirm_returnCommodity.setTextColor(Color.BLACK);
//                    returnAmount.setVisibility(View.VISIBLE);
//                    returnAmount.setText("应退金额：￥0.00");
//                    returnCommodity_select = 1;
//                    isRetailTradeCommodityNumberEditable = true;
//                } else {
//                    Toast.makeText(MainActivity.this, "该零售单为退货单，不允许进行退货操作！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else if (returnCommodity_select == 1) {
//            leaveReturnCommodityMode();
//            if (retailTradeAfterConfirmReturn != null) {
//                // 这里必须是使用clone出的对象，因为用户在点击+-时，是直接增减了商品的数量，而他不一定下一步就是点击确认退货
//                retailTradeAfterConfirmReturn = (RetailTrade) retrieveNRetailTradeList.get(positionOfRetailTradeSelected).clone();
//                showRetailTradeCommodity(retailTradeAfterConfirmReturn);
//            }
//            dAmountToReturnToCustomer = 0.000000d;
//        }
//    }
//
//    /**
//     * 确认退货，开始处理退货
//     */
//    private void confirmReturnCommodity() {
//        try {
//            if (retailTradeSelected.compareTo(retailTradeAfterConfirmReturn) != 0) {
//                if (retailTradeSelected.getListSlave1() == null || retailTradeAfterConfirmReturn.getListSlave1() == null) {
//                    log.error("发现异常零售单，其从表为null，ID=" + retailTradeAfterConfirmReturn.getID());
//                    closeLoadingDialog(loadingDailog);
//                    return;
//                }
//                //
//                retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
//                long tmpRowID = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                List<RetailTradeCommodity> listObjectToRemove = new ArrayList<>();
//                for (int i = 0; i < retailTradeAfterConfirmReturn.getListSlave1().size(); i++) {
//                    RetailTradeCommodity retailTradeCommodityAfterConfirmReturn = (RetailTradeCommodity) retailTradeAfterConfirmReturn.getListSlave1().get(i);
//                    retailTradeCommodityAfterConfirmReturn.setID(tmpRowID + i);
//                    //用原先的零售单商品数量减去现在退货零售单商品的数量得知需要退货多少。如界面中零售商品的数量为20，点击退货后修改零售单商品数量为19，则表示退货1
//                    retailTradeCommodityAfterConfirmReturn.setNO(MainActivity.this.retailTradeCommodityAfterConfirmReturn.get(i).getNO() - retailTradeCommodityAfterConfirmReturn.getNO());
//                    //说明该零售单商品并不需要退货
//                    if (retailTradeCommodityAfterConfirmReturn.getNO() == 0) {
//                        listObjectToRemove.add(retailTradeCommodityAfterConfirmReturn);
//                        continue;
//                    }
//                    if (retailTradeCommodityAfterConfirmReturn.getNO() > MainActivity.this.retailTradeCommodityAfterConfirmReturn.get(i).getNOCanReturn()) {
//                        Message message = new Message();
//                        message.what = 9;
//                        handler.sendMessage(message);
//                        return;
//                    }
//                }
//                for (int i = listObjectToRemove.size() - 1; i >= 0; i--) { // 从末尾开始减
//                    retailTradeAfterConfirmReturn.getListSlave1().remove(listObjectToRemove.get(i));
////                    tradeCommodityList.remove(listIndexToRemove.get(i));
//                }
//                if (retailTradeAfterConfirmReturn.getListSlave1().size() == 0) {
//                    Message message = new Message();
//                    message.what = 8;
//                    handler.sendMessage(message);
//                    return;
//                }
//
//                retailTradeAfterConfirmReturn.setAmount(dAmountToReturnToCustomer);
//                retailTradeAfterConfirmReturn.setSourceID(Integer.valueOf(String.valueOf(retailTradeAfterConfirmReturn.getID())));
//                long returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                retailTradeAfterConfirmReturn.setID(returnCommRetailTradeID);
//                retailTradeAfterConfirmReturn.setLocalSN((int) returnCommRetailTradeID);
//                retailTradeAfterConfirmReturn.setPos_ID(Constants.posID);
//                retailTradeAfterConfirmReturn.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                retailTradeAfterConfirmReturn.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//                retailTradeAfterConfirmReturn.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                retailTradeAfterConfirmReturn.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                // 退货单需要知道零售时的支付宝支付金额和微信支付金额
//                retailTradeAfterConfirmReturn.setSaleAmountAlipay(GeneralUtil.round(retailTradeSelected.getAmountAlipay(), 2));
//                retailTradeAfterConfirmReturn.setSaleAmountWeChat(GeneralUtil.round(retailTradeSelected.getAmountWeChat(), 2));
//                retailTradeAfterConfirmReturn.setSn(retailTradeSelected.getSn() + "_1"); // 退货单sn后面添加_1
//                //
//                TaskScheduler.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        StringBuilder sbMsg = new StringBuilder();
//                        try {
//                            returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//                            if (resolveAmountAndPaymentType(sbMsg, dAmountToReturnToCustomer)) { // 判断哪种支付方式退款
//                                returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//                                if (createReturnRetailTrade(sbMsg)) { // 创建退货单
//                                    returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
//                                }
//                            }
//                        } catch (Exception e) {
//                            log.error("退货要提醒的信息：" + sbMsg);
//                            log.error("退货失败，错误信息：" + e.getMessage());
//                        }
//                        returnCommodityHttpEvent.setMessageBeforeEventPosted(sbMsg.toString());
//                        EventBus.getDefault().post(returnCommodityHttpEvent);
//                    }
//                });
//            } else {
//                Message message = new Message();
//                message.what = 8;
//                handler.sendMessage(message);
//                return;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Message message = new Message();
//            message.what = 10;
//            handler.sendMessage(message);
//        }
//    }
//
//    private boolean createReturnRetailTrade(StringBuilder sbMsg) throws InterruptedException {
//        long lTimeOut = TIME_OUT;
//        RetailTradeSQLiteEvent e = new RetailTradeSQLiteEvent();
//        RetailTradeSQLiteBO bo = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), e, null);
//        e.setSqliteBO(bo);
//        e.setId(BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
//        e.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        e.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
//        e.setMessageBeforeEventPosted(sbMsg.toString());
//        if (!bo.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAfterConfirmReturn)) {
//            log.error("创建退货单失败！");// TODO
//            closeLoadingDialog(loadingDailog);
//            new AlertDialog.Builder(this)
//                    .setTitle("警告")
//                    .setMessage("退货单创建超时！请联系客服。\r\n" + sbMsg.toString())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .create().show();
//            return false;
//        }
//        while (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
//            log.error("钱已经退给顾客，但退货单没有创建成功！必须给SQLite插入这张单：retailTradeAfterConfirmReturn=" + retailTradeAfterConfirmReturn);
//            Toast.makeText(getApplicationContext(), "退货单创建超时！请找客服支持", Toast.LENGTH_SHORT).show(); //TODO
//            closeLoadingDialog(loadingDailog);
//            new AlertDialog.Builder(this)
//                    .setTitle("警告")
//                    .setMessage("退货单创建超时！请找客服支持。\r\n" + sbMsg.toString())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .create().show();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 决出退款金额和退款方式
//     */
//    private boolean resolveAmountAndPaymentType(StringBuilder sbMsg, double totalMoneyToReturnToCustomer) { //TODO 移到Model层
//        // 初始化支付金额
//        retailTradeAfterConfirmReturn.setAmountWeChat(0);
//        retailTradeAfterConfirmReturn.setAmountCash(0);
//        // 初始化支付方式
//        retailTradeAfterConfirmReturn.setPaymentType(0);
//        // 设置总金额为退货金额
//        retailTradeAfterConfirmReturn.setAmount(totalMoneyToReturnToCustomer);
//        // 现金支付大于或等于退货价，现金退款
//        if (GeneralUtil.sub(retailTradeSelected.getAmountCash(), totalMoneyToReturnToCustomer) >= 0) {
//            log.info("纯现金退款" + totalMoneyToReturnToCustomer + "元");
//            retailTradeAfterConfirmReturn.setAmountCash(totalMoneyToReturnToCustomer);
//            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
//            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(totalMoneyToReturnToCustomer) + "元");
//            // 微信支付等于退货价， 微信退款
//        } else if (Math.abs(GeneralUtil.sub(retailTradeSelected.getAmountWeChat(), totalMoneyToReturnToCustomer)) < BaseModel.TOLERANCE) { // 负数的情况
//            log.info("纯微信退款" + totalMoneyToReturnToCustomer + "元");
//            retailTradeAfterConfirmReturn.setAmountWeChat(totalMoneyToReturnToCustomer);
//            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 4); //TODO hardcode
//            if (returnMoney(retailTradeAfterConfirmReturn, retailTradeSelected.getAmountWeChat())) {
//                retailTradeAfterConfirmReturn.setWxRefundNO(wxOutRefundNO);// 更新退货单号
//                sbMsg.append("微信退款成功，金额：" + totalMoneyToReturnToCustomer + "元\r\n");
//            } else {
//                return false;
//            }
//        }
//        // 微信支付和现金支付都小于退货价，先退微信，再退现金
//        else if (retailTradeSelected.getAmountCash() < totalMoneyToReturnToCustomer && retailTradeSelected.getAmountWeChat() < totalMoneyToReturnToCustomer) {//...没有对支付宝支付时进行判断
//            log.info("微信退款" + retailTradeSelected.getAmountWeChat() + "元");
//            retailTradeAfterConfirmReturn.setAmountWeChat(retailTradeSelected.getAmountWeChat());
//            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 4); //TODO hardcode
//            if (returnMoney(retailTradeAfterConfirmReturn, retailTradeSelected.getAmountWeChat())) {
//                retailTradeAfterConfirmReturn.setWxRefundNO(wxOutRefundNO);// 更新退货单号
//                sbMsg.append("微信退款成功，金额：" + retailTradeSelected.getAmountWeChat() + "元\r\n");
//            } else {
//                return false;
//            }
//            //现金退款
//            log.info("现金退款" + GeneralUtil.sub(totalMoneyToReturnToCustomer, retailTradeSelected.getAmountWeChat()) + "元");
//            retailTradeAfterConfirmReturn.setAmountCash(GeneralUtil.sub(totalMoneyToReturnToCustomer, retailTradeSelected.getAmountWeChat()));
//            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
//            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(retailTradeAfterConfirmReturn.getAmountCash()) + "元");
//        }
//        // 现金支付小于退货价，微信支付大于退货价，直接现金退款
//        else if (retailTradeSelected.getAmountCash() < totalMoneyToReturnToCustomer && retailTradeSelected.getAmountWeChat() > totalMoneyToReturnToCustomer) {
//            log.info("纯现金退款" + totalMoneyToReturnToCustomer + "元");
//            retailTradeAfterConfirmReturn.setAmountCash(totalMoneyToReturnToCustomer);
//            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
//            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(totalMoneyToReturnToCustomer) + "元");
//        } else {
//            log.error("发现意外情况！");
//        }
//
//        return true;
//    }
//
//    private void chooseClient() {
//        AlertDialog.Builder vip_builder = null;
//        View vip_view = null;
//        objectList = createAlertDialog(vip_builder, vip_view, vip_dialog, 396, 650, R.layout.choose_client_dialog);
//        vip_dialog = (AlertDialog) objectList.get(2);
//
//        vip_view = (View) objectList.get(1);
//        Button telephone_number_1 = vip_view.findViewById(R.id.telephone_number_1);
//        Button telephone_number_2 = vip_view.findViewById(R.id.telephone_number_2);
//        Button telephone_number_3 = vip_view.findViewById(R.id.telephone_number_3);
//        Button telephone_number_4 = vip_view.findViewById(R.id.telephone_number_4);
//        Button telephone_number_5 = vip_view.findViewById(R.id.telephone_number_5);
//        Button telephone_number_6 = vip_view.findViewById(R.id.telephone_number_6);
//        Button telephone_number_7 = vip_view.findViewById(R.id.telephone_number_7);
//        Button telephone_number_8 = vip_view.findViewById(R.id.telephone_number_8);
//        Button telephone_number_9 = vip_view.findViewById(R.id.telephone_number_9);
//        Button telephone_number_0 = vip_view.findViewById(R.id.telephone_number_0);
//        Button telephone_number_line = vip_view.findViewById(R.id.telephone_number_line);
//        ImageView telephone_number_delete = vip_view.findViewById(R.id.telephone_number_delete);
//        TextView check_client = vip_view.findViewById(R.id.check_client);
//        phone = vip_view.findViewById(R.id.phone);
//
//        telephone_number_1.setOnClickListener(this);
//        telephone_number_2.setOnClickListener(this);
//        telephone_number_3.setOnClickListener(this);
//        telephone_number_4.setOnClickListener(this);
//        telephone_number_5.setOnClickListener(this);
//        telephone_number_6.setOnClickListener(this);
//        telephone_number_7.setOnClickListener(this);
//        telephone_number_8.setOnClickListener(this);
//        telephone_number_9.setOnClickListener(this);
//        telephone_number_0.setOnClickListener(this);
//        telephone_number_line.setOnClickListener(this);
//        telephone_number_delete.setOnClickListener(this);
//        check_client.setOnClickListener(this);
//
//        telephone_number_delete.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                phone.setText("");
//                choose_client.setText("");
//                return false;
//            }
//        });
//    }
//
//    private void bill() {
//        Toast toast = Toast.makeText(MainActivity.this, "该功能尚未实现", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//        // TODO: 取单功能暂时未实现，目前上线1.0版本暂时不开放该代码
//        /*AlertDialog.Builder bill_builder = null;
//        View bill_view = null;
//        AlertDialog bill_dialog = null;
//        objectList = createAlertDialog(bill_builder, bill_view, bill_dialog, 900, 450, R.layout.bill_dialog);
//
//        bill_view = (View) objectList.get(1);
//        RecyclerView rv_bill = bill_view.findViewById(R.id.rv_alertdialog_bill);
//        ImageView the_previous = bill_view.findViewById(R.id.the_previous);
//        ImageView the_next = bill_view.findViewById(R.id.the_next);
//        TextView confirm = bill_view.findViewById(R.id.confirm);
//        TextView cancel = bill_view.findViewById(R.id.cancel);
//        rv_bill.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        List<BillCommodityModel> bill_list = new ArrayList<>();
//        for (int i = 1; i < 9; i++) {
//            BillCommodityModel billCommodityModel = new BillCommodityModel();
//            billCommodityModel.setID(i);
////                    billCommodityModel.setTime("2018-7-21 11:20:"+i);
//            billCommodityModel.setName("商品名称" + i);
//            billCommodityModel.setTotalNumber("1" + i);
//            billCommodityModel.setSubtotal("10" + i);
//            bill_list.add(billCommodityModel);
//        }
//        DialogBillRecyclerViewAdapter dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter(bill_list, getApplicationContext());
//        rv_bill.setAdapter(dialogBillRecyclerViewAdapter);
//        the_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "上一单", Toast.LENGTH_SHORT).show();
//            }
//        });
//        the_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "下一单", Toast.LENGTH_SHORT).show();
//            }
//        });
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Dialog) objectList.get(2)).dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Dialog) objectList.get(2)).dismiss();
//            }
//        });*/
//    }
//
//    private void balance() {
//        if (BaseActivity.showCommList != null && BaseActivity.showCommList.size() > 0) {
//            //将RetailTrade的信息传递到支付界面
//            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//            RetailTrade retailTradeForPaymentActivity = BaseActivity.retailTrade;
//            retailTradeForPaymentActivity.setID(maxRetailTradeIDInSQLite);
//            retailTradeForPaymentActivity.setVipID(vip != null ? vip.getID() : 0);
//            retailTradeForPaymentActivity.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//            retailTradeForPaymentActivity.setSmallSheetID(1);//...
//            retailTradeForPaymentActivity.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
//            retailTradeForPaymentActivity.setLogo("");
//            retailTradeForPaymentActivity.setSourceID(-1);
//            retailTradeForPaymentActivity.setPaymentAccount("");
//            retailTradeForPaymentActivity.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//            retailTradeForPaymentActivity.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//            retailTradeForPaymentActivity.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//            retailTradeForPaymentActivity.setPos_ID(Constants.posID);
//            retailTradeForPaymentActivity.setSn(retailTradeForPaymentActivity.generateRetailTradeSN(Constants.posID));
//            retailTradeForPaymentActivity.setLocalSN((int) retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
//            retailTradeForPaymentActivity.setRemark("......");
//            //
//            long maxRetailtradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//            List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeForPaymentActivity.getListSlave1();
//            List<RetailTradeCommodity> newRetailTradeCommodityList = new ArrayList<>();
//            if (retailTradeCommodityList != null) {
//                for (int i = 0; i < retailTradeCommodityList.size(); i++) {
//                    RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
//                    //
//                    for (Commodity commodity : BaseActivity.showCommList) {
//                        if (commodity.getID() == retailTradeCommodityList.get(i).getCommodityID()) {
//                            retailTradeCommodity.setID(maxRetailtradeCommodityIDInSQLite + i);
//                            retailTradeCommodity.setTradeID(retailTradeForPaymentActivity.getID());
//                            retailTradeCommodity.setCommodityID(commodity.getID().intValue());
//                            retailTradeCommodity.setCommodityName(commodity.getName());
//                            retailTradeCommodity.setNO(commodity.getCommodityQuantity());
//                            retailTradeCommodity.setPriceOriginal(commodity.getPriceRetail());
//                            retailTradeCommodity.setTradeID(retailTradeForPaymentActivity.getID());
//                            retailTradeCommodity.setNOCanReturn(retailTradeCommodity.getNO());
//                            retailTradeCommodity.setPriceReturn(retailTradeCommodityList.get(i).getPriceReturn());
////                        retailTradeCommodity.setDiscount(commodity.getDiscount());
//                            //
//                            Barcodes barcodes = new Barcodes();
//                            barcodes.setSql("where F_CommodityID = ?");
//                            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
//                            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//                            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
//                            retailTradeCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());//...暂未考虑一品多码的问题
//                            //
//                            newRetailTradeCommodityList.add(retailTradeCommodity);
//                            break;
//                        }
//                    }
//                }
//            }
//            retailTradeForPaymentActivity.setListSlave1(newRetailTradeCommodityList);
//
//            if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
//                if (retailTradePromoting.getListSlave1().size() > 0) {
//                    //为RetailTradePromoting设置ID、tradeID和RetailTradePromotingFlow设置ID
//                    long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                    long tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                    //
//                    retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
//                    retailTradePromoting.setTradeID(retailTradeForPaymentActivity.getID().intValue());
//                    for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
//                        ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tmpRetailTradePromotingFlowIDInSQLite + i);
//                    }
//                }
//            }
//            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
//            intent.putExtra("RetailTradeData", retailTradeForPaymentActivity);
//            intent.putExtra("RetailTradePromoting", retailTradePromoting);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "商品列表为空", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /*
//    查单UI里选中商品可以修改商品数量
//     */
//    public void doIncreaseInUI(List<RetailTradeCommodity> commodityList, int position, View showCountView, View total_money) {
//        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
//        int currentNumber = Integer.valueOf(((EditText) showCountView).getText().toString());
//        currentNumber++;
//        ((EditText) showCountView).setText(String.valueOf(currentNumber));
//        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
//        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
//        ((TextView) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
//        retailTradeCommodity.setNO(currentNumber);
//        /*((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).setNO(currentNumber);*/
//        //计算应退金额
//        /*double newAmount = 0.000000d;*/
//        /*for (int i = 0; i < commodityList.size(); i++) {
//            newAmount = GeneralUtil.sum(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.mul(commodityList.get(i).getNO(), commodityList.get(i).getPriceReturn()))), newAmount);
//        }
//        double dAmountToReturnToCustomer = GeneralUtil.sub(retailTradeAfterConfirmReturn.getAmount(), newAmount);*/
//        dAmountToReturnToCustomer = GeneralUtil.sub(dAmountToReturnToCustomer, unitPrice);
//
//        returnAmount.setText("应退金额：￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
//        //当商品数量发生变化时，确认退货按钮亮起
//        confirm_returnCommodity.setBackgroundResource(R.drawable.button_rounded_background2);
//        confirm_returnCommodity.setTextColor(Color.WHITE);
//        //当商品数量恢复到原来的数量时，确认退货按钮熄灭
//        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
//            confirm_returnCommodity.setBackgroundResource(R.drawable.button_rounded_background1);
//            confirm_returnCommodity.setTextColor(Color.BLACK);
//        }
//    }
//
//    public void doDecreaseInUI(List<RetailTradeCommodity> commodityList, int position, View showCountView, View total_money) {
//        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
//        int currentNumber = Integer.valueOf(((EditText) showCountView).getText().toString());
//        if (currentNumber == 0) {
//            return;
//        }
//        currentNumber--;
//        ((EditText) showCountView).setText(String.valueOf(currentNumber));
//        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
//        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
//        ((TextView) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
//        retailTradeCommodity.setNO(currentNumber);
//        /*((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).setNO(currentNumber);*/
//        //计算应退金额
//        /*double newAmount = 0.000000d;
//        for (int i = 0; i < commodityList.size(); i++) {
//            newAmount = GeneralUtil.sum(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.mul(Double.valueOf(commodityList.get(i).getNO()), commodityList.get(i).getPriceReturn()))), newAmount);
//        }
//        double dAmountToReturnToCustomer = GeneralUtil.sub(retailTradeAfterConfirmReturn.getAmount(), newAmount);*/
//        //在纯现金支付的情况下，零售单的商品全部退货，允许应退金额与零售单金额存在0.01的误差，如果该误差存在的话，令应退金额等于零售单金额，以免在导致退货判断异常
//        if (isReturnWholeRetailTrade(commodityList)
//                || Math.abs(GeneralUtil.sub(dAmountToReturnToCustomer, retailTradeAfterConfirmReturn.getAmount()) - TORELANCE_ReturnWholeRetailTradeAllPaidByCash) < BaseModel.TOLERANCE) {
//            dAmountToReturnToCustomer = retailTradeAfterConfirmReturn.getAmount();
//        } else {
//            dAmountToReturnToCustomer = GeneralUtil.sum(dAmountToReturnToCustomer, unitPrice);
//        }
//        returnAmount.setText("应退金额：￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
//        //当商品数量发生变化时，确认退货按钮亮起
//        confirm_returnCommodity.setBackgroundResource(R.drawable.button_rounded_background2);
//        confirm_returnCommodity.setTextColor(Color.WHITE);
//        //当商品数量恢复到原来的数量时，确认退货按钮熄灭
//        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
//            confirm_returnCommodity.setBackgroundResource(R.drawable.button_rounded_background1);
//            confirm_returnCommodity.setTextColor(Color.BLACK);
//        }
//    }
//
//
//    //当零售单的商品数量全为0时，该零售单为全部商品退货：
//    private boolean isReturnWholeRetailTrade(List<RetailTradeCommodity> commodityList) {
//        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
//        if (commodityList != null) {
//            for (int i = 0; i < commodityList.size(); i++) {
//                if (commodityList.get(i).getNO() != 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    //通过线程每0.1秒发送一次消息
//    public class TimeThread extends Thread {
//        public void run() {
//            super.run();
//            do {
//                try {
//                    Thread.sleep(100);
//                    Message msg = new Message();
//                    msg.what = 1;
//                    handler.sendMessage(msg);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } while (true);
//        }
//
//    }
//
//    private Handler handler = new Handler(new Handler.Callback() {
//        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    long now = System.currentTimeMillis();
//                    now = now + 100 + NtpHttpBO.TimeDifference;//当前的时间，加上sleep的0.1秒，再加上服务器与Pos机的时间差
//                    if (show_time != null) {
//                        show_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEEEEEEE").format(new Date(now)));
//                    }
//                    break;
//                case 2://查询了Vip，服务器返回的Vip在UI展示
//                    closeLoadingDialog(loadingDailog);
//                    vip_dialog.dismiss();
//                    if (LocalCache.vip != null) {
//                        show_client_name.setText(LocalCache.vip.getName());
//                    }
//                    break;
//                case 3://点击搜索按钮查询零售单，服务器返回的数据需要在UI进行展示
//                    log.info("查询到的零售单有：" + retrieveNRetailTradeList);
//                    //计算返回的零售单需要分几页进行查看
//                    if (retrieveNRetailTradeList.size() > 0) {
//                        totalPage.setText(String.valueOf(iTotalPage)); //...
//                        showRetailTrade(retrieveNRetailTradeList);
//                        showPagingButton();
//                    }
//                    break;
//                case 4:
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(appApplication, "没有符合条件的零售单", Toast.LENGTH_SHORT).show();
//                    dialogCheckListOrderRecyclerViewAdapter = new DialogCheckListOrderRecyclerViewAdapter(null, getApplicationContext());
//                    check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter);
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//
//                    isRetailTradeCommodityNumberEditable = false;
//                    currentPage.setText("0");
//                    totalPage.setText("0");
//                    tvQueryRetailTrade.setClickable(true);
//                    break;
//                case 6:
//                    if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                        Toast.makeText(appApplication, "退货失败，请重新操作！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(appApplication, "退货成功，接下来可以进行退款操作", Toast.LENGTH_SHORT).show();
//                    }
//                    confirm_returnCommodity.setVisibility(View.GONE);
//                    returnAmount.setVisibility(View.GONE);
//
//                    returnCommodity.setBackgroundResource(R.drawable.button_rounded_background1);
//                    returnCommodity.setTextColor(Color.BLACK);
//                    returnRetailTrade = null;
//                    break;
//                case 7:
//                    String message = (String) msg.obj;
//                    if (message.length() > 0) {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                    // 改变退货按钮的状态
//                    setReturnCommodityButtonStyle();
//                    break;
//                case 8:
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(getApplicationContext(), "还未选择退货商品和退货数量", Toast.LENGTH_SHORT).show();
//                    // 改变退货按钮的状态
//                    setReturnCommodityButtonStyle();
//                    break;
//                case 9:
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(getApplicationContext(), "本次的退货数量大于该商品的可退货数量", Toast.LENGTH_SHORT).show();
//                    // 改变退货按钮的状态
//                    setReturnCommodityButtonStyle();
//                    break;
//                case 10:
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(getApplicationContext(), "退货单创建失败！", Toast.LENGTH_SHORT).show();
//                    // 改变退货按钮的状态
//                    setReturnCommodityButtonStyle();
//                    break;
//                case 11:
//                    closeLoadingDialog(loadingDailog); // 网络故障，不能进行微信退款
//                    Toast.makeText(getApplicationContext(), "网络故障，请稍后再试！", Toast.LENGTH_SHORT).show();
//                    // 改变退货按钮的状态
//                    setReturnCommodityButtonStyle();
//                    break;
//                case 12:
//                    LongClickData longClickDataIncrease = (LongClickData) msg.obj;
//                    if (Integer.valueOf(((EditText) longClickDataIncrease.bShowCountView).getText().toString()) < commNumber) {
//                        doIncreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataIncrease.bPosition, longClickDataIncrease.bShowCountView, longClickDataIncrease.bTotal_money);
//                    } else if (Integer.valueOf(((EditText) longClickDataIncrease.bShowCountView).getText().toString()) > commNumber) {
//                        ((EditText) longClickDataIncrease.bShowCountView).setText(String.valueOf(commNumber));
//                        Toast.makeText(getApplicationContext(), "输入的数量不能大于原商品数量", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case 13:
//                    LongClickData longClickDataDecrease = (LongClickData) msg.obj;
//                    if (Integer.valueOf(((EditText) longClickDataDecrease.bShowCountView).getText().toString()) > remainingQuantity) {
//                        doDecreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataDecrease.bPosition, longClickDataDecrease.bShowCountView, longClickDataDecrease.bTotal_money);
//                    }
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    });
//
//    private void uploadRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
//        /*上传当前的零售汇总到服务器*/
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation);
//        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        //
//        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
//            log.info("上传当前的零售汇总到服务器失败");
//        }
//        long lTimeOut = TIME_OUT;
//        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) { //
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) { //
//            log.error("上传收银汇总单失败！原因：超时！");
//            //...
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("上传收银汇总单失败！错误码为" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
//            //...
//        }
//    }
//
//    /**
//     * 控制查单页面分页按钮的状态
//     */
//    private void showPagingButton() {
//        Integer iCurrentPage = Integer.parseInt(currentPage.getText().toString());
//        nextPage.setClickable(true);
//        lastPage.setClickable(true);
//        tvQueryRetailTrade.setClickable(true);
//        //
//        if (iCurrentPage == 1) { // 第一页
//            lastPage.setClickable(false);
//        }
//        if (iCurrentPage == iTotalPage) {// 最后一页
//            nextPage.setClickable(false);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public List<RetailTrade> queryRetailTrade(RetailTrade retailTrade) {
//        log.info("查询零售单的条件：" + retailTrade);
//        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(this.getApplicationContext())) { // 联网查询
//            try {
//                String msg = retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
//                if ("".equals(msg)) {
//                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
//                    if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
//                        log.info("查询旧零售单失败！！");
//                        closeLoadingDialog(loadingDailog);
//                        Toast.makeText(appApplication, "网络故障，请重新登录", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(appApplication, msg, Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                log.info("seekRetailTrade异常" + e.getMessage());
//                e.printStackTrace();
//            }
//
//            return (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
//        } else if (bNetworkDisconnectedForTheFirstTime && GlobalController.getInstance().getSessionID() != null) { //如果是中途断网情况下点击上一页或下一页，提示错误信息
//            Toast.makeText(this, "网络故障，请稍后再试！", Toast.LENGTH_SHORT).show();
//            //
//            GlobalController.getInstance().setSessionID(null);
//            bNetworkDisconnectedForTheFirstTime = false;
//            // 页面初始化
//            totalPage.setText("0");
//            currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);
//            // 清空零售单列表
//            showRetailTrade(null);
//            //
//            nextPage.setClickable(false);
//            lastPage.setClickable(false);
//            return null;
//        } else {// 断网情况下，从SQLite中查询
//            // 先得到零售单总条数，进行分页
//            RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//            // 当断网时点击搜索，显示的是第一页的数据
//            System.out.println("进行本地查单操作：" + retailTrade);
//            String[] conditions = null;
//            String[] conditionsForTotalPage = null;
//            retailTrade.setSn(retailTrade.getQueryKeyword());
//            try {
//                String sql = "where F_SaleDatetime BETWEEN ? and ? ";
//                String sqlForTotalPage = "where F_SaleDatetime BETWEEN ? and ? ";
//                if (StringUtils.isEmpty(retailTrade.getSn())) { //true 为空,sn为空，查全部    //优化：1.if中有没有分支可以去掉  2.翻页isPageTurning 还原数据问题
//                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd())};
//                } else {
//                    sqlForTotalPage += " and F_SN like ?";
//                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd()), "%" + retailTrade.getSn() + "%"};
//                }
//                retailTrade.setSql(sqlForTotalPage);
//                retailTrade.setConditions(conditionsForTotalPage);
//                List<RetailTrade> totalPageByConditions = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
//                Integer count = totalPageByConditions.size();
//                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);
//                totalPage.setText(String.valueOf(iTotalPage));
//                if (Integer.parseInt(currentPage.getText().toString()) > iTotalPage) {
//                    currentPage.setText(String.valueOf(iTotalPage));
//                }
//                if (StringUtils.isEmpty(retailTrade.getSn())) { //true 为空,sn为空，查全部    //优化：1.if中有没有分支可以去掉  2.翻页isPageTurning 还原数据问题
//                    conditions = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd()), //
//                            String.valueOf((Integer.parseInt(currentPage.getText().toString()) - 1) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default)),//
//                            String.valueOf(Integer.parseInt(currentPage.getText().toString()) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default) - 1)};
//                } else {
//                    sql += " and F_SN like ?";
//                    conditions = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd()), "%" + retailTrade.getSn() + "%", //
//                            String.valueOf((Integer.parseInt(currentPage.getText().toString()) - 1) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default)), //
//                            String.valueOf(Integer.parseInt(currentPage.getText().toString()) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default) - 1)};
//                }
//                sql += "order by F_SaleDatetime desc LIMIT ?, ? ";//根据零售单/退货单生成的时间 进行降序排序
//                retailTrade.setSql(sql);
//                retailTrade.setConditions(conditions);
//
//                // 查询本地的零售单
//                List<RetailTrade> showRetailTrades = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
//
//                // 查询零售单从表
//                for (int i = 0; i < showRetailTrades.size(); i++) {
//                    RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
//                    conditions = new String[]{String.valueOf(showRetailTrades.get(i).getID())};
//                    String sqlCommodity = "where F_TradeID = ?";
//                    retailTradeCommodity.setSql(sqlCommodity);
//                    retailTradeCommodity.setConditions(conditions);
//
//                    List<RetailTradeCommodity> showRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity);
//                    //将从表设置进主表
//                    showRetailTrades.get(i).setListSlave1(showRetailTradeCommodityList);
//                }
//                // 显示
//                totalPage.setText(String.valueOf(iTotalPage));
//                currentPage.setText(currentPage.getText().toString()); // 展现当前页
//                showRetailTrade(showRetailTrades); //将每次查询到的主从表进行展示
//                showPagingButton();
//                // 断网情况下也需要将该list赋值给该list
//                retrieveNRetailTradeList = showRetailTrades;
//                return showRetailTrades; //...
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }
//
//    private void showRetailTrade(final List<RetailTrade> list) {
//        dialogCheckListOrderRecyclerViewAdapter = new DialogCheckListOrderRecyclerViewAdapter(list, getApplicationContext());
//        check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter);
//        dialogCheckListOrderRecyclerViewAdapter.setOnItemListener(new DialogCheckListOrderRecyclerViewAdapter.OnItemListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onClick(DialogCheckListOrderRecyclerViewAdapter.MyViewHolder holder, int position) {
//                dialogCheckListOrderRecyclerViewAdapter.setDefSelect(position);
//                dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                dAmountToReturnToCustomer = 0.000000d;/*初始化应退金额*/
//                //如果操作者点击其他的零售单 退货按钮恢复初始状态
//                if (returnCommodity_select == 1) {
//                    leaveReturnCommodityMode();
//                }
//                if (list.get(position).isSelect) {
//                    retailTradeAfterConfirmReturn = new RetailTrade();
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                    //选中状态点击之后改为未选中
//                    retailTradeSelected = null;
//                    list.get(position).isSelect = false;
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                    //选中状态点击之后改为未选中 退货按钮恢复初始状态
//                    clearRetailTradeCommodityList();
//                } else {
//                    if (!returnCommodity.isClickable()) {
//                        returnCommodity.setClickable(true);
//                    }
//                    // 查单后选中某零售单，显示对应商品信息
//                    positionOfRetailTradeSelected = position;
//                    retailTradeAfterConfirmReturn = (RetailTrade) list.get(position).clone();
//                    retailTradeSelected = (RetailTrade) list.get(position).clone(); //记录当前选中的零售单
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                    //未选中状态点击之后改为选中
//                    list.get(position).isSelect = true;
////                    selectedRetailTrade = checkListRetailTradeList.get(position); // 选中的零售单，用于退货退款
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//
//                    List<RetailTradeCommodity> selectRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
//                    if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
//                        log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
//                        returnCommodity.setClickable(false);
//                        Toast.makeText(MainActivity.this, "该单包含已删除商品，无法显示", Toast.LENGTH_LONG).show();
//                        check_list_commodity_rv.setAdapter(null);
//                        return; // TODO
//                    }
//                    double dTotalOriginalPrice = 0.000000d; //原价总和
//                    double dShouldPay = 0.000000d;//应收款
//                    double dActuallyPay = 0.000000d;//实收款。现在这个变量没用
//                    for (RetailTradeCommodity rtc : selectRetailTradeCommodityList) {
//                        if (rtc.getPriceOriginal() <= BaseModel.TOLERANCE || rtc.getPriceReturn() <= BaseModel.TOLERANCE) { // 若某个商品价格为零则忽略该商品的计算
//                            continue;
//                        }
//                        dTotalOriginalPrice = GeneralUtil.sum(dTotalOriginalPrice, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
//                        dShouldPay = GeneralUtil.sum(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dShouldPay);
//                        dActuallyPay = dShouldPay;
//                    }//应收和实收 待以后再商议放入什么数据 现在目前先放入零售单金额 ----need to design
//                    if (!(dTotalOriginalPrice == 0.000000d || dShouldPay == 0.000000d)) { // 判断单个商品是否为零,是否需要折扣，总计等计算。
//                        checkListOriginalPrice.setText(GeneralUtil.formatToShow(dTotalOriginalPrice));
//                        checkListReceivable.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmount()));//现在目前先放入零售单金额
//                        checkListNetReceipts.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmount()));//现在目前先放入零售单金额
//                        checkListDiscount.setText(GeneralUtil.div(GeneralUtil.mul(dShouldPay, 100.000000d), dTotalOriginalPrice, 2) + "%");
//                    } else {
//                        checkListOriginalPrice.setText("0.00");
//                        checkListReceivable.setText("0.00");
//                        checkListNetReceipts.setText("0.00");
//                        checkListDiscount.setText("0.00%");
//                    }
//                    returnAmount.setText("应退金额：￥0.00");
//
//                    //设置原价总金额，支付方式
//                    if (retailTradeAfterConfirmReturn.getAmountCash() != 0) {
//                        checkListPaymentMethod.setText("现金");
//                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
//                            checkListPaymentMethod.setText("现金、支付宝");
//                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
//                                checkListPaymentMethod.setText("现金、支付宝、微信");
//                            }
//                        } else {
//                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
//                                checkListPaymentMethod.setText("现金、微信");
//                            }
//                        }
//                    } else {
//                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
//                            checkListPaymentMethod.setText("支付宝");
//                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
//                                checkListPaymentMethod.setText("支付宝、微信");
//                            }
//                        } else {
//                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
//                                checkListPaymentMethod.setText("微信");
//                            }
//                        }
//                    }
//                    //展示零售单商品
//                    showRetailTradeCommodity(retailTradeAfterConfirmReturn);
//                    dialogCheckListOrderRecyclerViewAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }
//
//    private void showRetailTradeCommodity(RetailTrade retailTrade) {
//        retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
//        final List<RetailTradeCommodity> retailTradeCommodityListSelected = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
//        log.info("零售单商品列表：" + retailTradeCommodityAfterConfirmReturn);
//        //
//        if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
//            log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
//            returnCommodity.setClickable(false);
//            Toast.makeText(MainActivity.this, "该单包含已删除商品，无法显示", Toast.LENGTH_LONG).show();
//            check_list_commodity_rv.setAdapter(null);
//            return; // TODO
//        }
//        //
//        for (int i = 0; i < retailTradeCommodityAfterConfirmReturn.size(); i++) {
//            retailTradeCommodityAfterConfirmReturn.get(i).setNum(i + 1);
//            Commodity commodity = new Commodity();
//            commodity.setID(Long.valueOf(retailTradeCommodityAfterConfirmReturn.get(i).getCommodityID()));
//            commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
//            if (commodity != null) {
//                retailTradeCommodityAfterConfirmReturn.get(i).setName(commodity.getName());
//            } else {
//                Toast.makeText(appApplication, "需要先进行商品的同步", Toast.LENGTH_SHORT).show();
//            }
//        }
//        dialogCheckListCommodityRecyclerViewAdapter = new DialogCheckListCommodityRecyclerViewAdapter(retailTradeCommodityAfterConfirmReturn, getApplicationContext());
//        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter);
//        dialogCheckListCommodityRecyclerViewAdapter.setOnItemListener(new DialogCheckListCommodityRecyclerViewAdapter.OnItemListener() {
//            @Override
//            public void onClick(DialogCheckListCommodityRecyclerViewAdapter.MyViewHolder holder, final int position) {
//                if (returnCommodity_select == 1) {
//                    dialogCheckListCommodityRecyclerViewAdapter.setDefItem(position);
//                    log.info(position);
//                    dialogCheckListCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                    if (retailTradeCommodityAfterConfirmReturn.get(position).isSelect) {
//                        //选中状态点击之后改为未选中
//                        retailTradeCommodityAfterConfirmReturn.get(position).isSelect = false;
//                        dialogCheckListCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                    } else {
//                        //未选中状态点击之后改为选中
//                        retailTradeCommodityAfterConfirmReturn.get(position).isSelect = true;
//
//                        commNumber = retailTradeCommodityListSelected.get(position).getNO();//零售单商品数量
//                        noCanReturn = retailTradeCommodityListSelected.get(position).getNOCanReturn();//商品最大可退货数量
//                        remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量
//
//                        //是否展示编辑数量的按钮
//                        if (isRetailTradeCommodityNumberEditable) {
//                            retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = true;
//                        } else {
//                            retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = false;
//                        }
//                        dialogCheckListCommodityRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
//
//        dialogCheckListCommodityRecyclerViewAdapter.setModifyCountInterface(new DialogCheckListCommodityRecyclerViewAdapter.ModifyCountInterface() {
//            @Override
//            public void doIncrease(int position, View showCountView, View total_money, MotionEvent event) {
//                final LongClickData longClickData = new LongClickData();
//                longClickData.bPosition = position;
//                longClickData.bShowCountView = showCountView;
//                longClickData.bTotal_money = total_money;
//                Thread plusThread = new Thread(new Thread() {
//                    @Override
//                    public void run() {
//                        while (isOnLongClick) {
//                            try {
//                                Thread.sleep(200);
//                                Message message = new Message();
//                                message.obj = longClickData;
//                                message.what = 12;
//                                handler.sendMessage(message);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        super.run();
//                    }
//                });
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    isOnLongClick = true;
//                    plusThread.start();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (plusThread != null) {
//                        isOnLongClick = false;
//                    }
//                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    if (plusThread != null) {
//                        isOnLongClick = false;
//                    }
//                }
//            }
//
//            @Override
//            public void doDecrease(int position, View showCountView, View total_money, MotionEvent event) {
//                final LongClickData longClickData = new LongClickData();
//                longClickData.bPosition = position;
//                longClickData.bShowCountView = showCountView;
//                longClickData.bTotal_money = total_money;
//                Thread miusThread = new Thread(new Thread() {
//                    @Override
//                    public void run() {
//                        while (isOnLongClick) {
//                            try {
//                                Thread.sleep(200);
//                                Message message = new Message();
//                                message.obj = longClickData;
//                                message.what = 13;
//                                handler.sendMessage(message);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        super.run();
//                    }
//                });
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    isOnLongClick = true;
//                    miusThread.start();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (miusThread != null) {
//                        isOnLongClick = false;
//                    }
//                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    if (miusThread != null) {
//                        isOnLongClick = false;
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 创建对话框
//     *
//     * @param builder
//     * @param view
//     * @param dialog
//     * @param width
//     * @param height
//     * @param layout
//     */
//    private List<Object> createAlertDialog(AlertDialog.Builder builder, View view, Dialog dialog, int width, int height, int layout) {
//        List<Object> objectList = new ArrayList<>();
//        builder = new AlertDialog.Builder(MainActivity.this);
//        view = View.inflate(getApplicationContext(), layout, null);
//        builder.setView(view);
//        builder.setCancelable(true);
//        dialog = builder.create();
//        dialog.show();
//        dialog.getWindow().setLayout(width, height);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext_rounded_background);
//        objectList.add(builder);
//        objectList.add(view);
//        objectList.add(dialog);
//
//        return objectList;
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        log.info("---------------------------------onKeyDown:    KeyCode:" + keyCode + "------event:" + event + "------------------------------------");
//        scanGun.isMaybeScanning(keyCode, event);
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        //拦截物理键盘事件
//        log.info("---------------------------------dispatchKeyEvent:  KeyCode:" + event.getKeyCode() + "---Action:" + event.getAction() + "------------------------------------");
//
//        if (event.getKeyCode() > 6) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {//将键盘的删除键传递下去
//                    return super.dispatchKeyEvent(event);
//                }
//                if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {//将非数字键盘的数字键传递下去
//                    return super.dispatchKeyEvent(event);
//                }
//                this.onKeyDown(event.getKeyCode(), event);
//            }
//        } else {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//将键盘的返回事件传递下去
//                return super.dispatchKeyEvent(event);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 定时ping nbr以保持会话不过期。服务器的过期时间是30分钟。App每隔25分钟ping一次
//     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        timerTaskPing = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    staffHttpBO.pingEx();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.debug(e.getMessage());
//                }
//            }
//        };
//        timerPing.schedule(timerTaskPing, 0, 25 * 60 * 1000);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (timerPing != null) {
//            timerTaskPing.cancel();
//            timerPing.purge();
//        }
//    }
//
//    /**
//     * 根据条形码查找商品
//     *
//     * @param barcode 要搜索的条形码
//     * @return
//     */
//    private List<Commodity> retrieveNCommodityInSQLite(boolean isUseLike, String barcode) {
//        List<Commodity> commodityList = new ArrayList<Commodity>();
//        //在Barcodes中找到对应的commodityID，再根据commodityID找到对应的commodity
//        Barcodes barcodes = new Barcodes();
//        if (isUseLike) {
//            barcodes.setSql("where F_Barcode like ?");
//            barcodes.setConditions(new String[]{"%" + barcode + "%"});
//            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
//        } else {
//            barcodes.setSql("where F_Barcode = ?");
//            barcodes.setConditions(new String[]{barcode});
//            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
//        }
//        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
//
//        if (barcodesList != null) {
//            for (int i = 0; i < barcodesList.size(); i++) {
//                Commodity commodity = new Commodity();
//                commodity.setID(Long.valueOf(barcodesList.get(i).getCommodityID()));
//                commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
//                if (commodity != null) {
//                    commodity.setNumber(i + 1);
//                    commodity.setBarcode(barcodesList.get(i).getBarcode());
//
//                    PackageUnit packageUnit = new PackageUnit();
//                    packageUnit.setID(Long.valueOf(commodity.getPackageUnitID()));
//                    packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
//                    commodity.setPackageUnit(packageUnit.getName());
//
//                    commodityList.add(commodity);
//                }
//            }
//        }
//        log.info("commoditylist： " + commodityList);
//        return commodityList;
//    }
//
//    /**
//     * 判断该Commodity是否已经在待购列表中存在，若存在则只增加商品的购买数量和修改商品总价，否则在待购列表增加该商品
//     *
//     * @param c
//     */
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void isCommodityExistInUI(Commodity c) {
//        boolean isAddToCommList = true;//于判断是否在待购列表增加商品用
//        if (BaseActivity.showCommList.size() > 0) {//判断是否有重复ID，若有，则增加该商品数量和改变总价，否则在待购列表增加商品总价
//            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
//                if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() < Default_CommodityMaxNumber) {//已经存在该商品，只增加数量和改变商品总价
//                    BaseActivity.showCommList.get(i).setCommodityQuantity(Integer.valueOf(BaseActivity.showCommList.get(i).getCommodityQuantity()) + 1);
//                    BaseActivity.showCommList.get(i).setSubtotal(BaseActivity.showCommList.get(i).getCommodityQuantity() * BaseActivity.showCommList.get(i).getAfter_discount());
//                    isAddToCommList = false;
//                    break;
//                } else if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() >= Default_CommodityMaxNumber) {
//                    Toast.makeText(appApplication, "一次零售的一种商品的数量已达到最大", Toast.LENGTH_LONG).show();
//                    isAddToCommList = false;
//                    break;
//                } else {
//                    isAddToCommList = true;
//                }
//            }
//        }
//        if (isAddToCommList) {
//            //根据商品ID找对对应的商品信息
//            String barcode = c.getBarcode();
//            c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
//            //根据商品ID找到对应的barcodes
////            Barcodes barcodes = new Barcodes();
////            barcodes.setSql("where F_CommodityID = ?");
////            barcodes.setConditions(new String[]{String.valueOf(c.getID())});
////            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
//            //根据商品ID找到对应的单位
//            PackageUnit packageUnit = new PackageUnit();
//            packageUnit.setID(Long.valueOf(c.getPackageUnitID()));
//            packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
//
//            c.setBarcode(barcode);
//            c.setCommodityQuantity(1);
//            c.setPackageUnit(packageUnit.getName());
//            c.setDiscount(0.000000d);// 折扣
//            c.setAfter_discount(c.getPriceRetail());
//            c.setSubtotal(c.getAfter_discount() * c.getCommodityQuantity());
//            System.out.println("要展示在页面上的comm" + c);
//            BaseActivity.showCommList.add(c);
//        }
//        // ...等待完善
//        retailTradePromoting = new RetailTradePromoting();
//        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null), retailTradePromoting);
//        //...
//        //副屏部分
//        isShowToCustomer(getApplicationContext());
//        //T1副屏
//        showInT1Host();
//    }
//
//    @Override
//    protected void onPause() {
//        HttpRequestManager.getCache(EnumDomainType.EDT_Communication).pushHttpRequest(null);
////        hrs.stop();
//        super.onPause();
//    }
//
//    /*
//     * 将时间转换为时间戳
//     */
//    public String dateToStamp(String time) throws ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = simpleDateFormat.parse(time);
//        long ts = date.getTime();
//        return String.valueOf(ts);
//    }
//
//    private void showInT1Host() {
//        if (BaseActivity.showCommList.size() == 0) {
//            showTextInT1Host("", "欢迎惠顾");
//        } else {
//            double totalMoney = 0;
//            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
//                totalMoney += Double.valueOf(BaseActivity.showCommList.get(i).getSubtotal()); // TODO
//            }
//            showTextInT1Host("合计：", "￥" + GeneralUtil.formatToShow(totalMoney));
//        }
//    }
//
//    /**
//     * TODO 待重命名
//     */
//    private void clearRetailTradeCommodityList() {
//        // 清空查单页面右边的、某一零售单的商品信息
//        dialogCheckListCommodityRecyclerViewAdapter = new DialogCheckListCommodityRecyclerViewAdapter(null, getApplicationContext());
//        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter);
//        dialogCheckListCommodityRecyclerViewAdapter.notifyDataSetChanged();
//        // 重置零售单应收实收等数据
//        checkListOriginalPrice.setText("0.00");
//        checkListReceivable.setText("0.00");
//        checkListNetReceipts.setText("0.00");
//        checkListDiscount.setText("0.00%");
//        checkListPaymentMethod.setText("");
//        // 重置退货按钮
//        leaveReturnCommodityMode();
//        // 重置：之前想退但是没退的零售单单,让点退货按钮时弹出“请选择要退货的零售单”
//        retailTradeSelected = null;
//    }
//
//    /**
//     * 退出退货模式
//     */
//    private void leaveReturnCommodityMode() {
//        returnCommodity.setBackgroundResource(R.drawable.button_rounded_background1);
//        returnCommodity.setTextColor(Color.BLACK);
//        confirm_returnCommodity.setVisibility(View.GONE);
//        returnAmount.setVisibility(View.GONE);
//        returnCommodity_select = 0;
//        isRetailTradeCommodityNumberEditable = false;
//    }
//
//    /**
//     * 在输入准备金的时候点击数字，显示准备金金额
//     *
//     * @param number 点击的数字
//     */
//    private void clickKeyboardNumber(String number) {
//        //限制：第一位为0的时候，后面只能输入小数点
//        if (!"0".equals(recordkeyNumbers)) {
//            //限制只能输两位小数
//            if (recordkeyNumbers.indexOf(".") == -1
//                    || (recordkeyNumbers.indexOf(".") > -1
//                    && (recordkeyNumbers.length() - recordkeyNumbers.indexOf(".")) < 3)) {
//                reserve_et.setText("");
//                recordkeyNumbers += number;
//                reserve_et.setText(recordkeyNumbers);
//                reserve_et.setSelection(reserve_et.getText().length());
//            }
//        } else if (".".equals(number)) {
//            if (recordkeyNumbers.indexOf(".") == -1
//                    || (recordkeyNumbers.indexOf(".") > -1
//                    && (recordkeyNumbers.length() - recordkeyNumbers.indexOf(".")) < 3)) {    //没有满足这个条件.查看为什么需要这个条件
//                reserve_et.setText("");
//                recordkeyNumbers += number;
//                reserve_et.setText(recordkeyNumbers);
//                reserve_et.setSelection(reserve_et.getText().length());
//            }
//
//        }
//    }
//
//    /**
//     * 长按退货界面商品的+-按钮时，传递给message handler的数据
//     */
//    public class LongClickData {
//        public int bPosition;
//        public View bShowCountView;
//        public View bTotal_money;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (dayEndGenerateRetailTradeAggregationReceiver != null) {
//            dayEndGenerateRetailTradeAggregationReceiver.unregisterReceiver();
//        }
//        localBroadcastManager.unregisterReceiver(dayEndGenerateRetailTradeAggregationReceiver);
//    }
//}