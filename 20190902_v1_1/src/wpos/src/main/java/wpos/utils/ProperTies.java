package wpos.utils;

//import android.content.Context;

import wpos.helper.Configuration;
import wpos.helper.Constants;

//import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ProperTies {
//    private Log log = LogFactory.getLog(this.getClass());

//    public Properties getProperties(Context c) {
//        Properties props = new Properties();
//        try {
//            //通过stage中的context获取setting.properties的FileInputStream。参数appConfig在eclipse中应该是appConfig.properties才对,但在Android Studio中不用写后缀
//            InputStream in = c.getAssets().open("appConfig");
//            props.load(in);
//
//            AESUtil aes = new AESUtil();
//
//            //拿到配置文件中的value
//            String env = props.getProperty(Configuration.APP_Config_Key_env, Configuration.EnumEnv.PROD.getName());
//
//            //根据env来选择连接的服务器
//            if (env.equals(Configuration.EnumEnv.DEV.getName())) {
//                Configuration.currentRunningEnv = Configuration.EnumEnv.DEV;
//                Configuration.HTTP_IP = Configuration.SERVER_IP_DEV;
//                Configuration.bUseSandBox = false;
//            } else if (env.equals(Configuration.EnumEnv.SIT.getName())) {
//                Configuration.currentRunningEnv = Configuration.EnumEnv.SIT;
//                Configuration.HTTP_IP = Configuration.SERVER_IP_SIT;
//                Configuration.bUseSandBox = false;
//            } else if (env.equals(Configuration.EnumEnv.UAT.getName())) {
//                Configuration.currentRunningEnv = Configuration.EnumEnv.UAT;
//                Configuration.HTTP_IP = Configuration.SERVER_IP_UAT;
//                Configuration.bUseSandBox = false;
//            } else if (env.equals(Configuration.EnumEnv.PROD.getName())) {
//                Configuration.currentRunningEnv = Configuration.EnumEnv.PROD;
//                Configuration.HTTP_IP = Configuration.SERVER_IP_PRODUCTION;
//                Configuration.bUseSandBox = false;
//            } else {
//                Configuration.currentRunningEnv = Configuration.EnumEnv.DEV;
//                //...连接的是本机
//                Configuration.bUseSandBox = true;
//                log.info("-------------配置文件中的env的value不符合要求-------------");
//            }
//
//            log.info("------------------------：" + Configuration.HTTP_IP + "--" + Configuration.bUseSandBox);
//        } catch (Exception e) {
//            log.error("加载配置文件错误：" + e.getMessage());
//            e.printStackTrace();
//        }
//        return props;
//    }
}
