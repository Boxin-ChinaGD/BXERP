package com.bx.erp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class GeneralUtil {
    protected static DecimalFormat formatToCalculate = new DecimalFormat("0.000000");
    protected static DecimalFormat formatToShow = new DecimalFormat("0.00");

    /**
     * 格式化为六位小数，不足补零
     */
    public static String formatToCalculate(Double d1) {
        formatToCalculate.setRoundingMode(RoundingMode.HALF_UP);
        return formatToCalculate.format(round(d1, 6));
    }

    /**
     * 格式化为两位小数，不足补零
     */
    public static String formatToShow(Double d1) {
        formatToShow.setRoundingMode(RoundingMode.HALF_UP);
        return formatToShow.format(round(d1, 2));
    }

    /**
     * @param o1
     * @return 精度计算后的数据.
     */
    public static double round(Object o1, int precision) {
        BigDecimal bd = new BigDecimal(o1.toString());
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * 多个double 相加
     */
    public static double sumN(double... param) {
        double sumN = param[0];
        for (int i = 1; i < param.length; i++) {
            sumN = sum(sumN, param[i]);
        }
        return sumN;
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }


    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide
                (bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                        bitmapArray.length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将Bitmap转换成字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            string = Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return string;
    }

    public static int string_length(String value) {
        int valueLength = 0;
        if (value.getBytes().length == 1) {
            valueLength = 1;
        } else {
            valueLength = 2;
        }
        return valueLength;
    }

    /** 检查ia中有无重复元素 */
    public static boolean hasDuplicatedElement(Integer[] ia) {
        if (ia == null) {
            return true; // null算出错，所以返回true
        }

        HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
        for (Integer i : ia) {
            if (hm.containsKey(i)) {
                return true;
            }
            hm.put(i, i);
        }

        return false;
    }
}
