package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class DaySpanLoadingDialogViewController {
    @FXML
    public Label daySpanCountDown;
    private JFXAlert alert;
    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }
}
