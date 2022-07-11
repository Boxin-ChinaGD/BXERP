package wpos.allController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.model.Message;
import wpos.utils.PlatForm;
import wpos.adapter.RetrieveCommodityInventoryListCellAdapter;
import wpos.allEnum.ThreadMode;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.ScanGun;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.BarcodesPresenter;
import wpos.presenter.CommodityCategoryPresenter;
import wpos.presenter.CommodityPresenter;
import wpos.presenter.PackageUnitPresenter;
import wpos.utils.StringUtils;
import wpos.utils.ToastUtil;

import javax.annotation.Resource;
import java.util.*;

import static wpos.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;
import static wpos.bo.BaseSQLiteBO.CASE_Category_RetrieveNByConditions;

@Component("retrieveCommodityInventoryViewController")
public class RetrieveCommodityInventoryViewController implements PlatFormHandlerMessage {

    private Logger log = Logger.getLogger(this.getClass());
    private AllFragmentViewController viewController;

    @Resource
    private CommodityPresenter commodityPresenter;
    @Resource
    private CommoditySQLiteBO commoditySQLiteBO;
    @Resource
    private CommodityHttpBO commodityHttpBO;
    @Resource
    private CommoditySQLiteEvent commoditySQLiteEvent;
    @Resource
    private CommodityHttpEvent commodityHttpEvent;
    //
    @Resource
    private CommodityCategoryPresenter commodityCategoryPresenter;
    @Resource
    private CommodityCategorySQLiteBO commodityCategorySQLiteBO;
    @Resource
    private CommodityCategoryHttpBO commodityCategoryHttpBO;
    @Resource
    private CommodityCategorySQLiteEvent commodityCategorySQLiteEvent;
    @Resource
    private CommodityCategoryHttpEvent commodityCategoryHttpEvent;
    //
    @Resource
    private PackageUnitPresenter packageUnitPresenter;
    @Resource
    private PackageUnitSQLiteBO packageUnitSQLiteBO;
    @Resource
    private PackageUnitHttpBO packageUnitHttpBO;
    @Resource
    private PackageUnitSQLiteEvent packageUnitSQLiteEvent;
    @Resource
    private PackageUnitHttpEvent packageUnitHttpEvent;
    //
    @Resource
    private BarcodesPresenter barcodesPresenter;
    @Resource
    private BarcodesSQLiteBO barcodesSQLiteBO;
    @Resource
    private BarcodesSQLiteEvent barcodesSQLiteEvent;
    @Resource
    private BarcodesHttpBO barcodesHttpBO;
    @Resource
    private BarcodesHttpEvent barcodesHttpEvent;

    private RetrieveCommodityInventoryListCellAdapter adapter = null;
    private RetrieveCommodityInventoryListCellAdapter adapterQueryByBarcode = null;
    private List<Commodity> commodityList = new ArrayList<Commodity>();//当点击全部同步按钮的时候需要进行初始化
    /**
     * 库存页面按条形码查询，有一品多码的情况，所以需要去重
     */
    private Set<Integer> commodityIdSet = new HashSet<>();
    private int count;//call 普通action，返回的商品总数
    /**
     * 当前要同步的第currentlySyncCommodityPageIndex页的商品，从1算起。=1时，表明当前正在同步1页的商品数据
     */
    private int currentlySyncCommodityPageIndex = 1;
    /**
     * 当前要同步的第currentlySyncBarcodesPageIndex页的Barcodes，从1算起。=1时，表明当前正在同步1页的Barcodes数据
     */
    private int currentlySyncBarcodesPageIndex = 1;
    /**
     * 上拉加载更多的次数,当点击全部同步按钮的时候需要进行初始化为0。=1时，表明第1页已经显示，准备加载第2页。再滑动多一次，则再加1。如此类推
     */
    private int loadingMoreTimes;
    private String searchBarcodes;//在搜索框中输入的条形码
    private boolean isSendMessage;//handler是否发送message
    private ScanGun scanGun = null;
    public static final int PAGE_SIZE_UI = 15;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
            } else {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            event.onEvent();
            switch (event.getEventTypeSQLite()) {
                case ESET_Commodity_RefreshByServerDataAsyncC_Done:
                    if (!"".equals(commodityHttpEvent.getCount())) {
                        Commodity commodity = new Commodity();
                        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
                        count = Integer.parseInt(commodityHttpEvent.getCount());
                        log.info("商品总数为：" + count);
                        int totalPageIndex = count % Integer.valueOf(commodity.getPageSize()) != 0 ? count / Integer.valueOf(commodity.getPageSize()) + 1 : count / Integer.valueOf(commodity.getPageSize());//查询商品需要totalPageIndex页才能查完
                        if (currentlySyncCommodityPageIndex < totalPageIndex) {
                            log.info("运行同步");
                            commodity.setPageIndex(String.valueOf(++currentlySyncCommodityPageIndex));
                            if (commodity.getPageIndex() == String.valueOf(totalPageIndex)) {
                                commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_END);
                            }

                            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                                log.info("Commodity的全部同步失败！");
                                viewController.closeLoadingDialog();
                                ToastUtil.toast("网络故障，无法进行同步！", ToastUtil.LENGTH_SHORT);
                            }
                        } else {
                            // 同步完商品后，重置currentlySyncCommodityPageIndex
                            currentlySyncCommodityPageIndex = 1;
                            //同步Barcode
                            retrieveNCBarcode(BaseHttpBO.FIRST_PAGE_Index_Default);
                        }
                    }
                    break;
                case ESET_Commodity_UpdateNAsync:
                    System.out.println("event.getHttpBO().getHttpEvent().getListMasterTable():" + event.getHttpBO().getHttpEvent().getListMasterTable());
                    if (event.getHttpBO().getHttpEvent().getListMasterTable().size() > 0) {//当返回有commodity的时候才执行下面的显示内容
                        System.out.println("commoditySQLiteEvent.getListMasterTable():" + commoditySQLiteEvent.getListMasterTable());
                        if (commoditySQLiteEvent.getListMasterTable().size() > 0) {
                            List<Commodity> commList = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
                            System.out.println("commList:" + commList);
                            for (int i = 0; i < commList.size(); i++) {
                                //
                                Commodity commodity = enrichCommodityToShowOnUI(commList.get(i));
                                commodityList.add(commodity);
                            }
                            viewController.closeLoadingDialog();
                            if (commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                ToastUtil.toast(commoditySQLiteEvent.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                            }
                        } else { // 如果本地没有返回数据，代表更新本地商品时失败，可能是本地没有同步到该商品
                            viewController.closeLoadingDialog();
                            ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                        }

                        System.out.println("执行了这里" + commodityList.size());
                        Message message = new Message();
                        message.what = 2;
                        PlatForm.get().sendMessage(message);
                    } else if(commodityList.size() > 0){
                        Platform.runLater(()->{
                            viewController.showToastMessage("没有更多了");
                            viewController.closeLoadingDialog();
                            //
                            dismissLoadingView();
                            adapter.setLoadCompleted();
                        });
                    }
                    else {//若没有值返回，这显示没有值返回的UI
                        viewController.closeLoadingDialog();
                        Message message = new Message();
                        message.what = 3;
                        PlatForm.get().sendMessage(message);
                    }
                    break;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    private Commodity enrichCommodityToShowOnUI(Commodity comm) {
        Barcodes barcodes = new Barcodes();
        if (StringUtils.isTrimEmpty(comm.getBarcode())) {
            // 断网情况下，库存查询无法从服务器上查询到数据，所以需要从本地查询
            barcodes.setSql("where F_CommodityID = %s");
            barcodes.setConditions(new String[]{String.valueOf(comm.getID())});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
            if (barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                return null;
            }
            barcodes.setBarcode(barcodesList.get(0).getBarcode());
        } else {
            barcodes.setBarcode(comm.getBarcode());
        }
        //
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setID(comm.getCategoryID());
        commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(comm.getPackageUnitID());
        packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        //
        Commodity commodity = new Commodity();
        commodity.setBarcode(barcodes.getBarcode());
        commodity.setName(comm.getName());
        commodity.setCategory(commodityCategory.getName());
        commodity.setSpecification(comm.getSpecification());
        commodity.setListSlave2(comm.getListSlave2());
        commodity.setNO(comm.getNO());
        commodity.setPackageUnit(packageUnit.getName());
        return commodity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
            } else {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            event.onEvent();

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                //同步PackgeUnit
                retrieveNCPackageUnit();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
            } else {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            event.onEvent();

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                if (isSendMessage) {
                    viewController.closeLoadingDialog();
                    Message message = new Message();
                    message.what = 1;
                    PlatForm.get().sendMessage(message);
                    isSendMessage = false;
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
            } else {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            event.onEvent();
            if (barcodesHttpEvent.getCount() != null && !"".equals(barcodesHttpEvent.getCount())) {
                Barcodes barcodes = new Barcodes();
                barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
                count = Integer.parseInt(barcodesHttpEvent.getCount());
                barcodesHttpEvent.setCount(""); // TODO
                log.info("Barcodes的总数为: " + count);
                int totalPageIndex = count % Integer.valueOf(barcodes.getPageSize()) != 0 ? count / Integer.valueOf(barcodes.getPageSize()) + 1 : count / Integer.valueOf(barcodes.getPageSize());//查询条形码需要totalPageIndex页才能查完
                if (currentlySyncBarcodesPageIndex < totalPageIndex) {
                    retrieveNCBarcode(String.valueOf(++currentlySyncBarcodesPageIndex));
                } else {
                    //同步商品类别
                    retrieveNCommodityCategory();
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public RetrieveCommodityInventoryViewController() {

    }

    //库存页面搜索按钮。不管是否联网，都只从本地中搜索
    public void searchRci_click(int pageIndex, int pageSize) {
        commodityIdSet.clear();
        loadingMoreTimes = 0;
        viewController.commodityInfoListView.scrollTo(0);
        searchBarcodes = viewController.rci_condition_input.getText();
        if (searchBarcodes.trim().length() > BaseController.FUZZY_QUERY_LENGTH) {
            Commodity comm = new Commodity();
            comm.setBarcode(searchBarcodes);
            comm.setPageIndex(String.valueOf(pageIndex));
            comm.setPageSize(String.valueOf(pageSize));
            if(pageIndex == 1) {
                commodityList.clear();
                viewController.commodityInfoListView.setItems(null);
            }
//            if (true) {
//            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, comm)) {
//                if (false) { // 判断条形码搜索不能输入中文
////                if (commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) { // 判断条形码搜索不能输入中文
//                    ToastUtil.toast(commoditySQLiteEvent.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
//                    // 判断完后需要重置错误码和错误信息
//                    commoditySQLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                    commoditySQLiteEvent.setLastErrorMessage("");
//                }
//                else {
                    // 断网情况下，根据条形码从本地模糊搜索商品
                    if(adapter != null) {
                        adapter.setOnLoadMoreListener(null);
                        adapter = null;
                    }
                    viewController.commodityInfoListView.setCellFactory(null);
                    // 根据搜索条形码模糊查询到条形码ID
                    Barcodes barcodes = new Barcodes();
                    barcodes.setSql("where F_Barcode LIKE %s");
                    barcodes.setConditions(new String[]{"'%" + searchBarcodes + "%'"});
                    barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                    List<Barcodes> barcodesListAll = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
                    //
                    barcodes.setSql("where F_Barcode LIKE '%s' limit %s, %s");
                    barcodes.setConditions(new String[]{"%" + searchBarcodes + "%", String.valueOf(loadingMoreTimes), String.valueOf(PAGE_SIZE_UI)});
                    barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                    List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
                    // 根据条形码ID搜索相关的商品
                    for (Barcodes b : barcodesList) {
                        Commodity commodity = new Commodity();
                        commodity.setID(b.getCommodityID());
                        Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                        if (retrieveCommodityByID == null) { // 如果查不到相应的商品，就跳过此次循环
                            continue;
                        }
                        if(!commodityIdSet.contains(retrieveCommodityByID.getID())) {
                            Commodity c = enrichCommodityToShowOnUI(retrieveCommodityByID);
                            commodityList.add(c);
                            commodityIdSet.add(retrieveCommodityByID.getID());
                        }
                    }
                    RetrieveCommodityInventoryListCellAdapter.lastIndex = PAGE_SIZE_UI;
                    loadingMoreTimes = 0;
                    //
                    viewController.commodityInfoListView.setItems(null);
                    viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
                    viewController.commodityInfoListView.setCellFactory(e -> {
                        adapterQueryByBarcode = new RetrieveCommodityInventoryListCellAdapter();
                        adapterQueryByBarcode.setOnLoadMoreListener(() -> {
                            System.out.println("现已加载的数据量：" + commodityList.size() + "总数据量是：" + barcodesListAll.size());
                            if (loadingMoreTimes < (barcodesListAll.size() % PAGE_SIZE_UI == 0 ? barcodesListAll.size() / PAGE_SIZE_UI : barcodesListAll.size() / PAGE_SIZE_UI + 1) ) {
//                            if (commodityList.size() < barcodesListAll.size()) {
                                loadingMoreTimes++;
                                System.out.println("执行了该执行的" + barcodesListAll.size());
                                loadMoreByBarcodesCondition();
                            } else {
                                adapterQueryByBarcode.setLoadCompleted();
                                Platform.runLater(()-> {
                                    ToastUtil.toast("没有更多的商品");
                                });
                            }
                        });
                        return adapterQueryByBarcode;
                    });
//                }
                //

                Message message = new Message();
                if (commodityList.size() == 0) {
                    message.what = 3;
                } else {
                    message.what = 2;
                }
                PlatForm.get().sendMessage(message);
//            }
//            else {
//                Platform.runLater(()->{
//                    viewController.showLoadingDialog();
//                });
//            }
        } else if (searchBarcodes.trim().length() > 0) {
            viewController.query_fail_view.setVisible(true);
            viewController.query_fail_view.setManaged(true);
            viewController.tv_failtips.setText("由于输入的条形码不够完整，无法查询到库存信息...");
            viewController.commodityInfoListView.setVisible(false);
            viewController.commodityInfoListView.setManaged(false);
        } else {
            ToastUtil.toast("请输入商品条形码...", ToastUtil.LENGTH_SHORT);
        }
    }

    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        init();
    }

    public void init() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
        commodityList = new ArrayList<Commodity>();
        //第二次进入该页面时，需重置基础数据
        if (adapter != null) {
            adapter.setLastIndex(PAGE_SIZE_UI);
        }
        initEventAndBo();
        initData();

        // 设置key事件最大间隔，默认20ms，部分低端扫码枪效率低
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(scanResult -> {
            log.info("扫码枪扫描到的数据：" + scanResult);
            viewController.rci_condition_input.setText(scanResult);
            // 触发搜索点击事件
            Message message = new Message();
            message.what = 5;
            PlatForm.get().sendMessage(message);
        });

        //监听所有的控件的键盘点击事件，传递给scanGun
        initScanGunListener(viewController.rci_Pane);

        viewController.rci_condition_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("".equals(viewController.rci_condition_input.getText()) || null == viewController.rci_condition_input.getText()) {
                //删除按钮不显示
                viewController.rci_search1.setVisible(false);
            } else if (!"".equals(viewController.rci_condition_input.getText()) && null != viewController.rci_condition_input.getText()) {
                viewController.rci_search1.setVisible(true);
            }
            //显示editText的全部删除按钮
        });

        //请求焦点，以使扫码枪能使用
        viewController.commodityInfoListView.requestFocus();
    }

    //注册监听事件
    private void initScanGunListener(Pane pane) {
        for (Node rci_paneChild : pane.getChildren()) {
            if (rci_paneChild instanceof Pane) {
                initScanGunListener((Pane) rci_paneChild);
            } else {
                rci_paneChild.setOnKeyPressed(event -> scanGun.isMaybeScanning(event));
            }
        }
    }

    public void rci_condition_input_click() {
        //todo 展示浮动键盘
    }

    private void initEventAndBo() {
        commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        commodityHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        //
//        commodityCategoryPresenter = GlobalController.getInstance().getCommodityCategoryPresenter();
        commodityCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        commodityCategoryHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        commodityCategorySQLiteBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategorySQLiteBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategoryHttpBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategoryHttpBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
//        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        packageUnitSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        packageUnitHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        packageUnitSQLiteBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitSQLiteBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitHttpBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitHttpBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
        //
//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        barcodesSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        barcodesHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage);
        barcodesSQLiteBO.setHttpEvent(barcodesHttpEvent);
        barcodesSQLiteBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesHttpBO.setHttpEvent(barcodesHttpEvent);
        barcodesHttpBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
    }

    /**
     * 初始化数据，令数据在ListView中显示。进入本页面或点击deleteAll按钮时调用
     */
    public void initData() {
        commodityIdSet.clear();
        viewController.commodityInfoListView.scrollTo(0);
        RetrieveCommodityInventoryListCellAdapter.mIsLoading = false;
        // 防止切换菜单，加载中
        dismissLoadingView();
        if(adapterQueryByBarcode != null) {
            adapterQueryByBarcode.setOnLoadMoreListener(null);
            adapterQueryByBarcode = null;
            viewController.commodityInfoListView.setItems(null);
        }
        final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseHttpBO.INVALID_CASE_ID, null)).size();

        List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage("T_Commodity", 1, PAGE_SIZE_UI);

        // 清空原有商品列表
        commodityList.clear();
        // 重置加载次数（解决切换左侧菜单，不能继续加载的问题）
        loadingMoreTimes = 0;
        initCommodityList(allInfoCommList);
        viewController.commodityInfoListView.setItems(null);
        viewController.commodityInfoListView.setCellFactory(null);
        viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
        viewController.commodityInfoListView.scrollTo(commodityList.size() - PAGE_SIZE_UI);
        viewController.commodityInfoListView.setCellFactory(e -> {
            adapter = new RetrieveCommodityInventoryListCellAdapter();
            adapter.setOnLoadMoreListener(() -> {
                System.out.println("现已加载的数据量：" + commodityList.size() + "总数据量是：" + totalCommodityNumber);
                if (commodityList.size() < totalCommodityNumber) {
                    loadingMoreTimes++;
                    System.out.println("执行了该执行的" + totalCommodityNumber);
                    loadMore();
                } else {
                    adapter.setLoadCompleted();
                    ToastUtil.toast("没有更多的商品");
                }
            });
            return adapter;
        });
    }

    private void loadMore() {
        showLoadingView();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage("T_Commodity", loadingMoreTimes + 1, PAGE_SIZE_UI);
                initCommodityList(allInfoCommList);

                Message message = new Message();
                message.what = 4;
                PlatForm.get().sendMessage(message);
            }
        }.start();
    }

    /**
     * 库存页面，根据条形码查询后的滑动加载
     */
    private void loadMoreByBarcodesCondition() {
        showLoadingView();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Barcodes barcodes = new Barcodes();
                barcodes.setSql("where F_Barcode LIKE '%s' limit %s, %s");
                barcodes.setConditions(new String[]{"%" + searchBarcodes + "%", String.valueOf(loadingMoreTimes * PAGE_SIZE_UI), String.valueOf(PAGE_SIZE_UI)});
                barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);

                // 根据条形码ID搜索相关的商品
                for (Barcodes b : barcodesList) {
                    Commodity commodity = new Commodity();
                    commodity.setID(b.getCommodityID());
                    Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                    if (retrieveCommodityByID == null) { // 如果查不到相应的商品，就跳过此次循环
                        continue;
                    }
                    if(!commodityIdSet.contains(retrieveCommodityByID.getID())) {
                        Commodity c = enrichCommodityToShowOnUI(retrieveCommodityByID);
                        commodityList.add(c);
                        commodityIdSet.add(retrieveCommodityByID.getID());
                    }
                }
                Message message = new Message();
                message.what = 2;
                PlatForm.get().sendMessage(message);
            }
        }.start();
    }

    private void initCommodityList(List<Commodity> list) {
        for (int i = 0; i < list.size(); i++) {
            Commodity commodity = new Commodity();
            commodity.setID(list.get(i).getID());
            //
            Barcodes barcodes = new Barcodes();
            barcodes.setSql("where F_CommodityID = %s");
            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);

            CommodityCategory commodityCategory = new CommodityCategory();
            commodityCategory.setSql("where F_ID = %s");
            commodityCategory.setConditions(new String[]{String.valueOf(list.get(i).getCategoryID())});
            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(CASE_Category_RetrieveNByConditions, commodityCategory);
            //
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setID(list.get(i).getPackageUnitID());
            packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseHttpBO.INVALID_CASE_ID, packageUnit);
            //
            if (barcodesList != null) {
                if (barcodesList.size() > 0) {
                    commodity.setBarcode(barcodesList.get(0).getBarcode());
                }
            }
            commodity.setName(list.get(i).getName());
            if (commodityCategoryList != null && commodityCategoryList.size() > 0) {
                commodity.setCategory(commodityCategoryList.get(0).getName());
            }
            commodity.setSpecification(list.get(i).getSpecification());
            commodity.setNO(list.get(i).getNO());
            if (packageUnit != null) {
                commodity.setPackageUnit(packageUnit.getName());
            }
            commodity.setListSlave2(list.get(i).getListSlave2());
            commodityList.add(commodity);
        }
        //设置每个commodity属于列表中第几个数据，用来滑动加载数据时做判断，判断当前是否为最后一个数据；
        for (int i = 0; i < commodityList.size(); i++) {
            commodityList.get(i).setIndex(i + 1);//数据计数从1开始
        }
    }

    // TODO 待研究
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        condition_input.setText("");
//        unbinder.unbind();
//    }

    public void rci_Sync() {
        isSendMessage = true;
        loadingMoreTimes = 0;
        try {
            //同步商品
            Commodity commodity = new Commodity();
            commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
//                    commodityRunTimes = 0;
            commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);
            commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                log.info("Commodity的全部同步失败！");
                ToastUtil.toast("网络不可用，无法进行同步！", ToastUtil.LENGTH_SHORT);
            } else {
                viewController.showLoadingDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("同步失败！，异常信息为：" + e.getMessage());
        }
    }

    public void clear_all_click() {
        loadingMoreTimes = 0;
        viewController.rci_condition_input.setText("");
        viewController.query_fail_view.setVisible(false);
        viewController.query_fail_view.setManaged(false);
        viewController.commodityInfoListView.setVisible(true);
        viewController.commodityInfoListView.setManaged(true);
        commodityList.clear();
        if(adapter != null) {
            adapter.setOnLoadMoreListener(null);
            adapter = null;
        }
        viewController.commodityInfoListView.setItems(null);
        viewController.commodityInfoListView.setCellFactory(null);
        RetrieveCommodityInventoryListCellAdapter.lastIndex = PAGE_SIZE_UI;
        initData();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                commodityList.clear();
                final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null)).size();

                List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage("T_Commodity", 1, PAGE_SIZE_UI);
                initCommodityList(allInfoCommList);
                viewController.commodityInfoListView.setItems(null);
                viewController.commodityInfoListView.setItems(FXCollections.observableArrayList(commodityList));
                viewController.commodityInfoListView.scrollTo(0);
                RetrieveCommodityInventoryListCellAdapter.lastIndex = PAGE_SIZE_UI;
                viewController.commodityInfoListView.setCellFactory(param -> {
                    adapter = new RetrieveCommodityInventoryListCellAdapter();
                    adapter.setOnLoadMoreListener(() -> {
                        if (commodityList.size() < totalCommodityNumber) {
                            loadingMoreTimes++;
                            loadMore();
                        } else {
                            adapter.setLoadCompleted();
                            ToastUtil.toast("没有更多的商品");
                        }
                    });
                    return adapter;
                });
                break;
            case 2:
                dismissLoadingView();
                viewController.commodityInfoListView.setVisible(true);
                viewController.commodityInfoListView.setManaged(true);
                viewController.query_fail_view.setVisible(false);
                viewController.query_fail_view.setManaged(false);
                //
                for (int i = 0; i < commodityList.size(); i++) {
                    commodityList.get(i).setIndex(i + 1);
                }
                //
                viewController.commodityInfoListView.scrollTo(adapterQueryByBarcode.getLastIndex() - PAGE_SIZE_UI);
                viewController.commodityInfoListView.setItems(null);
                viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
                adapterQueryByBarcode.setLastIndex(commodityList.size());
                adapterQueryByBarcode.setLoadCompleted();
                break;
            case 3:
                viewController.commodityInfoListView.setVisible(false);
                viewController.commodityInfoListView.setManaged(false);
                viewController.query_fail_view.setVisible(true);
                viewController.query_fail_view.setManaged(true);
                viewController.tv_failtips.setText("无法找到条形码对应的商品库存...");
                //搜索不到条形码时的逻辑
                break;
            case 4:
                dismissLoadingView();
                viewController.commodityInfoListView.scrollTo(adapter.getLastIndex() - PAGE_SIZE_UI);
                viewController.commodityInfoListView.setItems(null);
                viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
                System.out.println("滑动至 " + (adapter.getLastIndex() - PAGE_SIZE_UI));
                //
                adapter.setLastIndex(commodityList.size());
                adapter.setLoadCompleted();
                //todo 原来是用来解决突然切换界面导致更新主线程失败的，现在不知是否还需要
//                if (getActivity() != null) {
//                    viewController.commodityInfoListView.setLoadCompleted();
//                }
                break;
            case 5:
                searchRci_click(1,PAGE_SIZE_UI);
//                viewController.rci_condition_input.setText("");
                break;
        }
    }

    private void retrieveNCBarcode(String pageIndex) {
        //同步商品条形码，因为条形码数量太多，所以需要分页同步，与Commodity一样
        Barcodes barcodes = new Barcodes();
        barcodes.setPageIndex(pageIndex);
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        if (!barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            log.info("Barcodes的全部同步失败！");
            viewController.closeLoadingDialog();
            ToastUtil.toast("网络故障，无法进行同步！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNCommodityCategory() {
        //同步商品类别
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            log.info("Category的全部同步失败！");
            viewController.closeLoadingDialog();
            ToastUtil.toast("网络故障，无法进行同步！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNCPackageUnit() {
        //同步商品包装单位
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        packageUnitSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            log.info("PackageUnit的全部同步失败！");
            viewController.closeLoadingDialog();
            ToastUtil.toast("网络故障，无法进行同步！", ToastUtil.LENGTH_SHORT);
        }
    }

    TimerTask timerTask;
    Timer timer = new Timer();
    double progressSize = 0;

    //滑动列表加载数据时，显示加载中的等待框
    public void showLoadingView() {
        viewController.progress.setVisible(true);
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (progressSize >= 1) {
                        progressSize = 0;
                    }
                    Platform.runLater(() -> {
                        viewController.progress.setProgress(progressSize);
                        progressSize += 0.1;
                    });
                }
            };
            if (timer == null) {
                timer = new Timer();
            }
            timer.schedule(timerTask, 100, 100);
        }
    }

    //隐藏列表加载数据时的等待框
    public void dismissLoadingView() {
        viewController.progress.setVisible(false);
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            timer.cancel();
            timer.purge();
            timer = null;
            progressSize = 0;
        }
    }

    // TODO
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//            //浮动键盘设置
//            DisplayCustomKeyBoard(condition_input,true,false);
//        }
//        return false;
//    }
}
