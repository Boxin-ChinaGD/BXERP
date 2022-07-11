package wpos.publiC;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class Shared {
    public static void printTestClassStartInfo() {
//        StackTraceElement ste = new Exception().getStackTrace()[1];
//        System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试类：" + ste.getClassName() + "...");
    }

    public static void printTestClassEndInfo() {
//        StackTraceElement ste = new Exception().getStackTrace()[1];
//        System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t结束运行测试类：" + ste.getClassName() + "...");
    }

    public static void printTestMethodStartInfo() {
//        StackTraceElement ste = new Exception().getStackTrace()[1];
//        System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试：" + ste.getClassName() + "." + ste.getMethodName() + "()...");
    }

    public static void printTestMethodStartInfo(String testCaseID, AtomicInteger order, String testCaseName) {
//        StackTraceElement ste = new Exception().getStackTrace()[1];
//        System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试：" + ste.getClassName() + "." + ste.getMethodName() + "()..." + "\r\n用例ID：" + testCaseID + order.incrementAndGet()
//                + "\r\n用例名称：" + testCaseName + "\r\n");
    }
}
