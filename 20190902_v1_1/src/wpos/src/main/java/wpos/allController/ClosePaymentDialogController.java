package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;

public class ClosePaymentDialogController {

    private JFXAlert alert;
    private ConfirmClickListener listener;

    @FXML
    private void cancel_click(){
        alert.close();
    }

    @FXML
    private void sure_click(){
        if (listener!=null){
            listener.onConfirmClick();
        }
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setListener(ConfirmClickListener listener) {
        this.listener = listener;
    }

    interface ConfirmClickListener {
        void onConfirmClick();
    }
}
