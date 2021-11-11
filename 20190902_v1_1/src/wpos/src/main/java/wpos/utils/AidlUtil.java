package wpos.utils;

import jpos.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 要支持打印，必须：1、安装对应的驱动程序。2、安装对应的ADK。本地开发时使用的打印机设备型号为：TM-T82II，
 * 驱动程序下载地址：https://download.epson-biz.com/modules/pos/index.php?page=single_soft&cid=6231&scat=31&pcat=3
 * ADK下载地址：https://download.epson-biz.com/modules/pos/index.php?page=soft&scat=40&start=0
 * <p>
 * 打印所需的4个jar，需要通过SetupPOS（安装ADK后可以找到）应用程序来生成，使用SVN上的jar是不能正常打印的。生成jar的步骤：
 * D:\BXERP\trunk\doc\研发刑事条例\wpos连接打印机.docx
 * <p>
 * 图片大小大于 5000 时，会使打印机出现异常
 */
public class AidlUtil {
    //打印机的名称
    private final String PRINTER = "myPosPrinter";
    //打印图片
    private final String printBitmap = "\u001b|1B";
    //默认字体
    private final String printNormalChar = "\u001b|N";
    //字体居右
    private final String printRightSideChar = "\u001b|rA";
    //字体居中
    private final String printCenterChar = "\u001b|cA";
    //字体加粗
    private final String printBold = "\u001b|bC";
    //字体带下划线
    private final String printUnderline = "\u001b|uC";
    //字体大小。不同于App的是，字体大小需要用以下3个变量之一决定，而不是在设计小票格式时，想设成多大就打印出多大
    private final String printWideChar2 = "\u001b|2C";
    private final String printWideChar3 = "\u001b|3C";
    private final String printWideChar4 = "\u001b|4C";
    //底部留出空行并且切纸
    private final String printLineFeedAndCutPage = "\u001b|fP";
    private final String printLineFeed = "\n";

    //2mm的空行,示例： ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|200uF");
    private final String print2mmSpaces = "\u001b|200uF";

    private static AidlUtil aidlUtil;
    private final POSPrinterControl114 ptr = new POSPrinter();

    private StringBuilder print = new StringBuilder();

    private AidlUtil() {
        connectPrinterService();
    }

    public static AidlUtil getInstance() {
        if (aidlUtil == null) {
            synchronized (AidlUtil.class) {
                if (aidlUtil == null) {
                    aidlUtil = new AidlUtil();
                }
            }
        }
        return aidlUtil;
    }

    private void connectPrinterService() {
        try {
            //打开设备
            //这里的PRINTER是打印机设备的设备名
            ptr.open(PRINTER);
            //获取已打开设备的独占控制权。
            //禁止其他应用程序访问该设备。
            ptr.claim(1000);
            //设置设备可用
            ptr.setDeviceEnabled(true);
            //即使使用任何打印机，0.01mm的单位也可以使打印整齐。
            ptr.setMapMode(POSPrinterConst.PTR_MM_METRIC);
            //设置异步打印
            ptr.setAsyncMode(true);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字，，，，设置打印内容，字体大小，是否加粗，居中（靠左靠右），下划线
     * gravity:-1居左，0居中，1居右
     */
    public void printText(String content, int size, boolean isBold, int gravity, boolean isUnderLine) {
        //清空stringBuilder
        print.setLength(0);
        switch (size) {
            case 2:
                print.append(printWideChar2);
                break;
            case 3:
                print.append(printWideChar3);
                break;
            case 4:
                print.append(printWideChar4);
                break;
            default:
                break;
        }

        if (isBold) {
            print.append(printBold);
        }

        switch (gravity) {
            case 17:
                print.append(printCenterChar);
                break;
            case 53:
                print.append(printRightSideChar);
                break;
            default:
                break;
        }

        if (isUnderLine) {
            print.append(printUnderline);
        }

        print.append(content);
        //换行
        print.append(printLineFeed);

        try {
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, print.toString());
            //Make 2mm speces
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, print2mmSpaces);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    //打印分割线
    public void printDivider(String delimiterToRepeat) {
        //清空stringBuilder
        print.setLength(0);

        delimiterToRepeat = delimiterToRepeat.equals("") ? " " : delimiterToRepeat;
        //打印分割线默认居中
        print.append(printCenterChar);
        int delimiterToRepeatLength = GeneralUtil.string_length(delimiterToRepeat); // 打印时，中文是2字节，英文是1字节
        System.out.println("字节长度为：" + delimiterToRepeatLength);
        //80mm的纸打满英文一整行是48，中文是24
        for (int i = 0; i < 48 / delimiterToRepeatLength; i++) {
            print.append(delimiterToRepeat);
        }
        //换行
        print.append(printLineFeed);
        try {
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, print.toString());
            //Make 2mm speces
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, print2mmSpaces);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    //切纸，结束打印
    public void cutPaper() {
        try {
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, printLineFeedAndCutPage);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    //根据图片地址打印图片
    public void printMemoryBitmap(String path) {
        try {
            byte[] data = null;
            try {
                //D:/BXERP/branches/20190902_v1_1/src/wpos/src/main/java/wpos/javapos.bmp
                //D:/BXERP/branches/20190902_v1_1/src/wpos/src/main/resources/image/viewpager1.jpeg
//                File file = new File("D:/BXERP/branches/20190902_v1_1/src/wpos/src/main/java/wpos/javapos.bmp");
//                if (file.exists()){
                FileInputStream fis = new FileInputStream(path);
                try {
                    data = new byte[fis.available()];
                    fis.read(data);
                    fis.close();
                    System.out.println("数据长度" + data.length);
                } catch (IOException ie) {
                    System.out.println("IOException!!");
                }
//                }

            } catch (FileNotFoundException fne) {
                System.out.println("没有找到文件！！");
            }
            //打印图片
            ptr.printMemoryBitmap(POSPrinterConst.PTR_S_RECEIPT, data, POSPrinterConst.PTR_BMT_BMP, 5000, POSPrinterConst.PTR_BM_CENTER);
        } catch (JposException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 断开连接，释放资源
     */
    public void disconnectPrinterService() {
        try {
            //Cancel the device.
            ptr.setDeviceEnabled(false);
            //Release the device exclusive control right.
            ptr.release();
            //Finish using the device.
            ptr.close();
        } catch (JposException ex) {

        }
    }

    String printData;
    String outLineData = "";//超出了一行的字符，单独拿出在下一行打印

    public void printTable(String[] colsTextArr) {
        try {
            printData = makePrintString(ptr.getRecLineChars(), colsTextArr[0], colsTextArr[1], colsTextArr[2]);
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, printData + printLineFeed);
            if (!outLineData.equals("")) {
                ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, outLineData + printLineFeed);
                outLineData = "";
            }
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    public String makePrintString(int lineChars, String text1, String text2, String text3) {
        String spacesBetweenColumn1AndColumn2 = "";
        String spacesBetweenColumn2AndColumn3 = "";
        int everySlotLength = lineChars / 4; // 共4列，商品名称占2列。因此打印出来是3列，分别是商品名称、数量、小计。如果以后需要调整，可以变化一下4这里
        try {
            for (int i = 0; i < everySlotLength * 2 - text1.length() * 2; i++) {
                spacesBetweenColumn1AndColumn2 += " ";
            }
            for (int i = 0; i < everySlotLength - text2.length() * 2; i++) {
                spacesBetweenColumn2AndColumn3 += " ";
            }
        } catch (Exception ex) {
        }
        return text1 + spacesBetweenColumn1AndColumn2 + text2 + spacesBetweenColumn2AndColumn3 + text3;
    }

    //打印空行
    public void linewrap(int i) {
        for (int j = 0; j < i; j++) {
            try {
                ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, printLineFeed);
            } catch (JposException e) {
                e.printStackTrace();
            }
        }

    }
}
