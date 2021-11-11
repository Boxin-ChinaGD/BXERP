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
import wpos.allController.RetailTradeAggregationViewController;
import wpos.allEnum.StageType;

import javax.annotation.Resource;
import java.awt.*;

@Component("retailTradeAggregationActivity")
public class RetailTradeAggregationActivity extends Application {
    public Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
    private Intent intent;

    @Resource
    RetailTradeAggregationViewController retailTradeAggregationViewController;

    public RetailTradeAggregationActivity(){
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void start(Stage primaryStage) throws Exception {
        //交班页面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/retailTradeAggregation.fxml"));

        loader.setController(retailTradeAggregationViewController);
        BorderPane parent = loader.load();
        Scene scene = new Scene(parent, screenSize.width, screenSize.height);
        primaryStage.setScene(scene);

        //设置窗口没有关闭等按钮
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //全屏最大化，隐藏底部菜单栏
        primaryStage.setMaximized(true);
        //展示窗口
        StageController.get().addStage(StageType.RETAILTRADEAGGREGATION_STAGE.name(),primaryStage).showStage(StageType.RETAILTRADEAGGREGATION_STAGE.name());
        if (intent != null) {
            retailTradeAggregationViewController.onCreate(intent);
        }else {
            throw new NullPointerException("必须传递intent数据");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
