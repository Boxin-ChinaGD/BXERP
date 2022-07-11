package com.bx.erp.common;

import android.app.Activity;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.List;

public class ActivityController {
    private static Logger log = Logger.getLogger(ActivityController.class);
    public static List<Activity> mActivityList = new ArrayList<>();
    private static Activity mCurrentActivity;

    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void addActivity(Activity activity) {
        lock.writeLock().lock();

        mActivityList.add(activity);
        log.debug("当前添加的Activity是:" + activity.getClass().getSimpleName() + ";hashCode=" + activity.hashCode());
        for (Activity act : mActivityList) {
            log.debug("addActivity()此时ActivityList中有:" + act.getClass().getSimpleName() + ";hashCode=" + act.hashCode());
        }

        lock.writeLock().unlock();
    }

    public static void removeActivity(Activity activity) {
        lock.writeLock().lock();

        mActivityList.remove(activity);
        log.debug("当前删除的Activity是:" + activity.getClass().getSimpleName() + ";hashCode=" + activity.hashCode());
        for (Activity act : mActivityList) {
            log.debug("removeActivity()此时ActivityList中有:" + act.getClass().getSimpleName() + ";hashCode=" + act.hashCode());
        }

        lock.writeLock().unlock();
    }

    public static void setCurrentActivity(Activity activity) {
        lock.writeLock().lock();

        mCurrentActivity = activity;

        lock.writeLock().unlock();
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static void finishAll() {
        lock.writeLock().lock();

        for (Activity activity : mActivityList) {
            log.info("当前准备finish掉的Activity是" + activity.getClass().getSimpleName() + ";hashCode=" + activity.hashCode());
            if (!activity.isFinishing()) {
                try {
                    activity.finish();
                    log.debug("已将" + activity.getClass().getSimpleName() + activity.hashCode() + "finish()成功!!!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("finishAll()异常：" + ex.getMessage());
                }
            } else {
                log.debug(activity.getClass().getSimpleName() + activity.hashCode() + "未能finish()!!!");
            }
        }
        mActivityList.clear();

        lock.writeLock().unlock();
    }
}
