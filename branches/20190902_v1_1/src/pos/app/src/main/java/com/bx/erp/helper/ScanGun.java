package com.bx.erp.helper;

import android.view.KeyEvent;

import org.apache.log4j.Logger;

public class ScanGun {
    private Logger log = Logger.getLogger(this.getClass());
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

    public ScanGun() {}

    public boolean isMaybeScanning(int keyCode, KeyEvent event) {
        log.info("isMaybeScanning--");

        log.info("event.getFlags:" + event.getFlags());
        if (event.getFlags() != 0x8 && event.getFlags() != 0x6) {
            return false;
        }
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
        // Shift
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                isKeySHIFT = true;
            } else {
                //松开shift键，表示小写
                isKeySHIFT = false;
            }
        }
        // Enter
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            isKeySHIFT = false;
            currentTime = 0;
            if (callBack != null) {
                log.info("stringBuilder:" + stringBuilder);
                callBack.onScanFinish(stringBuilder.toString());
            }
            return true;
        }
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            // 数字键（键盘上方横条数字，需要考虑Shift）
            handleTopNumKeys(keyCode);

        } else if (keyCode >= 29 && keyCode <= 54) {
            // 字母键（需要考虑Shift）ww
            checkShift((char) (keyCode + 68), (char) (keyCode + 36));
        } else if (keyCode >= 144 && keyCode <= 158) {
            // 数字键盘区
            handleNumPadKeys(keyCode);
            log.info("数字：" + stringBuilder);
        } else {
            // 键盘主键区其他双字符键位
            switch (keyCode) {
                case KeyEvent.KEYCODE_GRAVE: {
                    checkShift(ASCII.CHAR_SIGN_BACKTICK, ASCII.CHAR_SIGN_TILDE);
                    break;
                }
                case KeyEvent.KEYCODE_MINUS: {
                    checkShift(ASCII.CHAR_SIGN_MINUS, ASCII.CHAR_SIGN_UNDERSCORE);
                    break;
                }
                case KeyEvent.KEYCODE_EQUALS: {
                    checkShift(ASCII.CHAR_SIGN_EQUALS, ASCII.CHAR_SIGN_PLUS);
                    break;
                }

                case KeyEvent.KEYCODE_LEFT_BRACKET: {
                    checkShift(ASCII.CHAR_SIGN_BRACKET_LEFT,
                            ASCII.CHAR_SIGN_BRACE_LEFT);
                    break;
                }
                case KeyEvent.KEYCODE_RIGHT_BRACKET: {
                    checkShift(ASCII.CHAR_SIGN_BRACKET_RIGHT,
                            ASCII.CHAR_SIGN_BRACE_RIGHT);
                    break;
                }
                case KeyEvent.KEYCODE_BACKSLASH: {
                    checkShift(ASCII.CHAR_SIGN_BACKSLASH, ASCII.CHAR_SIGN_BAR);
                    break;
                }
                case KeyEvent.KEYCODE_SEMICOLON: {
                    checkShift(ASCII.CHAR_SIGN_SEMICOLON, ASCII.CHAR_SIGN_COLON);
                    break;
                }
                case KeyEvent.KEYCODE_APOSTROPHE: {
                    checkShift(ASCII.CHAR_SIGN_QUOTE, ASCII.CHAR_SIGN_DOUBLE_QUOTE);
                    break;
                }
                case KeyEvent.KEYCODE_COMMA: {
                    checkShift(ASCII.CHAR_SIGN_COMMA, ASCII.CHAR_SIGN_LESS);
                    break;
                }
                case KeyEvent.KEYCODE_PERIOD: {
                    checkShift(ASCII.CHAR_SIGN_PERIOD, ASCII.CHAR_SIGN_GREATER);
                    break;
                }
                case KeyEvent.KEYCODE_SLASH: {
                    checkShift(ASCII.CHAR_SIGN_SLASH, ASCII.CHAR_SIGN_QUESTION);
                    break;
                }
                // 其他单字符键位
                case KeyEvent.KEYCODE_SPACE: {
                    stringBuilder.append(ASCII.CHAR_SIGN_SPACE);
                    log.info("Other StringBuilder:" + stringBuilder);
                    break;
                }
                default: {
                    return false;
                }
            }
            return true;
        }

        return true;

    }

    /**
     * 判断是否同时按下Shift键
     *
     * @param ascallNoShift
     * @param ascallOnShift
     */
    private void checkShift(char ascallNoShift, char ascallOnShift) {
        if (isKeySHIFT) {
            stringBuilder.append(ascallOnShift);
            isKeySHIFT = false;
        } else {
            stringBuilder.append(ascallNoShift);
        }
    }

    /**
     * 数字键盘区按键
     *
     * @param keyCode
     */
    public void handleNumPadKeys(int keyCode) {
        if (keyCode <= 153) {
            stringBuilder.append((char) (keyCode - 96));
        } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_DIVIDE) {
            stringBuilder.append(ASCII.CHAR_SIGN_SLASH);
        } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_MULTIPLY) {
            stringBuilder.append(ASCII.CHAR_SIGN_STAR);
        } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_SUBTRACT) {
            stringBuilder.append(ASCII.CHAR_SIGN_MINUS);
        } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_ADD) {
            stringBuilder.append(ASCII.CHAR_SIGN_PLUS);
        } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_DOT) {
            stringBuilder.append(ASCII.CHAR_SIGN_PERIOD);
        }
    }

    /**
     * 键盘上方数字键
     *
     * @param keyCode
     */
    private void handleTopNumKeys(int keyCode) {
        if (keyCode < 7 || keyCode > 16) {
            return;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                checkShift(ASCII.CHAR_NUM_0, ASCII.CHAR_SIGN_PAREN_RIGHT);
                break;
            case KeyEvent.KEYCODE_1:
                checkShift(ASCII.CHAR_NUM_1, ASCII.CHAR_SIGN_EXCLAM);
                break;
            case KeyEvent.KEYCODE_2:
                checkShift(ASCII.CHAR_NUM_2, ASCII.CHAR_SIGN_AT);
                break;
            case KeyEvent.KEYCODE_3:
                checkShift(ASCII.CHAR_NUM_3, ASCII.CHAR_SIGN_HASH);
                break;
            case KeyEvent.KEYCODE_4:
                checkShift(ASCII.CHAR_NUM_4, ASCII.CHAR_SIGN_DOLLAR);
                break;
            case KeyEvent.KEYCODE_5:
                checkShift(ASCII.CHAR_NUM_5, ASCII.CHAR_SIGN_PERCENT);
                break;
            case KeyEvent.KEYCODE_6:
                checkShift(ASCII.CHAR_NUM_6, ASCII.CHAR_SIGN_CARET);
                break;
            case KeyEvent.KEYCODE_7:
                checkShift(ASCII.CHAR_NUM_7, ASCII.CHAR_SIGN_AMPERSAND);
                break;
            case KeyEvent.KEYCODE_8:
                checkShift(ASCII.CHAR_NUM_8, ASCII.CHAR_SIGN_STAR);
                break;
            case KeyEvent.KEYCODE_9:
                checkShift(ASCII.CHAR_NUM_9, ASCII.CHAR_SIGN_PAREN_LEFT);
                break;
            default:
                break;
        }
    }

    public interface ScanGunCallBack {
        public void onScanFinish(String data);
    }
}
