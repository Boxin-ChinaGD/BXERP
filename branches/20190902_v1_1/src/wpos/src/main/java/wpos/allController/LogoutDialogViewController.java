package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import wpos.StageController;
import wpos.allEnum.StageType;
import wpos.application.RetailTradeAggregationActivity;

public class LogoutDialogViewController {
    @FXML //取消按钮
    private Button cancel;
    @FXML //确定按钮
    private Button sure;

    private SureClickListener sureClickListener;
    private JFXAlert alert;

    @FXML   //确定按钮点击事件
    private void sure_click() {
        //关闭原先窗口，准备跳转到交班页面
        sureClickListener.click();
    }

    @FXML   //取消按钮点击事件
    private void cancel_click() {
        alert.close();
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setSureClickListener(SureClickListener sureClickListener) {
        this.sureClickListener = sureClickListener;
    }

    interface SureClickListener {
        void click();
    }
}
