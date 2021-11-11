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
import wpos.allController.PreSaleViewController;
import wpos.allEnum.StageType;

import javax.annotation.Resource;
import java.awt.*;

@Component("preSaleActivity")
public class PreSaleActivity extends Application {
    public Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();

    @Resource
    PreSaleViewController preSaleViewController;

    private Intent intent;

    @Override
    public void init() throws Exception {
        //窗体打开之前的一些初始化操作
        super.init();
    }

    public PreSaleActivity(){}

    public static void startPreSaleActivity(){
        PreSaleActivity preSaleActivity = new PreSaleActivity();
        try {
            preSaleActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        //获取窗口大小
        int proportionW = screenSize.width * 2 / 3;
        int proportionH = screenSize.height * 2 / 3;
        //登录页面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/preSale.fxml"));
        loader.setController(preSaleViewController);
        BorderPane parent = loader.load();
        Scene scene = new Scene(parent, proportionW, proportionH);
        primaryStage.setScene(scene);

        //设置窗口没有放大按钮
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //展示窗口
        StageController.get().addStage(StageType.PRESALE_STAGE.name(),primaryStage).showStage(StageType.PRESALE_STAGE.name());
        preSaleViewController.onCreate();
    }

    @Override
    public void stop() throws Exception {
        //窗体关闭
        System.out.println("窗体关闭");
        super.stop();
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
