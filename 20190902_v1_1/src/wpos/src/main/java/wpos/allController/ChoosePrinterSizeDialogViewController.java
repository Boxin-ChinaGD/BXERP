package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;


public class ChoosePrinterSizeDialogViewController {
    @FXML
    public BorderPane select1;
    @FXML
    public BorderPane select2;
    @FXML
    public Label select1_tv;
    @FXML
    public ImageView selected1;
    @FXML
    public Label select2_tv;
    @FXML
    public ImageView selected2;
    @FXML
    public Label top;

    @FXML
    public Button cancel;

    private JFXAlert alert;

    @FXML   //取消按钮点击事件
    private void cancel_click() {
        alert.close();
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }
}
