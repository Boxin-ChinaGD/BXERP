package com.sunmi.printerhelper.utils;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.Toast;

import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class AidlUtil {
    private static final String SERVICE_PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE_ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }


    /**
     * 连接服务
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE_PACKAGE);
        intent.setAction(SERVICE_ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /*
    断开服务
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback) {
        return new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {

            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }

            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {

            }
        };
    }

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0, 0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得打印机系统信息，放在list中
     */
    public List<String> getPrinterInfo() {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return null;
        }
        List<String> info = new ArrayList<>();
        try {
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(woyouService.getPrintedLength() + "");
            info.add("");
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE_PACKAGE, 0);
                if (packageInfo != null) {
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode + "");
                } else {
                    info.add("");
                    info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 初始化打印机
     */
    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(6, null);//走纸6行
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印条形码
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(6, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字，，，，设置打印内容，字体大小，是否加粗，居中（靠左靠右），下划线
     */
    public void printText(String content, float size, boolean isBold, int gravity, boolean isUnderLine) {//Gravity.LEFT==51,,Gravity.CENTER==17,,Gravity.RIGHT==53
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }
            if (gravity == 51) {
                woyouService.setAlignment(0, null);
            } else if (gravity == 17) {
                woyouService.setAlignment(1, null);
            } else if (gravity == 53) {
                woyouService.setAlignment(2, null);
            }
            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }
            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(1, null);
//            woyouService.cutPaper(null);//切纸动作,如果打印机没有切刀，不会执行该动作
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片
     */
    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(1, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片和文字按照指定排列顺序
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (woyouService == null) {
            Toast.makeText(context, "服务已断开", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (orientation == 0) {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
            } else {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("纵向排列", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("纵向排列", null);
            }
            woyouService.lineWrap(6, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            Toast.makeText(context, "The service has been disconnected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //获取当前的打印模式
    public int getPrintMode() {
        if (woyouService == null) {
            Toast.makeText(context, "服务已断开", Toast.LENGTH_SHORT).show();
            return -1;
        }
        int res;
        try {
            res = woyouService.getPrinterMode();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }

    //打印分割线
    public void printDivider(String delimiterToRepeat) {
        float fontSize = 20;
        int fontCenter = 17;
        delimiterToRepeat = delimiterToRepeat.equals("") ? " " : delimiterToRepeat;
        int delimiterToRepeatLength = GeneralUtil.string_length(delimiterToRepeat); // 打印时，中文是2字节，英文是1字节
        int aLineBytes; // 一行的字节

        if (getPrinterInfo().get(1).contains("T1mini") || getPrinterInfo().get(1).contains("T2mini")) {
            aLineBytes = 770;
        } else if (getPrinterInfo().get(1).contains("T1") || getPrinterInfo().get(1).contains("T2")) {
            //这是T2机用中文测出来的，15（fontSize）*38（个）*2=1140，不能大于1150(可等于)，中文占2个字节。
            aLineBytes = 1150;
        } else {
            aLineBytes = 0; // ... 暂时不知道还有其他什么型号
        }
        // 字符串拼接
        String printText = "";
        for (int j = 1; j <= aLineBytes / (delimiterToRepeatLength * fontSize); j++) {
            printText = printText.concat(delimiterToRepeat);
        }
        printText(printText, fontSize, true, fontCenter, false);
//            woyouService.lineWrap(1, null); // 太浪费纸了
    }

    //切刀
    public void cutPaper() throws RemoteException {
        woyouService.cutPaper(null);//切纸动作,如果打印机没有切刀，不会执行该动作
    }

    //走纸
    public void linewrap(int num) {
        try {
            woyouService.lineWrap(num, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printTable(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign) {
        try {
            woyouService.printColumnsString(colsTextArr, colsWidthArr, colsAlign, null);
//            linewrap(1); // 太浪费纸了
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //SmallTicketFrame对象用于存放打印内容和打印格式
    //把对象打印出来，只用于打印，，，不可设置打印内容与打印格式
//    public void printModel(SmallSheetFrame smallSheetFrame) {
//        boolean headerBold = false;
//        boolean documentNumberBold = false;
//        boolean dateBold = false;
//        boolean totalMoneyBold = false;
//        boolean paymentMethodBold = false;
//        boolean paymentMethod1Bold = false;
//        boolean paymentMethod2Bold = false;
//        boolean paymentMethod3Bold = false;
//        boolean footer1Bold = false;
//        boolean footer2Bold = false;
//        boolean footer3Bold = false;
//        boolean footer4Bold = false;
//        boolean footer5Bold = false;
//        boolean footer6Bold = false;
//        boolean footer7Bold = false;
//        boolean footer8Bold = false;
//        boolean footer9Bold = false;
//        boolean footer10Bold = false;
//        boolean bottomBold = false;
//
//        int goodsLength = smallSheetFrame.getListCommodity().size();
//        //
//        String headerContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(0)).getContent();
//        float headerSize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(0)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(0)).getBold() == 1) {
//            headerBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(0)).getBold() == 0) {
//            headerBold = false;
//        }
//        int headerGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(0)).getGravity();
//        //
//        String documentNumberContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(1)).getContent();
//        float documentNumberSize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(1)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(1)).getBold() == 1) {
//            documentNumberBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(1)).getBold() == 0) {
//            documentNumberBold = false;
//        }
//        int documnetNumberGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(1)).getGravity();
//        //
//        String dateContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(2)).getContent();
//        float dateSize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(2)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(2)).getBold() == 1) {
//            dateBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(2)).getBold() == 0) {
//            dateBold = false;
//        }
//        int dateGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(2)).getGravity();
//        //
//        String totalMoneyContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(3)).getContent();
//        float totalMoneySize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(3)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(3)).getBold() == 1) {
//            totalMoneyBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(3)).getBold() == 0) {
//            totalMoneyBold = false;
//        }
//        int totalMoneyGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(3)).getGravity();
//        //
//        String paymentMethodContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(4)).getContent();
//        float paymentMethodSize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(4)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(4)).getBold() == 1) {
//            paymentMethodBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(4)).getBold() == 0) {
//            paymentMethodBold = false;
//        }
//        int paymentMethodGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(4)).getGravity();
//        //
//        String paymentMethod1Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(5)).getContent();
//        float paymentMethod1Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(5)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(5)).getBold() == 1) {
//            paymentMethod1Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(5)).getBold() == 0) {
//            paymentMethod1Bold = false;
//        }
//        int paymentMethod1Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(5)).getGravity();
//        //
//        String paymentMethod2Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(6)).getContent();
//        float paymentMethod2Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(6)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(6)).getBold() == 1) {
//            paymentMethod2Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(6)).getBold() == 0) {
//            paymentMethod2Bold = false;
//        }
//        int paymentMethod2Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(6)).getGravity();
//        //
//        String paymentMethod3Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(7)).getContent();
//        float paymentMethod3Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(7)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(7)).getBold() == 1) {
//            paymentMethod3Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(7)).getBold() == 0) {
//            paymentMethod3Bold = false;
//        }
//        int paymentMethod3Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(7)).getGravity();
//        //
//        String footer1Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(8)).getContent();
//        float footer1Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(8)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(8)).getBold() == 1) {
//            footer1Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(8)).getBold() == 0) {
//            footer1Bold = false;
//        }
//        int footer1Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(8)).getGravity();
//        //
//        String footer2Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(9)).getContent();
//        float footer2Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(9)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(9)).getBold() == 1) {
//            footer2Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(9)).getBold() == 0) {
//            footer2Bold = false;
//        }
//        int footer2Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(9)).getGravity();
//        //
//        String footer3Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(10)).getContent();
//        float footer3Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(10)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(10)).getBold() == 1) {
//            footer3Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(10)).getBold() == 0) {
//            footer3Bold = false;
//        }
//        int footer3Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(10)).getGravity();
//        //
//        String footer4Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(11)).getContent();
//        float footer4Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(11)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(11)).getBold() == 1) {
//            footer4Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(11)).getBold() == 0) {
//            footer4Bold = false;
//        }
//        int footer4Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(11)).getGravity();
//        //
//        String footer5Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(12)).getContent();
//        float footer5Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(12)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(12)).getBold() == 1) {
//            footer5Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(12)).getBold() == 0) {
//            footer5Bold = false;
//        }
//        int footer5Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(12)).getGravity();
//        //
//        String footer6Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(13)).getContent();
//        float footer6Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(13)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(13)).getBold() == 1) {
//            footer6Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(13)).getBold() == 0) {
//            footer6Bold = false;
//        }
//        int footer6Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(13)).getGravity();
//        //
//        String footer7Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(14)).getContent();
//        float footer7Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(14)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(14)).getBold() == 1) {
//            footer7Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(14)).getBold() == 0) {
//            footer7Bold = false;
//        }
//        int footer7Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(14)).getGravity();
//        //
//        String footer8Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(15)).getContent();
//        float footer8Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(15)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(15)).getBold() == 1) {
//            footer8Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(15)).getBold() == 0) {
//            footer8Bold = false;
//        }
//        int footer8Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(15)).getGravity();
//        //
//        String footer9Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(16)).getContent();
//        float footer9Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(16)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(16)).getBold() == 1) {
//            footer9Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(16)).getBold() == 0) {
//            footer9Bold = false;
//        }
//        int footer9Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(16)).getGravity();
//        //
//        String footer10Content = ((SmallSheetText) smallSheetFrame.getListSlave1().get(17)).getContent();
//        float footer10Size = ((SmallSheetText) smallSheetFrame.getListSlave1().get(17)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(17)).getBold() == 1) {
//            footer10Bold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(17)).getBold() == 0) {
//            footer10Bold = false;
//        }
//        int footer10Gravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(17)).getGravity();
//
//        String bottomContent = ((SmallSheetText) smallSheetFrame.getListSlave1().get(18)).getContent();
//        float bottomSize = ((SmallSheetText) smallSheetFrame.getListSlave1().get(18)).getSize();
//        if (((SmallSheetText) smallSheetFrame.getListSlave1().get(18)).getBold() == 1) {
//            bottomBold = true;
//        } else if (((SmallSheetText) smallSheetFrame.getListSlave1().get(18)).getBold() == 0) {
//            bottomBold = false;
//        }
//        int bottomGravity = ((SmallSheetText) smallSheetFrame.getListSlave1().get(18)).getGravity();
//
//
//        if (smallSheetFrame.getLogo() != null || !"".equals(smallSheetFrame.getLogo())) {
//            Bitmap bm = stringToBitmap(smallSheetFrame.getLogo());
//            printBitmap(bm);
//        }
//        printText(headerContent, headerSize, headerBold, headerGravity, false);
//        printDivider("-");
//        printText(documentNumberContent, documentNumberSize, documentNumberBold, documnetNumberGravity, false);
//        printText(dateContent, dateSize, dateBold, dateGravity, false);
//        printDivider("-");
//
//        int[] colsWidthArrc = new int[]{2, 1, 1};//每一列所占权重
//        int[] colsAlign = new int[]{0, 1, 2};//每一列对齐方式
//        String[] goodsAttr = new String[]{"商品名称", "数量", "小计"};
//        printTable(goodsAttr, colsWidthArrc, colsAlign);
//        for (int i = 0; i < goodsLength; i++) {
//            printTable(smallSheetFrame.getListCommodity().get(i), colsWidthArrc, colsAlign);
//        }
//
//        printDivider("-");
//        printText(totalMoneyContent, totalMoneySize, totalMoneyBold, totalMoneyGravity, false);
//        printText(paymentMethodContent, paymentMethodSize, paymentMethodBold, paymentMethodGravity, false);
//        printText(paymentMethod1Content, paymentMethod1Size, paymentMethod1Bold, paymentMethod1Gravity, false);
//        printText(paymentMethod2Content, paymentMethod2Size, paymentMethod2Bold, paymentMethod2Gravity, false);
//        printText(paymentMethod3Content, paymentMethod3Size, paymentMethod3Bold, paymentMethod3Gravity, false);
//        printDivider("-");
//        printText(footer1Content, footer1Size, footer1Bold, footer1Gravity, false);
//        printText(footer2Content, footer2Size, footer2Bold, footer2Gravity, false);
//        printText(footer3Content, footer3Size, footer3Bold, footer3Gravity, false);
//        printText(footer4Content, footer4Size, footer4Bold, footer4Gravity, false);
//        printText(footer5Content, footer4Size, footer5Bold, footer5Gravity, false);
//        printText(footer6Content, footer6Size, footer6Bold, footer6Gravity, false);
//        printText(footer7Content, footer7Size, footer7Bold, footer7Gravity, false);
//        printText(footer8Content, footer8Size, footer8Bold, footer8Gravity, false);
//        printText(footer9Content, footer9Size, footer9Bold, footer9Gravity, false);
//        printText(footer10Content, footer10Size, footer10Bold, footer10Gravity, false);
//        linewrap(2);
//        printText(bottomContent, bottomSize, bottomBold, bottomGravity, false);
//        linewrap(6);
//    }

    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
