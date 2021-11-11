package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import wpos.listener.InputNumberDialogListener;
import wpos.utils.ToastUtil;

public class InputNumberDialogViewController {

    // 售卖商品列表中数量的默认最小值
    private final String Default_CommodityMinNumber = "1";
    // 售卖商品列表中数量的默认最大值
    private final int Default_CommodityMaxNumber = 9999;
    private JFXAlert alert;
    private InputNumberDialogListener inputNumberDialogListener;
    @FXML
    private TextField inputNum;

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
        //将焦点移动至输入框
        inputNum.requestFocus();
    }

    @FXML   //输入数量页面的确定按钮
    private void sure_click() {
        try {
            Integer.valueOf(inputNum.getText());
        } catch (Exception e) {
            ToastUtil.toast("请输入正整数");
            inputNum.setText(Default_CommodityMinNumber);
            return;
        }
        if ("".equals(inputNum.getText()) || Integer.parseInt(inputNum.getText()) <= 0) { // 用户不能输入1个或多个空格
            ToastUtil.toast("请输入正整数");
            inputNum.setText(Default_CommodityMinNumber);
            return;
        }
        if (Integer.parseInt(inputNum.getText()) > Default_CommodityMaxNumber) {
            ToastUtil.toast("售卖商品数量最大值为" + Default_CommodityMaxNumber);
            inputNum.setText(String.valueOf(Default_CommodityMaxNumber));
            return;
        }

        if (!"".equals(inputNum.getText()) && Integer.parseInt(inputNum.getText()) > 0) {
            if (inputNumberDialogListener != null) inputNumberDialogListener.inputNum(inputNum.getText());
            alert.close();
        } else {
            ToastUtil.toast("需要填写大于0的商品数量");
        }
    }

    @FXML   //输入数量页面的取消按钮
    private void cancel_click() {
        alert.close();
    }

    public void setInputNumberDialogListener(InputNumberDialogListener inputNumberDialogListener) {
        this.inputNumberDialogListener = inputNumberDialogListener;
    }

    @FXML
    private void key_delete() {
        if (inputNum.getText() != null && !inputNum.getText().equals("")) {
            inputNum.setText(inputNum.getText().substring(0, inputNum.getText().length() - 1));
        }
    }

    @FXML
    private void key0() {
        inputNum.setText(inputNum.getText() + "0");
    }

    @FXML
    private void key1() {
        inputNum.setText(inputNum.getText() + "1");
    }

    @FXML
    private void key2() {
        inputNum.setText(inputNum.getText() + "2");
    }

    @FXML
    private void key3() {
        inputNum.setText(inputNum.getText() + "3");
    }

    @FXML
    private void key4() {
        inputNum.setText(inputNum.getText() + "4");
    }

    @FXML
    private void key5() {
        inputNum.setText(inputNum.getText() + "5");
    }

    @FXML
    private void key6() {
        inputNum.setText(inputNum.getText() + "6");
    }

    @FXML
    private void key7() {
        inputNum.setText(inputNum.getText() + "7");
    }

    @FXML
    private void key8() {
        inputNum.setText(inputNum.getText() + "8");
    }

    @FXML
    private void key9() {
        inputNum.setText(inputNum.getText() + "9");
    }

}
