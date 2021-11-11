package wpos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetworkUtils {
    private static final long TIME_OUT = 10;
    private static Runtime runtime = Runtime.getRuntime();// 获取当前程序的运行进对象
    private static Process process = null; //声明处理类对象
    private static String line = null;//返回行信息
    private static InputStream is = null;
    private static InputStreamReader isr = null;
    private static BufferedReader br = null;
    private static final String ip = "www.baidu.com";

    //判断网络是否连接
    public static boolean isNetworkAvalible() {
        line = null;
        try {
            process = runtime.exec("ping " + ip);
            is = process.getInputStream(); // 实例化输入流
            isr = new InputStreamReader(is);// 把输入流转换成字节流
            br = new BufferedReader(isr);// 从字节中读取文本

            while ((line = br.readLine()) != null) {
                if (line.contains("TTL")) {
                    return true;
                }
            }
            is.close();
            isr.close();
            br.close();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
//
//    public NetworkUtils() {
//
//    }
//
//    public static int getConnectivityStatus(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        if (null != activeNetwork) {
//            if(ConnectivityManager.TYPE_WIFI == activeNetwork.getType()) {
//                return TYPE_WIFI;
//            } else if(ConnectivityManager.TYPE_MOBILE == activeNetwork.getType()) {
//                return TYPE_MOBILE;
//            }
//        }
//
//        return TYPE_NOT_CONNECTED;
//    }
//
//    public static String getConnectivityStatusString(Context context) {
//        int conn = NetworkUtils.getConnectivityStatus(context);
//        String status = null;
//        if (conn == NetworkUtils.TYPE_WIFI) {
//            status = "Wifi enabled";
//        } else if (conn == NetworkUtils.TYPE_MOBILE) {
//            status = "Mobile data enabled";
//        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
//            status = "Not connected to Internet";
//        }
//        return status;
//    }
//
}
