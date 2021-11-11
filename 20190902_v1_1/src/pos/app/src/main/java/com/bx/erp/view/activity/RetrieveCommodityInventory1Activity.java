package com.bx.erp.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityCategoryHttpBO;
import com.bx.erp.bo.CommodityCategorySQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.PackageUnitHttpBO;
import com.bx.erp.bo.PackageUnitSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.helper.ScanGun;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.CommodityDao;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.CommodityCategoryPresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PackageUnitPresenter;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.view.adapter.RetrieveCommodityInventoryListAdapter1;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_Category_RetrieveNByConditions;

/**
 * Created by WPNA on 2020/2/21.
 */

public class RetrieveCommodityInventory1Activity extends BaseFragment1 implements View.OnTouchListener {
    @BindView(R.id.search)
    ImageView ivSearch;
    private Logger log = Logger.getLogger(this.getClass());
    @BindView(R.id.category)
    LinearLayout category;
    @BindView(R.id.favorites_page)
    RelativeLayout favoritesPage;
    Unbinder unbinder;
    @BindView(R.id.commodity_infomation_listview)
    LoadMoreListView commodityInfoListView;
    @BindView(R.id.condition_input)
    EditText condition_input;
    @BindView(R.id.query_fail_view)
    RelativeLayout query_fail_view;
    @BindView(R.id.normal_view)
    LinearLayout normal_view;
    @BindView(R.id.tv_failtips)
    TextView tv_failtips;
    @BindView(R.id.delete_all)
    ImageView ivDelete_all;

    private CommodityPresenter commodityPresenter = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    //
    private CommodityCategoryPresenter commodityCategoryPresenter = null;
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    //
    private PackageUnitPresenter packageUnitPresenter = null;
    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
    private static PackageUnitHttpBO packageUnitHttpBO = null;
    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
    //
    private BarcodesPresenter barcodesPresenter = null;
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;

    private RetrieveCommodityInventoryListAdapter1 adapter = null;
    private RetrieveCommodityInventoryListAdapter1 adapterQueryByBarcode = null;
    private List<Commodity> commodityList = new ArrayList<Commodity>();//当点击全部同步按钮的时候需要进行初始化
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
    /**
     * 库存页面按条形码查询，有一品多码的情况，所以需要去重
     */
    private Set<Integer> commodityIdSet = new HashSet<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                closeLoadingDialog(loadingDailog);
                Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
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
                                closeLoadingDialog(loadingDailog);
                                Toast.makeText(getActivity(), "网络故障，无法进行同步！", Toast.LENGTH_SHORT).show();
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
                            closeLoadingDialog(loadingDailog);
                            if (commoditySQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                Toast.makeText(getActivity(), commoditySQLiteEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else { // 如果本地没有返回数据，代表更新本地商品时失败，可能是本地没有同步到该商品
                            closeLoadingDialog(loadingDailog);
                            Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {//若没有值返回，这显示没有值返回的UI
                        closeLoadingDialog(loadingDailog);
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
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
            barcodes.setSql("where F_CommodityID = ?");
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
        commodityCategory.setID(Long.valueOf(comm.getCategoryID()));
        commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(Long.valueOf(comm.getPackageUnitID()));
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
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                closeLoadingDialog(loadingDailog);
                Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
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
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                closeLoadingDialog(loadingDailog);
                Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
            event.onEvent();

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                if (isSendMessage) {
                    closeLoadingDialog(loadingDailog);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
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
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                closeLoadingDialog(loadingDailog);
                Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retrieve_commodity_inventory1, container, false);
        unbinder = ButterKnife.bind(this, view);
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        commodityList = new ArrayList<Commodity>();
        initEventAndBo();

        condition_input.setOnTouchListener(this);

        // 设置key事件最大间隔，默认20ms，部分低端扫码枪效率低
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
            @Override
            public void onScanFinish(String scanResult) {

                log.info("扫码枪扫描到的数据：" + scanResult);
                if (!scanResult.equals("")) {
                    condition_input.setText(scanResult);
                }
                adapter.notifyDataSetChanged();
                // 触发搜索点击事件
                Message message = new Message();
                message.what = 5;
                handler.sendMessage(message);
            }
        });

        condition_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(condition_input.getText().toString()) || null == condition_input.getText().toString()) {
                    ivDelete_all.setVisibility(View.INVISIBLE);
                } else if (!"".equals(condition_input.getText().toString()) && null != condition_input.getText().toString()) {
                    ivDelete_all.setVisibility(View.VISIBLE);
                }
                //显示edittext的全部删除按钮
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initData();
        return view;
    }

    private void initEventAndBo() {
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
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
        commodityCategoryPresenter = GlobalController.getInstance().getCommodityCategoryPresenter();
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (commodityCategorySQLiteBO == null) {
            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        if (commodityCategoryHttpBO == null) {
            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        if (packageUnitSQLiteEvent == null) {
            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
            packageUnitSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (packageUnitHttpEvent == null) {
            packageUnitHttpEvent = new PackageUnitHttpEvent();
            packageUnitHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (packageUnitSQLiteBO == null) {
            packageUnitSQLiteBO = new PackageUnitSQLiteBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
        }
        if (packageUnitHttpBO == null) {
            packageUnitHttpBO = new PackageUnitHttpBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
        }
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
        //
        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(BaseEvent.EVENT_ID_RetrieveCommodityInventoryActivity);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }

        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
    }

    /**
     * 初始化数据，令数据在ListView中显示。进入本页面或点击deleteAll按钮时调用
     */
    private void initData() {
        final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseHttpBO.INVALID_CASE_ID, null)).size();

        List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, 1, PAGE_SIZE_UI);
        // 清空原有商品列表
        commodityList.clear();
        loadingMoreTimes = 0;
        initCommodityList(allInfoCommList);
        adapter = new RetrieveCommodityInventoryListAdapter1(getActivity(), commodityList, R.layout.retrieve_commodity_inventory_list_item1);
        adapter.notifyDataSetChanged();
        commodityInfoListView.setAdapter(adapter);
        commodityInfoListView.setDivider(null);//取消分割线
        //ListView中item被点击的事件
        commodityInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HideVirtualKeyBoard();
                ListItemClick(i);
            }
        });
        adapter.notifyDataSetChanged();
        commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                /*Toast.makeText(appApplication, "长度：" + commodityList.size(), Toast.LENGTH_SHORT).show();*/
                if (commodityList.size() < totalCommodityNumber) {
                    loadingMoreTimes++;
                    loadMore();
                } else {
                    commodityInfoListView.setLoadCompleted();
                    Toast.makeText(getActivity(), "没有更多的商品", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void ListItemClick(int i) {
        if (favoritesPage.getVisibility() == View.GONE) {
            category.setVisibility(View.GONE);
            favoritesPage.setVisibility(View.VISIBLE);
            setAnimation(favoritesPage);
        }

        adapter.setType(2);
        adapter.setSelection(i);
        adapter.notifyDataSetChanged();
        commodityInfoListView.smoothScrollToPosition(i);
    }

    private void loadMore() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, loadingMoreTimes + 1, PAGE_SIZE_UI);

                initCommodityList(allInfoCommList);
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 库存页面，根据条形码查询后的滑动加载
     */
    private void loadMoreByBarcodesCondition() {
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
                barcodes.setSql("where F_Barcode LIKE ? limit ?, ?");
                barcodes.setConditions(new String[]{"%" + searchBarcodes + "%", String.valueOf(loadingMoreTimes * PAGE_SIZE_UI), String.valueOf(PAGE_SIZE_UI)});
                barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);

                // 根据条形码ID搜索相关的商品
                for (Barcodes b : barcodesList) {
                    Commodity commodity = new Commodity();
                    commodity.setID(Long.valueOf(String.valueOf(b.getCommodityID())));
                    Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                    if (retrieveCommodityByID == null) { // 如果查不到相应的商品，就跳过此次循环
                        continue;
                    }
                    if(!commodityIdSet.contains(retrieveCommodityByID.getID().intValue())) {
                        Commodity c = enrichCommodityToShowOnUI(retrieveCommodityByID);
                        commodityList.add(c);
                        commodityIdSet.add(retrieveCommodityByID.getID().intValue());
                    }
                }
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }.start();
    }

    private void initCommodityList(List<Commodity> list) {
        for (int i = 0; i < list.size(); i++) {
            Commodity commodity = new Commodity();
            commodity.setID(list.get(i).getID());
            //
            Barcodes barcodes = new Barcodes();
            barcodes.setSql("where F_CommodityID = ?");
            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
            //
            CommodityCategory commodityCategory = new CommodityCategory();
//            commodityCategory.setID(Long.valueOf(list.get(i).getCategoryID()));
            commodityCategory.setSql("where F_ID = ?");
            commodityCategory.setConditions(new String[]{String.valueOf(list.get(i).getCategoryID())});
            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(CASE_Category_RetrieveNByConditions, commodityCategory);
            //
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setID(Long.valueOf(list.get(i).getPackageUnitID()));
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("xxxxxxxxxxxxxxxxxxx执行了onDetach");
//        commodityPresenter = null;
//        commodityCategoryPresenter = null;
//        packageUnitPresenter = null;
//        packageUnitPresenter = null;
//        barcodesPresenter = null;
//        currentlySyncCommodityPageIndex = 1;
//        currentlySyncBarcodesPageIndex = 1;
//        scanGun = null;
//        searchBarcodes = null;
//        System.gc();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        condition_input.setText("");
        unbinder.unbind();
    }

    @OnClick({R.id.CommodityDetails_close, R.id.sync_text, R.id.search, R.id.delete_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.CommodityDetails_close:
                favoritesPage.setVisibility(View.GONE);
                category.setVisibility(View.VISIBLE);
                setAnimation(favoritesPage);
                adapter.setType(1);
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_all:
                condition_input.setText("");
                query_fail_view.setVisibility(View.GONE);
                normal_view.setVisibility(View.VISIBLE);
                initData();
                break;
            case R.id.search:
                searchBarcodes = condition_input.getText().toString();
                if (searchBarcodes.trim().length() > BaseActivity.FUZZY_QUERY_LENGTH) {
                    Commodity comm = new Commodity();
                    comm.setBarcode(searchBarcodes);
                    commodityList.clear();
                    commodityIdSet.clear();
                    adapter.notifyDataSetChanged();
                    loadingMoreTimes = 0;
//                    if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, comm)) {
//                    if (true) {
//                        if (commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) { // 判断条形码搜索不能输入中文
//                        if (false) { // 判断条形码搜索不能输入中文
//                            Toast.makeText(getActivity(), commoditySQLiteEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//                            // 判断完后需要重置错误码和错误信息
//                            commoditySQLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                            commoditySQLiteEvent.setLastErrorMessage("");
//                        }
                        // 联网断网情况下，都根据条形码从本地模糊搜索商品
                        // 根据搜索条形码模糊查询到条形码ID
                    Barcodes barcodes = new Barcodes();
                    //
                    barcodes.setSql("where F_Barcode LIKE ?");
                    barcodes.setConditions(new String[]{"%" + searchBarcodes + "%"});
                    barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                    final List<Barcodes> barcodesListAll = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
                    //
                    barcodes.setSql("where F_Barcode LIKE ? limit ?, ?");
                    barcodes.setConditions(new String[]{"%" + searchBarcodes + "%", String.valueOf(loadingMoreTimes), String.valueOf(PAGE_SIZE_UI)});
                    barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
                    List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);

                    // 根据条形码ID搜索相关的商品
                    for (Barcodes b : barcodesList) {
                        Commodity commodity = new Commodity();
                        commodity.setID(Long.valueOf(String.valueOf(b.getCommodityID())));
                        Commodity retrieveCommodityByID = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                        if (retrieveCommodityByID == null) { // 如果查不到相应的商品，就跳过此次循环
                            continue;
                        }
                        if(!commodityIdSet.contains(retrieveCommodityByID.getID().intValue())) {
                            Commodity c = enrichCommodityToShowOnUI(retrieveCommodityByID);
                            commodityList.add(c);
                            commodityIdSet.add(retrieveCommodityByID.getID().intValue());
                        }
                    }
                    //
                    adapterQueryByBarcode = new RetrieveCommodityInventoryListAdapter1(getActivity().getApplicationContext(), commodityList, R.layout.retrieve_commodity_inventory_list_item1);
                    adapterQueryByBarcode.notifyDataSetChanged();
                    commodityInfoListView.setAdapter(adapterQueryByBarcode);
                    commodityInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ListItemClick(i);
                        }
                    });
                    adapterQueryByBarcode.notifyDataSetChanged();
                    commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                        @Override
                        public void onloadMore() {
                            if (loadingMoreTimes < (barcodesListAll.size() % PAGE_SIZE_UI == 0 ? barcodesListAll.size() / PAGE_SIZE_UI : barcodesListAll.size() / PAGE_SIZE_UI + 1) ) {
//                            if (commodityList.size() < barcodesListAll.size()) {
                                /*Toast.makeText(appApplication, "长度：" + commodityList.size(), Toast.LENGTH_SHORT).show();*/
                                loadingMoreTimes++;
//                                        loadMore();
                                loadMoreByBarcodesCondition();
                            } else {
                                commodityInfoListView.setLoadCompleted();
                                Toast.makeText(getActivity(), "没有更多的商品", Toast.LENGTH_SHORT).show();
                            }
                            adapterQueryByBarcode.notifyDataSetChanged();
                        }
                    });
                    //
                    Message message = new Message();
                    if (commodityList.size() == 0) {
                        message.what = 3;
                    } else {
                        message.what = 2;
                    }
                    handler.sendMessage(message);
                    break;
//                    }
//                    else {
//                        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                    }
                } else if (searchBarcodes.trim().length() > 0) {
                    query_fail_view.setVisibility(View.VISIBLE);
                    tv_failtips.setText("由于输入的条形码不够完整，无法查询到库存信息...");
                    normal_view.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "请输入商品条形码...", Toast.LENGTH_SHORT).show();
                }
                HideVirtualKeyBoard();
                condition_input.clearFocus();   //清除输入框的焦点，否则会出现扫码枪扫码不正常
                break;
            case R.id.sync_text:
                isSendMessage = true;
                loadingMoreTimes = 0;
                try {
                    //同步商品
                    Commodity commodity = new Commodity();
                    commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                    commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
//                    currentlySyncCommodityPageIndex = 0;
                    commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);
                    commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
                    if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
                        log.info("Commodity的全部同步失败！");
                        Toast.makeText(getActivity(), "网络不可用，无法进行同步！", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("同步失败！，异常信息为：" + e.getMessage());
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    commodityList.clear();
                    final int totalCommodityNumber = (commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null)).size();

                    List<Commodity> allInfoCommList = (List<Commodity>) commodityPresenter.retrieveNAsyncByPage(CommodityDao.TABLENAME, 1, PAGE_SIZE_UI);
                    initCommodityList(allInfoCommList);
                    adapter = new RetrieveCommodityInventoryListAdapter1(getActivity().getApplicationContext(), commodityList, R.layout.retrieve_commodity_inventory_list_item1);
                    adapter.notifyDataSetChanged();
                    commodityInfoListView.setAdapter(adapter);
                    commodityInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ListItemClick(i);
                        }
                    });
                    adapter.notifyDataSetChanged();
                    commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                        @Override
                        public void onloadMore() {
                            if (commodityList.size() < totalCommodityNumber) {
                                /*Toast.makeText(appApplication, "长度：" + commodityList.size(), Toast.LENGTH_SHORT).show();*/
                                loadingMoreTimes++;
                                loadMore();
                            } else {
                                commodityInfoListView.setLoadCompleted();
                                Toast.makeText(getActivity(), "没有更多的商品", Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    normal_view.setVisibility(View.VISIBLE);
                    query_fail_view.setVisibility(View.GONE);
//                    adapter = new RetrieveCommodityInventoryListAdapter1(getActivity().getApplicationContext(), commodityList, R.layout.retrieve_commodity_inventory_list_item1);
//                    adapter.notifyDataSetChanged();
//                    commodityInfoListView.setAdapter(adapter);
//                    commodityInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            ListItemClick(i);
//                        }
//                    });
//                    adapter.notifyDataSetChanged();
//                    commodityInfoListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//                        @Override
//                        public void onloadMore() {
//                            commodityInfoListView.setLoadCompleted();
//                        }
//                    });
                    adapterQueryByBarcode.notifyDataSetChanged();
                    if (getActivity() != null) {
                        commodityInfoListView.setLoadCompleted();
                    }
                    break;
                case 3:
                    normal_view.setVisibility(View.GONE);
                    query_fail_view.setVisibility(View.VISIBLE);
                    tv_failtips.setText("无法找到对应的条形码的商品库存...");
                    //搜索不到条形码时的逻辑
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
                    if (getActivity() != null) {
                        commodityInfoListView.setLoadCompleted();
                    }
                    break;
                case 5:
                    ivSearch.performClick();
                    condition_input.setText("");
                    // 刷新EditText的焦点，使小键盘能正常弹出
                    condition_input.setFocusable(false);
                    condition_input.setFocusableInTouchMode(false);
                    condition_input.setFocusable(true);
                    condition_input.setFocusableInTouchMode(true);
                    break;
            }
        }
    };


    public void onKeyDownChild(int keyCode, KeyEvent event) {
        scanGun.isMaybeScanning(keyCode, event);
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
            closeLoadingDialog(loadingDailog);
            Toast.makeText(getActivity(), "网络故障，无法进行同步！", Toast.LENGTH_SHORT).show();
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
            closeLoadingDialog(loadingDailog);
            Toast.makeText(getActivity(), "网络故障，无法进行同步！", Toast.LENGTH_SHORT).show();
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
            closeLoadingDialog(loadingDailog);
            Toast.makeText(getActivity(), "网络故障，无法进行同步！", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            //浮动键盘设置
            DisplayCustomKeyBoard(condition_input,true,false);
        }
        return false;
    }
}
