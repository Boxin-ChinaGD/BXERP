package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import wpos.listener.InputCompanySNDialogListener;

public class InputCompanySNDialogViewController {
    private JFXAlert alert;
    private InputCompanySNDialogListener listener;

    @FXML
    public TextField companySN;

    @FXML
    private void confirm(){
        String companySNString = companySN.getText();
        listener.inputCompanySNDialogListenerConfirmClick(companySNString);
    }


    public void cleanTextFiled(){
        companySN.setText("");
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setListener(InputCompanySNDialogListener listener) {
        this.listener = listener;
    }
}
