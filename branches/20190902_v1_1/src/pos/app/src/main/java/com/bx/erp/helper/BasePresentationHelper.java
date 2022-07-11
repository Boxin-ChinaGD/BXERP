package com.bx.erp.helper;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;

import com.bx.erp.view.presentation.BasePresentation;

public class BasePresentationHelper {
    private static final String TAG = BasePresentationHelper.class.getName();

    private SparseArray<BasePresentation> sparseArray;
    private static volatile BasePresentationHelper helper;

    private int key = -1;
    private int lastKey = -1;
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (sparseArray == null || sparseArray.size() == 0) {
                return;
            }
            if (sparseArray.get(lastKey) == null) {
                lastKey = msg.what;
                return;
            }
            if (lastKey == msg.what) {
                return;
            }
            if (lastKey == 0 && msg.what > 0) {
                sparseArray.get(0).hide();
            }
            if (lastKey > 0) {
                sparseArray.get(lastKey).hide();
            }
            lastKey = msg.what;
        }
    };

    public static BasePresentationHelper getInstance() {
        BasePresentationHelper basePresentationHelper = helper;
        if (helper == null) {
            synchronized (BasePresentationHelper.class) {
                basePresentationHelper = helper;
                if (helper == null) {
                    basePresentationHelper = new BasePresentationHelper();
                    helper = basePresentationHelper;
                }
            }
        }
        return basePresentationHelper;
    }

    private BasePresentationHelper() {
        sparseArray = new SparseArray<>();
    }

    public int add(BasePresentation basePresentation) {
        key++;
        sparseArray.put(key, basePresentation);
        return key;
    }

    public void hide(BasePresentation basePresentation) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void show(int i) {
        for (int j = 0; j < sparseArray.size(); j++) {
            int key = sparseArray.keyAt(j);
            if (key != i && sparseArray.get(key) != null) {
                sparseArray.get(key).onSelect(false);
            }
        }
        sparseArray.get(i).onSelect(true);
        handler.sendEmptyMessageDelayed(i, 200);
    }

    public void dismissAll() {
        if (sparseArray == null || sparseArray.size() == 0) {
            return;
        }
        for (int i = 0; i < sparseArray.size(); i++) {
            int key = sparseArray.keyAt(i);
            if (sparseArray.get(key) != null) {
                sparseArray.get(key).dismiss();
            }
        }
        sparseArray.clear();
        handler.removeCallbacksAndMessages(null);
    }

    public void dismiss() {
        int key = sparseArray.keyAt(lastKey);
        if (sparseArray.get(key) != null) {
            sparseArray.get(key).dismiss();
            sparseArray.remove(key);
        }
    }

    public void onStop() {
        if (sparseArray == null || sparseArray.size() == 0) {
            return;
        }
        for (int i = 0; i < sparseArray.size(); i++) {
            int key = sparseArray.keyAt(i);
            if (sparseArray.get(key) != null && sparseArray.get(key).isShow) {
                sparseArray.get(key).hide();
            }
        }
    }
}
