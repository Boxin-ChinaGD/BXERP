package wpos.utils;

//import android.util.Log;

import wpos.bo.NtpHttpBO;
import wpos.helper.Constants;

//import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//TODO 缺少相关的测试
public final class StringUtils {
    //    private static Logger log = Logger.getLogger(StringUtils.class);
    public static Random random = new Random();
    private static final String TAG = StringUtils.class.getSimpleName();

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }


    public static boolean isNotEmpty(final CharSequence s) {
        return !isEmpty(s);
    }


    public static boolean isNumber(String str) {
        return str.matches("-?[0-9]+.?[0-9]*");
    }


    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(final String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * text保留字节长度(一个汉字是2个字节)
     *
     * @param text
     * @param maxByteSize
     * @return
     */
    public static String retainCharacter(String text, int maxByteSize) {
        if (null == text) {
            return null;
        }
        int endPosition = 0;
        int length = 0;
        for (int i = 0; i < text.length() && length < maxByteSize; i++) {
            endPosition = i;
            int ascii = Character.codePointAt(text, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
//            Log.d(TAG, "retain byte i:" + i + " char:" + ascii + " length:" + length);
        }
        if (endPosition + 1 >= text.length()) {
            return text;
        }
        if (endPosition + 1 <= text.length() && length % 2 == 1) {
            endPosition++;
        }
//        Log.d(TAG, "final text:" + text.substring(0, endPosition));
        if (endPosition < text.length()) {
            return text.substring(0, endPosition) + "...";
        } else {
            return text;
        }
    }

    public static void printCurrentFunction(String key, Object value) {
        StackTraceElement ste = new Exception().getStackTrace()[1];
//        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) + "\t\t当前执行的方法：" + ste.getClassName() + "." + ste.getMethodName() + "()...");
        if (key != null) {
//            log.info(key + "=" + value);
        }
    }

    /*
     * 类map的String转Map
     */
    public static Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(", ");
        Map<String, String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = "";
            if (string.split("=").length > 1) {
                value = string.split("=")[1];
            }
            map.put(key, value);
        }
        return map;
    }

    /*
     * 将String数组转换为int
     */
    public static Integer[] toIntArray(String ids) {
        String[] sarrID = ids.split(",");
        Integer[] iArrID = new Integer[sarrID.length];
        try {
            for (int i = 0; i < sarrID.length; i++) {
                iArrID[i] = Integer.parseInt(sarrID[i]);
            }
        } catch (Exception e) {
            iArrID = null;
        }

        return iArrID;
    }

    public static int contain(String str, String c) {
        int total = 0;
        for (int i = 0; i < str.length(); i++){
            int index = str.indexOf(c, i);
            if (index == i){
                total ++;
            }
        }
        return total;
    }
}
