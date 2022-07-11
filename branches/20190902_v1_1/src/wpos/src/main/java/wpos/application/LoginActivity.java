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
import wpos.allController.LoginViewController;
import wpos.allEnum.StageType;

import javax.annotation.Resource;
import java.awt.*;

@Component("loginActivity")
public class LoginActivity extends Application {
    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Intent intent;

    public LoginActivity() {
    }

    @Resource
    LoginViewController loginViewController;

    @Override
    public void init() throws Exception {
        //窗体打开之前的一些初始化操作
        super.init();
    }

    public LoginActivity(Intent intent) {
        System.out.println("执行了");
        this.intent = intent;
    }


//    public static void startLoginActivity(Intent intent) {
//        loginActivity.setIntent(intent);
//        try {
//            loginActivity.start(new Stage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    @Override
    public void start(Stage primaryStage) throws Exception {
        int proportionW = screenSize.width * 2 / 3;
        int proportionH = screenSize.height * 2 / 3;
        //登录页面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));

//        LoginViewController loginViewController = new LoginViewController(intent);
        loginViewController.setIntent(intent);
        loader.setController(loginViewController);
        BorderPane parent = loader.load();
        Scene scene = new Scene(parent, proportionW, proportionH);
        primaryStage.setScene(scene);

        //设置窗口没有放大按钮
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //展示窗口
        StageController.get().addStage(StageType.LOGIN_STAGE.name(), primaryStage).showStage(StageType.LOGIN_STAGE.name());
        loginViewController.register();
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

    //    public static void main(String[] args) {
//        launch(args);
//    }
}
