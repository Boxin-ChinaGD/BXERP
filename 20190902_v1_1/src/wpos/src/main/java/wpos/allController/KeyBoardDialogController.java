package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import wpos.listener.InputNumberDialogListener;

public class KeyBoardDialogController {

    keyBoardListener listener;
    private String value;
    private JFXAlert alert;

    @FXML
    private GridPane englishkeyboard;
    @FXML
    private GridPane numberkeyboard;
    @FXML
    private GridPane symbolkeyboard;

    //-----------------------英文键盘
    @FXML
    private void bq() {
        setInput("q");
    }

    @FXML
    private void bw() {
        setInput("w");
    }

    @FXML
    private void be() {
        setInput("e");
    }

    @FXML
    private void br() {
        setInput("r");
    }

    @FXML
    private void bt() {
        setInput("t");
    }

    @FXML
    private void by() {
        setInput("y");
    }

    @FXML
    private void bu() {
        setInput("u");
    }

    @FXML
    private void bi() {
        setInput("i");
    }

    @FXML
    private void bo() {
        setInput("o");
    }

    @FXML
    private void bp() {
        setInput("p");
    }

    @FXML
    private void ba() {
        setInput("a");
    }

    @FXML
    private void bs() {
        setInput("s");
    }

    @FXML
    private void bd() {
        setInput("d");
    }

    @FXML
    private void bf() {
        setInput("f");
    }

    @FXML
    private void bg() {
        setInput("g");
    }

    @FXML
    private void bh() {
        setInput("h");
    }

    @FXML
    private void bj() {
        setInput("j");
    }

    @FXML
    private void bk() {
        setInput("k");
    }

    @FXML
    private void bl() {
        setInput("l");
    }

    @FXML
    private void bz() {
        setInput("z");
    }

    @FXML
    private void bx() {
        setInput("x");
    }

    @FXML
    private void bc() {
        setInput("c");
    }

    @FXML
    private void bv() {
        setInput("v");
    }

    @FXML
    private void bb() {
        setInput("b");
    }

    @FXML
    private void bn() {
        setInput("n");
    }

    @FXML
    private void bm() {
        setInput("m");
    }

    @FXML
    private void bpoint() {
        setInput(".");
    }

    @FXML
    private void en_space() {
        setInput(" ");
    }

    @FXML
    private void en_num() {
        englishkeyboard.setVisible(false);
        englishkeyboard.setManaged(false);
        symbolkeyboard.setVisible(false);
        symbolkeyboard.setManaged(false);
        numberkeyboard.setVisible(true);
        numberkeyboard.setManaged(true);
    }

    @FXML
    private void en_symbol() {
        englishkeyboard.setVisible(false);
        englishkeyboard.setManaged(false);
        numberkeyboard.setVisible(false);
        numberkeyboard.setManaged(false);
        symbolkeyboard.setVisible(true);
        symbolkeyboard.setManaged(true);
    }

    @FXML
    private void en_sure() {
        alert.close();
    }

    @FXML
    private void en_delete() {
        if (value != null && !value.equals("")) {
            value = value.substring(0, value.length() - 1);
            if (listener != null) {
                listener.getKeyBoardKey(value);
            }
        }
    }

    //--------------------数字键盘

    @FXML
    private void b7() {
        setInput("7");
    }

    @FXML
    private void b8() {
        setInput("8");
    }

    @FXML
    private void b9() {
        setInput("9");
    }

    @FXML
    private void b4() {
        setInput("4");
    }

    @FXML
    private void b5() {
        setInput("5");
    }

    @FXML
    private void b6() {
        setInput("6");
    }

    @FXML
    private void b1() {
        setInput("1");
    }

    @FXML
    private void b2() {
        setInput("2");
    }

    @FXML
    private void b3() {
        setInput("3");
    }

    @FXML
    private void b0() {
        setInput("0");
    }

    @FXML
    private void num_point() {
        setInput(".");
    }

    @FXML
    private void num_space() {
        setInput(" ");
    }

    @FXML
    private void num_sure() {
        alert.close();
    }

    @FXML
    private void num_symbol() {
        englishkeyboard.setVisible(false);
        englishkeyboard.setManaged(false);
        numberkeyboard.setVisible(false);
        numberkeyboard.setManaged(false);
        symbolkeyboard.setVisible(true);
        symbolkeyboard.setManaged(true);
    }

    @FXML
    private void num_en() {
        englishkeyboard.setVisible(true);
        englishkeyboard.setManaged(true);
        numberkeyboard.setVisible(false);
        numberkeyboard.setManaged(false);
        symbolkeyboard.setVisible(false);
        symbolkeyboard.setManaged(false);
    }

    @FXML
    private void num_delete() {
        if (value != null && !value.equals("")) {
            value = value.substring(0, value.length() - 1);
            if (listener != null) {
                listener.getKeyBoardKey(value);
            }
        }
    }

    //--------------------符号键盘
    @FXML
    private void s1() {
        setInput(",");
    }

    @FXML
    private void s2() {
        setInput(".");
    }

    @FXML
    private void s3() {
        setInput("?");
    }

    @FXML
    private void s4() {
        setInput("!");
    }

    @FXML
    private void s5() {
        setInput(":");
    }

    @FXML
    private void s6() {
        setInput("/");
    }

    @FXML
    private void s7() {
        setInput("@");
    }

    @FXML
    private void s8() {
        setInput("...");
    }

    @FXML
    private void s9() {
        setInput("\"");
    }

    @FXML
    private void s10() {
        setInput(";");
    }

    @FXML
    private void s11() {
        setInput("'");
    }

    @FXML
    private void s12() {
        setInput("~");
    }

    @FXML
    private void s13() {
        setInput("+");
    }

    @FXML
    private void s14() {
        setInput("-");
    }

    @FXML
    private void s15() {
        setInput("=");
    }

    @FXML
    private void s16() {
        setInput("#");
    }

    @FXML
    private void s17() {
        setInput("$");
    }

    @FXML
    private void s18() {
        setInput("%");
    }

    @FXML
    private void s19() {
        setInput("&");
    }

    @FXML
    private void s20() {
        setInput("*");
    }

    @FXML
    private void symbol_space() {
        setInput(" ");
    }

    @FXML
    private void symbol_delete() {
        if (value != null && !value.equals("")) {
            value = value.substring(0, value.length() - 1);
            if (listener != null) {
                listener.getKeyBoardKey(value);
            }
        }
    }

    @FXML
    private void symbol_num() {
        englishkeyboard.setVisible(false);
        englishkeyboard.setManaged(false);
        numberkeyboard.setVisible(true);
        numberkeyboard.setManaged(true);
        symbolkeyboard.setVisible(false);
        symbolkeyboard.setManaged(false);
    }

    @FXML
    private void symbol_en() {
        englishkeyboard.setVisible(true);
        englishkeyboard.setManaged(true);
        numberkeyboard.setVisible(false);
        numberkeyboard.setManaged(false);
        symbolkeyboard.setVisible(false);
        symbolkeyboard.setManaged(false);
    }

    public void setListener(keyBoardListener listener) {
        this.listener = listener;
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    private void setInput(String s) {
        if (listener != null) {
            value += s;
            listener.getKeyBoardKey(value);
        }
    }

    interface keyBoardListener {
        void getKeyBoardKey(String value);
    }
}
