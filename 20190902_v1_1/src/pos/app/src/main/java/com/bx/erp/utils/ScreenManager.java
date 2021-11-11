package com.bx.erp.utils;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;

/**
 * Created by WPNA on 2019/3/22.
 */

public class ScreenManager {
    private final String TAG = ScreenManager.class.getName();
    public static ScreenManager manager = null;
    private Display displays[] = null;
    boolean isMinScreen;

    private ScreenManager() {

    }

    public static ScreenManager getInstance() {
        if (null == manager) {
            synchronized (ScreenManager.class) {
                if (null == manager) {
                    manager = new ScreenManager();
                }
            }
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void init(Context context) {
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (displays.length > 1) {
            Rect outSize0 = new Rect();
            displays[0].getRectSize(outSize0);

            Rect outSize1 = new Rect();
            displays[1].getRectSize(outSize1);

            if (outSize0.right - outSize1.right > 100) {
                isMinScreen = true;
            }
        }
    }

    public boolean isMinScreen() {
        return isMinScreen;
    }

    public Display[] getDisplays() {
        return displays;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Display getPresentationDisplays() {
        for (int i = 0; i < displays.length; i++) {
            if ((displays[i].getFlags() & Display.FLAG_SECURE) != 0
                    && (displays[i].getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (displays[i].getFlags() & Display.FLAG_PRESENTATION) != 0) {
                Log.e(TAG, "第一个真实存在的副屏屏幕" + displays[i]);
                return displays[i];
            }
        }
        return null;
    }
}
