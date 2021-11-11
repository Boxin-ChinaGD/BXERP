package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import wpos.listener.resetPasswordWaringDialogListener;

public class ResetPasswordWaringDialogViewController {

    private JFXAlert alert;
    private resetPasswordWaringDialogListener listener;

    @FXML
    private void sure_click(){
        listener.resetPasswordWaringDialogComfirmClick();
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setListener(resetPasswordWaringDialogListener listener) {
        this.listener = listener;
    }
}
