package com.bx.erp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.bx.erp.common.GlobalController;
import com.bx.erp.di.components.ApplicationComponent;
import com.bx.erp.di.components.DaggerApplicationComponent;
import com.bx.erp.di.modules.ApplicationModule;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestScheduler;
import com.bx.erp.http.sync.SyncThread;
import com.bx.erp.utils.CrashHandler;
import com.bx.erp.utils.StethoUtils;
import com.bx.erp.utils.StringUtils;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

public class AppApplication extends Application {
    private static Logger logger = Logger.getLogger(AppApplication.class);

    protected HttpRequestScheduler hrs;
    protected SyncThread syncThread;

    private boolean isAidl;//打印机连接的部分
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        StethoUtils.init(this);
        if (BuildConfig.DEBUG) {
            // 在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
            // FIXME 之所以是在debug模式下才捕捉,是因为可能会使用其他第三方crash收集sdk
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }

        initEnv();

        initBaseUrl(); // 这个要在initializeInjector前调用,用于设置base url
        initializeInjector();

        //initLogDb();  // 可以通过init方式或者用dagger注入的方式初始化数据库 (todo ApplicationModule.java)

        /**
         * 打印机连接的部分
         */
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);

        GlobalController.init(getApplicationContext());

        //启动底层通信线程
        hrs = new HttpRequestScheduler();
        hrs.start();
        logger.info("底层通信线程已经启动");
    }

    private void initEnv() {
        String env = BuildConfig.env;
        //根据env来选择连接的服务器
        if (env.equals(Configuration.EnumEnv.DEV.getName())) {
            Configuration.currentRunningEnv = Configuration.EnumEnv.DEV;
            Configuration.HTTP_IP = Configuration.SERVER_IP_DEV;
            Configuration.bUseSandBox = false;
        } else if (env.equals(Configuration.EnumEnv.SIT.getName())) {
            Configuration.currentRunningEnv = Configuration.EnumEnv.SIT;
            Configuration.HTTP_IP = Configuration.SERVER_IP_SIT;
            Configuration.bUseSandBox = false;
        } else if (env.equals(Configuration.EnumEnv.UAT.getName())) {
            Configuration.currentRunningEnv = Configuration.EnumEnv.UAT;
            Configuration.HTTP_IP = Configuration.SERVER_IP_UAT;
            Configuration.bUseSandBox = false;
        } else if (env.equals(Configuration.EnumEnv.PROD.getName())) {
            Configuration.currentRunningEnv = Configuration.EnumEnv.PROD;
            Configuration.HTTP_IP = Configuration.SERVER_IP_PRODUCTION;
            Configuration.bUseSandBox = false;
        } else {
            Configuration.currentRunningEnv = Configuration.EnumEnv.DEV;
            //...连接的是本机
            Configuration.bUseSandBox = true;
            System.out.println("-------------配置文件中的env的value不符合要求-------------");
        }
    }

    /**
     * 启动同步线程。如果已经启动，就不重新启动。
     */
    public void startSyncThread() {
        if (syncThread == null) {
            syncThread = new SyncThread(10L);
            SyncThread.aiPause.set(SyncThread.RESUME);
//            SyncThread.aiPause.set(SyncThread.PAUSE);
            syncThread.start();
            logger.info("同步线程已经启动。线程在运行状态。");
        } else {
            logger.debug("同步线程已经在运行中，不需要再启动");
        }
    }

    /**
     * 什么时候退出？1、会话过期。2、交班
     */
    public void exitFromSyncThread() {
        logger.debug("准备关闭同步线程");
        if (syncThread != null) {
            //退出同步线程
            syncThread.exit();
            try {
                syncThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            syncThread = null;
            logger.info("同步线程已经结束");
        } else {
            System.out.println("同步线程为null，应该是之前已经退出过");
        }
    }

    @Override
    public void onTerminate() {
        exitFromSyncThread();

        //退出底层通信线程
        hrs.stop();
        logger.info("底层通信线程已经结束");

        super.onTerminate();
    }

    /**
     * 打印机连接的部分
     */
    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    private void initBaseUrl() {
        if (BuildConfig.DEBUG) {
            // TODO: 2018/6/24 由于是用dagger inject ,所以要先写入请求的域名.测试百度网站返回，正式开发时在gradle.properties配置，并去掉这里的if块。
            Constants.BASE_URL = "http://baidu.com";
            return;
        }
        SharedPreferences sp = getSharedPreferences(Constants.URL_FILE, Context.MODE_PRIVATE);
        String baseUrl = sp.getString(Constants.SPKey.KEY_BASE_URL, null);
        if (StringUtils.isTrimEmpty(baseUrl)) {
            Constants.BASE_URL = BuildConfig.DEBUG ? BuildConfig.DEBUG_DOMAIN_NAME : BuildConfig.RELEASE_DOMAIN_NAME;
            sp.edit().putString(Constants.SPKey.KEY_BASE_URL, Constants.BASE_URL).apply();
        } else {
            Constants.BASE_URL = baseUrl;
        }
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //private void initLogDb() {
    //  DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, COMMODITY_TABLE_NAME);
    //  Database db = helper.getWritableDb();
    //  daoSession = new DaoMaster(db).newSession();
    //}
    //
//  public DaoSession getDaoSession() {
//    return daoSession;
//  }

    /**
     * 获取本POS机的序列码(SN)。在功能代码中只读，不能写。
     */
    public static String getPOS_SN() {
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            logger.info("sunmi the sn:" + (String) get.invoke(c, "ro.serialno"));
            logger.info("sunmi First four characters:"
                    + (String) ((String) get.invoke(c, "ro.serialno")).substring(0, 4));

            return (String) get.invoke(c, "ro.serialno");
//            return "T203189A40261";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
