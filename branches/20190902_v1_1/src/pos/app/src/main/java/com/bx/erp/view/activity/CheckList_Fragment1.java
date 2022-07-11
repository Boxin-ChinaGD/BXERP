package com.bx.erp.view.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.ReturnCommodityHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.utils.WXPayUtil;
import com.bx.erp.view.adapter.DialogCheckListCommodityRecyclerViewAdapter1;
import com.bx.erp.view.adapter.DialogCheckListOrderRecyclerViewAdapter1;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CheckList_Fragment1 extends BaseFragment1 implements View.OnTouchListener {
    private static Logger log = Logger.getLogger(CheckList_Fragment1.class);
    Unbinder unbinder;
    @BindView(R.id.Return_goods_page)
    LinearLayout ReturnGoodsPage;
    @BindView(R.id.bottom_linear)
    LinearLayout bottomLinear;
    @BindView(R.id.total_page)
    TextView totalPage;
    @BindView(R.id.original_price)
    TextView checkListOriginalPrice;
    @BindView(R.id.receivable)
    TextView checkListReceivable;
    @BindView(R.id.net_receipts)
    TextView checkListNetReceipts;
    @BindView(R.id.discount)
    TextView checkListDiscount;
    @BindView(R.id.payment_method)
    TextView checkListPaymentMethod;
    @BindView(R.id.check_list_sn)
    EditText check_list_sn;
    @BindView(R.id.last_page)
    ImageView lastPage;
    @BindView(R.id.next_page)
    ImageView nextPage;
    @BindView(R.id.current_page)
    TextView currentPage;
    @BindView(R.id.check_list_order_rv)
    RecyclerView check_list_order_rv;
    @BindView(R.id.order_information_rv)
    RecyclerView check_list_commodity_rv;
    @BindView(R.id.return_amount)
    TextView returnAmount;
    @BindView(R.id.check_list_startDate)
    TextView check_list_startDate;
    @BindView(R.id.check_list_endDate)
    TextView check_list_endDate;
    @BindView(R.id.check_list_search)
    Button tvQueryRetailTrade;
    @BindView(R.id.reprint_SmallSheet)
    TextView reprintSmallSheet;
    @BindView(R.id.confirm_return_goods)
    TextView confirm_returnCommodity;
    @BindView(R.id.tv_returngoods_number)
    TextView tv_returngoodsNumber;


    /**
     * ------------------------------------------------------分割线--------------------------------------------------------------------------------
     */

    /**
     * 最短的退货时间。必须在这个时间经过之后，才能对零售单进行退货。以秒为单位
     */
    private static final int MIN_Time_InSecond_ReturnRetailTrade = 120;
    @BindView(R.id.delete_all)
    ImageView deleteAll;
    private RetailTrade rtRetrieveCondition;//查询零售单，在该对象设置条件，传到NBR
    private RetailTrade returnRetailTrade; // 临时退货零售单
    private int ReturnGoods_Number = 0;//记录退货数量
    private CommodityPresenter commodityPresenter;
    int iTotalPage;//用于记录查单时，展示的总页数
    //上一次点击搜索按钮时输入的有效长度的单据号
    private String lastQueryKeyWordBySearchButton = "";
    //上一次点击搜索按钮时输入的起始时间
    private Date lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
    //上一次点击搜索按钮时输入的结束时间
    private Date lastDatetimeEndBySearchButton;
    private List<RetailTradeCommodity> retailTradeCommodityAfterConfirmReturn;//查单界面查询到的零售单商品保存在该List
    List<RetailTrade> retrieveNRetailTradeList;
    private List<RetailTrade> checkListRetailTradeList;//用于保存查单时返回的符合条件的零售单，用于分页查询
    List<List<RetailTrade>> showCheckListRetailTradeList;//将查询到的所有零售单list进行分成数个size为10的list，用于在分页的时候直接调用
    /**
     * 标识是否首次检测到网络发生了故障。如果是首次，则要重新初始化零售单列表。否则，直接从SQLite中搜索零售单显示到零售单列表中
     */
    boolean bNetworkDisconnectedForTheFirstTime;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    private RetailTradePresenter retailTradePresenter;
    private LinearLayoutManager manager1;
    private LinearLayoutManager manager2;
    private DialogCheckListOrderRecyclerViewAdapter1 dialogCheckListOrderRecyclerViewAdapter1;
    private DialogCheckListCommodityRecyclerViewAdapter1 dialogCheckListCommodityRecyclerViewAdapter1;

    //用于记录退货时候选中的零售单，因为如果不这么做，点击之后修改了数量，一种情况是点击另一张单，回到原来的零售单，数量是改变之后的值，
    // 还有一种就是，只是修改显示的值，但是没有办法拿到。所以需用一个retailTrade用于记录选中的单，当选择另一张单的时候就进行初始化
    RetailTrade retailTradeAfterConfirmReturn;


    /**
     * 记录选择的零售单的位置。
     */
    private int positionOfRetailTradeSelected = -1;
    int returnCommodity_select = 1;//查单中的退货按钮未点击
    private double dAmountToReturnToCustomer = 0.000000d;
    private int commNumber = 0;//用于记录退货时选中的零售单商品的原数量
    private int noCanReturn = 0;//用于记录每个零售单商品的可退货数量
    private int remainingQuantity;//当退货数量为最大可退货数量的时候，剩余的数量
    boolean isOnLongClick = false;//是否长按退货商品数量框
    private int currentIndex = 1;//当前正在下载的零售单的PageIndex
    private int lastSelectedYear_check_list_startDate;//
    private int lastSelectedMonth_check_list_startDate;
    private int lastSelectedDate_check_list_startDate;
    private int lastSelectedYear_check_list_endDate;
    private int lastSelectedMonth_check_list_endDate;
    private int lastSelectedDate_check_list_endDate;
    private String wxOutRefundErrorMsg;//商户退款失败信息
    private String wxOutRefundNO;//商户退款单号
    /**
     * 零售单商品数量是否可以编辑(界面上的"+"和"-"是否显示)
     */
    private boolean isRetailTradeCommodityNumberEditable;
    private RetailTrade retailTradeSelected; //用于记录选中的零售单。

    private static NetworkUtils networkUtils = new NetworkUtils();
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static ReturnCommodityHttpEvent returnCommodityHttpEvent = null;
    private static WXPayHttpBO wxPayHttpBO = null;
    private static WXPayHttpEvent wxPayHttpEvent = null;
    /**
     * 判断当前点击的是否为搜索按钮。
     * 查单有3种情况：
     * 1、进入查单界面，自动加载零售单。
     * 2、分页。
     * 3、点击查单按钮。
     * 分页时，如果遇到网络故障，则从本地查找零售单而非从服务器上查找，这时，需要提示网络故障，并引导收银员点击查单按钮，不能再点击分页按钮。
     * 收银员可以重复点击查单按钮，但在断网的情况下，不可以重复点击分页按钮，因为查单从服务器转移到了本地。
     */
    private boolean isSearchButtonClicked = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.check_list1, container, false);
        unbinder = ButterKnife.bind(this, view);

        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        lastDatetimeEndBySearchButton = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeCommodityAfterConfirmReturn = new ArrayList<RetailTradeCommodity>();//查单界面查询到的零售单商品保存在该List
        retrieveNRetailTradeList = new ArrayList<>();
        checkListRetailTradeList = new ArrayList<>();
        showCheckListRetailTradeList = new ArrayList<>();//将查询到的所有零售单list进行分成数个size为10的list，用于在分页的时候直接调
        bNetworkDisconnectedForTheFirstTime = true;
        lastQueryKeyWordBySearchButton = "";
        // 刚打开查单页面时，设置上一页，下一页按钮不可用
        lastPage.setClickable(false);
        nextPage.setClickable(false);

        manager1 = new LinearLayoutManager(getActivity());//提供给RecyclerView使用的
        manager2 = new LinearLayoutManager(getActivity());//提供给RecyclerView使用的
        manager1.setOrientation(LinearLayoutManager.VERTICAL);//设置LinearManger
        manager2.setOrientation(LinearLayoutManager.VERTICAL);//设置LinearManger
        check_list_order_rv.setLayoutManager(manager1);//绑定manger
        check_list_commodity_rv.setLayoutManager(manager2);//绑定manger
        initBOAndEvent();

        check_list_sn.setOnTouchListener(this);
        check_list_sn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (check_list_sn.getText().toString().equals("")) {
                    deleteAll.setVisibility(View.GONE);
                } else {
                    deleteAll.setVisibility(View.VISIBLE);
                }
            }
        });

        isSearchButtonClicked = false;
        tvQueryRetailTrade.performClick();

        return view;
    }

    @Override
    public void onStop() {  //重置一些数据为空或刚进来时的数据，因为fragment被destroy后，数据不一定被回收
        retailTradeSelected = null;
        positionOfRetailTradeSelected = -1;
        dAmountToReturnToCustomer = 0.000000d;
        commNumber = 0;
        noCanReturn = 0;
        isOnLongClick = false;
        currentIndex = 1;
        retailTradeAfterConfirmReturn = null;
        manager1 = null;
        manager2 = null;
        lastQueryKeyWordBySearchButton = "";
        ReturnGoods_Number = 0;
        lastQueryKeyWordBySearchButton = "";
        lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.check_list_search, R.id.check_list_page, R.id.last_page, R.id.next_page, R.id.check_list_startDate, R.id.check_list_endDate,
            R.id.reprint_SmallSheet, R.id.confirm_return_goods, R.id.delete_all, R.id.check_list_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_list_clear: // 清空查询条件按钮
                check_list_sn.setText("");
                check_list_startDate.setText("");
                check_list_endDate.setText("");
                break;
            case R.id.check_list_search://搜索按钮
                isSearchButtonClicked = true;//标志点击了搜索按钮
                tvQueryRetailTrade.setClickable(false);
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                rtRetrieveCondition = new RetailTrade();
                try {
                    checkListRetailTradeList = new ArrayList<RetailTrade>();
                    showCheckListRetailTradeList = new ArrayList<List<RetailTrade>>();
                    Date endDate = null;
                    if (!"".equals(check_list_endDate.getText().toString())) {
                        Calendar calendar = Calendar.getInstance();//日历对象
                        calendar.setTime(Constants.getSimpleDateFormat3().parse(check_list_endDate.getText().toString()));
                        calendar.add(Calendar.DATE, 1);
//                                endDate = Constants.getSimpleDateFormat3().parse(Constants.getSimpleDateFormat3().format(calendar.getTime()));
                        endDate = calendar.getTime();
                    }
                    if (FieldFormat.checkRetailTradeRetrieveNBySN(check_list_sn.getText().toString())) {
                        /**
                         *  单据号符合标准
                         */
                        //lastQueryKeyWordBySearchButton记录上一次点击搜索按钮时输入的有效的单据号，点击上/下按钮需要用到
                        lastQueryKeyWordBySearchButton = check_list_sn.getText().toString();
                        rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
                        rtRetrieveCondition.setDatetimeStart("".equals(check_list_startDate.getText().toString()) ? Constants.getDefaultSyncDatetime() : Constants.getSimpleDateFormat3().parse(check_list_startDate.getText().toString()));
                        rtRetrieveCondition.setDatetimeEnd("".equals(check_list_endDate.getText().toString()) ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : endDate);
                        lastDatetimeStartBySearchButton = rtRetrieveCondition.getDatetimeStart();
                        lastDatetimeEndBySearchButton = rtRetrieveCondition.getDatetimeEnd();
                        rtRetrieveCondition.setPageIndex(String.valueOf(currentIndex));
                        rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                        //查询零售单
                        currentPage.setText("1"); // 由于是点击搜索按钮，所以初始化页码为1
                        queryRetailTrade(rtRetrieveCondition);//此处跳转至查单
                        // tvQueryRetailTrade.setClickable(true);
                    } else {
                        /**
                         * 单据号不符合标准
                         */
                        showToastMessage(RetailTrade.FIELD_ERROR_sn_ForQuery);
                        tvQueryRetailTrade.setClickable(true);
                        // 初始化页数
                        currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);//查单界面，当前第几页
                        totalPage.setText("0");//查单界面，共有多少页
                        iTotalPage = 0;//用于记录查单时，展示的总页数
                        // RecyclerView 刷新，初始化，查单页面数据清空
                        dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderRecyclerViewAdapter1(null, getActivity());
                        check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter1);
                        dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                    }
                    //清除商品页面所有数据
                    clearRetailTradeCommodityList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                HideVirtualKeyBoard();
                break;
            case R.id.check_list_page://点击左侧的页面，关闭右侧页面
                if (ReturnGoodsPage.getVisibility() == View.VISIBLE) {
                    ReturnGoodsPage.setVisibility(View.GONE);
                    setAnimation(ReturnGoodsPage);
                    bottomLinear.setOrientation(LinearLayout.HORIZONTAL);
                }
                HideVirtualKeyBoard();
                //  dialogCheckListOrderRecyclerViewAdapter1.setDefSelect(-1);//清空点击状态，但是要保持已经被点击项高亮,所以不能做刷新动作
                break;
            case R.id.last_page://上一页按钮
                isSearchButtonClicked = false;

                //防止用户快速连续点击，查完零售单后会把lastPage设为可点击状态
                lastPage.setClickable(false);
                //
                showToastMessage("上一页");
                rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
                int iCurrentPage = Integer.parseInt(currentPage.getText().toString());
                if (iCurrentPage <= iTotalPage && iCurrentPage >= 1) {
                    currentPage.setText(String.valueOf(iCurrentPage - 1)); // 页数 - 1
                }
                //获取单据号查询条件
                //lastQueryKeyWordBySearchButton上一次点击搜索按钮时输入的有效长度的单据号
                rtRetrieveCondition.setQueryKeyword(lastQueryKeyWordBySearchButton);
                //获取时间查询条件
                //上一次点击搜索按钮时输入的起始时间
                rtRetrieveCondition.setDatetimeStart(lastDatetimeStartBySearchButton);
                //上一次点击搜索按钮时输入的结束时间
                rtRetrieveCondition.setDatetimeEnd(lastDatetimeEndBySearchButton);
                rtRetrieveCondition.setPageIndex(String.valueOf(iCurrentPage - 1)); // 页数 - 1
                rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                //查询零售单
                queryRetailTrade(rtRetrieveCondition);
                //
                clearRetailTradeCommodityList();
                break;
            case R.id.next_page://下一页按钮
                isSearchButtonClicked = false;
                //防止用户快速连续点击，查完零售单后会把nexPage设为可点击状态
                nextPage.setClickable(false);
                //
                showToastMessage("下一页");
                rtRetrieveCondition.setQueryKeyword(check_list_sn.getText().toString());
                iCurrentPage = Integer.parseInt(currentPage.getText().toString());
                if (iCurrentPage < iTotalPage) {
                    currentPage.setText(String.valueOf(iCurrentPage + 1)); // 页数 + 1
                }
                //获取单据号查询条件
                //lastQueryKeyWordBySearchButton上一次点击搜索按钮时输入的有效长度的单据号
                rtRetrieveCondition.setQueryKeyword(lastQueryKeyWordBySearchButton);
                //获取时间查询条件
                //上一次点击搜索按钮时输入的起始时间
                rtRetrieveCondition.setDatetimeStart(lastDatetimeStartBySearchButton);
                //上一次点击搜索按钮时输入的结束时间
                rtRetrieveCondition.setDatetimeEnd(lastDatetimeEndBySearchButton);
                rtRetrieveCondition.setPageIndex(String.valueOf(iCurrentPage + 1)); // 页数 + 1
                rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                //查询零售单
                queryRetailTrade(rtRetrieveCondition);
                //
                clearRetailTradeCommodityList();
                break;
            case R.id.check_list_startDate://开始日期
                /**
                 * 下面为功能代码
                 */
                if (check_list_startDate.getText().toString().equals("")) { //第一次选择日期，获取当前时间
                    Calendar ca = Calendar.getInstance();
                    lastSelectedYear_check_list_startDate = ca.get(Calendar.YEAR);
                    lastSelectedMonth_check_list_startDate = ca.get(Calendar.MONTH);
                    lastSelectedDate_check_list_startDate = ca.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strMonth = String.valueOf(month + 1);
                        String strDate = String.valueOf(dayOfMonth);
                        if (month + 1 < 10) {
                            strMonth = "0" + strMonth;
                        }
                        if (dayOfMonth < 10) {
                            strDate = "0" + dayOfMonth;
                        }
                        lastSelectedYear_check_list_startDate = year;
                        lastSelectedMonth_check_list_startDate = month;
                        lastSelectedDate_check_list_startDate = dayOfMonth;
                        check_list_startDate.setText(year + "/" + strMonth + "/" + strDate);
                    }
                }, lastSelectedYear_check_list_startDate, lastSelectedMonth_check_list_startDate, lastSelectedDate_check_list_startDate);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                try {
                    if (!"".equals(check_list_endDate.getText().toString())) {
                        datePicker.setMaxDate((Constants.getSimpleDateFormat3().parse(check_list_endDate.getText().toString())).getTime());
                        datePicker.setMinDate(Constants.getDefaultSyncDatetime().getTime());
                    } else {
                        datePicker.setMaxDate(Calendar.getInstance().getTime().getTime());
                        datePicker.setMinDate(Constants.getDefaultSyncDatetime().getTime());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
                break;
            case R.id.check_list_endDate://结束日期
                /**
                 * 下面为功能代码
                 */
                if (check_list_endDate.getText().toString().equals("")) { //第一次选择日期，获取当前时间
                    Calendar ca = Calendar.getInstance();
                    lastSelectedYear_check_list_endDate = ca.get(Calendar.YEAR);
                    lastSelectedMonth_check_list_endDate = ca.get(Calendar.MONTH);
                    lastSelectedDate_check_list_endDate = ca.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strMonth = String.valueOf(month + 1);
                        String strDate = String.valueOf(dayOfMonth);
                        if (month + 1 < 10) {
                            strMonth = "0" + strMonth;
                        }
                        if (dayOfMonth < 10) {
                            strDate = "0" + dayOfMonth;
                        }
                        lastSelectedYear_check_list_endDate = year;
                        lastSelectedMonth_check_list_endDate = month;
                        lastSelectedDate_check_list_endDate = dayOfMonth;
                        check_list_endDate.setText(year + "/" + strMonth + "/" + strDate);
                    }
                }, lastSelectedYear_check_list_endDate, lastSelectedMonth_check_list_endDate, lastSelectedDate_check_list_endDate);
                DatePicker datePicker2 = datePickerDialog2.getDatePicker();
                try {
                    if (!"".equals(check_list_startDate.getText().toString())) {
                        datePicker2.setMaxDate(Calendar.getInstance().getTime().getTime());
                        datePicker2.setMinDate((Constants.getSimpleDateFormat3().parse(check_list_startDate.getText().toString())).getTime());
                    } else {
                        datePicker2.setMaxDate(Calendar.getInstance().getTime().getTime());
                        datePicker2.setMinDate(Constants.getDefaultSyncDatetime().getTime());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                datePickerDialog2.show();
                break;
            case R.id.reprint_SmallSheet://重打小票
                /**
                 * 下面为功能代码
                 */
                if (retailTradeSelected != null) {
                    retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
                    RetailTrade retailTrade = new RetailTrade();
                    retailTrade.setID(retailTradeSelected.getID());
                    RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
                    if (retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade1 == null) {
                        log.error("retailTradePresenter.retrieve1ObjectSync失败,错误码=" + retailTradePresenter.getLastErrorCode());
                        showToastMessage("找不到零售单");
                    }

                    try {
                        printSmallSheet(retailTrade1);
                    } catch (Exception e) {
                        log.info("打印小票异常：" + e.getMessage());
                    }
                } else {
                    showToastMessage("请选择零售单");
                }
                break;
            case R.id.confirm_return_goods://退货按钮
                /**
                 * 点击退货按钮之后应当将退货按钮更改为重打小票
                 */
                confirm_returnCommodity.setVisibility(View.GONE);
                reprintSmallSheet.setVisibility(View.VISIBLE);
                /**
                 * 下面为功能代码
                 */
                if (CheckRetailTrade()) { //检查零售单是否为退货单等
                    System.out.println("终止掉了");
                    return;
                }

                confirm_returnCommodity.post(new Runnable() {
                    @Override
                    public void run() {
                        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
                    }
                });
                if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getActivity())) {
                    confirmReturnCommodity();
                } else {
                    // 断网情况下，当且仅当纯现金支付，才可以退货。其它支付方式不可以退货
                    if (retailTradeSelected.payViaCashOnly()) {
                        confirmReturnCommodity();
                    } else {
                        showRetailTradeCommodity(retailTradeSelected);// 重新加载选中的零售单从表。注意查看列表有无残留其它零售单的商品
                        returnAmount.setText("￥0.00");
                        returnAmount.setVisibility(View.INVISIBLE);
                        confirm_returnCommodity.setVisibility(View.INVISIBLE);
                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);
                    }
                }
                break;
            case R.id.delete_all:
                check_list_sn.setText("");
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        closeLoadingDialog(loadingDailog); // 查单时过快操作（转到其它页面），转圈圈一直不会消失。必须显式关闭。
    }

    /**
     * --------------------------------------------------------eventbus事件-------------------------------------------------------------------------------------
     *
     * @param event
     */


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        log.info("#########################################################MainActivity onRetailTradeSQLiteEvent");
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent(); //
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                retrieveNRetailTradeList = (List<RetailTrade>) event.getListMasterTable();

                int count = Integer.parseInt(retailTradeHttpEvent.getCount());
                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);//查询零售单需要totalPageIndex页才能查完

                closeLoadingDialog(loadingDailog);
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done) {
                closeLoadingDialog(loadingDailog);
            }

            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                closeLoadingDialog(loadingDailog);
                showToastMessage(event.getLastErrorMessage());
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade) {
            System.out.println("该event在POSTING中已经处理：" + BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("MainActivity.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处MainActivity.onRetailTradeSQLiteEvent1()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeSQLiteEvent1(RetailTradeSQLiteEvent event) throws InterruptedException {
        System.out.println("#########################################################POSTING");
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (!updateRetailTradeAggregation((RetailTrade) event.getBaseModel1())) {
                    //TODO 以后根据用户反应再作处理
                }
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            log.info("该event在MAIN中已经处理：" + BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("MainActivity.onRetailTradeSQLiteEvent1()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处MainActivity.onRetailTradeSQLiteEvent1()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_RetailTrade_RetrieveNOldRetailTrade && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (event.getListMasterTable() == null || event.getListMasterTable().size() == 0) {
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }

            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                closeLoadingDialog(loadingDailog);
                showToastMessage(event.getLastErrorMessage());
            }

        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseActivity.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseActivity.retailTradeAggregation);
                } else {
                    log.error("创建临时收银汇总失败！");
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    System.out.println("更新收银汇总成功！收银汇总=" + event.getBaseModel1());
                } else {
                    log.error("更新收银汇总失败！");
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReturnCommodityEvent(ReturnCommodityHttpEvent event) {
        log.info("----------------------------------------------onReturnCommodityEvent1");
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            log.info(event.getStatus());
            closeLoadingDialog(loadingDailog);
            String msg = "";
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                if (event.getMessageBeforeEventPosted().length() > 0) {
                    closeLoadingDialog(loadingDailog);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("警告")
                            .setMessage(event.getMessageBeforeEventPosted())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // RecyclerView 刷新，初始化
                                    dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderRecyclerViewAdapter1(null, getActivity());
                                    check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter1);
                                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                                    //
                                    clearRetailTradeCommodityList();
                                    //show 零售单
                                    showRetailTrade(retrieveNRetailTradeList);
                                }
                            })
                            .create().show();
                }
            } else {
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
                    msg = "微信退款失败！" + wxOutRefundErrorMsg;
                } else if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
                    msg = "退货单创建失败！";
                }
            }
            Message message = new Message();
            message.obj = msg;
            message.what = 7;
            handler.sendMessage(message);


        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if ("WXRefund".equals(event.getWxPayStatus())) {
                wxOutRefundNO = event.getRefundResponse().get("out_refund_no");
                event.setWxPayStatus("");
            } else if ("WXRefundFail".equals(event.getWxPayStatus())) {
                wxOutRefundErrorMsg = event.getLastErrorMessage(); // 微信返回的错误信息
                event.setWxPayStatus("");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    /**
     * 下面为全部功能代码--------------------------------------------------我是分界线------------------------------------------------------------------
     */


    /**
     * 创建退货单后，更新本地收银汇总
     */
    private boolean updateRetailTradeAggregation(RetailTrade rt) {
        System.out.println("创建退货单成功后，更新SQLite的收银汇总");
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        //未发生变化的数据
        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(Constants.posID);
        retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO());/*销售单数*/
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        //发生变化的数据：
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前退货单的生成时间*/
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));/*现金收入*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));/*微信收入*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
        //单数不用更新

        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("创建退货单后，准备更新零售单汇总失败！");
            return false;
        }
        long lTimeOut = TIME_OUT;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            log.error("Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
            return false;
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("Update错误码不正确！" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
            return false;
        } else {
            // 创建退货单后，收银汇总数据需要减去相应的钱。
            BaseActivity.retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));
            BaseActivity.retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseActivity.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));
            BaseActivity.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTradeAggregation.getWechatAmount()));
            BaseActivity.retailTradeAggregation.setWorkTimeEnd(date);
        }

        return true;
    }

    private Date getDefaultSyncDatetime() {
        try {
            return Constants.getDefaultSyncDatetime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initBOAndEvent() {
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }

        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }

        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }

        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }

        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, null);
        }

        if (returnCommodityHttpEvent == null) {
            returnCommodityHttpEvent = new ReturnCommodityHttpEvent();
            returnCommodityHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }

        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }

        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
        }
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);

        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);

    }

    private void initUI() {
        currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);
        totalPage.setText("0");
        showRetailTrade(null); // 清空零售单列表
        nextPage.setClickable(false); // 不能再分页
        lastPage.setClickable(false); // 不能再分页
        tvQueryRetailTrade.setClickable(true); // 让收银员可以再点击搜索按钮
    }

    //联网查询零售单或者本地查询
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<RetailTrade> queryRetailTrade(RetailTrade retailTrade) {
        log.info("查询零售单的条件：" + retailTrade);
        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getActivity().getApplication())) { // 联网查询
            try {
                String msg = retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                if ("".equals(msg)) {
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                    if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
                        log.info("查询旧零售单失败！！");
                        closeLoadingDialog(loadingDailog);
                        Toast.makeText(getActivity(), "网络故障，请重新登录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showToastMessage(msg);
                }
            } catch (Exception e) {
                log.info("seekRetailTrade异常" + e.getMessage());
                e.printStackTrace();
            }
            return (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();

        } else if (bNetworkDisconnectedForTheFirstTime && GlobalController.getInstance().getSessionID() != null && !isSearchButtonClicked) { //如果是中途断网情况下点击上一页或下一页，提示错误信息，所以需要判断!isSearchButtonClicked
            showToastMessage("网络故障，请稍后再试！");
            //
            GlobalController.getInstance().setSessionID(null);
            bNetworkDisconnectedForTheFirstTime = false;
            // 页面初始化
            initUI();
            return null;
        } else {// 断网情况下，从SQLite中查询
            log.debug("进行本地查单操作：" + retailTrade);
            if (!StringUtils.isEmpty(retailTrade.getQueryKeyword())) {
                String msg = retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                if (!"".equals(msg)) {
                    showToastMessage(msg);
                    initUI();
                    return null;
                }
            }
            // 先得到零售单总条数，进行分页
            RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
            // 当断网时点击搜索，显示的是第一页的数据
            String[] conditions = null;
            String[] conditionsForTotalPage = null;
            retailTrade.setSn(retailTrade.getQueryKeyword());
            try {
                String sqlForTotalPage = "where F_SaleDatetime BETWEEN ? and ? ";
                if (StringUtils.isEmpty(retailTrade.getSn())) { //true 为空,sn为空，查全部    //优化：1.if中有没有分支可以去掉  2.翻页isPageTurning 还原数据问题
                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd())};
                } else {
                    sqlForTotalPage += " and F_SN like ?";
                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd()), "%" + retailTrade.getSn() + "%"};
                }
                sqlForTotalPage += "order by F_SaleDatetime desc";
                retailTrade.setSql(sqlForTotalPage);
                retailTrade.setConditions(conditionsForTotalPage);
                //
                List<RetailTrade> totalPageByConditions = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
                if (totalPageByConditions.size() == 0) {
                    showToastMessage("没有符合条件的零售单");
                    initUI();
                    return null;
                }
                //
                Integer count = totalPageByConditions.size();
                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);
                totalPage.setText(String.valueOf(iTotalPage));
                if (Integer.parseInt(currentPage.getText().toString()) > iTotalPage) {
                    currentPage.setText(String.valueOf(iTotalPage));
                }

                // 查询本地的零售单
                int start = (Integer.parseInt(currentPage.getText().toString()) - 1) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default);
                int end = Integer.parseInt(currentPage.getText().toString()) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default);
                if (end > totalPageByConditions.size()) {
                    end = totalPageByConditions.size();
                }
                List<RetailTrade> showRetailTrades = new ArrayList<RetailTrade>();
                for (int i = start; i < end; i++) {
                    showRetailTrades.add(totalPageByConditions.get(i));
                }

                // 查询零售单从表
                for (int i = 0; i < showRetailTrades.size(); i++) {
                    RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                    conditions = new String[]{String.valueOf(showRetailTrades.get(i).getID())};
                    String sqlCommodity = "where F_TradeID = ?";
                    retailTradeCommodity.setSql(sqlCommodity);
                    retailTradeCommodity.setConditions(conditions);

                    List<RetailTradeCommodity> showRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity);
                    //将从表设置进主表
                    showRetailTrades.get(i).setListSlave1(showRetailTradeCommodityList);
                }
                // 显示
                totalPage.setText(String.valueOf(iTotalPage));
                currentPage.setText(currentPage.getText().toString()); // 展现当前页
                showRetailTrade(showRetailTrades); //将每次查询到的主从表进行展示
                showPagingButton();
                // 断网情况下也需要将该list赋值给该list
                retrieveNRetailTradeList = showRetailTrades;
                showToastMessage("搜索本地零售单成功");
                return showRetailTrades; //...
            } catch (Exception e) {
                e.printStackTrace();
                initUI();
                return null;
            }
        }
    }

    private void showRetailTrade(final List<RetailTrade> list) {
        dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderRecyclerViewAdapter1(list, getActivity());
        check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter1);
        isRetailTradeCommodityNumberEditable = true;
        dialogCheckListOrderRecyclerViewAdapter1.setOnItemListener(new DialogCheckListOrderRecyclerViewAdapter1.OnItemListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder holder, int position) {
                HideVirtualKeyBoard();
                ReturnGoods_Number = 0;//将退货数量重设为0；
                tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");
                returnAmount.setText("￥0.00");
                //当查单列表被选中时，显示的按钮必须是重打小票
                confirm_returnCommodity.setVisibility(View.GONE);
                reprintSmallSheet.setVisibility(View.VISIBLE);
                //1.选中零售单再点击的时候，不必执行打开退货页面的操作
                // 2.当退货页面不可见时，令退货页面可见
                if (!list.get(position).isSelect && ReturnGoodsPage.getVisibility() == View.GONE) {
                    bottomLinear.setOrientation(LinearLayout.VERTICAL);
                    ReturnGoodsPage.setVisibility(View.VISIBLE);
                    setAnimation(ReturnGoodsPage);
                }

                dialogCheckListOrderRecyclerViewAdapter1.setDefSelect(position);
                dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                dAmountToReturnToCustomer = 0.000000d;/*初始化应退金额*/

//                //如果操作者点击其他的零售单         无需退出退货模式，查单页面的数据被点击之后是一直处于退货模式的
//                if (returnCommodity_select == 1) {
//                    leaveReturnCommodityMode();
//                }
                if (list.get(position).isSelect) {      //此处为如果已经选中，再点一次将把右侧商品页面置为空
                    retailTradeAfterConfirmReturn = new RetailTrade();
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                    //选中状态点击之后改为未选中
                    retailTradeSelected = null;
                    list.get(position).isSelect = false;
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                    //选中状态点击之后改为未选中 退货按钮恢复初始状态
                    clearRetailTradeCommodityList();
                } else {    //如果点击的是另一个零售单，右侧商品页面将刷新为新点击的零售单的商品
                    // 查单后选中某零售单，显示对应商品信息
                    positionOfRetailTradeSelected = position;
                    retailTradeAfterConfirmReturn = (RetailTrade) list.get(position).clone();
                    retailTradeSelected = (RetailTrade) list.get(position).clone(); //记录当前选中的零售单
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                    //未选中状态点击之后改为选中
                    list.get(position).isSelect = true;
//                    selectedRetailTrade = checkListRetailTradeList.get(position); // 选中的零售单，用于退货退款
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();

                    List<RetailTradeCommodity> selectRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
                    if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                        log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
                        //  returnCommodity.setClickable(false);
                        showToastMessage("该单包含已删除商品，无法显示");
                        check_list_commodity_rv.setAdapter(null);
                        return; // TODO
                    }
                    double dTotalOriginalPrice = 0.000000d; //原价总和
                    double dShouldPay = 0.000000d;//应收款
                    double dActuallyPay = 0.000000d;//实收款。现在这个变量没用
                    for (RetailTradeCommodity rtc : selectRetailTradeCommodityList) {
                        if (rtc.getPriceOriginal() <= BaseModel.TOLERANCE || rtc.getPriceReturn() <= BaseModel.TOLERANCE) { // 若某个商品价格为零则忽略该商品的计算
                            continue;
                        }
                        dTotalOriginalPrice = GeneralUtil.sum(dTotalOriginalPrice, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
                        dShouldPay = GeneralUtil.sum(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dShouldPay);
                        dActuallyPay = dShouldPay;
                    }//应收和实收 待以后再商议放入什么数据 现在目前先放入零售单金额 ----need to design
                    if (!(dTotalOriginalPrice == 0.000000d || dShouldPay == 0.000000d)) { // 判断单个商品是否为零,是否需要折扣，总计等计算。
                        checkListOriginalPrice.setText(GeneralUtil.formatToShow(dTotalOriginalPrice));
                        checkListReceivable.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmount()));//现在目前先放入零售单金额
                        checkListNetReceipts.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmount()));//现在目前先放入零售单金额
                        checkListDiscount.setText(GeneralUtil.div(GeneralUtil.mul(dShouldPay, 100.000000d), dTotalOriginalPrice, 2) + "%");
                    } else {
                        checkListOriginalPrice.setText("0.00");
                        checkListReceivable.setText("0.00");
                        checkListNetReceipts.setText("0.00");
                        checkListDiscount.setText("0.00%");
                    }
                    returnAmount.setText("￥0.00");

                    //设置原价总金额，支付方式
                    if (retailTradeAfterConfirmReturn.getAmountCash() != 0) {
                        checkListPaymentMethod.setText("现金");
                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
                            checkListPaymentMethod.setText("现金、支付宝");
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                checkListPaymentMethod.setText("现金、支付宝、微信");
                            }
                        } else {
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                checkListPaymentMethod.setText("现金、微信");
                            }
                        }
                    } else {
                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
                            checkListPaymentMethod.setText("支付宝");
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                checkListPaymentMethod.setText("支付宝、微信");
                            }
                        } else {
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                checkListPaymentMethod.setText("微信");
                            }
                        }
                    }
                    //展示零售单商品
                    showRetailTradeCommodity(retailTradeAfterConfirmReturn);
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();
                }
            }
        });
    }

    private void showRetailTradeCommodity(RetailTrade retailTrade) {
        retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        final List<RetailTradeCommodity> retailTradeCommodityListSelected = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
        log.info("零售单商品列表：" + retailTradeCommodityAfterConfirmReturn);
        //
        if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
            log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
            showToastMessage("该单包含已删除商品，无法显示");
            check_list_commodity_rv.setAdapter(null);
            return; // TODO
        }
        //
        for (int i = 0; i < retailTradeCommodityAfterConfirmReturn.size(); i++) {
            retailTradeCommodityAfterConfirmReturn.get(i).setNum(i + 1);
            Commodity commodity = new Commodity();
            commodity.setID(Long.valueOf(retailTradeCommodityAfterConfirmReturn.get(i).getCommodityID()));
            commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            if (commodity != null) {
                retailTradeCommodityAfterConfirmReturn.get(i).setName(commodity.getName());
            } else {
                showToastMessage("需要先进行商品的同步");
            }
        }
        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityRecyclerViewAdapter1(retailTradeCommodityAfterConfirmReturn, getActivity());
        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter1);
        dialogCheckListCommodityRecyclerViewAdapter1.setOnItemListener(new DialogCheckListCommodityRecyclerViewAdapter1.OnItemListener() {
            @Override
            public void onClick(DialogCheckListCommodityRecyclerViewAdapter1.MyViewHolder holder, final int position) {
                dialogCheckListCommodityRecyclerViewAdapter1.setDefItem(position);
                log.info(position);
                dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
                if (retailTradeCommodityAfterConfirmReturn.get(position).isSelect) {
                    //选中状态点击之后改为未选中
                    retailTradeCommodityAfterConfirmReturn.get(position).isSelect = false;
                    dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
                } else {
                    //未选中状态点击之后改为选中
                    retailTradeCommodityAfterConfirmReturn.get(position).isSelect = true;

                    commNumber = retailTradeCommodityListSelected.get(position).getNO();//零售单商品数量
                    noCanReturn = retailTradeCommodityListSelected.get(position).getNOCanReturn();//商品最大可退货数量
                    remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量

                    //是否展示编辑数量的按钮
//                    if (isRetailTradeCommodityNumberEditable) {       //因UI界面改变，这里必定展示编辑数量按钮
//                        retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = true;
//                    } else {
//                        retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = false;
//                    }
                    //设置可以编辑数量；
                    retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = true;
                    dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
                }
            }
        });

        dialogCheckListCommodityRecyclerViewAdapter1.setModifyCountInterface(new DialogCheckListCommodityRecyclerViewAdapter1.ModifyCountInterface() {
            @Override
            public void doIncrease(int position, View showCountView, View total_money, MotionEvent event) {
                //如果用户不选中项目，直接点击加减，应当先获取商品数量
                commNumber = retailTradeCommodityListSelected.get(position).getNO();//零售单商品数量
                System.out.println("零售单商品数量" + commNumber);
                noCanReturn = retailTradeCommodityListSelected.get(position).getNOCanReturn();//商品最大可退货数量
                System.out.println("商品最大可退货数量" + commNumber);
                remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量

                if (CheckRetailTrade()) return; //点击加减数量按钮时，判断是否可以修改此单

                final LongClickData longClickData = new LongClickData();
                longClickData.bPosition = position;
                longClickData.bShowCountView = showCountView;
                longClickData.bTotal_money = total_money;
                Thread plusThread = new Thread(new Thread() {
                    @Override
                    public void run() {
                        while (isOnLongClick) {
                            try {
                                Thread.sleep(200);
                                Message message = new Message();
                                message.obj = longClickData;
                                message.what = 12;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        super.run();
                    }
                });
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isOnLongClick = true;
                    plusThread.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusThread != null) {
                        isOnLongClick = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (plusThread != null) {
                        isOnLongClick = false;
                    }
                }
            }

            @Override
            public void doDecrease(int position, View showCountView, View total_money, MotionEvent event) {
                //如果用户不选中项目，直接点击加减，应当先获取商品数量
                commNumber = retailTradeCommodityListSelected.get(position).getNO();//零售单商品数量
                System.out.println("零售单商品数量" + commNumber);
                noCanReturn = retailTradeCommodityListSelected.get(position).getNOCanReturn();//商品最大可退货数量
                System.out.println("商品最大可退货数量" + commNumber);
                remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量

                if (CheckRetailTrade()) return; //点击加减数量按钮时，判断是否可以修改此单

                final LongClickData longClickData = new LongClickData();
                longClickData.bPosition = position;
                longClickData.bShowCountView = showCountView;
                longClickData.bTotal_money = total_money;
                Thread miusThread = new Thread(new Thread() {
                    @Override
                    public void run() {
                        while (isOnLongClick) {
                            try {
                                Thread.sleep(200);
                                Message message = new Message();
                                message.obj = longClickData;
                                message.what = 13;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        super.run();
                    }
                });
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isOnLongClick = true;
                    miusThread.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (miusThread != null) {
                        isOnLongClick = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (miusThread != null) {
                        isOnLongClick = false;
                    }
                }
            }
        });
    }

    /**
     * 控制查单页面分页按钮的状态
     */
    private void showPagingButton() {
        Integer iCurrentPage = Integer.parseInt(currentPage.getText().toString());
        nextPage.setClickable(true);
        lastPage.setClickable(true);
        tvQueryRetailTrade.setClickable(true);
        //
        if (iCurrentPage == 1) { // 第一页
            lastPage.setClickable(false);
        }
        if (iCurrentPage == iTotalPage) {// 最后一页
            nextPage.setClickable(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            DisplayCustomKeyBoard(check_list_sn, true, false);
        }
        return false;
    }

    /**
     * 长按退货界面商品的+-按钮时，传递给message handler的数据
     */
    public class LongClickData {
        public int bPosition;
        public View bShowCountView;
        public View bTotal_money;
    }

    /**
     * 退出退货模式
     */
    private void leaveReturnCommodityMode() {
//        returnCommodity_select = 0;
//        isRetailTradeCommodityNumberEditable = false;
    }

    /**
     * TODO 待重命名
     */
    private void clearRetailTradeCommodityList() {
        // 清空查单页面右边的、某一零售单的商品信息
        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityRecyclerViewAdapter1(null, getActivity());
        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter1);
        dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
        // 重置零售单应收实收等数据
        checkListOriginalPrice.setText("0.00");
        checkListReceivable.setText("0.00");
        checkListNetReceipts.setText("0.00");
        checkListDiscount.setText("0.00%");
        checkListPaymentMethod.setText("");
//        // 重置退货按钮
//        leaveReturnCommodityMode();
        // 重置：之前想退但是没退的零售单,让点退货按钮时弹出“请选择要退货的零售单”
        retailTradeSelected = null;
    }

    /*
   查单UI里选中商品可以修改商品数量
    */
    public void doIncreaseInUI(List<RetailTradeCommodity> commodityList, int position, View showCountView, View total_money) {
        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
        int currentNumber = Integer.valueOf(((EditText) showCountView).getText().toString());
        currentNumber++;
        ((EditText) showCountView).setText(String.valueOf(currentNumber));
        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
        ((TextView) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
        retailTradeCommodity.setNO(currentNumber);
        /*((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).setNO(currentNumber);*/
        //计算应退金额
        /*double newAmount = 0.000000d;*/
        /*for (int i = 0; i < commodityList.size(); i++) {
            newAmount = GeneralUtil.sum(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.mul(commodityList.get(i).getNO(), commodityList.get(i).getPriceReturn()))), newAmount);
        }
        double dAmountToReturnToCustomer = GeneralUtil.sub(retailTradeAfterConfirmReturn.getAmount(), newAmount);*/
        dAmountToReturnToCustomer = GeneralUtil.sub(dAmountToReturnToCustomer, unitPrice);

        returnAmount.setText("￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
        //当商品数量发生变化时，确认退货按钮出现，重打小票按钮消失
        confirm_returnCommodity.setVisibility(View.VISIBLE);
        reprintSmallSheet.setVisibility(View.GONE);
        //记录并显示退货数量
        ReturnGoods_Number--;
        tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");

        //当商品数量恢复到原来的数量时，确认退货按钮消失，重打小票按钮出现
        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
            confirm_returnCommodity.setVisibility(View.GONE);
            reprintSmallSheet.setVisibility(View.VISIBLE);
        }
    }

    public void doDecreaseInUI(List<RetailTradeCommodity> commodityList, int position, View showCountView, View total_money) {
        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
        int currentNumber = Integer.valueOf(((EditText) showCountView).getText().toString());
        if (currentNumber == 0) {
            return;
        }
        currentNumber--;
        ((EditText) showCountView).setText(String.valueOf(currentNumber));
        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
        ((TextView) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
        retailTradeCommodity.setNO(currentNumber);
        /*((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).setNO(currentNumber);*/
        //计算应退金额
        /*double newAmount = 0.000000d;
        for (int i = 0; i < commodityList.size(); i++) {
            newAmount = GeneralUtil.sum(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.mul(Double.valueOf(commodityList.get(i).getNO()), commodityList.get(i).getPriceReturn()))), newAmount);
        }
        double dAmountToReturnToCustomer = GeneralUtil.sub(retailTradeAfterConfirmReturn.getAmount(), newAmount);*/
        //在纯现金支付的情况下，零售单的商品全部退货，允许应退金额与零售单金额存在0.01的误差，如果该误差存在的话，令应退金额等于零售单金额，以免在导致退货判断异常
        if (isReturnWholeRetailTrade(commodityList)
                || Math.abs(GeneralUtil.sub(dAmountToReturnToCustomer, retailTradeAfterConfirmReturn.getAmount()) - TORELANCE_ReturnWholeRetailTradeAllPaidByCash) < BaseModel.TOLERANCE) {
            dAmountToReturnToCustomer = retailTradeAfterConfirmReturn.getAmount();
        } else {
            dAmountToReturnToCustomer = GeneralUtil.sum(dAmountToReturnToCustomer, unitPrice);
        }
        returnAmount.setText("￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
        //当商品数量发生变化时，确认退货按钮出现，重打小票按钮消失
        confirm_returnCommodity.setVisibility(View.VISIBLE);
        reprintSmallSheet.setVisibility(View.GONE);
        //记录并显示退货数量
        ReturnGoods_Number++;
        tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");

        //当商品数量恢复到原来的数量时，确认退货按钮消失，重打小票按钮出现
        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
            confirm_returnCommodity.setVisibility(View.GONE);
            reprintSmallSheet.setVisibility(View.VISIBLE);
        }
    }

    //当零售单的商品数量全为0时，该零售单为全部商品退货：
    private boolean isReturnWholeRetailTrade(List<RetailTradeCommodity> commodityList) {
        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
        if (commodityList != null) {
            for (int i = 0; i < commodityList.size(); i++) {
                if (commodityList.get(i).getNO() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 控制退货按钮的状态//修改为 把右侧商品页重置回原来的数量并显示
     */
    private void setReturnCommodityButtonStyle() {
//        if (returnCommodity_select == 0) {
//            //当选择的零售单为退货单的时候，不能进入退货模式
//            if (retailTradeAfterConfirmReturn != null) {
//                if (retailTradeAfterConfirmReturn.getSourceID() == -1) { //TODO hardcode
//                    confirm_returnCommodity.setVisibility(View.VISIBLE);
//                    //当点击退货按钮后，弹出未亮起的确认退货按钮，当时商品数量发生改变的时候，确认退货按钮点亮
//                    returnAmount.setVisibility(View.VISIBLE);
//                    returnAmount.setText("应退金额：￥0.00");
//                    returnCommodity_select = 1;
//                    isRetailTradeCommodityNumberEditable = true;
//                } else {
//                    Toast.makeText(getActivity(), "该零售单为退货单，不允许进行退货操作！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else if (returnCommodity_select == 1) {
        if (retailTradeAfterConfirmReturn != null) {
            // 这里必须是使用clone出的对象，因为用户在点击+-时，是直接增减了商品的数量，而他不一定下一步就是点击确认退货
            retailTradeAfterConfirmReturn = (RetailTrade) retrieveNRetailTradeList.get(positionOfRetailTradeSelected).clone();
            showRetailTradeCommodity(retailTradeAfterConfirmReturn);
            returnAmount.setText("￥0.00");
        }
        dAmountToReturnToCustomer = 0.000000d;
//        }

    }

    /**
     * 确认退货，开始处理退货
     */
    private void confirmReturnCommodity() {

        try {
            if (retailTradeSelected.compareTo(retailTradeAfterConfirmReturn) != 0) {
                if (retailTradeSelected.getListSlave1() == null || retailTradeAfterConfirmReturn.getListSlave1() == null) {
                    log.error("发现异常零售单，其从表为null，ID=" + retailTradeAfterConfirmReturn.getID());
                    closeLoadingDialog(loadingDailog);
                    return;
                }
                //
                retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
                long tmpRowID = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                List<RetailTradeCommodity> listObjectToRemove = new ArrayList<>();
                for (int i = 0; i < retailTradeAfterConfirmReturn.getListSlave1().size(); i++) {
                    RetailTradeCommodity retailTradeCommodityAfterConfirmReturn = (RetailTradeCommodity) retailTradeAfterConfirmReturn.getListSlave1().get(i);
                    retailTradeCommodityAfterConfirmReturn.setID(tmpRowID + i);
                    //用原先的零售单商品数量减去现在退货零售单商品的数量得知需要退货多少。如界面中零售商品的数量为20，点击退货后修改零售单商品数量为19，则表示退货1
                    retailTradeCommodityAfterConfirmReturn.setNO(this.retailTradeCommodityAfterConfirmReturn.get(i).getNO() - retailTradeCommodityAfterConfirmReturn.getNO());
                    //说明该零售单商品并不需要退货
                    if (retailTradeCommodityAfterConfirmReturn.getNO() == 0) {
                        listObjectToRemove.add(retailTradeCommodityAfterConfirmReturn);
                        continue;
                    }
                    if (retailTradeCommodityAfterConfirmReturn.getNO() > this.retailTradeCommodityAfterConfirmReturn.get(i).getNOCanReturn()) {
                        Message message = new Message();
                        message.what = 9;
                        handler.sendMessage(message);
                        return;
                    }
                }
                for (int i = listObjectToRemove.size() - 1; i >= 0; i--) { // 从末尾开始减
                    retailTradeAfterConfirmReturn.getListSlave1().remove(listObjectToRemove.get(i));
//                    tradeCommodityList.remove(listIndexToRemove.get(i));
                }
                if (retailTradeAfterConfirmReturn.getListSlave1().size() == 0) {
                    System.out.println("第一个8");
                    Message message = new Message();
                    message.what = 8;
                    handler.sendMessage(message);
                    return;
                }

                retailTradeAfterConfirmReturn.setAmount(dAmountToReturnToCustomer);
                retailTradeAfterConfirmReturn.setSourceID(Integer.valueOf(String.valueOf(retailTradeAfterConfirmReturn.getID())));
                long returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                retailTradeAfterConfirmReturn.setID(returnCommRetailTradeID);
                retailTradeAfterConfirmReturn.setLocalSN((int) returnCommRetailTradeID);
                retailTradeAfterConfirmReturn.setPos_ID(Constants.posID);
                retailTradeAfterConfirmReturn.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                retailTradeAfterConfirmReturn.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                retailTradeAfterConfirmReturn.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                retailTradeAfterConfirmReturn.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                // 退货单需要知道零售时的支付宝支付金额和微信支付金额
                retailTradeAfterConfirmReturn.setSaleAmountAlipay(GeneralUtil.round(retailTradeSelected.getAmountAlipay(), 2));
                retailTradeAfterConfirmReturn.setSaleAmountWeChat(GeneralUtil.round(retailTradeSelected.getAmountWeChat(), 2));
                retailTradeAfterConfirmReturn.setSn(retailTradeSelected.getSn() + "_1"); // 退货单sn后面添加_1
                //
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder sbMsg = new StringBuilder();
                        try {
                            returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                            if (resolveAmountAndPaymentType(sbMsg, dAmountToReturnToCustomer)) { // 判断哪种支付方式退款
                                returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                                if (createReturnRetailTrade(sbMsg)) { // 创建退货单
                                    returnCommodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                                }
                            }
                        } catch (Exception e) {
                            log.error("退货要提醒的信息：" + sbMsg);
                            log.error("退货失败，错误信息：" + e.getMessage());
                        }
                        returnCommodityHttpEvent.setMessageBeforeEventPosted(sbMsg.toString());
                        EventBus.getDefault().post(returnCommodityHttpEvent);
                    }
                });
            } else {
                Message message = new Message();
                message.what = 8;
                handler.sendMessage(message);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Message message = new Message();
            message.what = 10;
            handler.sendMessage(message);
        }
    }

    private boolean CheckRetailTrade() {
        log.info("开始退货");
        /**
         * 退货之前先检查
         */
        if (retailTradeSelected == null) {
            showToastMessage("请选择要退货的零售单");
            return true;
        }
        // 判断该零售单是否超过一年
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        c.add(Calendar.YEAR, -1);
        Date date = c.getTime();
        if (date.getTime() > retailTradeSelected.getSaleDatetime().getTime()) {
            showToastMessage("该零售单已经超过一年，不能退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        // 判断该零售单创建时间是否超过120s,没有120s则不让退货
        if (new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime() - retailTradeSelected.getSaleDatetime().getTime() < MIN_Time_InSecond_ReturnRetailTrade * 1000) {
            showToastMessage("订单结算中，请稍后退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        // 检查是否已经创建退货单
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSourceID(Integer.valueOf(String.valueOf(retailTradeSelected.getID())));
        List<RetailTrade> returnRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned, retailTrade);
        if (returnRetailTradeList != null && returnRetailTradeList.size() > 0) {
            showToastMessage("该零售单已经退货过了，不能重复退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        if (retailTradeAfterConfirmReturn != null) {
            if (retailTradeAfterConfirmReturn.getSourceID() != -1) { //TODO hardcode
                showToastMessage("该零售单为退货单，不允许进行退货操作！");
                setReturnCommodityButtonStyle();
                return true;
            }
        }
        return false;
    }

    /**
     * 决出退款金额和退款方式
     */
    private boolean resolveAmountAndPaymentType(StringBuilder sbMsg, double totalMoneyToReturnToCustomer) { //TODO 移到Model层
        // 初始化支付金额
        retailTradeAfterConfirmReturn.setAmountWeChat(0);
        retailTradeAfterConfirmReturn.setAmountCash(0);
        // 初始化支付方式
        retailTradeAfterConfirmReturn.setPaymentType(0);
        // 设置总金额为退货金额
        retailTradeAfterConfirmReturn.setAmount(totalMoneyToReturnToCustomer);
        // 现金支付大于或等于退货价，现金退款
        if (GeneralUtil.sub(retailTradeSelected.getAmountCash(), totalMoneyToReturnToCustomer) >= 0) {
            log.info("纯现金退款" + totalMoneyToReturnToCustomer + "元");
            retailTradeAfterConfirmReturn.setAmountCash(totalMoneyToReturnToCustomer);
            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(totalMoneyToReturnToCustomer) + "元");
            // 微信支付等于退货价， 微信退款
        } else if (Math.abs(GeneralUtil.sub(retailTradeSelected.getAmountWeChat(), totalMoneyToReturnToCustomer)) < BaseModel.TOLERANCE) { // 负数的情况
            log.info("纯微信退款" + totalMoneyToReturnToCustomer + "元");
            retailTradeAfterConfirmReturn.setAmountWeChat(totalMoneyToReturnToCustomer);
            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 4); //TODO hardcode
            if (returnMoney(retailTradeAfterConfirmReturn, retailTradeSelected.getAmountWeChat())) {
                retailTradeAfterConfirmReturn.setWxRefundNO(wxOutRefundNO);// 更新退货单号
                sbMsg.append("微信退款成功，金额：" + totalMoneyToReturnToCustomer + "元\r\n");
            } else {
                return false;
            }
        }
        // 微信支付和现金支付都小于退货价，先退微信，再退现金
        else if (retailTradeSelected.getAmountCash() < totalMoneyToReturnToCustomer && retailTradeSelected.getAmountWeChat() < totalMoneyToReturnToCustomer) {//...没有对支付宝支付时进行判断
            log.info("微信退款" + retailTradeSelected.getAmountWeChat() + "元");
            retailTradeAfterConfirmReturn.setAmountWeChat(retailTradeSelected.getAmountWeChat());
            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 4); //TODO hardcode
            if (returnMoney(retailTradeAfterConfirmReturn, retailTradeSelected.getAmountWeChat())) {
                retailTradeAfterConfirmReturn.setWxRefundNO(wxOutRefundNO);// 更新退货单号
                sbMsg.append("微信退款成功，金额：" + retailTradeSelected.getAmountWeChat() + "元\r\n");
            } else {
                return false;
            }
            //现金退款
            log.info("现金退款" + GeneralUtil.sub(totalMoneyToReturnToCustomer, retailTradeSelected.getAmountWeChat()) + "元");
            retailTradeAfterConfirmReturn.setAmountCash(GeneralUtil.sub(totalMoneyToReturnToCustomer, retailTradeSelected.getAmountWeChat()));
            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(retailTradeAfterConfirmReturn.getAmountCash()) + "元");
        }
        // 现金支付小于退货价，微信支付大于退货价，直接现金退款
        else if (retailTradeSelected.getAmountCash() < totalMoneyToReturnToCustomer && retailTradeSelected.getAmountWeChat() > totalMoneyToReturnToCustomer) {
            log.info("纯现金退款" + totalMoneyToReturnToCustomer + "元");
            retailTradeAfterConfirmReturn.setAmountCash(totalMoneyToReturnToCustomer);
            retailTradeAfterConfirmReturn.setPaymentType(retailTradeAfterConfirmReturn.getPaymentType() | 1); //TODO hardcode
            sbMsg.append("你还需退还顾客现金：" + GeneralUtil.formatToShow(totalMoneyToReturnToCustomer) + "元");
        } else {
            log.error("发现意外情况！");
        }

        return true;
    }

    /**
     * 进行微信退款操作。这是阻塞操作
     */
    public boolean returnMoney(RetailTrade retailTrade, double wxAmountToReturn) {
        long lTimeOut = TIME_OUT;

        RetailTrade returnRetailTrade = (RetailTrade) retailTrade.clone();
        returnRetailTrade.setAmount(WXPayUtil.formatAmount(wxAmountToReturn));
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        wxPayHttpBO.refundAsync(returnRetailTrade);
        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.info("mainActivityRefund异常:" + e.getMessage());
                e.printStackTrace();
            }
        }
        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            log.error("微信退款超时....");
            return false;
        }

        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("微信退款错误码错误：" + wxPayHttpEvent.getLastErrorCode());
            return false;
        }
        return true;
    }

    private boolean createReturnRetailTrade(StringBuilder sbMsg) throws InterruptedException {
        long lTimeOut = TIME_OUT;
        RetailTradeSQLiteEvent e = new RetailTradeSQLiteEvent();
        RetailTradeSQLiteBO bo = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), e, null);
        e.setSqliteBO(bo);
        e.setId(BaseEvent.EVENT_ID_MainActivity_CreateReturnRetailTrade);
        e.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        e.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        e.setMessageBeforeEventPosted(sbMsg.toString());
        System.out.println("xxxxxxxxxxxxxxx" + retailTradeAfterConfirmReturn.getSourceID());
        if (!bo.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAfterConfirmReturn)) {
            log.error("创建退货单失败！");// TODO
            closeLoadingDialog(loadingDailog);
            new AlertDialog.Builder(getActivity())
                    .setTitle("警告")
                    .setMessage("退货单创建超时！请联系客服。\r\n" + sbMsg.toString())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();
            return false;
        }
        while (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            log.error("钱已经退给顾客，但退货单没有创建成功！必须给SQLite插入这张单：retailTradeAfterConfirmReturn=" + retailTradeAfterConfirmReturn);
            showToastMessage("退货单创建超时！请找客服支持");
            closeLoadingDialog(loadingDailog);
            new AlertDialog.Builder(getActivity())
                    .setTitle("警告")
                    .setMessage("退货单创建超时！请找客服支持。\r\n" + sbMsg.toString())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();
            return false;
        }
        return true;
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 3://点击搜索按钮查询零售单，服务器返回的数据需要在UI进行展示
                    log.info("查询到的零售单有：" + retrieveNRetailTradeList);
                    //计算返回的零售单需要分几页进行查看
                    if (retrieveNRetailTradeList.size() > 0) {
                        totalPage.setText(String.valueOf(iTotalPage)); //...
                        showRetailTrade(retrieveNRetailTradeList);
                        showPagingButton();
                    }
                    System.out.println("xxxxxxxxxxxx3");
                    break;
                case 4:
                    closeLoadingDialog(loadingDailog);
                    showToastMessage("没有符合条件的零售单");
                    dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderRecyclerViewAdapter1(null, getActivity());
                    check_list_order_rv.setAdapter(dialogCheckListOrderRecyclerViewAdapter1);
                    dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();

                    isRetailTradeCommodityNumberEditable = false;
                    currentPage.setText("0");
                    totalPage.setText("0");
                    tvQueryRetailTrade.setClickable(true);
                    System.out.println("xxxxxxxxxxxx4");
                    break;
                case 6:
                    if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                        showToastMessage("退货失败，请重新操作！");
                    } else {
                        showToastMessage("退货成功，接下来可以进行退款操作");
                    }
                    confirm_returnCommodity.setVisibility(View.GONE);
                    returnAmount.setVisibility(View.GONE);
                    returnRetailTrade = null;
                    break;
                case 7:
                    String message = (String) msg.obj;
                    if (message.length() > 0) {
                        showToastMessage(message);
                    }
                    // 改变退货按钮的状态
                    setReturnCommodityButtonStyle();
                    break;
                case 8:
                    closeLoadingDialog(loadingDailog);
                    showToastMessage("还未选择退货商品和退货数量");
                    // 改变退货按钮的状态
                    setReturnCommodityButtonStyle();
                    break;
                case 9:
                    closeLoadingDialog(loadingDailog);
                    showToastMessage("本次的退货数量大于该商品的可退货数量");
                    // 改变退货按钮的状态
                    setReturnCommodityButtonStyle();
                    break;
                case 10:
                    closeLoadingDialog(loadingDailog);
                    showToastMessage("退货单创建失败！");
                    // 改变退货按钮的状态
                    setReturnCommodityButtonStyle();
                    break;
                case 11:
                    closeLoadingDialog(loadingDailog); // 网络故障，不能进行微信退款
                    showToastMessage("网络故障，请稍后再试！");
                    // 改变退货按钮的状态
                    setReturnCommodityButtonStyle();
                    break;
                case 12:
                    LongClickData longClickDataIncrease = (LongClickData) msg.obj;
                    if (Integer.valueOf(((EditText) longClickDataIncrease.bShowCountView).getText().toString()) < commNumber) {
                        doIncreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataIncrease.bPosition, longClickDataIncrease.bShowCountView, longClickDataIncrease.bTotal_money);
                    } else if (Integer.valueOf(((EditText) longClickDataIncrease.bShowCountView).getText().toString()) > commNumber) {
                        ((EditText) longClickDataIncrease.bShowCountView).setText(String.valueOf(commNumber));
                        showToastMessage("输入的数量不能大于原商品数量");
                    }
                    break;
                case 13:
                    LongClickData longClickDataDecrease = (LongClickData) msg.obj;
                    if (Integer.valueOf(((EditText) longClickDataDecrease.bShowCountView).getText().toString()) > remainingQuantity) {
                        doDecreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataDecrease.bPosition, longClickDataDecrease.bShowCountView, longClickDataDecrease.bTotal_money);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });


}
