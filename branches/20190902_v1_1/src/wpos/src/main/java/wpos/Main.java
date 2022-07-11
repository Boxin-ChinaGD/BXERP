package wpos;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.internal.SessionImpl;
import org.ini4j.Ini;
import org.ini4j.Wini;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wpos.application.LoginActivity;
import wpos.bo.BaseSQLiteBO;
import wpos.common.GlobalController;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestScheduler;
import wpos.http.sync.SyncThread;
import wpos.model.BaseModel;
import wpos.model.Intent;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Component("main")
public class Main extends Application {
    public List<String> classNames = new ArrayList<String>();
    private final Log logger = LogFactory.getLog(this.getClass());

    protected HttpRequestScheduler hrs;
    protected static SyncThread syncThread;
    private boolean isAidl;//打印机连接的部分
//    private ApplicationComponent applicationComponent;

    public LoginActivity loginActivity;

    @Override
    public void init() throws Exception {
        onAppStart();
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 设置jpos的classPath路径
        loadIni();
        // TODO 检查版本，强制升级
        //...
        onUpgrade();
        // TODO 创建所有表
        // ...
        if (!createTables()) {
            // alert "创建数据表失败，请联系客服。"
            return;
        }
        loginActivity.setIntent(new Intent());
        loginActivity.start(new Stage());
//        LoginActivity.startLoginActivity(new Intent());
    }

    private void loadIni() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        Wini ini = new Wini(new File("D:/wpos/setting.ini"));
        Ini.Section section = ini.get("Jpos");
        String epsonjpos = section.get("epsonjpos");
        String jpos1141 = section.get("jpos1141");
        String xercesImpl = section.get("xercesImpl");
        String xmlApis = section.get("xmlApis");
        URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        addURL.setAccessible(true);
        File file = new File(epsonjpos);
        addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
        file = new File(jpos1141);
        addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
        file = new File(xercesImpl);
        addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
        file = new File(xmlApis);
        addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
    }

    private static void loadJar(String jarPath) throws MalformedURLException {
        File jarFile = new File(jarPath); // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹

        if (jarFile.exists() == false) {
            System.out.println("jar file not found.");
            return;
        }

        //获取类加载器的addURL方法，准备动态调用
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }

        // 获取方法的访问权限，保存原始值
        boolean accessible = method.isAccessible();
        try {
            //修改访问权限为可写
            if (accessible == false) {
                method.setAccessible(true);
            }

            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

            //获取jar文件的url路径
            java.net.URL url = jarFile.toURI().toURL();

            //jar路径加入到系统url路径里
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //回写访问权限
            method.setAccessible(accessible);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        onAppStop();
        super.stop();
    }

    private void onAppStart() {
        initEnv();

        initBaseUrl(); // 这个要在initializeInjector前调用,用于设置base url
//        initializeInjector();

        /**
         * 打印机连接的部分
         */
//        isAidl = true;
//        AidlUtil.getInstance().connectPrinterService(this);

        GlobalController.init();

        //启动底层通信线程
        hrs = new HttpRequestScheduler();
        hrs.start();
        logger.info("底层通信线程已经启动");
    }

    private void initEnv() {
        String env = Constants.env;
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

    private void initBaseUrl() {
        // TODO: 2020/6/11  BuildConfig在本项目中应该对应什么
//        if (BuildConfig.DEBUG) {
//            // TODO: 2018/6/24 由于是用dagger inject ,所以要先写入请求的域名.测试百度网站返回，正式开发时在gradle.properties配置，并去掉这里的if块。
//            Constants.BASE_URL = "http://baidu.com";
//            return;
//        }
//        SharedPreferences sp = getSharedPreferences(Constants.URL_FILE, Context.MODE_PRIVATE);
//        String baseUrl = sp.getString(Constants.SPKey.KEY_BASE_URL, null);
//        if (StringUtils.isTrimEmpty(baseUrl)) {
//            Constants.BASE_URL = BuildConfig.DEBUG ? BuildConfig.DEBUG_DOMAIN_NAME : BuildConfig.RELEASE_DOMAIN_NAME;
//            sp.edit().putString(Constants.SPKey.KEY_BASE_URL, Constants.BASE_URL).apply();
//        } else {
//            Constants.BASE_URL = baseUrl;
//        }
    }

    /**
     * 启动同步线程。如果已经启动，就不重新启动。
     */
    public static void startSyncThread() {
        if (syncThread == null) {
//            syncThread = new SyncThread(10L);
//            SyncThread.aiPause.set(SyncThread.RESUME);
////            SyncThread.aiPause.set(SyncThread.PAUSE);
//            syncThread.start();
//            logger.info("同步线程已经启动。线程在运行状态。");
            System.out.println("从容器中获取同步线程");
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
            syncThread = (SyncThread) context.getBean("syncThread");
        } else {
//            logger.debug("同步线程已经在运行中，不需要再启动");
        }
        syncThread.init();
        syncThread.aiPause.set(SyncThread.RESUME);
        System.out.println("启动同步线程");
        syncThread.start();
    }

    /**
     * 什么时候退出？1、会话过期。2、交班
     */
    public static void exitFromSyncThread() {
        System.out.println("准备关闭同步线程");
        if (syncThread != null) {
            //退出同步线程
            syncThread.exit();
            try {
                syncThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            syncThread = null;
            System.out.println("同步线程已经结束");
        } else {
            System.out.println("同步线程为null，应该是之前已经退出过");
        }
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

    /**
     * 获取本POS机的序列码(SN)。在功能代码中只读，不能写。
     */
    public String getPOS_SN() {
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            logger.info("sunmi the sn:" + get.invoke(c, "ro.serialno"));
            logger.info("sunmi First four characters:"
                    + ((String) get.invoke(c, "ro.serialno")).substring(0, 4));

            return (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void onAppStop() {
        exitFromSyncThread();

        //退出底层通信线程
        hrs.stop();
        logger.info("底层通信线程已经结束");
    }

    /**
     * 创建表。如果表已经存在，不会重复创建
     * TODO：职责不分明
     */
    private boolean createTables() throws SQLException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        EntityManagerFactory emf = (EntityManagerFactory) context.getBean("entityManagerFactory");
        loginActivity = (LoginActivity) context.getBean("loginActivity");
        syncThread = (SyncThread) context.getBean("syncThread");
        ResultSet rs = null;
        EntityManager entityManager = null;
        Connection connection = null;
        // 通过包路径。反射获取所有的model。查看model是否有@Entity注解。如果有则说明该model需要有数据库表,并检查SQLite是否存在此表，无此表则创建该表。
        try {
            // 获取到所有的modelName
            doScanner("wpos.model");

            // 实例化model并检查是否需要创建表。
            Map<String, BaseModel> map = doInstance();
            if (map == null) {
                return false;
            }

            entityManager = emf.createEntityManager();
            connection = entityManager.unwrap(SessionImpl.class).connection();
            Iterator<Map.Entry<String, BaseModel>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                DatabaseMetaData metaData = connection.getMetaData();

                Map.Entry<String, BaseModel> model = it.next();
                // 获取到这些model的BO,检查是否已经存在表，如果不存在则创建。
                rs = metaData.getTables(connection.getCatalog(), "", model.getKey(), new String[]{"TABLE"});
                if (!rs.next()) {
                    logger.info("准备往数据库中创建" + model.getKey() + "表");
                    // 获取相关model的BO.调用创建表的接口
                    String sqliteBoName = StringUtils.lowerFirstLetter(model.getValue().getClass().getSimpleName()) + "SQLiteBO";
                    BaseSQLiteBO baseSQLiteBO = (BaseSQLiteBO) context.getBean(sqliteBoName);
                    baseSQLiteBO.createTableSync();
                    // 检查表是否创建成功
                    rs = metaData.getTables(connection.getCatalog(), "", model.getKey(), new String[]{"TABLE"});
                    if (!rs.next()) {
                        logger.error("创建" + model.getKey() + "表失败");
                        rs.close();
//                        connection.close();
//                        entityManager.close();
                        return false;
                    } else {
                        logger.info("创建" + model.getKey() + "表成功");
                    }
                } else {
                    logger.info(model.getKey() + "表已存在数据库中,并不需要再进行创建。");
                }
                rs.close();
//                connection.close();
//                entityManager.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            rs.close();
//            connection.close();
//            entityManager.close();
            logger.error("createTables时发生异常。errmsg=" + e.getMessage());
            return false;
        } finally {
//            emf.close();
//            context.close();
            classNames.clear();
        }
        return true;
    }

    /**
     * 1.获取ini文件中的版本信息
     * 2.比较是否和当前版本一致
     * 3.当版本不一致时，当前的版本自增1，然后检查是否需要更新表结构，有则更新，无则跳过
     */
    private void onUpgrade() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        Wini ini = new Wini(new File("D:/wpos/upgrade.ini"));
        Ini.Section section = ini.get("CurrentVersion");
        /* .ini文件内容格式：
        [CurrentVersion]
        ### 当前wpos的版本号
        CurrentVersionSN = 2.0.9
        CurrentVersionNO = 9
         */
        int currentVersionNO = Integer.valueOf(section.get("CurrentVersionNO"));
        while (currentVersionNO < Constants.VersionCode) {
            currentVersionNO++;
            switch (currentVersionNO) {
//                case 9:
//                    TempMapper tempMapper = (TempMapper) context.getBean("tempMapper");
//                    tempMapper.createTable();
//                    tempMapper.create();
//                    tempMapper.dropBrandTable();
//                    tempMapper.createBrandTable();
//                    tempMapper.insertBrand();
//                    tempMapper.dropTempTable();
//                    section.put("CurrentVersionNO", currentVersionNO);
//                    ini.store();
//                    break;
            }
        }

    }

    /**
     * 获取Model包下所有需要创建的表的表名
     *
     * @param packageName
     * @return
     * @throws IOException
     */
    private List<String> doScanner(String packageName) throws IOException {
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = dirs.nextElement();
            File dir = new File(url.getFile());
            if (dir.listFiles() != null) { // 开发时启动程序
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        // 递归读取包
                        doScanner(packageName + "." + file.getName());
                    } else {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        if (!(className.contains("Test") || className.contains("Field"))) {//TODO 将来若真的有Model命名中包含这2个字符串，则需要重命名此Moddel
                            classNames.add(className);
                        }
                    }
                }
            } else {    // 部署到客户机器上启动程序
                String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                JarFile jf = new JarFile(new File(path));
                Enumeration<JarEntry> entries = jf.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String innerPath = jarEntry.getName();
                    if (!jarEntry.isDirectory() && innerPath.startsWith("wpos/model/")) {
                        innerPath = innerPath.replace("/", ".");
                        innerPath = innerPath.replace(".class", "");
                        if (!(innerPath.contains("Test") || innerPath.contains("Field"))) { //TODO 将来若真的有Model命名中包含这2个字符串，则需要重命名此Moddel
                            classNames.add(innerPath);
                        }
                    }
                }
            }
        }


        return classNames;
    }

    private Map<String, BaseModel> doInstance() {
        Map<String, BaseModel> map = new HashMap<String, BaseModel>(); // key=表名 value=实体类

        if (classNames.isEmpty()) {
            return null;
        }
        for (String className : classNames) {
            try {
                //把类搞出来,反射来实例化(只有加@Table需要实例化)
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Table.class)) {
                    Table annotation = clazz.getAnnotation(Table.class);
                    if (annotation.name().equals(("T_" + clazz.getSimpleName()))) {
                        map.put(annotation.name(), (BaseModel) clazz.newInstance());
                    } else {
                        logger.error(clazz.getSimpleName() + "类@Table注解中的表名未按照规范编写，需要修改为：T_" + clazz.getSimpleName());
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return map;
    }
}
