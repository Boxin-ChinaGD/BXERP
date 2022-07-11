package wpos.utils;

//import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WXPayUtil {
//    private static Logger log = Logger.getLogger(WXPayUtil.class);

    public static double formatAmount(double amount) {
        try {
            BigDecimal fen = new BigDecimal(100);
            BigDecimal yuan = new BigDecimal(amount);

            return fen.multiply(yuan).doubleValue();
        } catch (Exception e) {
//            log.info("金额转换失败，错误信息：" + e.getMessage());
            return 0;
        }
    }

    /**
     * 当amount非常大时，为了不让传递给WX的金额因为科学记数法出现较大误差，要使用本接口而非formatAmount(double amount)
     */
    public static String formatAmountToPayViaWX(double amount){
        BigDecimal fen = new BigDecimal(100);
        BigDecimal yuan = new BigDecimal(amount);
        //
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2); // 四舍五入到小数点两位

        return df.format(fen.multiply(yuan).doubleValue()).replaceAll(",", "");
    }
}
