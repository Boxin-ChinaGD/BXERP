//package com.bx.erp.view.activity;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.R;
//import com.bx.erp.bo.BarcodesHttpBO;
//import com.bx.erp.bo.BarcodesSQLiteBO;
//import com.bx.erp.bo.BaseHttpBO;
//import com.bx.erp.bo.BaseSQLiteBO;
//import com.bx.erp.bo.CommodityCategoryHttpBO;
//import com.bx.erp.bo.CommodityCategorySQLiteBO;
//import com.bx.erp.bo.CommodityHttpBO;
//import com.bx.erp.bo.CommoditySQLiteBO;
//import com.bx.erp.bo.PackageUnitHttpBO;
//import com.bx.erp.bo.PackageUnitSQLiteBO;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BarcodesHttpEvent;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.CommodityCategoryHttpEvent;
//import com.bx.erp.event.CommodityHttpEvent;
//import com.bx.erp.event.PackageUnitHttpEvent;
//import com.bx.erp.event.UI.BarcodesSQLiteEvent;
//import com.bx.erp.event.UI.BaseSQLiteEvent;
//import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
//import com.bx.erp.event.UI.CommoditySQLiteEvent;
//import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
//import com.bx.erp.helper.ScanGun;
//import com.bx.erp.model.Barcodes;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Commodity;
//import com.bx.erp.model.CommodityCategory;
//import com.bx.erp.model.CommodityDao;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.PackageUnit;
//import com.bx.erp.presenter.BarcodesPresenter;
//import com.bx.erp.presenter.CommodityCategoryPresenter;
//import com.bx.erp.presenter.CommodityPresenter;
//import com.bx.erp.presenter.PackageUnitPresenter;
//import com.bx.erp.utils.StringUtils;
//import com.bx.erp.view.adapter.RetrieveCommodityInventoryListAdapter;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import static com.bx.erp.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;
//import static com.bx.erp.bo.BaseSQLiteBO.CASE_Category_RetrieveNByConditions;
//
//public class RetrieveCommodityInventoryActivity extends BaseActivity implements View.OnClickListener {
//    private Logger log = Logger.getLogger(this.getClass());
//    @BindView(R.id.retrieve_commodity_inventory_back)
//    TextView retrieve_commodity_inventory_back;
//    @BindView(R.id.condition_input)
//    EditText condition_input;
//    @BindView(R.id.delete_all)
//    ImageView ivDelete_all;
//    @BindView(R.id.search)
//    ImageView ivSearch;
//    @BindView(R.id.sync_text)
//    TextView sync_text;
//    @BindView(R.id.commodity_infomation_listview)
//    LoadMoreListView commodityInfoListView;
//    @BindView(R.id.query_fail_view)
//    RelativeLayout query_fail_view;
//    @BindView(R.id.normal_view)
//    LinearLayout normal_view;
//    @BindView(R.id.tv_failtips)
//    TextView tv_failtips;
//
//    private CommodityPresenter commodityPresenter = null;
//    private static CommoditySQLiteBO commoditySQLiteBO = null;
//    private static CommodityHttpBO commodityHttpBO = null;
//    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
//    private static CommodityHttpEvent commodityHttpEvent = null;
//    //
//    private CommodityCategoryPresenter commodityCategoryPresenter = null;
//    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
//    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
//    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
//    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
//    //
//    private PackageUnitPresenter packageUnitPresenter = null;
//    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
//    private static PackageUnitHttpBO packageUnitHttpBO = null;
//    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
//    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
//    //
//    private BarcodesPresenter barcodesPresenter = null;
//    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
//    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
//    private static BarcodesHttpBO barcodesHttpBO = null;
//    private static BarcodesHttpEvent barcodesHttpEvent = null;
//
//    private RetrieveCommodityInventoryListAdapter adapter = null;
//    private List<Commodity> commodityList = new ArrayList<Commodity>();//?????????????????????????????????????????????????????????
//    private int count;//call ??????action????????????????????????
//    private int commodityRunTimes = 1;//????????????runTimes???????????????????????????????????????
//    private int barcodesRunTimes = 1;//????????????runTimes??????????????????????????????????????????
//    private int loadingMoreTimes;//???????????????????????????,????????????????????????????????????????????????????????????0
//    private String searchBarcodes;//?????????????????????????????????
//    private boolean isSendMessage;//handler????????????message
//    private ScanGun scanGun = null;
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommodityHttpEvent(CommodityHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            event.onEvent();
//            switch (event.getEventTypeSQLite()) {
//                case ESET_Commodity_RefreshByServerDataAsyncC_Done:
//                    if (!"".equals(commodityHttpEvent.getCount())) {
//                        Commodity commodity = new Commodity();
//                        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
//                        count = Integer.parseInt(commodityHttpEvent.getCount());
//                        log.info("??????????????????" + count);
//                        int totalPageIndex = count % Integer.valueOf(commodity.getPageSize()) != 0 ? count / Integer.valueOf(commodity.getPageSize()) + 1 : count / Integer.valueOf(commodity.getPageSize());//??????????????????totalPageIndex???????????????
//                        if (commodityRunTimes < totalPageIndex) {
//                            log.info("????????????");
//                            commodity.setPageIndex(String.valueOf(++commodityRunTimes));
//                            if (commodity.getPageIndex() == String.valueOf(totalPageIndex)) {
//                                commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_END);
//                            }
//
//                            if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
//                                log.info("Commodity????????????????????????");
//                                closeLoadingDialog(loadingDailog);
//                                Toast.makeText(appApplication, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            //??????Barcode
//                            retrieveNCBarcode(BaseHttpBO.FIRST_PAGE_Index_Default);
//                        }
//                    }
//                    break;
//                case ESET_Commodity_UpdateNAsync:
//                    System.out.println("event.getHttpBO().getHttpEvent().getListMasterTable():" + event.getHttpBO().getHttpEvent().getListMasterTable());
//                    if (event.getHttpBO().getHttpEvent().getListMasterTable().size() > 0) {//????????????commodity???????????????????????????????????????
//                        System.out.println("commoditySQLiteEvent.getListMasterTable():" + commoditySQLiteEvent.getListMasterTable());
//                        if (commoditySQLiteEvent.getListMasterTable().size() > 0) {
//                            List<Commodity> commList = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
//                            System.out.println("commList:" + commList);
//                            for (int i = 0; i < commList.size(); i++) {
//                                //
//                                Commodity commodity = enrichCommodityToShowOnUI(commList.get(i));
//                                commodityList.add(commodity);
//                            }
//                            closeLoadingDialog(loadingDailog);
//                            if (commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                                Toast.makeText(this, commoditySQLiteEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        } else { // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                            closeLoadingDialog(loadingDailog);
//                            Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        Message message = new Message();
//                        message.what = 2;
//                        handler.sendMessage(message);
//                    } else {//????????????????????????????????????????????????UI
//                        closeLoadingDialog(loadingDailog);
//                        Message message = new Message();
//                        message.what = 3;
//                        handler.sendMessage(message);
//                    }
//                    break;
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    private Commodity enrichCommodityToShowOnUI(Commodity comm) {
//        Barcodes barcodes = new Barcodes();
//        if (StringUtils.isTrimEmpty(comm.getBarcode())) {
//            // ????????????????????????????????????????????????????????????????????????????????????????????????
//            barcodes.setSql("where F_CommodityID = ?");
//            barcodes.setConditions(new String[]{String.valueOf(comm.getID())});
//            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
//            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
//            if (barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                return null;
//            }
//            barcodes.setBarcode(barcodesList.get(0).getBarcode());
//        } else {
//            barcodes.setBarcode(comm.getBarcode());
//        }
//        //
//        CommodityCategory commodityCategory = new CommodityCategory();
//        commodityCategory.setID(Long.valueOf(comm.getCategoryID()));
//        commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
//        //
//        PackageUnit packageUnit = new PackageUnit();
//        packageUnit.setID(Long.valueOf(comm.getPackageUnitID()));
//        packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
//        //
//        Commodity commodity = new Commodity();
//        commodity.setBarcode(barcodes.getBarcode());
//        commodity.setName(comm.getName());
//        commodity.setCategory(commodityCategory.getName());
//        commodity.setSpecification(comm.getSpecification());
//        commodity.setNO(comm.getNO());
//        commodity.setPackageUnit(packageUnit.getName());
//        return commodity;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCategoryHttpEvent(CommodityCategoryHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            event.onEvent();
//
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                //??????PackgeUnit
//                retrieveNCPackageUnit();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            event.onEvent();
//
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                if (isSendMessage) {
//                    closeLoadingDialog(loadingDailog);
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);
//                    isSendMessage = false;
//                }
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                closeLoadingDialog(loadingDailog);
//                Toast.makeText(this, event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
//        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
//            event.onEvent();
//            if (barcodesHttpEvent.getCount() != null && !"".equals(barcodesHttpEvent.getCount())) {
//                Barcodes barcodes = new Barcodes();
//                barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
//                count = Integer.parseInt(barcodesHttpEvent.getCount());
//                barcodesHttpEvent.setCount(""); // TODO
//                log.info("Barcodes????????????: " + count);
//                int totalPageIndex = count % Integer.valueOf(barcodes.getPageSize()) != 0 ? count / Integer.valueOf(barcodes.getPageSize()) + 1 : count / Integer.valueOf(barcodes.getPageSize());//?????????????????????totalPageIndex???????????????
//                if (barcodesRunTimes < totalPageIndex) {
//                    retrieveNCBarcode(String.valueOf(++barcodesRunTimes));
//                } else {
//                    //??????????????????
//                    retrieveNCommodityCategory();
//                }
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.retrieve_commodity_inventory);
//
//        ButterKnife.bind(this);
//
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
//        if (commoditySQLiteEvent == null) {
//            commoditySQLiteEvent = new CommoditySQLiteEvent();
//            commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (commodityHttpEvent == null) {
//            commodityHttpEvent = new CommodityHttpEvent();
//            commodityHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (commoditySQLiteBO == null) {
//            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        if (commodityHttpBO == null) {
//            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
//        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
//        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
//        commodityHttpEvent.setHttpBO(commodityHttpBO);
//        //
//        commodityCategoryPresenter = GlobalController.getInstance().getCommodityCategoryPresenter();
//        if (commodityCategorySQLiteEvent == null) {
//            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
//            commodityCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (commodityCategoryHttpEvent == null) {
//            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
//            commodityCategoryHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (commodityCategorySQLiteBO == null) {
//            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        }
//        if (commodityCategoryHttpBO == null) {
//            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        }
//        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
//        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
//        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
//        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
//        //
//        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
//        if (packageUnitSQLiteEvent == null) {
//            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
//            packageUnitSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (packageUnitHttpEvent == null) {
//            packageUnitHttpEvent = new PackageUnitHttpEvent();
//            packageUnitHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (packageUnitSQLiteBO == null) {
//            packageUnitSQLiteBO = new PackageUnitSQLiteBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
//        }
//        if (packageUnitHttpBO == null) {
//            packageUnitHttpBO = new PackageUnitHttpBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
//        }
//        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
//        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
//        //
//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
//        if (barcodesSQLiteEvent == null) {
//            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
//            barcodesSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (barcodesHttpEvent == null) {
//            barcodesHttpEvent = new BarcodesHttpEvent();
//            barcodesHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
//        }
//        if (barcodesSQLiteBO == null) {
//            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
//        }
//        if (barcodesHttpBO == null) {
//            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
//        }
//
//        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
//        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
//        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
//        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
//
//        //????????????????????????
//        condition_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    //?????????????????????
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(condition_input.getWindowToken(), 0);
//                }
//            }
//        });
//
//        retrieve_commodity_inventory_back.setOnClickListener(this);
//        ivDelete_all.setOnClickListener(this);
//        ivSearch.setOnClickListener(this);
//        sync_text.setOnClickListener(this);
//
//        // ??????key???????????????????????????20ms?????????????????????????????????
//        ScanGun.setMaxKeysInterval(50);
//        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
//            @Override
//            public void onScanFinish(String scanResult) {
//                log.info("??????????????????????????????" + scanResult);
//                if (!scanResult.equals("")) {
//                    condition_input.setText(scanResult);
//                }
//                adapter.notifyDataSetChanged();
//                // ????????????????????????
//                Message message = new Message();
//                message.what = 5;
//                handler.sendMessage(message);
//            }
//        });
//
//        condition_input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if ("".equals(condition_input.getText().toString()) || null == condition_input.getText().toString()) {
//                    ivDelete_all.setVisibility(View.INVISIBLE);
//                } else if (!"".equals(condition_input.getText().toString()) && null != condition_input.getText().toString()) {
//                    ivDelete_all.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
////        condition_input.setOnKeyListener(new View.OnKeyListener() {
////            @Override
////            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                condition_input.setText(condition_input.getText().toString() + event.getKeyCode());
////                return true;
////            }
////        });
//
//        initData();
//    }
//
//    /**
//     * ??????????????????????????????ListView????????????????????????????????????deleteAll???????????????
//     */
//    private void initData() {
//        final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseHttpBO.INVALID_CASE_ID, null)).size();
//
//        List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, 1, 15);
//        // ????????????????????????
//        commodityList.clear();
//        initCommodityList(allInfoCommList);
//        adapter = new RetrieveCommodityInventoryListAdapter(this, commodityList);
//        commodityInfoListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onloadMore() {
//                /*Toast.makeText(appApplication, "?????????" + commodityList.size(), Toast.LENGTH_SHORT).show();*/
//                if (commodityList.size() < totalCommodityNumber) {
//                    loadingMoreTimes++;
//                    loadMore();
//                } else {
//                    commodityInfoListView.setLoadCompleted();
//                    Toast.makeText(appApplication, "?????????????????????", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
//
//    private void loadMore() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, loadingMoreTimes + 1, 15);
//                initCommodityList(allInfoCommList);
//                Message message = new Message();
//                message.what = 4;
//                handler.sendMessage(message);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        commodityInfoListView.setLoadCompleted();
//                    }
//                });
//            }
//        }.start();
//    }
//
//    private void initCommodityList(List<Commodity> list) {
//        for (int i = 0; i < list.size(); i++) {
//            Commodity commodity = new Commodity();
//            commodity.setID(list.get(i).getID());
//            //
//            Barcodes barcodes = new Barcodes();
//            barcodes.setSql("where F_CommodityID = ?");
//            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
//            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
//            //
//            CommodityCategory commodityCategory = new CommodityCategory();
////            commodityCategory.setID(Long.valueOf(list.get(i).getCategoryID()));
//            commodityCategory.setSql("where F_ID = ?");
//            commodityCategory.setConditions(new String[]{String.valueOf(list.get(i).getCategoryID())});
//            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(CASE_Category_RetrieveNByConditions, commodityCategory);
//            //
//            PackageUnit packageUnit = new PackageUnit();
//            packageUnit.setID(Long.valueOf(list.get(i).getPackageUnitID()));
//            packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseHttpBO.INVALID_CASE_ID, packageUnit);
//            //
//            if (barcodesList != null) {
//                if (barcodesList.size() > 0) {
//                    commodity.setBarcode(barcodesList.get(0).getBarcode());
//                }
//            }
//            commodity.setName(list.get(i).getName());
//            if (commodityCategoryList != null && commodityCategoryList.size() > 0) {
//                commodity.setCategory(commodityCategoryList.get(0).getName());
//            }
//            commodity.setSpecification(list.get(i).getSpecification());
//            commodity.setNO(list.get(i).getNO());
//            if (packageUnit != null) {
//                commodity.setPackageUnit(packageUnit.getName());
//            }
//            commodityList.add(commodity);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.retrieve_commodity_inventory_back:
//                Intent mainIntent = new Intent(this, MainActivity.class);
//                RetrieveCommodityInventoryActivity.this.finish();
//                startActivity(mainIntent);
//                break;
//            case R.id.delete_all:
//                condition_input.setText("");
//                query_fail_view.setVisibility(View.GONE);
//                normal_view.setVisibility(View.VISIBLE);
//                initData();
//                break;
//            case R.id.search:
//                searchBarcodes = condition_input.getText().toString();
//                if (searchBarcodes.trim().length() > BaseActivity.FUZZY_QUERY_LENGTH) {
//                    Commodity comm = new Commodity();
//                    comm.setBarcode(searchBarcodes);
//                    commodityList.clear();
//                    adapter.notifyDataSetChanged();
//                    if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, comm)) {
//                        if (commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) { // ???????????????????????????????????????
//                            Toast.makeText(getApplicationContext(), commoditySQLiteEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                            // ????????????????????????????????????????????????
//                            commoditySQLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                            commoditySQLiteEvent.setLastErrorMessage("");
//                        } else { // ????????????????????????????????????????????????????????????
//                            // ?????????????????????????????????????????????ID
//                            Barcodes barcodes = new Barcodes();
//                            barcodes.setSql("where F_Barcode LIKE ?");
//                            barcodes.setConditions(new String[]{"%" + searchBarcodes + "%"});
//                            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
//                            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
//
//                            // ???????????????ID?????????????????????
//                            for (Barcodes b : barcodesList) {
//                                Commodity commodity = new Commodity();
//                                commodity.setID(Long.valueOf(String.valueOf(b.getCommodityID())));
//                                Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
//                                if (retrieveCommodityByID == null) { // ??????????????????????????????????????????????????????
//                                    continue;
//                                }
//                                Commodity c = enrichCommodityToShowOnUI(retrieveCommodityByID);
//                                commodityList.add(c);
//                            }
//                        }
//                        Message message = new Message();
//                        if (commodityList.size() == 0) {
//                            message.what = 3;
//                        } else {
//                            message.what = 2;
//                        }
//                        handler.sendMessage(message);
//                        break;
//                    } else {
//                        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                    }
//                } else if (searchBarcodes.trim().length() > 0) {
//                    query_fail_view.setVisibility(View.VISIBLE);
//                    tv_failtips.setText("??????????????????????????????????????????????????????????????????...");
//                    normal_view.setVisibility(View.GONE);
//                } else {
//                    Toast.makeText(getApplicationContext(), "????????????????????????...", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.sync_text:
//                isSendMessage = true;
//                loadingMoreTimes = 0;
//                try {
//                    //????????????
//                    Commodity commodity = new Commodity();
//                    commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//                    commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
////                    commodityRunTimes = 0;
//                    commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);
//                    commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
//                    if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
//                        log.info("Commodity????????????????????????");
//                        Toast.makeText(appApplication, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//
//                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.info("????????????????????????????????????" + e.getMessage());
//                }
//                break;
//        }
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    commodityList.clear();
//                    final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null)).size();
//
//                    List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, 1, 15);
//                    initCommodityList(allInfoCommList);
//                    adapter = new RetrieveCommodityInventoryListAdapter(getApplicationContext(), commodityList);
//                    commodityInfoListView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//                        @Override
//                        public void onloadMore() {
//                            if (commodityList.size() < totalCommodityNumber) {
//                                /*Toast.makeText(appApplication, "?????????" + commodityList.size(), Toast.LENGTH_SHORT).show();*/
//                                loadingMoreTimes++;
//                                loadMore();
//                            } else {
//                                commodityInfoListView.setLoadCompleted();
//                                Toast.makeText(appApplication, "?????????????????????", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    break;
//                case 2:
//                    normal_view.setVisibility(View.VISIBLE);
//                    query_fail_view.setVisibility(View.GONE);
//                    adapter = new RetrieveCommodityInventoryListAdapter(getApplicationContext(), commodityList);
//                    commodityInfoListView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//                        @Override
//                        public void onloadMore() {
//                            commodityInfoListView.setLoadCompleted();
//                        }
//                    });
//                    break;
//                case 3:
//                    normal_view.setVisibility(View.GONE);
//                    query_fail_view.setVisibility(View.VISIBLE);
//                    tv_failtips.setText("?????????????????????????????????????????????...");
//                    break;
//                case 4:
//                    adapter.notifyDataSetChanged();
//                    break;
//                case 5:
//                    ivSearch.performClick();
//                    condition_input.setText("");
//					// ??????EditText???????????????????????????????????????
//                    condition_input.setFocusable(false);
//                    condition_input.setFocusableInTouchMode(false);
//                    condition_input.setFocusable(true);
//                    condition_input.setFocusableInTouchMode(true);
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        scanGun.isMaybeScanning(keyCode, event);
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
////        //???????????????????????????6?????????????????????????????????????????????????????????????????????????????????????????????????????????
//        log.info(event.getDevice().getName());
//
////        if (!"Virtual".equals(event.getDevice().getName())) { //?????????????????????????????????????????????????????????????????????
//        if (event.getKeyCode() > 6) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {//?????????????????????????????????
//                    return super.dispatchKeyEvent(event);
//                }
//                if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {//??????????????????????????????????????????
//                    return super.dispatchKeyEvent(event);
//                }
//                this.onKeyDown(event.getKeyCode(), event);
//            }
//        } else {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//????????????????????????????????????
//                return super.dispatchKeyEvent(event);
//            }
//        }
////        } else {
////            if (event.getKeyCode() == KeyEvent.KEYCODE_0) {
////                condition_input.setText(condition_input.getText().toString() + "0");
////            } else {
////
////            }
////            switch (event.getKeyCode()) {
////                case KeyEvent.KEYCODE_0:
////                    condition_input.setText(condition_input.getText().toString() + "0");
////
////            }
//        log.info("keycode:" + event.getKeyCode() + "  action" + event.getAction());
////        }
//        return true;
//    }
//
//    private void retrieveNCBarcode(String pageIndex) {
//        //????????????????????????????????????????????????????????????????????????????????????Commodity??????
//        Barcodes barcodes = new Barcodes();
//        barcodes.setPageIndex(pageIndex);
//        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
//        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
//        if (!barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
//            log.info("Barcodes????????????????????????");
//            closeLoadingDialog(loadingDailog);
//            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void retrieveNCommodityCategory() {
//        //??????????????????
//        CommodityCategory commodityCategory = new CommodityCategory();
//        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
//        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
//        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
//            log.info("Category????????????????????????");
//            closeLoadingDialog(loadingDailog);
//            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void retrieveNCPackageUnit() {
//        //????????????????????????
//        PackageUnit packageUnit = new PackageUnit();
//        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
//        packageUnitSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
//        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
//            log.info("PackageUnit????????????????????????");
//            closeLoadingDialog(loadingDailog);
//            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//}