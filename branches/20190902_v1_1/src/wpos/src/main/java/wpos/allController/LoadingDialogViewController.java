package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingDialogViewController {
    private JFXAlert alert;
    Timer timer;
    TimerTask timerTask;
    private int i = 0;

    @FXML
    ImageView image;

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void startAnimotion() {
//        i = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (i > 360) {
                    i = 0;
                }
//                image.setRotate(i);
                i += 5;
                Platform.runLater(() -> {
                    image.setRotate(i);
                });
            }
        };
        timer.schedule(timerTask, 5, 100);
    }

    public void endAnimotion() {
        if (timer != null) {
            timerTask.cancel();
            timer.cancel();
            timer.purge();
            timer = null;
            i = 0;
        }
    }
}
