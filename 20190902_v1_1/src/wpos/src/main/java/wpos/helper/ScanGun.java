package wpos.helper;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 字母键盘上的数字键：DIGIT0-9
 * 数字键盘上的数字键：NUMPAD0-9
 */
public class ScanGun {
    /**
     * 默认按键之间时间间隔
     */
    public final static int MAX_KEYS_INTERVAL_DEFAULT = 200;

    private long currentTime = 0;
    private boolean isKeySHIFT = false;
    private StringBuilder stringBuilder = new StringBuilder();
    private ScanGunCallBack callBack = null;

    private static int maxKeysInterval = MAX_KEYS_INTERVAL_DEFAULT;

    /**
     * 设置按键事件的最大时间间隔（部分扫描枪稍大，建议范围20--100）
     *
     * @param interval 时间间隔
     */
    public static void setMaxKeysInterval(int interval) {
        maxKeysInterval = interval;
    }

    public ScanGun(ScanGunCallBack callBack) {
        this.callBack = callBack;
    }

    public ScanGun() {
    }

    public boolean isMaybeScanning(KeyEvent event) {
        System.out.println("isMaybeScanning--");
        if (currentTime == 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder = stringBuilder.delete(0, stringBuilder.length());
            }
            currentTime = System.currentTimeMillis();
        } else {
            if ((System.currentTimeMillis() - currentTime) > maxKeysInterval) {
                if (stringBuilder.length() > 0) {
                    stringBuilder = stringBuilder.delete(0,
                            stringBuilder.length());
                }
            }
            currentTime = System.currentTimeMillis();
        }

        if (event.isShiftDown()) {  //当前为按下了shift
            if (event.getCode().isLetterKey()) {
                //如果当前点击的是字母（大写字母）
                stringBuilder.append(event.getCode().toString());
            } else if (event.getCode().isDigitKey()) {
                //当前点击的是符号（字母键盘上数字的符号）
                if (event.getCode() == KeyCode.DIGIT0) {
                    stringBuilder.append(")");
                } else if (event.getCode() == KeyCode.DIGIT1) {
                    stringBuilder.append("!");
                } else if (event.getCode() == KeyCode.DIGIT2) {
                    stringBuilder.append("@");
                } else if (event.getCode() == KeyCode.DIGIT3) {
                    stringBuilder.append("#");
                } else if (event.getCode() == KeyCode.DIGIT4) {
                    stringBuilder.append("$");
                } else if (event.getCode() == KeyCode.DIGIT5) {
                    stringBuilder.append("%");
                } else if (event.getCode() == KeyCode.DIGIT6) {
                    stringBuilder.append("^");
                } else if (event.getCode() == KeyCode.DIGIT7) {
                    stringBuilder.append("&");
                } else if (event.getCode() == KeyCode.DIGIT8) {
                    stringBuilder.append("*");
                } else if (event.getCode() == KeyCode.DIGIT9) {
                    stringBuilder.append("(");
                }
            } else if (event.getCode().isKeypadKey()) {
                //如果是数字键盘键
                stringBuilder.append(event.getText());
            } else {
                System.out.println("未被收录的符号");
            }
        } else {    //当前为未按下shift
            if (event.getCode().isDigitKey() || event.getCode().isKeypadKey() || event.getCode().isLetterKey()) {
                //当前点击的是字母键盘上的数字 || 当前点击的是数字键盘上的数字 || 当前点击的是字母（小写）
                stringBuilder.append(event.getText());
            } else if (event.getCode().isWhitespaceKey()) {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("进入了回传数据");
                    callBack.onScanFinish(stringBuilder.toString());
                } else if (event.getCode() == KeyCode.SPACE) {
                    stringBuilder.append(" ");
                }
            } else {
                System.out.println("未被收录的符号");
            }
        }

        return true;
    }

    public interface ScanGunCallBack {
        public void onScanFinish(String data);
    }
}
