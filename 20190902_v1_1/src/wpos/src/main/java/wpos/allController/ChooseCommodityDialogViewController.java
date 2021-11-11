package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wpos.model.Commodity;

public class ChooseCommodityDialogViewController {
    private final Log log = LogFactory.getLog(this.getClass());

    //输入条形码的对话框
    private JFXAlert alert;
    @FXML   //库存的条目
    private Label tv_inventory_search;
    @FXML   //输入框
    public TextField search_commodity_et;
    @FXML   //删除按钮
    private FlowPane ivDelete_all;
    @FXML   //列表
    public ListView<Commodity> rv_alertdialog_commodity;


    private TextChangeListener textChangeListener;
    private DeleteAndCancelListener deleteAndCancelListener;

    @FXML
    private void searchCommodityEt_click() {
        //todo 对接键盘
    }

    @FXML   //返回按钮点击事件
    private void cancel_click() {
        log.debug("点击取消按钮");
        alert.close();
        if (deleteAndCancelListener != null) {
            deleteAndCancelListener.cancel();
        }
    }

    @FXML   //删除文本框的字
    private void deleteAll_click() {
        search_commodity_et.setText("");
        if (deleteAndCancelListener != null) {
            deleteAndCancelListener.deleteAll();
        }
    }

    @FXML   //加入按钮
    private void add_click() {
        log.debug("增加商品");
        if (deleteAndCancelListener != null) {
            deleteAndCancelListener.add();
        }
        alert.close();
    }

    public ChooseCommodityDialogViewController() {

    }

    public void setAlert(JFXAlert chooseCommodityDialog) {
        this.alert = chooseCommodityDialog;
        tv_inventory_search.setManaged(false);
        tv_inventory_search.setVisible(false);
    }

    public void requestFocus(){
        search_commodity_et.requestFocus();
    }

    //该方法必须在注册监听之后调用，否则可能会导致第一次输入失效
    public void setSearchCommodityEt(String scan_barcode_text) {
        //输入框文本变化监听
        search_commodity_et.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("".equals(search_commodity_et.getText())) {
                    ivDelete_all.setVisible(false);
                } else if (!"".equals(search_commodity_et.getText())) {
                    ivDelete_all.setVisible(true);
                }

                String commodityBarcodes = search_commodity_et.getText();
                if (commodityBarcodes.length() > BaseController.FUZZY_QUERY_LENGTH) {
                    if (textChangeListener != null) {
                        textChangeListener.textChanged(commodityBarcodes);
                    }
                }
            }
        });
        search_commodity_et.setText(scan_barcode_text);
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    public void setDeleteAndCancelListener(DeleteAndCancelListener deleteAndCancelListener) {
        this.deleteAndCancelListener = deleteAndCancelListener;
    }

    interface TextChangeListener {
        void textChanged(String commodityBarcodes);
    }

    interface DeleteAndCancelListener {
        void deleteAll();

        void cancel();

        void add();
    }
}
