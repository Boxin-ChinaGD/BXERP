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
    private List<Commodity> commodityList = new ArrayList<Commodity>();//?????????????????????????????????????????????????????????
    /**
     * ??????????????????????????????????????????????????????????????????????????????
     */
    private Set<Integer> commodityIdSet = new HashSet<>();
    private int count;//call ??????action????????????????????????
    /**
     * ?????????????????????currentlySyncCommodityPageIndex??????????????????1?????????=1??????????????????????????????1??????????????????
     */
    private int currentlySyncCommodityPageIndex = 1;
    /**
     * ?????????????????????currentlySyncBarcodesPageIndex??????Barcodes??????1?????????=1??????????????????????????????1??????Barcodes??????
     */
    private int currentlySyncBarcodesPageIndex = 1;
    /**
     * ???????????????????????????,????????????????????????????????????????????????????????????0???=1???????????????1?????????????????????????????????2????????????????????????????????????1???????????????
     */
    private int loadingMoreTimes;
    private String searchBarcodes;//?????????????????????????????????
    private boolean isSendMessage;//handler????????????message
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
                        log.info("??????????????????" + count);
                        int totalPageIndex = count % Integer.valueOf(commodity.getPageSize()) != 0 ? count / Integer.valueOf(commodity.getPageSize()) + 1 : count / Integer.valueOf(commodity.getPageSize());//??????????????????totalPageIndex???????????????
                        if (currentlySyncCommodityPageIndex < totalPageIndex) {
                            log.info("????????????");
                            commodity.setPageIndex(String.valueOf(++currentlySyncCommodityPageIndex));
                            if (commodity.getPageIndex() == String.valueOf(totalPageIndex)) {
                                commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_END);
                            }

                            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                                log.info("Commodity????????????????????????");
                                viewController.closeLoadingDialog();
                                ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                            }
                        } else {
                            // ???????????????????????????currentlySyncCommodityPageIndex
                            currentlySyncCommodityPageIndex = 1;
                            //??????Barcode
                            retrieveNCBarcode(BaseHttpBO.FIRST_PAGE_Index_Default);
                        }
                    }
                    break;
                case ESET_Commodity_UpdateNAsync:
                    System.out.println("event.getHttpBO().getHttpEvent().getListMasterTable():" + event.getHttpBO().getHttpEvent().getListMasterTable());
                    if (event.getHttpBO().getHttpEvent().getListMasterTable().size() > 0) {//????????????commodity???????????????????????????????????????
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
                        } else { // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            viewController.closeLoadingDialog();
                            ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                        }

                        System.out.println("???????????????" + commodityList.size());
                        Message message = new Message();
                        message.what = 2;
                        PlatForm.get().sendMessage(message);
                    } else if(commodityList.size() > 0){
                        Platform.runLater(()->{
                            viewController.showToastMessage("???????????????");
                            viewController.closeLoadingDialog();
                            //
                            dismissLoadingView();
                            adapter.setLoadCompleted();
                        });
                    }
                    else {//????????????????????????????????????????????????UI
                        viewController.closeLoadingDialog();
                        Message message = new Message();
                        message.what = 3;
                        PlatForm.get().sendMessage(message);
                    }
                    break;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    private Commodity enrichCommodityToShowOnUI(Commodity comm) {
        Barcodes barcodes = new Barcodes();
        if (StringUtils.isTrimEmpty(comm.getBarcode())) {
            // ????????????????????????????????????????????????????????????????????????????????????????????????
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryStage) {
            event.onEvent();

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                //??????PackgeUnit
                retrieveNCPackageUnit();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
                log.info("Barcodes????????????: " + count);
                int totalPageIndex = count % Integer.valueOf(barcodes.getPageSize()) != 0 ? count / Integer.valueOf(barcodes.getPageSize()) + 1 : count / Integer.valueOf(barcodes.getPageSize());//?????????????????????totalPageIndex???????????????
                if (currentlySyncBarcodesPageIndex < totalPageIndex) {
                    retrieveNCBarcode(String.valueOf(++currentlySyncBarcodesPageIndex));
                } else {
                    //??????????????????
                    retrieveNCommodityCategory();
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public RetrieveCommodityInventoryViewController() {

    }

    //????????????????????????????????????????????????????????????????????????
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
//                if (false) { // ???????????????????????????????????????
////                if (commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) { // ???????????????????????????????????????
//                    ToastUtil.toast(commoditySQLiteEvent.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
//                    // ????????????????????????????????????????????????
//                    commoditySQLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                    commoditySQLiteEvent.setLastErrorMessage("");
//                }
//                else {
                    // ????????????????????????????????????????????????????????????
                    if(adapter != null) {
                        adapter.setOnLoadMoreListener(null);
                        adapter = null;
                    }
                    viewController.commodityInfoListView.setCellFactory(null);
                    // ?????????????????????????????????????????????ID
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
                    // ???????????????ID?????????????????????
                    for (Barcodes b : barcodesList) {
                        Commodity commodity = new Commodity();
                        commodity.setID(b.getCommodityID());
                        Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                        if (retrieveCommodityByID == null) { // ??????????????????????????????????????????????????????
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
                            System.out.println("???????????????????????????" + commodityList.size() + "??????????????????" + barcodesListAll.size());
                            if (loadingMoreTimes < (barcodesListAll.size() % PAGE_SIZE_UI == 0 ? barcodesListAll.size() / PAGE_SIZE_UI : barcodesListAll.size() / PAGE_SIZE_UI + 1) ) {
//                            if (commodityList.size() < barcodesListAll.size()) {
                                loadingMoreTimes++;
                                System.out.println("?????????????????????" + barcodesListAll.size());
                                loadMoreByBarcodesCondition();
                            } else {
                                adapterQueryByBarcode.setLoadCompleted();
                                Platform.runLater(()-> {
                                    ToastUtil.toast("?????????????????????");
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
            viewController.tv_failtips.setText("??????????????????????????????????????????????????????????????????...");
            viewController.commodityInfoListView.setVisible(false);
            viewController.commodityInfoListView.setManaged(false);
        } else {
            ToastUtil.toast("????????????????????????...", ToastUtil.LENGTH_SHORT);
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
        //???????????????????????????????????????????????????
        if (adapter != null) {
            adapter.setLastIndex(PAGE_SIZE_UI);
        }
        initEventAndBo();
        initData();

        // ??????key???????????????????????????20ms?????????????????????????????????
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(scanResult -> {
            log.info("??????????????????????????????" + scanResult);
            viewController.rci_condition_input.setText(scanResult);
            // ????????????????????????
            Message message = new Message();
            message.what = 5;
            PlatForm.get().sendMessage(message);
        });

        //??????????????????????????????????????????????????????scanGun
        initScanGunListener(viewController.rci_Pane);

        viewController.rci_condition_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("".equals(viewController.rci_condition_input.getText()) || null == viewController.rci_condition_input.getText()) {
                //?????????????????????
                viewController.rci_search1.setVisible(false);
            } else if (!"".equals(viewController.rci_condition_input.getText()) && null != viewController.rci_condition_input.getText()) {
                viewController.rci_search1.setVisible(true);
            }
            //??????editText?????????????????????
        });

        //???????????????????????????????????????
        viewController.commodityInfoListView.requestFocus();
    }

    //??????????????????
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
        //todo ??????????????????
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
     * ??????????????????????????????ListView????????????????????????????????????deleteAll???????????????
     */
    public void initData() {
        commodityIdSet.clear();
        viewController.commodityInfoListView.scrollTo(0);
        RetrieveCommodityInventoryListCellAdapter.mIsLoading = false;
        // ??????????????????????????????
        dismissLoadingView();
        if(adapterQueryByBarcode != null) {
            adapterQueryByBarcode.setOnLoadMoreListener(null);
            adapterQueryByBarcode = null;
            viewController.commodityInfoListView.setItems(null);
        }
        final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseHttpBO.INVALID_CASE_ID, null)).size();

        List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage("T_Commodity", 1, PAGE_SIZE_UI);

        // ????????????????????????
        commodityList.clear();
        // ??????????????????????????????????????????????????????????????????????????????
        loadingMoreTimes = 0;
        initCommodityList(allInfoCommList);
        viewController.commodityInfoListView.setItems(null);
        viewController.commodityInfoListView.setCellFactory(null);
        viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
        viewController.commodityInfoListView.scrollTo(commodityList.size() - PAGE_SIZE_UI);
        viewController.commodityInfoListView.setCellFactory(e -> {
            adapter = new RetrieveCommodityInventoryListCellAdapter();
            adapter.setOnLoadMoreListener(() -> {
                System.out.println("???????????????????????????" + commodityList.size() + "??????????????????" + totalCommodityNumber);
                if (commodityList.size() < totalCommodityNumber) {
                    loadingMoreTimes++;
                    System.out.println("?????????????????????" + totalCommodityNumber);
                    loadMore();
                } else {
                    adapter.setLoadCompleted();
                    ToastUtil.toast("?????????????????????");
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
     * ??????????????????????????????????????????????????????
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

                // ???????????????ID?????????????????????
                for (Barcodes b : barcodesList) {
                    Commodity commodity = new Commodity();
                    commodity.setID(b.getCommodityID());
                    Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                    if (retrieveCommodityByID == null) { // ??????????????????????????????????????????????????????
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
        //????????????commodity??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        for (int i = 0; i < commodityList.size(); i++) {
            commodityList.get(i).setIndex(i + 1);//???????????????1??????
        }
    }

    // TODO ?????????
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
            //????????????
            Commodity commodity = new Commodity();
            commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
//                    commodityRunTimes = 0;
            commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);
            commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                log.info("Commodity????????????????????????");
                ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            } else {
                viewController.showLoadingDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("????????????????????????????????????" + e.getMessage());
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
                            ToastUtil.toast("?????????????????????");
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
                viewController.tv_failtips.setText("??????????????????????????????????????????...");
                //?????????????????????????????????
                break;
            case 4:
                dismissLoadingView();
                viewController.commodityInfoListView.scrollTo(adapter.getLastIndex() - PAGE_SIZE_UI);
                viewController.commodityInfoListView.setItems(null);
                viewController.commodityInfoListView.setItems(FXCollections.observableList(commodityList));
                System.out.println("????????? " + (adapter.getLastIndex() - PAGE_SIZE_UI));
                //
                adapter.setLastIndex(commodityList.size());
                adapter.setLoadCompleted();
                //todo ???????????????????????????????????????????????????????????????????????????????????????????????????
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
        //????????????????????????????????????????????????????????????????????????????????????Commodity??????
        Barcodes barcodes = new Barcodes();
        barcodes.setPageIndex(pageIndex);
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        if (!barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            log.info("Barcodes????????????????????????");
            viewController.closeLoadingDialog();
            ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNCommodityCategory() {
        //??????????????????
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            log.info("Category????????????????????????");
            viewController.closeLoadingDialog();
            ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNCPackageUnit() {
        //????????????????????????
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        packageUnitSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            log.info("PackageUnit????????????????????????");
            viewController.closeLoadingDialog();
            ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    TimerTask timerTask;
    Timer timer = new Timer();
    double progressSize = 0;

    //?????????????????????????????????????????????????????????
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

    //???????????????????????????????????????
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
//            //??????????????????
//            DisplayCustomKeyBoard(condition_input,true,false);
//        }
//        return false;
//    }
}
