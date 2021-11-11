package wpos.common;

import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.List;

public class StageController {
    private static Log log = LogFactory.getLog(StageController.class);
    public static List<Stage> mStageList = new ArrayList<Stage>();
    private static Stage mCurrentStage;

    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void addStage(Stage stage) {
        lock.writeLock().lock();

        mStageList.add(stage);
        log.debug("当前添加的Stage是:" + stage.getClass().getSimpleName() + ";hashCode=" + stage.hashCode());
        for (Stage act : mStageList) {
            log.debug("addStage()此时StageList中有:" + act.getClass().getSimpleName() + ";hashCode=" + act.hashCode());
        }

        lock.writeLock().unlock();
    }

    public static void removeStage(Stage stage) {
        lock.writeLock().lock();

        mStageList.remove(stage);
        log.debug("当前删除的Stage是:" + stage.getClass().getSimpleName() + ";hashCode=" + stage.hashCode());
        for (Stage act : mStageList) {
            log.debug("removeStage()此时StageList中有:" + act.getClass().getSimpleName() + ";hashCode=" + act.hashCode());
        }

        lock.writeLock().unlock();
    }

    public static void setCurrentStage(Stage stage) {
        lock.writeLock().lock();

        mCurrentStage = stage;

        lock.writeLock().unlock();
    }

    public static Stage getCurrentStage() {
        return mCurrentStage;
    }

    public static void finishAll() {
        lock.writeLock().lock();

        for (Stage stage : mStageList) {
            log.info("当前准备finish掉的Stage是" + stage.getClass().getSimpleName() + ";hashCode=" + stage.hashCode());
//            if (!stage.isFinishing()) {
//                try {
//                    stage.finish();
//                    log.debug("已将" + stage.getClass().getSimpleName() + stage.hashCode() + "finish()成功!!!");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    log.error("finishAll()异常：" + ex.getMessage());
//                }
//            } else {
//                log.debug(stage.getClass().getSimpleName() + stage.hashCode() + "未能finish()!!!");
//            }
        }
        mStageList.clear();

        lock.writeLock().unlock();
    }
}
