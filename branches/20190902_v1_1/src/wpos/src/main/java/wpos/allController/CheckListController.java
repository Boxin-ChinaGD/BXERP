package wpos.allController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.model.Message;
import wpos.utils.PlatForm;
import wpos.adapter.DialogCheckListCommodityListCellAdapter;
import wpos.adapter.DialogCheckListOrderListCellAdapter;
import wpos.allEnum.ThreadMode;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.RetailTradeHttpEvent;
import wpos.event.ReturnCommodityHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.event.WXPayHttpEvent;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.CommodityPresenter;
import wpos.presenter.RetailTradeCommodityPresenter;
import wpos.presenter.RetailTradePresenter;
import wpos.utils.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Component("checkListController")
public class CheckListController implements PlatFormHandlerMessage {
    private static Logger log = Logger.getLogger(CheckListController.class);
    public static boolean firstTime = true;

    AllFragmentViewController viewController;

    public CheckListController() {
    }

    public CheckListController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        PlatForm.get().setHandlerMessage(this);
    }

    public void initData() {
        List<RetailTrade> showRetailTrades = new ArrayList<RetailTrade>();
        for (int i = 0; i < 10; i++) {
            showRetailTrades.add(getRetailTrade());
        }
        showRetailTrade(showRetailTrades);
    }


    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
        initBOAndEvent();
        // 重置搜索条件
        check_list_clear_click();
    }


    /**
     * ------------------------------------------------------分割线--------------------------------------------------------------------------------
     */

    /**
     * 最短的退货时间。必须在这个时间经过之后，才能对零售单进行退货。以秒为单位
     */
    private static final int MIN_Time_InSecond_ReturnRetailTrade = 120;
    private RetailTrade rtRetrieveCondition;//查询零售单，在该对象设置条件，传到NBR
    private RetailTrade returnRetailTrade; // 临时退货零售单
    private int ReturnGoods_Number = 0;//记录退货数量
    @Resource
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
    @Resource
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    @Resource
    private RetailTradePresenter retailTradePresenter;
    //    private LinearLayoutManager manager1;
//    private LinearLayoutManager manager2;
    private DialogCheckListOrderListCellAdapter dialogCheckListOrderRecyclerViewAdapter1;
    private DialogCheckListCommodityListCellAdapter dialogCheckListCommodityRecyclerViewAdapter1;

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
    @Resource
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    private RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    private RetailTradeHttpBO retailTradeHttpBO;
    @Resource
    private RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    @Resource
    private RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;
    @Resource
    private ReturnCommodityHttpEvent returnCommodityHttpEvent;
    @Resource
    private WXPayHttpBO wxPayHttpBO;
    @Resource
    private WXPayHttpEvent wxPayHttpEvent;
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

    public void init() {
        lastDatetimeEndBySearchButton = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeCommodityAfterConfirmReturn = new ArrayList<RetailTradeCommodity>();//查单界面查询到的零售单商品保存在该List
        retrieveNRetailTradeList = new ArrayList<>();
        checkListRetailTradeList = new ArrayList<>();
        showCheckListRetailTradeList = new ArrayList<>();//将查询到的所有零售单list进行分成数个size为10的list，用于在分页的时候直接调
        bNetworkDisconnectedForTheFirstTime = true;
        lastQueryKeyWordBySearchButton = "";
        // 刚打开查单页面时，设置上一页，下一页按钮不可用
//        lastPage.setClickable(false);
//        nextPage.setClickable(false);
        viewController.lastPage.setDisable(true);
        viewController.nextPage.setDisable(true);

//        manager1 = new LinearLayoutManager(getActivity());//提供给RecyclerView使用的
//        manager2 = new LinearLayoutManager(getActivity());//提供给RecyclerView使用的
//        manager1.setOrientation(LinearLayoutManager.VERTICAL);//设置LinearManger
//        manager2.setOrientation(LinearLayoutManager.VERTICAL);//设置LinearManger
//        check_list_order_rv.setLayoutManager(manager1);//绑定manger
//        check_list_commodity_rv.setLayoutManager(manager2);//绑定manger
        dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderListCellAdapter();
        viewController.check_list_order_rv.setCellFactory(new Callback() {
            @Override
            public Object call(Object e) {
                return dialogCheckListOrderRecyclerViewAdapter1;
            }
        });
        viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityListCellAdapter();
        viewController.check_list_commodity_rv.setCellFactory(new Callback() {
            @Override
            public Object call(Object e) {
                return dialogCheckListCommodityRecyclerViewAdapter1;
            }
        });
        viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
        initBOAndEvent();

//        check_list_sn.setOnTouchListener(this);
//        check_list_sn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (check_list_sn.getText().toString().equals("")) {
////                    rci_search1.setVisibility(View.GONE);
//                    rci_search1.setVisible(false);
//                } else {
////                    rci_search1.setVisibility(View.VISIBLE);
//                    rci_search1.setVisible(false);
//                }
//            }
//        });

        isSearchButtonClicked = false;
//        tvQueryRetailTrade.performClick();
    }

    public void check_list_sn_click() {
        viewController.check_list_sn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (viewController.check_list_sn.getText().toString().equals("")) {
                    viewController.deleteAll.setVisible(false);
                    viewController.deleteAll.setManaged(false);
                } else {
                    viewController.deleteAll.setVisible(true);
                    viewController.deleteAll.setManaged(true);
                }
            }
        });
    }

    protected static List<RetailTradeCommodity> getRetailTradeCommodityList(long tradeID) {
        List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
        Random ran = new Random();
        for (int i = 0; i < 3; i++) {
            RetailTradeCommodity rtcInput = new RetailTradeCommodity();
            rtcInput.setTradeID(tradeID);
            rtcInput.setCommodityID(i + 1);
            rtcInput.setCommodityName(Shared.generateCompanyName(8));
            rtcInput.setNO(ran.nextInt(999) + 1);
            rtcInput.setPriceOriginal(ran.nextDouble() + 1d);
            rtcInput.setDiscount(0.5d);
            rtcInput.setBarcodeID(1);
            rtcInput.setPriceReturn(6.66);
            rtcInput.setPriceVIPOriginal(3.22);
            rtcInput.setNOCanReturn(rtcInput.getNO());

            retailTradeCommodities.add(rtcInput);
        }

        Map<String, RetailTradeCommodity> map = new HashMap<String, RetailTradeCommodity>();
        for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodities) {
            map.put(String.valueOf(retailTradeCommodity.getCommodityID()), retailTradeCommodity);
        }

        return new ArrayList<RetailTradeCommodity>(map.values());
    }


    protected static RetailTrade getRetailTrade() {
        Random ran = new Random();
        RetailTrade retailTrade = new RetailTrade();

        try {
//                long tmpRowID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
            retailTrade.setLocalSN((int) 100001 + new Random().nextInt(1000));
//                retailTrade.setLocalSN((int) tmpRowID);
            retailTrade.setPos_ID(1);
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setLogo("11");
            retailTrade.setStaffID(1);
            retailTrade.setPaymentType(1);
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setPaymentAccount("12");
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2d);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setListSlave1(getRetailTradeCommodityList(retailTrade.getLocalSN()));
//                retailTrade.setListSlave1(getRetailTradeCommodityList(tmpRowID));
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
            retailTrade.setSn(retailTrade.generateRetailTradeSN(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (RetailTrade) retailTrade.clone();
    }

    public void cl_search_click() {
        isSearchButtonClicked = true;//标志点击了搜索按钮
//        tvQueryRetailTrade.setClickable(false);
        viewController.tvQueryRetailTrade.setDisable(false);
        // 隐藏键盘
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        rtRetrieveCondition = new RetailTrade();
        try {
            checkListRetailTradeList = new ArrayList<RetailTrade>();
            showCheckListRetailTradeList = new ArrayList<List<RetailTrade>>();
            Date endDate = null;
            Date startDate = null;
            LocalDate localDateStart = viewController.check_list_startDate.getValue();
            if (localDateStart != null) {
                startDate = Date.from(localDateStart.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            }
            LocalDate localDateEnd = viewController.check_list_endDate.getValue();
            if (localDateEnd != null) { //由于根据日期查询时不算时分秒，所以在搜索当天的零售单时，要将结束日期加一天
                endDate = Date.from(localDateEnd.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
                Calendar calendar = Calendar.getInstance();//日历对象
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }
            if (endDate != null && startDate != null && !DatetimeUtil.isAfterDate(endDate, startDate, 0)) {
                isSearchButtonClicked = false;
                viewController.tvQueryRetailTrade.setDisable(false);
                viewController.showToastMessage("起始日期不可以大于结束日期");
                return;
            }
            if (FieldFormat.checkRetailTradeRetrieveNBySN(viewController.check_list_sn.getText().toString())) {
                /**
                 *  单据号符合标准
                 */
                //lastQueryKeyWordBySearchButton记录上一次点击搜索按钮时输入的有效的单据号，点击上/下按钮需要用到
                lastQueryKeyWordBySearchButton = viewController.check_list_sn.getText().toString();
                rtRetrieveCondition.setQueryKeyword(viewController.check_list_sn.getText().toString());
                rtRetrieveCondition.setDatetimeStart(localDateStart == null ? Constants.getDefaultSyncDatetime() : Constants.getSimpleDateFormat3().parse(Constants.getSimpleDateFormat3().format(Date.from(localDateStart.atStartOfDay(ZoneOffset.ofHours(8)).toInstant()))));
                rtRetrieveCondition.setDatetimeEnd(localDateEnd == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : endDate);
                lastDatetimeStartBySearchButton = rtRetrieveCondition.getDatetimeStart();
                lastDatetimeEndBySearchButton = rtRetrieveCondition.getDatetimeEnd();
                rtRetrieveCondition.setPageIndex(String.valueOf(currentIndex));
                rtRetrieveCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                //查询零售单
                viewController.currentPage.setText("1"); // 由于是点击搜索按钮，所以初始化页码为1
                //
                queryRetailTrade(rtRetrieveCondition);//此处跳转至查单

                // tvQueryRetailTrade.setClickable(true);
            } else {
                /**
                 * 单据号不符合标准
                 */
                viewController.showToastMessage(RetailTrade.FIELD_ERROR_sn_ForQuery);
//                tvQueryRetailTrade.setClickable(true);
                viewController.tvQueryRetailTrade.setDisable(false);
                // 初始化页数
                viewController.currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);//查单界面，当前第几页
                viewController.totalPage.setText("0");//查单界面，共有多少页
                iTotalPage = 0;//用于记录查单时，展示的总页数
                // RecyclerView 刷新，初始化，查单页面数据清空
                retrieveNRetailTradeList.clear();
                viewController.check_list_order_rv.setItems(null);
                viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                viewController.check_list_order_rv.setCellFactory(new Callback() {
                    @Override
                    public Object call(Object e) {
                        return new DialogCheckListOrderListCellAdapter();
                    }
                });
            }
            //清除商品页面所有数据
            // TODO
//            clearRetailTradeCommodityList();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        HideVirtualKeyBoard();
    }


    // TODO 是否还需要尚不清楚
//    @Override
//    public void onStop() {  //重置一些数据为空或刚进来时的数据，因为fragment被destroy后，数据不一定被回收
//        retailTradeSelected = null;
//        positionOfRetailTradeSelected = -1;
//        dAmountToReturnToCustomer = 0.000000d;
//        commNumber = 0;
//        noCanReturn = 0;
//        isOnLongClick = false;
//        currentIndex = 1;
//        retailTradeAfterConfirmReturn = null;
//        manager1 = null;
//        manager2 = null;
//        lastQueryKeyWordBySearchButton = "";
//        ReturnGoods_Number = 0;
//        lastQueryKeyWordBySearchButton = "";
//        lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
//        super.onStop();
//    }


    // 重置搜索按钮
    public void check_list_clear_click() {
        viewController.check_list_sn.setText("");
        viewController.check_list_startDate.setValue(null);
        viewController.check_list_endDate.setValue(null);
    }

    //点击左侧的页面，关闭右侧页面
    public void cl_Pane_click() {
        if (viewController.return_goods_pane.isVisible()) {
            viewController.return_goods_pane.setVisible(false);
            viewController.return_goods_pane.setManaged(false);
        }
    }

    public void lastPage_click() {
        isSearchButtonClicked = false;

        //防止用户快速连续点击，查完零售单后会把lastPage设为可点击状态
        viewController.lastPage.setDisable(true);
        //
        viewController.showToastMessage("上一页");
        rtRetrieveCondition.setQueryKeyword(viewController.check_list_sn.getText().toString());
        int iCurrentPage = Integer.parseInt(viewController.currentPage.getText().toString());
        if (iCurrentPage <= iTotalPage && iCurrentPage >= 1) {
            viewController.currentPage.setText(String.valueOf(iCurrentPage - 1)); // 页数 - 1
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
    }

    public void nextPage_click() {
        isSearchButtonClicked = false;
        //防止用户快速连续点击，查完零售单后会把nexPage设为可点击状态
//                nextPage.setClickable(false);
        viewController.nextPage.setDisable(true);
        //
        viewController.showToastMessage("下一页");
        rtRetrieveCondition.setQueryKeyword(viewController.check_list_sn.getText().toString());
        int iCurrentPage = Integer.parseInt(viewController.currentPage.getText().toString());
        if (iCurrentPage < iTotalPage) {
            viewController.currentPage.setText(String.valueOf(iCurrentPage + 1)); // 页数 + 1
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
    }

    public void reprintSmallSheet_click() {
        /**
         * 下面为功能代码
         */
        if (retailTradeSelected != null) {
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(retailTradeSelected.getID());
            RetailTrade retailTrade1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            if (retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade1 == null) {
                log.error("retailTradePresenter.retrieve1ObjectSync失败,错误码=" + retailTradePresenter.getLastErrorCode());
                viewController.showToastMessage("找不到零售单");
            }

            try {
                viewController.printSmallSheet(retailTrade1);
            } catch (Exception e) {
                log.info("打印小票异常：" + e.getMessage());
            }
        } else {
            viewController.showToastMessage("请选择零售单");
        }
    }

    public void confirm_returnCommodity_click() {
        /**
         * 点击退货按钮之后应当将退货按钮更改为重打小票
         */
        viewController.confirm_returnCommodity.setVisible(false);
        viewController.reprintSmallSheet.setVisible(true);
        /**
         * 下面为功能代码
         */
        if (CheckRetailTrade()) { //检查零售单是否为退货单等
            System.out.println("终止掉了");
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                viewController.showLoadingDialog();
            }
        });
        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible()) {
            confirmReturnCommodity();
        } else {
            // 断网情况下，当且仅当纯现金支付，才可以退货。其它支付方式不可以退货
            if (retailTradeSelected.payViaCashOnly()) {
                confirmReturnCommodity();
            } else {
                showRetailTradeCommodity(retailTradeSelected);// 重新加载选中的零售单从表。注意查看列表有无残留其它零售单的商品
                viewController.returnAmount.setText("￥0.00");
                viewController.returnAmount.setVisible(false);
                viewController.confirm_returnCommodity.setVisible(false);
                Message message = new Message();
                message.what = 11;
                PlatForm.get().sendMessage(message);
            }
        }
    }

    public void deleteAll_click() {
        viewController.check_list_sn.setText("");
    }

    // TODO 是否还需要尚不清楚
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//        closeLoadingDialog(loadingDailog); // 查单时过快操作（转到其它页面），转圈圈一直不会消失。必须显式关闭。
//    }

    /**
     * --------------------------------------------------------eventbus事件-------------------------------------------------------------------------------------
     *
     * @param event
     */


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        log.info("#########################################################MainActivity onRetailTradeSQLiteEvent");
        if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
            event.onEvent(); //
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                retrieveNRetailTradeList = (List<RetailTrade>) event.getListMasterTable();

                int count = Integer.parseInt(retailTradeHttpEvent.getCount());
                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);//查询零售单需要totalPageIndex页才能查完

                viewController.closeLoadingDialog();
                Message message = new Message();
                message.what = 3;
                PlatForm.get().sendMessage(message);
            }
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done) {
                viewController.closeLoadingDialog();
            }

            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                viewController.closeLoadingDialog();
//                showToastMessage(event.getLastErrorMessage());
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_MainStage_CreateReturnRetailTrade) {
            System.out.println("该event在POSTING中已经处理：" + BaseEvent.EVENT_ID_MainStage_CreateReturnRetailTrade);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent1(RetailTradeSQLiteEvent event) throws InterruptedException {
        System.out.println("#########################################################POSTING");
        if (event.getId() == BaseEvent.EVENT_ID_MainStage_CreateReturnRetailTrade) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (!updateRetailTradeAggregation((RetailTrade) event.getBaseModel1())) {
                    //TODO 以后根据用户反应再作处理
                }
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
            log.info("该event在MAIN中已经处理：" + BaseEvent.EVENT_ID_MainStage_CreateReturnRetailTrade);
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
        if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
            event.onEvent();
            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_RetailTrade_RetrieveNOldRetailTrade && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (event.getListMasterTable() == null || event.getListMasterTable().size() == 0) {
                    Message message = new Message();
                    message.what = 4;
                    PlatForm.get().sendMessage(message);
                }
            }

            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
//                showToastMessage(event.getLastErrorMessage());
            }

        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseController.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseController.retailTradeAggregation);
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
        if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
            event.onEvent();
            log.info(event.getStatus());
            viewController.closeLoadingDialog();
            String msg = "";
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                if (event.getMessageBeforeEventPosted().length() > 0) {
                    viewController.closeLoadingDialog();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText(event.getMessageBeforeEventPosted());
                    ButtonType buttonTypeConfirm = new ButtonType("确定");
                    alert.getButtonTypes().setAll(buttonTypeConfirm);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeConfirm) {
                        // RecyclerView 刷新，初始化
                        dialogCheckListOrderRecyclerViewAdapter1 = new DialogCheckListOrderListCellAdapter();
                        viewController.check_list_order_rv.setCellFactory(new Callback() {
                            @Override
                            public Object call(Object e) {
                                return dialogCheckListOrderRecyclerViewAdapter1;
                            }
                        });
                        viewController.check_list_order_rv.setItems(null);
                        viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                        //
                        clearRetailTradeCommodityList();
                        //show 零售单
                        showRetailTrade(retrieveNRetailTradeList);
                    } else {
                        throw new RuntimeException("黑客行为");
                    }
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
            PlatForm.get().sendMessage(message);

        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_CheckListStage) {
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
        retailTradeAggregation.setID(BaseController.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(Constants.posID);
        retailTradeAggregation.setTradeNO(BaseController.retailTradeAggregation.getTradeNO());/*销售单数*/
        retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
        //发生变化的数据：
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前退货单的生成时间*/
        retailTradeAggregation.setReserveAmount(BaseController.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseController.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));/*现金收入*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseController.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));/*微信收入*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
        //单数不用更新

        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("创建退货单后，准备更新零售单汇总失败！");
            return false;
        }
        long lTimeOut = viewController.TIME_OUT;
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
            BaseController.retailTradeAggregation.setCashAmount(GeneralUtil.sub(BaseController.retailTradeAggregation.getCashAmount(), rt.getAmountCash()));
            BaseController.retailTradeAggregation.setWechatAmount(GeneralUtil.sub(BaseController.retailTradeAggregation.getWechatAmount(), rt.getAmountWeChat()));
            BaseController.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTradeAggregation.getWechatAmount()));
            BaseController.retailTradeAggregation.setWorkTimeEnd(date);
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
        retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_CheckListStage);
        retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_CheckListStage);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        //
        retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_CheckListStage);
        retailTradeAggregationSQLiteBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        //
        returnCommodityHttpEvent.setId(BaseEvent.EVENT_ID_CheckListStage);
        //
        wxPayHttpEvent.setId(BaseEvent.EVENT_ID_CheckListStage);
        wxPayHttpBO.setHttpEvent(wxPayHttpEvent);
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
        //
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
    }

    private void initUI() {
        viewController.currentPage.setText(BaseHttpBO.FIRST_PAGE_Index_Default);
        viewController.totalPage.setText("0");
        showRetailTrade(null); // 清空零售单列表
        viewController.nextPage.setDisable(true); // 不能再分页
        viewController.lastPage.setDisable(true); // 不能再分页
        viewController.tvQueryRetailTrade.setDisable(false); // 让收银员可以再点击搜索按钮
    }

    //联网查询零售单或者本地查询
    public List<RetailTrade> queryRetailTrade(RetailTrade retailTrade) {
        log.info("查询零售单的条件：" + retailTrade);
        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible()) { // 联网查询
            try {
                String msg = retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                if ("".equals(msg)) {
                    viewController.showLoadingDialog();
                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                    if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
                        log.info("查询旧零售单失败！！");
                        viewController.closeLoadingDialog();
                        ToastUtil.toast("网络故障，请重新登录", ToastUtil.LENGTH_SHORT);
                    }
                } else {
                    ToastUtil.toast(msg, ToastUtil.LENGTH_SHORT);
                }
            } catch (Exception e) {
                log.info("seekRetailTrade异常" + e.getMessage());
                e.printStackTrace();
            }
            return (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();

        } else if (bNetworkDisconnectedForTheFirstTime && GlobalController.getInstance().getSessionID() != null && !isSearchButtonClicked) { //如果是中途断网情况下点击上一页或下一页，提示错误信息，所以需要判断!isSearchButtonClicked
            ToastUtil.toast("网络故障，请稍后再试！", ToastUtil.LENGTH_SHORT);
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
                    ToastUtil.toast(msg, ToastUtil.LENGTH_SHORT);
                    initUI();
                    return null;
                }
            }
            // 先得到零售单总条数，进行分页
            // 当断网时点击搜索，显示的是第一页的数据
            String[] conditions = null;
            String[] conditionsForTotalPage = null;
            retailTrade.setSn(retailTrade.getQueryKeyword());
            try {
                String sqlForTotalPage = "where F_SaleDatetime BETWEEN %s and %s ";
                if (StringUtils.isEmpty(retailTrade.getSn())) { //true 为空,sn为空，查全部    //优化：1.if中有没有分支可以去掉  2.翻页isPageTurning 还原数据问题
                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd())};
                } else {
                    sqlForTotalPage += " and F_SN like %s";
                    conditionsForTotalPage = new String[]{retailTradePresenter.dateToStamp(retailTrade.getDatetimeStart()), retailTradePresenter.dateToStamp(retailTrade.getDatetimeEnd()), "%" + retailTrade.getSn() + "%"};
                }
                sqlForTotalPage += "order by F_SaleDatetime desc";
                retailTrade.setSql(sqlForTotalPage);
                retailTrade.setConditions(conditionsForTotalPage);
                //
                List<RetailTrade> totalPageByConditions = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
                if (totalPageByConditions.size() == 0) {
                    ToastUtil.toast("没有符合条件的零售单", ToastUtil.LENGTH_SHORT);
                    initUI();
                    return null;
                }
                //
                Integer count = totalPageByConditions.size();
                iTotalPage = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);
                viewController.totalPage.setText(String.valueOf(iTotalPage));
                if (Integer.parseInt(viewController.currentPage.getText().toString()) > iTotalPage) {
                    viewController.currentPage.setText(String.valueOf(iTotalPage));
                }

                // 查询本地的零售单
                int start = (Integer.parseInt(viewController.currentPage.getText()) - 1) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default);
                int end = Integer.parseInt(viewController.currentPage.getText()) * Integer.parseInt(BaseHttpBO.PAGE_SIZE_Default);
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
                    String sqlCommodity = "where F_TradeID = %s";
                    retailTradeCommodity.setSql(sqlCommodity);
                    retailTradeCommodity.setConditions(conditions);

                    List<RetailTradeCommodity> showRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity);
                    //将从表设置进主表
                    showRetailTrades.get(i).setListSlave1(showRetailTradeCommodityList);
                }
                // 显示
                viewController.totalPage.setText(String.valueOf(iTotalPage));
                viewController.currentPage.setText(viewController.currentPage.getText().toString()); // 展现当前页
                showRetailTrade(showRetailTrades); //将每次查询到的主从表进行展示
                showPagingButton();
                // 断网情况下也需要将该list赋值给该list
                retrieveNRetailTradeList = showRetailTrades;
                ToastUtil.toast("搜索本地零售单成功", ToastUtil.LENGTH_SHORT);
                return showRetailTrades; //...
            } catch (Exception e) {
                e.printStackTrace();
                initUI();
                return null;
            }
        }
    }

    private void showRetailTrade(final List<RetailTrade> list) {
        retrieveNRetailTradeList = list;
        viewController.check_list_order_rv.setItems(null);
        for (int i = 0; i < retrieveNRetailTradeList.size(); i++) {
            retrieveNRetailTradeList.get(i).setNumber(i);
        }
        viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
        viewController.check_list_order_rv.setCellFactory(new Callback() {
            @Override
            public Object call(Object e) {
                DialogCheckListOrderListCellAdapter dialogCheckListOrderListCellAdapter = new DialogCheckListOrderListCellAdapter();

                return dialogCheckListOrderListCellAdapter;
            }
        });
//        viewController.check_list_order_rv.setItems(FXCollections.observableList(list));
        // TODO
        isRetailTradeCommodityNumberEditable = true;

        viewController.check_list_order_rv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RetailTrade>() {
            // TODO 退了一个零售单后，在点击ListView的元素，会出现调用三次changed方法
            @Override
            public void changed(ObservableValue<? extends RetailTrade> observable, RetailTrade oldValue, RetailTrade newValue) {
                ReturnGoods_Number = 0;//将退货数量重设为0；
                viewController.tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");
                viewController.returnAmount.setText("￥0.00");
                //当查单列表被选中时，显示的按钮必须是重打小票
                viewController.confirm_returnCommodity.setVisible(false);
                viewController.confirm_returnCommodity.setManaged(false);
                viewController.reprintSmallSheet.setVisible(true);
                viewController.reprintSmallSheet.setManaged(true);
                if (newValue == null) {
                    return;
                }
                //1.选中零售单再点击的时候，不必执行打开退货页面的操作
                // 2.当退货页面不可见时，令退货页面可见
                if (!list.get(newValue.getNumber()).isSelect && !viewController.return_goods_pane.isVisible()) {
                    viewController.return_goods_pane.setVisible(true);
                    viewController.return_goods_pane.setManaged(true);
                }
                // 不能new一个ListView赋值给viewController.check_list_order_rv，会刷新不了viewController.check_list_order_rv
                viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                dAmountToReturnToCustomer = 0.000000d;/*初始化应退金额*/

                if (list.get(newValue.getNumber()).isSelect) {      //此处为如果已经选中，再点一次将把右侧商品页面置为空
                } else {    //如果点击的是另一个零售单，右侧商品页面将刷新为新点击的零售单的商品
                    positionOfRetailTradeSelected = newValue.getNumber();
                    retailTradeAfterConfirmReturn = (RetailTrade) list.get(newValue.getNumber()).clone();
                    retailTradeSelected = (RetailTrade) list.get(newValue.getNumber()).clone(); //记录当前选中的零售单
                    viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                    //未选中状态点击之后改为选中
                    list.get(newValue.getNumber()).isSelect = true;
                    if (oldValue != null) {
                        list.get(oldValue.getNumber()).isSelect = false;
                    }
//                    selectedRetailTrade = checkListRetailTradeList.get(position); // 选中的零售单，用于退货退款
                    viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                    List<RetailTradeCommodity> selectRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
                    if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                        log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
                        //  returnCommodity.setClickable(false);
                        viewController.showToastMessage("该单包含已删除商品，无法显示");
//                        check_list_commodity_rv.setAdapter(null);
                        viewController.check_list_commodity_rv.setItems(null);
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
                        viewController.checkListOriginalPrice.setText(GeneralUtil.formatToShow(dTotalOriginalPrice));
                        viewController.checkListReceivable.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmount()));//现在目前先放入零售单金额
                        viewController.checkListNetReceipts.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmountPaidIn()));
                        viewController.checkListDiscount.setText(GeneralUtil.div(GeneralUtil.mul(dShouldPay, 100.000000d), dTotalOriginalPrice, 2) + "%");
                        viewController.checkListChange.setText(GeneralUtil.formatToShow(retailTradeSelected.getAmountChange()));
                    } else {
                        viewController.checkListOriginalPrice.setText("0.00");
                        viewController.checkListReceivable.setText("0.00");
                        viewController.checkListNetReceipts.setText("0.00");
                        viewController.checkListDiscount.setText("0.00%");
                        viewController.checkListChange.setText("0.00");
                    }
                    viewController.returnAmount.setText("￥0.00");

                    //设置原价总金额，支付方式
                    if (retailTradeAfterConfirmReturn.getAmountCash() != 0) {
                        viewController.checkListPaymentMethod.setText("现金");
                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
                            viewController.checkListPaymentMethod.setText("现金、支付宝");
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                viewController.checkListPaymentMethod.setText("现金、支付宝、微信");
                            }
                        } else {
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                viewController.checkListPaymentMethod.setText("现金、微信");
                            }
                        }
                    } else {
                        if (retailTradeAfterConfirmReturn.getAmountAlipay() != 0) {
                            viewController.checkListPaymentMethod.setText("支付宝");
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                viewController.checkListPaymentMethod.setText("支付宝、微信");
                            }
                        } else {
                            if (retailTradeAfterConfirmReturn.getAmountWeChat() != 0) {
                                viewController.checkListPaymentMethod.setText("微信");
                            }
                        }
                    }
                    //展示零售单商品
                    showRetailTradeCommodity(retailTradeAfterConfirmReturn);
                }
            }
        });
    }

    private void showRetailTradeCommodity(RetailTrade retailTrade) {
        if (retailTradeSelected != null) {
            retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
            final List<RetailTradeCommodity> retailTradeCommodityListSelected = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
            log.info("零售单商品列表：" + retailTradeCommodityAfterConfirmReturn);
            //
            if (retailTradeSelected.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
                log.error("发现可疑零售单，没有商品数据，ID为：" + retailTradeSelected.getID());
                ToastUtil.toast("该单包含已删除商品，无法显示", ToastUtil.LENGTH_SHORT);
                // TODO
//            check_list_commodity_rv.setAdapter(null);
                return; // TODO
            }
            //
            for (int i = 0; i < retailTradeCommodityAfterConfirmReturn.size(); i++) {
                retailTradeCommodityAfterConfirmReturn.get(i).setNum(i + 1);
                retailTradeCommodityAfterConfirmReturn.get(i).setNumber(i);
//                retailTradeCommodityAfterConfirmReturn.get(i).setName("商品" + i);
                retailTradeCommodityAfterConfirmReturn.get(i).setName(retailTradeCommodityAfterConfirmReturn.get(i).getCommodityName());
            }
            viewController.check_list_commodity_rv.setItems(null);
//        viewController.check_list_commodity_rv.refresh();
//        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityListCellAdapter();
            viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
            viewController.check_list_commodity_rv.setCellFactory(new Callback() {
                @Override
                public Object call(Object e) {
//                return dialogCheckListCommodityRecyclerViewAdapter1;
                    dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityListCellAdapter();
                    dialogCheckListCommodityRecyclerViewAdapter1.setModifyCountInterface(new DialogCheckListCommodityListCellAdapter.ModifyCountInterface() {
                        @Override
                        public void doIncrease(int position, Label showCountView, Label total_money, MouseEvent event) {
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
                                    // TODO 长按减号、加号，每隔0.2秒减一件商品
//                                while (isOnLongClick) {
                                    try {
                                        Thread.sleep(200);
                                        Message message = new Message();
                                        message.obj = longClickData;
                                        message.what = 12;
                                        PlatForm.get().sendMessage(message);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                }
                                    super.run();
                                }
                            });
                            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                isOnLongClick = true;
                                plusThread.start();
                            }
                        }

                        @Override
                        public void doDecrease(int position, Label showCountView, Label total_money, MouseEvent event) {
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
//                                while (isOnLongClick) {
                                    try {
                                        Thread.sleep(200);
                                        Message message = new Message();
                                        message.obj = longClickData;
                                        message.what = 13;
                                        PlatForm.get().sendMessage(message);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                }
                                    super.run();
                                }
                            });
                            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                isOnLongClick = true;
                                miusThread.start();
                            }
                        }
                    });
//                dialogCheckListCommodityRecyclerViewAdapter1.setListener(new DialogCheckListCommodityListCellAdapter.OnClickListener() {
//                    @Override
//                    public void onLeftClick(RetailTradeCommodity newValue) {
//                        if (newValue != null) {
//                            log.info(newValue.getNumber());
//                            viewController.check_list_commodity_rv.setItems(null);
//                            viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
//                            if (retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect) {
//                                //选中状态点击之后改为未选中
//                                retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect = false;
//                                viewController.check_list_commodity_rv.setItems(null);
//                                viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
//                            } else {
//                                //未选中状态点击之后改为选中
//                                viewController.check_list_commodity_rv.setItems(null);
//                                retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect = true;
//
//                                commNumber = retailTradeCommodityListSelected.get(newValue.getNumber()).getNO();//零售单商品数量
//                                noCanReturn = retailTradeCommodityListSelected.get(newValue.getNumber()).getNOCanReturn();//商品最大可退货数量
//                                remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量
//
//                                //是否展示编辑数量的按钮
////                                if (isRetailTradeCommodityNumberEditable) {       //因UI界面改变，这里必定展示编辑数量按钮
////                                    retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = true;
////                                } else {
////                                    retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = false;
////                                }
//                                //设置可以编辑数量；
//                                retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isRetailTradeCommodityNumberEditable = true;
//                                viewController.check_list_commodity_rv.setItems(null);
//                                viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
//                            }
//                        }
//                    }
//                });
                    return dialogCheckListCommodityRecyclerViewAdapter1;
                }
            });
//        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter1);


//        viewController.check_list_commodity_rv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RetailTradeCommodity>() {
//            public void changed(ObservableValue<? extends RetailTradeCommodity> observable, RetailTradeCommodity oldValue1, RetailTradeCommodity newValue) {
//                if (newValue != null) {
////                dialogCheckListCommodityRecyclerViewAdapter1.setDefItem(position);
////                    viewController.return_goods_pane.setManaged(false);
////                    viewController.return_goods_pane.setVisible(false);
//                    log.info(newValue.getNumber());
////                dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
//                    viewController.check_list_commodity_rv.setItems(null);
//                    viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
//                    if (retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect) {
////                    //选中状态点击之后改为未选中
//                        retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect = false;
////                    dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
//                        viewController.check_list_commodity_rv.setItems(null);
//                        viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
//                    } else {
//                        //未选中状态点击之后改为选中
//                        viewController.check_list_commodity_rv.setItems(null);
//                        retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isSelect = true;
//
//                        commNumber = retailTradeCommodityListSelected.get(newValue.getNumber()).getNO();//零售单商品数量
//                        noCanReturn = retailTradeCommodityListSelected.get(newValue.getNumber()).getNOCanReturn();//商品最大可退货数量
//                        remainingQuantity = commNumber - noCanReturn;//当退货数量为最大可退货数量的时候，剩余的数量
//
//                        //是否展示编辑数量的按钮
////                    if (isRetailTradeCommodityNumberEditable) {       //因UI界面改变，这里必定展示编辑数量按钮
////                        retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = true;
////                    } else {
////                        retailTradeCommodityAfterConfirmReturn.get(position).isRetailTradeCommodityNumberEditable = false;
////                    }
//                        //设置可以编辑数量；
//                        retailTradeCommodityAfterConfirmReturn.get(newValue.getNumber()).isRetailTradeCommodityNumberEditable = true;
////                    dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
//                        viewController.check_list_commodity_rv.setItems(null);
//                        viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
////                }
//                    }
//                }
//            }
//        });
        }
    }

    /**
     * 控制查单页面分页按钮的状态
     */
    private void showPagingButton() {
        Integer iCurrentPage = Integer.parseInt(viewController.currentPage.getText().toString());
        viewController.nextPage.setDisable(false);
        viewController.lastPage.setDisable(false);
        viewController.tvQueryRetailTrade.setDisable(false);
        //
        if (iCurrentPage == 1) { // 第一页
            viewController.lastPage.setDisable(true);
        }
        if (iCurrentPage == iTotalPage) {// 最后一页
            viewController.nextPage.setDisable(true);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//            DisplayCustomKeyBoard(check_list_sn, true, false);
//        }
//        return false;
//    }

    /**
     * 长按退货界面商品的+-按钮时，传递给message handler的数据
     */
    public class LongClickData {
        public int bPosition;
        // TODO
//        public View bShowCountView;
//        public View bTotal_money;
        public Label bShowCountView;
        public Label bTotal_money;
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
//        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityRecyclerViewAdapter1(null, getActivity());
//        check_list_commodity_rv.setAdapter(dialogCheckListCommodityRecyclerViewAdapter1);
        dialogCheckListCommodityRecyclerViewAdapter1 = new DialogCheckListCommodityListCellAdapter();
        viewController.check_list_commodity_rv.setItems(null);
        retailTradeCommodityAfterConfirmReturn = new ArrayList<>();
        viewController.check_list_commodity_rv.setItems(FXCollections.observableList(retailTradeCommodityAfterConfirmReturn));
        viewController.check_list_commodity_rv.setCellFactory(new Callback() {
            @Override
            public Object call(Object e) {
                return dialogCheckListCommodityRecyclerViewAdapter1;
            }
        });
//        dialogCheckListCommodityRecyclerViewAdapter1.notifyDataSetChanged();
        // TODO 下面一行测试用
        // 重置零售单应收实收等数据
        viewController.checkListOriginalPrice.setText("0.00");
        viewController.checkListReceivable.setText("0.00");
        viewController.checkListNetReceipts.setText("0.00");
        viewController.checkListDiscount.setText("0.00%");
        viewController.checkListPaymentMethod.setText("");
//        // 重置退货按钮
//        leaveReturnCommodityMode();
        // 重置：之前想退但是没退的零售单,让点退货按钮时弹出“请选择要退货的零售单”
        retailTradeSelected = null;
    }

    // TODO
    /*
   查单UI里选中商品可以修改商品数量
    */
    public void doIncreaseInUI(List<RetailTradeCommodity> commodityList, int position, Label showCountView, Label total_money) {
        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
        int currentNumber = Integer.valueOf(((Label) showCountView).getText().toString());
        currentNumber++;
        ((Label) showCountView).setText(String.valueOf(currentNumber));
        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
        ((Label) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
        retailTradeCommodity.setNO(currentNumber);
        /*((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).setNO(currentNumber);*/
        //计算应退金额
        /*double newAmount = 0.000000d;*/
        /*for (int i = 0; i < commodityList.size(); i++) {
            newAmount = GeneralUtil.sum(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.mul(commodityList.get(i).getNO(), commodityList.get(i).getPriceReturn()))), newAmount);
        }
        double dAmountToReturnToCustomer = GeneralUtil.sub(retailTradeAfterConfirmReturn.getAmount(), newAmount);*/
        dAmountToReturnToCustomer = GeneralUtil.sub(dAmountToReturnToCustomer, unitPrice);

        viewController.returnAmount.setText("￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
        //当商品数量发生变化时，确认退货按钮出现，重打小票按钮消失
//        confirm_returnCommodity.setVisibility(View.VISIBLE);
//        reprintSmallSheet.setVisibility(View.GONE);
        viewController.confirm_returnCommodity.setVisible(true);
        viewController.confirm_returnCommodity.setManaged(true);
        viewController.reprintSmallSheet.setVisible(false);
        viewController.reprintSmallSheet.setManaged(false);
        //记录并显示退货数量
        ReturnGoods_Number--;
        viewController.tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");

        //当商品数量恢复到原来的数量时，确认退货按钮消失，重打小票按钮出现
        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
//            confirm_returnCommodity.setVisibility(View.GONE);
//            reprintSmallSheet.setVisibility(View.VISIBLE);
            viewController.confirm_returnCommodity.setVisible(false);
            viewController.confirm_returnCommodity.setManaged(false);
            viewController.reprintSmallSheet.setVisible(true);
            viewController.reprintSmallSheet.setManaged(true);
        }
    }

    // TODO
    public void doDecreaseInUI(List<RetailTradeCommodity> commodityList, int position, Label showCountView, Label total_money) {
        RetailTradeCommodity retailTradeCommodity = commodityList.get(position);
        int currentNumber = Integer.valueOf(((Label) showCountView).getText().toString());
        if (currentNumber == 0) {
            return;
        }
        currentNumber--;
        ((Label) showCountView).setText(String.valueOf(currentNumber));
        //实际上从表不可能为null，因为从表为null时，不会显示商品信息
        double unitPrice = (retailTradeAfterConfirmReturn.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeAfterConfirmReturn.getListSlave1()).get(position).getPriceReturn() : 0.000000d);
        ((Label) total_money).setText(GeneralUtil.formatToShow(unitPrice * currentNumber));
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
                || Math.abs(GeneralUtil.sub(dAmountToReturnToCustomer, retailTradeAfterConfirmReturn.getAmount()) - viewController.TORELANCE_ReturnWholeRetailTradeAllPaidByCash) < BaseModel.TOLERANCE) {
            dAmountToReturnToCustomer = retailTradeAfterConfirmReturn.getAmount();
        } else {
            dAmountToReturnToCustomer = GeneralUtil.sum(dAmountToReturnToCustomer, unitPrice);
        }
        viewController.returnAmount.setText("￥" + GeneralUtil.formatToShow(dAmountToReturnToCustomer));
        //当商品数量发生变化时，确认退货按钮出现，重打小票按钮消失
//        confirm_returnCommodity.setVisibility(View.VISIBLE);
//        reprintSmallSheet.setVisibility(View.GONE);
        viewController.confirm_returnCommodity.setVisible(true);
        viewController.confirm_returnCommodity.setManaged(true);
        viewController.reprintSmallSheet.setVisible(false);
        viewController.reprintSmallSheet.setManaged(true);
        //记录并显示退货数量
        ReturnGoods_Number++;
        viewController.tv_returngoodsNumber.setText(ReturnGoods_Number + " 件");

        //当商品数量恢复到原来的数量时，确认退货按钮消失，重打小票按钮出现
        if (GeneralUtil.formatToShow(dAmountToReturnToCustomer).equals("0.00")) {
//            confirm_returnCommodity.setVisibility(View.GONE);
            viewController.confirm_returnCommodity.setVisible(false);
            viewController.confirm_returnCommodity.setManaged(false);
//            reprintSmallSheet.setVisibility(View.VISIBLE);
            viewController.reprintSmallSheet.setVisible(true);
            viewController.reprintSmallSheet.setManaged(true);
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
            viewController.returnAmount.setText("￥0.00");
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
                    viewController.closeLoadingDialog();
                    return;
                }
                //
                retailTradeCommodityAfterConfirmReturn = (List<RetailTradeCommodity>) retailTradeSelected.getListSlave1();
                Integer tmpRowID = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
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
                        PlatForm.get().sendMessage(message);
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
                    PlatForm.get().sendMessage(message);
                    return;
                }

                retailTradeAfterConfirmReturn.setAmount(dAmountToReturnToCustomer);
                retailTradeAfterConfirmReturn.setSourceID(Integer.valueOf(String.valueOf(retailTradeAfterConfirmReturn.getID())));
                Integer returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
                retailTradeAfterConfirmReturn.setID(returnCommRetailTradeID);
                retailTradeAfterConfirmReturn.setLocalSN((int) returnCommRetailTradeID);
                retailTradeAfterConfirmReturn.setPos_ID(Constants.posID);
                retailTradeAfterConfirmReturn.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                retailTradeAfterConfirmReturn.setStaffID(BaseController.retailTradeAggregation.getStaffID());
                retailTradeAfterConfirmReturn.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                retailTradeAfterConfirmReturn.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                // 退货单需要知道零售时的支付宝支付金额和微信支付金额
                retailTradeAfterConfirmReturn.setSaleAmountAlipay(GeneralUtil.round(retailTradeSelected.getAmountAlipay(), 2));
                retailTradeAfterConfirmReturn.setSaleAmountWeChat(GeneralUtil.round(retailTradeSelected.getAmountWeChat(), 2));
                retailTradeAfterConfirmReturn.setSn(retailTradeSelected.getSn() + "_1"); // 退货单sn后面添加_1
                //
                // TODO
                new Thread(new Runnable() {
                    //                TaskScheduler.execute(new Runnable() {
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
                }).start();
            } else {
                Message message = new Message();
                message.what = 8;
                PlatForm.get().sendMessage(message);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Message message = new Message();
            message.what = 10;
            PlatForm.get().sendMessage(message);
        }
    }

    private boolean CheckRetailTrade() {
        log.info("开始退货");
        /**
         * 退货之前先检查
         */
        if (retailTradeSelected == null) {
//            showToastMessage("请选择要退货的零售单");
            ToastUtil.toast("请选择要退货的零售单", ToastUtil.LENGTH_SHORT);
            return true;
        }
        // 判断该零售单是否超过一年
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        c.add(Calendar.YEAR, -1);
        Date date = c.getTime();
        if (date.getTime() > retailTradeSelected.getSaleDatetime().getTime()) {
            viewController.showToastMessage("该零售单已经超过一年，不能退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        // 判断该零售单创建时间是否超过120s,没有120s则不让退货
        if (new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime() - retailTradeSelected.getSaleDatetime().getTime() < MIN_Time_InSecond_ReturnRetailTrade * 1000) {
            viewController.showToastMessage("订单结算中，请稍后退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        // 检查是否已经创建退货单
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSourceID(Integer.valueOf(String.valueOf(retailTradeSelected.getID())));
        List<RetailTrade> returnRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned, retailTrade);
        if (returnRetailTradeList != null && returnRetailTradeList.size() > 0) {
            viewController.showToastMessage("该零售单已经退货过了，不能重复退货！");
            setReturnCommodityButtonStyle();
            return true;
        }
        if (retailTradeAfterConfirmReturn != null) {
            if (retailTradeAfterConfirmReturn.getSourceID() != -1) { //TODO hardcode
                viewController.showToastMessage("该零售单为退货单，不允许进行退货操作！");
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
        long lTimeOut = viewController.TIME_OUT;

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

    // TODO
    private boolean createReturnRetailTrade(StringBuilder sbMsg) throws InterruptedException {
        long lTimeOut = viewController.TIME_OUT;
//        RetailTradeSQLiteEvent e = new RetailTradeSQLiteEvent();
//        RetailTradeSQLiteBO bo = new RetailTradeSQLiteBO(e, null);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        RetailTradeSQLiteEvent e = (RetailTradeSQLiteEvent) context.getBean("retailTradeSQLiteEvent");
        RetailTradeSQLiteBO bo = (RetailTradeSQLiteBO) context.getBean("retailTradeSQLiteBO");
        bo.setSqLiteEvent(e);
        e.setSqliteBO(bo);
        e.setId(BaseEvent.EVENT_ID_MainStage_CreateReturnRetailTrade);
        e.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        e.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        e.setMessageBeforeEventPosted(sbMsg.toString());
        System.out.println("xxxxxxxxxxxxxxx" + retailTradeAfterConfirmReturn.getSourceID());
        if (!bo.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAfterConfirmReturn)) {
            log.error("创建退货单失败！");// TODO
            viewController.closeLoadingDialog();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("退货单创建超时！请联系客服。\r\n" + sbMsg.toString());
            ButtonType buttonTypeConfirm = new ButtonType("确定");
            alert.getButtonTypes().setAll(buttonTypeConfirm);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeConfirm) {
            } else {
                throw new RuntimeException("黑客行为");
            }
            return false;
        }
        while (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (e.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            log.error("钱已经退给顾客，但退货单没有创建成功！必须给SQLite插入这张单：retailTradeAfterConfirmReturn=" + retailTradeAfterConfirmReturn);
            viewController.showToastMessage("退货单创建超时！请找客服支持");
            viewController.closeLoadingDialog();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("退货单创建超时！请找客服支持。\r\n" + sbMsg.toString());
            ButtonType buttonTypeConfirm = new ButtonType("确定");
            alert.getButtonTypes().setAll(buttonTypeConfirm);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeConfirm) {
            } else {
                throw new RuntimeException("黑客行为");
            }
            return false;
        }
        return true;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 3://点击搜索按钮查询零售单，服务器返回的数据需要在UI进行展示
                log.info("查询到的零售单有：" + retrieveNRetailTradeList);
                //计算返回的零售单需要分几页进行查看
                if (retrieveNRetailTradeList.size() > 0) {
                    viewController.totalPage.setText(String.valueOf(iTotalPage)); //...
                    showRetailTrade(retrieveNRetailTradeList);
                    showPagingButton();
                }
                System.out.println("xxxxxxxxxxxx3");
                break;
            case 4:
                viewController.closeLoadingDialog();
                viewController.showToastMessage("没有符合条件的零售单");
                retrieveNRetailTradeList.clear();
                viewController.check_list_order_rv.setItems(null);
                viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
                viewController.check_list_order_rv.setCellFactory(new Callback() {
                    @Override
                    public Object call(Object e) {
                        return new DialogCheckListOrderListCellAdapter();
                    }
                });
                viewController.check_list_order_rv.setItems(FXCollections.observableList(retrieveNRetailTradeList));
//                dialogCheckListOrderRecyclerViewAdapter1.notifyDataSetChanged();

                isRetailTradeCommodityNumberEditable = false;
                viewController.currentPage.setText("0");
                viewController.totalPage.setText("0");
//                tvQueryRetailTrade.setClickable(true);
                viewController.tvQueryRetailTrade.setDisable(false);
                System.out.println("xxxxxxxxxxxx4");
                break;
            case 6:
                if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    viewController.showToastMessage("退货失败，请重新操作！");
                } else {
                    viewController.showToastMessage("退货成功，接下来可以进行退款操作");
                }
//                confirm_returnCommodity.setVisibility(View.GONE);
//                returnAmount.setVisibility(View.GONE);
                viewController.returnAmount.setVisible(false);
                viewController.returnAmount.setManaged(false);
                viewController.confirm_returnCommodity.setVisible(false);
                viewController.confirm_returnCommodity.setManaged(false);
                returnRetailTrade = null;
                break;
            case 7:
                String message = (String) msg.obj;
                if (message.length() > 0) {
                    viewController.showToastMessage(message);
                }
                // 改变退货按钮的状态
                setReturnCommodityButtonStyle();
                break;
            case 8:
                viewController.closeLoadingDialog();
                viewController.showToastMessage("还未选择退货商品和退货数量");
                // 改变退货按钮的状态
                setReturnCommodityButtonStyle();
                break;
            case 9:
                viewController.closeLoadingDialog();
                viewController.showToastMessage("本次的退货数量大于该商品的可退货数量");
                // 改变退货按钮的状态
                setReturnCommodityButtonStyle();
                break;
            case 10:
                viewController.closeLoadingDialog();
                viewController.showToastMessage("退货单创建失败！");
                // 改变退货按钮的状态
                setReturnCommodityButtonStyle();
                break;
            case 11:
                viewController.closeLoadingDialog(); // 网络故障，不能进行微信退款
                viewController.showToastMessage("网络故障，请稍后再试！");
                // 改变退货按钮的状态
                setReturnCommodityButtonStyle();
                break;
            case 12:
                LongClickData longClickDataIncrease = (LongClickData) msg.obj;
                if (Integer.valueOf(((Label) longClickDataIncrease.bShowCountView).getText().toString()) < commNumber) {
                    doIncreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataIncrease.bPosition, longClickDataIncrease.bShowCountView, longClickDataIncrease.bTotal_money);
                } else if (Integer.valueOf(((Label) longClickDataIncrease.bShowCountView).getText().toString()) > commNumber) {
//                    ((Label) longClickDataIncrease.bShowCountView).setText(String.valueOf(commNumber));
                    viewController.showToastMessage("输入的数量不能大于原商品数量");
                }
                break;
            case 13:
                isOnLongClick = false;
                LongClickData longClickDataDecrease = (LongClickData) msg.obj;
                if (Integer.valueOf(((Label) longClickDataDecrease.bShowCountView).getText().toString()) > remainingQuantity) {
                    doDecreaseInUI(retailTradeCommodityAfterConfirmReturn, longClickDataDecrease.bPosition, longClickDataDecrease.bShowCountView, longClickDataDecrease.bTotal_money);
                }
                break;
            default:
                break;
        }
    }

}

