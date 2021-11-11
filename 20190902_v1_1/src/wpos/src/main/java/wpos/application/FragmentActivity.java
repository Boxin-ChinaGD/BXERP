package wpos.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;
import wpos.model.Intent;
import wpos.Main;
import wpos.StageController;
import wpos.allController.AllFragmentViewController;
import wpos.allEnum.StageType;

import javax.annotation.Resource;
import java.awt.*;

@Component("fragmentActivity")
public class FragmentActivity extends Application {
    public Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
    private Intent intent;

    @Resource
    private AllFragmentViewController allFragmentViewController;

    @Override
    public void init() throws Exception {
        super.init();
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/all_fragment.fxml"));
        loader.setController(allFragmentViewController);
        StackPane parent = loader.load();
        Scene scene = new Scene(parent, screenSize.width, screenSize.height);
        primaryStage.setScene(scene);

        //设置窗口没有放大按钮
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //全屏最大化，隐藏底部菜单栏
        primaryStage.setMaximized(true);

        //展示窗口
        StageController.get().addStage(StageType.FRAGMENT_STAGE.name(),primaryStage).showStage(StageType.FRAGMENT_STAGE.name());
        allFragmentViewController.setMainController();
        Main.startSyncThread();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public FragmentActivity(Intent intent){
        System.out.println("执行了");
        this.intent = intent;
    }

    public FragmentActivity(){}

    public static void startFramentActivity(Intent intent) {
        FragmentActivity fragmentActivity = new FragmentActivity(intent);
        try {
            fragmentActivity.start(new Stage());
        } catch (Exception e) {
            System.out.println("启动frament失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
