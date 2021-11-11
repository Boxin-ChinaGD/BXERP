package wpos.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Timer;
import java.util.TimerTask;

public class ToastUtil {
    private static Stage stage=new Stage();
    public static final long LENGTH_SHORT = 2000L;
    public static final long LENGTH_LONG = 3000L;
    static {
        stage.initStyle(StageStyle.TRANSPARENT);//舞台透明
    }
    //默认3秒
    public static void toast(String msg) {
        toast(msg,2000);
    }

    /**
     * 指定时间消失
     * @param msg
     * @param time
     */
    public static void toast(String msg, long time) {
        if (!stage.isShowing()){
            TimerTask task= new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> stage.close());
                }
            };
            init(msg);
            Timer timer=new Timer();
            timer.schedule(task,time);
            stage.show();
        }
    }

    //设置消息
    private static void init(String msg) {
        Label label=new Label(msg);//默认信息
        label.setStyle("-fx-background: rgba(56,56,56,0.7);-fx-border-radius: 25;-fx-background-radius: 25");//label透明,圆角
        label.setTextFill(Color.rgb(225,255,226));//消息字体颜色
        label.setPrefHeight(50);
        label.setPadding(new Insets(15));
        label.setAlignment(Pos.BOTTOM_CENTER);//底部居中
        label.setFont(new Font(20));//字体大小
        Scene scene=new Scene(label);
        scene.setFill(null);//场景透明
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
    }
}
