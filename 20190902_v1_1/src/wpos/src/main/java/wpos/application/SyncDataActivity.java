package wpos.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;
import wpos.model.Intent;
import wpos.StageController;
import wpos.allController.SyncDataViewController;
import wpos.allEnum.StageType;

import javax.annotation.Resource;
import java.awt.*;

@Component("syncDataActivity")
public class SyncDataActivity extends Application {

    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Intent intent;
    @Resource
    SyncDataViewController syncDataViewController;

    @Override
    public void init() throws Exception {
        //窗体打开之前的一些初始化操作
        super.init();
    }

    public SyncDataActivity() {
    }

    public SyncDataActivity(Intent intent) {
        System.out.println("执行了");
        this.intent = intent;
    }

    public static void startSyncData() {
        SyncDataActivity syncDataActivity = new SyncDataActivity();
        try {
            syncDataActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        //同步页面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/syncdata.fxml"));

        loader.setController(syncDataViewController);
        BorderPane parent = loader.load();
        Scene scene = new Scene(parent, screenSize.width, screenSize.height);
        primaryStage.setScene(scene);

        //设置窗口没有放大按钮
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //全屏最大化，隐藏底部菜单栏
        primaryStage.setMaximized(true);
        //展示窗口
        StageController.get().addStage(StageType.SYNCDATA_STAGE.name(), primaryStage).showStage(StageType.SYNCDATA_STAGE.name());
        syncDataViewController.init();
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
