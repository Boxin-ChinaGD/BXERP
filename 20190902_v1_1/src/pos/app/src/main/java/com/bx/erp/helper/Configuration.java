package com.bx.erp.helper;

/**
 * 从配置文件中读取到的值保存在本类中
 */
public final class Configuration {
    public static final String APP_Config_Key_env = "env";

    public static final String SERVER_IP_DEV = "https://dev.wx.bxit.vip/";
    public static final String SERVER_IP_SIT = "https://sit.wx.bxit.vip/";
    public static final String SERVER_IP_UAT = "https://uat.wx.bxit.vip/";
    public static final String SERVER_IP_PRODUCTION = "https://account.prosalesbox.cn/";

    public static String HTTP_IP = "http://192.168.1.225/";//运行Pos_Local测试时，要确保与Pos在同一局域网；搞清楚NBR的IP是多少，并填写在这里

    public static boolean bUseSandBox = true;

    //用在读取配置文件的工具类
    public enum EnumEnv {
        DEV("DEV", 0), //
        SIT("SIT", 1), //
        UAT("UAT", 2),
        PROD("PROD", 3);

        private String name;
        private int index;

        private EnumEnv(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumEnv e : EnumEnv.values()) {
                if (e.getIndex() == index) {
                    return e.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * APP当前所运行的环境。某些时候需要在不同的测试/生产环境当中做不同的操作时，可以用本变量控制
     */
    public static EnumEnv currentRunningEnv;
}
