package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class FontSizeSeeBarDialogController {

    @FXML
    public Slider slider;

    private JFXAlert alert;
    private SureClickListener listener;

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    @FXML
    private void cancel_click(){
        alert.close();
    }

    @FXML
    private void sure_click(){
        listener.onClick();
    }

    public void setListener(SureClickListener listener) {
        this.listener = listener;
    }

    interface SureClickListener{
        void onClick();
    }
}
