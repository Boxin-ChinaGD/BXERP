package wpos.allController;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.adapter.DialogBillRecyclerViewAdapter;
import wpos.adapter.DialogBillRetailTradeRecyclerViewAdapter;
import wpos.adapter.DialogCheckListOrderListCellAdapter;
import wpos.listener.PlatFormHandlerMessage;
import wpos.model.*;
import wpos.utils.EventBus;
import wpos.utils.PlatForm;
import wpos.utils.ToastUtil;

import java.util.List;

@Component("billTradeController")
public class BillTradeController implements PlatFormHandlerMessage {
    private static Logger log = Logger.getLogger(BillTradeController.class);

    AllFragmentViewController viewController;

    /**
     * 挂单列表上的项目，点击可以选中，但再点击一次或多次不能取消选中，所以需要本flag标记是否选中了一个零售单
     */
    private boolean isSelected = false;
    private int isSelectNum;
    private List<RetailTrade> retrieveNRetailTradeList;

    public BillTradeController() {
    }

    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
    }

    public void showRetailTrade() {
        isSelected = false;
        retrieveNRetailTradeList = BaseController.retailTradeHoldBillList;
        //
        for (int i = 0; i < retrieveNRetailTradeList.size(); i++) {
            retrieveNRetailTradeList.get(i).setNumber(i);
        }
        viewController.rv_retailtrade.setItems(null);
        viewController.rv_retailtrade.setItems(FXCollections.observableList(retrieveNRetailTradeList));
        viewController.rv_retailtrade.setCellFactory(new Callback() {
            @Override
            public Object call(Object e) {
                return initBillAdapter();
            }
        });

        viewController.rv_commodity.setItems(null);
        viewController.rv_commodity.setCellFactory(new Callback<ListView<Commodity>, ListCell<Commodity>>() {
            @Override
            public ListCell<Commodity> call(ListView<Commodity> param) {
                DialogBillRecyclerViewAdapter dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter();
                return dialogBillRecyclerViewAdapter;
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {

    }

    public void delete_click() {
        if (!isSelected) {
            ToastUtil.toast("请先选择要操作的零售单", ToastUtil.LENGTH_SHORT);
        } else {
            isSelected = false;
            BaseController.retailTradeHoldBillList.remove(isSelectNum);
            //
            viewController.rv_retailtrade.setItems(null);
            viewController.rv_retailtrade.setItems(FXCollections.observableList(BaseController.retailTradeHoldBillList));
            viewController.rv_retailtrade.setCellFactory(new Callback() {
                @Override
                public Object call(Object e) {
                    return initBillAdapter();
                }
            });
            //
            viewController.rv_commodity.setItems(null);
            viewController.rv_commodity.setCellFactory(new Callback() {
                @Override
                public Object call(Object e) {
                    return new DialogCheckListOrderListCellAdapter();
                }
            });
        }
    }

    public void continueRetail_click() {
        if (!isSelected) {
            ToastUtil.toast("请先选择要操作的零售单", ToastUtil.LENGTH_SHORT);
        } else {
            isSelected = false;
            if (BaseController.retailTrade != null && BaseController.retailTrade.getListSlave1() != null && BaseController.retailTrade.getListSlave1().size() > 0) {
                ToastUtil.toast("收银界面存在未结零售单，请先处理", ToastUtil.LENGTH_SHORT);
            } else {
                BaseController.retailTrade = BaseController.retailTradeHoldBillList.get(isSelectNum);
                BaseController.showCommList = BaseController.retailTrade.getCommodityListOfHeldBill();
                BaseController.retailTradeHoldBillList.remove(isSelectNum);
                //
            }
            viewController.saleItem_click();
        }
    }

    public DialogBillRetailTradeRecyclerViewAdapter initBillAdapter() {
        DialogBillRetailTradeRecyclerViewAdapter dialogBillRetailTradeRecyclerViewAdapter = new DialogBillRetailTradeRecyclerViewAdapter();
        dialogBillRetailTradeRecyclerViewAdapter.setOnLickListener(new DialogBillRetailTradeRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onLeftClick(RetailTrade newValue) {
//                        if (BaseController.retailTradeHoldBillList.get(newValue.getNumber()).isSelect) {
//                            // 和APOS不同之处在于不能反选，故APOS的这个代码不能在WPOS这边重用
//                        } else {
//                        isSelect = false;
//                        BaseController.retailTradeHoldBillList.get(newValue.getNumber()).isSelect = true;
                isSelectNum = newValue.getNumber();
                isSelected = true;
                //
                for (int i = 0; i < BaseController.retailTradeHoldBillList.get(newValue.getNumber()).getCommodityListOfHeldBill().size(); i++) {
                    BaseController.retailTradeHoldBillList.get(newValue.getNumber()).getCommodityListOfHeldBill().get(i).setOrderNumber(i + 1);
                }
                viewController.rv_commodity.setItems(null);
                viewController.rv_commodity.setItems(FXCollections.observableList(BaseController.retailTradeHoldBillList.get(newValue.getNumber()).getCommodityListOfHeldBill()));
                viewController.rv_commodity.setCellFactory(new Callback<ListView<Commodity>, ListCell<Commodity>>() {
                    @Override
                    public ListCell<Commodity> call(ListView<Commodity> param) {
                        DialogBillRecyclerViewAdapter dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter();
                        return dialogBillRecyclerViewAdapter;
                    }
                });
            }
//                    }
        });
        return dialogBillRetailTradeRecyclerViewAdapter;

    }

}
