package wpos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import wpos.allController.BaseController;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 本类用来控制所有的Stage
 */

public class StageController {
    private Map<String, Stage> stages = new HashMap<String, Stage>();
    private Map<String, BaseController> controllers = new HashMap<String, BaseController>();
    private Map<String, Scene> scene = new HashMap<String, Scene>();
    private Stage showingStage;

    private static StageController stageController;

    public static StageController get(){
        if (stageController == null) {
            synchronized (StageController.class) {
                if (stageController == null)
                    stageController = new StageController();
            }
        }
        return stageController;
    }

    private StageController() {}

    //将stage添加进来
    public StageController addStage(String name, Stage stage) {
        this.stages.put(name, stage);
        return stageController;
    }

    //根据名字拿到Stage
    public Stage getStageBy(String name) {
        return this.stages.get(name);
    }

    //显示stage调用此方法
    public Stage showStage(String name) {
        Stage stage = this.getStageBy(name);
        if (stage == null) {
            return null;
        }
        stage.show();
        showingStage = stage;
        return stage;
    }

    public Stage getShowingStage() {
        return showingStage;
    }

    //切换stage调用此方法
    public boolean switchStage(String toShow, String toClose) {
        getStageBy(toClose).close();
        showStage(toShow);
        return true;
    }

    //关闭stage调用此方法
    public StageController closeStge(String name) {
        Stage target = getStageBy(name);
        target.close();
        return stageController;
    }

    public void closeAllstages() {
        for(Stage stage : stages.values()) {
            stage.close();
        }
        stages.clear();
    }

    //关闭stage后,如果不再需要该stage，应当调用此方法移除stage
    public boolean unloadStage(String name) {
        return this.stages.remove(name) != null;
    }

    //获取controller
    public BaseController getController(String name) {
        return this.controllers.get(name);
    }

    //添加controller
    public Stage loadStage(String name, String resource, StageStyle... styles) {
        Stage result = null;
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setResources(ResourceBundle.getBundle("i18n/message"));
            Pane tmpPane = loader.load();
            BaseController controlledStage = (BaseController) loader.getController();
            this.controllers.put(name, controlledStage);
            Scene tmpScene = new Scene(tmpPane);
            result = new Stage();
            result.setScene(tmpScene);

            for (StageStyle style : styles) {
                result.initStyle(style);
            }
            this.addStage(name, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String resource, Class<T> clazz) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(url);
            return (T) loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String resource, Class<T> clazz, ResourceBundle resources) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            return (T) FXMLLoader.load(url, resources);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addScene(String name,Scene scene){
        this.scene.put(name, scene);
    }

    public Scene getSceneBy(String name){
       return this.scene.get(name);
    }

}