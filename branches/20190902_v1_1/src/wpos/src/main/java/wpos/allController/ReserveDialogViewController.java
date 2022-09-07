package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReserveDialogViewController {

    @FXML
    private TextField inputNum;

    private JFXAlert alert;
    private ButtonClickListener listener;

    private String add_number = "";
    private boolean lastClickIsRMB = false;

    private static Pattern pDecimals = Pattern.compile("^([0-9][0-9]*)+(\\.[0-9]{1,2})?$");// 最多带2位小数的数字

    public static boolean checkReserve(String reserve) {
        if (reserve == null) {
            return false;
        }
        Matcher m = pDecimals.matcher(String.valueOf(reserve));
        return m.matches();
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    @FXML
    private void key_delete() {
        if (add_number != null && !add_number.equals("")) {
            add_number = add_number.substring(0, add_number.length() - 1);
            inputNum.setText(add_number);
        }
    }

    @FXML
    private void key0() {
        ClickNum("0");
    }

    @FXML
    private void key1() {
        ClickNum("1");
    }

    @FXML
    private void key2() {
        ClickNum("2");
    }

    @FXML
    private void key3() {
        ClickNum("3");
    }

    @FXML
    private void key4() {
        ClickNum("4");
    }

    @FXML
    private void key5() {
        ClickNum("5");
    }

    @FXML
    private void key6() {
        ClickNum("6");
    }

    @FXML
    private void key7() {
        ClickNum("7");
    }

    @FXML
    private void key8() {
        ClickNum("8");
    }

    @FXML
    private void key9() {
        ClickNum("9");
    }

    @FXML
    private void key10() {
        ClickRMB("10");
    }

    @FXML
    private void key20() {
        ClickRMB("20");
    }

    @FXML
    private void key50() {
        ClickRMB("50");
    }

    @FXML
    private void key100() {
        ClickRMB("100");
    }

    @FXML
    private void key_point() {
        if (!add_number.equals("")){
            if (!add_number.contains(".")){
                setNumber(".");
            }
        }
    }

    @FXML   //准备金页面的确定按钮
    private void sure_click() {
        add_number = inputNum.getText();
        listener.onClick(add_number);
    }

    //点击键盘人民币时
    private void ClickRMB(String RMB) {
        add_number = inputNum.getText();
        if (add_number.equals("")) {
            setNumber(RMB);
        } else {
            add_number = "";
            inputNum.setText(add_number);
            setNumber(RMB);
        }
        lastClickIsRMB = true;
    }

    //点击键盘数字时
    private void ClickNum(String Num) {
        if (lastClickIsRMB) {
            add_number = "";
            setNumber(Num);
            lastClickIsRMB = false;
        } else {
            setNumber(Num);
        }
    }

    private void setNumber(String s) {
        add_number = inputNum.getText();
        //限制：第一位为0的时候，后面只能输入小数点
        if (!"0".equals(add_number)) {
            //限制只能输两位小数
            if (add_number.indexOf(".") == -1
                    || (add_number.indexOf(".") > -1
                    && (add_number.length() - add_number.indexOf(".")) < 3)) {
                add_number += s;
            }
        } else if (".".equals(s)) {
            if (add_number.indexOf(".") == -1 || (add_number.indexOf(".") > -1 && (add_number.length() - add_number.indexOf(".")) < 3)) {    //没有满足这个条件.查看为什么需要这个条件
                add_number += s;
            }

        }
        inputNum.setText(add_number);
    }

    public void setListener(ButtonClickListener listener) {
        this.listener = listener;
    }


    interface ButtonClickListener {
        void onClick(String s);
    }

}
