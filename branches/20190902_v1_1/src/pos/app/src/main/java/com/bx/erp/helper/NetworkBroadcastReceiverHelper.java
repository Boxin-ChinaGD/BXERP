package com.bx.erp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.utils.LocalBroadcastUtils;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 网络状态广播接受者帮助类
 */
public class NetworkBroadcastReceiverHelper {
    private Logger log = Logger.getLogger(this.getClass());
    private Context mContext;
    private BroadcastReceiver mReceiver;
    private OnNetworkStateChangedListener mOnNetworkStateChangedListener;
    public static final String ACTION_NO_CONNECTION = "ACTION_NO_CONNECTION"; //网络没有连接
    public static final String ACTION_CONNECTIONED = "ACTION_CONNECTIONED";   //网络连接

    public NetworkBroadcastReceiverHelper(Context context, OnNetworkStateChangedListener listener) {
        this.mContext = context;
        this.mOnNetworkStateChangedListener = listener;
    }

    public void register() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(ACTION_CONNECTIONED, intent.getAction())) {
                    if (mOnNetworkStateChangedListener != null) {
                        try {
                            mOnNetworkStateChangedListener.onConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("连接网络成功遇到的异常：" + e.getMessage());
                        }
                    }
                } else if (TextUtils.equals(ACTION_NO_CONNECTION, intent.getAction())) {
                    if (mOnNetworkStateChangedListener != null) {
                        try {
                            mOnNetworkStateChangedListener.onDisConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("网络未连接遇到的异常：" + e.getMessage());
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECTIONED);
        filter.addAction(ACTION_NO_CONNECTION);
        LocalBroadcastUtils.register(mContext, mReceiver, filter);
    }

    public void setOnNetworkStateChangedListener(OnNetworkStateChangedListener onNetworkStateChangedListener) {
        this.mOnNetworkStateChangedListener = onNetworkStateChangedListener;
    }

    public OnNetworkStateChangedListener getOnNetworkStateChangedListener() {
        return this.mOnNetworkStateChangedListener;
    }

    public void unregister() {
        LocalBroadcastUtils.unregister(mContext, mReceiver);
    }

    public interface OnNetworkStateChangedListener {
        void onConnected();

        void onDisConnected();
    }
}
